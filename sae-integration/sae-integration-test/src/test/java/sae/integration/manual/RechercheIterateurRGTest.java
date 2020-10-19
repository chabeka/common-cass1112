
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
import sae.integration.webservice.modele.RechercheParIterateurV2RequestType;
import sae.integration.webservice.modele.RequetePrincipaleType;
import sae.integration.webservice.modele.ResultatRechercheType;
import sae.integration.webservice.modele.SaeServicePortType;

/**
 * Test de la recherche par itérateur, pour l'application RG
 */
public class RechercheIterateurRGTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheIterateurRGTest.class);

   @Test
   /**
    * Recherche par itérateur
    */
   public void rechercheDocTest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.FRONTAL_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.GNS_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.GNS_INT_CESU.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS("http://hwi31progednatgnscot1boweb.cer31.recouv/ged/services/SaeService/");
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS("http://frontalged.urssaf.recouv/frontalged_be/services/saeService/");
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS("http://hwi69fpgedboappli3.cer69.recouv:8080/frontalged_be/services/saeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");
      rechercheDoc(service, sysout);
      sysout.close();
   }

   private void rechercheDoc(final SaeServicePortType service, final PrintStream sysout) {
      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR117");
      SoapBuilder.addMeta(fixedMetadatas, "CodeRND", "3.1.2.2.1");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20200601", "20250831");
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
            System.out.println("NextPageId :" + nextPageId.getValeur());
         }
         catch (final SOAPFaultException e) {
            LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
            throw e;
         }
      }
      System.out.println("Nombre total de doc trouvés : " + totalCounter);
   }

   @Test
   /**
    * Recherche par itérateur V2 (sur le frontal)
    */
   public void rechercheDocV2Test() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS(Environments.FRONTAL_INT_CLIENT.getUrl());
      final PrintStream sysout = new PrintStream("c:/temp/out.txt");
      rechercheDocV2(service, sysout);
      sysout.close();
   }

   private void rechercheDocV2(final SaeServicePortType service, final PrintStream sysout) {
      final RechercheParIterateurV2RequestType request = new RechercheParIterateurV2RequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR117");
      SoapBuilder.addMeta(fixedMetadatas, "CodeRND", "3.1.2.2.1");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20200601", "20250831");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeProprietaire");
      request.setMetadonnees(metadataToReturn);
      request.setDelai(10);
      request.setCodeOrgaProprietaire("UR117");

      request.setNbDocumentsParPage(50);

      // Boucle sur les différentes pages
      int totalCounter = 0;
      int pageCounter = 0;
      while (true) {
         try {
            final RechercheParIterateurResponseType response = service.rechercheParIterateurV2(request);
            final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
            String dateArchivage = "";
            for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
               final String UUID = doc.getIdArchive();
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
            System.out.println("NextPageId :" + nextPageId.getValeur());
         }
         catch (final SOAPFaultException e) {
            LOGGER.info("Détail de l'exception : {}", SoapHelper.getSoapFaultDetail(e));
            throw e;
         }
      }
      System.out.println("Nombre total de doc trouvés : " + totalCounter);
   }

}
