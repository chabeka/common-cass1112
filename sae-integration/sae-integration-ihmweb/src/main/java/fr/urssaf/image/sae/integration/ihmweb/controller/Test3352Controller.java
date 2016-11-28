package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.DenominationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3352")
public class Test3352Controller extends AbstractTest3350SoapFaultController {

   /**
    * Code RND non autorisé.
    */
   private final static String CODE_RND_NON_AUTH = "0.0.0.0.0";

   @Override
   protected String getNumeroTest() {
      return "3352";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireCaptureUnitaire(
         CaptureUnitaireFormulaire formCaptureUnitaire) {
      formCaptureUnitaire.getResultats().setStatus(TestStatusEnum.SansStatus);
      formCaptureUnitaire.getMetadonnees().modifieValeurMeta(
            DenominationEnum.DENOMINATION.toString(),
            DenominationEnum.DENOMINATION_CODE_RND_NON_AUTORISE_KO.toString());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireCopie(
         CopieFormulaire formCopie) {
      formCopie.getResultats().setStatus(TestStatusEnum.SansStatus);
      formCopie.getListeMetadonnees().modifieValeurMeta(
            DenominationEnum.DENOMINATION.toString(),
            DenominationEnum.DENOMINATION_CODE_RND_NON_AUTORISE_KO_COPIE
            .toString());
      formCopie.getListeMetadonnees().modifieValeurMeta("CodeRND", CODE_RND_NON_AUTH);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche) {
      formRecherche
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_CODE_RND_NON_AUTORISE_KO
            .toString()));

      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.DEFAUT);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocCopie(
         RechercheFormulaire formRecherche) {
      formRecherche
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_CODE_RND_NON_AUTORISE_KO_COPIE
            .toString()));

      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.DEFAUT);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected MetadonneeValeurList getMetadonneesList(TypeRechercheEtape etape) {
      MetadonneeValeurList listMetasDefaut = super.getMetadonneesList(etape);

      if (TypeRechercheEtape.RECH_ETAPE_DOC_COPIE.toString().equals(
            etape.toString())) {
         listMetasDefaut.modifieValeurMeta("CodeRND", CODE_RND_NON_AUTH);
      }

      return listMetasDefaut;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getSoapFaultAttendu() {
      return "sae_UnknownCodeRnd";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   Object[] getSoapFaultArguments() {
      return new String[] { CODE_RND_NON_AUTH };
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getDenominationDocCopie() {
      return DenominationEnum.DENOMINATION_CODE_RND_NON_AUTORISE_KO_COPIE
            .toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getDenominationDocExistant() {
      return DenominationEnum.DENOMINATION_CODE_RND_NON_AUTORISE_KO
            .toString();
   }

}
