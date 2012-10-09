/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

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

/**
 * Service DAO de la famille de colonnes "DroitActionUnitaire"
 * 
 */
@Repository
public class ActionUnitaireDao {

   public static final String AU_CFNAME = "DroitActionUnitaire";

   public static final int MAX_AU_ATTIBUTS = 100;

   private final ColumnFamilyTemplate<String, String> cfTmpl;

   private final Keyspace keyspace;

   public static final String AU_DESCRIPTION = "description";

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public ActionUnitaireDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, AU_CFNAME, StringSerializer.get(),
            StringSerializer.get());

      cfTmpl.setCount(MAX_AU_ATTIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitActionUnitaire</code>
    */
   public final ColumnFamilyTemplate<String, String> getActionUnitaireTmpl() {

      return this.cfTmpl;
   }

   /**
    * 
    * @return Mutator de <code>DroitActionUnitaire</code>
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

   /**
    * ajoute une colonne {@value #AU_DESCRIPTION}
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

      addColumn(updater, AU_DESCRIPTION, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Suppression d'une action unitaire
    * 
    * @param mutator
    *           Mutator de <code>ActionUnitaire</code>
    * @param code
    *           identifiant de l'action unitaire
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionActionUnitaire(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, AU_CFNAME, clock);

   }

   /**
    * @return le keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }
}
