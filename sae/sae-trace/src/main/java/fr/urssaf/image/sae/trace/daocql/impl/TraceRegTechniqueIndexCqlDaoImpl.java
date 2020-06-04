package fr.urssaf.image.sae.trace.daocql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.gte;
import static com.datastax.driver.core.querybuilder.QueryBuilder.lte;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
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
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;

/**
 * Implementation de l'inerface DAO {@link ITraceRegTechniqueIndexCqlDao} de la famille de colonnes {@link TraceRegTechniqueIndexCql}
 * 
 * @param <TraceRegTechniqueIndexCql>
 *          Type de d'objet contenue dans le registre
 * @param <UUID>
 *          Le type d'Identifiant ({@link PartitionKey}) de l'objet
 */
@Repository
public class TraceRegTechniqueIndexCqlDaoImpl extends GenericDAOImpl<TraceRegTechniqueIndexCql, String> implements ITraceRegTechniqueIndexCqlDao {

  /**
   * @param ccf
   */
  @Autowired
  public TraceRegTechniqueIndexCqlDaoImpl(final CassandraCQLClientFactory ccf) {
    super(ccf);
  }

  @Override
  public Iterator<TraceRegTechniqueIndexCql> IterableFindById(final String journee, final boolean ordreInverse) {
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

  /**
   * {@inheritDoc}
   * 
   * @throws ParseException
   */
  @Override
  public Iterator<TraceRegTechniqueIndexCql> IterableFindById(final String journee, final boolean ordreInverse, final Date dateDebut, final Date dateFin,
                                                              final int limit)
  {
    Assert.notNull(journee, "L'identifiant ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

    final String keyName = keyField.getName();
    select.where(eq(keyName, journee));
    // Condition pour filtrer après le timestamp
    select.where(gte("timestamp", dateDebut));
    // Condition pour filtrer avant le timestamp
    select.where(lte("timestamp", dateFin));
    // Tri sur timestamp par date croissante ou décroissante (ordreInverse)
    if (ordreInverse) {
      select.orderBy(QueryBuilder.desc("timestamp"));
    } else {
      select.orderBy(QueryBuilder.asc("timestamp"));
    }
    // Clause pour permettre le filtre des timestamp
    select.allowFiltering();
    select.limit(limit);
    final ResultSet result = getSession().execute(select);
    return getMapper().map(result).iterator();
  }
}
