/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.modele;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser les données de la base documentaire.<BR />
 * elle contient les s :
 * <ul>
 * <li>
 * baseId : Le nom de la base</li>
 * <li>baseDescription : Le descriptif de la base</li>
 * <li>categories :Les catégories</li>
 * </ul>
 */
@XStreamAlias("base")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class SaeBase {
   @XStreamAlias("categories")
   private SaeCategories saeCategories;

   /**
    * @return Les categories
    */
   public final SaeCategories getSaeCategories() {
      return saeCategories;
   }

   /**
    * @param seaCategories
    *           : Les categories
    */
   public final void setSaeCategories(final SaeCategories seaCategories) {
      this.saeCategories = seaCategories;
   }

   /** {@inheritDoc} */
   @Override
   public final String toString() {
      final ToStringBuilder toStringBuilder = new ToStringBuilder(this,
            ToStringStyle.SIMPLE_STYLE);

      if (saeCategories != null) {
         toStringBuilder.append("saeCategories", saeCategories.toString());
      }
      return toStringBuilder.toString();
   }

}
