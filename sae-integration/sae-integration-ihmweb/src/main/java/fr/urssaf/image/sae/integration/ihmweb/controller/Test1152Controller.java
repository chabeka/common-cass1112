package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1151Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;

/**
 * 1152-Droits-Archivage-Unitaire-KO-PRMD-Innacessible
 */
@Controller
@RequestMapping(value = "test1152")
@SuppressWarnings( { "PMD.AvoidDuplicateLiterals" })
public class Test1152Controller extends
      AbstractTestWsController<Test1151Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1152";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected String getNomVue() {
      return "test1152";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1151Formulaire getFormulairePourGet() {

      Test1151Formulaire formulaire = new Test1151Formulaire();
      
      CaptureUnitaireFormulaire captureUnitaire = formulaire.getCaptureUnitaire();
      captureUnitaire
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Droit-1152-Droits-Archivage-Unitaire-KO-PRMD-Innacessible/documents/ADELPF_710_PSNV211157BPCA1L0000.pdf"));
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      metadonnees.add(new MetadonneeValeur("ApplicationProductrice", "ADELAIDE"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeGestionnaire", "CER69"));
      metadonnees.add(new MetadonneeValeur("CodeOrganismeProprietaire", "UR750"));
      metadonnees.add(new MetadonneeValeur("CodeRND", "2.3.1.1.13"));
      metadonnees.add(new MetadonneeValeur("DateCreation", "2011-09-01"));
      metadonnees.add(new MetadonneeValeur("Denomination", "Test 1152-Droits-Archivage-Unitaire-KO-PRMD-Innacessible"));
      metadonnees.add(new MetadonneeValeur("FormatFichier", "fmt/354"));
      metadonnees.add(new MetadonneeValeur("Hash", "d145ea8e0ca28b8c97deb0c2a550f0a969a322a3"));
      metadonnees.add(new MetadonneeValeur("NbPages", "2"));
      metadonnees.add(new MetadonneeValeur("Siren", "3090000001"));
      metadonnees.add(new MetadonneeValeur("Titre", "Attestation de vigilance"));
      metadonnees.add(new MetadonneeValeur("TypeHash", "SHA-1"));
      captureUnitaire.setMetadonnees(metadonnees);
      


      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer("INT_CS_ATT_VIGI");
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      pagmList.add("INT_PAGM_ATT_VIGI_ARCH_UNIT");
      viForm.setPagms(pagmList);
      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1151Formulaire formulaire) {

      String etape = formulaire.getEtape();

      if ("1".equals(etape)) {

         etape1CaptureUnitaireAppelWs(formulaire.getUrlServiceWeb(), formulaire
               .getCaptureUnitaire());

      } else {

         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }
   }
   

   private void etape1CaptureUnitaireAppelWs(String urlWebService,
         CaptureUnitaireFormulaire formulaire) {
      String[] result = new String[] { "" };
      // Appel de la méthode de test

      getCaptureUnitaireTestService().appelWsOpCaptureUnitaireSoapFault(
            urlWebService, formulaire, ViStyle.VI_OK,
            formulaire.getParent().getViFormulaire(), "sae_DroitsInsuffisants",
            result);

   }

}
