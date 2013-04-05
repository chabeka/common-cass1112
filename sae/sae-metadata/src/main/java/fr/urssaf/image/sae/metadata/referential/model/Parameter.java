package fr.urssaf.image.sae.metadata.referential.model;

public class Parameter {

   private String id;
   
   private Object value;
   
   /**
    * Constructeur de la classe Parameter
    * @param id identifiant du paramètre
    * @param value valeur du paramètre
    */
   public Parameter(String id, Object value){
      
   }

   /**
    * @return identifiant du parametre
    */
   public String getId() {
      return id;
   }

   /**
    * @param id identifiant du paramètre
    */
   public void setId(String id) {
      this.id = id;
   }

   /**
    * @return valeur du paramètre
    */
   public Object getValue() {
      return value;
   }

   /**
    * @param value Valeur du paramètre.
    */
   public void setValue(Object value) {
      this.value = value;
   }
}
