/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;


/**
 * Classe de modèle de la CF "TraceRegSecuriteIndex"
 * 
 */
public class TraceRegSecuriteIndex extends TraceIndex {

   /** Contexte de la trace */
   private String contexte;

   /**
    * Code du contrat de service
    */
   private String contrat;
   
   /**
    * Constructeur par défaut
    */
   public TraceRegSecuriteIndex() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param exploitation
    *           trace de sécurité
    */
   public TraceRegSecuriteIndex(TraceRegSecurite exploitation) {
      super(exploitation);
      this.contexte = exploitation.getContexte();
      this.contrat = exploitation.getContratService();
   }

   /**
    * @return le Contexte de la trace
    */
   public final String getContexte() {
      return contexte;
   }

   /**
    * @param contexte
    *           Contexte de la trace
    */
   public final void setContexte(String contexte) {
      this.contexte = contexte;
   }

   /**
    * @return the contrat
    */
   public String getContrat() {
      return contrat;
   }

   /**
    * @param contrat the contrat to set
    */
   public void setContrat(String contrat) {
      this.contrat = contrat;
   }
}
