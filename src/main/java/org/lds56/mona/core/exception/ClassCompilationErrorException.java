package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class ClassCompilationErrorException extends MonaCompilationException {

  public ClassCompilationErrorException() {
    super();
  }

  public ClassCompilationErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClassCompilationErrorException(String message) {
    super(message);
  }

  public ClassCompilationErrorException(Throwable cause) {
    super(cause);
  }

}
