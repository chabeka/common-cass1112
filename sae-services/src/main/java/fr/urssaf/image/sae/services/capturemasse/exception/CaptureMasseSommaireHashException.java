package fr.urssaf.image.sae.services.capturemasse.exception;

import java.text.MessageFormat;

/**
 * Exception levée si le type de hash obtenu est différent du type de hash
 * fourni
 * 
 * 
 */
public class CaptureMasseSommaireHashException extends Exception {

   /**
    * Constructeur
    * 
    * @param hashAttendu
    *           hash fourni
    * @param hashObtenu
    *           hash calculé
    * @param typeHash
    *           algo de hash
    */
   public CaptureMasseSommaireHashException(String hashAttendu,
         String hashObtenu, String typeHash) {
      super(
            MessageFormat
                  .format(
                        "Le hash du fichier sommaire.xml attendu : {0} est différent de celui obtenu : {1} (type de hash : {2})",
                        hashAttendu, hashObtenu, typeHash));
   }
}
