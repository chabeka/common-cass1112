package fr.urssaf.image.sae.integration.ihmweb.formulaire;



/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "archivageMasse" du WS SaeService
 */
public class Test1152Formulaire extends TestWsParentFormulaire {

   
   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   /**
    * Le sous formulaire de la capture unitaire
    * @return le sous formulaire de capture unitaire
    */

   public CaptureUnitaireFormulaire getCaptureUnitaire() {
      return captureUnitaire;
   }

}
