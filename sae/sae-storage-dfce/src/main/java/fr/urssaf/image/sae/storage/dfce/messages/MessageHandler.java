package fr.urssaf.image.sae.storage.dfce.messages;


import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.sae.storage.dfce.contants.Constants;

/**
 * Fournit des services qui retournent un message à partir de sa clés .
 * 
 * @author akenore
 * 
 */
public final class MessageHandler { 
   // Récupération du contexte pour les fichiers properties
   private static final MessageSource MESSAGE_SOURCES = (MessageSource) new ClassPathXmlApplicationContext(
         "applicationContext-sae-storage-dfce.xml").getBean("messageSource");
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
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.LOCAL)).append(" | ");

      strBuilder.append(" | ").append(
            MESSAGE_SOURCES.getMessage(messageKey, null,
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.LOCAL));
      strBuilder.append(" | ").append(
            MESSAGE_SOURCES.getMessage(impactKey, null,
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.LOCAL)).append(" | ").append(
            MESSAGE_SOURCES.getMessage(actionKey, null,
                  Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.LOCAL));
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
            Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.LOCAL);
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private MessageHandler() {
      assert false;
   }
}
