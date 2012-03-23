package fr.urssaf.image.sae.integration.ihmweb.modele.piletravaux;

import java.util.UUID;

/**
 * Propriétés principales des traitements dans la pile des travaux. Les propriétés sont :
 * <ul>
 * <li><code>idJob</code>: identifiant unique du traitement</li>
 * <li><code>type</code>: type de traitement</li>
 * <li><code>parameters</code>: paramètres du traitement</li>
 * </ul>
 * 
 * 
 */
public class SimpleJobRequest {

   private UUID idJob;

   private String type;

   private String parameters;


   /**
    * @return the idJob
    */
   public final UUID getIdJob() {
      return idJob;
   }

   /**
    * @param idJob
    *           the idJob to set
    */
   public final void setIdJob(UUID idJob) {
      this.idJob = idJob;
   }

   /**
    * @return the type
    */
   public final String getType() {
      return type;
   }

   /**
    * @param type
    *           the type to set
    */
   public final void setType(String type) {
      this.type = type;
   }

   /**
    * @return the parameters
    */
   public final String getParameters() {
      return parameters;
   }

   /**
    * @param parameters
    *           the parameters to set
    */
   public final void setParameters(String parameters) {
      this.parameters = parameters;
   }

}
