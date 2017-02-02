package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Résultat d'un appel au service web de capture unitaire
 */
public class CaptureUnitaireResultat {

   private String idArchivage;
   private String sha1;
   
   
   /**
    * L'identifiant d'archivage renvoyé par l'opération de capture unitaire
    * 
    * @return L'identifiant d'archivage renvoyé par l'opération de capture unitaire
    */
   public final String getIdArchivage() {
      return idArchivage;
   }
   
   
   /**
    * L'identifiant d'archivage renvoyé par l'opération de capture unitaire
    * 
    * @param idArchivage L'identifiant d'archivage renvoyé par l'opération de capture unitaire
    */
   public final void setIdArchivage(String idArchivage) {
      this.idArchivage = idArchivage;
   }
   
   
   /**
    * Le SHA-1 du fichier document envoyé (calculé par ce programme de test)
    * 
    * @return Le SHA-1 du fichier document envoyé (calculé par ce programme de test)
    */
   public final String getSha1() {
      return sha1;
   }
   
   
   /**
    * Le SHA-1 du fichier document envoyé (calculé par ce programme de test)
    * 
    * @param sha1 Le SHA-1 du fichier document envoyé (calculé par ce programme de test)
    */
   public final void setSha1(String sha1) {
      this.sha1 = sha1;
   }
   
   
}
