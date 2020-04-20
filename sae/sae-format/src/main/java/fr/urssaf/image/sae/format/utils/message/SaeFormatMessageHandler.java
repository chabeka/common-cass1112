package fr.urssaf.image.sae.format.utils.message;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Permet de lire le fichier properites : sae_format_messages.properties.
 * Pour la gestion de messages simples et messages d'exception.
 */

public final class SaeFormatMessageHandler {

  private static MessageSource messageSource;
  /*
   * On instancie le messageSource dans un bloc statique,
   * ce bloc est exécuté une seule fois au moment
   * du chargement de la classe
   */
  static {

    final ResourceBundleMessageSource resourceBundleMessageSource = new ResourceBundleMessageSource();
    resourceBundleMessageSource.setBasename("i18n/sae_format_messages");
    messageSource = resourceBundleMessageSource;

  }

  /**
   * Récupére un message à partir de sa clé.
   * 
   * @param messageKey
   *          La clé du message
   * @return Le message avec les valeurs substituées.
   */

  public static String getMessage(final String messageKey) {

    return messageSource.getMessage(messageKey, null, Locale.getDefault());
  }

  /**
   * Récupére un message.
   * 
   * @param messageKey
   *          La clé du message
   * @param valueKey
   *          La valeur de substitution
   * @return Le message avec les valeurs substituées.
   */

  public static String getMessage(final String messageKey, final String valueKey) {
    return messageSource.getMessage(messageKey, new Object[] {valueKey}, Locale.getDefault());
  }

  /**
   * Récupére un message.
   * 
   * @param messageKey
   *          La clé du message
   * @param firstValueKey
   *          La valeur de substitution.
   * @param secondValueKey
   *          La valeur de substitution
   * @return Le message avec les valeurs substituées.
   */

  public static String getMessage(final String messageKey, final String firstValueKey, final Object secondValueKey) {
    return messageSource.getMessage(messageKey, new Object[] {firstValueKey, secondValueKey}, Locale.getDefault());
  }

}
