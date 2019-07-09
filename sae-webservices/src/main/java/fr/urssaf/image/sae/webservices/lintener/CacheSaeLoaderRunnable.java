package fr.urssaf.image.sae.webservices.lintener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.storage.dfce.bo.DocumentsTypeList;
import fr.urssaf.image.sae.storage.dfce.bo.IndexCompositeComponent;

/**
 * Thread permettant de charger la liste des type de documents et index composites suite
 * au démarrage de DFCE
 */
public class CacheSaeLoaderRunnable implements Runnable {

   private final DocumentsTypeList typeList;

   private final DFCEServices dfceServices;

   private final IndexCompositeComponent indexCompositeComponent;

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
                                                     .getLogger(CacheSaeLoaderRunnable.class);

   public CacheSaeLoaderRunnable(final DFCEServices dfceServices, final DocumentsTypeList typeList, final IndexCompositeComponent indexCompositeComponent) {
      this.dfceServices = dfceServices;
      this.typeList = typeList;
      this.indexCompositeComponent = indexCompositeComponent;
   }

   @Override
   public void run() {

      LOGGER.debug("Début du chargement des types de documents et index composites en cache");
      try {
         // On attend que DFCE soit démarré
         while (true) {
            try {
               dfceServices.reconnect();
               // C'est bon, DFCE est dispo, on peut continuer
               break;
            }
            catch (final Exception ex) {
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
         LOGGER.debug("Chargement des types de documents...");
         typeList.getTypes();
         LOGGER.debug("Chargement des index composites...");
         indexCompositeComponent.getIndexCompositeList();

      }
      finally {
         LOGGER.debug("Fin de chargement des types de documents");
      }
   }

}
