/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.listener;

import org.springframework.batch.core.annotation.BeforeProcess;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;

/**
 * Méthodes de base pour les listener
 */
public abstract class AbstractDfceListener extends AbstractListener {

  @BeforeProcess
  public void beforeProcess() throws InterruptionTraitementException {
    getInterruptionTraitementSupport().verifyInterruptedProcess(getInterruptionConfig());
  }

  /**
   * Getter
   * 
   * @return la classe support de vérification de l'interruption du service DFCe pour redemarrage.
   */
  protected abstract InterruptionTraitementMasseSupport getInterruptionTraitementSupport();

  /**
   * Getter
   * 
   * @return La classe de configuration de l'interruption du service DFCe pour redemarrage.
   */
  protected abstract InterruptionTraitementConfig getInterruptionConfig();

}
