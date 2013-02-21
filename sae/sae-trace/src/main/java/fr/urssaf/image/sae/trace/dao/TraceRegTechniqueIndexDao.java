/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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

   private final ColumnFamilyTemplate<String, UUID> techIndexTmpl;
   private final Keyspace keyspace;

   private final DateFormat dateFormatJournee = new SimpleDateFormat(
         "yyyyMMdd", Locale.FRANCE);

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceRegTechniqueIndexDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      techIndexTmpl = new ThriftColumnFamilyTemplate<String, UUID>(keyspace,
            REG_TECHNIQUE_INDEX_CFNAME, StringSerializer.get(), UUIDSerializer
                  .get());

      techIndexTmpl.setCount(MAX_ATTRIBUTS);
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
    *           updater de <b>TraceRegTechniqueIndex</b>
    * @param name
    *           nom de la colonne
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumn(ColumnFamilyUpdater<String, UUID> updater,
         UUID name, TraceRegTechniqueIndex value, long clock) {
      addColumn(updater, name, value, TraceRegTechniqueIndexSerializer.get(),
            clock);
   }

   /**
    * 
    * @return SliceQuery de <code>TraceRegTechniqueIndex</code>
    */
   public final SliceQuery<String, UUID, TraceRegTechniqueIndex> createSliceQuery() {

      SliceQuery<String, UUID, TraceRegTechniqueIndex> sliceQuery = HFactory
            .createSliceQuery(keyspace, StringSerializer.get(), UUIDSerializer
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
         Mutator<String> mutator, String code, long clock) {

      mutator.addDeletion(code, REG_TECHNIQUE_INDEX_CFNAME, clock);
   }

   /**
    * 
    * @return Mutator de <code>TraceRegTechniqueIndex</code>
    */
   public final Mutator<String> createMutator() {

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }

   /**
    * Renvoie la journée au format AAAAMMJJ correspondant à un timestamp<br>
    * La journée est la clé de la CF {@value #REG_TECHNIQUE_INDEX_CFNAME}
    * 
    * @param timestamp
    *           le timestamp
    * @return la journée au format {@value #REG_TECHNIQUE_INDEX_CFNAME} (clé de
    *         la CF {@value #REG_TECHNIQUE_INDEX_CFNAME})
    */
   public final String getJournee(Date timestamp) {
      return dateFormatJournee.format(timestamp);
   }

   /**
    * Création du ColumnFamilyUpdater
    * 
    * @param journee
    *           la journée, au format obtenu de {@link #getJournee(Date)}
    * @return le ColumnFamilyUpdater
    */
   public final ColumnFamilyUpdater<String, UUID> createUpdater(String journee) {
      return techIndexTmpl.createUpdater(journee);
   }

   /**
    * Flush les mises à jour
    * 
    * @param updater
    *           l'updater de la CF
    */
   public final void update(ColumnFamilyUpdater<String, UUID> updater) {
      techIndexTmpl.update(updater);
   }

}
