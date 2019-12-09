/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * TraitementService
 */
@Aspect
@Component
public class TraitementServiceValidation {

  private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

  private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.executable.service.TraitementService.";

  private static final String PURGE_METHOD = "execution(void " + CLASS_NAME
      + "purger(*))" + " && args(typePurge)";

  private static final String JOURNALISATION_METHOD = "execution(void "
      + CLASS_NAME + "journaliser(*))" + " && args(journalisationType)";

  /**
   * Réalise la validation de la méthode lecture de l'interface RegService
   * 
   * @param typePurge
   *          type de purge
   */
  @Before(PURGE_METHOD)
  public final void testPurge(final PurgeType typePurge) {

    if (typePurge == null) {
      throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
                                                             "{0}",
                                                             "type de purge"));
    }
  }

  /**
   * Réalise la validation de la méthode lecture de l'interface RegService
   * 
   * @param journalisationType
   *          type de journalisation
   */
  @Before(JOURNALISATION_METHOD)
  public final void testJournalisaton(final JournalisationType journalisationType) {

    if (journalisationType == null) {
      throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
                                                             "{0}",
                                                             "type de journalisation"));
    }
  }

}
