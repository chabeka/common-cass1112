
package sae.integration.manual;

import java.util.List;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.ArchivageUtils;
import sae.integration.util.SoapBuilder;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.ArchivageUnitairePJRequestType;
import sae.integration.webservice.modele.ConsultationMTOMRequestType;
import sae.integration.webservice.modele.ConsultationMTOMResponseType;
import sae.integration.webservice.modele.DataFileType;
import sae.integration.webservice.modele.ListeMetadonneeCodeType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Récupère un document de la production, et le copie sur un environnement d'intégration
 */
public class ConsultationAndCopyTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ConsultationAndCopyTest.class);

   @Test
   public void consultationAndCopyForSylvainTest() throws Exception {
      // Environnement source
      final SaeServicePortType sourceService = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      // Environnement destination
      final SaeServicePortType destSservice = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_CLIENT.getUrl());

      // Id du document à récupérer
      final String docId = "49afb93a-28d7-4ab8-88a9-722cd47b29f4";

      // Liste des métadonnées à récupérer et copier
      final ListeMetadonneeCodeType metadatas = new ListeMetadonneeCodeType();
      final List<String> metadataList = metadatas.getMetadonneeCode();
      metadataList.add("ApplicationMetier");
      metadataList.add("ApplicationProductrice");
      metadataList.add("ApplicationTraitement");
      metadataList.add("CodeBaseV2");
      metadataList.add("CodeCategorieV2");
      metadataList.add("CodeDocument");
      metadataList.add("CodeOrganismeGestionnaire");
      metadataList.add("CodeOrganismeProprietaire");
      metadataList.add("CodeRND");
      metadataList.add("DateCreation");
      metadataList.add("DateReception");
      metadataList.add("Denomination");
      metadataList.add("FormatFichier");
      metadataList.add("Hash");
      metadataList.add("IdGed");
      metadataList.add("NbPages");
      metadataList.add("NomFichier");
      metadataList.add("NumeroAffaireWATT");
      metadataList.add("NumeroCompteExterne");
      metadataList.add("NumeroGroupe");
      metadataList.add("NumeroInterneSalarie");
      metadataList.add("NumeroPersonne");
      metadataList.add("Siren");
      metadataList.add("Siret");
      metadataList.add("SiteGestion");
      metadataList.add("StatutWATT");
      metadataList.add("Titre");
      metadataList.add("TypeHash");

      // Lancement de la copie
      consultAndCopyDocument(sourceService, destSservice, docId, metadatas);
   }

   /**
    * Permet de consulter un document et ses métadonnées sur en environnement, et de l'archiver sur un autre
    * 
    * @param sourceService
    *           environnement source
    * @param destSservice
    *           environnement destination
    * @param docId
    *           Id du document à récupérer
    * @param metadatas
    *           Liste des métas à récupérer
    * @throws Exception
    */
   public void consultAndCopyDocument(final SaeServicePortType sourceService, final SaeServicePortType destSservice, final String docId,
         final ListeMetadonneeCodeType metadatas) throws Exception {

      final ConsultationMTOMRequestType request = new ConsultationMTOMRequestType();
      request.setIdArchive(docId);
      request.setMetadonnees(metadatas);

      // Lancement de la récupération du document
      ConsultationMTOMResponseType response;
      try {
         LOGGER.info("Lancement de la consultation du document");
         response = sourceService.consultationMTOM(request);
         LOGGER.info("Taille du document : {}", IOUtils.readBytesFromStream(response.getContenu().getInputStream()).length);
      }
      catch (final SOAPFaultException e) {
         LOGGER.info("Exception reçue : {}", e.getMessage());
         LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
         throw e;
      }

      // Archivage du document récupéré
      final ArchivageUnitairePJRequestType archivageRequest = new ArchivageUnitairePJRequestType();
      final ListeMetadonneeType archivageMeta = SoapBuilder.cloneListeMetadonnee(response.getMetadonnees());
      SoapBuilder.deleteMeta(archivageMeta, "NomFichier");
      archivageRequest.setMetadonnees(archivageMeta);
      final DataFileType dataFile = new DataFileType();
      dataFile.setFile(response.getContenu());
      dataFile.setFileName(SoapHelper.getMetaValue(response.getMetadonnees(), "NomFichier"));
      archivageRequest.setDataFile(dataFile);
      LOGGER.info("Lancement de l'archivage du document");
      final String uuid = ArchivageUtils.sendArchivageUnitaire(destSservice, archivageRequest);
      Assert.assertEquals(docId.toLowerCase(), uuid.toLowerCase());
      LOGGER.info("Archivage terminé");
   }
}