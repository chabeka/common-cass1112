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
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.RegTechniqueService;
import fr.urssaf.image.sae.trace.service.RegTechniqueServiceCql;
import fr.urssaf.image.sae.trace.service.RegTechniqueServiceThrift;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;

/**
 * Classe d'implémentation du support {@link RegTechniqueService}. Cette classe
 * est un singleton et peut être accessible par le mécanisme d'injection IOC
 * avec l'annotation @Autowired
 */
@Service
public class RegTechniqueServiceImpl implements RegTechniqueService {

  private final JobClockSupport clockSupport;

  private final LoggerSupport loggerSupport;

  private static final Logger LOGGER = LoggerFactory
                                                    .getLogger(RegTechniqueServiceImpl.class);

  private static final String cfName = "traceregtechnique";

  private final RegTechniqueServiceCql regTechniqueServiceCql;

  private final RegTechniqueServiceThrift regTechniqueServiceThrift;

  private static final String FIN_LOG = "{} - Fin";

  private static final String DEBUT_LOG = "{} - Début";

  /**
   * Constructeur
   *
   * @param support
   *          Support de la classe DAO TraceRegTechniqueDao
   * @param clockSupport
   *          JobClockSupport Cassandra
   * @param loggerSupport
   *          Support pour l'écriture des traces applicatives
   */
  @Autowired
  public RegTechniqueServiceImpl(final RegTechniqueServiceCql regTechniqueServiceCql, final RegTechniqueServiceThrift regTechniqueServiceThrift,
                                 final JobClockSupport clockSupport, final LoggerSupport loggerSupport) {
    super();
    this.regTechniqueServiceCql = regTechniqueServiceCql;
    this.regTechniqueServiceThrift = regTechniqueServiceThrift;
    this.clockSupport = clockSupport;
    this.loggerSupport = loggerSupport;
  }

  /**
   * {@inheritDoc}
   */
  public JobClockSupport getClockSupport() {
    return clockSupport;
  }

  /**
   * {@inheritDoc}
   */
  public Logger getLogger() {
    return LOGGER;
  }

  /**
   * {@inheritDoc}
   */
  public LoggerSupport getLoggerSupport() {
    return loggerSupport;
  }

  @Override
  public TraceRegTechnique lecture(final UUID identifiant) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      // ON MAP
      final TraceRegTechniqueCql tracecql = this.regTechniqueServiceCql.lecture(identifiant);
      return UtilsTraceMapper.createTraceRegTechniqueFromCqlToThrift(tracecql);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.regTechniqueServiceThrift.lecture(identifiant);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  @Override
  public List<TraceRegTechniqueIndex> lecture(final Date dateDebut, final Date dateFin, final int limite, final boolean reversed) {
    final String prefix = "lecture()";
    getLogger().debug(DEBUT_LOG, prefix);

    final List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);
    getLogger().debug("{} - Liste des dates à regarder : {}", prefix, dates);

    List<TraceRegTechniqueIndex> value = null;
    List<TraceRegTechniqueIndex> list;
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
      nbTracesPurgees = this.regTechniqueServiceCql.getSupport().delete(dateIndex,
                                                                        getClockSupport().currentCLock());
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      nbTracesPurgees = this.regTechniqueServiceThrift.getSupport().delete(dateIndex,
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

  @Override
  public boolean hasRecords(final Date date) {
    final String trcPrefix = "hasRecords()";
    getLogger().debug(DEBUT_LOG, trcPrefix);

    final Date beginDate = DateUtils.truncate(date, Calendar.DATE);
    Date endDate = DateUtils.addDays(beginDate, 1);
    endDate = DateUtils.addMilliseconds(endDate, -1);

    final List<TraceRegTechniqueIndex> list = lecture(beginDate, endDate, 1, false);

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

  private List<TraceRegTechniqueIndex> findNormalOrder(final List<Date> dates, final int limite) {
    int index = 0;
    int countLeft = limite;
    List<TraceRegTechniqueIndex> result = new ArrayList<>();
    final List<TraceRegTechniqueIndex> values = new ArrayList<TraceRegTechniqueIndex>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                                          .get(dates.size() - 1));

      final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
      if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
        final List<TraceRegTechniqueIndexCql> resultCql = this.regTechniqueServiceCql.getSupport().findByDate(currentDate, limite);
        if (resultCql != null) {
          for (final TraceRegTechniqueIndexCql traceJournalEvtIndexCql : resultCql) {
            final TraceRegTechniqueIndex indexThrift = UtilsTraceMapper.createTraceRegTechniqueIndexFromCqlToThrift(traceJournalEvtIndexCql);
            result.add(indexThrift);
          }
        }
      } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
        result = this.regTechniqueServiceThrift.getSupport().findByDates(startDate,
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
        result.clear();
      }
      index++;
    } while (index < dates.size() && countLeft > 0
        && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

    return values;
  }

  private List<TraceRegTechniqueIndex> findReversedOrder(final List<Date> dates, final int limite) {

    int index = dates.size() - 1;
    int countLeft = limite;
    List<TraceRegTechniqueIndex> result = new ArrayList<>();
    final List<TraceRegTechniqueIndex> values = new ArrayList<TraceRegTechniqueIndex>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                                          .get(dates.size() - 1));

      final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
      if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
        final List<TraceRegTechniqueIndexCql> resultCql = this.regTechniqueServiceCql.getSupport().findByDate(currentDate, limite);
        if (resultCql != null) {
          for (final TraceRegTechniqueIndexCql traceJournalEvtIndexCql : resultCql) {
            final TraceRegTechniqueIndex indexThrift = UtilsTraceMapper.createTraceRegTechniqueIndexFromCqlToThrift(traceJournalEvtIndexCql);
            result.add(indexThrift);
          }
        }
      } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
        result = this.regTechniqueServiceThrift.getSupport().findByDates(startDate,
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
        result.clear();
      }
      index--;
    } while (index >= 0 && countLeft > 0
        && !DateUtils.isSameDay(dates.get(0), dates.get(dates.size() - 1)));

    return values;
  }
}
