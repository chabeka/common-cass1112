package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "modification" du WS SaeService
 */
public class TestWsModificationFormulaire extends TestWsParentFormulaire {

   private final ModificationFormulaire modification = new ModificationFormulaire(
         this);

   /**
    * Le sous-formulaire pour l'appel à l'opération "modification"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "modification"
    */
   public final ModificationFormulaire getModification() {
      return this.modification;
   }

}
