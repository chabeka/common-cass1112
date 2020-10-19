
package sae.integration.manual;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;

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


   @Test
   /**
    * Suppression des documents de MODCOT 2019 suite à demande
    */
   public void rattrapageMODCOT() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNS("http://hwi69saeweb.cer69.recouv/sae/services/SaeService");

      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();

      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeRND", "2.1.2.3.1");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationProductrice", "MODREV");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationTraitement", "MODREV");
      SoapBuilder.addMeta(fixedMetadatas, "ApplicationMetier", "MODREV");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190101", "20191231");
      mainRequest.setVaryingMetadata(varyingMetadata);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeProprietaire");
      metadataToReturn.getMetadonneeCode().add("CodeRND");
      metadataToReturn.getMetadonneeCode().add("Titre");
      metadataToReturn.getMetadonneeCode().add("NumeroCompteExterne");
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


   @Test
   /**
    * Suppression de document à partir d'une liste d'UUID
    */
   public void suppressionParListeUUID() throws Exception {
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final boolean simulationMode = false;

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      BufferedReader reader = null;
      String ligne;
      try {
         reader = new BufferedReader(new FileReader("c:/temp/liste_uuid.csv"));
      }
      catch (final FileNotFoundException exc) {
         LOGGER.error("Erreur d'ouverture du ficher");
      }

      int cpt = 0;
      while ((ligne = reader.readLine()) != null) {
         // LOGGER.debug("{}", ligne);
         sysout.println(ligne);

         if (!simulationMode) {
            // Lancement de la suppression unitaire
            CleanHelper.deleteOneDocument(service, ligne);
         }
         cpt++;
         // LOGGER.info("Nombre de documents parcourus : {}", cpt);
      }
      sysout.close();
      LOGGER.info("Nombre de documents parcourus : {}", cpt);
   }

   @Test
   /**
    * Rattrapage Watt :
    * Suppression d'un document : http://redmine.altair.recouv/issues/489252
    */
   public void suppressionUnitaire() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForWattGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      CleanHelper.deleteOneDocument(service, "D685C969-6684-49DD-ADCE-8FB7282127BC");
   }
}
