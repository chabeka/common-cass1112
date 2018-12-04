package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "ajoutNote"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "ajoutNote.tag" (attribut
 * "objetFormulaire")
 */
public class AjoutNoteFormulaire  extends GenericForm {
   
   private ResultatTest resultats = new ResultatTest();

   private String idArchivage;
   
   private String note;
   
   /**
    * @param parent
    */
   public AjoutNoteFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   /**
    * Constructeur
    * 
    */
   public AjoutNoteFormulaire() {
      super();
   }

   /**
    * Les résultats de l'appel à l'opération
    * 
    * @return Les résultats de l'appel à l'opération
    */
   public final ResultatTest getResultats() {
      return this.resultats;
   }

   /**
    * Les résultats de l'appel à l'opération
    * 
    * @param resultats
    *           Les résultats de l'appel à l'opération
    */
   public final void setResultats(ResultatTest resultats) {
      this.resultats = resultats;
   }

   /**
    * L'identifiant d'archivage du document auquel on souhaite attacher une note
    * 
    * @return L'identifiant d'archivage de l'archive que l'on souhaite attacher une note
    */
   public final String getIdArchivage() {
      return idArchivage;
   }

   /**
    * L'identifiant d'archivage du document auquel on souhaite ajouter une note
    * 
    * @param idArchivage
    *           L'identifiant d'archivage du document auquel on souhaite
    *           ajouter une note
    */
   public final void setIdArchivage(String idArchivage) {
      this.idArchivage = idArchivage;
   }

   /**
    * Contenu de la note à attacher au document 
    * 
    * @return Contenu de la note
    */
   public String getNote() {
      return note;
   }
   
   /**
    * Note à attacher au document 
    * 
    * @param note
    *           La note à ajouter
    */ 
   public void setNote(String note) {
      this.note = note;
   }

}
