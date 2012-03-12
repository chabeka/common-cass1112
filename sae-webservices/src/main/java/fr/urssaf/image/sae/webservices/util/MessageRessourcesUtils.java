package fr.urssaf.image.sae.webservices.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

import fr.urssaf.image.sae.webservices.context.WebServicesApplicationContext;


/**
 * Cette classe permet d'externaliser la récupération des messages
 * d'erreur et des exceptions.
 * <br>
 * Effectivement dans plusieurs classes d'implementation ou de validation(dans le cas d'aspect)
 * plusieurs méthodes sont utilisées
 * 
 */
public final class MessageRessourcesUtils {

   private MessageRessourcesUtils() {
   }
   // Recupération du contexte pour les fichiers properties
//   private static final ApplicationContext CONTEXT = new ClassPathXmlApplicationContext("/applicationContext.xml");
//   private static final MessageSource MESSAGESOURCES = (MessageSource) CONTEXT.getBean("messageSource_sae_webservices");

   private static MessageSource messageSource;
   static {

      messageSource = WebServicesApplicationContext.getApplicationContext()
            .getBean("messageSource_sae_webservices", MessageSource.class);
   }
   
   
   /**
    * Methode qui récupére les messages d'erreur avec objet en question.
    * 
    * @param message cle de l'exception contenu dans le fichier .properties
    * @param object l'url complete ou le chemin du fichier complet
    * @return String message exception en question ou valeur
    */
   public static String recupererMessage(String message, Object object) {
      Object[] param = new Object[] {object};
      return messageSource.getMessage(message, param, Locale.getDefault());
   }
}