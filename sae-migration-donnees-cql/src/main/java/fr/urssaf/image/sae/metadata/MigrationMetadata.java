/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.metadata.referential.dao.cql.IMetadataDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;

/**
 * (AC75095351) Classe de migration Metadata Thrift<-> Cql
 */
@Component
public class MigrationMetadata {

  @Autowired
  private IMetadataDaoCql metadataDaoCql;

  @Autowired
  private SaeMetadataSupport metadataSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationMetadata.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  public void migrationFromThriftToCql() {

    LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- start ");

    final List<MetadataReference> metadatas = metadataSupport.findAll();

    if (!metadatas.isEmpty()) {
      metadataDaoCql.saveAll(metadatas);
    }

    LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- end ");
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  public void migrationFromCqlTothrift() {

    LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- start ");


    final Iterator<MetadataReference> metadatas = metadataDaoCql.findAllWithMapper();

    while (metadatas.hasNext()) {
      final MetadataReference metadata = metadatas.next();
      metadataSupport.create(metadata, new Date().getTime());
    }


    LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- end ");
  }
}
