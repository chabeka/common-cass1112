package fr.urssaf.image.sae.batch.documents.executable.multithreading;

import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.service.ServiceProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.FrozenDocumentException;

import fr.urssaf.image.sae.batch.documents.executable.bootstrap.ExecutableMain;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;

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
   public DeleteDocsRunnable(DfceService dfceService, Document doc){
      super();
      setDocument(doc);
      setDfceService(dfceService);
   }

   private void setDfceService(DfceService dfceService) {
      this.dfceService = dfceService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() {
      
      ServiceProvider provider = dfceService.getServiceProvider();
      try {
         // -- Suppresion du document en base
         provider.getStoreService().deleteDocument(document.getUuid());
         
         if(ExecutableMain.DEBUG_MODE)
            LOGGER.info("DELETED : {}", document.getUuid());
         
      } catch (FrozenDocumentException e) {
         String tpl = "Impossible de supprimer le document gelé : {}";
         LOGGER.warn(tpl, document.getUuid());
      } catch (Exception e) {
         String mssg = "Une erreur c'est produite lors de la suppression du document {}: {}";
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
