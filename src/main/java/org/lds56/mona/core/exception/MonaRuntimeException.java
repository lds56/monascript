package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 * @description Exception during execution of mona script
 */
public class MonaRuntimeException extends RuntimeException {


  public MonaRuntimeException() {
    super();
  }


  public MonaRuntimeException(String message, Throwable cause) {
    super(message, cause);

  }


  public MonaRuntimeException(String message) {
    super(message);

  }


  public MonaRuntimeException(Throwable cause) {
    super(cause);

  }

}
