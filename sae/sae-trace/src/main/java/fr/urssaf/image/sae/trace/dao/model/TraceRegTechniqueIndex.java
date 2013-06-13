/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Classe de modèle de la CF "TraceRegTechniqueIndex"
 * 
 */
public class TraceRegTechniqueIndex extends TraceIndex {

   /** Contexte de la trace */
   private String contexte;

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
