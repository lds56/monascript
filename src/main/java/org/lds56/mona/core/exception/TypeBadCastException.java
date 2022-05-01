package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class TypeBadCastException extends MonaRuntimeException {

  public TypeBadCastException() {
    super();
  }

  public TypeBadCastException(String message, Throwable cause) {
    super(message, cause);
  }

  public TypeBadCastException(String message) {
    super(message);
  }

  public TypeBadCastException(Throwable cause) {
    super(cause);
  }

}
