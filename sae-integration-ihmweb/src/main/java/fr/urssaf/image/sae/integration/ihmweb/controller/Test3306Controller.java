package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.DenominationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3306")
public class Test3306Controller extends AbstractTest3300OkController {

   @Override
   protected String getNumeroTest() {
      return "3306";
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
            DenominationEnum.DENOMINATION_META_NON_RENSEIGNEE_OK.toString());
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
            DenominationEnum.DENOMINATION_META_NON_RENSEIGNEE_OK_COPIE.toString());
      formCopie.getListeMetadonnees().add(
            new MetadonneeValeur("Periode", "0407"));
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche) {
      formRecherche.setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_META_NON_RENSEIGNEE_OK
            .toString()));

      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.PERIODE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocCopie(
         RechercheFormulaire formRecherche) {
      formRecherche.setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_META_NON_RENSEIGNEE_OK_COPIE
            .toString()));

      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.PERIODE);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected MetadonneeValeurList getMetadonneesList(TypeRechercheEtape etape) {
      MetadonneeValeurList listMetas = null;
      if (TypeRechercheEtape.RECH_ETAPE_DOC_EXISTANT.toString().equals(etape.toString())) {
         listMetas = new MetadonneeValeurList();

         listMetas.add("Denomination",
               DenominationEnum.DENOMINATION_META_NON_RENSEIGNEE_OK.toString());
         listMetas.add("Periode", StringUtils.EMPTY);
      } else if (TypeRechercheEtape.RECH_ETAPE_DOC_COPIE.toString().equals(etape.toString())) {
         listMetas = new MetadonneeValeurList();

         listMetas.add("Denomination",
               DenominationEnum.DENOMINATION_META_NON_RENSEIGNEE_OK_COPIE
                     .toString());
         listMetas.add("Periode", "0407");
      }

      return listMetas;
   }
}
