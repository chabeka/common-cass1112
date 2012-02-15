package fr.urssaf.image.sae.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Classe permettant de désérialiser une catégorie.
 * <ul>
 * <li>name : Le libellé de la catégorie</li>
 * <li>value : La valeur de la ctégorie
 * <li>
 * </ul>
 * 
 * @author akenore, rhofir.
 * 
 */
@XStreamAlias("category")
public class SaeCategory {

   private String name;
   private String value;

   /**
    * @param value
    *           : La valeur de la catégorie.
    */
   public final void setValue(final String value) {
      this.value = value;
   }

   /**
    * @return La valeur de la catégorie
    */
   public final String getValue() {
      return value;
   }

   /**
    * @param name
    *           : Le nom de la catégorie
    */
   public final void setName(final String name) {
      this.name = name;
   }

   /**
    * @return Le nom de la catégorie
    */
   public final String getName() {
      return name;
   }

   /** {@inheritDoc} */
   public final String toString() {
      final ToStringBuilder toStringBuilder = new ToStringBuilder(this);
      toStringBuilder.append("name", name);
      toStringBuilder.append("value", value);
      return toStringBuilder.toString();
   }

}
