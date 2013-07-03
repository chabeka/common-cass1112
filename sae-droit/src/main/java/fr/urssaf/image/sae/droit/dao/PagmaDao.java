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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Service DAO de la famille de colonnes "DroitPagma"
 * 
 */
@Repository
public class PagmaDao {

   public static final String PAGMA_AU = "actionsUnitaires";

   public static final String PAGMA_CFNAME = "DroitPagma";

   public static final int MAX_ATTRIBUTS = 100;

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

      pagmaTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            PAGMA_CFNAME, StringSerializer.get(), StringSerializer.get());

      pagmaTmpl.setCount(MAX_ATTRIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitPagma</code>
    */
   public final ColumnFamilyTemplate<String, String> getPagmaTmpl() {

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

   private void addColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, Serializer<String> nameSerializer,
         Serializer<String> valueSerializer, long clock) {

      HColumn<String, String> column = HFactory.createColumn(colName,
            StringUtils.EMPTY, nameSerializer, valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne Action unitaire
    * 
    * @param updater
    *           updater de <code>DroitPagma</code>
    * @param colName
    *           identifiant de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritActionUnitaire(
         ColumnFamilyUpdater<String, String> updater, String colName, long clock) {

      addColumn(updater, colName, StringSerializer.get(), StringSerializer.get(),
            clock);

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

   /**
    * @return the keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }

}
