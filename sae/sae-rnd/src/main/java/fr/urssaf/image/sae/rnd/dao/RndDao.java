package fr.urssaf.image.sae.rnd.dao;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.IntegerSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.commons.dao.AbstractDao;

/**
 * Manipulation de la CF Rnd
 * 
 * 
 */
@Repository
public class RndDao extends AbstractDao<String, String> {

   public static final String RND_CFNAME = "Rnd";

   public static final String RND_CODE_FONCTION = "codeFonction";
   public static final String RND_CODE_ACTIVITE = "codeActivite";
   public static final String RND_LIBELLE = "libelleEnd";
   public static final String RND_DUREE_CONSERVATION = "dureeConservation";
   public static final String RND_CLOTURE = "cloture";
   public static final String RND_TYPE = "type";

   /**
    * Constructeur
    * 
    * @param keyspace
    *           {@link Keyspace}
    */
   @Autowired
   public RndDao(Keyspace keyspace) {
      super(keyspace);
   }

   @Override
   public final String getColumnFamilyName() {
      return RND_CFNAME;
   }

   @Override
   public final Serializer<String> getColumnKeySerializer() {
      return StringSerializer.get();
   }

   @Override
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * Ecriture du code fonction
    * 
    * @param codeFonction
    *           le code fonction
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritCodeFonction(Integer codeFonction,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, RND_CODE_FONCTION, codeFonction, IntegerSerializer
            .get(), clock);
   }

   /**
    * Ecriture du code activité
    * 
    * @param codeActivite
    *           le code activité
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritCodeActivite(Integer codeActivite,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, RND_CODE_ACTIVITE, codeActivite, IntegerSerializer
            .get(), clock);
   }

   /**
    * Ecriture du libellé
    * 
    * @param libelle
    *           le libellé
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritLibelle(String libelle,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, RND_LIBELLE, libelle, StringSerializer.get(), clock);
   }

   /**
    * Ecriture de la durée de conservation
    * 
    * @param duree
    *           la durée de conservation
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritDureeConservation(Integer duree,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, RND_DUREE_CONSERVATION, duree,
            IntegerSerializer.get(), clock);
   }

   /**
    * Ecriture cloture
    * 
    * @param cloture
    *           clôture
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritCloture(Boolean cloture,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, RND_CLOTURE, cloture, BooleanSerializer.get(), clock);
   }

   /**
    * Ecriture du type
    * 
    * @param type
    *           le type
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritType(String type,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, RND_TYPE, type, StringSerializer.get(), clock);
   }

}
