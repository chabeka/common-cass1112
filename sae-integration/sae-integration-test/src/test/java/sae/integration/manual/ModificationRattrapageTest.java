
package sae.integration.manual;

import java.io.PrintStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.ModificationUtils;
import sae.integration.util.SoapBuilder;
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
 * Exemple d'utilisation de l'itérateur et de la modification unitaire pour faire de la modification "de masse"
 */
public class ModificationRattrapageTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(ModificationRattrapageTest.class);

   @Test
   /**
    * Rattrapage sur UR437 : Beaucoup de CodeOrganismeProprietaire étaient à UR438 au lieu de UR437
    * à cause de COLD mal paramétrés
    */
   public void rattrapageUR438() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_DEV2.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PrintStream sysout = new PrintStream("d:/temp/out.txt");

      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR438");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190812", "20190822");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
      metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(200);
      final boolean simulationMode = true;

      // Boucle sur les différents documents CodeOrganismeProprietaire=UR438 au Statut PRET
      int counter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            sysout.println(UUID);

            if (!simulationMode) {
               // Lancement de la modification unitaire pour mettre CodeOrganismeProprietaire à UR437
               ModificationUtils.sendModification(service, UUID, "CodeOrganismeProprietaire", "UR437");
            }
            counter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      LOGGER.info("Nombre de documents modifiés : {}", counter);
      sysout.close();
   }

}
