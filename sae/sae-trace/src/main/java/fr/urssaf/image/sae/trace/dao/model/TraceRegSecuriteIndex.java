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

}
