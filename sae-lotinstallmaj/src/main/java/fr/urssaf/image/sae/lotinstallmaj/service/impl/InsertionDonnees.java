/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.service.utils.DroitsServiceUtils;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.ReferentielServiceUtils;

/**
 * 
 * 
 */
public class InsertionDonnees {

   private static final Logger LOG = LoggerFactory
         .getLogger(InsertionDonnees.class);

   private static final String DISPO = "dispo";

   private final Keyspace keyspace;

   /**
    * Constructeur
    * 
    * @param keyspace
    *           le keyspace CASSANDRA
    */
   public InsertionDonnees(Keyspace keyspace) {
      this.keyspace = keyspace;
   }

   /**
    * Insertion de données de droits
    */
   public final void addDroits() {
      DroitsServiceUtils.addDroits(keyspace);
   }


   /**
    * Ajoute les paramètres nécéssaires à la maj du RND
    */
   public final void addRndParameters() {
      LOG.info("Création des paramètres de maj du RND");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Parameters", StringSerializer.get(),
            StringSerializer.get());

      checkAndAddRndParameter(cfTmpl, "VERSION_RND_NUMERO", "");
      checkAndAddRndParameter(cfTmpl, "VERSION_RND_DATE_MAJ", getRndDate());

   }

   /**
    * Ajoute les paramètres nécessaires à la traçabilité SAE
    */
   public final void addTracabiliteParameters() {

      LOG.info("Création des paramètres de traçabilité");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Parameters", StringSerializer.get(),
            StringSerializer.get());

      Date debutTracabilite = getTracabiliteDerniereDateTraitee();

      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_TECH_DUREE",
            Integer.valueOf(10));
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_SECU_DUREE",
            Integer.valueOf(10));
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EXPLOIT_DUREE",
            Integer.valueOf(10));
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EVT_DUREE",
            Integer.valueOf(10));

      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_TECH_IS_RUNNING",
            Boolean.FALSE);
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_SECU_IS_RUNNING",
            Boolean.FALSE);
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EXPLOIT_IS_RUNNING",
            Boolean.FALSE);
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EVT_IS_RUNNING",
            Boolean.FALSE);

      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_TECH_DATE",
            debutTracabilite);
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_SECU_DATE",
            debutTracabilite);
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EXPLOIT_DATE",
            debutTracabilite);
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EVT_DATE",
            debutTracabilite);

      checkAndAddTracabiliteParameter(cfTmpl, "JOURNALISATION_EVT_DATE",
            debutTracabilite);
      checkAndAddTracabiliteParameter(cfTmpl, "JOURNALISATION_EVT_IS_RUNNING",
            Boolean.FALSE);

      checkAndAddTracabiliteParameter(cfTmpl,
            "JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT",
            "00000000-0000-0000-0000-000000000000");
      checkAndAddTracabiliteParameter(cfTmpl,
            "JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT",
            "0000000000000000000000000000000000000000");

      addTracabiliteParameter(cfTmpl, "JOURNALISATION_EVT_META_TITRE",
            "Journal des événements SAE");
      addTracabiliteParameter(cfTmpl,
            "JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE", "SAE");
      addTracabiliteParameter(cfTmpl,
            "JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT", "SAE");
      addTracabiliteParameter(cfTmpl, "JOURNALISATION_EVT_META_CODE_ORGA",
            "UR750");
      addTracabiliteParameter(cfTmpl, "JOURNALISATION_EVT_META_CODE_RND",
            "7.7.8.8.1");

   }

   /**
    * Ajoute les paramètres nécessaires à la purge de la corbeille
    */
   public final void addCorbeilleParameters() {

      LOG.info("Création des paramètres de purge de la corbeille");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Parameters", StringSerializer.get(),
            StringSerializer.get());

      Date debutPurgeCorbeille = getCorbeilleDerniereDateTraitee();

      checkAndAddCorbeilleParameter(cfTmpl, "PURGE_CORBEILLE_DUREE",
            Integer.valueOf(20));

      checkAndAddCorbeilleParameter(cfTmpl, "PURGE_CORBEILLE_IS_RUNNING",
            Boolean.FALSE);

      checkAndAddCorbeilleParameter(cfTmpl, "PURGE_CORBEILLE_DATE_LANCEMENT",
            debutPurgeCorbeille);

      checkAndAddCorbeilleParameter(cfTmpl, "PURGE_CORBEILLE_DATE_SUCCES",
            debutPurgeCorbeille);

      checkAndAddCorbeilleParameter(cfTmpl, "PURGE_CORBEILLE_DATE_DEBUT_PURGE",
            debutPurgeCorbeille);

   }

   private void checkAndAddTracabiliteParameter(
         ColumnFamilyTemplate<String, String> cfTmpl, String subname,
         Object valeur) {

      boolean inserted = checkAndAddValue(cfTmpl, "parametresTracabilite",
            subname, valeur, StringSerializer.get(), ObjectSerializer.get());

      if (!inserted) {
         LOG.info("Le paramètre de traçabilité {} existe déjà", subname);
      }

   }

   private void checkAndAddRndParameter(
         ColumnFamilyTemplate<String, String> cfTmpl, String subname,
         Object valeur) {

      boolean inserted = checkAndAddValue(cfTmpl, "parametresRnd", subname,
            valeur, StringSerializer.get(), ObjectSerializer.get());

      if (!inserted) {
         LOG.info("Le paramètre de maj du RND {} existe déjà", subname);
      }

   }

   private void checkAndAddCorbeilleParameter(
         ColumnFamilyTemplate<String, String> cfTmpl, String subname,
         Object valeur) {

      boolean inserted = checkAndAddValue(cfTmpl, "parametresCorbeille",
            subname, valeur, StringSerializer.get(), ObjectSerializer.get());

      if (!inserted) {
         LOG.info("Le paramètre de corbeille {} existe déjà", subname);
      }

   }

   private void addTracabiliteParameter(
         ColumnFamilyTemplate<String, String> cfTmpl, String subname,
         Object valeur) {

      addValue(cfTmpl, "parametresTracabilite", subname, valeur,
            StringSerializer.get(), ObjectSerializer.get());

   }

   private <ROW, COL, VAL> void addValue(ColumnFamilyTemplate<ROW, COL> cfTmpl,
         ROW rowName, COL colName, VAL value, Serializer<COL> colSerializer,
         Serializer<VAL> valSerializer) {

      ColumnFamilyUpdater<ROW, COL> updater = cfTmpl.createUpdater(rowName);
      HColumn<COL, VAL> column = HFactory.createColumn(colName, value,
            colSerializer, valSerializer);
      updater.setColumn(column);
      cfTmpl.update(updater);
      LOG.info("Ecriture du paramètre de traçabilité {}", colName);

   }

   private <ROW, COL, VAL> boolean checkAndAddValue(
         ColumnFamilyTemplate<ROW, COL> cfTmpl, ROW rowName, COL colName,
         VAL value, Serializer<COL> colSerializer, Serializer<VAL> valSerializer) {

      boolean inserted = false;

      Collection<COL> columnNames = cfTmpl.queryColumns(rowName)
            .getColumnNames();

      if (!columnNames.contains(colName)) {
         addValue(cfTmpl, rowName, colName, value, colSerializer, valSerializer);
         inserted = true;
      }

      return inserted;

   }

   private Date getTracabiliteDerniereDateTraitee() {

      // On démarre la traçabilité SAE au 01/02/2013
      // La dernière date des traitements est positionné au 31/01/2013
      // Car les traitements font un J+1 à partir des valeurs de paramètres

      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, 2013);
      calendar.set(Calendar.MONTH, 0); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH, 31);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar.getTime();

   }

   private Date getRndDate() {

      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, 1970);
      calendar.set(Calendar.MONTH, 0); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar.getTime();

   }

   private Date getCorbeilleDerniereDateTraitee() {

      // La dernière date des traitements est positionné au 01/06/2016

      Calendar calendar = Calendar.getInstance();
      calendar.set(Calendar.YEAR, 2016);
      calendar.set(Calendar.MONTH, 5); // les numéros de mois commencent à 0
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      return calendar.getTime();

   }


   /**
    * Ajout de la colonne 'dispo' pour chaque metadonnée fournit en paramètre.
    * 
    * @param listeRows
    *           liste des codes long des métadonnée
    */
   public final void addColumnClientAvailableMetadata(List<String> listeRows) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Metadata", StringSerializer.get(),
            StringSerializer.get());

      for (String rowName : listeRows) {
         ColumnFamilyUpdater<String, String> updater = cfTmpl
               .createUpdater(rowName);

         Collection<String> columnNames = cfTmpl.queryColumns(rowName)
               .getColumnNames();

         if (!columnNames.contains(DISPO)) {
            LOG.info("Ajout de la colonne {} pour la métadonnée {}", DISPO,
                  rowName);
            HColumn<String, Boolean> column = HFactory
                  .createColumn(DISPO, Boolean.TRUE, StringSerializer.get(),
                        BooleanSerializer.get());
            updater.setColumn(column);
            cfTmpl.update(updater);
         } else {
            LOG.info("Le colonne {} existe déjà pour la métadonnée {} ", DISPO,
                  rowName);
         }

      }
   }

   /**
    * Methode permettant de mettre à jour le referentiel Version 3.
    */
   public final void addReferentielEvenementV3() {
      ReferentielServiceUtils.addReferentielEvenementV3(keyspace);
   }

   /**
    * Methode permettant de mettre à jour le referentiel Version 1.
    */
   public final void addReferentielEvenementV1() {
      ReferentielServiceUtils.addReferentielEvenementV1(keyspace);
   }

   /**
    * Methode permettant de mettre à jour le referentiel Version 2.
    */
   public final void addReferentielEvenementV2() {
      ReferentielServiceUtils.addReferentielEvenementV2(keyspace);
   }

   /**
    * Methode permettant d'ajouter le referentiel des formats.
    */
   public final void addReferentielFormat() {
      ReferentielServiceUtils.addReferentielFormat(keyspace);
   }

   /**
    * Methode permettant d'ajouter le format control profil.
    */
   public final void addFormatControleProfil() {
      ReferentielServiceUtils.addFormatControleProfil(keyspace);
   }

   /**
    * Methode permettant de mettre à jour le referentiel Version 4.
    */
   public final void addReferentielEvenementV4() {
      ReferentielServiceUtils.addReferentielEvenementV4(keyspace);
   }

   /**
    * Methode permettant de mettre à jour le referentiel Version 5.
    */
   public final void addReferentielEvenementV5() {
      ReferentielServiceUtils.addReferentielEvenementV5(keyspace);
   }

   /**
    * Methode permettant d'ajouter le referentiel de format V2.
    */
   public final void addReferentielFormatV2() {
      ReferentielServiceUtils.addReferentielFormatV2(keyspace);
   }

   /**
    * Methode permettant d'ajouter le referentiel d'evenement V6.
    */
   public final void addReferentielEvenementV6() {
      ReferentielServiceUtils.addReferentielEvenementV6(keyspace);
   }

   /**
    * Methode permettant d'ajouter le referentiel d'evenement V7.
    */
   public final void addReferentielEvenementV7() {
      ReferentielServiceUtils.addReferentielEvenementV7(keyspace);
   }

   /**
    * Methode permettant d'ajouter l'action unitaire Note.
    */
   public final void addActionUnitaireNote() {
      DroitsServiceUtils.addActionUnitaireNote(keyspace);
   }

   /**
    * Methode permettant d'ajouter l'action unitaire pour la recherche par
    * itérateur.
    */
   public final void addActionUnitaireRechercheParIterateur() {
      DroitsServiceUtils.addActionUnitaireRechercheParIterateur(keyspace);
   }

   /**
    * Methode permettant d'ajouter l'action unitaire pour l'ajout de note.
    */
   public final void modifyActionUnitaireAjoutNote() {
      DroitsServiceUtils.modifyActionUnitaireAjoutNote(keyspace);
   }

   /**
    * Methode permettant de modifier le référentiel pour le format fmt 354.
    */
   public final void modifyReferentielFormatFmt354() {
      ReferentielServiceUtils.modifyReferentielFormatFmt354(keyspace);
   }

   /**
    * Methode permettant d'ajouter une action unitaire pour les Note.
    */
   public final void addActionUnitaireNote2() {
      DroitsServiceUtils.addActionUnitaireNote2(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 3 des référentiels de format.
    */
   public final void addReferentielFormatV3() {
      ReferentielServiceUtils.addReferentielFormatV3(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 8 des référentiels d'événement.
    */
   public final void addReferentielEvenementV8() {
      ReferentielServiceUtils.addReferentielEvenementV8(keyspace);
   }

   /**
    * Methode permettant d'ajouter une action unitaire pour les documents
    * attachés.
    */
   public final void addActionUnitaireAjoutDocAttache() {
      DroitsServiceUtils.addActionUnitaireAjoutDocAttache(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 4 des référentiels de format.
    */
   public final void addReferentielFormatV4() {
      ReferentielServiceUtils.addReferentielFormatV4(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 9 des référentiels d'événement.
    */
   public final void addReferentielEvenementV9() {
      ReferentielServiceUtils.addReferentielEvenementV9(keyspace);
   }

   /**
    * Methode permettant d'ajouter une action unitaire pour les traitements de
    * masse.
    */
   public final void addActionUnitaireTraitementMasse() {
      DroitsServiceUtils.addActionUnitaireTraitementMasse(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 10 des référentiels d'événement.
    */
   public final void addReferentielEvenementV10() {
      ReferentielServiceUtils.addReferentielEvenementV10(keyspace);

   }

   /**
    * Methode permettant d'ajouter la version 11 des référentiels d'événement.
    */
   public final void addReferentielEvenementV11() {
      ReferentielServiceUtils.addReferentielEvenementV11(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 5 des référentiels des formats.
    */
   public final void addReferentielFormatV5() {
      ReferentielServiceUtils.addReferentielFormatV5(keyspace);
   }

   /**
    * Methode permettant d'ajouter une action unitaire pour la suppression et la
    * modification.
    */
   public final void addActionUnitaireSuppressionModification() {
      DroitsServiceUtils.addActionUnitaireSuppressionModification(keyspace);
   }

   /**
    * Methode permettant d'ajouter une action unitaire pour la copie.
    */
   public final void addActionUnitaireCopie() {
      DroitsServiceUtils.addActionUnitaireCopie(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 6 des référentiels des formats.
    */
   public final void addReferentielFormatV6() {
      ReferentielServiceUtils.addReferentielFormatV6(keyspace);
   }

   /**
    * Methode permettant de modifier le format fmt/353.
    */
   public final void modifyReferentielFormatFmt353() {
      ReferentielServiceUtils.modifyReferentielFormatFmt353(keyspace);
   }

   /**
    * Methode permettant de modifier le format fmt/44.
    */
   public final void modifyReferentielFormatFmt44() {
      ReferentielServiceUtils.modifyReferentielFormatFmt44(keyspace);
   }

   /**
    * Methode permettant d'ajouter la version 6 bis des référentiels des
    * formats.
    */
   public final void addReferentielFormatV6Bis() {
      ReferentielServiceUtils.addReferentielFormatV6Bis(keyspace);
   }

   /**
    * Methode permettant de
    */
   public final void modifyReferentielFormatCrtl1() {
      ReferentielServiceUtils.modifyReferentielFormatCrtl1(keyspace);
   }

   /**
    * Methode permettant d'ajouter le colonne autorisé en GED pour le
    * référentiel des formats.
    */
   public final void addColumnAutoriseGEDReferentielFormat() {
      ReferentielServiceUtils.addColumnAutoriseGEDReferentielFormat(keyspace);
   }

   /**
    * Methode permettant d'ajouter les droits GED.
    */
   public final void addDroitsGed() {
      DroitsServiceUtils.addDroitsGed(keyspace);
   }

   public void addActionUnitaireTraitementMasse2() {
      LOG.info("Mise à jour des actions unitaires");
      DroitsServiceUtils.addActionUnitaireTraitementMasseBis(keyspace);
   }

   /**
    * Référentiel des événements en V12 Ajout des évenements : <li>
    * WS_MODIFICATION_MASSE|KO</li> <li>MODIFICATION_MASSE|KO</li>
    */
   public void addReferentielEvenementV12() {
      LOG.info("Mise à jour du référentiel des événements");
      ReferentielServiceUtils.addReferentielEvenementV12(keyspace);
   }

}
