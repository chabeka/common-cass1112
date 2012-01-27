/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * 
 * 
 */
public class GenericForm {

   /**
    * Formulaire parent
    */
   private TestWsParentFormulaire parent;

   /**
    * Constructeur
    * 
    * @param parent formulaire pere
    */
   public GenericForm(TestWsParentFormulaire parent) {
      this.parent = parent;
   }

   /**
    * @return the parent formulaire pere
    */
   public final TestWsParentFormulaire getParent() {
      return parent;
   }

}
