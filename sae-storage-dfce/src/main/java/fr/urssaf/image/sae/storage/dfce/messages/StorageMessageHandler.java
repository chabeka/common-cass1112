package fr.urssaf.image.sae.storage.dfce.messages;


import org.springframework.context.MessageSource;

import fr.urssaf.image.sae.storage.dfce.constants.Constants;
import fr.urssaf.image.sae.storage.dfce.context.StorageApplicationContext;

/**
 * Fournit des services qui retournent un message à partir de sa clés .
 */
public final class StorageMessageHandler { 
  
   private static final MessageSource MESSAGE_SOURCES;

   static {
      // Récupération du contexte pour les fichiers properties
      MESSAGE_SOURCES = StorageApplicationContext.getApplicationContext()
            .getBean("messageSource_sae_storage_dfce", MessageSource.class);
   }
   
   /**
    * Récupéré un message d'erreur.
    * 
    * @param errorCode
    *           : Le code erreur
    * @param messageKey
    *           : La clé du message
    * @param impactKey
    *           : La clé de l'impact
    * @param actionKey
    *           : La clé de l'action
    * @return Le message en faisant la concaténation du code erreur|message
    *         |impact| action
    */
   @SuppressWarnings("PMD.AvoidDuplicateLiterals")
   public static String getMessage(final String errorCode,
         final String messageKey, final String impactKey, final String actionKey) {

      final StringBuilder strBuilder = new StringBuilder();
      strBuilder.setLength(0);
      strBuilder.append(
            MESSAGE_SOURCES.getMessage(errorCode, null,
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.DEFAULT_LOCAL)).append(" | ");

      strBuilder.append(" | ").append(
            MESSAGE_SOURCES.getMessage(messageKey, null,
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.DEFAULT_LOCAL));
      strBuilder.append(" | ").append(
            MESSAGE_SOURCES.getMessage(impactKey, null,
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.DEFAULT_LOCAL)).append(" | ").append(
            MESSAGE_SOURCES.getMessage(actionKey, null,
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.DEFAULT_LOCAL));
      return strBuilder.toString();
   }

   /**
    * @param messageKey
    *           : La clé du message
    * @return Le message à partir de la clé
    */
   @SuppressWarnings("PMD.LongVariable")
   public static String getMessage(final String messageKey) {
      return MESSAGE_SOURCES.getMessage(messageKey, null,
            Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.DEFAULT_LOCAL);
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private StorageMessageHandler() {
      assert false;
   }
}
