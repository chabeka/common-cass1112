package fr.urssaf.image.sae.metadata.referential.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import fr.urssaf.image.sae.commons.dao.AbstractDao;
@Repository
public class DictionaryDao extends AbstractDao<String, String> {
   
   private static String CF_NAME="Dictionary";
   @Autowired
   public DictionaryDao(Keyspace keyspace) {
      super(keyspace);
      // TODO Auto-generated constructor stub
   }

   @Override
   public String getColumnFamilyName() {
      return CF_NAME;
   }

   @Override
   public Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   @Override
   public Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   
   public void ecritElement(String element,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, element, "", StringSerializer.get(), clock);
   }
}
