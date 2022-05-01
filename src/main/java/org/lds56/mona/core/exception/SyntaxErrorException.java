package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class SyntaxErrorException extends MonaCompilationException {

  public SyntaxErrorException() {
    super();
  }

  public SyntaxErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public SyntaxErrorException(String message) {
    super(message);
  }

  public SyntaxErrorException(Throwable cause) {
    super(cause);
  }

}
