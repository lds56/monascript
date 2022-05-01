package org.lds56.mona.core.exception;

import org.lds56.mona.core.runtime.types.MonaObject;

/**
 * Internal exception occurs in script
 *
 */
public class MonaInternalException extends MonaRuntimeException {

  private final MonaObject throwedObj;

  public MonaInternalException(String message, Throwable cause, MonaObject obj) {
    super(message, cause);
    this.throwedObj = obj;
  }

  public MonaInternalException(String message, MonaObject obj) {
    super(message);
    this.throwedObj = obj;
  }

  public MonaInternalException(MonaObject obj) {
    super();
    this.throwedObj = obj;
  }

  public MonaObject getThrowed() {
    return this.throwedObj;
  }
  public Object getThrowedObject() {
    return this.throwedObj == null? null : this.throwedObj.getValue();
  }


}
