
package sae.integration.manual;

import java.io.PrintStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * Test de la recherche par itérateur, sur le siret, pour OPC
 */
public class RechercheIterateurSiretTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheIterateurSiretTest.class);

   @Test
   /**
    * Recherche par itérateur, sur la production
    */
   public void rechercheDocATransfererTest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNS("http://hwi69progednatgnscot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("Siret", "33005523700060", "33005523700060");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeProprietaire");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(10);

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
      sysout.close();
   }



}
