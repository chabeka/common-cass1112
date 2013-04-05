package fr.urssaf.image.sae.metadata.referential.dao;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import fr.urssaf.image.sae.commons.dao.AbstractDao;

public class SaeMetadataDao extends AbstractDao<String, String> {

   private String META_LONG_ECDE = "ICode";
   private String META_TYPE = "type";
   private String META_REQ_ARCH = "reqArch";
   private String META_RED_STOR = "reqStor";
   private String META_LENGTH = "length";
   private String META_PATTERN = "pattern";
   private String META_CONSUL = "cons";
   private String META_DEF_CONSUL = "defCons";
   private String META_SEARCH = "search";
   private String META_INTERNAL = "int";
   private String META_ARCH = "arch";
   private String META_LABEL = "label";
   private String META_DESCR = "descr";
   private String META_HAS_DICT = "dictName";
   private String META_INDEXED = "index";

   public SaeMetadataDao(Keyspace keyspace) {
      super(keyspace);
      // TODO Auto-generated constructor stub
   }

   @Override
   public String getColumnFamilyName() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Serializer<String> getColumnKeySerializer() {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public Serializer<String> getRowKeySerializer() {

      // TODO Auto-generated method stub
      return null;
   }

   public void ecritLongCode(String icode) {

   }

   public void ecritType(String type) {

   }

   public void ecritRequiredArchival(Boolean reqArch) {

   }

   public void ecritRequiredStorage(Boolean reqStor) {

   }

   public void ecritLength(Integer length) {

   }

   public void ecritPattern(String pattern) {

   }

   public void ecritBoolean(Boolean cons) {

   }

   public void ecritSearchable(String search) {

   }

   public void ecritInternal(String internal) {

   }

   public void ecritArchivable(String arch) {

   }

   public void ecritLabel(String label) {

   }

   public void ecritDescription(String descr) {

   }

   public void ecritHasDictionary(Boolean hasDict) {

   }

   public void ecritDictionaryName(String dictName) {

   }

   public void ecritIndexed(Boolean index) {

   }

}
