/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.ContratServiceDao;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.serializer.ListSerializer;
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
 * Classe de support de la classe {@link ContratServiceDao}
 * 
 */
@Component
public class ContratServiceSupport extends
AbstractSupport<ServiceContract, String, String> {

  private final ContratServiceDao dao;

  /**
   * constructeur
   * 
   * @param csDao
   *           DAO associée au Constrat de service
   */
  @Autowired
  public ContratServiceSupport(final ContratServiceDao csDao) {
    dao = csDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void create(final ServiceContract contratService, final long clock) {

    // On utilise un ColumnFamilyUpdater, et on renseigne
    // la valeur de la clé dans la construction de l'updater
    final ColumnFamilyUpdater<String, String> updaterJobRequest = dao.getCfTmpl()
        .createUpdater(contratService.getCodeClient());

    // écriture des colonnes
    dao.ecritLibelle(updaterJobRequest, contratService.getLibelle(), clock);
    dao.ecritDescription(updaterJobRequest, contratService.getDescription(),
                         clock);
    dao.ecritViDuree(updaterJobRequest, contratService.getViDuree(), clock);

    if (CollectionUtils.isNotEmpty(contratService.getListPki())) {
      dao.ecritListePki(updaterJobRequest, contratService.getListPki(),
                        clock);
    } else if (StringUtils.isNotEmpty(contratService.getIdPki())) {
      dao.ecritListePki(updaterJobRequest, Arrays.asList(contratService
                                                         .getIdPki()), clock);
    }

    if (CollectionUtils.isNotEmpty(contratService.getListCertifsClient())) {
      dao.ecritListeCert(updaterJobRequest, contratService
                         .getListCertifsClient(), clock);
    } else if (contratService.getIdCertifClient() != null) {
      dao.ecritListeCert(updaterJobRequest, Arrays.asList(contratService
                                                          .getIdCertifClient()), clock);
    }

    dao.ecritFlagControlNommage(updaterJobRequest, contratService
                                .isVerifNommage(), clock);

    // écriture en base
    dao.getCfTmpl().update(updaterJobRequest);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final ServiceContract getObjectFromResult(
                                                      final ColumnFamilyResult<String, String> result) {

    ServiceContract contract = null;

    if (result != null && result.hasResults()) {
      contract = new ServiceContract();
      contract.setCodeClient(result.getKey());
      contract.setLibelle(result.getString(ContratServiceDao.CS_LIBELLE));
      contract.setDescription(result
                              .getString(ContratServiceDao.CS_DESCRIPTION));
      contract.setViDuree(result.getLong(ContratServiceDao.CS_VI_DUREE));

      if (result.getString(ContratServiceDao.CS_PKI) != null) {
        contract.setIdPki(result.getString(ContratServiceDao.CS_PKI));
      }

      if (result.getString(ContratServiceDao.CS_CERT) != null) {
        contract.setIdCertifClient(result
                                   .getString(ContratServiceDao.CS_CERT));
      }

      if (result.getByteArray(ContratServiceDao.CS_LISTE_CERT) != null) {
        final byte[] bytes = result.getByteArray(ContratServiceDao.CS_LISTE_CERT);
        contract
        .setListCertifsClient(ListSerializer.get().fromBytes(bytes));
      }

      if (result.getByteArray(ContratServiceDao.CS_LISTE_PKI) != null) {
        final byte[] bytes = result.getByteArray(ContratServiceDao.CS_LISTE_PKI);
        contract.setListPki(ListSerializer.get().fromBytes(bytes));
      }

      if (result.getBoolean(ContratServiceDao.CS_VERIF_NOMMAGE) != null) {
        contract.setVerifNommage(result
                                 .getBoolean(ContratServiceDao.CS_VERIF_NOMMAGE));
      }
    }

    return contract;
  }

  /**
   * Retourne l'ensemble des Pagma
   * 
   * @return l'ensemble des pagma
   */
  public final List<ServiceContract> findAll() {

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
    final List<ServiceContract> list = new ArrayList<>();
    for (final ColumnFamilyResult<String, String> row : resultIterator) {

      list.add(getObjectFromResult(row));

    }
    return list;

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
}
