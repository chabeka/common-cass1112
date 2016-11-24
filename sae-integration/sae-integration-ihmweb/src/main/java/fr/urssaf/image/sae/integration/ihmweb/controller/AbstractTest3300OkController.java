/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.CaptureUnitaireFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.CopieFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.RechercheFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CaptureUnitaireResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.CopieResultat;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.comparator.ResultatRechercheComparator.TypeComparaison;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RechercheResponse;


/**
 * Classe abstraite de gestion des tests 3300 de type resultat attendue OK.
 */
public abstract class AbstractTest3300OkController extends AbstractTest3300Controller {

   /**
    * Nombre de resultat par defaut attendu.
    */
   private final static int NB_RESULTAT_RECH_ATTENDU = 1;

   /**
    * {@inheritDoc}
    */
   @Override
   protected CopieResultat appelWsOpCopieService(String urlServiceWeb,
         CopieFormulaire formulaire, ViFormulaire viFormulaire) {
      return this.getCopieTestService().appelWsOpCopie(urlServiceWeb, formulaire,
            viFormulaire);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected CaptureUnitaireResultat appelWsOpCaptureUnitaireService(
         String urlServiceWeb,
         CaptureUnitaireFormulaire formulaire, ViFormulaire viFormulaire) {
      return this.getCaptureUnitaireTestService()
            .appelWsOpCaptureUnitaireReponseAttendue(urlServiceWeb, formulaire,
                  viFormulaire);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected RechercheResponse appelWsOpRechercheService(String urlServiceWeb,
         RechercheFormulaire formulaire, ViFormulaire viFormulaire) {
      return this.getRechercheTestService().appelWsOpRechercheReponseCorrecteAttendue(
            urlServiceWeb, formulaire, NB_RESULTAT_RECH_ATTENDU, false,
            TypeComparaison.DateCreation, viFormulaire);
   }
}
