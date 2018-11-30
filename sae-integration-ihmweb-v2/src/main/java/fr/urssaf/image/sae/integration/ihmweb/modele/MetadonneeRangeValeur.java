package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Une valeur de métadonnée
 */
public class MetadonneeRangeValeur {

   private String code;
   
   private String valeurMin;
   
   private String valeurMax;

   
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
    * Constructeur par défaut
    */
   public MetadonneeRangeValeur() {
      // rien à faire ici
   }
   
   
   /**
    * Constructeur
    * 
    * @param code code de la métadonnée
    * @param valeurMin valeurMin de la métadonnée
    * @param valeurMax valeurMax de la métadonnée
    */
   public MetadonneeRangeValeur(String code,String valeurMin, String valeurMax) {
      this.code = code;
      this.valeurMin = valeurMin;
      this.valeurMax = valeurMax;
   }

   /**
    * @return the valeurMin
    */
   public String getValeurMin() {
      return valeurMin;
   }

   /**
    * @param valeurMin the valeurMin to set
    */
   public void setValeurMin(String valeurMin) {
      this.valeurMin = valeurMin;
   }

   /**
    * @return the valeurMax
    */
   public String getValeurMax() {
      return valeurMax;
   }

   /**
    * @param valeurMax the valeurMax to set
    */
   public void setValeurMax(String valeurMax) {
      this.valeurMax = valeurMax;
   }
   
   public String toString() {
      return code + "(" + valeurMin + "/" + valeurMax + ")";
   }
   
}
