package fr.urssaf.image.sae.metadata.referential.dao;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;

/**
 * DAO associé à la CF Dictionary
 * 
 * 
 */
@Repository
public class DictionaryDao extends AbstractDao<String, String> {

   private static final String CF_NAME = "Dictionary";

   /**
    * constructeur de la DAO
    * 
    * @param keyspace
    *           le keyspace contenant la CF
    */
   @Autowired
   public DictionaryDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    *renvoie le nom de la CF
    * 
    * @return {@link String}
    */
   @Override
   public final String getColumnFamilyName() {
      return CF_NAME;
   }

   /**
    *renvoie le serializer de la CF
    * 
    * @return {@link StringSerializer}
    */
   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    *renvoie le serializer de la clé de la CF
    * 
    * @return {@link StringSerializer}
    */
   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * Méthode permettant d'écrire une ligne de la CF
    * 
    * @param element
    *           element à écrire
    * @param updater
    *           un CF updater
    * @param clock
    *           le timestamp
    */
   public final void ecritElement(String element,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, element, StringUtils.EMPTY, StringSerializer.get(), clock);
   }
}
