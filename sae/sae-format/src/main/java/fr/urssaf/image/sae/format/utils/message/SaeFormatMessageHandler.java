package fr.urssaf.image.sae.format.utils.message;

import java.util.Locale;

import org.springframework.context.MessageSource;

import fr.urssaf.image.sae.format.context.SaeFormatApplicationContext;

/**
 * Permet de lire le fichier properites : sae_format_messages.properties.
 * 
 * Pour la gestion de messages simples et messages d'exception.
 */
public final class SaeFormatMessageHandler {

   private static final MessageSource MESSAGE_SOURCES;

   static {
      // Récupération du contexte pour les fichiers properties
      MESSAGE_SOURCES = SaeFormatApplicationContext.getApplicationContext()
            .getBean("messageSource_sae_format", MessageSource.class);
   }

   /**
    * Récupére un message à partir de sa clé.
    * 
    * @param messageKey
    *           : La clé du message
    * @return Le message avec les valeurs substituées.
    */
   @SuppressWarnings("PMD.AvoidDuplicateLiterals")
   public static String getMessage(final String messageKey) {

      return MESSAGE_SOURCES.getMessage(messageKey, null, Locale.getDefault());
   }

   /**
    * Récupére un message.
    * 
    * @param messageKey
    *           : La clé du message
    * @param valueKey
    *           : La valeur de substitution
    * @return Le message avec les valeurs substituées.
    */
   @SuppressWarnings("PMD.AvoidDuplicateLiterals")
   public static String getMessage(final String messageKey,
         final String valueKey) {
      return MESSAGE_SOURCES.getMessage(messageKey, new Object[] { valueKey },
            Locale.getDefault());
   }

   /**
    * Récupére un message.
    * 
    * @param messageKey
    *           : La clé du message
    * @param firstValueKey
    *           : La valeur de substitution.
    * @param secondValueKey
    *           : La valeur de substitution
    * @return Le message avec les valeurs substituées.
    */
   @SuppressWarnings("PMD.AvoidDuplicateLiterals")
   public static String getMessage(final String messageKey,
         final String firstValueKey, final Object secondValueKey) {
      return MESSAGE_SOURCES.getMessage(messageKey, new Object[] {
            firstValueKey, secondValueKey }, Locale.getDefault());
   }

   /** Cette classe n'est pas faite pour être instanciée. */
   private SaeFormatMessageHandler() {
      assert false;
   }
}
