package fr.urssaf.image.sae.lotinstallmaj.modele;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.util.CollectionUtils;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Cette classe contient les catégories suite à la désérialisation du fichier
 * xml [SaeBase.xml]
 * <ul>
 * <li>categories : Les catégories</li>
 * </ul>
 * 
 * @author rhofir.
 * 
 */

@XStreamAlias("categories")
public class SaeCategories {
   @XStreamImplicit(itemFieldName = "category")
   private List<SaeCategory> categories;

   /**
    * @param categories
    *           : Les catégories
    */
   public final void setCategories(final List<SaeCategory> categories) {
      this.categories = categories;
   }

   /**
    * @return la liste des catégories.
    */
   public final List<SaeCategory> getCategories() {
      return categories;
   }

   /**
    * {@inheritDoc}
    */
   public final String toString() {
      final ToStringBuilder toStringBuilder = new ToStringBuilder(this,
            ToStringStyle.SIMPLE_STYLE);

      if (!CollectionUtils.isEmpty(categories)) {

         for (SaeCategory saeCategory : categories) {
            toStringBuilder.append("SaeCategory", saeCategory.toString());
         }
      }
      return toStringBuilder.toString();
   }

}
