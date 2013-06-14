/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;

/**
 * Service DAO de la famille de colonnes "TraceRegSecurite"
 * 
 */
@Repository
public class TraceRegSecuriteDao extends AbstractDao<UUID, String>{

   public static final String REG_SECURITE_CFNAME = "TraceRegSecurite";

   /** Date de création de la trace */
   public static final String COL_TIMESTAMP = "timestamp";

   /** Contexte de l'événement */
   public static final String COL_CONTEXTE = "contexte";

   /** code de l'événement */
   public static final String COL_CODE_EVT = "codeEvt";

   /** code du contrat de service */
   public static final String COL_CONTRAT_SERVICE = "cs";

   /** Le ou les PAGMS */
   public static final String COL_PAGMS = "pagms";

   /** identifiant utilisateur */
   public static final String COL_LOGIN = "login";

   /** informations supplémentaires */
   public static final String COL_INFOS = "infos";

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
    * @return le sérializer d'une colonne
    */
   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * @return le sérializer de la clé d'une ligne
    */
   @Override
   public final Serializer<UUID> getRowKeySerializer() {
      return UUIDSerializer.get();
   }

   /**
    * ajoute une colonne {@value TraceRegSecuriteDao#COL_TIMESTAMP}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnTimestamp(
         ColumnFamilyUpdater<UUID, String> updater, Date value, long clock) {
      addColumn(updater, COL_TIMESTAMP, value, DateSerializer.get(), clock);
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

   /**
    * ajoute une colonne {@value TraceRegSecuriteDao#COL_CODE_EVT}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnCodeEvt(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CODE_EVT, value, StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceRegSecuriteDao#COL_CONTRAT_SERVICE}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnContratService(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_CONTRAT_SERVICE, value, StringSerializer.get(),
            clock);
   }

   /**
    * ajoute une colonne {@value TraceRegSecuriteDao#COL_PAGMS}
    * 
    * @param updater
    *           updater de <b>TraceRegExploitation</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnPagms(
         ColumnFamilyUpdater<UUID, String> updater, List<String> value,
         long clock) {
      addColumn(updater, COL_PAGMS, value, ListSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceRegSecuriteDao#COL_LOGIN}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnLogin(
         ColumnFamilyUpdater<UUID, String> updater, String value, long clock) {
      addColumn(updater, COL_LOGIN, value, StringSerializer.get(), clock);
   }

   /**
    * ajoute une colonne {@value TraceRegSecuriteDao#COL_INFOS}
    * 
    * @param updater
    *           updater de <b>TraceRegSecurite</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnInfos(
         ColumnFamilyUpdater<UUID, String> updater, Map<String, Object> value,
         long clock) {
      addColumn(updater, COL_INFOS, value, MapSerializer.get(), clock);
   }

}
