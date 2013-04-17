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
@Component
public class SaeMetadataSupport {

   private int MAX_FIND_RESULT=5000;
   private SaeMetadataDao saeMetadataDao;
   
   @Autowired
   public SaeMetadataSupport(SaeMetadataDao saeMetadataDao){
      this.saeMetadataDao = saeMetadataDao;
   }
   
   /**
    * Créé ou modifie la métadonné
    * @param metadata
    * @param clock
    */
   public void create(MetadataReference metadata, long clock){
      ColumnFamilyUpdater<String, String> updater = saeMetadataDao.getCfTmpl()
      .createUpdater(metadata.getShortCode());
      
      saeMetadataDao.ecritLongCode(metadata.getLongCode(), updater, clock);
      saeMetadataDao.ecritType(metadata.getType(), updater, clock);
      if(BooleanUtils.isFalse(metadata.isRequiredForArchival()) ||BooleanUtils.isTrue(metadata.isRequiredForArchival())){
         saeMetadataDao.ecritRequiredArchival(metadata.isRequiredForArchival(), updater, clock);
      }else{
         saeMetadataDao.ecritRequiredArchival(false, updater, clock);
      }
      if(BooleanUtils.isFalse(metadata.isRequiredForStorage()) ||BooleanUtils.isTrue(metadata.isRequiredForStorage())){
         saeMetadataDao.ecritRequiredStorage(metadata.isRequiredForStorage(), updater, clock);
      }else{
         saeMetadataDao.ecritRequiredStorage(false, updater, clock);
      }
      if(metadata.getLength()>=0 ){
         saeMetadataDao.ecritLength(metadata.getLength(), updater, clock);
      }else{
         saeMetadataDao.ecritLength(-1, updater, clock);   
      }
      saeMetadataDao.ecritPattern(metadata.getPattern(), updater, clock);
      
      if(BooleanUtils.isFalse(metadata.isConsultable()) ||BooleanUtils.isTrue(metadata.isConsultable())){
         saeMetadataDao.ecritConsultable(metadata.isConsultable(), updater, clock);
      }else{
         saeMetadataDao.ecritConsultable(false, updater, clock);
      }
      
      if(BooleanUtils.isFalse(metadata.isDefaultConsultable()) ||BooleanUtils.isTrue(metadata.isDefaultConsultable())){
         saeMetadataDao.ecritDefaultConsultable(metadata.isDefaultConsultable(), updater, clock);
      }else{
         saeMetadataDao.ecritDefaultConsultable(false, updater, clock);
      }
      
      if(BooleanUtils.isFalse(metadata.isSearchable()) ||BooleanUtils.isTrue(metadata.isSearchable())){
         saeMetadataDao.ecritSearchable(metadata.isSearchable(), updater, clock);
      }else{
         saeMetadataDao.ecritSearchable(false, updater, clock);
      }
      
      if(BooleanUtils.isFalse(metadata.isInternal()) ||BooleanUtils.isTrue(metadata.isInternal())){
         saeMetadataDao.ecritInternal(metadata.isInternal(), updater, clock);
      }else{
         saeMetadataDao.ecritInternal(false, updater, clock);
      }
      
      if(BooleanUtils.isFalse(metadata.isArchivable()) ||BooleanUtils.isTrue(metadata.isArchivable())){
         saeMetadataDao.ecritArchivable(metadata.isArchivable(), updater, clock);
      }else{
         saeMetadataDao.ecritArchivable(false, updater, clock);
      }
      
      saeMetadataDao.ecritLabel(metadata.getLabel(), updater, clock);
      saeMetadataDao.ecritDescription(metadata.getDescription(), updater, clock);
      
      if(BooleanUtils.isFalse(metadata.getHasDictionary()) ||BooleanUtils.isTrue(metadata.getHasDictionary())){
         saeMetadataDao.ecritHasDictionary(metadata.getHasDictionary(), updater, clock);
      }else{
         saeMetadataDao.ecritHasDictionary(false, updater, clock);
      }
      
      saeMetadataDao.ecritDictionaryName(metadata.getDictionaryName(), updater, clock);
      
      if(BooleanUtils.isFalse(metadata.getIsIndexed()) ||BooleanUtils.isTrue(metadata.getIsIndexed())){
         saeMetadataDao.ecritIndexed(metadata.getIsIndexed(), updater, clock);
      }else{
         saeMetadataDao.ecritIndexed(false, updater, clock);
      }
      
      updater.update();
   }
   
   /**
    * Retourne la liste des métadonnées
    * @return Liste des métadonnées
    */
   
   public List<MetadataReference> findAll(){
      
      BytesArraySerializer bytesSerializer = BytesArraySerializer.get();
      RangeSlicesQuery<String, String, byte[]> rangeSlicesQuery = HFactory
            .createRangeSlicesQuery(saeMetadataDao.getKeyspace(), StringSerializer.get(),
                  StringSerializer.get(), bytesSerializer);
      rangeSlicesQuery.setColumnFamily(saeMetadataDao.getColumnFamilyName());
      rangeSlicesQuery.setRange("", "", false, MAX_FIND_RESULT);
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
    * Méthode permettant de récupérer les informations pour une métadonnée spécifique
    * @param code Code court de la métadonné à rechercher
    * @return {@link MetadataReference}
    */
   public MetadataReference find(String code){
      ColumnFamilyResult<String, String> result = saeMetadataDao.getCfTmpl()
      .queryColumns(code);

      MetadataReference meta = getMetadataFromResult(result);

      return meta;
   }
  
   /**
    * Construction d'un objet {@link MetadataReference} à partir du résultat de la requête
    * @param result {@link ColumnFamilyResult}
    * @return {@link MetadataReference}
    */
   private MetadataReference getMetadataFromResult(ColumnFamilyResult<String, String> result){
      MetadataReference meta = null;
      if (result != null && result.hasResults()) {
         meta = new MetadataReference();
         meta.setDescription(result.getString(SaeMetadataDao.META_DESCR));
         meta.setDictionaryName(result.getString(SaeMetadataDao.META_DICT_NAME));
         meta.setLabel(result.getString(SaeMetadataDao.META_LABEL));
         if(result.getString(SaeMetadataDao.META_LENGTH)!=null && !result.getString(SaeMetadataDao.META_LENGTH).equals("-1")){
            meta.setLength(result.getInteger(SaeMetadataDao.META_LENGTH));
         }else if(result.getString(SaeMetadataDao.META_LENGTH)!=null && result.getString(SaeMetadataDao.META_LENGTH).equals("-1")){
            meta.setLength(-1);
         }else{
            meta.setLength(0);
         }
         meta.setLongCode(result.getString(SaeMetadataDao.META_LONG_CODE));
         meta.setPattern(result.getString(SaeMetadataDao.META_PATTERN));
         if(result.getString(SaeMetadataDao.META_REQ_ARCH)!=null){
            meta.setRequiredForArchival(new Boolean(result.getString(SaeMetadataDao.META_REQ_ARCH)));
         }else{
            meta.setRequiredForArchival(Boolean.FALSE);
         }
         if(result.getString(SaeMetadataDao.META_REQ_STOR)!=null){
            meta.setRequiredForStorage(new Boolean(result.getString(SaeMetadataDao.META_REQ_STOR)));
         }else{
            meta.setRequiredForStorage(Boolean.FALSE);
         }
         if(result.getString(SaeMetadataDao.META_SEARCH)!=null){
            meta.setSearchable(new Boolean(result.getString(SaeMetadataDao.META_SEARCH)));
         }else{
            meta.setSearchable(Boolean.FALSE);
         }
         meta.setType(result.getString(SaeMetadataDao.META_TYPE));
         if(result.getString(SaeMetadataDao.META_ARCH)!=null){
            meta.setArchivable(new Boolean(result.getString(SaeMetadataDao.META_ARCH)));
         }else{
            meta.setArchivable(Boolean.FALSE);
         }
         if(result.getString(SaeMetadataDao.META_CONSUL)!=null){
            meta.setConsultable(new Boolean(result.getString(SaeMetadataDao.META_CONSUL)));
         }else{
            meta.setConsultable(Boolean.FALSE);
         }
         if(result.getString(SaeMetadataDao.META_DEF_CONSUL)!=null){
            meta.setDefaultConsultable(new Boolean(result.getString(SaeMetadataDao.META_DEF_CONSUL)));
         }else{
            meta.setConsultable(Boolean.FALSE);
         }
         if(result.getString(SaeMetadataDao.META_HAS_DICT)!=null){
            meta.setHasDictionary(new Boolean(result.getString(SaeMetadataDao.META_HAS_DICT)));
         }else{
            meta.setHasDictionary(Boolean.FALSE);
         }
         if(result.getString(SaeMetadataDao.META_INTERNAL)!=null){
            meta.setInternal(new Boolean(result.getString(SaeMetadataDao.META_INTERNAL)));
         }else{
            meta.setInternal(Boolean.FALSE);
         }
         if(result.getString(SaeMetadataDao.META_INDEXED)!=null){
            meta.setIsIndexed(new Boolean(result.getString(SaeMetadataDao.META_INDEXED)));
         }else{
            meta.setIsIndexed(Boolean.FALSE);
         }
         meta.setShortCode(result.getKey());
      }
      return meta;
   }
}
