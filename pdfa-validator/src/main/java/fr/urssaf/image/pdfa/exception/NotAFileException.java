package fr.urssaf.image.pdfa.exception;

import java.text.MessageFormat;

public class NotAFileException extends Exception {
   /**
    * Constructeur
    * 
    */
   public NotAFileException(String cheminLog) {
      super(MessageFormat.format("Le fichier de log {0} indiqué est invalid : Ce n'est pas un fichier.",cheminLog));
   }
}
