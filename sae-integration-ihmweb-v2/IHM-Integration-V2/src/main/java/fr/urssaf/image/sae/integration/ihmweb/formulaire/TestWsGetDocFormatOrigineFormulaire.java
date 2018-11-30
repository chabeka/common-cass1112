package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "consultation" du WS SaeService
 */
public class TestWsGetDocFormatOrigineFormulaire extends TestWsParentFormulaire {

   private final GetDocFormatOrigineFormulaire getDocFormatOrigine = new GetDocFormatOrigineFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "getDocFormatOrigine"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "getDocFormatOrigine"
    */
   public final GetDocFormatOrigineFormulaire getGetDocFormatOrigine() {
      return this.getDocFormatOrigine;
   }

}
