package fr.urssaf.image.sae.metadata.referential.dao;

import org.springframework.beans.factory.annotation.Autowired;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import fr.urssaf.image.sae.commons.dao.AbstractDao;

public class DictionaryDao extends AbstractDao<String, String> {
   @Autowired
   private Keyspace keySpace;
   
   public DictionaryDao(Keyspace keyspace) {
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

}
