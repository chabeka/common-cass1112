/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.exception;

import org.springframework.util.Assert;

import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentType;

/**
 * Exception levée par un document du fichier sommaire.xml lors du traitement de
 * capture de masse
 * 
 */
public class CaptureMasseSommaireDocumentException extends Exception {

   private static final long serialVersionUID = 1L;

   private final int index;
   private final DocumentType documentType;

   /**
    * Constructeur
    * 
    * @param index
    *           index du document dans le fichier sommaire.xml
    * @param cause
    *           cause de l'exception de contrôle
    * @param documentType
    *           Le document du fichier sommaire.xml où l'erreur a eu lieu
    */
   public CaptureMasseSommaireDocumentException(final int index, final Exception cause,
         final DocumentType documentType) {

      super(cause);

      Assert.notNull(cause, "l'exception mère ne doit pas être à null");
      Assert.notNull(documentType, "le document concerné doit être renseigné");

      this.index = index;
      this.documentType = documentType;

   }

   /**
    * @return the index
    */
   public final int getIndex() {
      return index;
   }

   /**
    * @return the documentType
    */
   public final DocumentType getDocumentType() {
      return documentType;
   }

}
