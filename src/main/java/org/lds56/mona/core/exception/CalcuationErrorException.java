package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class CalcuationErrorException extends MonaRuntimeException {

  public CalcuationErrorException() {
    super();
  }

  public CalcuationErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public CalcuationErrorException(String message) {
    super(message);
  }

  public CalcuationErrorException(Throwable cause) {
    super(cause);
  }

}
