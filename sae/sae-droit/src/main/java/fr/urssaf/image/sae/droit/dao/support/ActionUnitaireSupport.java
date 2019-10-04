/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.ActionUnitaireDao;
import fr.urssaf.image.sae.droit.dao.model.ActionUnitaire;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Classe de support de la classe {@link ActionUnitaireDao}
 * 
 */
@Component
public class ActionUnitaireSupport extends
AbstractSupport<ActionUnitaire, String, String> {

  private final ActionUnitaireDao dao;

  /**
   * constructeur
   * 
   * @param auDao
   *           DAO associée aux Actions unitaires
   */
  @Autowired
  public ActionUnitaireSupport(final ActionUnitaireDao auDao) {
    dao = auDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void create(final ActionUnitaire actionUnitaire, final long clock) {

    // On utilise un ColumnFamilyUpdater, et on renseigne
    // la valeur de la clé dans la construction de l'updater
    final ColumnFamilyUpdater<String, String> updaterActionUnitaireRequest = dao.getCfTmpl()
        .createUpdater(actionUnitaire.getCode());

    // Ecriture des colonnes
    dao.ecritDescription(updaterActionUnitaireRequest,
                         actionUnitaire.getDescription(),
                         clock);

    // Ecrit en base
    dao.getCfTmpl().update(updaterActionUnitaireRequest);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final ActionUnitaire getObjectFromResult(
                                                     final ColumnFamilyResult<String, String> row) {

    ActionUnitaire actionUnitaire = null;

    if (row != null && row.hasResults()) {
      actionUnitaire = new ActionUnitaire();

      actionUnitaire.setCode(row.getKey());
      actionUnitaire.setDescription(row
                                    .getString(ActionUnitaireDao.AU_DESCRIPTION));
    }
    return actionUnitaire;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final AbstractDao<String, String> getDao() {
    return dao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final String getMax() {
    return StringUtils.EMPTY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final String getMin() {
    return StringUtils.EMPTY;
  }

  /**
   * Retourne l'ensemble des actions unitaires
   * 
   * @return l'ensemble des actions unitaires
   */
  public final List<ActionUnitaire> findAll() {

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(dao.getKeyspace(),
                                StringSerializer.get(),
                                StringSerializer.get(),
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(dao.getColumnFamilyName());
    rangeSlicesQuery.setRange(
                              StringUtils.EMPTY,
                              StringUtils.EMPTY,
                              false,
                              AbstractDao.DEFAULT_MAX_COLS);
    rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
    QueryResult<OrderedRows<String, String, byte[]>> queryResult;
    queryResult = rangeSlicesQuery.execute();

    // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
    // son utilisation
    final QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<>();
    final ColumnFamilyResultWrapper<String, String> result = converter
        .getColumnFamilyResultWrapper(queryResult,
                                      StringSerializer.get(),
                                      StringSerializer.get(),
                                      bytesSerializer);

    // On itère sur le résultat
    final HectorIterator<String, String> resultIterator = new HectorIterator<>(
        result);
    final List<ActionUnitaire> list = new ArrayList<>();
    for (final ColumnFamilyResult<String, String> row : resultIterator) {

      list.add(getObjectFromResult(row));

    }
    return list;

  }

}
