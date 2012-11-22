package fr.urssaf.image.sae.services.capturemasse.exception;

import java.text.MessageFormat;

public class CaptureMasseSommaireTypeHashException extends Exception {

   public CaptureMasseSommaireTypeHashException(String typeHash) {
      super(MessageFormat.format("Le type de hash {0} n'est pas autorisé",
            typeHash));

   }

}
