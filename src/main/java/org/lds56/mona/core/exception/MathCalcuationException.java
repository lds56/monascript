package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class MathCalcuationException extends CalcuationErrorException {

  public MathCalcuationException() {
    super();
  }

  public MathCalcuationException(String message, Throwable cause) {
    super(message, cause);
  }

  public MathCalcuationException(String message) {
    super(message);
  }

  public MathCalcuationException(Throwable cause) {
    super(cause);
  }

}
