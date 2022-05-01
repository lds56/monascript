package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 * @description Exception from engine
 */
public class MonaEngineException extends RuntimeException {


  public MonaEngineException() {
    super();
  }


  public MonaEngineException(String message, Throwable cause) {
    super(message, cause);

  }


  public MonaEngineException(String message) {
    super(message);

  }


  public MonaEngineException(Throwable cause) {
    super(cause);

  }

}
