/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.dao.supportcql;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.commons.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.dao.support.TraceRegSecuriteSupport;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteIndexCqlDao;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

/**
 * TODO (AC75095028) Description du type
 */
@Component
public class TraceRegSecuriteCqlSupport extends GenericAbstractTraceCqlSupport<TraceRegSecuriteCql, TraceRegSecuriteIndexCql> {

  private static final String REG_SECURITE_NAME = "registre de sécurité";

  private static final String DATE_FORMAT = "yyyyMMdd";

  private final ITraceRegSecuriteCqlDao dao;

  private final ITraceRegSecuriteIndexCqlDao indexDao;

  private final TimeUUIDEtTimestampSupport timeUUIDSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(TraceRegSecuriteSupport.class);

  @Autowired
  public TraceRegSecuriteCqlSupport(final ITraceRegSecuriteCqlDao dao,
                                    final ITraceRegSecuriteIndexCqlDao indexDao,
                                    final TimeUUIDEtTimestampSupport timeUUIDSupport) {
    super();
    this.dao = dao;
    this.indexDao = indexDao;
    this.timeUUIDSupport = timeUUIDSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  Iterator<TraceRegSecuriteIndexCql> getIterator(final Date date) {
    Date dateJ = null;
    final DateFormat dateFormat = new SimpleDateFormat(getDateFormat());
    final String journee = DateRegUtils.getJournee(date);
    try {
      dateJ = dateFormat.parse(journee);
    }
    catch (final ParseException e) {
      LOGGER.error("Le parsin de la date fournie est impossible");
      e.printStackTrace();
    }
    return indexDao.IterableFindById(dateJ);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  IGenericDAO<TraceRegSecuriteCql, UUID> getDao() {
    return dao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  IGenericDAO<TraceRegSecuriteIndexCql, Date> getIndexDao() {
    return indexDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  TraceRegSecuriteIndexCql getIndexFromTrace(final TraceRegSecuriteCql trace) {
    final TraceRegSecuriteIndexCql index = new TraceRegSecuriteIndexCql(trace);
    // date en string sous la forme de YYYYMMJJ sans les heures et les secondes
    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    index.setIdentifiantIndex(journee);
    return index;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getRegistreName() {
    return REG_SECURITE_NAME;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  TraceRegSecuriteCql createNewInstance(final UUID idTrace, final Date timestamp) {
    return new TraceRegSecuriteCql(idTrace, timestamp);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  TimeUUIDEtTimestampSupport getTimeUuidSupport() {
    return timeUUIDSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  Logger getLogger() {
    return LOGGER;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getDateFormat() {
    return DATE_FORMAT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getIndexId(final TraceRegSecuriteIndexCql trace) {
    return trace.getIdentifiantIndex();
  }

}
