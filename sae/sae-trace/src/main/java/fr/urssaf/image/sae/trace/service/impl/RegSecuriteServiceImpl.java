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
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.model.PurgeType;
import fr.urssaf.image.sae.trace.service.RegSecuriteService;
import fr.urssaf.image.sae.trace.service.RegSecuriteServiceCql;
import fr.urssaf.image.sae.trace.service.RegSecuriteServiceThrift;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;

/**
 * @author AC75007648
 */
@Service
public class RegSecuriteServiceImpl implements RegSecuriteService {

  private final String cfName = "traceregsecurite";

  private final RegSecuriteServiceThrift regSecuriteThriftService;

  private final RegSecuriteServiceCql regSecuriteCqlService;

  private static final String FIN_LOG = "{} - Fin";

  private static final String DEBUT_LOG = "{} - Début";

  @Autowired
  public RegSecuriteServiceImpl(final RegSecuriteServiceThrift regSecuriteThriftServiceImpl, final RegSecuriteServiceCql regSecuriteCqlServiceImpl) {
    super();
    regSecuriteCqlService = regSecuriteCqlServiceImpl;
    regSecuriteThriftService = regSecuriteThriftServiceImpl;
  }

  public LoggerSupport getLoggerSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return regSecuriteCqlService.getLoggerSupport();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return regSecuriteThriftService.getLoggerSupport();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return regSecuriteThriftService.getLoggerSupport();
    }
    return null;
  }

  public JobClockSupport getClockSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
    	// nothing => le clock est gerer automatiquement par datastax
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return regSecuriteThriftService.getClockSupport();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return regSecuriteThriftService.getClockSupport();
    }
    return null;
  }

  public Logger getLogger() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      return regSecuriteCqlService.getLogger();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return regSecuriteThriftService.getLogger();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return regSecuriteThriftService.getLogger();
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.trace.service.RegSecuriteService#lecture(java.util.UUID)
   */
  @Override
  public TraceRegSecurite lecture(final UUID identifiant) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      // ON MAP
      final TraceRegSecuriteCql tracecql = regSecuriteCqlService.lecture(identifiant);
      return UtilsTraceMapper.createTraceRegSecuriteFromCqlToThrift(tracecql);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      return regSecuriteThriftService.lecture(identifiant);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      return regSecuriteThriftService.lecture(identifiant);
    }
    return null;
  }

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.trace.service.RegSecuriteService#lecture(java.util.Date, java.util.Date, int, boolean)
   */
  @Override
  public List<TraceRegSecuriteIndex> lecture(final Date dateDebut, final Date dateFin, final int limite, final boolean reversed) {
    final String prefix = "lecture()";
    getLogger().debug(DEBUT_LOG, prefix);

    final List<Date> dates = DateRegUtils.getListFromDates(dateDebut, dateFin);
    getLogger().debug("{} - Liste des dates à regarder : {}", prefix, dates);

    List<TraceRegSecuriteIndex> value = null;
    List<TraceRegSecuriteIndex> list;
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

  /*
   * (non-Javadoc)
   * @see fr.urssaf.image.sae.trace.service.RegSecuriteService#purge(java.util.Date)
   */
  @Override
  public void purge(final Date date, final int nbMaxLigneEvtToDelete) {
    final String prefix = "purge()";
    getLogger().debug(DEBUT_LOG, prefix);

    final Date dateIndex = DateUtils.truncate(date, Calendar.DATE);

    getLoggerSupport().logPurgeJourneeDebut(getLogger(),
                                            prefix,
                                            PurgeType.PURGE_EVT,
                                            DateRegUtils.getJournee(date));

    long nbTracesPurgees = 0;

    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      nbTracesPurgees = regSecuriteCqlService.getSupport().delete(dateIndex);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      nbTracesPurgees = regSecuriteThriftService.getSupport().delete(dateIndex,
                                                                     getClockSupport().currentCLock(), nbMaxLigneEvtToDelete);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
      nbTracesPurgees = regSecuriteThriftService.getSupport()
          .delete(dateIndex,
                  getClockSupport().currentCLock(), nbMaxLigneEvtToDelete);
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

    final List<TraceRegSecuriteIndex> list = lecture(beginDate, endDate, 1, false);

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

  private List<TraceRegSecuriteIndex> findNormalOrder(final List<Date> dates, final int limite) {
    int index = 0;
    int countLeft = limite;
    List<TraceRegSecuriteIndex> result = new ArrayList<>();
    final List<TraceRegSecuriteIndex> values = new ArrayList<>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                        .get(dates.size() - 1));

      final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
      if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
        final List<TraceRegSecuriteIndexCql> resultCql = regSecuriteCqlService.getSupport().findByDate(currentDate, limite);
        if (resultCql != null) {
          for (final TraceRegSecuriteIndexCql traceRegSecuIndexCql : resultCql) {
            final TraceRegSecuriteIndex indexThrift = UtilsTraceMapper.createTraceRegSecuIndexFromCqlToThrift(traceRegSecuIndexCql);
            result.add(indexThrift);
          }
        }
      } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
        result = regSecuriteThriftService.getSupport().findByDates(startDate,
                                                                   endDate,
                                                                   countLeft,
                                                                   true);
      } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
        result = regSecuriteThriftService.getSupport()
            .findByDates(startDate,
                         endDate,
                         countLeft,
                         true);
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

  private List<TraceRegSecuriteIndex> findReversedOrder(final List<Date> dates, final int limite) {

    int index = dates.size() - 1;
    int countLeft = limite;
    List<TraceRegSecuriteIndex> result = new ArrayList<>();
    final List<TraceRegSecuriteIndex> values = new ArrayList<>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                        .get(dates.size() - 1));

      final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
      if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
        final List<TraceRegSecuriteIndexCql> resultCql = regSecuriteCqlService.getSupport().findByDate(currentDate, limite);
        if (resultCql != null) {
          for (final TraceRegSecuriteIndexCql traceJournalEvtIndexCql : resultCql) {
            final TraceRegSecuriteIndex indexThrift = UtilsTraceMapper.createTraceRegSecuIndexFromCqlToThrift(traceJournalEvtIndexCql);
            result.add(indexThrift);
          }
        }
      } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
        result = regSecuriteThriftService.getSupport().findByDates(startDate,
                                                                   endDate,
                                                                   countLeft,
                                                                   true);
      } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE)) {
        result = regSecuriteThriftService.getSupport()
                                         .findByDates(startDate,
                                                      endDate,
                                                      countLeft,
                                                      true);
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
