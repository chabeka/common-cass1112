package fr.urssaf.image.sae.batch.documents.executable.multithreading;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.FrozenDocumentException;

import fr.urssaf.image.sae.batch.documents.executable.bootstrap.ExecutableMain;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;
import net.docubase.toolkit.model.document.Document;

/**
 * Thread d’ajout de métadonnées à un document
 */
public class DeleteDocsRunnable implements Runnable {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DeleteDocsRunnable.class);

   /**
    * Informations du document.
    */
   private Document document;

   /**
    * Service d'accès couche DFCE
    */
   private DfceService dfceService;

   /**
    * Constructeur de la classe
    *
    * @param dfceService
    *           services DFCE
    * @param doc
    *           document
    * @param metas
    *           métadonnées
    */
   public DeleteDocsRunnable(final DfceService dfceService, final Document doc){
      super();
      setDocument(doc);
      setDfceService(dfceService);
   }

   private void setDfceService(final DfceService dfceService) {
      this.dfceService = dfceService;
   }

   /**
    * {@inheritDoc}
    */
   public final void run() {

      try {
         // -- Suppression du document en base
         dfceService.getDfceServices().deleteDocument(document.getUuid());

         if(ExecutableMain.DEBUG_MODE) {
            LOGGER.info("DELETED : {}", document.getUuid());
         }

      } catch (final FrozenDocumentException e) {
         final String tpl = "Impossible de supprimer le document gelé : {}";
         LOGGER.warn(tpl, document.getUuid());
      } catch (final Exception e) {
         final String mssg = "Une erreur s'est produite lors de la suppression du document {}: {}";
         LOGGER.error(mssg, document.getUuid(), e.getMessage());
      }
   }

   /**
    * Permet de récupérer l'objet Document.
    *
    * @return Document
    */
   public final Document getDocument() {
      return document;
   }

   /**
    * Permet de modifier l'objet Document.
    *
    * @param document
    *           document DFCE
    */
   public final void setDocument(final Document document) {
      this.document = document;
   }
}
