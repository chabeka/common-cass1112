/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import java.util.Map;

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

import fr.urssaf.image.sae.droit.dao.serializer.MapSerializer;

/**
 * Service DAO de la famille de colonnes "DroitPrmd"
 * 
 */
@Repository
public class PrmdDao {

   public static final String PRMD_DESCRIPTION = "description";

   public static final String PRMD_LUCENE = "lucene";

   public static final String PRMD_METADATA = "metadata";

   public static final String PRMD_BEAN = "bean";

   public static final String PRMD_CFNAME = "DroitPrmd";

   public static final int MAX_ATTRIBUTS = 100;

   private final ColumnFamilyTemplate<String, String> prmdTmpl;

   private final Keyspace keyspace;

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PrmdDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      prmdTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            PRMD_CFNAME, StringSerializer.get(), StringSerializer.get());

      prmdTmpl.setCount(MAX_ATTRIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitPagmp</code>
    */
   public final ColumnFamilyTemplate<String, String> getPrmdTmpl() {

      return this.prmdTmpl;
   }

   /**
    * 
    * @return Mutator de <code>DroitPagmp</code>
    */
   public final Mutator<String> createMutator() {

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }

   private void addColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, String value, Serializer<String> nameSerializer,
         Serializer<String> valueSerializer, long clock) {

      HColumn<String, String> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   private void addMapColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, Map<String, String> value,
         Serializer<String> nameSerializer,
         Serializer<Map<String, String>> valueSerializer, long clock) {

      HColumn<String, Map<String, String>> column = HFactory.createColumn(
            colName, value, nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne {@value #PRMD_DESCRIPTION}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritDescription(
         ColumnFamilyUpdater<String, String> updater, String value, long clock) {

      addColumn(updater, PRMD_DESCRIPTION, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PRMD_LUCENE}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritLucene(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, PRMD_LUCENE, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PRMD_METADATA}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritMetaData(ColumnFamilyUpdater<String, String> updater,
         Map<String, String> value, long clock) {

      addMapColumn(updater, PRMD_METADATA, value, StringSerializer.get(),
            MapSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PRMD_BEAN}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritBean(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, PRMD_BEAN, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Suppression d'un Prmd
    * 
    * @param mutator
    *           Mutator de <code>Prmd</code>
    * @param code
    *           identifiant de l'action unitaire
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionPrmd(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, PRMD_CFNAME, clock);

   }

   /**
    * @return the keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }
}
