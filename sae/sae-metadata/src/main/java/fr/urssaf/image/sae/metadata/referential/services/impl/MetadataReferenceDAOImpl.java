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

import fr.urssaf.image.sae.metadata.exceptions.DictionaryNotFoundException;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.metadata.referential.support.SaeMetadataSupport;
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

   private LoadingCache<MetaType, Map<String, MetadataReference>> metadataReference;

   @Autowired
   private SaeMetadataSupport metadataSupport;

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
   @Deprecated
   public final Map<String, MetadataReference> getAllMetadataReferences() {

      synchronized (this) {

         return metadataReference.getUnchecked(MetaType.ALL_METADATAS);
      }

   }

   /**
    * {@inheritDoc}
    * 
    */
   public final Map<String, MetadataReference> getConsultableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> csltMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();

      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
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
   public final Map<String, MetadataReference> getSearchableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> srchMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
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
   public final Map<String, MetadataReference> getArchivableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> archMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
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
   public final MetadataReference getByLongCode(final String longCode)
         throws ReferentialException {
      
      return getAllMetadataReferences().get(longCode);
   }

   /**
    * {@inheritDoc}
    * 
    */
   @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
   public final MetadataReference getByShortCode(final String shortCode)
         throws ReferentialException {
      MetadataReference metadatafound = null;
      for (Entry<String, MetadataReference> reference : Utils.nullSafeMap(
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
   public final Map<String, MetadataReference> getRequiredForStorageMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> reqMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
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
   public final Map<String, MetadataReference> getDefaultConsultableMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> reqMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
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
   public final Map<String, MetadataReference> getRequiredForArchivalMetadataReferences()
         throws ReferentialException {
      final Map<String, MetadataReference> reqMetaDatas = new HashMap<String, MetadataReference>();
      final Map<String, MetadataReference> referentiel = getAllMetadataReferences();
      for (Map.Entry<String, MetadataReference> metaData : Utils.nullSafeMap(
            referentiel).entrySet()) {
         if (metaData.getValue().isRequiredForArchival()) {
            reqMetaDatas.put(metaData.getKey(), metaData.getValue());
         }
      }
      return reqMetaDatas;
   }


   /**
    * @param cacheDuration la durée du cache définie dans le fichier sae-config 
    * Construit un objet de type {@link MetadataReferenceDAOImpl}
    */
   @Autowired
   public MetadataReferenceDAOImpl(
         @Value("${sae.metadata.cache}") int cacheDuration) {
      metadataReference = CacheBuilder.newBuilder().refreshAfterWrite(
            cacheDuration, TimeUnit.MINUTES).build(
            new CacheLoader<MetaType, Map<String, MetadataReference>>() {

               @Override
               public Map<String, MetadataReference> load(MetaType identifiant)
                     throws DictionaryNotFoundException {
                  if (identifiant.equals(MetaType.ALL_METADATAS)) {
                     List<MetadataReference> listeMeta = metadataSupport
                           .findAll();
                     Map<String, MetadataReference> mapMeta = new HashMap<String, MetadataReference>();
                     for (MetadataReference meta : listeMeta) {
                        mapMeta.put(meta.getLongCode(), meta);
                     }
                     return mapMeta;
                  } else {
                     throw new MetadataRuntimeException(
                           "Le type de métadonnée n'est pas autorisé");
                  }
               }

            });

   }

}
