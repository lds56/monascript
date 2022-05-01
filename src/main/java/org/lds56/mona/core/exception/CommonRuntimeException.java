package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class CommonRuntimeException extends MonaRuntimeException {

  public CommonRuntimeException() {
    super();
  }

  public CommonRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }

  public CommonRuntimeException(String message) {
    super(message);
  }

  public CommonRuntimeException(Throwable cause) {
    super(cause);
  }

}
