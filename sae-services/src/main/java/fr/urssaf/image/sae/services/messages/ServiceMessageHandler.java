package fr.urssaf.image.sae.services.messages;

import org.springframework.context.MessageSource;

import fr.urssaf.image.sae.services.context.ServicesApplicationContext;
import fr.urssaf.image.sae.storage.dfce.constants.Constants;

/**
 * Fournit des services qui retournent un message à partir de sa clés .
 */
public final class ServiceMessageHandler {

   private static final MessageSource MESSAGE_SOURCES;

   static {
      // Récupération du contexte pour les fichiers properties
      MESSAGE_SOURCES = ServicesApplicationContext.getApplicationContext()
            .getBean("messageSource_sae_services", MessageSource.class);
   }

   /**
    * @param messageKey
    *           : La clé du message
    * @return Le message à partir de la clé
    */
   public static String getMessage(final String messageKey) {
      return MESSAGE_SOURCES.getMessage(messageKey, null,
            Constants.NO_MESSAGE_FOR_THIS_KEY, Constants.DEFAULT_LOCAL);
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private ServiceMessageHandler() {
      assert false;
   }
}
