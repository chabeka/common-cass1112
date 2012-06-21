/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import me.prettyprint.cassandra.serializers.LongSerializer;
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
 * Service DAO de la famille de colonnes "DroitContratService"
 * 
 */
@Repository
public class ContratServiceDao {

   public static final String CS_CFNAME = "DroitContratService";

   public static final int MAX_CS_ATTIBUTS = 100;

   private final ColumnFamilyTemplate<String, String> cfTmpl;

   private final Keyspace keyspace;

   /** code de l'organisme client lié au contrat de service */
   public static final String CS_CODE_CLIENT = "codeClient";
   
   /** durée maximum de l'habilitation exprimée en secondes */
   public static final String CS_VI_DUREE = "viDuree";
   
   /** description du contrat de service */
   public static final String CS_DESCRIPTION = "description";

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public ContratServiceDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, CS_CFNAME, StringSerializer.get(),
            StringSerializer.get());

      cfTmpl.setCount(MAX_CS_ATTIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitContratService</code>
    */
   public final ColumnFamilyTemplate<String, String> getContratServiceTmpl() {

      return this.cfTmpl;
   }

   /**
    * 
    * @return Mutator de <code>DroitContratService</code>
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
   
   private void addLongColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, Long value, Serializer<String> nameSerializer,
         Serializer<Long> valueSerializer, long clock) {

      HColumn<String, Long> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne {@value #CS_CODE_CLIENT}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritCodeClient(
         ColumnFamilyUpdater<String, String> updater, String value, long clock) {

      addColumn(updater, CS_CODE_CLIENT, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }
   
   /**
    * ajoute une colonne {@value #CS_VI_DUREE}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritViDuree(
         ColumnFamilyUpdater<String, String> updater, Long value, long clock) {

      addLongColumn(updater, CS_VI_DUREE, value, StringSerializer.get(),
            LongSerializer.get(), clock);

   }
   
   /**
    * ajoute une colonne {@value #CS_DESCRIPTION}
    * 
    * @param updater
    *           updater de <code>DroitContratService</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritDescription(
         ColumnFamilyUpdater<String, String> updater, String value, long clock) {

      addColumn(updater, CS_DESCRIPTION, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * Suppression d'un Contrat de service
    * 
    * @param mutator
    *           Mutator de <code>ContratService</code>
    * @param code
    *           identifiant du contrat de service
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionContratService(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, CS_CFNAME, clock);

   }

   /**
    * @return the keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }
}
