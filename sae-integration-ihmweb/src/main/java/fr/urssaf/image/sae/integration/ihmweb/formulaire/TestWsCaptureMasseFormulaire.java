package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "archivageMasse" du WS SaeService
 */
public class TestWsCaptureMasseFormulaire extends TestWsParentFormulaire {

   
   private final CaptureMasseFormulaire captureMasse = new CaptureMasseFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "archivageMasse"
    */
   public final CaptureMasseFormulaire getCaptureMasse() {
      return this.captureMasse;
   }

}
