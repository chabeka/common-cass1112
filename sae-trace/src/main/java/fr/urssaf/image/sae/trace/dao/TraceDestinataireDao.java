/**
 * 
 */
package fr.urssaf.image.sae.trace.dao;

import java.util.ArrayList;
import java.util.List;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.mutation.Mutator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;
import fr.urssaf.image.sae.trace.dao.serializer.ListSerializer;

/**
 * Service DAO de la famille de colonnes "TraceDestinataire"
 * 
 */
@Repository
public class TraceDestinataireDao extends AbstractDao<String, String> {


   public static final String DEST_CFNAME = "TraceDestinataire";

   public static final String COL_HIST_EVT = "HIST_EVENEMENT";
   public static final String COL_HIST_ARCHIVE = "HIST_ARCHIVE";
   public static final String COL_REG_SECURITE = "REG_SECURITE";
   public static final String COL_REG_EXPLOIT = "REG_EXPLOITATION";
   public static final String COL_REG_TECHNIQUE = "REG_TECHNIQUE";
   public static final String COL_JOURN_EVT = "JOURN_EVT";


   /**
    * Constructeur
    * 
    * @param keyspace
    *           Keyspace utilisé
    */
   @Autowired
   public TraceDestinataireDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * Ajoute une colonne {@value #COL_HIST_ARCHIVE} à la ligne donnée
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
    * Ajoute une colonne {@value #COL_HIST_EVT} à la ligne donnée
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
    * Ajoute une colonne {@value #COL_REG_EXPLOIT} à la ligne donnée
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
    * Ajoute une colonne {@value #COL_REG_SECURITE} à la ligne donnée
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
    * Ajoute une colonne {@value #COL_JOURN_EVT} à la ligne donnée
    * 
    * @param updater
    *           updater de <b>TraceDestinataire</b>
    * @param value
    *           valeur de la colonne
    * @param clock
    *           horloge de la colonne
    */
   public final void writeColumnJournalEvt(
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

      addColumn(updater, COL_JOURN_EVT, refValues, ListSerializer.get(), clock);
   }

   /**
    * Ajoute une colonne {@value #COL_REG_TECHNIQUE} à la ligne donnée
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

   @Override
   public String getColumnFamilyName() {
      return DEST_CFNAME;
   }

   @Override
   public Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   @Override
   public Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

}
