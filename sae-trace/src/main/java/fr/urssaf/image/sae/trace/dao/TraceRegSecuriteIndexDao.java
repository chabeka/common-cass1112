/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegSecuriteIndexSerializer;

/**
 * Service DAO de la famille de colonnes "TraceRegSecuriteIndex"
 * 
 */
@Repository
public class TraceRegSecuriteIndexDao extends
      AbstractTraceIndexDao<TraceRegSecuriteIndex> {

   public static final String REG_SECURITE_INDEX_CFNAME = "TraceRegSecuriteIndex";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisés
    */
   @Autowired
   public TraceRegSecuriteIndexDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * Méthode de suppression d'une ligne TraceRegSecuriteIndex
    * 
    * @param mutator
    *           Mutator de <code>TraceRegSecuriteIndex</code>
    * @param code
    *           identifiant de la ligne d'index
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionTraceRegSecuriteIndex(
         Mutator<String> mutator, String code, long clock) {

      mutatorSuppressionLigne(mutator, code, clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getColumnFamilyName() {
      return REG_SECURITE_INDEX_CFNAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Serializer<TraceRegSecuriteIndex> getValueSerializer() {
      return TraceRegSecuriteIndexSerializer.get();
   }
}
