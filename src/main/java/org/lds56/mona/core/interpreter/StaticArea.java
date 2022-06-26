package org.lds56.mona.core.interpreter;

import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * @Author: Rui Chen
 * @Date: 24 Jun 2022
 * @Description: This is description.
 */
public class StaticArea {

    String[] names;

    MonaObject[] values;

    public StaticArea duplicate() {
        StaticArea newStaticArea = new StaticArea();
        newStaticArea.names = new String[this.names.length];
        System.arraycopy(this.names, 0, newStaticArea.names, 0, this.names.length);
        newStaticArea.values = new MonaObject[this.values.length];
        System.arraycopy(this.values, 0, newStaticArea.values, 0, this.values.length);
        return newStaticArea;
    }

    public StaticArea() {
        this.names = new String[]{};
        this.values = new MonaObject[]{};
    }

    public StaticArea(String[] names) {
        this.names = names;
        this.values = new MonaObject[names.length];
    }

    public StaticArea(String[] names, MonaObject[] values) {
        this.names = names;
        this.values = values;
    }

    public String getName(int idx) {
        return names[idx];
    }

    public MonaObject get(int idx) {
        // TODO: exception
        return values[idx];
    }

    public void set(int idx, MonaObject value) {
        values[idx] = value;
    }

    public int size() {
        return names.length;
    }
}
