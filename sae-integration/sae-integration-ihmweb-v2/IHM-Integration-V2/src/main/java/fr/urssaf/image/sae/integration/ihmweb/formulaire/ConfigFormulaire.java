/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSource;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSources;

/**
 * Classe de formulaire pour le contrôleur ConfigController 
 */
public class ConfigFormulaire {

   private EcdeSources ecdeSources = new EcdeSources();

   private EcdeSource source = new EcdeSource();

   private String urlWS;
   
   private String cheminTest;
   
   private String cheminTestXml;
   
   private String cheminTestAttendu;
   
   
   
   
   public String getCheminTestAttendu() {
      return cheminTestAttendu;
   }

   public void setCheminTestAttendu(String cheminTestAttendu) {
      this.cheminTestAttendu = cheminTestAttendu;
   }

   public String getCheminTestXml() {
      return cheminTestXml;
   }

   public void setCheminTestXml(String cheminTestXml) {
      this.cheminTestXml = cheminTestXml;
   }

   public String getCheminTest() {
      return cheminTest;
   }

   public void setCheminTest(String cheminTest) {
      this.cheminTest = cheminTest;
   }

   /**
    * @return the ecdeSources
    */
   public final EcdeSources getEcdeSources() {
      return ecdeSources;
   }

   /**
    * @param ecdeSources
    *           the ecdeSources to set
    */
   public final void setEcdeSources(EcdeSources ecdeSources) {
      this.ecdeSources = ecdeSources;
   }

   /**
    * @return the source
    */
   public final EcdeSource getSource() {
      return source;
   }

   /**
    * @param source
    *           the source to set
    */
   public final void setSource(EcdeSource source) {
      this.source = source;
   }

   /**
    * @return the urlWS
    */
   public final String getUrlWS() {
      return urlWS;
   }

   /**
    * @param urlWS the urlWS to set
    */
   public final void setUrlWS(String urlWS) {
      this.urlWS = urlWS;
   }

}
