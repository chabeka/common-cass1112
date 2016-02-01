/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.modele;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 
 * Cette classe contient le modèle de base SAE suite à la désérialisation du
 * fichier xml [SaeBase.xml].<BR />
 * Elle contient :
 * <ul>
 * <li>base : La base SAE</li>
 * <li>IndexComposites : les index composites</li>
 * <li></li>
 * </ul>
 * 
 */
@XStreamAlias("docuBase")
public class DataBaseModel {
   @XStreamAlias("base")
   private SaeBase base;

   /**
    * @return the base
    */
   public final SaeBase getBase() {
      return base;
   }

   /**
    * @param base
    *           : une instance de base
    */
   public final void setBase(final SaeBase base) {
      this.base = base;
   }

   /**
    * 
    * @return une instance de base
    */
   public final SaeBase getDataBase() {

      return base;
   }

   @Override
   public final String toString() {
      final ToStringBuilder toStringBuilder = new ToStringBuilder(this,
            ToStringStyle.SIMPLE_STYLE);
      if (base != null) {
         toStringBuilder.append("base", base.toString());
      }
      return toStringBuilder.toString();
   }
}
