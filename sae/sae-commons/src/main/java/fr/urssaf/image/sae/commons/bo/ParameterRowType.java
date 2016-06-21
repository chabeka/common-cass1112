package fr.urssaf.image.sae.commons.bo;

/**
 * Ligne de paramètre de configuration
 * 
 */
public enum ParameterRowType {

   /** Nom correspondant à la ligne contenant les paramètres de tracabilite */
   TRACABILITE {

      @Override
      public String toString() {
         return "parametresTracabilite";
      }
   },

   /** Nom correspondant à la ligne contenant les paramètres de purge de la corbeille */
   CORBEILLE {

      @Override
      public String toString() {
         return "parametresCorbeille";
      }
   },
   
   /**
    * Nom correspondant à la ligne contenant les paramètres du RND
    */
   parametresRnd

}
