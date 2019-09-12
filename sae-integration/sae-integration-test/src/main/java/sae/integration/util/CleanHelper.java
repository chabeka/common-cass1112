package sae.integration.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.webservice.modele.SaeServicePortType;
import sae.integration.webservice.modele.SuppressionRequestType;

/**
 * Classe facilitant le nettoyage des documents archivés pour les besoins des tests
 */
public class CleanHelper implements AutoCloseable {

   private static final Logger LOGGER = LoggerFactory.getLogger(CleanHelper.class);

   private final List<String> docsToDelete = new ArrayList<>();

   private final SaeServicePortType service;

   public CleanHelper(final SaeServicePortType service) {
      this.service = service;
   }

   public void addDocumentToDelete(final String docId) {
      docsToDelete.add(docId);
   }

   public static void deleteOneDocument(final SaeServicePortType service, final String docId) {
      final SuppressionRequestType request = new SuppressionRequestType();
      request.setUuid(docId);
      try {
         service.suppression(request);
      }
      catch (final SOAPFaultException e) {
         if (e.getMessage().contains("Il n'existe aucun document pour l'identifiant d'archivage")) {
            LOGGER.debug("Nettoyage : " + e.getMessage());
         } else {
            LOGGER.warn("Erreur lors de la tentative de nettoyage du document {} : {}", docId, e.getMessage());
         }
      }
   }

   private void doCleanup() {
      for (final Iterator<String> it = docsToDelete.iterator(); it.hasNext();) {
         final String docId = it.next();
         try {
            LOGGER.debug("Suppression du doc {}", docId);
            deleteOneDocument(service, docId);
            it.remove();
         }
         catch (final Exception e) {
            LOGGER.warn("Erreur lors du nettoyage du doc " + docId, e);
         }
      }
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void close() throws Exception {
      doCleanup();
   }

}
