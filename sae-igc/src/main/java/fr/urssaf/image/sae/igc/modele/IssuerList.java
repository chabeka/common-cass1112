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
@XStreamAlias("issuers")
public class IssuerList {

   @XStreamImplicit(itemFieldName="issuer")
   private List<String> issuers;

   /**
    * @return the issuers
    */
   public final List<String> getIssuers() {
      return issuers;
   }

   /**
    * @param issuers the issuers to set
    */
   public final void setIssuers(List<String> issuers) {
      this.issuers = issuers;
   }
   
   
   
}
