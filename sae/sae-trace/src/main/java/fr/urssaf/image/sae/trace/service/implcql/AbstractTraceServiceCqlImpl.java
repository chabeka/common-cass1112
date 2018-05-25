package fr.urssaf.image.sae.trace.service.implcql;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.Trace;
import fr.urssaf.image.sae.trace.dao.model.TraceIndex;
import fr.urssaf.image.sae.trace.dao.supportcql.GenericAbstractTraceCqlSupport;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.RegService;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * Classe mère d'implémentation des support de trace
 *
 * @param <T>
 *          Type de trace contenue dans le registre
 * @param <I>
 *          Index des traces
 */
public abstract class AbstractTraceServiceCqlImpl<T extends Trace, I extends TraceIndex>
                                                 implements RegService<T, I> {

  private static final String FIN_LOG = "{} - Fin";

  private static final String DEBUT_LOG = "{} - Début";

  /**
   * {@inheritDoc}
   */
  @Override
  public final List<I> lecture(final Date dateDebut, final Date dateFin, final int limite,
                               final boolean reversed) {
    final String prefix = "lecture()";
    getLogger().debug(DEBUT_LOG, prefix);

    final List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);
    getLogger().debug("{} - Liste des dates à regarder : {}", prefix, dates);

    List<I> value = null;
    List<I> list;
    if (reversed) {
      list = findReversedOrder(dates, limite);
    } else {
      list = findNormalOrder(dates, limite);
    }

    if (CollectionUtils.isNotEmpty(list)) {
      value = list;
    }

    getLogger().debug(FIN_LOG, prefix);

    return value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final T lecture(final UUID identifiant) {
    final Optional<T> tOp = getSupport().find(identifiant);
    return tOp.get();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void purge(final Date date) {
    final String prefix = "purge()";
    getLogger().debug(DEBUT_LOG, prefix);

    final Date dateIndex = DateUtils.truncate(date, Calendar.DATE);

    getLoggerSupport().logPurgeJourneeDebut(getLogger(),
                                            prefix,
                                            PurgeType.PURGE_EVT,
                                            DateRegUtils.getJournee(date));
    final long nbTracesPurgees = getSupport().delete(dateIndex,
                                                     getClockSupport().currentCLock());
    getLoggerSupport()
                      .logPurgeJourneeFin(getLogger(),
                                          prefix,
                                          PurgeType.PURGE_EVT,
                                          DateRegUtils.getJournee(date),
                                          nbTracesPurgees);

    getLogger().debug(FIN_LOG, prefix);

  }

  private List<I> findNormalOrder(final List<Date> dates, final int limite) {
    int index = 0;
    int countLeft = limite;
    List<I> result;
    final List<I> values = new ArrayList<I>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                                          .get(dates.size() - 1));

      result = getSupport()
                           .findByDates(startDate, endDate, countLeft, false);

      if (CollectionUtils.isNotEmpty(result)) {
        values.addAll(result);
        countLeft = limite - values.size();
      }
      index++;
    } while (index < dates.size() && countLeft > 0
        && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

    return values;
  }

  private List<I> findReversedOrder(final List<Date> dates, final int limite) {

    int index = dates.size() - 1;
    int countLeft = limite;
    List<I> result;
    final List<I> values = new ArrayList<I>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                                          .get(dates.size() - 1));

      result = getSupport().findByDates(startDate, endDate, countLeft, true);

      if (CollectionUtils.isNotEmpty(result)) {
        values.addAll(result);
        countLeft = limite - values.size();
      }
      index--;
    } while (index >= 0 && countLeft > 0
        && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

    return values;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final boolean hasRecords(final Date date) {

    final String trcPrefix = "hasRecords()";
    getLogger().debug(DEBUT_LOG, trcPrefix);

    final Date beginDate = DateUtils.truncate(date, Calendar.DATE);
    Date endDate = DateUtils.addDays(beginDate, 1);
    endDate = DateUtils.addMilliseconds(endDate, -1);

    final List<I> list = lecture(beginDate, endDate, 1, false);

    final boolean hasRecords = CollectionUtils.isNotEmpty(list);

    if (!hasRecords) {
      getLogger().info(
                       "{} - Aucune trace trouvée pour la journée du {}",
                       new Object[] {
                                     trcPrefix,
                                     new SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                                                                                      .format(date)});
    }

    getLogger().debug(FIN_LOG, trcPrefix);
    return hasRecords;
  }

  /**
   * @return le support permettant la réalisation des opérations
   */
  public abstract GenericAbstractTraceCqlSupport<T, I> getSupport();

  /**
   * @return le support de log
   */
  public abstract LoggerSupport getLoggerSupport();

  /**
   * @return le support de timing des opérations
   */
  public abstract JobClockSupport getClockSupport();

  /**
   * @return le logger de la classe concernée
   */
  public abstract Logger getLogger();
}
