package fr.urssaf.image.sae.saml.opensaml;

import org.opensaml.DefaultBootstrap;
import org.opensaml.xml.ConfigurationException;

/**
 * Classe d'initialisation de la librairie OpenSAML<br>
 * Il est indispensable d'instancier cette classe avant d'instancier les autres
 * classes<br>
 * 
 * <pre>
 * new SamlConfiguration();
 * ...
 * </pre>
 * 
 * 
 */
public class SamlConfiguration {

   /**
    * appel de la méthode {@link DefaultBootstrap#bootstrap()}<br>
    * <br>
    * 
    * throws IllegalStateException : {@link ConfigurationException} est levé
    */
   public SamlConfiguration() {

      try {
         DefaultBootstrap.bootstrap();
      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }
   }
}
