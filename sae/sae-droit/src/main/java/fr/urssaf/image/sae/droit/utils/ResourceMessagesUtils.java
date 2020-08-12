/**
 * 
 */
package fr.urssaf.image.sae.droit.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

/**
 * Classe utilitaire pour formater les messages.<br>
 * Le bean de configuration des messages est :
 * 
 * <pre>
 * &lt;bean id="messageSource_sae_services"
 *     class="org.springframework.context.support....">
 *       ...
 * &lt;/bean>
 * </pre>
 * 
 */
@Component
public final class ResourceMessagesUtils {

  private static MessageSource MESSAGE_SOURCES;

  /**
   * Constructeur
   * 
   * @param messageSource
   *           l'object d'accès aux messages
   */
  @Autowired
  public ResourceMessagesUtils(
                               @Qualifier("messageSource_sae_droits") final MessageSource messageSource) {
    // Récupération du contexte pour les fichiers properties
    MESSAGE_SOURCES = messageSource;
  }

  /**
   * charge un message
   * 
   * @param code
   *           code du message
   * @param args
   *           arguments du message
   * @return message formaté
   */
  public static String loadMessage(final String code, final Object... args) {
    return MESSAGE_SOURCES.getMessage(code, args, Locale.getDefault());
  }

}
