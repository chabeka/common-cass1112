/**
 *
 */
package sae.integration.util;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.webservice.modele.ConsultationMTOMRequestType;
import sae.integration.webservice.modele.ConsultationMTOMResponseType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.MetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Classe utilitaire facilitant la vérification de l'archivage des documents
 */
public final class ArchivageValidationUtils {

   private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageValidationUtils.class);

   private ArchivageValidationUtils() {
      // Classe statique
   }

   /**
    * Vérifie que le document archivé est bien arrivé, est consultable, et que ses métadonnées sont bonnes
    * 
    * @param service
    *           Accès au service GNT/GNS, permettant de récupérer le document
    * @param docId
    *           Id du document à vérifier
    * @param expectedMetas
    *           Les métadonnées attendues sur le document
    */
   public static void validateDocument(final SaeServicePortType service, final String docId, final ListeMetadonneeType expectedMetas) {
      final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
      request.setIdArchive(docId);
      request.setMetadonnees(SoapBuilder.extractMetaCodeList(expectedMetas));
      LOGGER.info("Récupération du document");
      final ConsultationMTOMResponseType response = service.consultationMTOM(request);
      final ListeMetadonneeType metaFromConsultation = response.getMetadonnees();
      final String sha1FromConsultation = DigestUtils.sha1Hex(response.getContenu());
      LOGGER.info("Vérification du hash du document récupéré");
      Assert.assertEquals("Vérification du hash", SoapHelper.getMetaValue(expectedMetas, "Hash"), sha1FromConsultation);
      LOGGER.info("Vérification des métadonnées du document récupéré");
      validateMeta(expectedMetas, metaFromConsultation);
   }

   private static void validateMeta(final ListeMetadonneeType expectedMeta, final ListeMetadonneeType realMeta) {
      for (final MetadonneeType meta : expectedMeta.getMetadonnee()) {
         final String metaCode = meta.getCode();
         final String expectedValue = meta.getValeur();
         final String realValue = SoapHelper.getMetaValue(realMeta, metaCode);
         LOGGER.debug("Méta : {} - expected={} - real={}", metaCode, expectedValue, realValue);
         Assert.assertEquals("Vérification de la méta " + metaCode, expectedValue, realValue);
      }
   }

   /**
    * Indique si le document existe
    * 
    * @param service
    *           service GNS ou GNT
    * @param docId
    *           id du document recherché
    * @return
    *         vrai si le document existe, faux sinon
    */
   public static boolean docExists(final SaeServicePortType service, final String docId) {
      try {
         final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
         request.setIdArchive(docId);
         final ConsultationMTOMResponseType response = service.consultationMTOM(request);
         return response.getContenu().length > 0;
      }
      catch (final SOAPFaultException e) {
         if (e.getMessage().contains("Il n'existe aucun document pour l'identifiant d'archivage")) {
            return false;
         }
         throw e;
      }
   }
}
