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
import fr.urssaf.image.sae.droit.dao.PagmpDao;
import fr.urssaf.image.sae.droit.dao.model.Pagmp;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

/**
 * Classe de support de la classe {@link PagmpDao}
 * 
 */
@Component
public class PagmpSupport extends AbstractSupport<Pagmp, String, String> {

  private final PagmpDao dao;

  /**
   * constructeur
   * 
   * @param pagmpDao
   *           DAO associée au pagmp
   */
  @Autowired
  public PagmpSupport(final PagmpDao pagmpDao) {
    dao = pagmpDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void create(final Pagmp pagmp, final long clock) {

    final ColumnFamilyUpdater<String, String> updater = dao.getCfTmpl()
        .createUpdater(pagmp.getCode());

    dao.ecritDescription(updater, pagmp.getDescription(), clock);
    dao.ecritPrmd(updater, pagmp.getPrmd(), clock);

    dao.getCfTmpl().update(updater);

  }

  /**
   * Méthode de création d'un ligne avec Mutator
   * 
   * @param pagmp
   *           propriétés du PAGMp à créer
   * @param clock
   *           horloge de la création
   * @param mutator
   *           Mutator
   */
  public final void create(final Pagmp pagmp, final long clock, final Mutator<String> mutator) {
    dao.ecritDescription(pagmp.getCode(), pagmp.getDescription(), clock,
                         mutator);
    dao.ecritPrmd(pagmp.getCode(), pagmp.getPrmd(), clock, mutator);
  }

  /**
   * Méthode de suppression d'une ligne avec Murator en paramètre
   * 
   * @param code
   *           identifiant du PAGMp
   * @param clock
   *           horloge de suppression
   * @param mutator
   *           Mutator
   */
  public final void delete(final String code, final long clock, final Mutator<String> mutator) {
    dao.mutatorSuppressionLigne(mutator, code, clock);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final Pagmp getObjectFromResult(
                                            final ColumnFamilyResult<String, String> result) {

    Pagmp pagmp = null;

    if (result != null && result.hasResults()) {
      pagmp = new Pagmp();
      pagmp.setCode(result.getKey());
      pagmp.setDescription(result.getString(PagmpDao.PAGMP_DESCRIPTION));
      pagmp.setPrmd(result.getString(PagmpDao.PAGMP_PRMD));
    }

    return pagmp;
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
   * Retourne l'ensemble des Pagmp
   * 
   * @return l'ensemble des pagmp
   */
  public final List<Pagmp> findAll() {

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
    final List<Pagmp> list = new ArrayList<>();
    for (final ColumnFamilyResult<String, String> row : resultIterator) {

      list.add(getObjectFromResult(row));

    }
    return list;

  }

}
