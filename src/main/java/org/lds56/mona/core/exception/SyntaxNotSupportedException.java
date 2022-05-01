package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/18
 */
public class SyntaxNotSupportedException extends SyntaxErrorException {

  public SyntaxNotSupportedException() {
    super();
  }

  public SyntaxNotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public SyntaxNotSupportedException(String message) {
    super(message);
  }

  public SyntaxNotSupportedException(Throwable cause) {
    super(cause);
  }

}
