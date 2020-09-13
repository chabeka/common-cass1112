package fr.urssaf.image.parser_opencsv.application.exception;

public class EmptyFileException extends Exception {

  private static final long serialVersionUID = -6053353836651774753L;

  /**
   * @param message
   * @param cause
   */
  public EmptyFileException(final String message, final Throwable cause) {
    super(message, cause);
  }

  /**
   * @param message
   */
  public EmptyFileException(final String message) {
    super(message);
  }

}
