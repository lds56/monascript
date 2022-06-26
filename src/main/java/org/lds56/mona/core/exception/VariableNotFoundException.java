package org.lds56.mona.core.exception;

/**
 * @author lds56
 * @date 2022/04/07
 */
public class VariableNotFoundException extends CalcuationErrorException {

  public VariableNotFoundException() {
    super();
  }

  public VariableNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public VariableNotFoundException(String message) {
    super(message);
  }

  public VariableNotFoundException(Throwable cause) {
    super(cause);
  }


  public VariableNotFoundException(Class clazz, String funcName, Throwable cause) {
    super("Class " + clazz.getName() + " has no function named " + funcName, cause);
  }

}
