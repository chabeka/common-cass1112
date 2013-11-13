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
 * Service DAO de la famille de colonnes "TraceRegTechnique"
 * 
 */
@Repository
public class TraceRegTechniqueDao extends AbstractTraceDao {

   /** Contexte de l'événement */
   public static final String COL_CONTEXTE = "contexte";

   /** trace d'erreur */
   public static final String COL_STACKTRACE = "stacktrace";

   /** nom de la Column Family */
   public static final String REG_TECHNIQUE_CFNAME = "TraceRegTechnique";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceRegTechniqueDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * @return le nom de la CF
    */
   @Override
   public final String getColumnFamilyName() {
      return REG_TECHNIQUE_CFNAME;
   }

   /**
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_CONTEXTE}
    * 
    * @param updater
    *           updater de <b>TraceRegTechnique</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnContexte(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CONTEXTE, value, StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceRegTechniqueDao#COL_STACKTRACE}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnStackTrace(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_STACKTRACE, value, StringSerializer.get(), clock);
   }

}
