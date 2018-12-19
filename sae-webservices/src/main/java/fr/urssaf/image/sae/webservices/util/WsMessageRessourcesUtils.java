package fr.urssaf.image.sae.webservices.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.webservices.modele.WsMessageSource;

/**
 * Cette classe permet d'externaliser la récupération des messages d'erreur et
 * des exceptions.
 */
@Component
public final class WsMessageRessourcesUtils {

   @Autowired
   private WsMessageSource wsMessageSource;

   /**
    * Methode qui récupére les messages d'erreur avec objet en question.
    * 
    * @param message
    *           cle de l'exception contenu dans le fichier .properties
    * @param object
    *           l'url complete ou le chemin du fichier complet
    * @return String message exception en question ou valeur
    */
   public String recupererMessage(String message, Object object) {
      Object[] param = new Object[] { object };
      return wsMessageSource.getMessage(message, param, Locale.getDefault());
   }

}