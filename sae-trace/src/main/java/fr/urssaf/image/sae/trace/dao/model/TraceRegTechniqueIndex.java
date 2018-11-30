/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;


/**
 * Classe de modèle de la CF "TraceRegTechniqueIndex"
 * 
 */
public class TraceRegTechniqueIndex extends TraceIndex {

   /** Contexte de la trace */
   private String contexte;
   
   /**
    * Code du contrat de service
    */
   private String contrat;

   /**
    * constructeur par défaut
    */
   public TraceRegTechniqueIndex() {
      super();
   }

   /**
    * Constructeur
    * 
    * @param exploitation
    *           trace technique
    */
   public TraceRegTechniqueIndex(TraceRegTechnique exploitation) {
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
