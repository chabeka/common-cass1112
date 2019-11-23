/**
 * 
 */
package fr.urssaf.image.sae.igc.modele;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * 
 * 
 */
@XStreamAlias("IgcConfigs")
public class IgcConfigs {

   @XStreamImplicit(itemFieldName = "IgcConfig")
   private List<IgcConfig> igcListConfigs;

   
   /**
    * @return the igcConfigs
    */
   public final List<IgcConfig> getIgcConfigs() {
      return igcListConfigs;
   }

   /**
    * @param igcConfigs the igcConfigs to set
    */
   public final void setIgcConfigs(List<IgcConfig> igcConfigs) {
      this.igcListConfigs = igcConfigs;
   }
   
}
