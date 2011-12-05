package fr.urssaf.image.sae.ecde.util;

import java.util.Locale;

import org.springframework.context.MessageSource;

import fr.urssaf.image.sae.ecde.context.EcdeApplicationContext;


/**
 * Cette classe permet d'externaliser la récupération des messages
 * d'erreur et des exceptions.
 * <br>
 * Effectivement dans plusieurs classes d'implementation ou de validation(dans le cas d'aspect)
 * plusieurs méthodes sont utilisées
 * 
 */
public final class MessageRessourcesUtils {

   
   private static final MessageSource MESSAGESOURCES;

	static {
		// Récupération du contexte pour les fichiers properties
		MESSAGESOURCES = EcdeApplicationContext.getApplicationContext().getBean("messageSource_sae_ecde", MessageSource.class);
	}

   private static final String NONRENSEIGNE = "ecdeFileAttributNonRenseigne";
   /**
    * Methode qui récupére les messages d'erreur avec objet non renseigne.
    * 
    * @param message cle de l'exception contenu dans le fichier .properties
    * @param ecdeFileorUrl 'ecdeFile' ou 'ecdeUrl' ajouté dans le message
    * @return String message exception en question
    */
   public static String recupererMessageObject(String message, String ecdeFileorUrl) {
      return MESSAGESOURCES.getMessage(message, new Object[] {ecdeFileorUrl}, Locale.getDefault());
   }
   /**
    * Methode qui récupére les messages d'erreur avec objet en question.
    * 
    * @param message cle de l'exception contenu dans le fichier .properties
    * @param object l'url complete ou le chemin du fichier complet
    * @return String message exception en question
    */
   public static String recupererMessage(String message, Object object) {
      Object[] param = new Object[] {object};
      return MESSAGESOURCES.getMessage(message, param, Locale.getDefault());
   }
   /**
    * Methode qui récupére les messages d'erreur indiquant qu'elle attribut est manquant et dans 
    * quel position. 
    * 
    * @param valeur nom de l'attribut
    * @param curseur position de l'attribut dans la liste
    * @return String message exception en question
    */
   public static String recupMessageNonRenseigneException(String valeur, int curseur) {
      return MESSAGESOURCES.getMessage(NONRENSEIGNE, new Object[] {valeur, curseur}, Locale.getDefault());
   }
   private MessageRessourcesUtils() {
   }
}