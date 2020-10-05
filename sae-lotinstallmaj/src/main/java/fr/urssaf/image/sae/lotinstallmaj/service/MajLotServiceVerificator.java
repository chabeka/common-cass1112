/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.lotinstallmaj.service;

import java.util.ArrayList;
import java.util.List;

import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.ReferentielFormatService;
import fr.urssaf.image.sae.lotinstallmaj.component.DFCEConnexionComponent;
import fr.urssaf.image.sae.metadata.referential.model.SaeIndexComposite;
import fr.urssaf.image.sae.metadata.referential.support.SaeIndexCompositeSupport;

public interface MajLotServiceVerificator {

  /**
   * Vérifie que les formats de fichiers sont listés sont bien présents dans le référentiel des formats
   * 
   * @return
   */
  default boolean verifyFormatFichier(final List<String> formatsToCheck) {

    final List<FormatFichier> listFormat = getReferentielFormatService().getAllFormat();
    final List<String> extensions = new ArrayList<>();
    for (final FormatFichier format : listFormat) {
      extensions.add(format.getExtension());
    }

    return extensions.containsAll(formatsToCheck);
  }

  /**
   * Vérifie que les index composites sont bien à jour dans dfce
   * 
   * @return
   */
  default boolean verifyIndexesComposites(final String indexCodeCourt) {

    boolean isOK = false;

    final SaeIndexCompositeSupport serviceSupport = new SaeIndexCompositeSupport(openConnection().getDFCEServices());
    final List<SaeIndexComposite> listIndex = serviceSupport.getListeCompositeIndex();

    for (final SaeIndexComposite index : listIndex) {
      if (index.getName().equals(indexCodeCourt)) {
        isOK = true;
      }
    }
    return isOK;
  }

  /**
   * Vérification qu'une mise à jour a bien été installée
   * 
   * @param version
   *          numéro de la version à vérifier
   * @return
   */
  boolean verify(final int version);

  /**
   * @return
   */
  ReferentielFormatService getReferentielFormatService();

  /**
   * @return
   */
  DFCEConnexionComponent openConnection();

}
