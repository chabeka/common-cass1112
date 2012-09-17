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

   /** identifiant de la PKI attendue lors de la vérification des droits */
   private String idPki;

   /** flag indiquant si la vérification de nommage doit être réalisée ou non */
   private boolean verifNommage;

   /**
    * identifiant du certificat client attendu lors de la vérification des
    * droits
    */
   private String idCertifClient;

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

   /**
    * @return l'identifiant de la PKI attendue lors de la vérification des
    *         droits
    */
   public final String getIdPki() {
      return idPki;
   }

   /**
    * @param idPki
    *           l'identifiant de la PKI attendue lors de la vérification des
    *           droits
    */
   public final void setIdPki(String idPki) {
      this.idPki = idPki;
   }

   /**
    * @return l'identifiant du certificat client attendu lors de la vérification
    *         des droits
    */
   public final String getIdCertifClient() {
      return idCertifClient;
   }

   /**
    * @param idCertifClient
    *           identifiant du certificat client attendu lors de la vérification
    *           des droits
    */
   public final void setIdCertifClient(String idCertifClient) {
      this.idCertifClient = idCertifClient;
   }

   /**
    * @return le flag de vérification de nommage
    */
   public final boolean isVerifNommage() {
      return verifNommage;
   }

   /**
    * @param verifNommage
    *           le flag de vérification de nommage
    */
   public final void setVerifNommage(boolean verifNommage) {
      this.verifNommage = verifNommage;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean equals(Object obj) {
      boolean areEquals = false;

      if (obj instanceof ServiceContract) {
         ServiceContract contract = (ServiceContract) obj;

         areEquals = codeClient.equals(contract.getCodeClient())
               && description.equals(contract.getDescription())
               && libelle.equals(contract.getLibelle())
               && viDuree.equals(contract.getViDuree());

      }

      return areEquals;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final int hashCode() {
      return super.hashCode();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {

      return "code client : " + codeClient + "\n" + "description : "
            + description + "\n" + "libellé : " + libelle + "\n"
            + "durée vi : " + viDuree.toString() + "\n";
   }

}
