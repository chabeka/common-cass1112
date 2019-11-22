/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigration;
import fr.urssaf.image.sae.droit.dao.cql.IContratServiceDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration de ContratService
 */
@Component
public class MigrationContratService implements IMigration {

  @Autowired
  private IContratServiceDaoCql contratServiceDaoCql;

  @Autowired
  private ContratServiceSupport contratServiceSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationContratService.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public void migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromThriftToCql- start ");

    final List<ServiceContract> contratsServicesThrift = contratServiceSupport.findAll();

    if (!contratsServicesThrift.isEmpty()) {
      contratServiceDaoCql.saveAll(contratsServicesThrift);
    }
    final List<ServiceContract> contratsServicesCql = new ArrayList<>();
    final Iterator<ServiceContract> ontratsServicesIterator = contratServiceDaoCql.findAllWithMapper();
    ontratsServicesIterator.forEachRemaining(contratsServicesCql::add);
    compareContratService(contratsServicesThrift, contratsServicesCql);
    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromCqlTothrift- start ");

    final Iterator<ServiceContract> contratsServicesIterator = contratServiceDaoCql.findAllWithMapper();
    final List<ServiceContract> contratsServicesCql = new ArrayList<>();
    while (contratsServicesIterator.hasNext()) {
      final ServiceContract contratService = contratsServicesIterator.next();
      contratsServicesCql.add(contratService);
      contratServiceSupport.create(contratService, new Date().getTime());
    }
    final List<ServiceContract> contratServicesThrift = contratServiceSupport.findAll();
    compareContratService(contratServicesThrift, contratsServicesCql);
    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromCqlTothrift- end ");
  }

  /**
   * Comparaison des liste en taille et en contenu
   * 
   * @param contratServicesThrift
   * @param contratServicesCql
   */
  private void compareContratService(final List<ServiceContract> contratServicesThrift, final List<ServiceContract> contratServicesCql) {

    LOGGER.info("MIGRATION_CONTRAT_SERVICE -- SizeThriftDroitcontratServices=" + contratServicesThrift.size());
    LOGGER.info("MIGRATION_CONTRAT_SERVICE -- SizeCqlDroitcontratServices=" + contratServicesCql.size());
    if (CompareUtils.compareListsGeneric(contratServicesThrift, contratServicesCql)) {
      LOGGER.info("MIGRATION_CONTRAT_SERVICE -- Les listes ContratService sont identiques");
    } else {
      LOGGER.warn("MIGRATION_CONTRAT_SERVICE -- ATTENTION: Les listes ContratService sont différentes ");
    }
  }
}
