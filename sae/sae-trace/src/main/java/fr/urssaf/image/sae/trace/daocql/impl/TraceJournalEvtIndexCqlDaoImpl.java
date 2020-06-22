/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.sae.trace.daocql.impl;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static com.datastax.driver.core.querybuilder.QueryBuilder.gte;
import static com.datastax.driver.core.querybuilder.QueryBuilder.lte;

import java.lang.reflect.Field;
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
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;
import fr.urssaf.image.sae.trace.utils.DateRegUtils;

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

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterator<TraceJournalEvtIndexCql> IterableFindById(final String journee, final boolean ordreInverse, final Date dateDebut, final Date dateFin,
                                                            final int limit) {
    Assert.notNull(journee, "L'identifiant ne peut être null");
    final Select select = QueryBuilder.select().from(ccf.getKeyspace(), getTypeArgumentsName());
    final Field keyField = ColumnUtil.getSimplePartionKeyField(daoType);
    Assert.notNull(keyField, "La clé de l'entité à chercher ne peut être null");

    final String keyName = keyField.getName();
    select.where(eq(keyName, journee));
    // Si les heures et minutes ne sont pas celles par défaut Condition pour filtrer après le timestamp
    if (!DateRegUtils.dateDebutDefaut(dateDebut)) {
      select.where(gte("timestamp", dateDebut));
    }
    // Si les heures et minutes ne sont pas celles par défaut Condition pour filtrer avant le timestamp
    if (!DateRegUtils.dateFinDefaut(dateFin)) {
      select.where(lte("timestamp", dateFin));
    }
    // Tri sur timestamp par date croissante ou décroissante (ordreInverse)
    if (ordreInverse) {
      select.orderBy(QueryBuilder.desc("timestamp"));
    } else {
      select.orderBy(QueryBuilder.asc("timestamp"));
    }
    // Si les heures et minutes ne sont pas celles par défaut clause pour permettre le filtre des timestamp
    if (!DateRegUtils.dateDebutDefaut(dateDebut) || !DateRegUtils.dateFinDefaut(dateFin)) {
      select.allowFiltering();
    }
    select.limit(limit);
    final ResultSet result = getSession().execute(select);
    return getMapper().map(result).iterator();
  }

}
