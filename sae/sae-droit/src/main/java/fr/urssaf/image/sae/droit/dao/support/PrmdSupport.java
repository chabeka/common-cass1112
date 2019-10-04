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
import fr.urssaf.image.sae.droit.dao.PrmdDao;
import fr.urssaf.image.sae.droit.dao.model.FormatControlProfil;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.droit.exception.DroitRuntimeException;
import fr.urssaf.image.sae.droit.utils.ResourceMessagesUtils;
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
 * Classe de support de la classe {@link PrmdDao}
 * 
 */
@Component
public class PrmdSupport extends AbstractSupport<Prmd, String, String> {

  private final PrmdDao prmdDao;

  /**
   * constructeur
   * 
   * @param prmdDao
   *           DAO associée au PRMD
   */
  @Autowired
  public PrmdSupport(final PrmdDao prmdDao) {
    this.prmdDao = prmdDao;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void create(final Prmd prmd, final long clock) {
    final ColumnFamilyUpdater<String, String> updater = prmdDao.getCfTmpl()
        .createUpdater(prmd.getCode());

    prmdDao.ecritDescription(updater, prmd.getDescription(), clock);

    if (prmd.getLucene() != null) {
      prmdDao.ecritLucene(updater, prmd.getLucene(), clock);
    }
    if (prmd.getBean() != null) {
      prmdDao.ecritBean(updater, prmd.getBean(), clock);
    }
    if (prmd.getMetadata() != null) {
      prmdDao.ecritMetaData(updater, prmd.getMetadata(), clock);
    }

    prmdDao.getCfTmpl().update(updater);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final Prmd getObjectFromResult(final ColumnFamilyResult<String, String> result) {
    Prmd prmd = null;

    if (result != null && result.hasResults()) {
      prmd = new Prmd();
      prmd.setCode(result.getKey());
      prmd.setDescription(result.getString(PrmdDao.PRMD_DESCRIPTION));
      prmd.setLucene(result.getString(PrmdDao.PRMD_LUCENE));

      if (result.getByteArray(PrmdDao.PRMD_METADATA) != null) {
        final byte[] bMetadata = result.getByteArray(PrmdDao.PRMD_METADATA);
        prmd.setMetadata(MapSerializer.get().fromBytes(bMetadata));
      }

      prmd.setBean(result.getString(PrmdDao.PRMD_BEAN));
    }

    return prmd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected final AbstractDao<String, String> getDao() {
    return prmdDao;
  }

  /**
   * Récupération de tous les FormatControlProfil de la famille de colonne.
   * 
   * @return Une liste d’objet {@link FormatControlProfil} représentant le
   *         contenu de CF DroitFormatControlProfil
   */
  public final List<Prmd> findAll() {

    try {
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

      final RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
          .createRangeSlicesQuery(prmdDao.getKeyspace(),
                                  StringSerializer.get(),
                                  StringSerializer.get(),
                                  bytesSerializer);

      rangeSlicesQuery.setColumnFamily(prmdDao
                                       .getColumnFamilyName());
      rangeSlicesQuery.setRange("", "", false, AbstractDao.DEFAULT_MAX_COLS);
      rangeSlicesQuery.setRowCount(AbstractDao.DEFAULT_MAX_ROWS);
      final QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
          .execute();

      // Convertion du résultat en ColumnFamilyResultWrapper pour mieux
      // l'utiliser
      final QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<>();
      final ColumnFamilyResultWrapper<String, String> result = converter
          .getColumnFamilyResultWrapper(queryResult,
                                        StringSerializer
                                        .get(),
                                        StringSerializer.get(),
                                        bytesSerializer);

      final HectorIterator<String, String> resultIterator = new HectorIterator<>(
          result);

      final List<Prmd> list = new ArrayList<>();
      for (final ColumnFamilyResult<String, String> row : resultIterator) {
        if (row != null && row.hasResults()) {

          final Prmd prmd = getObjectFromResult(row);
          list.add(prmd);
        }
      }

      return list;
    }
    catch (final Exception except) {
      throw new DroitRuntimeException(ResourceMessagesUtils
                                      .loadMessage("erreur.impossible.recup.info"),
                                      except);
    }
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
