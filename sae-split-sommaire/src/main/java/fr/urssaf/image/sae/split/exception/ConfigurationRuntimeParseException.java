package fr.urssaf.image.sae.split.exception;

/**
 * Erreur levée en cas d'erreur critique lors du spilt du sommaire
 */
public class ConfigurationRuntimeParseException extends RuntimeException {

  private static final long serialVersionUID = -7650012673945394957L;

  /**
   * Constructeur
   * 
   * @param message
   *           message d'origine
   */
  public ConfigurationRuntimeParseException(final String message) {
    super(message);
  }

  public ConfigurationRuntimeParseException(final String message, final Throwable e) {
    super(message, e);
  }

  public ConfigurationRuntimeParseException(final Throwable e) {
    super(e);
  }

}
