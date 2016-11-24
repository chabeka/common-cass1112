/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.CopieResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.DenominationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.ResultatRechercheType;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.utils.LuceneUtils;

/**
 * Classes abstraite de gestion des test 3300.
 */
public abstract class AbstractTest3300Controller extends
AbstractTestWsController<TestWsCopieFormulaire> {

   /**
    * Document de copie OK cas simple dans ECDE.
    */
   private static final String DOC_TEST_3301_COPIE_OK_CAS_SIMPLE = "SAE_INTEGRATION/20110822/Copie-3301-Copie-OK-CasSimple/documents/doc1.PDF";

   /**
    * Enumeration pour pouvoir diférentier les differentes meta specifiques aux
    * cas de test.
    */
   protected enum GroupMetasType {
      /**
       * ACTIVITE FONCTION
       */
      ACTIVITE_FONCTION {

         @Override
         public String toString() {
            return "activiteFonction";
         }

      },
      /**
       * NOUVELLE META DICO
       */
      NVL_META_DICO {

         @Override
         public String toString() {
            return "nouvelleMetaDico";
         }
      },
      /**
       * PERIODE
       */
      PERIODE {

         @Override
         public String toString() {
            return "periode";
         }
      }
   }

   /**
    * Enumeration pour pouvoir diférentier les differentes meta specifiques aux
    * cas de test.
    */
   protected enum TypeRechercheEtape {
      /**
       * RECHERCHE ETAPE DOCUMENT EXISTANT
       */
      RECH_ETAPE_DOC_EXISTANT {

         @Override
         public String toString() {
            return "rechercheEtapeDocExistant";
         }

      },
      /**
       * RECHERCHE ETAPE DOCUMENT COPIE
       */
      RECH_ETAPE_DOC_COPIE {

         @Override
         public String toString() {
            return "rechercheEtapeDocCopie";
         }
      }
   }

   /**
    * 
    * {@inheritDoc}
    */
   @Override
   protected TestWsCopieFormulaire getFormulairePourGet() {
      // Création du formulaire de test de la copie
      TestWsCopieFormulaire formulaire = new TestWsCopieFormulaire();

      // Modification du formulaire de capture unitaire
      this.modifierFormulaireCaptureUnitaire(formulaire.getCaptureUnitaire(),
            ReferentielMetadonneesService.getMetadonneesExemplePourCopie());

      // Modification du formulaire de copie
      this.modifierFormulaireCopie(formulaire.getCopie(),
            ReferentielMetadonneesService.getMetadonneesExemplePourCopie());

      // Modification du formulaire de la recherche du document existant
      this.modifierFormulaireRechercheDocExistant(formulaire
            .getRechercheDocExistant());

      // Modification du formulaire de la recherche du document existant
      this.modifierFormulaireRechercheDocCopie(formulaire
            .getRechercheDocCopie());

      return formulaire;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void doPost(TestWsCopieFormulaire formulaire) {
      String etape = formulaire.getEtape();
      boolean isTestLibre = "3300".equals(getNumeroTest());

      if ("capture_unitaire_copie".equals(etape)) {
         this.captureUnitaire(formulaire, isTestLibre);
      } else if ("copie".equals(etape)) {
         this.copie(formulaire, isTestLibre);
      } else if ("recherche_document_copie".equals(etape)) {
         verificationRechercheReponse(this.recherche(formulaire.getUrlServiceWeb(),
               formulaire.getRechercheDocCopie(), formulaire.getViFormulaire(),
               isTestLibre),
               formulaire.getRechercheDocCopie(),
               TypeRechercheEtape.RECH_ETAPE_DOC_COPIE, isTestLibre);
      } else if ("recherche_document_existant".equals(etape)) {
         verificationRechercheReponse(this.recherche(
               formulaire.getUrlServiceWeb(),
               formulaire.getRechercheDocExistant(),
               formulaire.getViFormulaire(), isTestLibre),
               formulaire.getRechercheDocCopie(),
               TypeRechercheEtape.RECH_ETAPE_DOC_EXISTANT, isTestLibre);
      } else {
         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }
   }


   /**
    * Methode permettant de
    * 
    * @param recherche
    * @return
    */
   private void modifierFormulaireRechercheDocCopie(
         RechercheFormulaire formRecherche) {
      // Formulaire de recherche initialisé
      this.modifierFormulaireRecherche(formRecherche,
            DenominationEnum.DENOMINATION_TEST_LIBRE_OK_COPIE.toString());
      // Formulaire de recherche spécifique au test.
      this.modificationSpecifiqueFormulaireRechercheDocCopie(formRecherche);

   }

   /**
    * Méthode permettant de modifier le formulaire pour la recherche generique.
    * 
    * @param RechercheFormulaire
    *           Formulaire de recherche
    */
   private void modifierFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche) {
      // Formulaire de recherche initialisé
      this.modifierFormulaireRecherche(formRecherche,
            DenominationEnum.DENOMINATION_TEST_LIBRE_OK.toString());
      // Formulaire de recherche spécifique au test.
      this.modificationSpecifiqueFormulaireRechercheDocExistant(formRecherche);

   }

   /**
    * Methode permettant de modifier le formulaire pour la recherche generique.
    * 
    * @param formRecherche
    *           Formulaire de recherche.
    * @param luceneExempleExtract
    *           Extrait de la requete Lucene utilisée dans le test.
    */
   private void modifierFormulaireRecherche(RechercheFormulaire formRecherche,
         String luceneExempleExtract) {
      formRecherche
      .setRequeteLucene(determineRequeteLucene(luceneExempleExtract));
      formRecherche.setCodeMetadonnees(initialisationDefaultMetadonnees());
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);
   }

   /**
    * Methode permettant d'initialiser la liste des codes des metadonnées pour
    * le test libre (Défaut).
    * 
    * @return Liste des codes des metadonnées.
    */
   private CodeMetadonneeList initialisationDefaultMetadonnees() {
      CodeMetadonneeList metas = new CodeMetadonneeList();
      metas.add("ApplicationProductrice");
      metas.add("CodeOrganismeGestionnaire");
      metas.add("CodeOrganismeProprietaire");
      metas.add("CodeRND");
      metas.add("DateCreation");
      metas.add("DateDebutConservation");
      metas.add("Denomination");
      metas.add("FormatFichier");
      metas.add("Hash");
      metas.add("NbPages");
      metas.add("Titre");
      metas.add("TypeHash");
      metas.add("VersionRND");
      return metas;
   }

   /**
    * Methode permettant d'initialiser la liste des codes des metadonnées pour
    * les fonctions activités.
    * 
    * @return Liste des codes des metadonnées.
    */
   protected CodeMetadonneeList initialisationFonctionActiviteMetadonnees() {
      CodeMetadonneeList metas = new CodeMetadonneeList();
      metas.add("CodeActivite");
      metas.add("CodeFonction");
      metas.add("CodeRND");
      metas.add("Titre");
      return metas;
   }

   /**
    * Methode permettant d'initialiser la liste des codes des metadonnées pour
    * le test libre (Défaut).
    * 
    * @return Liste des codes des metadonnées.
    */
   protected CodeMetadonneeList initialisationNvlMetaDicoMetadonnees() {
      CodeMetadonneeList metas = new CodeMetadonneeList();
      metas.add("CodeFonction");
      metas.add("CodeRND");
      metas.add("NouvelleMetaDico");
      metas.add("Titre");
      return metas;
   }

   /**
    * Methode permettant d'initialiser la liste des codes des metadonnées pour
    * le test libre (Défaut).
    * 
    * @return Liste des codes des metadonnées.
    */
   protected CodeMetadonneeList initialisationPeriodeMetadonnees() {
      CodeMetadonneeList metas = new CodeMetadonneeList();
      metas.add("Denomination");
      metas.add("Periode");
      return metas;
   }

   /**
    * Methode permettant d'initialiser les métadonnées pour ce test.
    * 
    * @param metasCodes
    *           Liste de code de métadonnées
    */
   protected void initialiseMetadonnees(CodeMetadonneeList metasCodes,
         GroupMetasType groupTypeMetas) {
      metasCodes.clear();

      if (GroupMetasType.ACTIVITE_FONCTION.equals(groupTypeMetas)) {
         metasCodes.addAll(initialisationFonctionActiviteMetadonnees());
      } else if (GroupMetasType.NVL_META_DICO.equals(groupTypeMetas)) {
         metasCodes.addAll(initialisationNvlMetaDicoMetadonnees());
      } else if (GroupMetasType.PERIODE.equals(groupTypeMetas)) {
         metasCodes.addAll(initialisationPeriodeMetadonnees());
      }
   }

   /**
    * Methode permettant de determiner la requete LUCENE à fournir pour les
    * tests.
    * 
    * @param luceneExempleExtract
    *           Extrait de la requete.
    * @return la requete LUCENE à fournir pour les tests
    */
   protected String determineRequeteLucene(String luceneExempleExtract) {
      return LuceneUtils.trouverRequeteLuceneDansListAvecExtrait(getCasTest()
            .getLuceneExempleList(), luceneExempleExtract);
   }

   /**
    * Methode permettant de modifier le formulaire pour la copie.
    * 
    * @param CopieFormulaire
    *           Formulaire de copie
    * @param metas
    *           liste de metadonnées
    */
   private void modifierFormulaireCopie(CopieFormulaire formCopie,
         MetadonneeValeurList metas) {
      formCopie.getResultats().setStatus(TestStatusEnum.SansStatus);

      // Id Archive defaut
      formCopie.setIdGed(null);

      // Des métadonnées exemples
      metas.add(DenominationEnum.DENOMINATION.toString(),
            DenominationEnum.DENOMINATION_TEST_LIBRE_OK_COPIE.toString());
      formCopie.getListeMetadonnees().addAll(metas);

      this.modificationSpecifiqueFormulaireCopie(formCopie);

   }

   /**
    * Methode permettant de modifier le formulaire pour la capture unitaire.
    * 
    * @param formCaptureUnitaire
    *           Formulaire de capture unitaire
    * @param metas
    *           liste de metadonnées
    */
   private void modifierFormulaireCaptureUnitaire(
         CaptureUnitaireFormulaire formCaptureUnitaire,
         MetadonneeValeurList metas) {
      // Initialisation d'un formulaire par défault
      formCaptureUnitaire.getResultats().setStatus(TestStatusEnum.SansStatus);
      // Un exemple d'URL ECDE de fichier à capturer
      // (qui correspond à un document réellement existant sur l'ECDE
      // d'intégration)
      formCaptureUnitaire.setUrlEcde(getEcdeService().construitUrlEcde(
            DOC_TEST_3301_COPIE_OK_CAS_SIMPLE));
      metas.add(DenominationEnum.DENOMINATION.toString(),
            DenominationEnum.DENOMINATION_TEST_LIBRE_OK.toString());
      formCaptureUnitaire.getMetadonnees().addAll(metas);

      this.modificationSpecifiqueFormulaireCaptureUnitaire(formCaptureUnitaire);
   }

   /**
    * Methode permettant de faire une capture unitaire par appel de webservice.
    * 
    * @param formulaire
    *           Formulaire de test copie
    * @param isTestLibre
    *           True si test libre, false sinon.
    */
   private void captureUnitaire(final TestWsCopieFormulaire formulaire,
         boolean isTestLibre) {

      CaptureUnitaireFormulaire formCaptureUnitaire = formulaire
            .getCaptureUnitaire();

      CopieFormulaire formCopie = formulaire.getCopie();
      formCopie.getResultats().clear();
      formCopie.setIdGed(null);

      RechercheFormulaire formRechercheCopie = formulaire
            .getRechercheDocCopie();
      formRechercheCopie.getResultats().clear();

      RechercheFormulaire formRechercheExistant = formulaire
            .getRechercheDocExistant();
      formRechercheExistant.getResultats().clear();

      // Appel de la méthode de test
      CaptureUnitaireResultat resultat = null;
      if (isTestLibre) {
         resultat = getCaptureUnitaireTestService()
               .appelWsOpCaptureUnitaireUrlEcdeTestLibre(
                     formulaire.getUrlServiceWeb(), formCaptureUnitaire,
                     formulaire.getViFormulaire());
      } else {
         resultat = this.appelWsOpCaptureUnitaireService(
               formulaire.getUrlServiceWeb(),
               formCaptureUnitaire, formulaire.getViFormulaire());
      }

      // Si le test est en succès ...
      if (TestStatusEnum.Succes.equals(formCaptureUnitaire.getResultats()
            .getStatus())
            || TestStatusEnum.SansStatus.equals(formCaptureUnitaire
                  .getResultats().getStatus())) {

         if (resultat.getIdArchivage() != null) {
            // On affecte l'identifiant d'archivage à l'étape de copie
            formCopie.setIdGed(resultat.getIdArchivage());
         } else {
            formCaptureUnitaire.getResultats().setStatus(
                  TestStatusEnum.AControler);
         }
      }
   }

   /**
    * 
    * Methode permettant de faire la copie par appel webService.
    * 
    * @param formulaire
    *           Formulaire de copie {@link TestWsCopieFormulaire}0
    * @param isTestLibre
    *           True si test libre, false sinon.
    * 
    */
   private void copie(final TestWsCopieFormulaire formulaire,
         boolean isTestLibre) {
      CopieResultat copieResult = null;
      CopieFormulaire formCopie = formulaire.getCopie();
      // Appel des services de copie
      if (isTestLibre) {
         copieResult = this.getCopieTestService().appelWsOpCopieTestLibre(
               formulaire.getUrlServiceWeb(), formCopie);
      } else {
         copieResult = this.appelWsOpCopieService(formulaire.getUrlServiceWeb(),formCopie, formulaire.getViFormulaire());
      }

      if (copieResult != null
            && TestStatusEnum.Succes.equals(formCopie.getResultats().getStatus())
            && !(copieResult.getIdGed() != null && StringUtils.isNotEmpty(copieResult.getIdGed().getUuidType()))) {
         formCopie.getResultats().setStatus(TestStatusEnum.AControler);
      }

   }

   /**
    * 
    * Methode permettant de faire la recherche par appel webService.
    * 
    * @param urlWebService
    *           Url du webService
    * @param rechForm
    *           Formulaire
    * @param viForm
    *           Formulaire du VI
    * @param isTestLibre
    *           True si test libre, false sinon.
    */
   private RechercheResponse recherche(final String urlWebService,
         final RechercheFormulaire formulaire, final ViFormulaire viFormulaire,
         boolean isTestLibre) {

      RechercheResponse reponse = null;
      if (isTestLibre) {
         this.getRechercheTestService().appelWsOpRechercheTestLibre(
               urlWebService, formulaire, viFormulaire);
      } else {
         reponse = this.appelWsOpRechercheService(urlWebService, formulaire, viFormulaire);
      }

      return reponse;
   }

   /**
    * Methode permettant de
    * 
    * @param rechercheReponse
    * @param rechercheFormulaire 
    * @param etape 
    * @param isTestLibre
    */
   private void verificationRechercheReponse(RechercheResponse rechercheReponse,
         RechercheFormulaire rechercheFormulaire, TypeRechercheEtape etape, boolean isTestLibre) {
      if (!isTestLibre && rechercheReponse != null
            && rechercheReponse.getRechercheResponse().getResultats() != null
            && rechercheReponse.getRechercheResponse().getResultats()
            .getResultat() != null) {
         ResultatRechercheType resultatRecherche = rechercheReponse
               .getRechercheResponse().getResultats().getResultat()[0];
         verifieResultatRechercheDefaut(resultatRecherche,
               rechercheFormulaire.getResultats(), etape);
      }

   }

   /**
    * 
    * Methode permettant de verifier la recherche avec les valeurs par défaut.
    * 
    * @param resultatRecherche
    *           type de resultat de recherche.
    * @param resultatTest
    *           Resultat du test.
    * @param denomination
    *           denomination.
    * @param numeroResultatRecherche
    *           Numero resultat de recherche.
    * @param etape
    */
   private void verifieResultatRechercheDefaut(
         ResultatRechercheType resultatRecherche, ResultatTest resultatTest,
         TypeRechercheEtape etape) {

      MetadonneeValeurList valeursAttendues = this.getMetadonneesList(etape);

      if (CollectionUtils.isEmpty(valeursAttendues)) {
         resultatTest.setStatus(TestStatusEnum.Echec);
         resultatTest
         .getLog()
         .appendLogLn(
               "La liste de metadonnées à comparer est null ou vide. La comparaison avec le resultat de la recherche est impossible.");
      } else {
         this.getRechercheTestService().verifieResultatRecherche(
               resultatRecherche, etape.toString(), resultatTest,
               valeursAttendues);
      }

   }

   /**
    * Methode permettant de récupérer la liste de metadonnées par défaut
    * permettant de comparer le resultat de la recherche.
    * 
    * @return la liste de metadonnées par défaut.
    */
   protected MetadonneeValeurList getMetadonneesListDefaut(String denomination) {
      MetadonneeValeurList valeursAttendues = new MetadonneeValeurList();

      valeursAttendues.add("ApplicationProductrice", "ADELAIDE");
      valeursAttendues.add("CodeOrganismeGestionnaire", "CER69");
      valeursAttendues.add("CodeOrganismeProprietaire", "AC750");
      valeursAttendues.add("CodeRND", "2.3.1.1.12");
      valeursAttendues.add("DateCreation", "2010-09-23");
      valeursAttendues.add("DateDebutConservation", "2011-09-01");
      valeursAttendues.add("Denomination", denomination);
      valeursAttendues.add("FormatFichier", "fmt/354");
      valeursAttendues.add("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      valeursAttendues.add("NbPages", "2");
      valeursAttendues.add("Titre", "Attestation de vigilance");
      valeursAttendues.add("TypeHash", "SHA-1");

      return valeursAttendues;
   }

   /**
    * 
    * Methode permettant de récupérer le formulaire spécifique pour la capture
    * unitaire.
    * 
    * @param formCaptureUnitaire
    *           Formulaire de capture unitaire
    */
   protected abstract void modificationSpecifiqueFormulaireCaptureUnitaire(
         final CaptureUnitaireFormulaire formCaptureUnitaire);

   /**
    * 
    * Methode permettant de récupérer le formulaire spécifique pour la copie.
    * 
    * @param formCopie
    *           Formulaire de copie
    */
   protected abstract void modificationSpecifiqueFormulaireCopie(
         CopieFormulaire formCopie);

   /**
    * 
    * Methode permettant de récupérer le formulaire spécifique pour la recherche
    * du document existant.
    * 
    * @param formRecherche
    *           Formulaire de recherche
    */
   protected abstract void modificationSpecifiqueFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche);

   /**
    * 
    * Methode permettant de récupérer le formulaire spécifique pour la recherche
    * du document copié.
    * 
    * @param formRecherche
    *           Formulaire de copie
    */
   protected abstract void modificationSpecifiqueFormulaireRechercheDocCopie(
         RechercheFormulaire formRecherche);

   /**
    * Methode permettant d'appeler le service nécessaire pour le copie.
    * 
    * @param viFormulaire Formulaire des paramètres VI {@link ViFormulaire).
    * 
    * @param formulaire Formulaire de copie {@link CopieFormulaire}.
    * @param urlServiceWeb Url d'appel du webservice.
    */
   protected abstract CopieResultat appelWsOpCopieService(String urlServiceWeb,
         CopieFormulaire formulaire, ViFormulaire viFormulaire);

   /**
    * Methode permettant d'appeler le service nécessaire pour la capture unitaire.
    * 
    * @param viFormulaire Formulaire des paramètres VI {@link ViFormulaire).
    * 
    * @param formulaire Formulaire de copie {@link CopieFormulaire}.
    * @param urlServiceWeb Url d'appel du webservice.
    */
   protected abstract CaptureUnitaireResultat appelWsOpCaptureUnitaireService(
         String urlServiceWeb, CaptureUnitaireFormulaire formulaire,
         ViFormulaire viFormulaire);

   /**
    * Methode permettant d'appeler le service nécessaire pour la recherche.
    * 
    * @param viFormulaire Formulaire des paramètres VI {@link ViFormulaire).
    * 
    * @param formulaire Formulaire de copie {@link CopieFormulaire}.
    * @param urlServiceWeb Url d'appel du webservice.
    */
   protected abstract RechercheResponse appelWsOpRechercheService(String urlServiceWeb,
         RechercheFormulaire formulaire, ViFormulaire viFormulaire);


   /**
    * Methode permettant de récupérer la liste de metadonnées du test afin de
    * comparer le resultat de la recherche.
    * 
    * @param etape
    * 
    * @return la liste de metadonnées du test
    */
   protected abstract MetadonneeValeurList getMetadonneesList(TypeRechercheEtape etape);
}
