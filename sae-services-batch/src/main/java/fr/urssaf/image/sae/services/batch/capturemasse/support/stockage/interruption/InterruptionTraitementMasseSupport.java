/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption;

import org.joda.time.DateTime;

import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;

/**
 * Composant pour l'interruption programmée des traitements de capture de masse
 */
public interface InterruptionTraitementMasseSupport {

  /**
   * L'interruption du service DFCE est parfois nécessaire pour des questions
   * de maintenance, et programmée à des moments précis pour être redémarré
   * ensuite. Cette méthode permet de mettre en pause le
   * {@link java.lang.Thread} courant pendant cette période puis de se
   * reconnecter au service DFCE
   * 
   * @param currentDate
   *          date courante
   * @param conf
   *          configuration de l'interruption
   * @throws InterruptionTraitementException
   *           une exception a été levée lors de la tentative de reconnexion à
   *           DFCE
   */
  void interruption(DateTime currentDate,
                    InterruptionTraitementConfig conf)
      throws InterruptionTraitementException;

  /**
   * Vérifie si une date courante est dans une période d'interruption
   * 
   * @param currentDate
   *          date courante
   * @param conf
   *          configuration de l'interruption
   * @return <code>true</code> si <code>currentDate</code> est situé dans la
   *         période d'interruption, <code>false</code> sinon
   */
  boolean hasInterrupted(DateTime currentDate,
                         InterruptionTraitementConfig conf);

  /**
   * True si le traitement est interrompu pour cause de redemarrage des serveurs d'application. False sinon.
   */
  public boolean isInterrupted();

  /**
   * 
   * @return le message d'erreur lié à l'exception lorsqu'on arrive pas à se connecter au DFCE
   */
  public String getConnectionResultExceptionMessage();
  
  /**
   * Methode de vérification sur l'état du redemmarrage du serveur d'application.
   * 
   * @param config
   *          Configuration d'interruption du serveur d'application
   * @throws InterruptionTraitementException
   * @{@link InterruptionTraitementException}
   */
  void verifyInterruptedProcess(InterruptionTraitementConfig config) throws InterruptionTraitementException;
}
