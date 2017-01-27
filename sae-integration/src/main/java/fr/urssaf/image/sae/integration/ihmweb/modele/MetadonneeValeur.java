package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Une valeur de métadonnée
 */
public class MetadonneeValeur {

   private String code;
   
   private String valeur;

   
   /**
    * Code de la métadonnée
    * 
    * @return Code de la métadonnée
    */
   public final String getCode() {
      return code;
   }

   /**
    * Code de la métadonnée
    * 
    * @param code Code de la métadonnée
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * Valeur de la métadonnée
    * 
    * @return Valeur de la métadonnée
    */
   public final String getValeur() {
      return valeur;
   }

   /**
    * Valeur de la métadonnée
    * 
    * @param valeur Valeur de la métadonnée
    */
   public final void setValeur(String valeur) {
      this.valeur = valeur;
   }
   
   
   /**
    * Constructeur par défaut
    */
   public MetadonneeValeur() {
      // rien à faire ici
   }
   
   
   /**
    * Constructeur
    * 
    * @param code code de la métadonnée
    * @param valeur valeur de la métadonnée
    */
   public MetadonneeValeur(String code,String valeur) {
      this.code = code;
      this.valeur = valeur;
   }
   
   public String toString() {
      return code + "(" + valeur + ")";
   }
}
