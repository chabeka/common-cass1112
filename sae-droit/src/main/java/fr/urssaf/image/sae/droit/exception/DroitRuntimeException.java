/**
 * 
 */
package fr.urssaf.image.sae.droit.exception;

/**
 * 
 * 
 */
public class DroitRuntimeException extends RuntimeException {

  private static final long serialVersionUID = 834427765645695073L;

  /**
   * Constructeur
   * 
   * @param message
   *           message de l'exception
   */
  public DroitRuntimeException(final String message) {
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
  public DroitRuntimeException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructeur
   * 
   * @param cause
   *           la cause de l'exception
   */
  public DroitRuntimeException(final Throwable cause) {
    super(cause);
  }

  /**
   * constructeur
   * 
   * @param cause
   *          cause mère
   */
  public DroitRuntimeException(final Exception exception) {
    super(exception);
  }
}
