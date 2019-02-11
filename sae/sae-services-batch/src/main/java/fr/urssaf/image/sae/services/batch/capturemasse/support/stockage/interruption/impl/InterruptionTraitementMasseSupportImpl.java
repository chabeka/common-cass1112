/**
 *
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.impl;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.util.InterruptionTraitementUtils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 *
 *
 */
@Component
public class InterruptionTraitementMasseSupportImpl implements
                                                    InterruptionTraitementMasseSupport {

  private static final Logger LOG = LoggerFactory
                                                 .getLogger(InterruptionTraitementMasseSupportImpl.class);

  private final DFCEServices dfceServices;

  private static final String CATCH = "AvoidCatchingThrowable";

  private final StorageServiceProvider serviceProvider;

  private boolean interrupted;

  /**
   * @param dfceManager
   *          ensemble des services de manipulation DFCE
   * @param serviceProvider
   *          le provider de service
   */
  @Autowired
  public InterruptionTraitementMasseSupportImpl(
                                                final DFCEServices dfceServices,
                                                final @Qualifier("storageServiceProvider") StorageServiceProvider serviceProvider) {

    Assert.notNull(dfceServices, "'dfceServices' is required");

    Assert.notNull(serviceProvider, "'serviceProvider' is required");

    this.dfceServices = dfceServices;
    this.serviceProvider = serviceProvider;

  }

  /**
   * Délai par défaut d'attente entre chaque tentative de reconnexion après une
   * interruption
   */
  protected static final long DEFAULT_DELAY = 120000;

  private long defaultDelay = DEFAULT_DELAY;

  /**
   * @param defaultDelay
   *          temps d'attente en secondes entre chaque tentatives après la
   *          première
   */
  public final void setDelay(final long defaultDelay) {
    this.defaultDelay = defaultDelay;
  }

  private static final String LOG_PREFIX = "Interruption programmée d'un traitement";

  // format de date avec heure
  private static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

  /**
   * {@inheritDoc} <br>
   * Après la première tentative le delai d'attente entre chaque tentative est
   * fixé par {@link #setDelay(long)} en secondes.<br>
   * Par défaut cette valeur est fixé à {@value #DEFAULT_DELAY}
   */
  @Override
  public final void interruption(final DateTime currentDate,
                                 final InterruptionTraitementConfig config)
      throws InterruptionTraitementException {

    final long diffTime = InterruptionTraitementUtils.waitTime(currentDate,
                                                               config);

    if (diffTime > 0) {

      final DateTime endDate = currentDate.plus(diffTime);

      final DateTimeFormatter formatter = DateTimeFormat
                                                        .forPattern(DATE_TIME_PATTERN);

      LOG.debug("{} - Reprise prévue à {}",
                LOG_PREFIX,
                formatter
                         .print(endDate));

      // On ne ferme pas la connexion avec DFCE pour éviter une levée
      // d'exception sur les Threads en cours d'exécution qui effectuent une
      // insertion dans DFCE
      // dfceManager.closeConnection();

      ConnectionResult connectionResult;
      try {
        connectionResult = pause(diffTime,
                                 null,
                                 config.getTentatives(),
                                 config.getTentatives(),
                                 dfceServices);
      }
      catch (final InterruptedException e) {
        // Interruption lors de la mise en pause du traitement
        throw new InterruptionTraitementException(config, e);
      }

      if (connectionResult.exception != null) {

        throw new InterruptionTraitementException(config,
                                                  connectionResult.exception);
      }

      LOG.debug(
                "{} - Réussite de la tentative n°{}/{} de reconnexion à DFCE ",
                new Object[] {LOG_PREFIX, connectionResult.step,
                              config.getTentatives()});

    }
  }

  @SuppressWarnings(CATCH)
  private ConnectionResult pause(final long delay,
                                 final Exception lastException, final int tentatives, final int total,
                                 final DFCEServices dfceServices)
      throws InterruptedException {

    final int step = total - tentatives + 1;

    ConnectionResult connectionResult = new ConnectionResult();
    connectionResult.exception = lastException;
    connectionResult.step = step;

    if (tentatives > 0) {

      final Duration duration = Duration.millis(delay);

      LOG.debug("{} - Interruption de {} secondes",
                LOG_PREFIX,
                duration
                        .getStandardSeconds());

      Thread.sleep(delay);

      try {

        LOG.debug("{} - Tentative n°{}/{} de reconnexion à DFCE",
                  new Object[] {LOG_PREFIX, step, total});

        dfceServices.reconnect();

        // réussite de la connexion à DFCE

        connectionResult.step = step;
        connectionResult.setLastException(null);

      }
      catch (final Exception e) {

        // échec de la connection

        final int newTentatives = tentatives - 1;

        LOG.debug(
                  "{} - Echec de la tentative n°{}/{} de reconnexion à DFCE ",
                  new Object[] {LOG_PREFIX, step, total});

        connectionResult = pause(defaultDelay,
                                 e,
                                 newTentatives,
                                 total,
                                 dfceServices);

      }

    }

    return connectionResult;

  }

  private static final class ConnectionResult {
    private ConnectionResult() {
    }

    private Exception exception;

    private int step;

    private void setLastException(final Exception lastException) {
      exception = lastException;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean hasInterrupted(final DateTime currentDate,
                                      final InterruptionTraitementConfig config) {

    return InterruptionTraitementUtils.waitTime(currentDate, config) > 0;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws InterruptionTraitementException
   */
  @Override
  public void verifyInterruptedProcess(final InterruptionTraitementConfig config) throws InterruptionTraitementException {
    // on vérifie que le traitement ne doit pas s'interrompre
    final DateTime currentDate = new DateTime();

    if (config != null
        && hasInterrupted(currentDate, config)) {

      synchronized (this) {

        // un seul thread est chargé de la reconnexion
        // les autre attendent
        while (Boolean.TRUE.equals(isInterrupted())) {

          try {
            this.wait();
          }
          catch (final InterruptedException e) {
            throw new IllegalStateException(e);
          }

        }

        // isInterrupted == null signifie que c'est le premier passage
        // c'est donc ce thread là qui sera chargé de la reconnexion
        if (!isInterrupted()) {

          setInterrupted(Boolean.TRUE);
        }

      }

      if (Boolean.TRUE.equals(isInterrupted())) {

        try {
          // appel de la méthode de reconnexion
          interruption(currentDate, config);
        }
        finally {

          // de toutes les façons il faut libérer l'ensemble des Threads en
          // attente
          setInterrupted(Boolean.FALSE);
          synchronized (this) {
            notifyAll();
          }
        }

      }

    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInterrupted() {
    return interrupted;
  }

  /**
   * Setter
   * 
   * @param interrupted
   *          the interrupted to set
   */
  private void setInterrupted(final boolean interrupted) {
    this.interrupted = interrupted;
  }

}
