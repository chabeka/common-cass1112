/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
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

import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegSecuriteIndexSerializer;

/**
 * Service DAO de la famille de colonnes "TraceRegSecuriteIndex"
 * 
 */
@Repository
public class TraceRegSecuriteIndexDao {

   private static final int MAX_ATTRIBUTS = 100;
   public static final String REG_SECURITE_INDEX_CFNAME = "TraceRegSecuriteIndex";

   private final ColumnFamilyTemplate<String, UUID> secuIndexTmpl;
   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisés
    */
   @Autowired
   public TraceRegSecuriteIndexDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      secuIndexTmpl = new ThriftColumnFamilyTemplate<String, UUID>(keyspace,
            REG_SECURITE_INDEX_CFNAME, StringSerializer.get(), UUIDSerializer
                  .get());

      secuIndexTmpl.setCount(MAX_ATTRIBUTS);
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<String, UUID> updater,
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
    *           updater de <b>TraceRegSecuriteIndex</b>
    * @param name
    *           nom de la colonne
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumn(ColumnFamilyUpdater<String, UUID> updater,
         UUID name, TraceRegSecuriteIndex value, long clock) {
      addColumn(updater, name, value, TraceRegSecuriteIndexSerializer.get(),
            clock);
   }

   /**
    * 
    * @return SliceQuery de <code>TraceRegSecuriteIndex</code>
    */
   public final SliceQuery<String, UUID, TraceRegSecuriteIndex> createSliceQuery() {

      SliceQuery<String, UUID, TraceRegSecuriteIndex> sliceQuery = HFactory
            .createSliceQuery(keyspace, StringSerializer.get(), UUIDSerializer
                  .get(), TraceRegSecuriteIndexSerializer.get());

      sliceQuery.setColumnFamily(REG_SECURITE_INDEX_CFNAME);

      return sliceQuery;
   }

   /**
    * Méthode de suppression d'une ligne TraceRegSecuriteIndex
    * 
    * @param mutator
    *           Mutator de <code>TraceRegSecuriteIndex</code>
    * @param code
    *           identifiant de la ligne d'index
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionTraceRegSecuriteIndex(
         Mutator<String> mutator, String code, long clock) {

      mutator.addDeletion(code, REG_SECURITE_INDEX_CFNAME, clock);
   }

   /**
    * 
    * @return Mutator de <code>TraceRegSecuriteIndex</code>
    */
   public final Mutator<String> createMutator() {

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }

   /**
    * Création du ColumnFamilyUpdater
    * 
    * @param journee
    *           la journée, au format obtenu de {@link #getJournee(Date)}
    * @return le ColumnFamilyUpdater
    */
   public final ColumnFamilyUpdater<String, UUID> createUpdater(String journee) {
      return secuIndexTmpl.createUpdater(journee);
   }

   /**
    * Flush les mises à jour
    * 
    * @param updater
    *           l'updater de la CF
    */
   public final void update(ColumnFamilyUpdater<String, UUID> updater) {
      secuIndexTmpl.update(updater);
   }
}
