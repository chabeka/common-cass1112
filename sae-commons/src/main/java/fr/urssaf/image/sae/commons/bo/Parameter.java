/**
 * 
 */
package fr.urssaf.image.sae.commons.bo;

/**
 * Objet contenant un paramètre
 * 
 */
public class Parameter {

   /** Nom du paramètre */
   private final ParameterType name;

   /** valeur du paramètre */
   private final Object value;

   /**
    * Constructeur
    * 
    * @param name
    *           nom du paramètre
    * @param value
    *           valeur du paramètre
    */
   public Parameter(ParameterType name, Object value) {
      this.name = name;
      this.value = value;
   }

   /**
    * @return le nom du paramètre
    */
   public final ParameterType getName() {
      return name;
   }

   /**
    * @return la valeur du paramètre
    */
   public final Object getValue() {
      return value;
   }

}
