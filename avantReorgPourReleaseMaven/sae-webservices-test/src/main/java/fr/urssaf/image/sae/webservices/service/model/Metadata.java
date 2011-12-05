package fr.urssaf.image.sae.webservices.service.model;

/**
 * Modèle d'une métadonnée pour les messages SOAP.<br>
 * <ul>
 * <li>
 * code : code de la métadonnée</li>
 * <li>
 * value : valeur de la métadonnée</li>
 * </ul>
 * 
 */
public class Metadata {

   private String code;

   private String value;

   /**
    * @return code de la métadonnée
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           code de la métadonnée
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return valeur de la métadonnée
    */
   public final String getValue() {
      return value;
   }

   /**
    * @param value
    *           valeur de la métadonnée
    */
   public final void setValue(String value) {
      this.value = value;
   }

}
