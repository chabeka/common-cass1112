package fr.urssaf.image.commons.exemples.cassandra.dfce.dao;

import java.util.Date;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import fr.urssaf.image.commons.cassandra.serializer.NullableDateSerializer;
import fr.urssaf.image.commons.exemples.cassandra.dfce.modele.Job;


public class JobsDao {

   private static final String CFNAME = "Jobs";
   
   private static final String COL_TYPE_ATTRIBUTE_NAME = "TYPE_ATTRIBUTE_NAME";
   private static final String COL_KEY = "key";
   private static final String COL_LAST_OK_DATE = "lastSuccessfullRunDate";
   private static final String COL_LAUNCH_DATE = "launchDate";
   private static final String COL_RUNNING = "running";
   
    
   
   
   public ColumnFamilyTemplate<String, String> createCFTemplate(Keyspace keyspace) {
      
      // Propriété de clé:
      //  - Type de la valeur : String
      //  - Serializer de la valeur : StringSerializer
      
      ColumnFamilyTemplate<String, String> tmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace,
            CFNAME,
            StringSerializer.get(),
            StringSerializer.get());
      
      tmpl.setCount(100);
      
      return tmpl; 
      
   }
   
   
   
   @SuppressWarnings("unchecked")
   private void addColumn(
         ColumnFamilyUpdater<String, String> updater,
         String colName, 
         Object value, 
         Serializer nameSerializer,
         Serializer valueSerializer
         // ,long clock
         ) {
      
      HColumn<String, Object> column = HFactory.createColumn(
            colName, 
            value,
            nameSerializer, 
            valueSerializer);
      
      // column.setTtl(TTL);
      // column.setClock(clock);
      updater.setColumn(column);
      
   }
   
   
   public void ecritColonneRunning(
         ColumnFamilyUpdater<String, String> updater,
         Boolean valeur
         // , long clock
         ) {
      
      addColumn(updater, COL_RUNNING, valeur, StringSerializer.get(), BooleanSerializer.get()
            // , clock
            );
      
   }
   
   
   
   public Job createModeleFromResult(
         ColumnFamilyResult<String, String> result) {
      
      if (result == null || !result.hasResults()) {
         return null;
      }
      
      Serializer<Date> dSlz = NullableDateSerializer.get();
      
      Job job = new Job();
      
      
      job.setNom(result.getKey());
      
      
      job.setKey(result.getString(COL_KEY));
      
      if (result.getByteArray(COL_LAST_OK_DATE)!=null) {
         Date laDate = dSlz.fromBytes(result.getByteArray(COL_LAST_OK_DATE));
         job.setLastSuccessfullRunDate(laDate);
      }
      
      if (result.getByteArray(COL_LAUNCH_DATE)!=null) {
         Date laDate = dSlz.fromBytes(result.getByteArray(COL_LAUNCH_DATE));
         job.setLaunchDate(laDate);
      }
      
      job.setRunning(result.getBoolean(COL_RUNNING));
      
      
      job.setTypeAttributeName(result.getString(COL_TYPE_ATTRIBUTE_NAME));
      

      return job;
   }
   
   

   
   
   
}
