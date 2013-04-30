package fr.urssaf.image.sae.metadata.referential.support;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyResultWrapper;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.metadata.referential.dao.SaeMetadataDao;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;

/**
 * Classe permettant de réaliser les actions de manipulation des DAO pour la
 * famille de colonne "Metadata"
 */ 
@SuppressWarnings({"PMD.CyclomaticComplexity", "PMD.NPathComplexity"})
@Component
public class SaeMetadataSupport {

   private static final int MAX_FIND_RESULT = 5000;

   private final SaeMetadataDao saeMetadataDao;

   /**
    * constructeur de la classe support
    * @param saeMetadataDao la dao
    */
   @Autowired
   public SaeMetadataSupport(SaeMetadataDao saeMetadataDao) {
      this.saeMetadataDao = saeMetadataDao;
   }

   /**
    * Créé ou modifie la métadonné
    * 
    * @param metadata l'objet métadonné
    * @param clock le timestamp de l'opération
    */ 
   public void create(MetadataReference metadata, long clock) {

      ColumnFamilyUpdater<String, String> updater = saeMetadataDao.getCfTmpl()
            .createUpdater(metadata.getLongCode());

      saeMetadataDao.ecritShortCode(metadata.getShortCode(), updater, clock);
      saeMetadataDao.ecritType(metadata.getType(), updater, clock);
      if (BooleanUtils.isFalse(metadata.isRequiredForArchival())
            || BooleanUtils.isTrue(metadata.isRequiredForArchival())) {
         saeMetadataDao.ecritRequiredArchival(metadata.isRequiredForArchival(),
               updater, clock);
      } else {
         saeMetadataDao.ecritRequiredArchival(false, updater, clock);
      }
      if (BooleanUtils.isFalse(metadata.isRequiredForStorage())
            || BooleanUtils.isTrue(metadata.isRequiredForStorage())) {
         saeMetadataDao.ecritRequiredStorage(metadata.isRequiredForStorage(),
               updater, clock);
      } else {
         saeMetadataDao.ecritRequiredStorage(false, updater, clock);
      }
      if (metadata.getLength() >= 0) {
         saeMetadataDao.ecritLength(metadata.getLength(), updater, clock);
      } else {
         saeMetadataDao.ecritLength(-1, updater, clock);
      }
      saeMetadataDao.ecritPattern(metadata.getPattern(), updater, clock);

      if (BooleanUtils.isFalse(metadata.isConsultable())
            || BooleanUtils.isTrue(metadata.isConsultable())) {
         saeMetadataDao.ecritConsultable(metadata.isConsultable(), updater,
               clock);
      } else {
         saeMetadataDao.ecritConsultable(false, updater, clock);
      }

      if (BooleanUtils.isFalse(metadata.isDefaultConsultable())
            || BooleanUtils.isTrue(metadata.isDefaultConsultable())) {
         saeMetadataDao.ecritDefaultConsultable(
               metadata.isDefaultConsultable(), updater, clock);
      } else {
         saeMetadataDao.ecritDefaultConsultable(false, updater, clock);
      }

      if (BooleanUtils.isFalse(metadata.isSearchable())
            || BooleanUtils.isTrue(metadata.isSearchable())) {
         saeMetadataDao
               .ecritSearchable(metadata.isSearchable(), updater, clock);
      } else {
         saeMetadataDao.ecritSearchable(false, updater, clock);
      }

      if (BooleanUtils.isFalse(metadata.isInternal())
            || BooleanUtils.isTrue(metadata.isInternal())) {
         saeMetadataDao.ecritInternal(metadata.isInternal(), updater, clock);
      } else {
         saeMetadataDao.ecritInternal(false, updater, clock);
      }

      if (BooleanUtils.isFalse(metadata.isArchivable())
            || BooleanUtils.isTrue(metadata.isArchivable())) {
         saeMetadataDao
               .ecritArchivable(metadata.isArchivable(), updater, clock);
      } else {
         saeMetadataDao.ecritArchivable(false, updater, clock);
      }

      saeMetadataDao.ecritLabel(metadata.getLabel(), updater, clock);
      saeMetadataDao
            .ecritDescription(metadata.getDescription(), updater, clock);

      if (BooleanUtils.isFalse(metadata.getHasDictionary())
            || BooleanUtils.isTrue(metadata.getHasDictionary())) {
         saeMetadataDao.ecritHasDictionary(metadata.getHasDictionary(),
               updater, clock);
      } else {
         saeMetadataDao.ecritHasDictionary(false, updater, clock);
      }

      saeMetadataDao.ecritDictionaryName(metadata.getDictionaryName(), updater,
            clock);

      if (BooleanUtils.isFalse(metadata.getIsIndexed())
            || BooleanUtils.isTrue(metadata.getIsIndexed())) {
         saeMetadataDao.ecritIndexed(metadata.getIsIndexed(), updater, clock);
      } else {
         saeMetadataDao.ecritIndexed(false, updater, clock);
      }

      saeMetadataDao.getCfTmpl().update(updater);
   }

   /**
    * Retourne la liste des métadonnées
    * 
    * @return Liste des métadonnées
    */

   public List<MetadataReference> findAll() {

      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(saeMetadataDao.getKeyspace(),
                  StringSerializer.get(), StringSerializer.get(),
                  bytesSerializer);
      rangeSlicesQuery.setColumnFamily(saeMetadataDao.getColumnFamilyName());
      rangeSlicesQuery.setRange(StringUtils.EMPTY, StringUtils.EMPTY, false,
            MAX_FIND_RESULT);
      QueryResult<OrderedRows<String, String, byte[]>> queryResult = rangeSlicesQuery
            .execute();

      // On convertit le résultat en ColumnFamilyResultWrapper pour faciliter
      // son utilisation
      QueryResultConverter<String, String, byte[]> converter = new QueryResultConverter<String, String, byte[]>();
      ColumnFamilyResultWrapper<String, String> result = converter
            .getColumnFamilyResultWrapper(queryResult, StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);

      // On itère sur le résultat
      HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
            result);
      List<MetadataReference> list = new ArrayList<MetadataReference>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {

         list.add(getMetadataFromResult(row));

      }
      return list;

   }

   /**
    * Méthode permettant de récupérer les informations pour une métadonnée
    * spécifique
    * 
    * @param codeLong
    *           Code long de la métadonné à rechercher
    * @return {@link MetadataReference}
    */
   public MetadataReference find(String codeLong) {
      ColumnFamilyResult<String, String> result = saeMetadataDao.getCfTmpl()
            .queryColumns(codeLong);

      MetadataReference meta = getMetadataFromResult(result);

      return meta;
   }

   /**
    * Construction d'un objet {@link MetadataReference} à partir du résultat de
    * la requête
    * 
    * @param result
    *           {@link ColumnFamilyResult}
    * @return {@link MetadataReference}
    */
   private MetadataReference getMetadataFromResult(
         ColumnFamilyResult<String, String> result) {
      MetadataReference meta = null;
      if (result != null && result.hasResults()) {
         meta = new MetadataReference();

         meta.setLongCode(result.getKey());

         meta.setDescription(result.getString(SaeMetadataDao.META_DESCR));
         meta
               .setDictionaryName(result
                     .getString(SaeMetadataDao.META_DICT_NAME));
         meta.setLabel(result.getString(SaeMetadataDao.META_LABEL));

         // NB: -1 est la valeur signifiant "non renseigné"
         if (StringUtils.isEmpty(result.getString(SaeMetadataDao.META_LENGTH))) {
            meta.setLength(-1);
         } else {
            if ("-1".equals(result.getString(SaeMetadataDao.META_LENGTH))) {
               meta.setLength(-1);
            } else {
               meta.setLength(result.getInteger(SaeMetadataDao.META_LENGTH));
            }
            
         }

         meta.setShortCode(result.getString(SaeMetadataDao.META_SHORT_CODE));
         meta.setPattern(result.getString(SaeMetadataDao.META_PATTERN));
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_REQ_ARCH).toString())) {           
            meta.setRequiredForArchival(Boolean.FALSE);
         } else {
            meta.setRequiredForArchival(result
                  .getBoolean(SaeMetadataDao.META_REQ_ARCH));
         }
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_REQ_STOR).toString())) {
            meta.setRequiredForStorage(Boolean.FALSE);
         } else {
            meta.setRequiredForStorage(result
                  .getBoolean(SaeMetadataDao.META_REQ_STOR));            
         }
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_SEARCH).toString())) {
            meta.setSearchable(Boolean.FALSE);
         } else {
            meta.setSearchable(result
                  .getBoolean(SaeMetadataDao.META_SEARCH));
         }
         meta.setType(result.getString(SaeMetadataDao.META_TYPE));
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_ARCH).toString())) {
            meta.setArchivable(Boolean.FALSE);
         } else {
            meta.setArchivable(result
                  .getBoolean(SaeMetadataDao.META_ARCH));            
         }
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_CONSUL).toString())) {
            meta.setConsultable(Boolean.FALSE);
         } else {
            meta.setConsultable(result
                  .getBoolean(SaeMetadataDao.META_CONSUL));

         }
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_DEF_CONSUL).toString())) {
            meta.setConsultable(Boolean.FALSE);
         } else {
            meta.setDefaultConsultable(result
                  .getBoolean(SaeMetadataDao.META_DEF_CONSUL));            
         }
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_HAS_DICT).toString())) {
            meta.setHasDictionary(Boolean.FALSE);
         } else {
            meta.setHasDictionary(result
                  .getBoolean(SaeMetadataDao.META_HAS_DICT));
         }
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_INTERNAL).toString())) {
            meta.setInternal(Boolean.FALSE);
         } else {
            meta.setInternal(result
                  .getBoolean(SaeMetadataDao.META_INTERNAL));
         }
         if (StringUtils.isEmpty(result.getBoolean(SaeMetadataDao.META_INDEXED).toString())) {
            meta.setIsIndexed(Boolean.FALSE);
         } else {
            meta.setIsIndexed(result
                  .getBoolean(SaeMetadataDao.META_INDEXED));
            
         }

      }
      return meta;
   }
}
