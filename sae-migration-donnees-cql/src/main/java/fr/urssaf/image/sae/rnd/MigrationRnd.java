/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.rnd;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.rnd.dao.cql.IRndDaoCql;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;

/**
 * (AC75095351) Classe de migration Rnd Thrift<-> Cql
 */
@Component
public class MigrationRnd {

  @Autowired
  private IRndDaoCql rndDaoCql;

  @Autowired
  private RndSupport rndSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationRnd.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationRnd - migrationFromThriftToCql- start ");

    final List<TypeDocument> typeDocuments = rndSupport.findAll();

    if (!typeDocuments.isEmpty()) {
      rndDaoCql.saveAll(typeDocuments);
    }

    LOGGER.info(" MigrationRnd - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationRnd - migrationFromCqlTothrift- start ");


    final Iterator<TypeDocument> typeDocuments = rndDaoCql.findAllWithMapper();

    while (typeDocuments.hasNext()) {
      final TypeDocument typeDocument = typeDocuments.next();
      rndSupport.ajouterRnd(typeDocument, new Date().getTime());
    }


    LOGGER.info(" MigrationRnd - migrationFromCqlTothrift- end ");
  }
}
