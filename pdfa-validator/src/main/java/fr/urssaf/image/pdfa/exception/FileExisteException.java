package fr.urssaf.image.pdfa.exception;

import java.text.MessageFormat;

public class FileExisteException extends Exception {
   public FileExisteException(String cheminLog) {
      super(MessageFormat.format("Le fichier de log {0} indiqué existe déjà, merci de le supprimer.",cheminLog));
   }
}
