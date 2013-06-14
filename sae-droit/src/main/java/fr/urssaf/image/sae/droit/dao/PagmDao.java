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

import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;

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

   public static final String PAGM_CFNAME = "DroitPagm";

   public static final int MAX_ATTRIBUTS = 100;

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

      pagmTmpl.setCount(MAX_ATTRIBUTS);

   }

   /**
    * 
    * @return CassandraTemplate de <code>DroitPagm</code>
    */
   public final ColumnFamilyTemplate<String, String> getPagmTmpl() {

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
         String colName, Pagm value, Serializer<String> nameSerializer,
         Serializer<Pagm> pagmSerializer, long clock) {

      HColumn<String, Pagm> column = HFactory.createColumn(colName, value,
            nameSerializer, pagmSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * ajoute une colonne de PAGM
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritPagm(ColumnFamilyUpdater<String, String> updater,
         Pagm value, long clock) {

      addColumn(updater, value.getCode(), value, StringSerializer.get(),
            PagmSerializer.get(), clock);

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

   /**
    * @return the keyspace
    */
   public final Keyspace getKeyspace() {
      return keyspace;
   }
}
