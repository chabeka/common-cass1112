package fr.urssaf.image.sae.trace.daocql.impl;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;

@Repository
public class TraceRegTechniqueIndexCqlDaoImpl extends GenericIndexCqlDaoImpl<TraceRegTechniqueIndexCql, String> implements ITraceRegTechniqueIndexCqlDao {

}
