/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;

/**
 * Service DAO de la famille de colonnes "DroitPagma"
 * 
 */
@Repository
public class PagmaDao extends AbstractDao<String, String> {

   public static final String PAGMA_AU = "actionsUnitaires";

   public static final String PAGMA_CFNAME = "DroitPagma";

   public static final int MAX_ATTRIBUTS = 100;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PagmaDao(Keyspace keyspace) {
      super(keyspace);

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

      addColumn(updater, colName, StringUtils.EMPTY, StringSerializer.get(),
            clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getColumnFamilyName() {
      return PAGMA_CFNAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

}
