
package sae.integration.manual;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sae.integration.util.CleanHelper;
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
 * Exemple d'utilisation de l'itérateur et de la suppression unitaire pour faire de la suppression "de masse"
 */
public class SuppressionRattrapageTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(SuppressionRattrapageTest.class);

   @Test
   /**
    * Rattrapage sur UR247 :
    * Suppression des doc injectés par erreur :code produit PP06, code traitement PP06,
    * Statut du produit PRET sur le code Urssaf UR109 du 24 janvier 2020
    */
   public void rattrapageUR247() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR109");
      SoapBuilder.addMeta(fixedMetadatas, "CodeProduitV2", "PP06");
      SoapBuilder.addMeta(fixedMetadatas, "CodeTraitementV2", "PP06");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20200124", "20200125");
      mainRequest.setVaryingMetadata(varyingMetadata);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
      metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      final boolean simulationMode = true;
      deleteDocumentsByIteration(service, simulationMode, fixedMetadatas, varyingMetadata);
   }


   /**
    * Utilise une recherche paginée pour supprimer "en masse" des documents
    * 
    * @param service
    *           Service d'accès GNT/GNS
    * @param simulationMode
    *           Vrai s'il faut parcourir les documents pour simulation, mais sans faire réellement les modifications
    * @param fixedMetadatas
    *           Les métadonnées fixes pour la recherche par itérateur
    * @param varyingMetadata
    *           La métadonnée variable pour la recherche par itérateur
    */
   private void deleteDocumentsByIteration(final SaeServicePortType service, final boolean simulationMode,
         final ListeMetadonneeType fixedMetadatas, final RangeMetadonneeType varyingMetadata) {

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
               // Lancement de la suppression unitaire
               CleanHelper.deleteOneDocument(service, UUID);
            }
            counter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         searchRequest.setIdentifiantPage(nextPageId);
      }
      LOGGER.info("Nombre de documents supprimés : {}", counter);
   }
}
