package fr.urssaf.image.sae.trace.daocql;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.mapping.Mapper;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexDocCql;

/**
 * Interface DAO de {@link TraceJournalEvtIndexDocCql}
 * 
 * @param <TraceJournalEvtIndexDocCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant de l'objet
 */
public interface ITraceJournalEvtIndexDocCqlDao extends IGenericDAO<TraceJournalEvtIndexDocCql, UUID> {
	
	/**
	   * Retourne toutes les entitées de la table en utilsant le {@link Mapper} pour mapper
	   * le resultat ({@link ResultSet}) avec le type T fournie en paramètre de la classe.
	   * La liste retournée est une liste mapper de type T
	   *
	   * @return La {@link List} de type T
	   */
	  public default Iterator<TraceJournalEvtIndexDocCql> findAllWithMapper() {
	    final Statement st = QueryBuilder.select().from(getCcf().getKeyspace(), getTypeArgumentsName());
	    return getMapper().map(getSession().execute(st)).iterator();
	  }
}
