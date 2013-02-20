/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.Date;

import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.model.Parameter;

/**
 * Service DAO de la famille de colonnes "Parameters"
 * 
 */
@Repository
public class ParametersDao {

   private static final int MAX_ATTRIBUTS = 100;
   public static final String PARAM_CFNAME = "Parameters";
   public static final String KEY_ROW_PURGE = "parametresPurges";

   private final ColumnFamilyTemplate<String, String> paramsTmpl;
   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public ParametersDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      paramsTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            PARAM_CFNAME, StringSerializer.get(), StringSerializer.get());

      paramsTmpl.setCount(MAX_ATTRIBUTS);
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, Object value, Serializer valueSerializer, long clock) {

      HColumn<String, Object> column = HFactory.createColumn(colName, value,
            StringSerializer.get(), valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne de paramètre <b>name</b>
    * 
    * @param updater
    *           updater de <b>TraceRegTechniqueIndex</b>
    * @param parameter
    *           le paramètre à écrire
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnParameter(
         ColumnFamilyUpdater<String, String> updater, Parameter parameter,
         long clock) {
      String name = parameter.getName().toString();
      Object value = parameter.getValue();
      addColumn(updater, name, value, ObjectSerializer.get(), clock);
   }

   /**
    * 
    * @return Mutator de <code>TraceRegTechniqueIndex</code>
    */
   public final Mutator<Date> createMutator() {

      Mutator<Date> mutator = HFactory.createMutator(keyspace, DateSerializer
            .get());

      return mutator;

   }

   /**
    * @return le CassandraTemplate de <code>TraceRegTechniqueIndex</code>
    */
   public final ColumnFamilyTemplate<String, String> getParamsTmpl() {
      return paramsTmpl;
   }
}
