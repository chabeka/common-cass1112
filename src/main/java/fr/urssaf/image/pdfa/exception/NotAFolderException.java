package fr.urssaf.image.pdfa.exception;

import java.text.MessageFormat;

public class NotAFolderException extends Exception {
   public NotAFolderException(String cheminPDF) {
      super(MessageFormat.format("Le chemin vers le répertoire {0} est incorrect.",cheminPDF));
   }
}
