package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/20
 */
public class InterpretErrorException extends MonaRuntimeException {

  public InterpretErrorException() {
    super();
  }

  public InterpretErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public InterpretErrorException(String message) {
    super(message);
  }

  public InterpretErrorException(Throwable cause) {
    super(cause);
  }

}
