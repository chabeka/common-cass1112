package fr.urssaf.image.sae.regionalisation.bean;

/**
 * Classe représentant une métadonnée.
 * 
 * 
 */
public class Metadata {

   private String code;

   private Object value;

   private boolean flag;

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
    * @return nouvelle valeur de la métadonnée
    */
   public final Object getValue() {
      return value;
   }

   /**
    * @param value
    *           nouvelle valeur de la métadonnée
    */
   public final void setValue(Object value) {
      this.value = value;
   }

   /**
    * @return <code>true</code> si la métadonnée est à modifier,
    *         <code>false</code> sinon
    */
   public final boolean isFlag() {
      return flag;
   }

   /**
    * @param flag
    *           <code>true</code> si la métadonnée est à modifier,
    *           <code>false</code> sinon
    */
   public final void setFlag(boolean flag) {
      this.flag = flag;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("Metadata [code=").append(code).append(", flag=").append(
            flag).append(", value=").append(value).append("]");
      return builder.toString();
   }

}
