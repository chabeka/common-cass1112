package fr.urssaf.image.sae.integration.ihmweb.modele;

import java.util.UUID;


/**
 * Une valeur de métadonnée
 */
public class IdentifiantPage {

   private String valeur;
   
   private UUID idArchive;
   
   
   
   /**
    * Constructeur par défaut
    */
   public IdentifiantPage() {
      // rien à faire ici
   }
   
   
   /**
    * Constructeur
    * 
    * @param code code de la métadonnée
    * @param valeur valeur de la métadonnée
    */
   public IdentifiantPage(String valeur, UUID idArchive) {
      this.valeur = valeur;
      this.idArchive = idArchive;
   }

   /**
    * @return the valeur
    */
   public String getValeur() {
      return valeur;
   }

   /**
    * @param valeur the valeur to set
    */
   public void setValeur(String valeur) {
      this.valeur = valeur;
   }

   /**
    * @return the idArchive
    */
   public UUID getIdArchive() {
      return idArchive;
   }

   /**
    * @param idArchive the idArchive to set
    */
   public void setIdArchive(UUID idArchive) {
      this.idArchive = idArchive;
   }
   
}
