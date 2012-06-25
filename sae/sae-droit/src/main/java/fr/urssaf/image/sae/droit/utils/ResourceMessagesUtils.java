/**
 * 
 */
package fr.urssaf.image.sae.droit.utils;

import java.util.Locale;

import org.springframework.context.MessageSource;

import fr.urssaf.image.sae.droit.context.ServicesApplicationContext;

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
public final class ResourceMessagesUtils {
   
      private ResourceMessagesUtils() {
      }

      private static MessageSource messageSource;

      static {

         messageSource = ServicesApplicationContext.getApplicationContext()
               .getBean("messageSource_sae_services", MessageSource.class);
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
      public static String loadMessage(String code, Object... args) {
         return messageSource.getMessage(code, args, Locale.getDefault());
      }

   }

