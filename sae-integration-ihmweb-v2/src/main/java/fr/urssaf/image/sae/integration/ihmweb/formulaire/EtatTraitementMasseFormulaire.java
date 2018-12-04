package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.UUIDList;

/**
 * Classe de sous-formulaire pour un test du WS SaeService, opération
 * "etatTraitementMasse"<br>
 * <br>
 * Un objet de cette classe s'associe au tag "etatTraitementMasse.tag" (attribut
 * "objetFormulaire")
 */
public class EtatTraitementMasseFormulaire extends GenericForm {

   private ResultatTest resultats = new ResultatTest();

   private UUIDList requeteListeUUID = new UUIDList();

   /**
    * Constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public EtatTraitementMasseFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }
   
   
   /**
    * Constructeur
    * 
    */
   public EtatTraitementMasseFormulaire() {

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
   {}

   
   /**
    * La requête liste UUID
    * 
    * @return La requête liste UUID
    */
   public final UUIDList getRequeteListeUUID() {
      return requeteListeUUID;
   }

   /**
    * La requête  liste UUID
    * 
    * @param requeteListeUUID
    *           La requête LUCENE
    */
   public final void setRequeteListeUUID(UUIDList requeteListeUUID) {
      this.requeteListeUUID = requeteListeUUID;
   }
   
}