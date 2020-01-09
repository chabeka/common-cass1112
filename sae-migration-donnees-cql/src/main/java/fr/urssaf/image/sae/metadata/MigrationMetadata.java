/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.IMigrationR;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IMetadataDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.utils.CompareUtils;

/**
 * (AC75095351) Classe de migration Metadata Thrift<-> Cql
 */
@Component
public class MigrationMetadata implements IMigrationR {

  @Autowired
  private IMetadataDaoCql metadataDaoCql;

  @Autowired
  private SaeMetadataSupport metadataSupport;

  private static final Logger LOGGER = LoggerFactory.getLogger(MigrationMetadata.class);

  /**
   * Migration de la CF Thrift vers la CF cql
   */
  @Override
  public boolean migrationFromThriftToCql() {

    LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- start ");

    final List<MetadataReference> metadatasThrift = metadataSupport.findAll();

    if (!metadatasThrift.isEmpty()) {
      metadataDaoCql.saveAll(metadatasThrift);
    }
    final List<MetadataReference> metadatasCql = new ArrayList<>();
    final Iterator<MetadataReference> metadatasIterator = metadataDaoCql.findAllWithMapper();
    metadatasIterator.forEachRemaining(metadatasCql::add);
    LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- end ");
    return compareMetadatas(metadatasThrift, metadatasCql);
  }

  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public boolean migrationFromCqlTothrift() {

    LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- start ");


    final Iterator<MetadataReference> metadatasIterator = metadataDaoCql.findAllWithMapper();
    final List<MetadataReference> metadatasCql = new ArrayList<>();
    while (metadatasIterator.hasNext()) {
      final MetadataReference metadata = metadatasIterator.next();
      metadatasCql.add(metadata);
      metadataSupport.create(metadata, new Date().getTime());
    }
    final List<MetadataReference> metadatasThrift = metadataSupport.findAll();
    LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- end ");
    return compareMetadatas(metadatasThrift, metadatasCql);
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param metadatasThrift
   * @param metadatasCql
   */
  private boolean compareMetadatas(final List<MetadataReference> metadatasThrift, final List<MetadataReference> metadatasCql) {
    final boolean result = CompareUtils.compareListsGeneric(metadatasThrift, metadatasCql);

    if (result) {
      LOGGER.info("MIGRATION_METADATA -- Les listes metadata sont identiques");
    } else {
      LOGGER.info("MIGRATION_METADATA -- NbThrift=" + metadatasThrift.size());
      LOGGER.info("MIGRATION_METADATA -- NbCql=" + metadatasCql.size());
      LOGGER.warn("MIGRATION_METADATA -- ATTENTION: Les listes metadata sont différentes ");
    }
    return result;
  }

}
