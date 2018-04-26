package fr.urssaf.image.commons.webservice.ssl;

import org.apache.axis.AxisProperties;

/**
 * Classe utilitaire pour initialiser certaines propriétés de axis
 * 
 * 
 */
public final class InitAxisProperties {

   private InitAxisProperties() {

   }

   /**
    * initialisation de axis.socketSecureFactory
    * 
    * @param classe
    *           valeur de la propriété de type AbstractJSSESocketFactory
    */
   public static void initSoketSecureFactory(
         Class<? extends AbstractJSSESocketFactory> classe) {

      AxisProperties.setProperty("axis.socketSecureFactory", classe
            .getCanonicalName());
   }
}
