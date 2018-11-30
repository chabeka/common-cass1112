package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;

/**
 * Formatage d'un objet de type MetadonneeRangeValeur pour le passage d'une
 * classe de formulaire à un contrôleur.<br>
 * <br>
 * On s'attend à récupérer du formulaire une chaîne du type :<br>
 * <br>
 * CodeMetadonnee=ValeurMinMetadonnee&ValeurMaxMetadonnee<br>
 * <br>
 * Et il faut faire le transtypage en un objet MetadonneeRangeValeur
 */
public class RangeMetadonneeEditor extends PropertyEditorSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      MetadonneeRangeValeur meta = new MetadonneeRangeValeur();

      if (StringUtils.isNotBlank(text)) {
         meta = getMeta(text);
      }

      setValue(meta);

   }

   private MetadonneeRangeValeur getMeta(String cleValeur) {

      // NB : ne pas trimmer, car on peut vouloir laisser des espaces
      // String cleValeurOk = StringUtils.trim(cleValeur);
      String cleValeurOk = cleValeur;
      String[] cleValeurSplit = null;

      if (cleValeurOk.contains("JetonDePreuve")) {
         cleValeurSplit = cleValeurOk.split("=", 2);
      } else {
         cleValeurSplit = StringUtils.split(cleValeurOk, "=");
      }

      if (cleValeurSplit.length > 2) {
         throw new IntegrationRuntimeException(
               "La syntaxe de métadonnée suivante est incorrecte : "
                     + cleValeur);
      }
      String code = cleValeurSplit[0];
      // String valeur = cleValeurSplit[1];
      String valeurMin;
      String valeurMax;
      if (cleValeurSplit.length > 1) {
         String[] valeursMinMax = null;
         valeursMinMax = StringUtils.split(cleValeurSplit[1], "&");
         if (valeursMinMax.length > 1) {
            valeurMin = valeursMinMax[0];
            valeurMax = valeursMinMax[1];
         } else {
            valeurMin = valeursMinMax[0];
            valeurMax = StringUtils.EMPTY;
         }
      } else {
         valeurMin = StringUtils.EMPTY;
         valeurMax = StringUtils.EMPTY;
      }

      return new MetadonneeRangeValeur(code, valeurMin, valeurMax);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {

      MetadonneeRangeValeur meta = (MetadonneeRangeValeur) getValue();

      StringBuilder result = new StringBuilder();

      if (meta != null) {

         result.append(meta.getCode() + "=" + meta.getValeurMin() + "&"
               + meta.getValeurMax());
      }

      return result.toString();

   }

}
