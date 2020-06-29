/**
 * 
 */
package fr.urssaf.image.sae.trace.service.validation;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.trace.model.PurgeType;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * RegService
 */
@Aspect
@Component
public class PurgeServiceValidation {

  private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

  private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.service.PurgeService.";

  private static final String PURGE_METHOD = "execution(void " + CLASS_NAME
      + "purgerRegistre(*))" + " && args(typePurge)";

  private static final String PURGE_JOURN_METHOD = "execution(void "
      + CLASS_NAME + "purgerJournal(*))" + " && args(typePurge)";

  /**
   * Réalise la validation de la méthode de purge de l'interface PurgeService
   * 
   * @param typePurge
   *          le type de purge
   */
  @After(PURGE_METHOD)
  public final void testPurge(final PurgeType typePurge) {

    if (typePurge == null) {
      throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
                                                             "{0}",
          "type de purge"));
    }
  }

  /**
   * Réalise la validation de la méthode de purge de l'interface PurgeService
   * 
   * @param typePurge
   *          le type de purge
   */
  @After(PURGE_JOURN_METHOD)
  public final void testPurgeJournal(final PurgeType typePurge) {

    if (typePurge == null) {
      throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
                                                             "{0}",
          "type de purge"));
    }
  }

}
