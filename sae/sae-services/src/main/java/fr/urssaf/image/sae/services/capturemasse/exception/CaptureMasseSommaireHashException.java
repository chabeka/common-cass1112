package fr.urssaf.image.sae.services.capturemasse.exception;

import java.text.MessageFormat;

public class CaptureMasseSommaireHashException extends Exception {

   public CaptureMasseSommaireHashException(String hashAttendu,
         String hashObtenu, String typeHash) {
      super(
            MessageFormat
                  .format(
                        "Le hash du fichier sommaire.xml attendu {0} est différent de celui obtenu {1} (type de hash {2})",
                        hashAttendu, hashObtenu, typeHash));
   }
}
