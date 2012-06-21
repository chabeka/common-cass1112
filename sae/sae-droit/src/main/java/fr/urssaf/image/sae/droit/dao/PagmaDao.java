/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import java.util.List;

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

import fr.urssaf.image.sae.droit.dao.serializer.ListStringSerializer;

/**
 * Service DAO de la famille de colonnes "DroitPagma" 
 * 
 */
@Repository
public class PagmaDao {
   
   public static final String PAGMA_AU = "actionsUnitaires";

   private static final String PAGMA_CFNAME = "DroitPagma";

   private static final int MAX_JOB_ATTIBUTS = 100;

   private final ColumnFamilyTemplate<String, String> pagmaTmpl;

   private final Keyspace keyspace;
   
   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PagmaDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      pagmaTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, PAGMA_CFNAME, StringSerializer.get(),
            StringSerializer.get());

      pagmaTmpl.setCount(MAX_JOB_ATTIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitPagma</code>
    */
   public final ColumnFamilyTemplate<String, String> gePagmaTmpl() {

      return this.pagmaTmpl;
   }

   /**
    * 
    * @return Mutator de <code>DroitPagma</code>
    */
   public final Mutator<String> createMutator() {

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }

   private void addListColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, List<String> value, Serializer<String> nameSerializer,
         ListStringSerializer valueSerializer, long clock) {

      HColumn<String, List<String>> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }
   
   
   /**
    * ajoute une colonne {@value #PAGMA_AU}
    * 
    * @param updater
    *           updater de <code>DroitPagma</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritActionsUnitaires(
         ColumnFamilyUpdater<String, String> updater, List<String> value, long clock) {

      addListColumn(updater, PAGMA_AU, value, StringSerializer.get(),
            ListStringSerializer.get(), clock);

   }
   
   /**
    * Suppression d'un Pagma
    * 
    * @param mutator
    *           Mutator de <code>PAGMA</code>
    * @param code
    *           identifiant du PAGMA
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionPagma(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, PAGMA_CFNAME, clock);

   }
   
}
