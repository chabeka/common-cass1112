/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.model;

/**
 * Classe de modèle d'un contrat de service
 * 
 */
public class ServiceContract {

   /** code intelligible du CS */
   private String libelle;

   /** code de l'organisme client lié au contrat de service */
   private String codeClient;

   /** durée maximum de l'habilitation exprimée en secondes */
   private Long viDuree;

   /** description du contrat de service */
   private String description;

   /**
    * @return le code intelligible du CS
    */
   public final String getLibelle() {
      return libelle;
   }

   /**
    * @param libelle
    *           code intelligible du CS
    */
   public final void setLibelle(String libelle) {
      this.libelle = libelle;
   }

   /**
    * @return le code de l'organisme client lié au contrat de service
    */
   public final String getCodeClient() {
      return codeClient;
   }

   /**
    * @param codeClient
    *           code de l'organisme client lié au contrat de service
    */
   public final void setCodeClient(String codeClient) {
      this.codeClient = codeClient;
   }

   /**
    * @return la durée maximum de l'habilitation exprimée en secondes
    */
   public final Long getViDuree() {
      return viDuree;
   }

   /**
    * @param viDuree
    *           durée maximum de l'habilitation exprimée en secondes
    */
   public final void setViDuree(Long viDuree) {
      this.viDuree = viDuree;
   }

   /**
    * @return la description du contrat de service
    */
   public final String getDescription() {
      return description;
   }

   /**
    * @param description
    *           description du contrat de service
    */
   public final void setDescription(String description) {
      this.description = description;
   }

}
