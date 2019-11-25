/**
 *
 */
package sae.integration.util;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.data.RandomData;
import sae.integration.data.TestData;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ArchivageUnitairePJResponseType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Classe utilitaire facilitant l'archivage de documents
 */
public final class ArchivageUtils {

   private static final Logger LOGGER = LoggerFactory.getLogger(ArchivageUtils.class);

   private ArchivageUtils() {
      // Classe statique
   }

   /**
    * Lance un archivage unitaire en MTOM, et renvoie l'id du document archivé
    * 
    * @param service
    *           L'accès aux services SAE
    * @param request
    *           La requête d'archivage unitaire
    * @return
    *         L'id document archivé
    */
   public static String sendArchivageUnitaire(final SaeServicePortType service, final ArchivageUnitairePJRequestType request) {
      try {
         // Activation MTOM
         final BindingProvider bp = (BindingProvider) service;
         final SOAPBinding binding = (SOAPBinding) bp.getBinding();
         binding.setMTOMEnabled(true);
         // Lancement
         final ArchivageUnitairePJResponseType response = service.archivageUnitairePJ(request);
         // récupération du résultat
         final String docId = response.getIdArchive();
         return docId;
      }
      catch (final SOAPFaultException e) {
         LOGGER.warn(e.getMessage());
         LOGGER.warn("Détail : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }

   }

   /**
    * Lance un archivage unitaire en MTOM, et renvoie l'id du document archivé
    * 
    * @param service
    *           L'accès aux services SAE
    * @param request
    *           La requête d'archivage unitaire
    * @param cleanHelper
    *           Garde l'id du document archivé, pour faciliter le nettoyage en fin de test
    * @return
    *         L'id document archivé
    */
   public static String sendArchivageUnitaire(final SaeServicePortType service, final ArchivageUnitairePJRequestType request, final CleanHelper cleanHelper) {
      final String docId = sendArchivageUnitaire(service, request);
      cleanHelper.addDocumentToDelete(docId);
      return docId;
   }

   /**
    * Archive un document PDF de test, avec métadonnées aléatoires
    * 
    * @param service
    *           L'accès aux services SAE
    * @return
    *         L'id document archivé
    */
   public static String archivagePDF(final SaeServicePortType service) {
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      final ListeMetadonneeType metaList = RandomData.getRandomMetadatas();
      request.setMetadonnees(metaList);
      request.setDataFile(TestData.getPdfFile(metaList));
      return sendArchivageUnitaire(service, request);
   }

   /**
    * Archive un document Tiff de test, avec métadonnées aléatoires
    * 
    * @param service
    *           L'accès aux services SAE
    * @return
    *         L'id document archivé
    */
   public static String archivageTiff(final SaeServicePortType service) {
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      final ListeMetadonneeType metaList = RandomData.getRandomMetadatas();
      request.setMetadonnees(metaList);
      request.setDataFile(TestData.getTiffFile(metaList));
      return sendArchivageUnitaire(service, request);
   }

   /**
    * Archive un document TXT de test, avec métadonnées aléatoires
    * 
    * @param service
    *           L'accès aux services SAE
    * @return
    *         L'id document archivé
    */
   public static String archivageTxt(final SaeServicePortType service) {
      final ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      final ListeMetadonneeType metaList = RandomData.getRandomMetadatas();
      request.setMetadonnees(metaList);
      request.setDataFile(TestData.getTxtFile(metaList));
      return sendArchivageUnitaire(service, request);
   }

}
