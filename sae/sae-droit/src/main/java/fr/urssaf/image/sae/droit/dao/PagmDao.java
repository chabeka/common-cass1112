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

import fr.urssaf.image.sae.droit.dao.serializer.MapStringSerializer;

/**
 * Service DAO de la famille de colonnes "DroitPagm"
 * 
 */
@Repository
public class PagmDao {

   public static final String PAGM_PAGMA = "pagma";

   public static final String PAGM_PAGMP = "pagmp";

   public static final String PAGM_DESCRIPTION = "description";

   public static final String PAGM_PARAMETRES = "parametres";

   private static final String PAGM_CFNAME = "DroitPagm";

   private static final int MAX_JOB_ATTIBUTS = 100;

   private final ColumnFamilyTemplate<String, String> pagmTmpl;

   private final Keyspace keyspace;

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PagmDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      pagmTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            PAGM_CFNAME, StringSerializer.get(), StringSerializer.get());

      pagmTmpl.setCount(MAX_JOB_ATTIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitPagm</code>
    */
   public final ColumnFamilyTemplate<String, String> gePagmaTmpl() {

      return this.pagmTmpl;
   }

   /**
    * 
    * @return Mutator de <code>DroitPagm</code>
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
    * ajoute une colonne {@value #PAGM_PAGMA}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritPagma(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, PAGM_PAGMA, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PAGM_PAGMP}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritPagmp(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, PAGM_PAGMP, value, StringSerializer.get(),
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

      addColumn(updater, PAGM_DESCRIPTION, value, StringSerializer.get(),
            StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PAGM_PARAMETRES}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritParametres(
         ColumnFamilyUpdater<String, String> updater,
         Map<String, String> value, long clock) {

      addMapColumn(updater, PAGM_PARAMETRES, value, StringSerializer.get(),
            MapStringSerializer.get(), clock);

   }
   
   /**
    * Suppression d'un PAGM
    * 
    * @param mutator
    *           Mutator de <code>Pagm</code>
    * @param code
    *           identifiant du Pagm
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionPagm(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, PAGM_CFNAME, clock);

   }
}
