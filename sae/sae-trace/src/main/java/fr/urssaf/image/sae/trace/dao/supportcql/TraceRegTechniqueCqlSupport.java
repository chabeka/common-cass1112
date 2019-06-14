package fr.urssaf.image.sae.trace.dao.supportcql;

import java.util.Date;
import java.util.Iterator;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;
import fr.urssaf.image.sae.trace.support.TimeUUIDEtTimestampSupport;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

@Component
public class TraceRegTechniqueCqlSupport extends GenericAbstractTraceCqlSupport<TraceRegTechniqueCql, TraceRegTechniqueIndexCql> {

  private static final String REG_SECURITE_NAME = "registre de sécurité";

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
  public Iterator<TraceRegTechniqueIndexCql> getIterator(final Date date) {
    final String journee = DateRegUtils.getJournee(date);
    return indexDao.IterableFindById(journee);
  }

  @Override
  IGenericDAO<TraceRegTechniqueCql, UUID> getDao() {
    return dao;
  }

  @Override
  IGenericDAO<TraceRegTechniqueIndexCql, String> getIndexDao() {
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

  /**
   * {@inheritDoc}
   */
  @Override
  UUID getTraceId(final TraceRegTechniqueIndexCql trace) {
    return trace.getIdentifiant();
  }

}
