/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

import java.lang.reflect.Field;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.annotations.PartitionKey;

import fr.urssaf.image.commons.cassandra.cql.dao.impl.GenericDAOImpl;
import fr.urssaf.image.commons.cassandra.helper.CassandraCQLClientFactory;
import fr.urssaf.image.commons.cassandra.utils.ColumnUtil;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceJournalEvtIndexCqlDao} de la famille de colonnes {@link TraceJournalEvtIndexCql}
 * 
 * @param <TraceJournalEvtIndexCql>
 *          Type de d'objet contenue dans le registre
 * @param <String>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceJournalEvtIndexCqlDaoImpl extends GenericDAOImpl<TraceJournalEvtIndexCql, String> implements ITraceJournalEvtIndexCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceJournalEvtIndexCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

  @Override
  public Iterator<TraceJournalEvtIndexCql> IterableFindById(final String journee, final boolean ordreInverse) {
    Assert.notNull(journee, "L'identifiant ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

    final String keyName = keyField.getName();
    select.where(eq(keyName, journee));
    if (ordreInverse) {
      select.orderBy(QueryBuilder.desc("timestamp"));
    } else {
      select.orderBy(QueryBuilder.asc("timestamp"));
    }
    final ResultSet result = getSession().execute(select);
    return getMapper().map(result).iterator();
  }
}
