package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class OperationNotSupportedException extends MonaRuntimeException {

  public OperationNotSupportedException() {
    super();
  }

  public OperationNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public OperationNotSupportedException(String message) {
    super(message);
  }

  public OperationNotSupportedException(Throwable cause) {
    super(cause);
  }

}
