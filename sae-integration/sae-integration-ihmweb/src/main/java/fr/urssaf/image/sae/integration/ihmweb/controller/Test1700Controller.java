package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.SaeIntegrationConstantes;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ModificationFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.Test1700Formulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;

/**
 * 1700-Ged-Technique-OK-Modification-Valeur-Meta
 */
@Controller
@RequestMapping(value = "test1700")
public class Test1700Controller extends
      AbstractTestWsController<Test1700Formulaire> {

   /**
    * {@inheritDoc}
    */
   @Override
   protected final String getNumeroTest() {
      return "1700";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final Test1700Formulaire getFormulairePourGet() {

      
      Test1700Formulaire formulaire = new Test1700Formulaire();
      
      // capture unitaire
      CaptureUnitaireFormulaire captUnit = formulaire.getCaptureUnitaire();

      // L'URL ECDE
      captUnit
            .setUrlEcde(getEcdeService()
                  .construitUrlEcde(
                        "SAE_INTEGRATION/20110822/Ged-Technique-1700-Ged-Technique-OK-Modification-Valeur-Meta/documents/doc1.PDF"));

      // Le nom du fichier
      captUnit.setNomFichier("doc1.PDF");

      // Les métadonnées
      MetadonneeValeurList metadonnees = new MetadonneeValeurList();
      captUnit.setMetadonnees(metadonnees);
      metadonnees.add("ApplicationProductrice", "ADELAIDE");
      metadonnees.add("CodeOrganismeGestionnaire", "CER69");
      metadonnees.add("CodeOrganismeProprietaire", "UR750");
      metadonnees.add("CodeRND", "2.3.1.1.12");
      metadonnees.add("DateCreation", "2011-09-05");
      metadonnees.add("Denomination",
            "Test 1700-Ged-Technique-OK-Modification-Meta");
      metadonnees.add("FormatFichier", "fmt/354");
      metadonnees.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.add("NbPages", "2");
      metadonnees.add("Titre", "AUTRE COURRIER ENTRANT RELATIF A LA GESTION DES DONNEES ADMINISTRATIVES");
      metadonnees.add("TypeHash", "SHA-1");

      // Initialise le formulaire de recherche
      
      List<RechercheFormulaire> rechFormulaireList = formulaire.getRechFormulaireList();
      
      CodeMetadonneeList codeMetadonneeList = new CodeMetadonneeList();
      
      for(int i=0; i< getCasTest().getLuceneExempleList().size(); i++){
         RechercheFormulaire formulaireRecherche = new RechercheFormulaire(formulaire);
         formulaireRecherche.setRequeteLucene(getCasTest().getLuceneExempleList().get(i));
         formulaireRecherche.setCodeMetadonnees(codeMetadonneeList);
         rechFormulaireList.add(formulaireRecherche);
      }
      
      // formulaire de modification
      ModificationFormulaire formModification = formulaire.getModification();
      formModification.getResultats().setStatus(TestStatusEnum.SansStatus);
      
      // Les métadonnées
      MetadonneeValeurList metaModif = new MetadonneeValeurList();
      metaModif.add("Denomination",
            "Test 1700-Ged-Technique-Modifiee");
      formModification.setMetadonnees(metaModif);
      
      // Paramètres du VI
      ViFormulaire viForm = formulaire.getViFormulaire();
      viForm.setIssuer(SaeIntegrationConstantes.VI_DEFAULT_ISSUER);
      viForm.setRecipient(SaeIntegrationConstantes.VI_DEFAULT_RECIPIENT);
      viForm.setAudience(SaeIntegrationConstantes.VI_DEFAULT_AUDIENCE);
      PagmList pagmList = new PagmList();
      viForm.setPagms(pagmList);
      pagmList.add(SaeIntegrationConstantes.VI_DEFAULT_PAGM);
      
      return formulaire;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final void doPost(Test1700Formulaire formulaire) {
      
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
         etape1captureUnitaireAppelWs(formulaire);
      } else if ("2".equals(etape)) {
         modification(formulaire.getUrlServiceWeb(), formulaire
               .getModification());
      } else if ("3".equals(etape)) {
         int numSousEtape = new Integer(sousEtape);
         int nbResultatsAttendus = -1;
         if (numSousEtape == 0) {
            nbResultatsAttendus = 0;
         } else {
            nbResultatsAttendus = 1;
         }
         etape3Recherche(formulaire, numSousEtape, nbResultatsAttendus);
      } 
   }
   
   private void etape1captureUnitaireAppelWs(Test1700Formulaire formulaire) {

      // Initialise
      CaptureUnitaireFormulaire formCaptureEtp1 = formulaire.getCaptureUnitaire();

      // Lance le test
      CaptureUnitaireResultat resultat = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(
                  formulaire.getUrlServiceWeb(), formCaptureEtp1, formulaire.getViFormulaire());
      
      // Si le test n'est pas en échec, alors on peut initialiser l'id du document à supprimer
      if (!TestStatusEnum.Echec.equals(formulaire.getCaptureUnitaire().getResultats().getStatus())) {
         formulaire.getModification().setIdDocument(UUID.fromString(resultat.getIdArchivage()));
      }
   }
   
   private void modification(String urlWebService,
         ModificationFormulaire formulaire) {

      // Appel de la méthode de test
      getModificationTestService().appelWsOpModificationReponseAttendue(
            urlWebService, formulaire, null);
   }

   private void etape3Recherche(Test1700Formulaire formulaire, int sousEtape, int nbResultatsAttendus) {

      // Appel le service de test de la recherche
      getRechercheTestService()
      .appelWsOpRechercheReponseCorrecteAttendue(
            formulaire.getUrlServiceWeb(),
            formulaire.getRechFormulaireList().get(sousEtape), nbResultatsAttendus, false,
            TypeComparaison.NumeroRecours);


      ResultatTest resultatTest = formulaire.getRechFormulaireList().get(sousEtape).getResultats();

      if (!TestStatusEnum.Echec.equals(resultatTest.getStatus())) {

         resultatTest.setStatus(TestStatusEnum.Succes);
      }

   }

}
