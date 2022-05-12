package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 * @description Exception during compilation of mona script
 */
public class MonaCompilationException extends RuntimeException {

  public MonaCompilationException() {
    super();
  }

  public MonaCompilationException(String message, Throwable cause) {
    super(message, cause);
  }

  public MonaCompilationException(String message) {
    super(message);
  }

  public MonaCompilationException(Throwable cause) {
    super(cause);
  }

}
