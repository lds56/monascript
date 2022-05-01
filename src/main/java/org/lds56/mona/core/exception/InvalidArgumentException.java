package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class InvalidArgumentException extends InvokeErrorException {

  public InvalidArgumentException() {
    super();
  }

  public InvalidArgumentException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidArgumentException(String message) {
    super(message);
  }

  public InvalidArgumentException(Throwable cause) {
    super(cause);
  }

}
