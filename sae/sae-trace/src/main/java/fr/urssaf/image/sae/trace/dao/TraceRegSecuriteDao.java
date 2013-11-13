/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.UUID;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Service DAO de la famille de colonnes "TraceRegSecurite"
 * 
 */
@Repository
public class TraceRegSecuriteDao extends AbstractTraceDao {

   /** nom de la Column Family */
   public static final String REG_SECURITE_CFNAME = "TraceRegSecurite";

   /** Contexte de l'événement */
   public static final String COL_CONTEXTE = "contexte";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceRegSecuriteDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * @return le nom de la CF
    */
   @Override
   public final String getColumnFamilyName() {
      return REG_SECURITE_CFNAME;
   }
   
   /**
    * ajoute une colonne {@value TraceRegSecuriteDao#COL_CONTEXTE}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnContexte(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CONTEXTE, value, StringSerializer.get(), clock);
   }
}
