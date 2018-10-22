package fr.urssaf.image.sae.services.batch.common.model;

public class ErreurTraitement {

   private String codeErreur;

   private String messageErreur;

   private Exception exception;

   /**
    * Getter
    * 
    * @return the codeErreur
    */
   public String getCodeErreur() {
      return codeErreur;
   }

   /**
    * Setter
    * 
    * @param codeErreur
    *           the codeErreur to set
    */
   public void setCodeErreur(String codeErreur) {
      this.codeErreur = codeErreur;
   }

   /**
    * Getter
    * 
    * @return the messageErreur
    */
   public String getMessageErreur() {
      return messageErreur;
   }

   /**
    * Setter
    * 
    * @param messageErreur
    *           the messageErreur to set
    */
   public void setMessageErreur(String messageErreur) {
      this.messageErreur = messageErreur;
   }

   /**
    * @return the exception
    */
   public Exception getException() {
      return exception;
   }

   /**
    * @param exception
    *           the exception to set
    */
   public void setException(Exception exception) {
      this.exception = exception;
   }

}
