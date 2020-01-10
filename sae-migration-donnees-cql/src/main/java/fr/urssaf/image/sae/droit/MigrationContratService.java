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

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.droit.dao.cql.IContratServiceDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;
import fr.urssaf.image.sae.droit.model.ServiceContractM;
import fr.urssaf.image.sae.utils.CompareUtils;

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
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromThriftToCql- start ");

    final List<ServiceContract> contratsServicesThrift = contratServiceSupport.findAll();

    if (!contratsServicesThrift.isEmpty()) {
      contratServiceDaoCql.saveAll(contratsServicesThrift);
    }
    final List<ServiceContract> contratsServicesCql = new ArrayList<>();
    final Iterator<ServiceContract> contratsServicesIterator = contratServiceDaoCql.findAllWithMapper();
    contratsServicesIterator.forEachRemaining(contratsServicesCql::add);
    final boolean result = compareContratService(contratsServicesThrift, contratsServicesCql);
    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromThriftToCql- end ");
    return result;
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromCqlTothrift- start ");

    final Iterator<ServiceContract> contratsServicesIterator = contratServiceDaoCql.findAllWithMapper();
    final List<ServiceContract> contratsServicesCql = new ArrayList<>();
    while (contratsServicesIterator.hasNext()) {
      final ServiceContract contratService = contratsServicesIterator.next();
      contratsServicesCql.add(contratService);
      contratServiceSupport.create(contratService, new Date().getTime());
    }
    final List<ServiceContract> contratServicesThrift = contratServiceSupport.findAll();
    final boolean result = compareContratService(contratServicesThrift, contratsServicesCql);
    LOGGER.info(" MIGRATION_CONTRAT_SERVICE - migrationFromCqlTothrift- end ");
    return result;
  }

  /**
   * Logs: Comparaison des liste en taille et en contenu
   * 
   * @param contratServicesThrift
   * @param contratServicesCql
   */
  public boolean compareContratService(final List<ServiceContract> contratServicesThrift, final List<ServiceContract> contratServicesCql) {

    final List<ServiceContractM> contratServicesThriftM = convertList(contratServicesThrift);
    final List<ServiceContractM> contratServicesCqlM = convertList(contratServicesCql);
    // logCompare(result, contratServicesThrift, contratServicesCql);

    return CompareUtils.compareListsGeneric(contratServicesThriftM, contratServicesCqlM);
  }

  /**
   * @param contratServicesThrift
   * @param contratServicesCql
   * @param result
   */
  private void logCompare(final boolean compare, final List<ServiceContract> contratServicesThrift, final List<ServiceContract> contratServicesCql) {
    if (compare) {
      LOGGER.info("MIGRATION_CONTRAT_SERVICE -- Les listes ContratService sont identiques");
    } else {
      LOGGER.info("MIGRATION_CONTRAT_SERVICE -- NbThrift=" + contratServicesThrift.size());
      LOGGER.info("MIGRATION_CONTRAT_SERVICE -- NbCql=" + contratServicesCql.size());
      LOGGER.warn("MIGRATION_CONTRAT_SERVICE -- ATTENTION: Les listes ContratService sont différentes ");
    }
  }

  private List<ServiceContractM> convertList(final List<ServiceContract> serviceContracts) {

    final List<ServiceContractM> serviceContractsM = new ArrayList<>();
    for (final ServiceContract serviceContract : serviceContracts) {
      final ServiceContractM serviceContractM = new ServiceContractM();
      serviceContractM.setCodeClient(serviceContract.getCodeClient());
      serviceContractM.setDescription(serviceContract.getDescription());
      serviceContractM.setLibelle(serviceContract.getLibelle());
      serviceContractM.setViDuree(serviceContract.getViDuree());
      serviceContractM.setViDuree(serviceContract.getViDuree());
      serviceContractM.setViDuree(serviceContract.getViDuree());
      serviceContractM.setViDuree(serviceContract.getViDuree());
      serviceContractsM.add(serviceContractM);
    }
    return serviceContractsM;
  }
}
