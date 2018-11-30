package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseResultatFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ComptagesTdmFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test224Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;

/**
 *  224-CaptureMasse-OK-RUM
 */
@Controller
@RequestMapping(value = "test224")
public class Test224Controller extends
      AbstractTestWsController<Test224Formulaire> {

   /**
    * Nombre d'occurence attendu
    */
   private static final int COUNT_WAITED = 1;
   private static final int COMPTAGE_COUNT_WAITED = 11;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "224";
   }

   private String getDebutUrlEcde() {
      return getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/CaptureMasse-224-CaptureMasse-OK-RUM/");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test224Formulaire getFormulairePourGet() {

      Test224Formulaire formulaire = new Test224Formulaire();

      // Initialise le formulaire de capture de masse
      
      CaptureMasseFormulaire formCapture = formulaire
            .getCaptureMasseDeclenchement();
      formCapture.setUrlSommaire(getDebutUrlEcde() + "sommaire.xml");
      formCapture.getResultats().setStatus(TestStatusEnum.SansStatus);

      CaptureMasseResultatFormulaire formResultat = formulaire
            .getCaptureMasseResultat();
      formResultat.setUrlSommaire(getDebutUrlEcde() + "resultat.xml");
      formResultat.getResultats().setStatus(TestStatusEnum.SansStatus);

      
      // Initialise le formulaire de recherche
      
      //List<RechercheFormulaire> rechFormulaireList = formulaire.getRechFormulaireList();
      
      List<RechercheFormulaire> rechFormulaireList = formulaire.getRechFormulaireList();
      
      
      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      codeMetadonneeList.add("Denomination");
      codeMetadonneeList.add("RUM");
      codeMetadonneeList.add("NumeroRecours");
      
      for(int i=0; i< getCasTest().getLuceneExempleList().size(); i++){
         RechercheFormulaire formulaireRecherche = new RechercheFormulaire(formulaire);
         formulaireRecherche.setRequeteLucene(getCasTest().getLuceneExempleList().get(i));
         formulaireRecherche.setCodeMetadonnees(codeMetadonneeList);
         rechFormulaireList.add(formulaireRecherche);
      }
      
      // Initialise le formulaire de consultation
      
      ConsultationFormulaire formConsult = formulaire.getConsultFormulaire();
      
      CodeMetadonneeList codeMetaConsult = new CodeMetadonneeList();
      formConsult.setCodeMetadonnees(codeMetaConsult);
      codeMetaConsult.add("Denomination");
      codeMetaConsult.add("RUM");
      codeMetaConsult.add("NumeroRecours");
      

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test224Formulaire formulaire) {

      String sousEtape = StringUtils.EMPTY;
      String etape = formulaire.getEtape();
      if(etape.length()>1){
         sousEtape = StringUtils.substringAfter(etape, ".");
         etape= etape.substring(0, 1);
      }
      for(RechercheFormulaire f :formulaire.getRechFormulaireList()){
         f.setParent(formulaire);
      }
      if ("1".equals(etape)) {

         etape1captureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire);

      } else if ("2".equals(etape)) {

         etape2captureMasseResultats(formulaire.getCaptureMasseResultat());
         
         // initialise l'identifiant de traitement de masse en lisant le fichier
         // debut_traitement.flag
         String idTdm = getCaptureMasseTestService().readIdTdmInDebutTrait(
               formulaire.getCaptureMasseDeclenchement().getUrlSommaire());
         ComptagesTdmFormulaire formComptage = formulaire
               .getComptagesFormulaire();
         formComptage.setIdTdm(idTdm);

      } else if ("3".equals(etape)) {

         etape3Recherche(formulaire, new Integer(sousEtape));

      } else if ("4".equals(etape)) {

         etape4Consultation(formulaire);

      } else if ("5".equals(etape)) {

         etape5Comptages(formulaire);

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }

   }
   


   private void etape1captureMasseAppelWs(String urlWebService,
         Test224Formulaire formulaire) {

      // Vide le résultat du test précédent de l'étape 2
      CaptureMasseResultatFormulaire formCaptMassRes = formulaire
            .getCaptureMasseResultat();
      formCaptMassRes.getResultats().clear();
      formCaptMassRes.setUrlSommaire(formulaire.getCaptureMasseDeclenchement()
            .getUrlSommaire());

      // Appel de la méthode de test
      getCaptureMasseTestService().appelWsOpArchiMasseOKAttendu(urlWebService,
            formulaire.getCaptureMasseDeclenchement());

   }

   private void etape2captureMasseResultats(
         CaptureMasseResultatFormulaire formulaire) {

      getCaptureMasseTestService()
            .testResultatsTdmReponseOKAttendue(formulaire);

   }

   private void etape3Recherche(Test224Formulaire formulaire, int sousEtape) {
      
      
      //for(RechercheFormulaire rechFormulaire : formulaire.getRechFormulaireList()){
         
      // Appel le service de test de la recherche
         RechercheResponse response = getRechercheTestService()
               .appelWsOpRechercheReponseCorrecteAttendue(
                     formulaire.getUrlServiceWeb(),
                     formulaire.getRechFormulaireList().get(sousEtape), COUNT_WAITED, false,
                     TypeComparaison.NumeroRecours);

         
         ResultatTest resultatTest = formulaire.getRechFormulaireList().get(sousEtape).getResultats();

         if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

            // Récupère l'unique résultat
            ResultatRechercheType resultatRecherche = response
                  .getRechercheResponse().getResultats().getResultat()[0];
            
            // Le vérifie
            verifieResultatRecherche(resultatRecherche, resultatTest, sousEtape);
            
            // Si le test n'est pas en échec, alors on peut le passer en succès,
            // car tout a pu être vérifié
            if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
               resultatTest.setStatus(TestStatusEnum.Succes);
            }
            
            // Initialise le formulaire de consultation
            formulaire.getConsultFormulaire().setIdArchivage(
                  resultatRecherche.getIdArchive().toString());

         }

         
      //}
      
   }

   private void verifieResultatRecherche(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest, int sousEtape) {

      String numeroResultatRecherche = "1";

      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues= getValeursAttendues(sousEtape);

      getRechercheTestService().verifieResultatRecherche(resultatRecherche,
            numeroResultatRecherche, resultatTest, valeursAttendues);

   }

   private void etape4Consultation(Test224Formulaire formulaire) {
      
      // Les codes des métadonnées attendues
      CodeMetadonneeList codeMetaAttendues = new CodeMetadonneeList();
      codeMetaAttendues.add("Denomination");
      codeMetaAttendues.add("NumeroRecours");
      codeMetaAttendues.add("RUM");
      
      // Valeurs des métadonnées attendues
      List<MetadonneeValeur> valeursMetaAttendus = new ArrayList<MetadonneeValeur>();
      valeursMetaAttendus.add(new MetadonneeValeur("Denomination","Test 224-CaptureMasse-OK-RUM"));
      //valeursMetaAttendus.add(new MetadonneeValeur("RUM","a2f93f1f121ebba0faef2c0596f2f126eacae77b"));
      
      // Appel du service de vérification
      getConsultationTestService()
            .appelWsOpConsultationReponseCorrecteAttendue(
                  formulaire.getUrlServiceWeb(),
                  formulaire.getConsultFormulaire(),
                  null,
                  codeMetaAttendues,
                  valeursMetaAttendus);
      
      // Si le test n'est pas en échec, alors on peut le passer en succès,
      // car tout a pu être vérifié
      ResultatTest resultatTest = formulaire.getConsultFormulaire().getResultats();
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.AControler);
      }
      
   }
   
   private void etape5Comptages(Test224Formulaire formulaire) {

      // Récupération de l'objet ResultatTest
      ResultatTest resultatTest = formulaire.getComptagesFormulaire()
            .getResultats();
      resultatTest.clear();

      // Lecture de l'identifiant du traitement de masse
      String idTdm = formulaire.getComptagesFormulaire().getIdTdm();

      // Appel du service de comptages
      getCaptureMasseTestService().comptages(idTdm, resultatTest,
            new Long(COMPTAGE_COUNT_WAITED));

      // Passe le test en OK si pas KO
      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {
         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }
   
 private MetadonneeValeurList getValeursAttendues(int numeroResultat) {
      
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();
      
      if (numeroResultat==0) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","12345678901234567890123456789012345");
         valeursAttendues.add("NumeroRecours","1");
         
      } else if (numeroResultat==1) {
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","     678901234567890123456789012345");
         valeursAttendues.add("NumeroRecours","2");         
      } else if (numeroResultat==2) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM"," 2345678901234567890     6789012345");
         valeursAttendues.add("NumeroRecours","3");         
      } else if (numeroResultat==3) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","??abc : d 12 ef");
         valeursAttendues.add("NumeroRecours","4");         
      } else if (numeroResultat==4) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","     ??abc : d 12 ef     ");
         valeursAttendues.add("NumeroRecours","5");         
      } else if (numeroResultat==5) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","98765432109876543210987654321098765");
         valeursAttendues.add("NumeroRecours","6");         
      } else if (numeroResultat==6) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","12345     12345     12345     00000");
         valeursAttendues.add("NumeroRecours","7");         
      } else if (numeroResultat==7) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","00000000001234567890123456789000000");
         valeursAttendues.add("NumeroRecours","8");        
      } else if (numeroResultat==8) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","                              12345");
         valeursAttendues.add("NumeroRecours","9");         
      }else if (numeroResultat==9) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","00000000000000000000000000000000000");
         valeursAttendues.add("NumeroRecours","10");         
      }else if (numeroResultat==10) {
         
         valeursAttendues.add("Denomination","Test 224-CaptureMasse-OK-RUM");
         valeursAttendues.add("RUM","++345678901234567890123456789012345");
         valeursAttendues.add("NumeroRecours","11");         
      }else {
         throw new IntegrationRuntimeException("Numéro de résultat " + numeroResultat + " inconnu");
      }
      
      
      // Renvoi du résultat
      return valeursAttendues;
      
   }
   
}
