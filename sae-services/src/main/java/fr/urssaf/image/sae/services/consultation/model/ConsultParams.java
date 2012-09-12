/**
 * 
 */
package fr.urssaf.image.sae.services.consultation.model;

import java.util.List;
import java.util.UUID;

/**
 * Objet regroupant les paramètres nécessaires à la consultation. Dans cette
 * spécification, les paramètres définis sont l'identifiant unique du document
 * et la liste des métadonnées saisies
 * 
 */
public class ConsultParams {

   /**
    * Identifiant du document à consulter
    */
   private UUID idArchive;

   /**
    * Liste de code long de métadonnées désirées à la consultation
    */
   private List<String> metadonnees;

   
   /**
    * Constructeur
    * 
    * @param idArchive
    *           Identifiant du document à consulter
    */
   public ConsultParams(UUID idArchive) {
      super();
      this.idArchive = idArchive;
   }

   /**
    * Constructeur
    * 
    * @param idArchive
    *           Identifiant du document à consulter
    * @param metadonnees
    *           Liste de code long de métadonnées désirées à la consultation
    */
   public ConsultParams(UUID idArchive, List<String> metadonnees) {
      this.idArchive = idArchive;
      this.metadonnees = metadonnees;
   }

   /**
    * @return the idArchive Identifiant du document à consulter
    */
   public final UUID getIdArchive() {
      return idArchive;
   }

   /**
    * @param idArchive
    *           Identifiant du document à consulter
    */
   public final void setIdArchive(UUID idArchive) {
      this.idArchive = idArchive;
   }

   /**
    * @return Liste de code long de métadonnées désirées à la consultation
    */
   public final List<String> getMetadonnees() {
      return metadonnees;
   }

   /**
    * @param metadonnees
    *           Liste de code long de métadonnées désirées à la consultation
    */
   public final void setMetadonnees(List<String> metadonnees) {
      this.metadonnees = metadonnees;
   }

}
