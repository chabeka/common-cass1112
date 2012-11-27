package fr.urssaf.image.sae.services.capturemasse.exception;

import java.text.MessageFormat;

/**
 * Exception levée si le type de hash n'est pas autorisé
 */
public class CaptureMasseSommaireTypeHashException extends Exception {

   /**
    * Constructeur
    * 
    * @param typeHash
    *           type de hash en erreur
    */
   public CaptureMasseSommaireTypeHashException(String typeHash) {
      super(MessageFormat.format("Le type de hash {0} n'est pas autorisé",
            typeHash));

   }

}
