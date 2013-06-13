/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de modèle d'une trace du registre de sécurité
 * 
 */
public class TraceRegSecurite extends Trace {

 
   /** Contexte de la trace */
   private String contexte;
   
   
   public TraceRegSecurite(UUID idTrace, Date timestamp) {
      super(idTrace, timestamp);
   }

   public TraceRegSecurite(TraceToCreate trace, List<String> listInfos,
         UUID idTrace, Date timestamp) {
      
      super(trace, listInfos, idTrace, timestamp);
      this.contexte = trace.getContexte();

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
