
package sae.integration.manual;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.environment.Environments;
import sae.integration.util.ModificationUtils;
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

      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR438");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190812", "20190822");
      mainRequest.setVaryingMetadata(varyingMetadata);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
      metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      final boolean simulationMode = true;
      final String newCodeOrga = "UR437";
      modifyOneMetaByIteration(service, simulationMode, fixedMetadatas, varyingMetadata, "CodeOrganismeProprietaire", newCodeOrga);
   }

   @Test
   /**
    * Rattrapage sur UR117 : produit injecté avec le statut "TESTGEN" au lieu de "PRET"
    */
   public void rattrapageUR117_Statut() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "IdTraitementMasse", "70A45EAB-A3A8-4F37-92F8-E66A1A9A83C0");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "TESTGEN");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR117");
      final String newStatut = "PRET";
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20200101", "20200110");
      final boolean simulationMode = true;
      modifyOneMetaByIteration(service, simulationMode, fixedMetadatas, varyingMetadata, "StatutWATT", newStatut);
   }

   @Test
   /**
    * Rattrapage sur UR451 : Beaucoup de CodeOrganismeProprietaire étaient à UR451 au lieu de UR247
    * à cause de l'application Osiris mal paramétrée
    */
   public void rattrapageUR451() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationProductrice", "OSIRIS");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationTraitement", "OSIRIS");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationMetier", "WATT");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR451");
      final String newCodeOrga = "UR247";
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190911", "20190914");
      final boolean simulationMode = true;
      modifyOneMetaByIteration(service, simulationMode, fixedMetadatas, varyingMetadata, "CodeOrganismeProprietaire", newCodeOrga);
   }

   /**
    * Utilise une recherche paginée pour modifier "en masse" une métadonnée sur des documents
    * 
    * @param service
    *           Service d'accès GNT/GNS
    * @param simulationMode
    *           Vrai s'il faut parcourir les documents pour simulation, mais sans faire réellement les modifications
    * @param fixedMetadatas
    *           Les métadonnées fixes pour la recherche par itérateur
    * @param varyingMetadata
    *           La métadonnée variable pour la recherche par itérateur
    * @param metaCodeToModify
    *           Code de la métadonnée à modifier
    * @param newMetaValue
    *           La valeur à mettre dans la métadonnée à modifier
    */
   private void modifyOneMetaByIteration(final SaeServicePortType service, final boolean simulationMode,
         final ListeMetadonneeType fixedMetadatas, final RangeMetadonneeType varyingMetadata, final String metaCodeToModify, final String newMetaValue) {

      final RechercheParIterateurRequestType searchRequest = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      mainRequest.setFixedMetadatas(fixedMetadatas);
      mainRequest.setVaryingMetadata(varyingMetadata);
      searchRequest.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      searchRequest.setMetadonnees(metadataToReturn);
      searchRequest.setNbDocumentsParPage(200);

      // Boucle sur les différents documents
      int counter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(searchRequest);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            final ListeMetadonneeType meta = doc.getMetadonnees();
            final String dateArchivage = SoapHelper.getMetaValue(meta, "DateArchivage");
            LOGGER.debug("{} {}", UUID, dateArchivage);

            if (!simulationMode) {
               // Lancement de la modification unitaire pour modifier la méta à modifier
               ModificationUtils.sendModification(service, UUID, metaCodeToModify, newMetaValue);
            }
            counter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         searchRequest.setIdentifiantPage(nextPageId);
      }
      LOGGER.info("Nombre de documents modifiés : {}", counter);
   }
}
