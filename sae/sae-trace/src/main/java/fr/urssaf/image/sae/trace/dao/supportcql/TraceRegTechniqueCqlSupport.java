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
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

@Component
public class TraceRegTechniqueCqlSupport extends GenericAbstractTraceCqlSupport<TraceRegTechniqueCql, TraceRegTechniqueIndexCql> {

  private static final String REG_SECURITE_NAME = "registre de sécurité";

  private static final String DATE_FORMAT = "yyyyMMdd";

  private final ITraceRegTechniqueCqlDao dao;

  private final ITraceRegTechniqueIndexCqlDao indexDao;

  private final TimeUUIDEtTimestampSupport timeUUIDSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(TraceRegTechniqueCqlSupport.class);

  @Autowired
  public TraceRegTechniqueCqlSupport(final ITraceRegTechniqueCqlDao dao,
                                     final ITraceRegTechniqueIndexCqlDao indexDao,
                                     final TimeUUIDEtTimestampSupport timeUUIDSupport) {
    super();
    this.dao = dao;
    this.indexDao = indexDao;
    this.timeUUIDSupport = timeUUIDSupport;
  }

  @Override
  Iterator<TraceRegTechniqueIndexCql> getIterator(final Date date) {
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

  @Override
  Iterator<TraceRegTechniqueIndexCql> getIterator(final Date dateStar, final Date dateEnd, final boolean reversed, final Integer limit) {
    return indexDao.findByDateInterval(DateRegUtils.getJournee(dateStar), DateRegUtils.getJournee(dateEnd), reversed, limit);
  }

  @Override
  IGenericDAO<TraceRegTechniqueCql, UUID> getDao() {
    return dao;
  }

  @Override
  IGenericDAO<TraceRegTechniqueIndexCql, Date> getIndexDao() {
    return indexDao;
  }

  @Override
  TraceRegTechniqueIndexCql getIndexFromTrace(final TraceRegTechniqueCql trace) {
    final TraceRegTechniqueIndexCql index = new TraceRegTechniqueIndexCql(trace);
    // date en string sous la forme de YYYYMMJJ sans les heures et les secondes
    final String journee = DateRegUtils.getJournee(index.getTimestamp());
    index.setIdentifiantIndex(journee);
    return index;
  }

  @Override
  String getRegistreName() {
    return REG_SECURITE_NAME;
  }

  @Override
  TraceRegTechniqueCql createNewInstance(final UUID idTrace, final Date timestamp) {
    return new TraceRegTechniqueCql(idTrace, timestamp);
  }

  @Override
  TimeUUIDEtTimestampSupport getTimeUuidSupport() {
    return timeUUIDSupport;
  }

  @Override
  Logger getLogger() {
    return LOGGER;
  }

  @Override
  String getDateFormat() {
    return DATE_FORMAT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  String getIndexId(final TraceRegTechniqueIndexCql trace) {
    return trace.getIdentifiantIndex();
  }

}
