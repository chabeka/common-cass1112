package fr.urssaf.image.sae.integration.ihmweb.propertyeditor;

import java.beans.PropertyEditorSupport;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.modele.IdentifiantPage;
import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeRangeValeur;

/**
 * Formatage d'un objet de type IdentifiantPage pour le passage d'une classe de
 * formulaire à un contrôleur.<br>
 * <br>
 * On s'attend à récupérer du formulaire une chaîne du type :<br>
 * <br>
 * UUID&valeurMetadonnee<br>
 * <br>
 * Et il faut faire le transtypage en un objet IdentifiantPage
 */
public class IdentifiantPageEditor extends PropertyEditorSupport {

   /**
    * {@inheritDoc}
    */
   @Override
   public final void setAsText(String text) {

      IdentifiantPage idPage = new IdentifiantPage();

      if (StringUtils.isNotBlank(text)) {
         idPage = getIdPage(text);
      }

      setValue(idPage);

   }

   private IdentifiantPage getIdPage(String text) {

      String[] split = StringUtils.split(text, "&");

      if (split.length > 2) {
         throw new IntegrationRuntimeException(
               "La syntaxe de métadonnée suivante est incorrecte : " + text);
      }
      String uuid;
      String valeur;
      if (split.length > 1) {
         uuid = split[0];
         valeur = split[1];
      } else {
         uuid = StringUtils.EMPTY;
         valeur = StringUtils.EMPTY;
      }

      return new IdentifiantPage(valeur, UUID.fromString(uuid));

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getAsText() {

      IdentifiantPage idPage = (IdentifiantPage) getValue();

      StringBuilder result = new StringBuilder();

      if (idPage != null) {
         if (idPage.getIdArchive() != null && idPage.getValeur() != null) {
            result.append(idPage.getIdArchive() + "&" + idPage.getValeur());
         }
      }

      return result.toString();

   }

}
