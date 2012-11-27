/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.List;

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

import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;

/**
 * Service DAO de la famille de colonnes "TraceDestinataire"
 * 
 */
@Repository
public class TraceDestinataireDao {

   private static final int MAX_ATTRIBUTS = 100;
   public static final String DEST_CFNAME = "TraceDestinataire";

   public static final String COL_HIST_EVT = "HIST_EVENEMENT";
   public static final String COL_HIST_ARCHIVE = "HIST_ARCHIVE";
   public static final String COL_REG_SECURITE = "REG_SECURITE";
   public static final String COL_REG_EXPLOIT = "REG_EXPLOITATION";
   public static final String COL_REG_TECHNIQUE = "REG_TECHNIQUE";

   private final ColumnFamilyTemplate<String, String> pagmTmpl;

   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé par la pile des travaux
    */
   @Autowired
   public TraceDestinataireDao(Keyspace keyspace) {

      this.keyspace = keyspace;

      pagmTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            DEST_CFNAME, StringSerializer.get(), StringSerializer.get());

      pagmTmpl.setCount(MAX_ATTRIBUTS);
   }

   @SuppressWarnings("unchecked")
   private void addColumn(ColumnFamilyUpdater<String, String> updater,
         String colName, Object value, Serializer valueSerializer, long clock) {

      HColumn<String, Object> column = HFactory.createColumn(colName, value,
            StringSerializer.get(), valueSerializer);

      column.setClock(clock);
      updater.setColumn(column);

   }

   /**
    * Ajoute une colonne {@value TraceDestinataireDao#COL_HIST_ARCHIVE} à la
    * ligne donnée
    * 
    * @param updater
    *           updater de <b>TraceDestinataire</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnHistArchive(
         ColumnFamilyUpdater<String, String> updater, List<String> value,
         long clock) {
      addColumn(updater, COL_HIST_ARCHIVE, value, ListSerializer.get(),
            clock);
   }

   /**
    * Ajoute une colonne {@value TraceDestinataireDao#COL_HIST_EVT} à la ligne
    * donnée
    * 
    * @param updater
    *           updater de <b>TraceDestinataire</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnHistEvt(
         ColumnFamilyUpdater<String, String> updater, List<String> value,
         long clock) {
      addColumn(updater, COL_HIST_EVT, value, ListSerializer.get(),
            clock);
   }

   /**
    * Ajoute une colonne {@value TraceDestinataireDao#COL_REG_EXPLOIT} à la
    * ligne donnée
    * 
    * @param updater
    *           updater de <b>TraceDestinataire</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnRegExploit(
         ColumnFamilyUpdater<String, String> updater, List<String> value,
         long clock) {
      addColumn(updater, COL_REG_EXPLOIT, value, ListSerializer.get(),
            clock);
   }

   /**
    * Ajoute une colonne {@value TraceDestinataireDao#COL_REG_SECURITE} à la
    * ligne donnée
    * 
    * @param updater
    *           updater de <b>TraceDestinataire</b> <li></li> <li>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnRegSecurite(
         ColumnFamilyUpdater<String, String> updater, List<String> value,
         long clock) {
      addColumn(updater, COL_REG_SECURITE, value, ListSerializer.get(),
            clock);
   }

   /**
    * Ajoute une colonne {@value TraceDestinataireDao#COL_REG_TECHNIQUE} à la
    * ligne donnée
    * 
    * @param updater
    *           updater de <b>TraceDestinataire</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnRegTechnique(
         ColumnFamilyUpdater<String, String> updater, List<String> value,
         long clock) {
      addColumn(updater, COL_REG_TECHNIQUE, value,
            ListSerializer.get(), clock);
   }

   /**
    * 
    * @return Mutator de <code>TraceDestinataire</code>
    */
   public final Mutator<String> createMutator() {

      Mutator<String> mutator = HFactory.createMutator(keyspace,
            StringSerializer.get());

      return mutator;

   }
}
