package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureMasseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConsultationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1150Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;

/**
 * 1150-Droits-Toutes-Actions-KO-CS-Inconnu
 */
@Controller
@RequestMapping(value = "test1150")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1150Controller extends
      AbstractTestWsController<Test1150Formulaire> {

   private static final int WAITED_COUNT = 8;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1150";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "test1150";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1150Formulaire getFormulairePourGet() {

      Test1150Formulaire formulaire = new Test1150Formulaire();
      
      CaptureUnitaireFormulaire captureUnitaire = formulaire.getCaptureUnitaire();
      CaptureMasseFormulaire captureMasse = formulaire.getCaptureMasse();
      
      ConsultationFormulaire consultation = formulaire.getConsultFormulaire();
      
      RechercheFormulaire formRecherche = formulaire.getRechercheFormulaire();
      
      
      captureUnitaire
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Droit-1150-Droits-Toutes-Actions-KO-CS-INCONNU/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      metadonnees.add(new MetadonneeValeur("ApplicationProductrice", "ADELAIDE"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeGestionnaire", "CER69"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeProprietaire", "UR750"));
      metadonnees.add(new MetadonneeValeur("CodeRND", "2.3.1.1.12"));
      metadonnees.add(new MetadonneeValeur("DateCreation", "2011-09-01"));
      metadonnees.add(new MetadonneeValeur("Denomination", "Test 1150-Droits-Toutes-Actions-KO-CS-INCONNU"));
      metadonnees.add(new MetadonneeValeur("FormatFichier", "fmt/354"));
      metadonnees.add(new MetadonneeValeur("Hash", "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3"));
      metadonnees.add(new MetadonneeValeur("NbPages", "2"));
      metadonnees.add(new MetadonneeValeur("Siren", "3090000001"));
      metadonnees.add(new MetadonneeValeur("Titre", "Attestation de vigilance"));
      metadonnees.add(new MetadonneeValeur("TypeHash", "SHA-1"));
      captureUnitaire.setMetadonnees(metadonnees);
      
      captureMasse.setUrlSommaire(getEcdeService()
            .construitUrlEcde(
                  "SAE_INTEGRATION/20110822/Droit-1150-Droits-Toutes-Actions-KO-CS-INCONNU/")+"sommaire.xml");
      
      
      consultation.setIdArchivage("9EFE8188-03D5-4A72-BEBC-306FD31EB686");
      
      
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Requête de recherche correspondant au jeu de test inséré en base
      // d'intégration
      formRecherche.setRequeteLucene(getCasTest().getLuceneExemple());

      // Pas de métadonnées spécifiques à récupérer
      CodeMetadonneeList codesMeta = new CodeMetadonneeList();
      formRecherche.setCodeMetadonnees(codesMeta);

      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_INCONNU");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add("INT_PAGM_CS_INCONNU");

      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1150Formulaire formulaire) {

      String etape = formulaire.getEtape();

      if ("1".equals(etape)) {

         etape1CaptureUnitaireAppelWs(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureUnitaire());

      } else if ("2".equals(etape)) {

         etape2CaptureMasseAppelWs(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureMasse());

      } else if ("3".equals(etape)) {

         recherche(formulaire.getUrlServiceWeb(), formulaire
               .getRechercheFormulaire());

      } else if ("4".equals(etape)) {

         etape4Consultation(formulaire.getUrlServiceWeb(), formulaire
               .getConsultFormulaire());

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }
   }
   

   private void etape1CaptureUnitaireAppelWs(String urlWebService,
         CaptureUnitaireFormulaire formulaire) {
      String[] result = new String[]{""};
      // Appel de la méthode de test
      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
            urlWebService, formulaire, ViStyle.VI_OK, "vi_InvalidIssuer",
            result);

   }

   private void etape2CaptureMasseAppelWs(String urlWebService, CaptureMasseFormulaire formulaire){
      String[] result = new String[]{""};
      getCaptureMasseTestService().appelWsOpArchiMasseSoapFaultAttendue(urlWebService, formulaire, "vi_InvalidIssuer", result);
   }

   private void etape4Consultation(String urlWebService, ConsultationFormulaire formulaire){
      String[] result = new String[]{""};
      getConsultationTestService().appelWsOpConsultationSoapFault(urlWebService, formulaire, ViStyle.VI_OK, "vi_InvalidIssuer", result);
   }

   private void recherche(String urlServiceWeb, RechercheFormulaire formulaire) {
      String[] result = new String[]{""};
      // Appel de la méthode de test
      getRechercheTestService().appelWsOpRechercheSoapFault(urlServiceWeb,
            formulaire, ViStyle.VI_OK, "vi_InvalidIssuer", result);

   }

}
