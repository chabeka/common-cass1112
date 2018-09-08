package fr.urssaf.image.sae.webservices.lintener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;

/**
 * Thread permettant de charger la liste des type de documents suite
 * au démarrage de DFCE
 *
 */
public class DocumentTypeLoaderRunnable implements Runnable {

   private final DocumentsTypeList typeList;

   private final DFCEServices dfceServices;

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DocumentTypeLoaderRunnable.class);

   public DocumentTypeLoaderRunnable(final DFCEServices dfceServices, final DocumentsTypeList typeList) {
      this.dfceServices = dfceServices;
      this.typeList = typeList;
   }

   @Override
   public void run() {

      LOGGER.debug("Début du chargement des types de documents");
      try {
         // On attend que DFCE soit démarré
         while (true) {
            try {
               dfceServices.reconnect();
               // C'est bon, DFCE est dispo, on peut continuer
               break;
            }
            catch (final Throwable ex) {
               // DFCE n'est pas dispo... on attend un peu avant de retenter...
               LOGGER.warn("DocumentTypeLoaderRunnable : En attente de la disponibilité de DFCE...");
               try {
                  Thread.sleep(1000);
               }
               catch (final InterruptedException e) {
                  break;
               }
            }
         }
         // Chargement de la liste des types de documents
         typeList.loadDocumentTypeList();
      } finally {
         LOGGER.debug("Fin de chargement des types de documents");
      }
   }

}
