package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.DenominationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3354")
public class Test3354Controller extends AbstractTest3350SoapFaultController {

   @Override
   protected String getNumeroTest() {
      return "3354";
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
            DenominationEnum.DENOMINATION_META_EN_DOUBLE_KO.toString());
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
            DenominationEnum.DENOMINATION_META_EN_DOUBLE_KO_COPIE
            .toString());
      formCopie.getListeMetadonnees().modifieValeurMeta("CodeRND", "3.1.2.1.2");
      formCopie.getListeMetadonnees().add(
            new MetadonneeValeur("CodeRND", "2.3.1.1.12"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche) {
      formRecherche
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_META_EN_DOUBLE_KO
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
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_META_EN_DOUBLE_KO_COPIE
            .toString()));

      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.DEFAUT);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getSoapFaultAttendu() {
      return "sae_DuplicatedMetadata";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   Object[] getSoapFaultArguments() {
      return new String[] { "CodeRND" };
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getDenominationDocCopie() {
      return DenominationEnum.DENOMINATION_META_EN_DOUBLE_KO_COPIE.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getDenominationDocExistant() {
      return DenominationEnum.DENOMINATION_META_EN_DOUBLE_KO.toString();
   }

}