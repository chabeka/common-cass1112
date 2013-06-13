/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import fr.urssaf.image.sae.trace.model.TraceToCreate;

/**
 * Classe de modèle d'une trace du registre de surveillance technique
 * 
 */
public class TraceRegTechnique extends Trace {

   /** Contexte de la trace */
   private String contexte;

   /** Trace technique de l'exception */
   private String stacktrace;

   /**
    * Constructeur
    * 
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
   public TraceRegTechnique(UUID idTrace, Date timestamp) {
      super(idTrace, timestamp);
   }

   /**
    * Constructeur
    * 
    * @param trace
    *           trace d'origine
    * @param listInfos
    *           liste des informations supplémentaires à récupérer
    * @param idTrace
    *           l'identifiant unique à affecter à la trace
    * @param timestamp
    *           le timestamp à affecter à la trace
    */
   public TraceRegTechnique(TraceToCreate trace, List<String> listInfos,
         UUID idTrace, Date timestamp) {

      super(trace, listInfos, idTrace, timestamp);
      this.contexte = trace.getContexte();
      this.stacktrace = trace.getStracktrace();
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
    * @return la Trace technique de l'exception
    */
   public final String getStacktrace() {
      return stacktrace;
   }

   /**
    * @param stacktrace
    *           Trace technique de l'exception
    */
   public final void setStacktrace(String stacktrace) {
      this.stacktrace = stacktrace;
   }

}
