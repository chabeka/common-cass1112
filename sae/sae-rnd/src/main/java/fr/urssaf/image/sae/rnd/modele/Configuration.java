package fr.urssaf.image.sae.rnd.modele;

import org.springframework.stereotype.Component;


/**
 * Classe contenant les paramètres nécessaires à l'application
 * 
 *
 */
public class Configuration {

   /**
    * URL du WS de l'ADRN
    */
   private String urlWsAdrn;

   /**
    * @return the urlWsAdrn
    */
   public final String getUrlWsAdrn() {
      return urlWsAdrn;
   }

   /**
    * @param urlWsAdrn the urlWsAdrn to set
    */
   public final void setUrlWsAdrn(String urlWsAdrn) {
      this.urlWsAdrn = urlWsAdrn;
   }
   
}
