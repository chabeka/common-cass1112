/**
 * 
 */
package fr.urssaf.image.commons.cassandra.modeapi;

/**
 * 
 * 
 */
public class ModeAPIRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 834427765645695073L;

  /**
   * Constructeur
   * 
   * @param message
   *           message de l'exception
   */
  public ModeAPIRuntimeException(final String message) {
    super(message);
  }

  /**
   * Constructeur
   * 
   * @param message
   *           message d'origine
   * @param cause
   *           cause de l'exception
   */
  public ModeAPIRuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructeur
   * 
   * @param cause
   *           la cause de l'exception
   */
  public ModeAPIRuntimeException(final Throwable cause) {
    super(cause);
  }

  /**
   * constructeur
   * 
   * @param cause
   *          cause mère
   */
  public ModeAPIRuntimeException(final Exception exception) {
    super(exception);
  }
}
