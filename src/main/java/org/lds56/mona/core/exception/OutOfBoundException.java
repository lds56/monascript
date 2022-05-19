package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class OutOfBoundException extends MonaRuntimeException {

  public OutOfBoundException() {
    super();
  }

  public OutOfBoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public OutOfBoundException(String message) {
    super(message);
  }

  public OutOfBoundException(Throwable cause) {
    super(cause);
  }

}
