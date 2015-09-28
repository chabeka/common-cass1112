package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour les tests qui comprennent uniquement un appel à
 * l'opération "ajoutnote" du WS SaeService
 */
public class TestWsAjoutNoteFormulaire extends TestWsParentFormulaire {

   private final AjoutNoteFormulaire ajoutNote = new AjoutNoteFormulaire(this) ;
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "ajoutNote"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "ajoutNote"
    */
   public final AjoutNoteFormulaire getAjoutNote() {
      return this.ajoutNote;
   }

}
