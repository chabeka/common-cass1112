package fr.urssaf.image.sae.webservices.lintener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.exception.DocumentTypeException;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;

/**
 * Thread permettant de charger la liste des type de documents suite 
 * au démarrage de DFCE
 *
 */
public class DocumentTypeLoaderRunnable implements Runnable {
   
   private DocumentsTypeList typeList;

   private DFCEServicesManager dfceService;
   
   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocumentTypeLoaderRunnable.class);
   
   public DocumentTypeLoaderRunnable(DFCEServicesManager dfceServiceManager, DocumentsTypeList typeList) {
      this.dfceService = dfceServiceManager;
      this.typeList = typeList;
   }

   @Override
   public void run() {
      
      try {
         while (!dfceService.isActive()) {
            //Thread.currentThread();
            dfceService.getConnection();
         }
         if(dfceService.isActive()){
            if(dfceService.getDFCEService() !=null) {
               typeList.loadDocumentTypeList();
            }
         }
      }  catch (ConnectionServiceEx exception) {
         throw new DocumentTypeException("impossible de se connecter à DFCE", exception);
      } finally {
         LOGGER.debug("Fin de chargement des types de documents:");
      }
   }

}
