package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.StockageUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test2799Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ModeStockageUnitaireEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.StockageUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;

/**
 * 2799-StockageUnitaire-TestLibre
 */
@Controller
@RequestMapping(value = "test2799")
public class Test2799Controller extends
      AbstractTestWsController<Test2799Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "2799";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test2799Formulaire getFormulairePourGet() {

      Test2799Formulaire formulaire = new Test2799Formulaire();

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape n°1: Capture unitaire
      // -----------------------------------------------------------------------------
      StockageUnitaireFormulaire formStockage = formulaire.getStockageUnitaire();
 
      // Le mode d'utilisation de la capture
      formStockage.setModeStockage(ModeStockageUnitaireEnum.stockageUnitaireAvecUrlEcde);
 
      // L'URL ECDE
      formStockage
            .setUrlEcde(this
                  .getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureStockage-2700-CaptureStockage-TestLibre/documents/doc1.PDF"));
      // L'URL ECDE Origine
      formStockage
            .setUrlEcdeOrig(this
                  .getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/CaptureStockage-2700-CaptureStockage-TestLibre/documents/doc2.PDF"));
      // Le nom du fichier
      formStockage.setNomFichier("doc1.PDF");
      // Le nom du fichier d'origine
      formStockage.setNomFichierOrig("doc2.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      formStockage.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "AC750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2010-09-23");
      metadonnees.add("Denomination", "Test 2700-CaptureStockage-TestLibre");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre","Attestation de vigilance");
      metadonnees.add("TypeHash", "SHA-1");

      // -----------------------------------------------------------------------------
      // Initialisation du formulaire de l'étape n°2: Recherche #1
      // -----------------------------------------------------------------------------

      RechercheFormulaire formRech = formulaire.getRecherche();

      // Requête de recherche
      formRech.setRequeteLucene(getCasTest().getLuceneExempleList().get(0));

      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta = formRech.getCodeMetadonnees() ;
      codesMeta.add("Denomination");
     // codesMeta.add("DocFormatOrigine");

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test2799Formulaire formulaire) {

      String etape = formulaire.getEtape();
      String idArchivage = null;
      
      if ("1".equals(etape)) {
         etape1stockageUnitaire(formulaire);
         idArchivage = formulaire.getConsultation().getIdArchivage();
         formulaire.getConsultation().setIdArchivage(idArchivage);

      } else if ("2".equals(etape)) {

         etape2recherche(formulaire);

      } else if ("3".equals(etape)) {

         etape3consultation(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }

   private void etape1stockageUnitaire(Test2799Formulaire formulaire) {

      // Initialise
      StockageUnitaireFormulaire formCaptureEtp1 = formulaire
            .getStockageUnitaire();

      // Vide le résultat du test précédent de l'étape 2
      formulaire.getRecherche().getResultats().clear();

      // Vide le résultat du test précédent de l'étape 3
      formulaire.getConsultation().getResultats().clear();

      // Initialise la valeur de retour
      StockageUnitaireResultat result = new StockageUnitaireResultat();
       
      // Lance le test
      result = this.getStockageUnitaireTestService()
      .appelWsOpStockageUnitaireUrlEcdeTestLibre(
            formulaire.getUrlServiceWeb(), formCaptureEtp1);
      
      // On mémorise l'identifiant d'archivage
      String idArchivageSU = result.getIdArchivage();
      formCaptureEtp1.setDernierIdArchivage(idArchivageSU);
      formulaire.getConsultation().setIdArchivage(idArchivageSU);
                 
   }

   private void etape2recherche(Test2799Formulaire formulaire) {

      // Résultats attendus
      int nbResultatsAttendus = 1;
      boolean flagResultatsTronquesAttendu = false;

      // Appel de la méthode de test
      RechercheResponse response = getRechercheTestService()
            .appelWsOpRechercheReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(), formulaire.getRecherche(),
                  nbResultatsAttendus, flagResultatsTronquesAttendu, null);

      // Vérifications en profondeur
      ResultatTest resultatTest = formulaire.getRecherche().getResultats();
      if ((response != null)
            && (!TestStatusEnum.Echec.equals(resultatTest.getStatus()))) {

         // Vérifie le résultat
         getRechercheTestService().verifieResultatRecherche(
               response.getRechercheResponse().getResultats().getResultat()[0],
               "1", resultatTest, getValeursAttendues());
      }
      
      // Les métadonnées que l'on souhaite en retour
      CodeMetadonneeList codesMeta = formulaire.getConsultation().getCodeMetadonnees();
      codesMeta.add("Denomination");
      //codesMeta.add("DocFormatOrigine");
      
      // Si pas en échec, alors test en OK (tout a été vérifié)
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

   private MetadonneeValeurList getValeursAttendues() {

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("Denomination", "Test 2700-CaptureStockage-TestLibre");
      //valeursAttendues.add("DocFormatOrigine", "true");

      return valeursAttendues;

   }

   private void etape3consultation(Test2799Formulaire formulaire) {

         // Initialise
         ConsultationFormulaire formConsult = formulaire.getConsultation();
         
         // Le SHA-1 attendu
         String sha1attendu = null;
         
         // La liste des codes des métadonnées attendues
         CodeMetadonneeList codesMetasAttendues = new CodeMetadonneeList();
         codesMetasAttendues.add("Denomination");
       //  codesMetasAttendues.add("DocFormatOrigine");

         // Les valeurs des métadonnées attendues
         List<MetadonneeValeur> metaAttendues = new ArrayList<MetadonneeValeur>();
         metaAttendues.add(new MetadonneeValeur("Denomination",
               "Test 2700-CaptureStockage-TestLibre"));       
         //metaAttendues.add(new MetadonneeValeur("DocFormatOrigine","true"));
         // Lance le test
         getConsultationTestService()
               .appelWsOpConsultationReponseCorrecteAttendue(
                     formulaire.getUrlServiceWeb(), formConsult, sha1attendu,
                     codesMetasAttendues, metaAttendues);

         // Au mieux, le résultat du test est "à contrôler", car le type MIME
         // doit être contrôlé manuellement pour l'instant
         if (!TestStatusEnum.Echec.equals(formConsult.getResultats().getStatus())) {
            formConsult.getResultats().setStatus(TestStatusEnum.AControler);
         }
   }

}
