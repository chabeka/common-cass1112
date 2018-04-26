package fr.urssaf.image.sae.webdemo.component;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

/**
 * Composant d'accès aux resources messages de l'application<br>
 * <br>
 * Ce composant n'est utilisable que si la méthode
 * {@link #setMessageSource(MessageSource)} est appelée<br>
 * L'appel de cette méthode est automatique avec la configuration
 * <code>applicationContext.xml</code> <br>
 * Une exception de type {@link IllegalArgumentException} peut-être levée si
 * aucun bean de type {@link MessageSource} n'est configuré <br>
 * <br>
 * Exemple de configuration :
 * 
 * <pre>
 *    &lt;bean id="messageSource"
 *       class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
 *       &lt;property name="basenames">
 *          &lt;list>
 *             &lt;value>/message/title&lt;/value>
 *             &lt;value>/message/error&lt;/value>
 *          &lt;/list>
 *       &lt;/property>
 *    &lt;/bean>
 * </pre>
 * 
 * 
 */
@Component
public final class MessageComponent {

   private static final Logger LOG = Logger.getLogger(MessageComponent.class);

   private static MessageSource messageSource;

   @Autowired
   protected void setMessageSource(MessageSource messageSource) {
      if (messageSource == null) {

         throw new IllegalArgumentException(
               "un bean de type 'MessageSource' is required");
      }
      MessageComponent.messageSource = messageSource;
   }

   /**
    * @param code
    *           code du message
    * @param defaultMessage
    *           message si le code n'existe pas
    * @param request
    *           requête HTTP pour déterminer la locale
    *           {@link HttpServletRequest#getLocale()}
    * @return message de {@link MessageSource}
    */
   public static String getMessage(String code, String defaultMessage,
         HttpServletRequest request) {

      return messageSource.getMessage(code, new String[0], defaultMessage,
            request.getLocale());

   }

   /**
    * Si le code n'existe pas alors on renvoie le code comme message
    * 
    * @param code
    *           code du message
    * @param request
    *           requête HTTP pour déterminer la locale
    *           {@link HttpServletRequest#getLocale()}
    * @return message de {@link MessageSource}
    */
   public static String getMessage(String code, HttpServletRequest request) {

      String message = null;

      try {
         message = messageSource.getMessage(code, new String[0], request
               .getLocale());
      } catch (NoSuchMessageException e) {

         LOG.warn(StringUtils.join(new Object[] { code,
               " is required in message source" }));

         message = code;
      }

      return message;
   }
}
