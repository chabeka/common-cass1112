package fr.urssaf.image.sae.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe représentant une métadonnée de test
 * 
 * @author rhofir
 */
@XStreamAlias("metadata")
public class SAEMockMetadata {
   private String code;
   private String value;

   /**
    * @return Code de la métadonnée
    */
   public final String getCode() {
      return code;
   }

   /**
    * @param code
    *           :Code de la métadonnée
    */
   public final void setCode(String code) {
      this.code = code;
   }

   /**
    * @return Valeur de la métadonnée.
    */
   public final String getValue() {
      return value;
   }

   /**
    * @param value
    *           : Valeur de la métadonnée.
    */
   public final void setValue(String value) {
      this.value = value;
   }

}
