/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.ArrayList;
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

   private final ColumnFamilyTemplate<String, String> destTmpl;

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

      destTmpl = new ThriftColumnFamilyTemplate<String, String>(keyspace,
            DEST_CFNAME, StringSerializer.get(), StringSerializer.get());

      destTmpl.setCount(MAX_ATTRIBUTS);
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

      // bien que la liste soit nulle, on veut quand meme que la trace soit
      // présente dans les registres, on la met donc à vide
      List<String> refValues;
      if (value == null) {
         refValues = new ArrayList<String>();
      } else {
         refValues = value;
      }

      addColumn(updater, COL_HIST_ARCHIVE, refValues, ListSerializer.get(),
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

      // bien que la liste soit nulle, on veut quand meme que la trace soit
      // présente dans les registres, on la met donc à vide
      List<String> refValues;
      if (value == null) {
         refValues = new ArrayList<String>();
      } else {
         refValues = value;
      }

      addColumn(updater, COL_HIST_EVT, refValues, ListSerializer.get(), clock);
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
      // bien que la liste soit nulle, on veut quand meme que la trace soit
      // présente dans les registres, on la met donc à vide
      List<String> refValues;
      if (value == null) {
         refValues = new ArrayList<String>();
      } else {
         refValues = value;
      }

      addColumn(updater, COL_REG_EXPLOIT, refValues, ListSerializer.get(),
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
      // bien que la liste soit nulle, on veut quand meme que la trace soit
      // présente dans les registres, on la met donc à vide
      List<String> refValues;
      if (value == null) {
         refValues = new ArrayList<String>();
      } else {
         refValues = value;
      }

      addColumn(updater, COL_REG_SECURITE, refValues, ListSerializer.get(),
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
      // bien que la liste soit nulle, on veut quand meme que la trace soit
      // présente dans les registres, on la met donc à vide
      List<String> refValues;
      if (value == null) {
         refValues = new ArrayList<String>();
      } else {
         refValues = value;
      }

      addColumn(updater, COL_REG_TECHNIQUE, refValues, ListSerializer.get(),
            clock);
   }

   /**
    * Méthode de suppression d'une ligne TraceDestinataire
    * 
    * @param mutator
    *           Mutator de <code>TraceDestinataire</code>
    * @param code
    *           identifiant de la trace
    * @param clock
    *           horloge de la suppression
    */
   public final void mutatorSuppressionDestinataire(Mutator<String> mutator,
         String code, long clock) {

      mutator.addDeletion(code, DEST_CFNAME, clock);
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

   /**
    * @return le CassandraTemplate de <code>TraceDestinataire</code>
    */
   public final ColumnFamilyTemplate<String, String> getDestTmpl() {
      return destTmpl;
   }

}
