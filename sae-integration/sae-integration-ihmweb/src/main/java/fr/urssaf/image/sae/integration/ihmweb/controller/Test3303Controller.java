package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.DenominationEnum;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestStatusEnum;

@Controller
@RequestMapping(value = "test3303")
public class Test3303Controller extends AbstractTest3300OkController {

   @Override
   protected String getNumeroTest() {
      return "3303";
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
            DenominationEnum.DENOMINATION_SANS_CODE_ACTIVITE_OK.toString());
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
            DenominationEnum.DENOMINATION_SANS_CODE_ACTIVITE_OK_COPIE
            .toString());

      formCopie.getListeMetadonnees().modifieValeurMeta("CodeRND", "1.A.X.X.X");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche) {
      formRecherche
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_SANS_CODE_ACTIVITE_OK
            .toString()));
      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.ACTIVITE_FONCTION);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocCopie(
         RechercheFormulaire formRecherche) {
      formRecherche
      .setRequeteLucene(determineRequeteLucene(DenominationEnum.DENOMINATION_SANS_CODE_ACTIVITE_OK_COPIE
            .toString()));
      this.initialiseMetadonnees(formRecherche.getCodeMetadonnees(),
            GroupMetasType.ACTIVITE_FONCTION);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected MetadonneeValeurList getMetadonneesList(TypeRechercheEtape etape) {
      MetadonneeValeurList listMetas = null;
      if (TypeRechercheEtape.RECH_ETAPE_DOC_EXISTANT.toString().equals(etape.toString())) {
         listMetas = new MetadonneeValeurList();

         listMetas.add("CodeActivite", "3");
         listMetas.add("CodeFonction", "2");
         listMetas.add("CodeRND", "2.3.1.1.12");
         listMetas.add("Titre", "Attestation de vigilance");
      } else if (TypeRechercheEtape.RECH_ETAPE_DOC_COPIE.toString().equals(etape.toString())) {
         listMetas = new MetadonneeValeurList();

         listMetas.add("CodeActivite", StringUtils.EMPTY);
         listMetas.add("CodeFonction", "1");
         listMetas.add("CodeRND", "1.A.X.X.X");
         listMetas.add("Titre", "Attestation de vigilance");
      }

      return listMetas;
   }
}
