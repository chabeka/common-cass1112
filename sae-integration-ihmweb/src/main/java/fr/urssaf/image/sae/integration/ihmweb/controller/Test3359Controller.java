package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.constantes.PagmCodeEnum;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.DenominationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.PagmList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3359")
public class Test3359Controller extends AbstractTest3350SoapFaultController {

   @Override
   protected String getNumeroTest() {
      return "3359";
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
            DenominationEnum.DENOMINATION_VALEUR_DICO_INEXISTANT_KO.toString());
      formCaptureUnitaire.getMetadonnees().add(
            new MetadonneeValeur("NouvelleMetaDico", "dico1_val2"));
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
            DenominationEnum.DENOMINATION_VALEUR_DICO_INEXISTANT_KO_COPIE
            .toString());
      formCopie.getListeMetadonnees().add(
            new MetadonneeValeur("NouvelleMetaDico", "dico1_val10"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche) {
      formRecherche
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_VALEUR_DICO_INEXISTANT_KO
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
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_VALEUR_DICO_INEXISTANT_KO_COPIE
            .toString()));

      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.DEFAUT);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getSoapFaultAttendu() {
      return "sae_MetadataValueNotInDictionary";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   Object[] getSoapFaultArguments() {
      return new String[] { "NouvelleMetaDico" };
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireVI(ViFormulaire viFormulaire) {
      super.modificationSpecifiqueFormulaireVI(viFormulaire);

      PagmList pagmList = viFormulaire.getPagms();
      pagmList.clear();
      pagmList.add(PagmCodeEnum.INT_PAGM_COPIE_ALL.toString());
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getDenominationDocCopie() {
      return DenominationEnum.DENOMINATION_VALEUR_DICO_INEXISTANT_KO_COPIE
            .toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   String getDenominationDocExistant() {
      return DenominationEnum.DENOMINATION_VALEUR_DICO_INEXISTANT_KO.toString();
   }
}
