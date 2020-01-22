/**
 *   (AC75095351) 
 */
package fr.urssaf.image.sae.metadata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.javers.core.Javers;
import org.javers.core.diff.Diff;
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
  public Diff migrationFromThriftToCql(final Javers javers) {

    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- start ");

    final List<MetadataReference> metadatasThrift = metadataSupport.findAll();

    if (!metadatasThrift.isEmpty()) {
      metadataDaoCql.saveAll(metadatasThrift);
    }
    final List<MetadataReference> metadatasCql = new ArrayList<>();
    final Iterator<MetadataReference> metadatasIterator = metadataDaoCql.findAllWithMapper();
    metadatasIterator.forEachRemaining(metadatasCql::add);
    final Diff diff = compareMetadatas(metadatasThrift, metadatasCql, javers);
    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- end ");
    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- nbThrift={} ",metadatasThrift.size());
    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromThriftToCql- nbCql={} ", metadatasCql.size());
    return diff;
  }
  /**
   * Migration de la CF cql vers la CF Thrift
   */
  @Override
  public Diff migrationFromCqlTothrift(final Javers javers) {

    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- start ");


    final Iterator<MetadataReference> metadatasIterator = metadataDaoCql.findAllWithMapper();
    final List<MetadataReference> metadatasCql = new ArrayList<>();
    while (metadatasIterator.hasNext()) {
      final MetadataReference metadata = metadatasIterator.next();
      metadatasCql.add(metadata);
      metadataSupport.create(metadata, new Date().getTime());
    }
    final List<MetadataReference> metadatasThrift = metadataSupport.findAll();
    final Diff diff = compareMetadatas(metadatasThrift, metadatasCql, javers);
    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- end ");
    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- nbThrift={} ", metadatasThrift.size());
    MigrationMetadata.LOGGER.info(" MigrationMetadata - migrationFromCqlTothrift- nbCql={} ", metadatasCql.size());
    return diff;
  }

  /**
   * Logs: Comparaison des listes en taille et en contenu
   * 
   * @param metadatasThrift
   * @param metadatasCql
   */
  public Diff compareMetadatas(final List<MetadataReference> metadatasThrift, final List<MetadataReference> metadatasCql, final Javers javers) {
    CompareUtils.compareListsGeneric(metadatasThrift, metadatasCql);

    Collections.sort(metadatasThrift);
    Collections.sort(metadatasCql);

    final Diff diff = javers.compareCollections(metadatasThrift, metadatasCql, MetadataReference.class);
    return diff;
  }

}
