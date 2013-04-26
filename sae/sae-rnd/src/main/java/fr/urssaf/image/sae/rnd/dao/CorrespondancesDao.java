package fr.urssaf.image.sae.rnd.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;

import java.util.Date;

import fr.urssaf.image.sae.commons.dao.AbstractDao;


/**
 * Manipulation de la CF CorrespondancesRnd
 * 
 * 
 */
@Repository
public class CorrespondancesDao extends AbstractDao<String, String> {

   public static final String CORR_CODE_DEFINITIF = "codeDefinitif";
   public static final String CORR_ETAT = "etat";
   public static final String CORR_DATE_DEBUT_MAJ = "dateDebutMaj";
   public static final String CORR_DATE_FIN_MAJ = "dateFinMaj";

   public static final String CORRESPONDANCES_CFNAME = "CorrespondancesRnd";

   /**
    * Contructeur
    * 
    * @param keyspace
    *           {@link Keyspace}
    */
   @Autowired
   public CorrespondancesDao(Keyspace keyspace) {
      super(keyspace);
   }

   /**
    * @return le nom de la CF
    */
   @Override
   public final String getColumnFamilyName() {
      return CORRESPONDANCES_CFNAME;
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
   public final Serializer<String> getRowKeySerializer() {
      return StringSerializer.get();
   }

   /**
    * Ecrit le code définitif
    * 
    * @param code
    *           Le code à écrire
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritCodeDefinitif(String code,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, CORR_CODE_DEFINITIF, code, StringSerializer.get(),
            clock);
   }

   /**
    * Ecrit le code l'état
    * 
    * @param etat
    *           L'état à écrire
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritEtat(String etat,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, CORR_ETAT, etat, StringSerializer.get(), clock);
   }

   /**
    * Ecrit la date de début de MAJ
    * 
    * @param dateDebut
    *           La date à écrire
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritDateDebutMaj(Date dateDebut,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, CORR_DATE_DEBUT_MAJ, dateDebut, DateSerializer.get(),
            clock);
   }

   /**
    * Ecrit la date de fin de MAJ
    * 
    * @param dateFin
    *           La date à écrire
    * @param updater
    *           {@link ColumnFamilyUpdater}
    * @param clock
    *           le timestamp d'écriture
    */
   public final void ecritDateFinMaj(Date dateFin,
         ColumnFamilyUpdater<String, String> updater, long clock) {
      addColumn(updater, CORR_DATE_FIN_MAJ, dateFin, DateSerializer.get(),
            clock);
   }

}
