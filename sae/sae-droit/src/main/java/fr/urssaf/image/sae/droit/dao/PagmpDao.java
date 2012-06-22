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
 * Service DAO de la famille de colonnes "DroitPagmp" 
 * 
 */
@Repository
public class PagmpDao {

   public static final String PAGMP_DESCRIPTION = "description";
   
   public static final String PAGMP_PRMD = "prmd";
   
   public static final String PAGMP_CFNAME = "DroitPagmp";

   public static final int MAX_ATTRIBUTS = 100;

   private final ColumnFamilyTemplate<String, String> pagmpTmpl;

   private final Keyspace keyspace;

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PagmpDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      pagmpTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            PAGMP_CFNAME, StringSerializer.get(), StringSerializer.get());

      pagmpTmpl.setCount(MAX_ATTRIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitPagmp</code>
    */
   public final ColumnFamilyTemplate<String, String> getPagmpTmpl() {

      return this.pagmpTmpl;
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
   
   
   /**
    * ajoute une colonne {@value #PAGMP_PRMD}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritPrmd(
         ColumnFamilyUpdater<String, String> updater, String value, long clock) {

      addColumn(updater, PAGMP_PRMD, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }
   
   /**
    * ajoute une colonne {@value #PAGM_DESCRIPTION}
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

      addColumn(updater, PAGMP_DESCRIPTION, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }
   
   /**
    * Suppression d'un PAGMP
    * 
    * @param mutator
    *           Mutator de <code>pagmp</code>
    * @param code
    *           identifiant du PAGMP
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionPagmp(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, PAGMP_CFNAME, clock);

   }

   /**
    * @return the keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }
}
