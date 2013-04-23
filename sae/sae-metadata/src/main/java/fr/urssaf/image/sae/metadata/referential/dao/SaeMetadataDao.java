package fr.urssaf.image.sae.metadata.referential.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
@SuppressWarnings("PMD.TooManyMethods")
@Repository
public class SaeMetadataDao extends AbstractDao<String, String> {

   public static final String META_SHORT_CODE = "sCode";
   public static final String META_TYPE = "type";
   public static final String META_REQ_ARCH = "reqArch";
   public static final String META_REQ_STOR = "reqStor";
   public static final String META_LENGTH = "length";
   public static final String META_PATTERN = "pattern";
   public static final String META_CONSUL = "cons";
   public static final String META_DEF_CONSUL = "defCons";
   public static final String META_SEARCH = "search";
   public static final String META_INTERNAL = "int";
   public static final String META_ARCH = "arch";
   public static final String META_LABEL = "label";
   public static final String META_DESCR = "descr";
   public static final String META_HAS_DICT = "hasName";
   public static final String META_DICT_NAME = "dictName";
   public static final String META_INDEXED = "index";

   public static final String METADATA_CFNAME = "MetaData";

   @Autowired
   public SaeMetadataDao(Keyspace keyspace) {
      super(keyspace);
   }

   @Override
   public String getColumnFamilyName() {
      return METADATA_CFNAME;
   }

   @Override
   public Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   @Override
   public Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   public void ecritShortCode(String sCode,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_SHORT_CODE, sCode, StringSerializer.get(), clock);
   }

   public void ecritType(String type,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_TYPE, type, StringSerializer.get(), clock);
   }

   public void ecritRequiredArchival(Boolean reqArch,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_REQ_ARCH, reqArch, BooleanSerializer.get(), clock);
   }

   public void ecritRequiredStorage(Boolean reqStor,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_REQ_STOR, reqStor, BooleanSerializer.get(), clock);
   }

   public void ecritLength(Integer length,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_LENGTH, length, IntegerSerializer.get(), clock);
   }

   public void ecritPattern(String pattern,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_PATTERN, pattern, StringSerializer.get(), clock);
   }

   public void ecritConsultable(Boolean cons,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_CONSUL, cons, BooleanSerializer.get(), clock);
   }

   public void ecritDefaultConsultable(Boolean defCons,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_DEF_CONSUL, defCons, BooleanSerializer.get(),
            clock);
   }

   public void ecritSearchable(Boolean search,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_SEARCH, search, BooleanSerializer.get(), clock);
   }

   public void ecritInternal(Boolean internal,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_INTERNAL, internal, BooleanSerializer.get(),
            clock);
   }

   public void ecritArchivable(Boolean arch,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_ARCH, arch, BooleanSerializer.get(), clock);
   }

   public void ecritLabel(String label,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_LABEL, label, StringSerializer.get(), clock);
   }

   public void ecritDescription(String descr,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_DESCR, descr, StringSerializer.get(), clock);
   }

   public void ecritHasDictionary(Boolean hasDict,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_HAS_DICT, hasDict, BooleanSerializer.get(), clock);
   }

   public void ecritDictionaryName(String dictName,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_DICT_NAME, dictName, StringSerializer.get(),
            clock);
   }

   public void ecritIndexed(Boolean index,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, META_INDEXED, index, BooleanSerializer.get(), clock);
   }

}
