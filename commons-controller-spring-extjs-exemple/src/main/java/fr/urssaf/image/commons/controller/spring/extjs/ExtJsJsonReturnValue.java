package fr.urssaf.image.commons.controller.spring.extjs;

import java.util.HashMap;
import java.util.Map;

/**
 * Valeur de retour pour un appel Ajax ExtJs.
 * 
 * Afin de ne pas directement écrire une chaîne JSON, il est plus pratique de passer par
 * cette classe, puis d'appeler la classe de service ExtJsJsonService afin de la transformer
 * en Json.
 * 
 * La classe est au "format" décrit dans la fiche de développement F022 : 
 * Javascript : Utilisation du framework extjs
 *
 */
public class ExtJsJsonReturnValue {

   
   /**
    * Booléen indiquant si le retour est un succès ou un échec 
    */
   private boolean success;
   
   /**
    * Message général (de succès ou d'échec) 
    */
   private String message;
   
   /**
    * Liste des erreurs de saisie utilisateur
    */
   private final Map<String, String> errors;
   
   
   /**
    * Constructeur
    */
   public ExtJsJsonReturnValue()
   {
      errors = new HashMap<String, String>();
   }


   /**
    * Renvoie le flag indiquant si le retour est un succès ou un échec
    * @return indique si le retour est un succès ou un échec
    */
   public final Boolean getSuccess() {
      return success;
   }


   /**
    * Positionne le flag indiquant si le retour est un succès ou un échec
    * @param success indique si le retour est un succès ou un échec
    */
   public final void setSuccess(Boolean success) {
      this.success = success;
   }


   /**
    * Renvoie le message général (de succès ou d'échec)
    * @return Message général (de succès ou d'échec) 
    */
   public final String getMessage() {
      return message;
   }


   /**
    * Définit le message général (de succès ou d'échec)
    * @param message Message général (de succès ou d'échec)
    */
   public final void setMessage(String message) {
      this.message = message;
   }


   /**
    * Renvoie la liste des erreurs de saisie utilisateur
    * @return liste des erreurs de saisie utilisateur
    */
   public final Map<String, String> getErrors() {
      return errors;
   }


   /**
    * Ajoute une erreur à la liste des erreurs
    * @param nomChamp Le nom du champ du formulaire
    * @param messageErreur Le message d'erreur
    */
   public final void ajouteErreur(String nomChamp, String messageErreur)
   {
      this.errors.put(nomChamp, messageErreur);
   }
   
}
