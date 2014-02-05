package fr.urssaf.image.sae.format.utils.message;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Permet de lire le fichier properites : sae_format_messages.properties.
 * 
 * Pour la gestion de messages simples et messages d'exception.
 */
@Component
public final class SaeFormatMessageHandler {

   private static MessageSource MESSAGE_SOURCES;

   @Autowired
   public SaeFormatMessageHandler(
         @Qualifier("messageSource_sae_format") MessageSource messageSource) {
      // Récupération du contexte pour les fichiers properties
      MESSAGE_SOURCES = messageSource;
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
}
