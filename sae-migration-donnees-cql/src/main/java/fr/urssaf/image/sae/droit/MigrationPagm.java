/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.datastax.driver.core.Row;

import fr.urssaf.image.commons.cassandra.helper.CassandraClientFactory;
import fr.urssaf.image.sae.droit.dao.IGenericDroitTypeDao;
import fr.urssaf.image.sae.droit.dao.cql.IPagmDaoCql;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.modelcql.PagmCql;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;
import fr.urssaf.image.sae.droit.dao.support.PagmSupport;
import fr.urssaf.image.sae.droit.model.GenericDroitType;
import fr.urssaf.image.sae.droit.utils.PagmUtils;
import fr.urssaf.image.sae.utils.CompareUtils;
import me.prettyprint.cassandra.serializers.StringSerializer;

/**
 * (AC75095351) Classe de migration pour les pagm
 */
@Component
public class MigrationPagm {

  @Autowired
  private IPagmDaoCql pagmDaoCql;

  @Autowired
  private PagmSupport pagmSupport;

  @Autowired
  IGenericDroitTypeDao genericdao;

  @Autowired
  private CassandraClientFactory ccf;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationPagm.class);

  /**
   * Migration pagm thrift->cql
   */
  public List<String> migrationFromThriftToCql() {
    final List<String> keys = new ArrayList<>();
    final Iterator<GenericDroitType> it = genericdao.findAllByCFName("DroitPagm", ccf.getKeyspace().getKeyspaceName());

    PagmCql pagmCql;
    int nb = 0;
    String key = null;

    while (it.hasNext()) {
      // Extraction de la clé
      final Row row = (Row) it.next();
      key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (key != null && !keys.contains(key)) {
        keys.add(key);
      }
      // extraction de la colonne
      // final ByteBuffer columnName = row.getBytes("column1");
      pagmCql = new PagmCql();
      pagmCql.setIdClient(key);
      // extraction de la value
      final Pagm pagmValue = PagmSerializer.get().fromByteBuffer(row.getBytes("value"));
      pagmCql.setCode(pagmValue.getCode());
      pagmCql.setPagma(pagmValue.getPagma());
      pagmCql.setPagmp(pagmValue.getPagmp());
      pagmCql.setPagmf(pagmValue.getPagmf());
      pagmCql.setDescription(pagmValue.getDescription());
      final Map<String, String> map = pagmValue.getParametres();
      pagmCql.setParametres(map);
      pagmCql.setCompressionPdfActive(pagmValue.getCompressionPdfActive());
      pagmCql.setSeuilCompressionPdf(pagmValue.getSeuilCompressionPdf());
      // enregistrement
      // pagmDaoCql.save(pagmCql);
      pagmDaoCql.saveWithMapper(pagmCql);

      nb++;
    }
    final List<PagmCql> pagmsCql = new ArrayList<>();
    final Iterator<PagmCql> pagmsIterator = pagmDaoCql.findAllWithMapper();
    pagmsIterator.forEachRemaining(pagmsCql::add);
    comparePagms(getListPagmCqlFromThrift(keys), pagmsCql);

    LOGGER.debug(" Totale : " + nb);
    return keys;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_PAGM - migrationFromCqlTothrift- start ");
    final Iterator<PagmCql> pagmsCqlIterator = pagmDaoCql.findAllWithMapper();
    final List<PagmCql> pagmsCql = new ArrayList<>();
    while (pagmsCqlIterator.hasNext()) {
      final PagmCql pagmCql = pagmsCqlIterator.next();
      pagmsCql.add(pagmCql);
      final Pagm pagm = new Pagm();
      pagm.setCode(pagmCql.getCode());
      pagm.setPagma(pagmCql.getPagma());
      pagm.setPagmp(pagmCql.getPagmp());
      pagm.setPagmf(pagmCql.getPagmf());
      pagm.setDescription(pagmCql.getDescription());
      pagm.setParametres(pagmCql.getParametres());
      pagm.setCompressionPdfActive(pagmCql.getCompressionPdfActive());
      pagm.setSeuilCompressionPdf(pagmCql.getSeuilCompressionPdf());

      pagmSupport.create(pagmCql.getIdClient(), pagm, new Date().getTime());
    }
    comparePagms(getListPagmCqlFromThrift(getKeys()), pagmsCql);
    LOGGER.info(" MIGRATION_PAGM - migrationFromCqlTothrift- end ");

  }

  public List<PagmCql> getListPagmCqlFromThrift(final List<String> keys) {
    final List<PagmCql> listAllFromThrift = new ArrayList<>();
    for (final String idClient : keys) {
      final List<Pagm> listThrift = pagmSupport.find(idClient);
      final List<PagmCql> listPagmFromThrift = PagmUtils.convertListPagmToListPagmCql(listThrift, idClient);
      if (listPagmFromThrift != null && !listPagmFromThrift.isEmpty()) {
        listAllFromThrift.addAll(listPagmFromThrift);
      }
    }
    return listAllFromThrift;
  }

  /**
   * Comparaison des liste en taille et en contenu
   * 
   * @param pagmThrift
   * @param pagmCql
   */
  private void comparePagms(final List<PagmCql> pagmThrift, final List<PagmCql> pagmCql) {

    LOGGER.info("MIGRATION_PAGM -- SizeThriftParameter=" + pagmThrift.size());
    LOGGER.info("MIGRATION_PAGM -- SizeCqlParameter=" + pagmCql.size());
    if (CompareUtils.compareListsGeneric(pagmThrift, pagmCql)) {
      LOGGER.info("MIGRATION_PAGM -- Les listes pagm sont identiques");
    } else {
      LOGGER.warn("MIGRATION_PAGM -- ATTENTION: Les listes pagm sont différentes ");
    }
  }

  /**
   * Récupération de la liste des keys
   * 
   * @return
   */
  private List<String> getKeys() {
    final List<String> keys = new ArrayList<>();
    String key = null;
    final Iterator<GenericDroitType> it = genericdao.findAllByCFName("DroitPagm", ccf.getKeyspace().getKeyspaceName());
    while (it.hasNext()) {
      // Extraction de la clé
      final Row row = (Row) it.next();
      key = StringSerializer.get().fromByteBuffer(row.getBytes("key"));
      if (key != null && !keys.contains(key)) {
        keys.add(key);
      }
    }
    return keys;
  }

}
