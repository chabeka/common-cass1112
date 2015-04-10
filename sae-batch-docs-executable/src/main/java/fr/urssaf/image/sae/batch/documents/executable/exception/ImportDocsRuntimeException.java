package fr.urssaf.image.sae.batch.documents.executable.exception;

/**
 * Erreur levée en cas d'erreur critique lors de l'import de documents
 */
public class ImportDocsRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -7650012673945394957L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ImportDocsRuntimeException(String message) {
      super(message);
   }
   
   public ImportDocsRuntimeException(String message, Throwable e) {
      super(message, e);
   }
   
   public ImportDocsRuntimeException(Throwable e) {
      super(e);
   }

}
