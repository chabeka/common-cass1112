package fr.urssaf.image.sae.batch.documents.executable.exception;

/**
 * Erreur levée en cas d'erreur critique lors de l'export de documents
 */
public class ExportDocsRuntimeException extends RuntimeException {

   private static final long serialVersionUID = -7650012673945394957L;

   /**
    * Constructeur
    * 
    * @param message
    *           message d'origine
    */
   public ExportDocsRuntimeException(String message) {
      super(message);
   }
   
   public ExportDocsRuntimeException(String message, Throwable e) {
      super(message, e);
   }
   
   public ExportDocsRuntimeException(Throwable e) {
      super(e);
   }

}
