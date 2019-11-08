
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
import sae.integration.webservice.modele.MetadonneeType;
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
      // final SaeServicePortType service = SaeServiceStubFactory.getServiceForDevToutesActions(Environments.GNT_INT_PAJE.getUrl());

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
    * Reproduit une requête lancée par WATT
    */
   public void rechercheIterateurWattTest4() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", "UR827");
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");
      addCodeProduitAndCodeTraitement4(fixedMetadatas);

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190915", "20190925");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("LoginBonAPayer");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeGestionnaire");
      metadataToReturn.getMetadonneeCode().add("LoginServiceFait");
      metadataToReturn.getMetadonneeCode().add("DateCourrierV2");
      metadataToReturn.getMetadonneeCode().add("NumeroCompteExterne");
      metadataToReturn.getMetadonneeCode().add("ATransfererScribe");
      metadataToReturn.getMetadonneeCode().add("RUM");
      metadataToReturn.getMetadonneeCode().add("CodeTraitementV2");
      metadataToReturn.getMetadonneeCode().add("NumeroIdArchivage");
      metadataToReturn.getMetadonneeCode().add("IdGed");
      metadataToReturn.getMetadonneeCode().add("NumeroCompteBancaire");
      metadataToReturn.getMetadonneeCode().add("CodeSousCategorieV2");
      metadataToReturn.getMetadonneeCode().add("PortefeuilleComptable");
      metadataToReturn.getMetadonneeCode().add("NumeroDevisSicomor");
      metadataToReturn.getMetadonneeCode().add("DateDebutConservation");
      metadataToReturn.getMetadonneeCode().add("Note");
      metadataToReturn.getMetadonneeCode().add("LoginControleComptable");
      metadataToReturn.getMetadonneeCode().add("NumeroVoletSocial");
      metadataToReturn.getMetadonneeCode().add("NumeroOrdre");
      metadataToReturn.getMetadonneeCode().add("NumeroFactureFournisseur");
      metadataToReturn.getMetadonneeCode().add("CodeActivite");
      metadataToReturn.getMetadonneeCode().add("ApplicationProductrice");
      metadataToReturn.getMetadonneeCode().add("DateReception");
      metadataToReturn.getMetadonneeCode().add("DemandeurAchat");
      metadataToReturn.getMetadonneeCode().add("NumeroDevisFournisseur");
      metadataToReturn.getMetadonneeCode().add("NumeroRecours");
      metadataToReturn.getMetadonneeCode().add("NumeroNotification");
      metadataToReturn.getMetadonneeCode().add("AnneeExercice");
      metadataToReturn.getMetadonneeCode().add("SiteGestion");
      metadataToReturn.getMetadonneeCode().add("Circuit");
      metadataToReturn.getMetadonneeCode().add("Titre");
      metadataToReturn.getMetadonneeCode().add("ControleComptable");
      metadataToReturn.getMetadonneeCode().add("DateLivraison");
      metadataToReturn.getMetadonneeCode().add("CodeBanque");
      metadataToReturn.getMetadonneeCode().add("ReferenceDossier");
      metadataToReturn.getMetadonneeCode().add("CodePartenaire");
      metadataToReturn.getMetadonneeCode().add("SiteComptable");
      metadataToReturn.getMetadonneeCode().add("NumeroLot");
      metadataToReturn.getMetadonneeCode().add("NomSalarie");
      metadataToReturn.getMetadonneeCode().add("Montant2");
      metadataToReturn.getMetadonneeCode().add("DelaiPresenceRMR");
      metadataToReturn.getMetadonneeCode().add("NumeroGroupe");
      metadataToReturn.getMetadonneeCode().add("DelaiNbEcheance");
      metadataToReturn.getMetadonneeCode().add("CodeAgent");
      metadataToReturn.getMetadonneeCode().add("DateAdmission");
      metadataToReturn.getMetadonneeCode().add("NumeroBonLivraison");
      metadataToReturn.getMetadonneeCode().add("NumeroCheque");
      metadataToReturn.getMetadonneeCode().add("NomFournisseur");
      metadataToReturn.getMetadonneeCode().add("PrenomAgent");
      metadataToReturn.getMetadonneeCode().add("MontantRegle");
      metadataToReturn.getMetadonneeCode().add("CodeProduitV2");
      metadataToReturn.getMetadonneeCode().add("NomFichier");
      metadataToReturn.getMetadonneeCode().add("CourrielCotisant");
      metadataToReturn.getMetadonneeCode().add("TailleFichier");
      metadataToReturn.getMetadonneeCode().add("CodeAgence");
      metadataToReturn.getMetadonneeCode().add("CodeDocument");
      metadataToReturn.getMetadonneeCode().add("ApplicationMetier");
      metadataToReturn.getMetadonneeCode().add("NumeroPli");
      metadataToReturn.getMetadonneeCode().add("NumeroPersonne");
      metadataToReturn.getMetadonneeCode().add("SiteAcquisition");
      metadataToReturn.getMetadonneeCode().add("DateServiceFait");
      metadataToReturn.getMetadonneeCode().add("MoisPaie");
      metadataToReturn.getMetadonneeCode().add("DateCreation");
      metadataToReturn.getMetadonneeCode().add("NumeroStructure");
      metadataToReturn.getMetadonneeCode().add("NumeroCompteInterne");
      metadataToReturn.getMetadonneeCode().add("DateEffet");
      metadataToReturn.getMetadonneeCode().add("ReferenceDocumentaire");
      metadataToReturn.getMetadonneeCode().add("SousPortefeuilleComptable");
      metadataToReturn.getMetadonneeCode().add("Siren");
      metadataToReturn.getMetadonneeCode().add("DateBonAPayer");
      metadataToReturn.getMetadonneeCode().add("DateControleComptable");
      metadataToReturn.getMetadonneeCode().add("NumeroTiers");
      metadataToReturn.getMetadonneeCode().add("Libre1");
      metadataToReturn.getMetadonneeCode().add("MontantCheque");
      metadataToReturn.getMetadonneeCode().add("CodeBureau");
      metadataToReturn.getMetadonneeCode().add("DateJourneeComptable");
      metadataToReturn.getMetadonneeCode().add("NumeroAffaireWATT");
      metadataToReturn.getMetadonneeCode().add("ServiceFait");
      metadataToReturn.getMetadonneeCode().add("Evenement");
      metadataToReturn.getMetadonneeCode().add("DatePaiement");
      metadataToReturn.getMetadonneeCode().add("DateFinConservation");
      metadataToReturn.getMetadonneeCode().add("NumeroInterneSalarie");
      metadataToReturn.getMetadonneeCode().add("ReferenceDossierDUE");
      metadataToReturn.getMetadonneeCode().add("DureeConservation");
      metadataToReturn.getMetadonneeCode().add("NumeroCommande");
      metadataToReturn.getMetadonneeCode().add("NumeroDemandeAchat");
      metadataToReturn.getMetadonneeCode().add("Libre2");
      metadataToReturn.getMetadonneeCode().add("DocFormatOrigine");
      metadataToReturn.getMetadonneeCode().add("NumeroIntControle");
      metadataToReturn.getMetadonneeCode().add("Denomination");
      metadataToReturn.getMetadonneeCode().add("DateNaissanceSalarie");
      metadataToReturn.getMetadonneeCode().add("NumeroBonReception");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("RIBA");
      metadataToReturn.getMetadonneeCode().add("NomPatronymiqueAgent");
      metadataToReturn.getMetadonneeCode().add("Periode");
      metadataToReturn.getMetadonneeCode().add("DateEtHeureEnvoi");
      metadataToReturn.getMetadonneeCode().add("CodeRND");
      metadataToReturn.getMetadonneeCode().add("NbPages");
      metadataToReturn.getMetadonneeCode().add("JetonDePreuve");
      metadataToReturn.getMetadonneeCode().add("NumeroPiece");
      metadataToReturn.getMetadonneeCode().add("CodeBaseV2");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeProprietaire");
      metadataToReturn.getMetadonneeCode().add("CodeFonction");
      metadataToReturn.getMetadonneeCode().add("NumeroEngagement");
      metadataToReturn.getMetadonneeCode().add("ApplicationTraitement");
      metadataToReturn.getMetadonneeCode().add("IdTraitementMasse");
      metadataToReturn.getMetadonneeCode().add("PseudoSiret");
      metadataToReturn.getMetadonneeCode().add("DateCommande");
      metadataToReturn.getMetadonneeCode().add("FormatFichier");
      metadataToReturn.getMetadonneeCode().add("DateLimite");
      metadataToReturn.getMetadonneeCode().add("ParametreNatureProduitV2");
      metadataToReturn.getMetadonneeCode().add("NomMaritalSalarie");
      metadataToReturn.getMetadonneeCode().add("Siret");
      metadataToReturn.getMetadonneeCode().add("ReferenceApplicationProductrice");
      metadataToReturn.getMetadonneeCode().add("DateDevisFournisseur");
      metadataToReturn.getMetadonneeCode().add("NniEmployeur");
      metadataToReturn.getMetadonneeCode().add("NumeroCompteComptable");
      metadataToReturn.getMetadonneeCode().add("SiteAgent");
      metadataToReturn.getMetadonneeCode().add("PrenomSalarie");
      metadataToReturn.getMetadonneeCode().add("DateSignature");
      metadataToReturn.getMetadonneeCode().add("CodeCategorieV2");
      metadataToReturn.getMetadonneeCode().add("Objet");
      metadataToReturn.getMetadonneeCode().add("NumeroMatriculeAgent");
      metadataToReturn.getMetadonneeCode().add("NniSalarie");
      metadataToReturn.getMetadonneeCode().add("NumeroFactureSicomor");
      metadataToReturn.getMetadonneeCode().add("DateFacture");
      metadataToReturn.getMetadonneeCode().add("Hash");
      metadataToReturn.getMetadonneeCode().add("BonAPayer");
      metadataToReturn.getMetadonneeCode().add("TypeHash");
      metadataToReturn.getMetadonneeCode().add("StatutWATT");
      metadataToReturn.getMetadonneeCode().add("FlagRattachementDossier");
      metadataToReturn.getMetadonneeCode().add("ReferenceDossierAM");
      metadataToReturn.getMetadonneeCode().add("CodeOrganismeEmetteurCommande");
      metadataToReturn.getMetadonneeCode().add("Montant1");
      metadataToReturn.getMetadonneeCode().add("MontantDevis");
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
            /*
            sysout.println(counter + " " + UUID);
            if (counter >= 1818 && counter <= 1821) {
               dumpMeta(sysout, doc.getMetadonnees());
            }
             */
            checkNumeric(sysout, UUID, doc.getMetadonnees(), "NumeroStructure");

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

   /**
    * @param sysout
    * @param metadonnees
    */
   private void dumpMeta(final PrintStream sysout, final ListeMetadonneeType metadonnees) {
      for (final MetadonneeType meta : metadonnees.getMetadonnee()) {
         sysout.println("   " + meta.getCode() + "=" + meta.getValeur());
      }
   }


   private void addCodeProduitAndCodeTraitement4(final ListeMetadonneeType metadatas) {
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC25-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PCA1-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TCP6-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PCA1");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC17-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TD50");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC16-FS");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC23");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC07");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC01");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "RC06");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC18");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC06");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC17");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TC28");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "TCP6");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC25");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC20");
      SoapBuilder.addMeta(metadatas, "CodeTraitementV2", "PC20-FS");

      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC77A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC80");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC25B");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC37C");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC81");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC60");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC37D");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC59A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC43");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC21");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC65");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC24");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC44");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC45");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC46");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC20");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC29");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC09");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC03");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC47");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC48");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC04");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC05");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC27");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC18A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PCA1J");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC21A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC66A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "T50L0");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "T50L1");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC10");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC54");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC11");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC12");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC13");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC64A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC31");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC18");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC19");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "QC66A");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC36");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC14");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC58");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC59");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "PC16");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC16");
      SoapBuilder.addMeta(metadatas, "CodeProduitV2", "NC17");
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

   @Test
   /**
    * Recherche les documents au statut PRET, mais qui ont des caractères non numériques
    * dans des méta considérées comme numériques par WATT
    */
   public void rechercheStatutPRET_withInvalidFormatTest() throws Exception {
      final SaeServicePortType service = SaeServiceStubFactory
            .getServiceForRechercheDocumentaireGNT("http://hwi69progednatgntcot1boweb1.cer69.recouv/ged/services/SaeService/");

      final PrintStream sysout = new PrintStream("c:/temp/out.txt");

      final String[] organismes = new String[] {"UR827"};
      for (final String codeOrga : organismes) {
         System.out.println("Orga : " + codeOrga);
         rechercheStatutPRET_withInvalidFormatForOneOrga(service, sysout, codeOrga);
      }
      sysout.close();
   }

   private void rechercheStatutPRET_withInvalidFormatForOneOrga(final SaeServicePortType service, final PrintStream sysout, final String orgaToCheck) {
      final RechercheParIterateurRequestType request = new RechercheParIterateurRequestType();
      final RequetePrincipaleType mainRequest = new RequetePrincipaleType();
      final ListeMetadonneeType fixedMetadatas = new ListeMetadonneeType();
      SoapBuilder.addMeta(fixedMetadatas, "DomaineCotisant", "true");
      SoapBuilder.addMeta(fixedMetadatas, "CodeOrganismeProprietaire", orgaToCheck);
      SoapBuilder.addMeta(fixedMetadatas, "StatutWATT", "PRET");
      addCodeProduitAndCodeTraitement(fixedMetadatas);

      mainRequest.setFixedMetadatas(fixedMetadatas);
      final RangeMetadonneeType varyingMetadata = SoapBuilder.buildRangeMetadata("DateArchivage", "20190920", "20251212");
      mainRequest.setVaryingMetadata(varyingMetadata);
      request.setRequetePrincipale(mainRequest);
      final ListeMetadonneeCodeType metadataToReturn = new ListeMetadonneeCodeType();
      metadataToReturn.getMetadonneeCode().add("NumeroAffaireWATT");
      metadataToReturn.getMetadonneeCode().add("DateArchivage");
      metadataToReturn.getMetadonneeCode().add("DelaiNbEcheance");
      metadataToReturn.getMetadonneeCode().add("NumeroPersonne");
      metadataToReturn.getMetadonneeCode().add("NumeroPiece");
      metadataToReturn.getMetadonneeCode().add("NumeroStructure");
      metadataToReturn.getMetadonneeCode().add("NumeroCompteExterne");
      metadataToReturn.getMetadonneeCode().add("CodeFonction");
      metadataToReturn.getMetadonneeCode().add("DateNaissanceSalarie");
      metadataToReturn.getMetadonneeCode().add("DateReception");
      metadataToReturn.getMetadonneeCode().add("DelaiPresenceRMR");
      request.setMetadonnees(metadataToReturn);

      request.setNbDocumentsParPage(200);

      // Boucle sur les différentes pages
      int totalCounter = 0;
      while (true) {
         final RechercheParIterateurResponseType response = service.rechercheParIterateur(request);
         final IdentifiantPageType nextPageId = response.getIdentifiantPageSuivante();
         for (final ResultatRechercheType doc : response.getResultats().getResultat()) {
            final String UUID = doc.getIdArchive();
            final ListeMetadonneeType meta = doc.getMetadonnees();
            checkNumeric(sysout, UUID, meta, "DelaiNbEcheance");
            checkNumeric(sysout, UUID, meta, "NumeroPersonne");
            checkNumeric(sysout, UUID, meta, "NumeroPiece");
            checkNumeric(sysout, UUID, meta, "NumeroStructure");
            checkNumeric(sysout, UUID, meta, "NumeroCompteExterne");
            checkNumeric(sysout, UUID, meta, "CodeFonction");
            checkDate(sysout, UUID, meta, "DateNaissanceSalarie");
            checkDate(sysout, UUID, meta, "DateReception");
            checkDate(sysout, UUID, meta, "DelaiPresenceRMR");
            sysout.println(UUID);
            if (UUID.contains("7AB59705-DF72-4C6E-9D68-3629DDE94F69")) {
               dumpMeta(sysout, meta);
            }
            totalCounter++;
         }
         if (response.isDernierePage()) {
            break;
         }
         request.setIdentifiantPage(nextPageId);
      }
      System.out.println("Nombre total de doc parcourus : " + totalCounter);
   }

   private void checkDate(final PrintStream sysout, final String UUID, final ListeMetadonneeType meta, final String metaCode) {
      final String metaValue = SoapHelper.getMetaValue(meta, metaCode);
      if (metaValue.length() > 0) {
         // sysout.println(UUID + " - " + metaCode + "=" + metaValue);
      }
   }

   private void checkNumeric(final PrintStream sysout, final String UUID, final ListeMetadonneeType meta, final String metaCode) {
      final String metaValue = SoapHelper.getMetaValue(meta, metaCode);
      if (!isNumeric(metaValue)) {
         sysout.println("!!!!!!!!!!!!!!! " + UUID + " - " + metaCode + "=" + metaValue);
      } else {
         // sysout.println(UUID + " - " + metaCode + "=" + metaValue);
      }
   }

   private static boolean isNumeric(final String strNum) {
      if (strNum.length() == 0) {
         return true;
      }
      return strNum.matches("-?\\d+(\\.\\d+)?");
   }
}
