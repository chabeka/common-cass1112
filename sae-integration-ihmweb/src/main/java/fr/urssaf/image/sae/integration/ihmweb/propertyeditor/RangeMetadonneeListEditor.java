package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeurList;
import fr.urssaf.image.sae.integration.ihmweb.utils.PropertyEditorUtils;

/**
 * Formatage d'un objet de type MetadonneeRangeValeurList pour le passage d'une
 * classe de formulaire à un contrôleur.<br>
 * <br>
 * On s'attend à récupérer du formulaire une chaîne du type :<br>
 * <br>
 * CodeMetadonnee1=ValeurMinMetadonnee1&ValeurMaxMetadonnee1<br>
 * CodeMetadonnee2=ValeurMinMetadonnee2&ValeurMaxMetadonnee2<br>
 * <br>
 * Et il faut faire le transtypage en un objet MetadonneeRangeValeurList
 */
public class RangeMetadonneeListEditor extends PropertyEditorSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      MetadonneeRangeValeurList listeMeta = new MetadonneeRangeValeurList();

      if (StringUtils.isNotBlank(text)) {

         String[] pairesCleValeur = PropertyEditorUtils
               .eclateSurRetourCharriot(text);

         if (ArrayUtils.isNotEmpty(pairesCleValeur)) {
            for (String cleValeur : pairesCleValeur) {
               addMeta(listeMeta, cleValeur);
            }
         }

      }

      setValue(listeMeta);

   }

   private void addMeta(MetadonneeRangeValeurList listeMeta, String cleValeur) {

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
         valeursMinMax = StringUtils.split(cleValeurOk, "&");
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

      listeMeta.add(new MetadonneeRangeValeur(code, valeurMin, valeurMax));

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {

      MetadonneeRangeValeurList metadonnees = (MetadonneeRangeValeurList) getValue();

      StringBuilder result = new StringBuilder();

      if (CollectionUtils.isNotEmpty(metadonnees)) {
         for (MetadonneeRangeValeur metadonnee : metadonnees) {
            result.append(metadonnee.getCode() + "=" + metadonnee.getValeurMin() + "&" + metadonnee.getValeurMax());
            result.append("\r");
         }
      }

      return result.toString();

   }

}
