package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "archivageUnitaire" du WS SaeService
 */
public class TestWsCaptureUnitaireFormulaire extends TestWsParentFormulaire {

   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "archivageUnitaire"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "archivageUnitaire"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   }

}
