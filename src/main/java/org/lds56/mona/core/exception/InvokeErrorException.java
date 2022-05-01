package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class InvokeErrorException extends CalcuationErrorException {

  public InvokeErrorException() {
    super();
  }

  public InvokeErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvokeErrorException(String message) {
    super(message);
  }

  public InvokeErrorException(Throwable cause) {
    super(cause);
  }

}
