/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegTechniqueIndexSerializer;

/**
 * Service DAO de la famille de colonnes "TraceRegTechniqueIndex"
 * 
 */
@Repository
public class TraceRegTechniqueIndexDao extends
      AbstractTraceIndexDao<TraceRegTechniqueIndex> {

   public static final String REG_TECHNIQUE_INDEX_CFNAME = "TraceRegTechniqueIndex";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceRegTechniqueIndexDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * Méthode de suppression d'une ligne TraceRegTechniqueIndex
    * 
    * @param mutator
    *           Mutator de <code>TraceRegTechniqueIndex</code>
    * @param code
    *           identifiant de la ligne d'index
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionTraceRegTechniqueIndex(
         Mutator<String> mutator, String code, long clock) {

      mutatorSuppressionLigne(mutator, code, clock);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getColumnFamilyName() {
      return REG_TECHNIQUE_INDEX_CFNAME;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Serializer<TraceRegTechniqueIndex> getValueSerializer() {
      return TraceRegTechniqueIndexSerializer.get();
   }

}
