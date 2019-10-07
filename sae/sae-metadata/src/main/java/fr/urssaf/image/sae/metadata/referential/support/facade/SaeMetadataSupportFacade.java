package fr.urssaf.image.sae.metadata.referential.support.facade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.exception.ModeGestionAPIUnkownException;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI;
import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.metadata.referential.dao.cql.IMetadataDaoCql;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
import fr.urssaf.image.sae.metadata.referential.support.cql.SaeMetadataCqlSupport;
import fr.urssaf.image.sae.metadata.utils.Constantes;

/**
 * classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Metadata"
 */

@Component
public class SaeMetadataSupportFacade {

  @Autowired
  IMetadataDaoCql metadataDaoCql;

  private final String cfName = Constantes.CF_METADATA;

  private final SaeMetadataSupport metadataSupport;

  private final SaeMetadataCqlSupport metadataCqlSupport;

  private final JobClockSupport clockSupport;

  /**
   * constructeur
   * 
   * @param auDao
   *          DAO associée metadata
   */
  @Autowired
  public SaeMetadataSupportFacade(final SaeMetadataSupport metadataSupport,
                                  final SaeMetadataCqlSupport metadataCqlSupport,
                                  final JobClockSupport clockSupport) {
    this.metadataSupport = metadataSupport;
    this.metadataCqlSupport = metadataCqlSupport;
    this.clockSupport = clockSupport;
  }


  public final void create(final MetadataReference metadata) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      metadataSupport.create(metadata, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      metadataCqlSupport.create(metadata);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      metadataCqlSupport.create(metadata);
      metadataSupport.create(metadata, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("MetadataSupportFacade/Create/Mode API inconnu");

    }
  }

  public final void modify(final MetadataReference metadata) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      metadataSupport.modify(metadata, clockSupport.currentCLock());
      break;

    case MODE_API.DATASTAX:
      metadataCqlSupport.modify(metadata);
      break;

    case MODE_API.DUAL_MODE_READ_THRIFT:
    case MODE_API.DUAL_MODE_READ_CQL:
      metadataCqlSupport.modify(metadata);
      metadataSupport.modify(metadata, clockSupport.currentCLock());
      break;

    default:
      throw new ModeGestionAPIUnkownException("MetadataSupportFacade/Create/Mode API inconnu");

    }
  }

  public final MetadataReference find(final String identifiant) {

    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return metadataSupport.find(identifiant);

    case MODE_API.DATASTAX:
      return metadataCqlSupport.find(identifiant);

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return metadataSupport.find(identifiant);

    case MODE_API.DUAL_MODE_READ_CQL:
      return metadataCqlSupport.find(identifiant);

    default:
      throw new ModeGestionAPIUnkownException("MetadataSupportFacade/Find/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public List<MetadataReference> findAll() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return metadataSupport.findAll();

    case MODE_API.DATASTAX:
      return metadataCqlSupport.findAll();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return metadataSupport.findAll();

    case MODE_API.DUAL_MODE_READ_CQL:
      return metadataCqlSupport.findAll();

    default:
      throw new ModeGestionAPIUnkownException("MetadataSupportFacade/findAll/Mode API inconnu");
    }
  }

  /**
   * {@inheritDoc}
   */

  public List<MetadataReference> findMetadatasRecherchables() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return metadataSupport.findMetadatasRecherchables();

    case MODE_API.DATASTAX:
      return metadataCqlSupport.findMetadatasRecherchables();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return metadataSupport.findMetadatasRecherchables();

    case MODE_API.DUAL_MODE_READ_CQL:
      return metadataCqlSupport.findMetadatasRecherchables();

    default:
      throw new ModeGestionAPIUnkownException("MetadataSupportFacade/findMetadatasRecherchables/Mode API inconnu");
    }
  }

  public List<MetadataReference> findMetadatasConsultables() {
    switch (ModeGestionAPI.getModeApiCf(cfName)) {

    case MODE_API.HECTOR:
      return metadataSupport.findMetadatasConsultables();

    case MODE_API.DATASTAX:
      return metadataCqlSupport.findMetadatasConsultables();

    case MODE_API.DUAL_MODE_READ_THRIFT:
      return metadataSupport.findMetadatasConsultables();

    case MODE_API.DUAL_MODE_READ_CQL:
      return metadataCqlSupport.findMetadatasConsultables();

    default:
      throw new ModeGestionAPIUnkownException("MetadataSupportFacade/findMetadatasConsultables/Mode API inconnu");
    }
  }

}
