/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lds56.mona.utils.reflection;

import org.apache.commons.logging.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A cache of introspection information for a specific class instance.
 * Keys objects by an aggregation of the method name and the classes
 * that make up the parameters.
 * <p>
 * Originally taken from the Velocity tree so we can be self-sufficient.
 * </p>
 *
 * @see MethodKey
 * @since 1.0
 */
final class ClassMap {
    /**
     * A method that returns itself used as a marker for cache miss,
     * allows the underlying cache map to be strongly typed.
     *
     * @return itself as a method
     */
    public static Method cacheMiss() {
        try {
            return ClassMap.class.getMethod("cacheMiss");
        } catch (final Exception xio) {
            // this really cant make an error...
            return null;
        }
    }

    /**
     * The cache miss marker method.
     */
    private static final Method CACHE_MISS = cacheMiss();
    /**
     * This is the cache to store and look up the method information.
     * <p>
     * It stores the association between:
     * - a key made of a method name and an array of argument types.
     * - a method.
     * </p>
     * <p>
     * Since the invocation of the associated method is dynamic, there is no need (nor way) to differentiate between
     * foo(int,int) and foo(Integer,Integer) since in practice only the latter form will be used through a call.
     * This of course, applies to all 8 primitive types.
     * </p>
     * Uses ConcurrentMap since 3.0, marginally faster than 2.1 under contention.
     */
    private final ConcurrentMap<MethodKey, Method> byKey = new ConcurrentHashMap<>();
    /**
     * Keep track of all methods with the same name; this is not modified after creation.
     */
    private final Map<String, Method[]> byName = new HashMap<>();
    /**
     * Cache of fields.
     */
    private final Map<String, Field> fieldCache;

    /**
     * Standard constructor.
     *
     * @param aClass      the class to deconstruct.
     * @param permissions the permissions to apply during introspection
     * @param log         the logger.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    ClassMap(final Class<?> aClass, final Permissions permissions, final Log log) {
        // eagerly cache methods
        create(this, permissions, aClass, log);
        // eagerly cache public fields
        final Field[] fields = aClass.getFields();
        if (fields.length > 0) {
            final Map<String, Field> cache = new HashMap<>();
            for (final Field field : fields) {
                if (permissions.allow(field)) {
                    cache.put(field.getName(), field);
                }
            }
            fieldCache = cache;
        } else {
            fieldCache = Collections.emptyMap();
        }
    }

    /**
     * Find a Field using its name.
     *
     * @param fname the field name
     * @return A Field object representing the field to invoke or null.
     */
    Field getField(final String fname) {
        return fieldCache.get(fname);
    }

    /**
     * Gets the field names cached by this map.
     *
     * @return the array of field names
     */
    String[] getFieldNames() {
        return fieldCache.keySet().toArray(new String[fieldCache.size()]);
    }

    /**
     * Gets the methods names cached by this map.
     *
     * @return the array of method names
     */
    String[] getMethodNames() {
        return byName.keySet().toArray(new String[byName.size()]);
    }

    /**
     * Gets all the methods with a given name from this map.
     *
     * @param methodName the seeked methods name
     * @return the array of methods (null or non-empty)
     */
    Method[] getMethods(final String methodName) {
        final Method[] lm = byName.get(methodName);
        if (lm != null && lm.length > 0) {
            return lm.clone();
        }
        return null;
    }

    /**
     * Find a Method using the method name and parameter objects.
     * <p>
     * Look in the methodMap for an entry. If found,
     * it'll either be a CACHE_MISS, in which case we
     * simply give up, or it'll be a Method, in which
     * case, we return it.
     * </p>
     * <p>
     * If nothing is found, then we must actually go
     * and introspect the method from the MethodMap.
     * </p>
     *
     * @param methodKey the method key
     * @return A Method object representing the method to invoke or null.
     * @throws MethodKey.AmbiguousException When more than one method is a match for the parameters.
     */
    Method getMethod(final MethodKey methodKey) throws MethodKey.AmbiguousException {
        // Look up by key
        Method cacheEntry = byKey.get(methodKey);
        // We looked this up before and failed.
        if (cacheEntry == CACHE_MISS) {
            return null;
        }
        if (cacheEntry == null) {
            try {
                // That one is expensive...
                final Method[] methodList = byName.get(methodKey.getMethod());
                if (methodList != null) {
                    cacheEntry = methodKey.getMostSpecificMethod(methodList);
                }
                if (cacheEntry == null) {
                    byKey.put(methodKey, CACHE_MISS);
                } else {
                    byKey.put(methodKey, cacheEntry);
                }
            } catch (final MethodKey.AmbiguousException ae) {
                // that's a miss :-)
                byKey.put(methodKey, CACHE_MISS);
                throw ae;
            }
        }

        // Yes, this might just be null.
        return cacheEntry;
    }

    /**
     * Populate the Map of direct hits. These are taken from all the public methods
     * that our class, its parents and their implemented interfaces provide.
     *
     * @param cache          the ClassMap instance we create
     * @param permissions    the permissions to apply during introspection
     * @param classToReflect the class to cache
     * @param log            the Log
     */
    private static void create(final ClassMap cache, final Permissions permissions, Class<?> classToReflect, final Log log) {
        //
        // Build a list of all elements in the class hierarchy. This one is bottom-first (i.e. we start
        // with the actual declaring class and its interfaces and then move up (superclass etc.) until we
        // hit java.lang.Object. That is important because it will give us the methods of the declaring class
        // which might in turn be abstract further up the tree.
        //
        // We also ignore all SecurityExceptions that might happen due to SecurityManager restrictions.
        //
        for (; classToReflect != null; classToReflect = classToReflect.getSuperclass()) {
            if (Modifier.isPublic(classToReflect.getModifiers())) {
                populateWithClass(cache, permissions, classToReflect, log);
            }
            final Class<?>[] interfaces = classToReflect.getInterfaces();
            for (final Class<?> anInterface : interfaces) {
                populateWithInterface(cache, permissions, anInterface, log);
            }
        }
        // now that we've got all methods keyed in, lets organize them by name
        if (!cache.byKey.isEmpty()) {
            final List<Method> lm = new ArrayList<>(cache.byKey.size());
            lm.addAll(cache.byKey.values());
            // sort all methods by name
            lm.sort(Comparator.comparing(Method::getName));
            // put all lists of methods with same name in byName cache
            int start = 0;
            while (start < lm.size()) {
                final String name = lm.get(start).getName();
                int end = start + 1;
                while (end < lm.size()) {
                    final String walk = lm.get(end).getName();
                    if (!walk.equals(name)) {
                        break;
                    }
                    end += 1;
                }
                final Method[] lmn = lm.subList(start, end).toArray(new Method[end - start]);
                cache.byName.put(name, lmn);
                start = end;
            }
        }
    }

    /**
     * Recurses up interface hierarchy to get all super interfaces.
     *
     * @param cache       the cache to fill
     * @param permissions the permissions to apply during introspection
     * @param iface       the interface to populate the cache from
     * @param log         the Log
     */
    private static void populateWithInterface(final ClassMap cache,
                                              final Permissions permissions,
                                              final Class<?> iface,
                                              final Log log) {
        if (Modifier.isPublic(iface.getModifiers())) {
            populateWithClass(cache, permissions, iface, log);
            final Class<?>[] supers = iface.getInterfaces();
            for (final Class<?> aSuper : supers) {
                populateWithInterface(cache, permissions, aSuper, log);
            }
        }
    }

    /**
     * Recurses up class hierarchy to get all super classes.
     *
     * @param cache       the cache to fill
     * @param permissions the permissions to apply during introspection
     * @param clazz       the class to populate the cache from
     * @param log         the Log
     */
    private static void populateWithClass(final ClassMap cache,
                                          final Permissions permissions,
                                          final Class<?> clazz,
                                          final Log log) {
        try {
            final Method[] methods = clazz.getDeclaredMethods();
            for (final Method mi : methods) {
                // add method to byKey cache; do not override
                final MethodKey key = new MethodKey(mi);
                final Method pmi = cache.byKey.putIfAbsent(key, permissions.allow(mi) ? mi : CACHE_MISS);
                if (pmi != null && pmi != CACHE_MISS && log.isDebugEnabled() && !key.equals(new MethodKey(pmi))) {
                    // foo(int) and foo(Integer) have the same signature for JEXL
                    log.debug("Method " + pmi + " is already registered, key: " + key.debugString());
                }
            }
        } catch (final SecurityException se) {
            // Everybody feels better with...
            if (log.isDebugEnabled()) {
                log.debug("While accessing methods of " + clazz + ": ", se);
            }
        }
    }
}
