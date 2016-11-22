/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsCopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CodeMetadonneeList;
import fr.urssaf.image.sae.integration.ihmweb.modele.DenominationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.utils.LuceneUtils;

/**
 * 
 * 
 */
public abstract class AbstractTest3300Controller extends AbstractTestWsController<TestWsCopieFormulaire> {

   /**
    * Document de copie OK cas simple dans ECDE.
    */
   private static final String DOC_TEST_3301_COPIE_OK_CAS_SIMPLE = "SAE_INTEGRATION/20110822/Copie-3301-Copie-OK-CasSimple/documents/doc1.PDF";

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
      this.modifierFormulaireRechercheDocExistant(formulaire.getRechercheDocExistant());

      // Modification du formulaire de la recherche du document existant
      this.modifierFormulaireRechercheDocCopie(formulaire.getRechercheDocCopie());

      return formulaire;
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
      this.modificationSpecifiqueFormulaireRechercheDocCopie(formRecherche);

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
      formRecherche.setRequeteLucene(determineRequeteLucene(luceneExempleExtract));
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
      formRecherche.setCodeMetadonnees(metas);
      formRecherche.getResultats().setStatus(TestStatusEnum.SansStatus);
   }

   /**
    * Methode permettant de determiner la requete LUCENE à fournir pour les
    * tests.
    * 
    * @param luceneExempleExtract
    *           Extrait de la requete.
    * @return la requete LUCENE à fournir pour les tests
    */
   private String determineRequeteLucene(String luceneExempleExtract) {
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
      formCopie.setIdGed(SaeServiceTestService.getIdArchivageExemple());

      // Des métadonnées exemples
      metas.modifieValeurMeta(DenominationEnum.DENOMINATION.toString(),
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
    * {@inheritDoc}
    */
   @Override
   protected void doPost(TestWsCopieFormulaire formulaire) {
      String etape = formulaire.getEtape();

      if ("capture_unitaire_copie".equals(etape)) {
         this.captureUnitaire(formulaire);
      } else if ("copie".equals(etape)) {
         this.copie(formulaire);
      } else if ("recherche_document_copie".equals(etape)) {
         this.recherche(formulaire.getUrlServiceWeb(),
               formulaire.getRechercheDocCopie(), formulaire.getViFormulaire());
      } else if ("recherche_document_existant".equals(etape)) {
         this.recherche(formulaire.getUrlServiceWeb(),
               formulaire.getRechercheDocExistant(),
               formulaire.getViFormulaire());
      } else {
         throw new IntegrationRuntimeException("L'étape " + etape
               + " est inconnue !");

      }
   }

   /**
    * Methode permettant de faire une capture unitaire par appel de webservice.
    * 
    * @param formulaire
    *           Formulaire de test copie
    */
   private void captureUnitaire(final TestWsCopieFormulaire formulaire) {

      CaptureUnitaireFormulaire formCaptureUnitaire = formulaire
            .getCaptureUnitaire();

      CopieFormulaire formCopie = formulaire.getCopie();
      formCopie.getResultats().clear();
      formCopie.setIdGed(null);
      
      RechercheFormulaire formRechercheCopie = formulaire.getRechercheDocCopie();
      formRechercheCopie.getResultats().clear();
      
      RechercheFormulaire formRechercheExistant = formulaire.getRechercheDocExistant();
      formRechercheExistant.getResultats().clear();
      
      // Appel de la méthode de test
      CaptureUnitaireResultat resultat = getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireUrlEcdeTestLibre(
            formulaire.getUrlServiceWeb(), formCaptureUnitaire,
            formulaire.getViFormulaire());
      
      
      // Si le test est en succès ...
      if (formCaptureUnitaire.getResultats().getStatus()
            .equals(TestStatusEnum.Succes)
            || formCaptureUnitaire.getResultats().getStatus()
                  .equals(TestStatusEnum.SansStatus)) {

         // On affecte l'identifiant d'archivage à l'étape de copie
         formCopie.setIdGed(resultat.getIdArchivage());

      }
   }

   /**
    * 
    * Methode permettant de faire la copie par appel webService
    * 
    * @param formulaire
    *           Formulaire de test copie
    */
   private void copie(final TestWsCopieFormulaire formulaire) {
      // Appel de la méthode de test
      this.getCopieTestService().appelWsOpCopieTestLibre(
            formulaire.getUrlServiceWeb(), formulaire.getCopie());

   }

   /**
    * 
    * Methode permettant de faire la recherche par appel webService
    * 
    * @param urlWebService
    *           Url du webService
    * @param rechForm
    *           Formulaire
    * @param viForm
    *           Formulaire du VI
    */
   private void recherche(final String urlWebService,
         final RechercheFormulaire rechForm, final ViFormulaire viForm) {

      this.getRechercheTestService().appelWsOpRechercheTestLibre(urlWebService,
            rechForm, viForm);

   }

   /**
    * 
    * Methode permettant de récupérer le formulaire spécifique pour la capture
    * unitaire.
    * 
    * @param formCaptureUnitaire
    *           Formulaire de capture unitaire
    * @return le formulaire spécifique pour la capture unitaire.
    */
   protected abstract void modificationSpecifiqueFormulaireCaptureUnitaire(
         final CaptureUnitaireFormulaire formCaptureUnitaire);

   /**
    * 
    * Methode permettant de récupérer le formulaire spécifique pour la copie.
    * 
    * @param formCopie
    *           Formulaire de copie
    * @return le formulaire spécifique pour la copie.
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
    * @return le formulaire spécifique pour la recherche du document existant.
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
    * @return le formulaire spécifique pour la recherche du document copié.
    */
   protected abstract void modificationSpecifiqueFormulaireRechercheDocCopie(
         RechercheFormulaire formRecherche);
}
