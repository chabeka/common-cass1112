package fr.urssaf.image.sae.vi.modele;

/**
 * Classe pour un droit applicatif dans le jeton d'authentification<br>
 * <br>
 * L'instanciation est uniquement possible avec
 * {@link ObjectFactory#createDroitAplicatif()}
 * 
 */
public class DroitApplicatif {

   private String code;

   private String perimetreType;

   private String perimetreValue;

   @SuppressWarnings("PMD.UncommentedEmptyConstructor")
   protected DroitApplicatif() {

   }

   /**
    * 
    * @param code
    *           droit applicatof
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * 
    * @return droit applicatif
    */
   public final String getCode() {
      return code;
   }

   /**
    * 
    * @return type du périmètre de données
    */
   public final String getPerimetreType() {
      return perimetreType;
   }

   /**
    * 
    * @param perimetreType
    *           type du périmètre de données
    */
   public final void setPerimetreType(String perimetreType) {
      this.perimetreType = perimetreType;
   }

   /**
    * 
    * @return périmètre de données
    */
   public final String getPerimetreValue() {
      return perimetreValue;
   }

   /**
    * 
    * @param perimetreValue
    *           périmètre de données
    */
   public final void setPerimetreValue(String perimetreValue) {
      this.perimetreValue = perimetreValue;
   }

}
