package fr.urssaf.image.sae.dfce.admin.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Cette classe représente un type de document du référentiel des codes RND. <BR />
 * Elle contient les attributs :
 * <ul>
 * <li>rndCode : Référentiel National de Documents.</li>
 * <li>activityCode : Le Code d'activité.</li>
 * <li>fonctionCode : Le Code fonction.</li>
 * <li>rndDescription : Le libelle RND.</li>
 * <li>storageDuration : La durée de conservation.</li>
 * </ul>
 * 
 * 
 * @author rhofir.
 * 
 */
@XStreamAlias("Rule")
public class Rule {
   @XStreamAlias("CodeRND")
   private String rndCode;
   @XStreamAlias("DureeConservation")
   private int storageDuration;

   /**
    * @return Référentiel National de Documents
    */
   public final String getRndCode() {
      return rndCode;
   }

   /**
    * @param rndCode
    *           : Référentiel National de Documents
    */
   public final void setRndCode(final String rndCode) {
      this.rndCode = rndCode;
   }

   /**
    * @return La durée de conservation.
    */
   public final int getStorageDuration() {
      return storageDuration;
   }

   /**
    * @param storageDuration
    *           : La durée de conservation.
    */
   public final void setStorageDuration(final int storageDuration) {
      this.storageDuration = storageDuration;
   }


}
