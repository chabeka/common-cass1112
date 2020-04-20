/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.PagmDao;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;
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
 * Classe de support de la classe {@link PagmDao}
 * 
 */
@Component
public class PagmSupport {

  private final PagmSerializer pagmSerializer = PagmSerializer.get();

  private final PagmDao dao;

  /**
   * constructeur
   * 
   * @param pagmDao
   *           DAO associée au pagm
   */
  @Autowired
  public PagmSupport(final PagmDao pagmDao) {
    dao = pagmDao;
  }

  /**
   * Méthode de création d'un ligne
   * 
   * @param idClient
   *           identifiant du client
   * @param pagm
   *           propriétés du PAGM à créer
   * @param clock
   *           horloge de la création
   */
  public final void create(final String idClient, final Pagm pagm, final long clock) {

    final ColumnFamilyUpdater<String, String> updater = dao.getCfTmpl()
        .createUpdater(idClient);

    dao.ecritPagm(updater, pagm, clock);

    dao.getCfTmpl().update(updater);

  }

  /**
   * Méthode de création d'un ligne avec utilisation d'un mutator
   * 
   * @param idClient
   *           identifiant du client
   * @param pagm
   *           propriétés du PAGM à créer
   * @param clock
   *           horloge de la création
   * @param mutator
   *           mutator de la ligne de pagm
   */
  public final void create(final String idClient, final Pagm pagm, final long clock,
                           final Mutator<String> mutator) {

    dao.mutatorEcritPagm(idClient, pagm, clock, mutator);
  }

  /**
   * Méthode de suppression d'une ligne
   * 
   * @param idContratService
   *           identifiant du contrat de service auquel est rattaché le PAGM
   * @param codePagm
   *           Identifiant du PAGM à supprimer
   * @param clock
   *           horloge de suppression
   */
  public final void delete(final String idContratService, final String codePagm, final long clock) {

    final Mutator<String> mutator = dao.createMutator();

    dao.mutatorSuppressionPagm(mutator, idContratService, codePagm, clock);

    mutator.execute();
  }

  /**
   * Méthode de suppression d'une ligne avec mutator en paramètre
   * 
   * @param idContratService
   *           Identifiant du contrat de service auquel le PAGM est rattaché
   * @param codePagm
   *           identifiant du Pagm à supprimer
   * @param clock
   *           horloge de suppression
   * @param mutator
   *           Mutator
   */
  public final void delete(final String idContratService, final String codePagm,
                           final long clock, final Mutator<String> mutator) {
    dao.mutatorSuppressionPagm(mutator, idContratService, codePagm, clock);
  }

  /**
   * Méthode de lecture d'une ligne
   * 
   * @param code
   *           identifiant du Contrat de service
   * @return la liste des PAGM correspondant à l'identifiant passé en paramètre
   */
  public final List<Pagm> find(final String code) {

    dao.getCfTmpl().setCount(AbstractDao.DEFAULT_MAX_COLS);
    final ColumnFamilyResult<String, String> result = dao.getCfTmpl().queryColumns(
                                                                                   code);

    final Collection<String> colNames = result.getColumnNames();

    List<Pagm> pagms = null;

    if (CollectionUtils.isNotEmpty(colNames)) {
      pagms = new ArrayList<>(colNames.size());
    }

    for (final String name : colNames) {

      final byte[] bResult = result.getByteArray(name);
      final Pagm pagm = pagmSerializer.fromBytes(bResult);

      pagms.add(pagm);
    }

    return pagms;

  }


  /**
   * Retourne l'ensemble des Pagm sous forme de liste PagmCql
   * 
   * @return l'ensemble des pagm
   */

  public final List<PagmCql> findAll() {

    final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
    final RangeSlicesQuery<byte[], String, byte[]> rangeSlicesQuery = HFactory
        .createRangeSlicesQuery(dao.getKeyspace(),
                                bytesSerializer,
                                StringSerializer.get(),
                                bytesSerializer);
    rangeSlicesQuery.setColumnFamily(dao.getColumnFamilyName());
    rangeSlicesQuery.setRange(
                              StringUtils.EMPTY,
                              StringUtils.EMPTY,
                              false,
                              AbstractDao.DEFAULT_MAX_COLS);
    rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
    QueryResult<OrderedRows<byte[], String, byte[]>> queryResult;
    queryResult = rangeSlicesQuery.execute();
    // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
    // son utilisation
    final QueryResultConverter<byte[], String, byte[]> converter = new QueryResultConverter<>();
    final ColumnFamilyResultWrapper<byte[], String> result = converter
        .getColumnFamilyResultWrapper(queryResult,
                                      bytesSerializer,
                                      StringSerializer.get(),
                                      bytesSerializer);
    // On itère sur le résultat
    final HectorIterator<byte[], String> resultIterator = new HectorIterator<>(
        result);
    final List<PagmCql> list = new ArrayList<>();
    for (final ColumnFamilyResult<byte[], String> row : resultIterator) {
      if (row != null && row.hasResults()) {

        final PagmCql pagm = getPagmFromResult(row);
        list.add(pagm);
      }
    }
    return list;
  }

  private PagmCql getPagmFromResult(final ColumnFamilyResult<byte[], String> result) {

    PagmCql pagm = null;

    if (result != null && result.hasResults()) {
      pagm = new PagmCql();

      pagm.setIdClient(result.getKey().toString());

      pagm.setCode(result.getString("code"));

      pagm.setPagma(result.getString("pagma"));

      pagm.setPagmf(result.getString("pagmf"));

      pagm.setPagmp(result.getString("pagmp"));

      pagm.setDescription(result.getString("description"));

      pagm.setCompressionPdfActive(result.getBoolean("compressionPdfActive"));

      pagm.setSeuilCompressionPdf(result.getInteger("seuilCompressionPdf"));

      // Voir pour parametres
    }

    return pagm;
  }
}
