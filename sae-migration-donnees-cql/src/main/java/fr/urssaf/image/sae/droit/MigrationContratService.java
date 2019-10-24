/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.droit;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.droit.dao.cql.IContratServiceDaoCql;
import fr.urssaf.image.sae.droit.dao.model.ServiceContract;
import fr.urssaf.image.sae.droit.dao.support.ContratServiceSupport;

/**
 * (AC75095351) Classe de migration de ContratService
 */
@Component
public class MigrationContratService {

  @Autowired
  private IContratServiceDaoCql contratServiceDaoCql;

  @Autowired
  private ContratServiceSupport contratServiceSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationContratService.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationContratService - migrationFromThriftToCql- start ");

    final List<ServiceContract> ContratServices = contratServiceSupport.findAll();

    if (!ContratServices.isEmpty()) {
      contratServiceDaoCql.saveAll(ContratServices);
    }

    LOGGER.info(" MigrationContratService - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationContratService - migrationFromCqlTothrift- start ");

    final Iterator<ServiceContract> ContratServices = contratServiceDaoCql.findAllWithMapper();
    while (ContratServices.hasNext()) {
      final ServiceContract ContratService = ContratServices.next();
      contratServiceSupport.create(ContratService, new Date().getTime());
    }

    LOGGER.info(" MigrationContratService - migrationFromCqlTothrift- end ");
  }
}
