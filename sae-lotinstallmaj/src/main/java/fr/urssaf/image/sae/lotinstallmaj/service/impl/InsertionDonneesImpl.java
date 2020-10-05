/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.InsertionDonnees;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.DroitsServiceUtils;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.ReferentielServiceUtils;
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

/**
 * Implementation Thrift pour l'insertion des données en base SAE
 */
@Service
@Qualifier("InsertionDonneesImpl")
public class InsertionDonneesImpl implements InsertionDonnees {

   private static final Logger LOG = LoggerFactory
         .getLogger(InsertionDonneesImpl.class);

   private static final String DISPO = "dispo";

   private Keyspace keyspace;

   @Override
   public final void addDroits() {
      DroitsServiceUtils.addDroits(keyspace);
   }

   @Override
   public final void addRndParameters() {
      LOG.info("Création des paramètres de maj du RND");

      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, "Parameters", StringSerializer.get(),
            StringSerializer.get());

      checkAndAddRndParameter(cfTmpl, "VERSION_RND_NUMERO", "");
      checkAndAddRndParameter(cfTmpl, "VERSION_RND_DATE_MAJ", getRndDate());

   }

   @Override
   public final void addTracabiliteParameters() {

      LOG.info("Création des paramètres de traçabilité");

      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, "Parameters", StringSerializer.get(),
            StringSerializer.get());

      final Date debutTracabilite = getTracabiliteDerniereDateTraitee();

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

   @Override
   public final void addCorbeilleParameters() {

      LOG.info("Création des paramètres de purge de la corbeille");

      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, "Parameters", StringSerializer.get(),
            StringSerializer.get());

      final Date debutPurgeCorbeille = getCorbeilleDerniereDateTraitee();

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
                                                final ColumnFamilyTemplate<String, String> cfTmpl, final String subname,
                                                final Object valeur) {

      final boolean inserted = checkAndAddValue(cfTmpl, "parametresTracabilite",
                                                subname, valeur, StringSerializer.get(), ObjectSerializer.get());

      if (!inserted) {
         LOG.info("Le paramètre de traçabilité {} existe déjà", subname);
      }

   }

   private void checkAndAddRndParameter(
                                        final ColumnFamilyTemplate<String, String> cfTmpl, final String subname,
                                        final Object valeur) {

      final boolean inserted = checkAndAddValue(cfTmpl, "parametresRnd", subname,
                                                valeur, StringSerializer.get(), ObjectSerializer.get());

      if (!inserted) {
         LOG.info("Le paramètre de maj du RND {} existe déjà", subname);
      }

   }

   private void checkAndAddCorbeilleParameter(
                                              final ColumnFamilyTemplate<String, String> cfTmpl, final String subname,
                                              final Object valeur) {

      final boolean inserted = checkAndAddValue(cfTmpl, "parametresCorbeille",
                                                subname, valeur, StringSerializer.get(), ObjectSerializer.get());

      if (!inserted) {
         LOG.info("Le paramètre de corbeille {} existe déjà", subname);
      }

   }

   private void addTracabiliteParameter(
                                        final ColumnFamilyTemplate<String, String> cfTmpl, final String subname,
                                        final Object valeur) {

      addValue(cfTmpl, "parametresTracabilite", subname, valeur,
               StringSerializer.get(), ObjectSerializer.get());

   }

   private <ROW, COL, VAL> void addValue(final ColumnFamilyTemplate<ROW, COL> cfTmpl,
                                         final ROW rowName, final COL colName, final VAL value, final Serializer<COL> colSerializer,
                                         final Serializer<VAL> valSerializer) {

      final ColumnFamilyUpdater<ROW, COL> updater = cfTmpl.createUpdater(rowName);
      final HColumn<COL, VAL> column = HFactory.createColumn(colName, value,
                                                             colSerializer, valSerializer);
      updater.setColumn(column);
      cfTmpl.update(updater);
      LOG.info("Ecriture du paramètre de traçabilité {}", colName);

   }

   private <ROW, COL, VAL> boolean checkAndAddValue(
                                                    final ColumnFamilyTemplate<ROW, COL> cfTmpl, final ROW rowName, final COL colName,
                                                    final VAL value, final Serializer<COL> colSerializer, final Serializer<VAL> valSerializer) {

      boolean inserted = false;

      final Collection<COL> columnNames = cfTmpl.queryColumns(rowName)
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

      final Calendar calendar = Calendar.getInstance();
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

      final Calendar calendar = Calendar.getInstance();
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

      final Calendar calendar = Calendar.getInstance();
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
   public final void addColumnClientAvailableMetadata(final List<String> listeRows) {
      final ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<>(
            keyspace, "Metadata", StringSerializer.get(),
            StringSerializer.get());

      for (final String rowName : listeRows) {
         final ColumnFamilyUpdater<String, String> updater = cfTmpl
               .createUpdater(rowName);

         final Collection<String> columnNames = cfTmpl.queryColumns(rowName)
               .getColumnNames();

         if (!columnNames.contains(DISPO)) {
            LOG.info("Ajout de la colonne {} pour la métadonnée {}", DISPO,
                     rowName);
            final HColumn<String, Boolean> column = HFactory
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

   @Override
   public final void addReferentielEvenementV3() {
      ReferentielServiceUtils.addReferentielEvenementV3(keyspace);
   }

   @Override
   public final void addReferentielEvenementV1() {
      ReferentielServiceUtils.addReferentielEvenementV1(keyspace);
   }

   @Override
   public final void addReferentielEvenementV2() {
      ReferentielServiceUtils.addReferentielEvenementV2(keyspace);
   }

   @Override
   public final void addReferentielFormat() {
      ReferentielServiceUtils.addReferentielFormat(keyspace);
   }

   @Override
   public final void addFormatControleProfil() {
      ReferentielServiceUtils.addFormatControleProfil(keyspace);
   }

   @Override
   public final void addReferentielEvenementV4() {
      ReferentielServiceUtils.addReferentielEvenementV4(keyspace);
   }

   @Override
   public final void addReferentielEvenementV5() {
      ReferentielServiceUtils.addReferentielEvenementV5(keyspace);
   }

   @Override
   public final void addReferentielFormatV2() {
      ReferentielServiceUtils.addReferentielFormatV2(keyspace);
   }

   @Override
   public final void addReferentielEvenementV6() {
      ReferentielServiceUtils.addReferentielEvenementV6(keyspace);
   }

   @Override
   public final void addReferentielEvenementV7() {
      ReferentielServiceUtils.addReferentielEvenementV7(keyspace);
   }

   @Override
   public final void addActionUnitaireNote() {
      DroitsServiceUtils.addActionUnitaireNote(keyspace);
   }

   @Override
   public final void addActionUnitaireRechercheParIterateur() {
      DroitsServiceUtils.addActionUnitaireRechercheParIterateur(keyspace);
   }

   @Override
   public final void modifyActionUnitaireAjoutNote() {
      DroitsServiceUtils.modifyActionUnitaireAjoutNote(keyspace);
   }

   @Override
   public final void modifyReferentielFormatFmt354() {
      ReferentielServiceUtils.modifyReferentielFormatFmt354(keyspace);
   }

   @Override
   public final void addActionUnitaireNote2() {
      DroitsServiceUtils.addActionUnitaireNote2(keyspace);
   }

   @Override
   public final void addReferentielFormatV3() {
      ReferentielServiceUtils.addReferentielFormatV3(keyspace);
   }

   @Override
   public final void addReferentielEvenementV8() {
      ReferentielServiceUtils.addReferentielEvenementV8(keyspace);
   }

   @Override
   public final void addActionUnitaireAjoutDocAttache() {
      DroitsServiceUtils.addActionUnitaireAjoutDocAttache(keyspace);
   }

   @Override
   public final void addReferentielFormatV4() {
      ReferentielServiceUtils.addReferentielFormatV4(keyspace);
   }

   @Override
   public final void addReferentielEvenementV9() {
      ReferentielServiceUtils.addReferentielEvenementV9(keyspace);
   }

   @Override
   public final void addActionUnitaireTraitementMasse() {
      DroitsServiceUtils.addActionUnitaireTraitementMasse(keyspace);
   }

   @Override
   public final void addReferentielEvenementV10() {
      ReferentielServiceUtils.addReferentielEvenementV10(keyspace);

   }

   @Override
   public final void addReferentielEvenementV11() {
      ReferentielServiceUtils.addReferentielEvenementV11(keyspace);
   }

   @Override
   public final void addReferentielFormatV5() {
      ReferentielServiceUtils.addReferentielFormatV5(keyspace);
   }

   @Override
   public final void addActionUnitaireSuppressionModification() {
      DroitsServiceUtils.addActionUnitaireSuppressionModification(keyspace);
   }

   @Override
   public final void addActionUnitaireCopie() {
      DroitsServiceUtils.addActionUnitaireCopie(keyspace);
   }

   @Override
   public final void addReferentielFormatV6() {
      ReferentielServiceUtils.addReferentielFormatV6(keyspace);
   }

   @Override
   public final void modifyReferentielFormatFmt353() {
      ReferentielServiceUtils.modifyReferentielFormatFmt353(keyspace);
   }

   @Override
   public final void modifyReferentielFormatFmt44() {
      ReferentielServiceUtils.modifyReferentielFormatFmt44(keyspace);
   }

   @Override
   public final void addReferentielFormatV6Bis() {
      ReferentielServiceUtils.addReferentielFormatV6Bis(keyspace);
   }

   @Override
   public final void modifyReferentielFormatCrtl1() {
      ReferentielServiceUtils.modifyReferentielFormatCrtl1(keyspace);
   }

   @Override
   public final void addColumnAutoriseGEDReferentielFormat() {
      ReferentielServiceUtils.addColumnAutoriseGEDReferentielFormat(keyspace);
   }

   @Override
   public final void addDroitsGed() {
      DroitsServiceUtils.addDroitsGed(keyspace);
   }

   @Override
   public void addActionUnitaireTraitementMasse2() {
      LOG.info("Mise à jour des actions unitaires");
      DroitsServiceUtils.addActionUnitaireTraitementMasseBis(keyspace);
   }

   @Override
   public void addActionUnitaireRepriseMasse() {
      LOG.info("Mise à jour des actions unitaires");
      DroitsServiceUtils.addActionUnitaireRepriseMasse(keyspace);
   }

   @Override
   public void addReferentielEvenementV12() {
      LOG.info("Mise à jour du référentiel des événements");
      ReferentielServiceUtils.addReferentielEvenementV12(keyspace);
   }

   @Override
   public void addReferentielFormatV7() {
      ReferentielServiceUtils.addReferentielFormatV7(keyspace);
   }

   @Override
   public void addReferentielEvenementV13() {
      LOG.info("Mise à jour du référentiel des événements");
      ReferentielServiceUtils.addReferentielEvenementV13(keyspace);
   }

   @Override
   public void addReferentielEvenementV14() {
      LOG.info("Mise à jour du référentiel des événements");
      ReferentielServiceUtils.addReferentielEvenementV14(keyspace);
   }

   /**
    * @param keyspace
    *           the keyspace to set
    */
   public void setKeyspace(final Keyspace keyspace) {
      this.keyspace = keyspace;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addReferentielEvenementV15() {
      throw new MajLotRuntimeException("Cette implémentation Thrift n'est plus nécéssaire "
            + "car Hector sera abandonné très prochainement!");
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addReferentielFormatV8() {
      throw new MajLotRuntimeException("Cette implémentation Thrift n'est plus nécéssaire "
            + "car Hector sera abandonné très prochainement!");
   }

}
