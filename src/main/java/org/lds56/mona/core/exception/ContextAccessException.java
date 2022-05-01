package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class ContextAccessException extends CalcuationErrorException {

  public ContextAccessException() {
    super();
  }

  public ContextAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public ContextAccessException(String message) {
    super(message);
  }

  public ContextAccessException(Throwable cause) {
    super(cause);
  }

}
