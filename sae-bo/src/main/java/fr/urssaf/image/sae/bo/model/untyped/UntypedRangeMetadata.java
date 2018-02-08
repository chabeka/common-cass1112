package fr.urssaf.image.sae.bo.model.untyped;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;

/**
 * Classe représentant un range de métadonnée. Il s’agit d’une métadonnée avec
 * une borne minimum et une borne maximum.
 * 
 * 
 */
public class UntypedRangeMetadata extends AbstractMetadata {

   /**
    * Valeur minimum de la métadonnée
    */
   private String valeurMin;

   /**
    * Valeur maximum de la métadonnée
    */
   private String valeurMax;

   /**
    * @return the valeurMin
    */
   public String getValeurMin() {
      return valeurMin;
   }

   /**
    * @param valeurMin
    *           the valeurMin to set
    */
   public void setValeurMin(String valeurMin) {
      this.valeurMin = valeurMin;
   }

   /**
    * @return the valeurMax
    */
   public String getValeurMax() {
      return valeurMax;
   }

   /**
    * @param valeurMax
    *           the valeurMax to set
    */
   public void setValeurMax(String valeurMax) {
      this.valeurMax = valeurMax;
   }

   /**
    * Construit un objet de type {@link UntypedMetadata}
    */
   public UntypedRangeMetadata() {
      super();
   }

   /**
    * Construit un objet de type {@link UntypedRangeMetadata}
    * 
    * @param longCode
    *           : Le long court de la métadonnée métier.
    * @param valeurMin
    *           : La valeur minimum de la métadonnée.
    * @param valeurMin
    *           : La valeur maximum de la métadonnée.
    */

   public UntypedRangeMetadata(final String longCode, final String valeurMin,
         final String valeurMax) {
      super(longCode);
      this.valeurMin = valeurMin;
      this.valeurMax = valeurMax;
   }

   /**
    * {@inheritDoc}
    */
   public final String toString() {
      final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
            ToStringStyle.SHORT_PREFIX_STYLE);
      toStrBuilder.append("code long:", getLongCode());
      toStrBuilder.append("valeur min", getValeurMin());
      toStrBuilder.append("valeur max", getValeurMax());
      return toStrBuilder.toString();

   }
}
