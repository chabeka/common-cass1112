package fr.urssaf.image.sae.ecde.messages;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Cette classe permet d'externaliser la récupération des messages d'erreur et
 * des exceptions.
 */
@Component
public final class EcdeMessageRessourcesUtils {

   @Autowired
   private EcdeMessageSource ecdeMessageSource;

   /**
    * Renvoie un message contenu dans un fichier properties à partir de sa clé
    * 
    * @param message
    *           clé du message contenu dans le fichier .properties
    * @return le message
    */
   public String recupererMessage(String message) {
      return ecdeMessageSource.getMessage(message, null, Locale.getDefault());
   }

   /**
    * Renvoie un message contenu dans un fichier properties à partir de sa clé
    * 
    * @param message
    *           cle de l'exception contenu dans le fichier .properties
    * @param objects
    *           les paramètres du message
    * @return String message exception en question ou valeur
    */
   public String recupererMessage(String message, Object... objects) {
      return ecdeMessageSource
            .getMessage(message, objects, Locale.getDefault());
   }

}