/**
 * 
 */
package fr.urssaf.image.sae.trace.model;

/**
 * Paramètre de configuration
 * 
 */
public class Parameter {

   private final ParameterType name;

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
      super();
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
