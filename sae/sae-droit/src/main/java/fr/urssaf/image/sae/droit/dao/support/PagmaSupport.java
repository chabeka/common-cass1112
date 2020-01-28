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
import fr.urssaf.image.sae.droit.dao.PagmaDao;
import fr.urssaf.image.sae.droit.dao.model.Pagma;
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
 * Classe de support de la classe {@link PagmaDao}
 * 
 */
@Component
public class PagmaSupport {

  private final PagmaDao dao;

  /**
   * constructeur
   * 
   * @param pagmaDao
   *           DAO associée au pagma
   */
  @Autowired
  public PagmaSupport(final PagmaDao pagmaDao) {
    dao = pagmaDao;
  }

  /**
   * Méthode de création d'un ligne
   * 
   * @param pagma
   *           propriétés du PAGMa à créer
   * @param clock
   *           horloge de la création
   */
  public final void create(final Pagma pagma, final long clock) {

    final ColumnFamilyUpdater<String, String> updater = dao.getCfTmpl()
        .createUpdater(pagma.getCode());

    for (final String code : pagma.getActionUnitaires()) {
      dao.ecritActionUnitaire(updater, code, clock);
    }
    dao.getCfTmpl().update(updater);

  }

  /**
   * Méthode de création d'un ligne avec Mutator
   * 
   * @param pagma
   *           propriétés du PAGMa à créer
   * @param clock
   *           horloge de la création
   * @param mutator
   *           mutator de la ligne de Pagma
   */
  public final void create(final Pagma pagma, final long clock, final Mutator<String> mutator) {

    for (final String action : pagma.getActionUnitaires()) {
      dao.ecritActionUnitaire(pagma.getCode(), action, clock, mutator);
    }

  }

  /**
   * Méthode de suppression d'une ligne
   * 
   * @param code
   *           identifiant du PAGMa
   * @param clock
   *           horloge de suppression
   */
  public final void delete(final String code, final long clock) {

    final Mutator<String> mutator = dao.createMutator();

    dao.mutatorSuppressionLigne(mutator, code, clock);

    mutator.execute();
  }

  /**
   * Méthode de suppression d'une ligne avec Mutator en paramètre
   * 
   * @param code
   *           identifiant du PAGMa
   * @param clock
   *           horloge de suppression
   * @param mutator
   *           Mutator
   */
  public final void delete(final String code, final long clock, final Mutator<String> mutator) {
    dao.mutatorSuppressionLigne(mutator, code, clock);
  }

  /**
   * Méthode de lecture d'une ligne
   * 
   * @param code
   *           identifiant du PAGMa
   * @return un PAGMa correpodant à l'identifiant passé en paramètre
   */
  public final Pagma find(final String code) {

    final ColumnFamilyResult<String, String> result = dao.getCfTmpl().queryColumns(
                                                                                   code);

    final Pagma pagma = getPagmaFromResult(result);

    return pagma;

  }

  private Pagma getPagmaFromResult(final ColumnFamilyResult<String, String> result) {

    Pagma pagma = null;

    if (result != null && result.hasResults()) {
      pagma = new Pagma();
      pagma.setCode(result.getKey());
      final List<String> list = new ArrayList<>(result.getColumnNames());
      pagma.setActionUnitaires(list);
    }

    return pagma;
  }

  /**
   * Retourne l'ensemble des Pagma
   * 
   * @return l'ensemble des pagma
   */
  public final List<Pagma> findAll() {

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
    final List<Pagma> list = new ArrayList<>();
    for (final ColumnFamilyResult<String, String> row : resultIterator) {

      list.add(getPagmaFromResult(row));

    }
    return list;

  }
}
