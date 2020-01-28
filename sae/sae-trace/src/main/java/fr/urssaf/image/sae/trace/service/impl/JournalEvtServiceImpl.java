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
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
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
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      return journalEvtCqlService.export(date, repertoire, idJournalPrecedent, hashJournalPrecedent);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      return journalEvtServiceThrift.export(date, repertoire, idJournalPrecedent, hashJournalPrecedent);

    }
    return null;
  }

  public LoggerSupport getLoggerSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      return journalEvtCqlService.getLoggerSupport();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      return journalEvtServiceThrift.getLoggerSupport();
    }
    return null;
  }

  public JobClockSupport getClockSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)
    ) {
      // nothing => le clock est gerer automatiquement par datastax
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      return journalEvtServiceThrift.getClockSupport();
    }
    return null;
  }

  public Logger getLogger() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      return journalEvtCqlService.getLogger();
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      return journalEvtServiceThrift.getLogger();
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TraceJournalEvt lecture(final UUID identifiant) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX) 
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      final TraceJournalEvtCql tracecql = journalEvtCqlService.lecture(identifiant);
      return UtilsTraceMapper.createTraceJournalEvtFromCqlToThrift(tracecql);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      return journalEvtServiceThrift.lecture(identifiant);
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
  public void purge(final Date date, final int nbMaxLigneEvtToDelete) {
    final String prefix = "purge()";
    getLogger().debug(DEBUT_LOG, prefix);

    final Date dateIndex = DateUtils.truncate(date, Calendar.DATE);

    getLoggerSupport().logPurgeJourneeDebut(getLogger(),
                                            prefix,
                                            PurgeType.PURGE_EVT,
                                            DateRegUtils.getJournee(date));

    final long nbTracesPurgees = deleteEvt(nbMaxLigneEvtToDelete, dateIndex);

    getLoggerSupport()
    .logPurgeJourneeFin(getLogger(),
                        prefix,
                        PurgeType.PURGE_EVT,
                        DateRegUtils.getJournee(date),
                        nbTracesPurgees);

    getLogger().debug(FIN_LOG, prefix);
  }

  /**
   * Suppression des évènements suivant le mode API
   * 
   * @param nbMaxLigneEvtToDelete
   * @param dateIndex
   * @param nbTracesPurgees
   * @return
   */
  private long deleteEvt(final int nbMaxLigneEvtToDelete, final Date dateIndex) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    long nbTracesPurgees = 0;
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)) {
      nbTracesPurgees = journalEvtCqlService.getSupport().delete(dateIndex);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)) {
      nbTracesPurgees = journalEvtServiceThrift.getSupport().delete(dateIndex,
                                                                    getClockSupport().currentCLock(), nbMaxLigneEvtToDelete);
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      nbTracesPurgees = journalEvtServiceThrift.getSupport()
          .delete(dateIndex,
                  getClockSupport().currentCLock(),
                  nbMaxLigneEvtToDelete);
      nbTracesPurgees = nbTracesPurgees + journalEvtCqlService.getSupport().delete(dateIndex);
    }

    return nbTracesPurgees;
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
                                     .format(date) });
    }

    getLogger().debug(FIN_LOG, trcPrefix);
    return hasRecords;
  }

  private List<TraceJournalEvtIndex> findNormalOrder(final List<Date> dates, final int limite) {
    int index = 0;
    int countLeft = limite;
    List<TraceJournalEvtIndex> result = new ArrayList<>();
    final List<TraceJournalEvtIndex> values = new ArrayList<>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                        .get(dates.size() - 1));

      result = findEvtByDate(limite, countLeft, result, currentDate, startDate, endDate);

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

  /**
   * Recherche évènements par date suivant mode API
   * 
   * @param limite
   * @param countLeft
   * @param result
   * @param currentDate
   * @param startDate
   * @param endDate
   * @return
   */
  private List<TraceJournalEvtIndex> findEvtByDate(final int limite, final int countLeft, List<TraceJournalEvtIndex> result, final Date currentDate, final Date startDate,
                                                   final Date endDate) {

    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi.equals(ModeGestionAPI.MODE_API.DATASTAX)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_CQL)) {
      final List<TraceJournalEvtIndexCql> resultCql = journalEvtCqlService.getSupport().findByDate(currentDate, limite);
      if (resultCql != null) {
        for (final TraceJournalEvtIndexCql traceJournalEvtIndexCql : resultCql) {
          final TraceJournalEvtIndex indexThrift = UtilsTraceMapper.createTraceJournalIndexFromCqlToThrift(traceJournalEvtIndexCql);
          result.add(indexThrift);
        }
      }
    } else if (modeApi.equals(ModeGestionAPI.MODE_API.HECTOR)
        || modeApi.equals(ModeGestionAPI.MODE_API.DUAL_MODE_READ_THRIFT)) {
      result = journalEvtServiceThrift.getSupport().findByDates(startDate,
                                                                endDate,
                                                                countLeft,
                                                                true);
    }
    return result;
  }

  private List<TraceJournalEvtIndex> findReversedOrder(final List<Date> dates, final int limite) {

    int index = dates.size() - 1;
    int countLeft = limite;
    List<TraceJournalEvtIndex> result = new ArrayList<>();
    final List<TraceJournalEvtIndex> values = new ArrayList<>();
    Date currentDate, startDate, endDate;

    do {
      currentDate = dates.get(index);
      startDate = DateRegUtils.getStartDate(currentDate, dates.get(0));
      endDate = DateRegUtils.getEndDate(currentDate, dates
                                        .get(dates.size() - 1));

      result = findEvtByDate(limite, countLeft, result, currentDate, startDate, endDate);

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
