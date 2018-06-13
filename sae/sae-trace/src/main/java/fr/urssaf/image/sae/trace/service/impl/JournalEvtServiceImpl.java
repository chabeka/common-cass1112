/**
 *
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.JournalEvtServiceCql;
import fr.urssaf.image.sae.trace.service.JournalEvtServiceThrift;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;

/**
 * @author AC75007648
 */
@Service
public class JournalEvtServiceImpl implements JournalEvtService {

  private final String cfName = "tracejournalevt";

  private final JournalEvtServiceThrift journalEvtServiceThrift;

  private final JournalEvtServiceCql journalEvtCqlService;

  private static final String FIN_LOG = "{} - Fin";

  private static final String DEBUT_LOG = "{} - Début";

  @Autowired
  public JournalEvtServiceImpl(final JournalEvtServiceThrift journalEvtServiceThrift, final JournalEvtServiceCql journalEvtCqlService) {
    super();
    this.journalEvtServiceThrift = journalEvtServiceThrift;
    this.journalEvtCqlService = journalEvtCqlService;
  }

  @Override
  public String export(final Date date, final String repertoire, final String idJournalPrecedent, final String hashJournalPrecedent) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlService.export(date, repertoire, idJournalPrecedent, hashJournalPrecedent);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThrift.export(date, repertoire, idJournalPrecedent, hashJournalPrecedent);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  public LoggerSupport getLoggerSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlService.getLoggerSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThrift.getLoggerSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  public JobClockSupport getClockSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlService.getClockSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThrift.getClockSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  public Logger getLogger() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlService.getLogger();
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThrift.getLogger();
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TraceJournalEvt lecture(final UUID identifiant) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      final TraceJournalEvtCql tracecql = this.journalEvtCqlService.lecture(identifiant);
      return UtilsTraceMapper.createTraceThriftFromCql(tracecql);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThrift.lecture(identifiant);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TraceJournalEvtIndex> lecture(final Date dateDebut, final Date dateFin, final int limite, final boolean reversed) {
    final String prefix = "lecture()";
    getLogger().debug(DEBUT_LOG, prefix);

    final List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);
    getLogger().debug("{} - Liste des dates à regarder : {}", prefix, dates);

    List<TraceJournalEvtIndex> value = null;
    List<TraceJournalEvtIndex> list;
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
  public void purge(final Date date) {
    final String prefix = "purge()";
    getLogger().debug(DEBUT_LOG, prefix);

    final Date dateIndex = DateUtils.truncate(date, Calendar.DATE);

    getLoggerSupport().logPurgeJourneeDebut(getLogger(),
                                            prefix,
                                            PurgeType.PURGE_EVT,
                                            DateRegUtils.getJournee(date));

    long nbTracesPurgees = 0;

    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      nbTracesPurgees = this.journalEvtCqlService.getSupport().delete(dateIndex,
                                                                      getClockSupport().currentCLock());
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      nbTracesPurgees = this.journalEvtServiceThrift.getSupport().delete(dateIndex,
                                                                         getClockSupport().currentCLock());
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }

    getLoggerSupport()
                      .logPurgeJourneeFin(getLogger(),
                                          prefix,
                                          PurgeType.PURGE_EVT,
                                          DateRegUtils.getJournee(date),
                                          nbTracesPurgees);

    getLogger().debug(FIN_LOG, prefix);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRecords(final Date date) {
    final String trcPrefix = "hasRecords()";
    getLogger().debug(DEBUT_LOG, trcPrefix);

    final Date beginDate = DateUtils.truncate(date, Calendar.DATE);
    Date endDate = DateUtils.addDays(beginDate, 1);
    endDate = DateUtils.addMilliseconds(endDate, -1);

    final List<TraceJournalEvtIndex> list = lecture(beginDate, endDate, 1, false);

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

  private List<TraceJournalEvtIndex> findNormalOrder(final List<Date> dates, final int limite) {
    int index = 0;
    int countLeft = limite;
    final List<TraceJournalEvtIndex> result = new ArrayList<>();
    final List<TraceJournalEvtIndex> values = new ArrayList<TraceJournalEvtIndex>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                                          .get(dates.size() - 1));

      final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
      if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
        final List<TraceJournalEvtIndexCql> resultCql = this.journalEvtCqlService.getSupport().findByDates(startDate, endDate, countLeft, true);
        for (final TraceJournalEvtIndexCql traceJournalEvtIndexCql : resultCql) {
          final TraceJournalEvtIndex indexThrift = UtilsTraceMapper.createTraceJournalIndexFromCqlToThrift(traceJournalEvtIndexCql);
          result.add(indexThrift);
        }
      } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
        return this.journalEvtServiceThrift.getSupport().findByDates(startDate,
                                                                     endDate,
                                                                     countLeft,
                                                                     true);
      } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
        // Pour exemple
        // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
      }

      if (CollectionUtils.isNotEmpty(result)) {
        values.addAll(result);
        countLeft = limite - values.size();
      }
      index++;
    } while (index < dates.size() && countLeft > 0
        && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

    return values;
  }

  private List<TraceJournalEvtIndex> findReversedOrder(final List<Date> dates, final int limite) {

    int index = dates.size() - 1;
    int countLeft = limite;
    final List<TraceJournalEvtIndex> result = new ArrayList<>();
    final List<TraceJournalEvtIndex> values = new ArrayList<TraceJournalEvtIndex>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                                          .get(dates.size() - 1));

      final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
      if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
        final List<TraceJournalEvtIndexCql> resultCql = this.journalEvtCqlService.getSupport().findByDates(startDate, endDate, countLeft, true);
        for (final TraceJournalEvtIndexCql traceJournalEvtIndexCql : resultCql) {
          final TraceJournalEvtIndex indexThrift = UtilsTraceMapper.createTraceJournalIndexFromCqlToThrift(traceJournalEvtIndexCql);
          result.add(indexThrift);
        }
      } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
        return this.journalEvtServiceThrift.getSupport().findByDates(startDate,
                                                                     endDate,
                                                                     countLeft,
                                                                     true);
      } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
        // Pour exemple
        // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
      }

      if (CollectionUtils.isNotEmpty(result)) {
        values.addAll(result);
        countLeft = limite - values.size();
      }
      index--;
    } while (index >= 0 && countLeft > 0
        && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

    return values;
  }

}
