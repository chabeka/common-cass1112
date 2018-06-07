/**
 *
 */
package fr.urssaf.image.sae.trace.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.service.JournalEvtService;
import fr.urssaf.image.sae.trace.service.implcql.JournalEvtCqlServiceImpl;
import fr.urssaf.image.sae.trace.service.support.LoggerSupport;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;

/**
 * @author AC75007648
 */
@Service
public class JournalEvtServiceImpl implements JournalEvtService {

  private final String cfName = "journalevt";

  private final JournalEvtServiceThriftImpl journalEvtServiceThriftImpl;

  private final JournalEvtCqlServiceImpl journalEvtCqlServiceImpl;

  @Autowired
  public JournalEvtServiceImpl(final JournalEvtServiceThriftImpl journalEvtServiceThriftImpl, final JournalEvtCqlServiceImpl journalEvtCqlServiceImpl) {
    super();
    this.journalEvtServiceThriftImpl = journalEvtServiceThriftImpl;
    this.journalEvtCqlServiceImpl = journalEvtCqlServiceImpl;
  }

  @Override
  public String export(final Date date, final String repertoire, final String idJournalPrecedent, final String hashJournalPrecedent) {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlServiceImpl.export(date, repertoire, idJournalPrecedent, hashJournalPrecedent);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThriftImpl.export(date, repertoire, idJournalPrecedent, hashJournalPrecedent);
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  public LoggerSupport getLoggerSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlServiceImpl.getLoggerSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThriftImpl.getLoggerSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  public JobClockSupport getClockSupport() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlServiceImpl.getClockSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThriftImpl.getClockSupport();
    } else if (modeApi == ModeGestionAPI.MODE_API.DUAL_MODE) {
      // Pour exemple
      // Dans le cas d'une lecture aucun intérêt de lire dans les 2 modes et donc dans 2 CF différentes
    }
    return null;
  }

  public Logger getLogger() {
    final String modeApi = ModeGestionAPI.getModeApiCf(cfName);
    if (modeApi == ModeGestionAPI.MODE_API.DATASTAX) {
      return this.journalEvtCqlServiceImpl.getLogger();
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThriftImpl.getLogger();
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
      // ON MAP
      final TraceJournalEvtCql tracecql = this.journalEvtCqlServiceImpl.lecture(identifiant);
      return UtilsTraceMapper.createTraceThriftFromCqlTrace(tracecql);
    } else if (modeApi == ModeGestionAPI.MODE_API.HECTOR) {
      return this.journalEvtServiceThriftImpl.lecture(identifiant);
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
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void purge(final Date date) {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasRecords(final Date date) {
    // TODO Auto-generated method stub
    return false;
  }

}
