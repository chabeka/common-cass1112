/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.Date;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegTechniqueIndexSerializer;

/**
 * Service DAO de la famille de colonnes "TraceRegTechniqueIndex"
 * 
 */
@Repository
public class TraceRegTechniqueIndexDao {

   private static final int MAX_ATTRIBUTS = 100;
   public static final String REG_TECHNIQUE_INDEX_CFNAME = "TraceRegTechniqueIndex";

   private final ColumnFamilyTemplate<Date, UUID> techIndexTmpl;
   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceRegTechniqueIndexDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      techIndexTmpl = new ThriftColumnFamilyTemplate<Date, UUID>(keyspace,
            REG_TECHNIQUE_INDEX_CFNAME, DateSerializer.get(), UUIDSerializer
                  .get());

      techIndexTmpl.setCount(MAX_ATTRIBUTS);
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<Date, UUID> updater,
         UUID colName, Object value, Serializer valueSerializer, long clock) {

      HColumn<UUID, Object> column = HFactory.createColumn(colName, value,
            UUIDSerializer.get(), valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne <b>name</b>
    * 
    * @param updater
    *           updater de <b>TraceRegTechniqueIndex</b>
    * @param name
    *           nom de la colonne
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumn(ColumnFamilyUpdater<Date, UUID> updater,
         UUID name, TraceRegTechniqueIndex value, long clock) {
      addColumn(updater, name, value, TraceRegTechniqueIndexSerializer.get(),
            clock);
   }

   /**
    * 
    * @return SliceQuery de <code>TraceRegTechniqueIndex</code>
    */
   public final SliceQuery<Date, UUID, TraceRegTechniqueIndex> createSliceQuery() {

      SliceQuery<Date, UUID, TraceRegTechniqueIndex> sliceQuery = HFactory
            .createSliceQuery(keyspace, DateSerializer.get(), UUIDSerializer
                  .get(), TraceRegTechniqueIndexSerializer.get());

      sliceQuery.setColumnFamily(REG_TECHNIQUE_INDEX_CFNAME);

      return sliceQuery;
   }

   /**
    * Méthode de suppression d'une ligne TraceRegTechniqueIndex
    * 
    * @param mutator
    *           Mutator de <code>TraceRegTechniqueIndex</code>
    * @param code
    *           identifiant de la ligne d'index
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionTraceRegTechniqueIndex(
         Mutator<Date> mutator, Date code, long clock) {

      mutator.addDeletion(code, REG_TECHNIQUE_INDEX_CFNAME, clock);
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
   public final ColumnFamilyTemplate<Date, UUID> getTechIndexTmpl() {
      return techIndexTmpl;
   }
}
