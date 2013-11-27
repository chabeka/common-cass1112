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

/**
 * Service DAO de la famille de colonnes "DroitPagmp"
 * 
 */
@Repository
public class PagmpDao extends AbstractDao<String, String> {

   public static final String PAGMP_DESCRIPTION = "description";

   public static final String PAGMP_PRMD = "prmd";

   public static final String PAGMP_CFNAME = "DroitPagmp";

   public static final int MAX_ATTRIBUTS = 100;

   /**
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PagmpDao(Keyspace keyspace) {
      super(keyspace);
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
   public final void ecritPrmd(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, PAGMP_PRMD, value, StringSerializer.get(), clock);

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
            clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getColumnFamilyName() {
      return PAGMP_CFNAME;
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
