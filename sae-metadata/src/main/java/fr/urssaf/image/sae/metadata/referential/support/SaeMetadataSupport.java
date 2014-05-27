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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.cassandra.helper.HectorIterator;
import fr.urssaf.image.commons.cassandra.helper.QueryResultConverter;
import fr.urssaf.image.sae.metadata.exceptions.MetadataRuntimeException;
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
    * Créé la métadonnée
    * 
    * @param metadata
    *           l'objet métadonnée
    * @param clock
    *           le timestamp de l'opération
    */
   public final void create(MetadataReference metadata, long clock) {

      checkCodeCourtInexistant(metadata.getShortCode());

      createMetadata(metadata, clock);
   }

   /**
    * Modifie la métadonnée
    * 
    * @param metadata
    *           l'objet métadonnée
    * @param clock
    *           le timestamp de l'opération
    */
   public final void modify(MetadataReference metadata, long clock) {

      checkCodeCourtExistant(metadata.getShortCode());

      createMetadata(metadata, clock);
   }

   private void createMetadata(MetadataReference metadata, long clock) {
      ColumnFamilyUpdater<String, String> updater = saeMetadataDao.getCfTmpl()
            .createUpdater(metadata.getLongCode());

      saeMetadataDao.ecritShortCode(metadata.getShortCode(), updater, clock);
      saeMetadataDao.ecritType(metadata.getType(), updater, clock);

      Boolean requiredArchiv = getBooleanValue(metadata.isRequiredForArchival());
      saeMetadataDao.ecritRequiredArchival(requiredArchiv, updater, clock);

      Boolean requiredStor = getBooleanValue(metadata.isRequiredForStorage());
      saeMetadataDao.ecritRequiredStorage(requiredStor, updater, clock);

      Boolean leftTrim = getBooleanValue(metadata.isLeftTrimable());
      saeMetadataDao.ecritLeftTrim(leftTrim, updater, clock);

      Boolean rightTrim = getBooleanValue(metadata.isRightTrimable());
      saeMetadataDao.ecritRightTrim(rightTrim, updater, clock);

      int length = -1;
      if (metadata.getLength() >= 0) {
         length = metadata.getLength();
      }
      saeMetadataDao.ecritLength(length, updater, clock);

      saeMetadataDao.ecritPattern(metadata.getPattern(), updater, clock);

      Boolean consultable = getBooleanValue(metadata.isConsultable());
      saeMetadataDao.ecritConsultable(consultable, updater, clock);

      Boolean defaultConsultable = getBooleanValue(metadata
            .isDefaultConsultable());
      saeMetadataDao
            .ecritDefaultConsultable(defaultConsultable, updater, clock);

      Boolean searchable = getBooleanValue(metadata.isSearchable());
      saeMetadataDao.ecritSearchable(searchable, updater, clock);

      Boolean internal = getBooleanValue(metadata.isInternal());
      saeMetadataDao.ecritInternal(internal, updater, clock);

      Boolean archivable = getBooleanValue(metadata.isArchivable());
      saeMetadataDao.ecritArchivable(archivable, updater, clock);

      saeMetadataDao.ecritLabel(metadata.getLabel(), updater, clock);
      saeMetadataDao
            .ecritDescription(metadata.getDescription(), updater, clock);

      Boolean hasDict = getBooleanValue(metadata.getHasDictionary());
      saeMetadataDao.ecritHasDictionary(hasDict, updater, clock);

      saeMetadataDao.ecritDictionaryName(metadata.getDictionaryName(), updater,
            clock);

      Boolean indexed = getBooleanValue(metadata.getIsIndexed());
      saeMetadataDao.ecritIndexed(indexed, updater, clock);

      Boolean modifiable = getBooleanValue(metadata.isModifiable());
      saeMetadataDao.ecritModifiable(modifiable, updater, clock);

      Boolean clientAvailable = getBooleanValue(metadata.isClientAvailable());
      saeMetadataDao.ecritMisADisposition(clientAvailable, updater, clock);

      saeMetadataDao.getCfTmpl().update(updater);
   }

   /**
    * Retourne la liste des métadonnées
    * 
    * @return Liste des métadonnées
    */
   public final List<MetadataReference> findAll() {

      ColumnFamilyResultWrapper<String, String> result = getAllMetadatas();

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
    * Récupère toutes les métadonnées recherchables
    * 
    * @return la liste des métadonnées recherchables
    */
   public final List<MetadataReference> findMetadatasRecherchables() {
      ColumnFamilyResultWrapper<String, String> result = getAllMetadatas();

      // On itère sur le résultat et on ne récupère que les métadonnées
      // recherchable
      HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
            result);
      List<MetadataReference> list = new ArrayList<MetadataReference>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         MetadataReference meta = getMetadataFromResult(row);
         if (meta.isSearchable()) {
            list.add(getMetadataFromResult(row));
         }
      }
      return list;
   }

   /**
    * Récupère la liste des métadonnées consultables
    * 
    * @return la liste des métadonnées consultables
    */
   public final List<MetadataReference> findMetadatasConsultables() {
      ColumnFamilyResultWrapper<String, String> result = getAllMetadatas();

      // On itère sur le résultat et on ne récupère que les métadonnées
      // recherchable
      HectorIterator<String, String> resultIterator = new HectorIterator<String, String>(
            result);
      List<MetadataReference> list = new ArrayList<MetadataReference>();
      for (ColumnFamilyResult<String, String> row : resultIterator) {
         MetadataReference meta = getMetadataFromResult(row);
         if (meta.isConsultable()) {
            list.add(getMetadataFromResult(row));
         }
      }
      return list;
   }

   private ColumnFamilyResultWrapper<String, String> getAllMetadatas() {
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
      return result;
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

         Boolean leftTrim = getBooleanValue(result,
               SaeMetadataDao.META_LEFT_TRIM);
         meta.setLeftTrimable(leftTrim);

         Boolean rightTrim = getBooleanValue(result,
               SaeMetadataDao.META_RIGHT_TRIM);
         meta.setRightTrimable(rightTrim);

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

         Boolean dispo = getBooleanValue(result, SaeMetadataDao.META_DISPO);
         meta.setClientAvailable(dispo);

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

   private Boolean getBooleanValue(Boolean srcValue) {
      Boolean value = Boolean.FALSE;

      if (srcValue != null) {
         value = srcValue;
      }

      return value;
   }

   private void checkCodeCourtInexistant(String codeCourt) {
      List<MetadataReference> metadatas = findAll();
      boolean found = false;
      int index = 0;

      while (!found && index < metadatas.size()) {
         if (codeCourt.equalsIgnoreCase(metadatas.get(index).getShortCode())) {
            found = true;
         }

         index++;
      }

      if (found) {
         throw new MetadataRuntimeException("Code court déjà existant");
      }
   }

   private void checkCodeCourtExistant(String codeCourt) {
      List<MetadataReference> metadatas = findAll();
      boolean found = false;
      int index = 0;

      while (!found && index < metadatas.size()) {
         if (codeCourt.equalsIgnoreCase(metadatas.get(index).getShortCode())) {
            found = true;
         }

         index++;
      }

      if (!found) {
         throw new MetadataRuntimeException("Code court inexistant");
      }
   }
}
