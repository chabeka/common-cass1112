
package sae.integration.manual;

import java.io.PrintStream;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.SoapBuilder;
import sae.integration.util.SoapHelper;
import sae.integration.webservice.factory.SaeServiceStubFactory;
import sae.integration.webservice.modele.IdentifiantPageType;
import sae.integration.webservice.modele.ListeMetadonneeCodeType;
import sae.integration.webservice.modele.ListeMetadonneeType;
import sae.integration.webservice.modele.RangeMetadonneeType;
import sae.integration.webservice.modele.RechercheParIterateurRequestType;
import sae.integration.webservice.modele.RechercheParIterateurResponseType;
import sae.integration.webservice.modele.RequetePrincipaleType;
import sae.integration.webservice.modele.ResultatRechercheType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test de la recherche par itérateur, pour l'application Scribe
 */
public class RechercheIterateurScribeTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheIterateurScribeTest.class);

   @Test
   /**
    * Recherche par itérateur, sur la production
    */
   public void rechercheDocATransfererTest() throws Exception {
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT("http://frontalged.urssaf.recouv/frontalged_be/services/saeService/");
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT("http://frontalint2.gidn.recouv/frontalged_be/services/saeService/");
      // final SaeServicePortType service = SaeServiceStubFactory
      // .getServiceForRechercheDocumentaireGNT("http://hwi69int2pgedboint2.gidn.recouv:8080/frontalged_be/services/saeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");
      rechercheDocATransferer(service, sysout);
      sysout.close();
   }

   private void rechercheDocATransferer(final SaeServicePortType service, final PrintStream sysout) {
      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationProductrice", "SCRIBE");
      SoapBuilder.addMeta(fixedMetadatas, "ATransfererScribe", "true");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20200419", "20200421235959");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeProprietaire");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(50);

      // Boucle sur les différentes pages
      int totalCounter = 0;
      int pageCounter = 0;
      while (true) {
         try {
            final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
            final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
            String dateArchivage = "";
            for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
               final String UUID = doc.getIdArchive();
               System.out.println("UUID=" + UUID);
               final ListeMetadonneeType meta = doc.getMetadonnees();
               dateArchivage = SoapHelper.getMetaValue(meta, "DateArchivage");
               final String codeOrga = SoapHelper.getMetaValue(meta, "CodeOrganismeProprietaire");
               totalCounter++;
            }
            if (response.isDernierePage()) {
               break;
            }
            request.setIdentifiantPage(nextPageId);
            pageCounter++;
            System.out.println("Page n°" + pageCounter + " - dateArchivage=" + dateArchivage + " - totalCounter=" + totalCounter);
         }
         catch (final SOAPFaultException e) {
            LOGGER.info("Exception reçue : {}", e.getMessage());
            LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
            throw e;
         }
      }
      System.out.println("Nombre total de doc trouvés : " + totalCounter);
   }

   private void rechercheDocWatt(final SaeServicePortType service, final PrintStream sysout, final String orgaToCheck) {
      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", orgaToCheck);
      // SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "EN ATTENTE");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "OK");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190101", "20251212");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeProprietaire");
      metadataToReturn.getMetadonneeCode().add("ContratDeService");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(50);

      // Boucle sur les différentes pages
      int totalCounter = 0;
      int pageCounter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         String dateArchivage = "";
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            final ListeMetadonneeType meta = doc.getMetadonnees();
            dateArchivage = SoapHelper.getMetaValue(meta, "DateArchivage");
            final String codeOrga = SoapHelper.getMetaValue(meta, "CodeOrganismeProprietaire");
            final String cs = SoapHelper.getMetaValue(meta, "ContratDeService");
            System.out.println(UUID + "- " + cs);
            totalCounter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
         pageCounter++;
         System.out.println("Page n°" + pageCounter + " - dateArchivage=" + dateArchivage + " - totalCounter=" + totalCounter);
      }
      System.out.println("Nombre total de doc trouvés : " + totalCounter);
   }

}
