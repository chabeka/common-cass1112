package fr.urssaf.image.sae.trace.daocql.impl;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueCqlDao;

@Repository
public class TraceRegTechniqueDaoImpl extends GenericDAOImpl<TraceRegTechniqueCql, UUID> implements ITraceRegTechniqueCqlDao {

}