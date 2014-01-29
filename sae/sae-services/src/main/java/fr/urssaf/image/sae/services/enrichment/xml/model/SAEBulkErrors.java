package fr.urssaf.image.sae.services.enrichment.xml.model;

/**
 * Énumération contenant la listes erreurs.<br/>
 */
public enum SAEBulkErrors {
   // TECHNICALERROR
   TECHNICAL_ERROR("erreur.technique", "erreur.technique.capture.en.masse"),
   // FUNCTIONALERROR
   FUNCTIONAL_ERROR("erreur.fonctionnelle",
         "erreur.fonctionnelle.capture.en.masse"),
   // pas de valeur
   NOVALUE("", "");

   // Le code long de la métadonnée.
   private String errorType;
   private String message;

   /**
    * 
    * @param errorType
    *           : Type de l'erreur <br>
    *           <ul>
    *           <li>Technique</li>
    *           <li>Fonctionnelle</li>
    *           </ul>
    * 
    */
   SAEBulkErrors(final String errorType, final String message) {
      this.errorType = errorType;
      this.setMessage(message);
   }

   /**
    * @param errorType
    *           : Type de l'erreur.
    */
   public void setErrorType(final String errorType) {
      this.errorType = errorType;
   }

   /**
    * @return : Type de l'erreur.
    */
   public String getErrorType() {
      return errorType;
   }

   /**
    * @param message
    *           : Le message d'erreur.
    */
   public void setMessage(String message) {
      this.message = message;
   }

   /**
    * @return Le message d'erreur.
    */
   public String getMessage() {
      return message;
   }
}
