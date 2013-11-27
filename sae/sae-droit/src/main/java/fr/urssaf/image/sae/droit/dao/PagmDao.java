/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.model.Pagm;
import fr.urssaf.image.sae.droit.dao.serializer.PagmSerializer;

/**
 * Service DAO de la famille de colonnes "DroitPagm"
 * 
 */
@Repository
public class PagmDao extends AbstractDao<String, String> {

   public static final String PAGM_PAGMA = "pagma";

   public static final String PAGM_PAGMP = "pagmp";

   public static final String PAGM_DESCRIPTION = "description";

   public static final String PAGM_PARAMETRES = "parametres";

   public static final String PAGM_CFNAME = "DroitPagm";

   public static final int MAX_ATTRIBUTS = 100;

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PagmDao(Keyspace keyspace) {
      super(keyspace);

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

      addColumn(updater, value.getCode(), value, PagmSerializer.get(), clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getColumnFamilyName() {
      return PAGM_CFNAME;
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
