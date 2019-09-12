
package sae.integration.manual;

import java.io.PrintStream;

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
 * Test de la recherche par itérateur, sur environnement d'intégration client GNT
 */
public class RechercheIterateurWattTest {

   private static final Logger LOGGER = LoggerFactory.getLogger(RechercheIterateurWattTest.class);

   @Test
   /**
    * Reproduit une requête lancée par WATT
    */
   public void rechercheIterateurWattTest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.LOCALHOST.getUrl());

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR827");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");
      addCodeProduitAndCodeTraitement(fixedMetadatas);

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190618", "201906181224");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
      metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      request.setMetadonnees(metadataToReturn);

      // request.setFiltres();
      // request.setIdentifiantPage();
      request.setNbDocumentsParPage(200);

      // Boucle sur les différentes pages
      int counter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         if (nextPageId != null) {
            System.out.println("PageId = " + nextPageId.getValeur());
         }
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            sysout.println(UUID);
            counter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      System.out.println("counter=" + counter);
      sysout.close();
   }

   private void addCodeProduitAndCodeTraitement(final ListeMetadonneeType metadatas) {
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC77A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC80");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC81");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC25B");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC60");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC37A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "L01");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "L00");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC43");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC21");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC65");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC24");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC44");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC45");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC46");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC63");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC64");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC29");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC09");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC03");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC47");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC04");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC48");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC05");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "QC37A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC21A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC66A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PP38A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC10");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC11");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC12");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC64A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC31");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC18");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC19");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "QC66A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC58");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC36");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC14");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC59");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC16");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC16");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC17");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "RP17");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TP17");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TD50");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC01");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC07");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC23");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "RC06");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC06");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC17");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC25");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC20");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC21");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC16");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "RC07");
   }

   @Test
   /**
    * Reproduit une requête lancée par WATT
    */
   public void rechercheIterateurWattTest2() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.GNT_INT_CLIENT.getUrl());
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT(Environments.LOCALHOST.getUrl());

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR827");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");
      SoapBuilder.addMeta(fixedMetadatas, "CodeProduitV2", "PC77A");
      SoapBuilder.addMeta(fixedMetadatas, "CodeProduitV2", "PC66A");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190618", "201906181224");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
      metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      request.setMetadonnees(metadataToReturn);

      // request.setFiltres();
      // request.setIdentifiantPage();
      request.setNbDocumentsParPage(200);

      // Boucle sur les différentes pages
      int counter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         if (nextPageId != null) {
            System.out.println("PageId = " + nextPageId.getValeur());
         }
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            sysout.println(UUID);
            counter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      System.out.println("counter=" + counter);
      sysout.close();

   }

   @Test
   /**
    * Reproduit une requête lancée par WATT, sur GNT_DEV2
    */
   public void rechercheIterateurWattTest3() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_DEV2.getUrl());

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR827");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190622", "20190702");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
      metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(200);

      // Boucle sur les différentes pages
      int counter = 0;
      while (true) {
         LOGGER.info("Avant rechercheParIterateur");
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         LOGGER.info("Après rechercheParIterateur");
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         if (nextPageId != null) {
            System.out.println("PageId = " + nextPageId.getValeur());
         }
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            sysout.println(UUID);
            counter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      System.out.println("counter=" + counter);
      sysout.close();
   }

   private void addCodeProduitAndCodeTraitement3(final ListeMetadonneeType metadatas) {
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC25-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PCA1-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PCA1");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC17-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TD50");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC16-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC23");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC07");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC01");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "RC06");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC06");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC17");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC25");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC20");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC20-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC21");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC06-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC07-FS");

      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC77A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC25B");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC37C");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC60");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC37D");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC21");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC65");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC20");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC29");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC27");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC66A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "T50L0");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "T50L1");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC10");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC54");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC11");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC12");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC13");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC18");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC19");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "QC66A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC14");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC58");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC59");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC16");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC17");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC80");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC81");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC43");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC24");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC87");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC44");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC88");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC45");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC46");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC25B-FS");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC86");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC09");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC03");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC47");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC48");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC04");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC05");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PCA1J");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC21A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC64A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC31");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC36");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC16");
   }

   @Test
   /**
    * Recherche les documents au statut AFFAIRE mais sans numéro d'affaire
    */
   public void rechercheStatutAFFAIRETest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory.getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final String[] organismes = new String[] {"UR727", "UR837", "UR917", "UR747", "UR547", "UR257", "UR200"};
      for (final String codeOrga : organismes) {
         System.out.println("Orga : " + codeOrga);
         rechercheStatutAFFAIREForOneOrga(service, sysout, codeOrga);
      }
      sysout.close();
   }

   private void rechercheStatutAFFAIREForOneOrga(final SaeServicePortType service, final PrintStream sysout, final String orgaToCheck) {
      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", orgaToCheck);
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "AFFAIRE");
      addCodeProduitAndCodeTraitement(fixedMetadatas);

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190101", "20251212");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("NumeroAffaireWATT");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeProprietaire");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(200);

      // Boucle sur les différentes pages
      int emptyCounter = 0;
      int totalCounter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            final ListeMetadonneeType meta = doc.getMetadonnees();
            final String numeroAffaire = SoapHelper.getMetaValue(meta, "NumeroAffaireWATT");
            final String dateArchivage = SoapHelper.getMetaValue(meta, "DateArchivage");
            final String codeOrga = SoapHelper.getMetaValue(meta, "CodeOrganismeProprietaire");
            if (numeroAffaire.isEmpty()) {
               sysout.println(UUID + " - " + codeOrga + " - " + dateArchivage);
               emptyCounter++;
            }
            totalCounter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      System.out.println("Nombre total de doc à l'état AFFAIRE : " + totalCounter);
      System.out.println("Nombre de doc à l'état AFFAIRE sans numéro d'affaire : " + emptyCounter);
   }
}
