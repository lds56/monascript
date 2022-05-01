package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class ScriptCompilationErrorException extends MonaCompilationException {

  public ScriptCompilationErrorException() {
    super();
  }

  public ScriptCompilationErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ScriptCompilationErrorException(String message) {
    super(message);
  }

  public ScriptCompilationErrorException(Throwable cause) {
    super(cause);
  }

}
