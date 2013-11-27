/**
 * 
 */
package fr.urssaf.image.sae.droit.dao;

import java.util.List;
import java.util.Map;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.droit.dao.serializer.MapSerializer;

/**
 * Service DAO de la famille de colonnes "DroitPrmd"
 * 
 */
@Repository
public class PrmdDao extends AbstractDao<String, String> {

   public static final String PRMD_DESCRIPTION = "description";

   public static final String PRMD_LUCENE = "lucene";

   public static final String PRMD_METADATA = "metadata";

   public static final String PRMD_BEAN = "bean";

   public static final String PRMD_CFNAME = "DroitPrmd";

   public static final int MAX_ATTRIBUTS = 100;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public PrmdDao(Keyspace keyspace) {
      super(keyspace);

   }

   /**
    * ajoute une colonne {@value #PRMD_DESCRIPTION}
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

      addColumn(updater, PRMD_DESCRIPTION, value, StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PRMD_LUCENE}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritLucene(ColumnFamilyUpdater<String, String> updater,
         String value, long clock) {

      addColumn(updater, PRMD_LUCENE, value, StringSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PRMD_METADATA}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritMetaData(ColumnFamilyUpdater<String, String> updater,
         Map<String, List<String>> value, long clock) {

      addColumn(updater, PRMD_METADATA, value, MapSerializer.get(), clock);

   }

   /**
    * ajoute une colonne {@value #PRMD_BEAN}
    * 
    * @param updater
    *           updater de <code>DroitActionUnitaire</code>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void ecritBean(
         ColumnFamilyUpdater<String, String> updater, String value, long clock) {

      addColumn(updater, PRMD_BEAN, value, StringSerializer.get(), clock);

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final String getColumnFamilyName() {
      return PRMD_CFNAME;
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
