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
@SuppressWarnings( { "PMD.CyclomaticComplexity", "PMD.NPathComplexity" })
@Component
public class SaeMetadataSupport {

   private static final int MAX_FIND_RESULT = 5000;

   private final SaeMetadataDao saeMetadataDao;

   /**
    * constructeur de la classe support
    * 
    * @param saeMetadataDao
    *           la dao
    */
   @Autowired
   public SaeMetadataSupport(SaeMetadataDao saeMetadataDao) {
      this.saeMetadataDao = saeMetadataDao;
   }

   /**
    * Créé ou modifie la métadonné
    * 
    * @param metadata
    *           l'objet métadonné
    * @param clock
    *           le timestamp de l'opération
    */
   public final void create(MetadataReference metadata, long clock) {

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

      if (BooleanUtils.isFalse(metadata.isModifiable())
            || BooleanUtils.isTrue(metadata.isModifiable())) {
         saeMetadataDao
               .ecritModifiable(metadata.isModifiable(), updater, clock);
      } else {
         saeMetadataDao.ecritModifiable(false, updater, clock);
      }

      saeMetadataDao.getCfTmpl().update(updater);
   }

   /**
    * Retourne la liste des métadonnées
    * 
    * @return Liste des métadonnées
    */

   public final List<MetadataReference> findAll() {

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
   public final MetadataReference find(String codeLong) {
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

         } else if ("-1".equals(result.getString(SaeMetadataDao.META_LENGTH))) {
            meta.setLength(-1);

         } else {
            meta.setLength(result.getInteger(SaeMetadataDao.META_LENGTH));
         }

         meta.setShortCode(result.getString(SaeMetadataDao.META_SHORT_CODE));
         meta.setPattern(result.getString(SaeMetadataDao.META_PATTERN));

         Boolean requredArchiv = getBooleanValue(result,
               SaeMetadataDao.META_REQ_ARCH);
         meta.setRequiredForArchival(requredArchiv);

         Boolean requiredStor = getBooleanValue(result,
               SaeMetadataDao.META_REQ_STOR);
         meta.setRequiredForStorage(requiredStor);

         Boolean searchable = getBooleanValue(result,
               SaeMetadataDao.META_SEARCH);
         meta.setSearchable(searchable);

         meta.setType(result.getString(SaeMetadataDao.META_TYPE));

         Boolean metaArchivable = getBooleanValue(result,
               SaeMetadataDao.META_ARCH);
         meta.setArchivable(metaArchivable);

         Boolean metaConsultable = getBooleanValue(result,
               SaeMetadataDao.META_CONSUL);
         meta.setConsultable(metaConsultable);

         Boolean defaultConsultable = getBooleanValue(result,
               SaeMetadataDao.META_DEF_CONSUL);
         meta.setDefaultConsultable(defaultConsultable);

         Boolean hasDict = getBooleanValue(result, SaeMetadataDao.META_HAS_DICT);
         meta.setHasDictionary(hasDict);

         Boolean internal = getBooleanValue(result,
               SaeMetadataDao.META_INTERNAL);
         meta.setInternal(internal);

         Boolean indexed = getBooleanValue(result, SaeMetadataDao.META_INDEXED);
         meta.setIsIndexed(indexed);

         Boolean update = getBooleanValue(result, SaeMetadataDao.META_UPDATE);
         meta.setModifiable(update);

      }
      return meta;
   }

   private Boolean getBooleanValue(ColumnFamilyResult<String, String> result,
         String columnName) {
      Boolean value = Boolean.FALSE;
      if (result.getBoolean(columnName) != null) {
         value = result.getBoolean(columnName);
      }
      return value;
   }
}
