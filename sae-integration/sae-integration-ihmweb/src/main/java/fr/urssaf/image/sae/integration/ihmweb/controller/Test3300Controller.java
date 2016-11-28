package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CopieResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;

@Controller
@RequestMapping(value = "test3300")
public class Test3300Controller extends AbstractTest3300Controller {

   @Override
   protected String getNumeroTest() {
      return "3300";
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireCaptureUnitaire(
         CaptureUnitaireFormulaire formCaptureUnitaire) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireCopie(
         CopieFormulaire formCopie) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocExistant(
         RechercheFormulaire formRecherche) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireRechercheDocCopie(
         RechercheFormulaire formRecherche) {
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected CopieResultat appelWsOpCopieService(String urlServiceWeb,
         CopieFormulaire formulaire, ViFormulaire viFormulaire) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected CaptureUnitaireResultat appelWsOpCaptureUnitaireService(
         String urlServiceWeb, CaptureUnitaireFormulaire formulaire,
         ViFormulaire viFormulaire) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected RechercheResponse appelWsOpRechercheService(String urlServiceWeb,
         RechercheFormulaire formulaire, ViFormulaire viFormulaire) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected MetadonneeValeurList getMetadonneesList(TypeRechercheEtape etape) {
      return null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected void modificationSpecifiqueFormulaireVI(ViFormulaire viFormulaire) {
   }

}
