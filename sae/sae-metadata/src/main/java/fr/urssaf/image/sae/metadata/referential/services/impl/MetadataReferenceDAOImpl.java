package fr.urssaf.image.sae.metadata.referential.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.support.facade.SaeMetadataSupportFacade;
import fr.urssaf.image.sae.metadata.utils.Utils;

/**
 * Classe qui implémente l'interface
 * {@link fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO
 * MetadataReferenceService}
 * 
 */
@Service
@Qualifier("metadataReferenceDAO")
public class MetadataReferenceDAOImpl implements MetadataReferenceDAO {

  @Autowired
  private ApplicationContext context;

  private enum MetaType {
    ALL_METADATAS
  };

  private final LoadingCache<MetaType, Map<String, MetadataReference>> metadataReference;

  @Autowired
  private SaeMetadataSupportFacade metadataSupport;

  /**
   * @return Le context.
   */
  @Deprecated
  public final ApplicationContext getContext() {
    return context;
  }

  /**
   * @param context
   *           : le context
   */
  @Deprecated
  public final void setContext(final ApplicationContext context) {
    this.context = context;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getAllMetadataReferences() {
    synchronized (this) {
      return metadataReference.getUnchecked(MetaType.ALL_METADATAS);
    }
  }

  /**
   * {@inheritDoc}
    */
   @Override
   public Map<String, MetadataReference> getAllMetadataReferencesByShortCode() throws ReferentialException {
      final Map<String, MetadataReference> result = new HashMap<>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();

      for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(referentiel).entrySet()) {
         result.put(metaData.getValue().getShortCode(), metaData.getValue());
      }
      return result;
   }

   /**
    * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getConsultableMetadataReferences()
      throws ReferentialException {
    final Map<String, MetadataReference> csltMetaDatas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = getAllMetadataReferences();

    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isConsultable()) {
        csltMetaDatas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return csltMetaDatas;
  }

  /**
   * {@inheritDoc}
   * 
   */
  @Override
  public final Map<String, MetadataReference> getSearchableMetadataReferences()
      throws ReferentialException {
    final Map<String, MetadataReference> srchMetaDatas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isSearchable()) {
        srchMetaDatas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return srchMetaDatas;
  }

  /**
   * {@inheritDoc}
   * 
   */
  @Override
  public final Map<String, MetadataReference> getArchivableMetadataReferences()
      throws ReferentialException {
    final Map<String, MetadataReference> archMetaDatas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isArchivable()) {
        archMetaDatas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return archMetaDatas;
  }

  /**
   * {@inheritDoc}
   * 
   */
  @Override
  public final MetadataReference getByLongCode(final String longCode)
      throws ReferentialException {

    return getAllMetadataReferences().get(longCode);
  }

  /**
   * {@inheritDoc}
   * 
   */
  @Override
  @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
  public final MetadataReference getByShortCode(final String shortCode)
      throws ReferentialException {
    MetadataReference metadatafound = null;
    for (final Entry<String, MetadataReference> reference : Utils.nullSafeMap(
                                                                              getAllMetadataReferences()).entrySet()) {
      if (reference.getValue().getShortCode().equals(shortCode)) {
        metadatafound = reference.getValue();
      }
    }
    return metadatafound;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getRequiredForStorageMetadataReferences()
      throws ReferentialException {
    final Map<String, MetadataReference> reqMetaDatas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isRequiredForStorage()) {
        reqMetaDatas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return reqMetaDatas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getDefaultConsultableMetadataReferences()
      throws ReferentialException {
    final Map<String, MetadataReference> reqMetaDatas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isDefaultConsultable()) {
        reqMetaDatas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return reqMetaDatas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getRequiredForArchivalMetadataReferences()
      throws ReferentialException {
    final Map<String, MetadataReference> reqMetaDatas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isRequiredForArchival()) {
        reqMetaDatas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return reqMetaDatas;
  }

  /**
   * @param cacheDuration
   *           la durée du cache définie dans le fichier sae-config Construit
   *           un objet de type {@link MetadataReferenceDAOImpl}
   */
  @Autowired
  public MetadataReferenceDAOImpl(
                                  @Value("${sae.metadata.cache}") final int cacheDuration,
                                  @Value("${sae.metadata.initCacheOnStartup}") final boolean initCacheOnStartup,
                                  final SaeMetadataSupportFacade metadataSupport) {
    metadataReference = CacheBuilder.newBuilder()
        .refreshAfterWrite(cacheDuration, TimeUnit.MINUTES)
        .build(new CacheLoader<MetaType, Map<String, MetadataReference>>() {

          @Override
          public Map<String, MetadataReference> load(final MetaType identifiant) {
            if (identifiant.equals(MetaType.ALL_METADATAS)) {
              final List<MetadataReference> listeMeta = metadataSupport
                  .findAll();
              final Map<String, MetadataReference> mapMeta = new HashMap<>();
              for (final MetadataReference meta : listeMeta) {
                mapMeta.put(meta.getLongCode(), meta);
              }
              return mapMeta;
            } else {
              throw new MetadataRuntimeException(
                                                 "Le type de métadonnée n'est pas autorisé");
            }
          }

        });

    this.metadataSupport = metadataSupport;

    if (initCacheOnStartup) {
      // afin d'eviter de recoder ce qui est dans le load du cacheloader
      // on va juste recuperer le seul element en cache
      // (MetaType.ALL_METADATAS)
      metadataReference.getUnchecked(MetaType.ALL_METADATAS);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getModifiableMetadataReferences()
      throws ReferentialException {

    final Map<String, MetadataReference> archMetas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = metadataReference
        .getUnchecked(MetaType.ALL_METADATAS);
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isModifiable()) {
        archMetas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return archMetas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getLeftTrimableMetadataReference() {
    final Map<String, MetadataReference> archMetas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = metadataReference
        .getUnchecked(MetaType.ALL_METADATAS);
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isLeftTrimable()) {
        archMetas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return archMetas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getRightTrimableMetadataReference() {
    final Map<String, MetadataReference> archMetas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = metadataReference
        .getUnchecked(MetaType.ALL_METADATAS);
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().isRightTrimable()) {
        archMetas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return archMetas;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Map<String, MetadataReference> getTransferableMetadataReference()
      throws ReferentialException {
    final Map<String, MetadataReference> transMetas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = metadataReference
        .getUnchecked(MetaType.ALL_METADATAS);
    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {
      if (metaData.getValue().getTransferable()) {
        transMetas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return transMetas;
  }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Map<String, MetadataReference> getTransferableMetadataReferenceByShortCode()
         throws ReferentialException {
      final Map<String, MetadataReference> transMetas = new HashMap<>();
      final Map<String, MetadataReference> referentiel = metadataReference.getUnchecked(MetaType.ALL_METADATAS);
      for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(referentiel).entrySet()) {
         if (metaData.getValue().getTransferable()) {
            transMetas.put(metaData.getValue().getShortCode(), metaData.getValue());
         }
      }
      return transMetas;
   }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, MetadataReference> getAllMetadataReferencesPourVerifDroits()
      throws ReferentialException {
    final Map<String, MetadataReference> csltMetaDatas = new HashMap<>();
    final Map<String, MetadataReference> referentiel = getAllMetadataReferences();

    for (final Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
                                                                                 referentiel).entrySet()) {

      if (!"not".equals(metaData.getValue().getShortCode())
          && !"gel".equals(metaData.getValue().getShortCode())
          && !"dco".equals(metaData.getValue().getShortCode())) {
        csltMetaDatas.put(metaData.getKey(), metaData.getValue());
      }
    }
    return csltMetaDatas;
  }

}
