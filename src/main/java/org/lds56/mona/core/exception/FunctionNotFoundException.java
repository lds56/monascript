package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class FunctionNotFoundException extends CalcuationErrorException {

  public FunctionNotFoundException() {
    super();
  }

  public FunctionNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public FunctionNotFoundException(String message) {
    super(message);
  }

  public FunctionNotFoundException(Throwable cause) {
    super(cause);
  }


  public FunctionNotFoundException(Class clazz, String funcName, Throwable cause) {
    super("Class " + clazz.getName() + " has no function named " + funcName, cause);
  }

}
