package fr.urssaf.image.cassandra.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.Assert;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.cassandra.service.spring.HectorTemplateImpl;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import fr.urssaf.image.administration.modele.ColumnFamily;
import fr.urssaf.image.administration.modele.ComparatorTypeFinder;
import fr.urssaf.image.administration.modele.DataBaseModel;
import fr.urssaf.image.cassandra.SAEHector;

public class SAEHectorImpl extends HectorTemplateImpl implements SAEHector {
   private static final MessageSource MESSAGE_SOURCES = (MessageSource) new ClassPathXmlApplicationContext(
         "applicationContext-sae-hector.xml").getBean("messageSource");

   private Keyspace keyspace;

   /**
    * Créer un schéma de base de données.
    * 
    * @param dataModel
    * @return le nom du schéma.
    * @throws UnsupportedEncodingException
    * 
    */
   public String createCassandraSchema(DataBaseModel dataModel)
         throws UnsupportedEncodingException {
      KeyspaceDefinition keyspaceDef = dataModel.getCluster().describeKeyspace(
            dataModel.getKeyspace());
      // If keyspace does not exist, the CFs don't exist either. => create them.
      // createSchema(dataModel);
      if (keyspaceDef == null) {
         keyspaceDef = createSchema(dataModel);
      }
      return keyspaceDef.getName();
   }

   /**
    * Créer un schéma de base de données.
    * 
    * @param keyspace
    * @return KeyspaceDefinition
    * @throws UnsupportedEncodingException
    */
   private KeyspaceDefinition createSchema(DataBaseModel dataModel)
         throws UnsupportedEncodingException {
      Assert.assertNotNull(dataModel);
      ArrayList<ColumnFamilyDefinition> listeCf = new ArrayList<ColumnFamilyDefinition>();
      List<ColumnFamily> columnFamilies = columnFamilies = dataModel
            .getColumnFamilies().getColumnFamily();
      ColumnFamilyDefinition cfDef = null;
      for (ColumnFamily columnFamily : columnFamilies) {
         cfDef = HFactory.createColumnFamilyDefinition(dataModel.getKeyspace(),
               columnFamily.getName(), ComparatorTypeFinder
                     .comparatorTypeFinder(columnFamily.getComparatorType()));
         int gcGraceSeconds = Integer.valueOf(MESSAGE_SOURCES.getMessage(
               "gcGraceSeconds", null, Locale.FRENCH));
         cfDef.setGcGraceSeconds(gcGraceSeconds);
         Map<String, String> compressionOptions = new HashMap<String, String>();
         compressionOptions.put(MESSAGE_SOURCES.getMessage("compressionCode",
               null, Locale.FRENCH), MESSAGE_SOURCES.getMessage(
               "compressionValue", null, Locale.FRENCH));
         cfDef.setCompressionOptions(compressionOptions);
         cfDef.setCompactionStrategy(MESSAGE_SOURCES.getMessage(
               "leveledCompactionStrategy", null, Locale.FRENCH));
         if (cfDef != null) {
            listeCf.add(cfDef);
         }
      }
      KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition(
            dataModel.getKeyspace(), ThriftKsDef.DEF_STRATEGY_CLASS, dataModel
                  .getReplicationFactor(), listeCf);
      dataModel.getCluster().addKeyspace(newKeyspace, true);

      return dataModel.getCluster().describeKeyspace(dataModel.getKeyspace());
   }

   public void deleteColumn(String keyspace, String columnFamilyName) {
      if (getCluster().describeKeyspace(keyspace) != null) {
         ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<String, String>(
               getKeyspace(), columnFamilyName, StringSerializer.get(),
               StringSerializer.get());
         template.deleteColumn(keyspace, columnFamilyName);
      }
   }

   /**
    * @return the keyspace
    */
   @Override
   public Keyspace getKeyspace() {
      return keyspace;
   }

   /**
    * @param keyspace
    *           the keyspace to set
    */
   @Override
   public void setKeyspace(Keyspace keyspace) {
      this.keyspace = keyspace;
   }

}
