/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.diff.ListCompareAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IContratServiceDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;

/**
 * (AC75095351) Classe de migration de ContratService
 */
@Component
public class MigrationContratService implements IMigrationR {

  @Autowired
  private IContratServiceDaoCql contratServiceDaoCql;

  @Autowired
  private ContratServiceSupport contratServiceSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationContratService.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public Diff migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromThriftToCql- start ");

    final List<ServiceContract> contratsServicesThrift = contratServiceSupport.findAll();

    if (!contratsServicesThrift.isEmpty()) {
      contratServiceDaoCql.saveAll(contratsServicesThrift);
    }
    final List<ServiceContract> contratsServicesCql = new ArrayList<>();
    final Iterator<ServiceContract> contratsServicesIterator = contratServiceDaoCql.findAllWithMapper();
    contratsServicesIterator.forEachRemaining(contratsServicesCql::add);
    final Diff diff = compareContratService(contratsServicesThrift, contratsServicesCql);
    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromThriftToCql- end ");
    return diff;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromCqlTothrift- start ");

    final Iterator<ServiceContract> contratsServicesIterator = contratServiceDaoCql.findAllWithMapper();
    final List<ServiceContract> contratsServicesCql = new ArrayList<>();
    while (contratsServicesIterator.hasNext()) {
      final ServiceContract contratService = contratsServicesIterator.next();
      contratsServicesCql.add(contratService);
      contratServiceSupport.create(contratService, new Date().getTime());
    }
    final List<ServiceContract> contratServicesThrift = contratServiceSupport.findAll();
    final Diff diff = compareContratService(contratServicesThrift, contratsServicesCql);
    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromCqlTothrift- end ");
    return diff;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param contratServicesThrift
   * @param contratServicesCql
   */
  public Diff compareContratService(final List<ServiceContract> contratServicesThrift, final List<ServiceContract> contratServicesCql) {

    Collections.sort(contratServicesThrift);
    Collections.sort(contratServicesCql);
    final Javers javers = JaversBuilder
        .javers()
        .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
        .build();
    final Diff diff = javers.compareCollections(contratServicesThrift, contratServicesCql, ServiceContract.class);
    return diff;
  }

  /* *//**
   * @param contratServicesThrift
   * @param contratServicesCql
   * @param result
   *//*
   * private void logCompare(final boolean compare, final List<ServiceContract> contratServicesThrift, final List<ServiceContract> contratServicesCql) {
   * if (compare) {
   * LOGGER.info("MIGRATION_CONTRAT_SERVICE -- Les listes ContratService sont identiques");
   * } else {
   * LOGGER.info("MIGRATION_CONTRAT_SERVICE -- NbThrift=" + contratServicesThrift.size());
   * LOGGER.info("MIGRATION_CONTRAT_SERVICE -- NbCql=" + contratServicesCql.size());
   * LOGGER.warn("MIGRATION_CONTRAT_SERVICE -- ATTENTION: Les listes ContratService sont différentes ");
   * }
   * }
   */

}
