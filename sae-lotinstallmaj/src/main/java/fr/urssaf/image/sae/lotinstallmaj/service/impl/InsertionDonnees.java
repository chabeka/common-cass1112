/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.Serializer;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.sae.lotinstallmaj.modele.FormatProfil;
import fr.urssaf.image.sae.lotinstallmaj.modele.Pagm;
import fr.urssaf.image.sae.lotinstallmaj.serializer.FormatProfilSerializer;
import fr.urssaf.image.sae.lotinstallmaj.serializer.ListSerializer;
import fr.urssaf.image.sae.lotinstallmaj.serializer.PagmSerializer;

/**
 * 
 * 
 */
public class InsertionDonnees {

   private static final Logger LOG = LoggerFactory
         .getLogger(InsertionDonnees.class);

   private static final String DESCRIPTION = "description";

   private static final int DEFAULT_CONSERVATION = 7200;

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
      addActionsUnitaires();
      addPrmd();
      addPagma();
      addPagmp();
      addPagm();
      addContratService();
   }

   private void addActionsUnitaires() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("consultation", "consultation", cfTmpl);
      addActionUnitaire("recherche", "recherche", cfTmpl);
      addActionUnitaire("archivage_masse", "archivage de masse", cfTmpl);
      addActionUnitaire("archivage_unitaire", "archivage unitaire", cfTmpl);

   }

   /**
    * @param string
    * @param string2
    * @param updater
    */
   @SuppressWarnings("unchecked")
   private void addColumn(Object colName, Object value,
         Serializer nameSerializer, Serializer valueSerializer,
         ColumnFamilyUpdater<String, String> updater) {
      HColumn<String, String> column = HFactory.createColumn(colName, value,
            nameSerializer, valueSerializer);
      updater.setColumn(column);

   }

   private void addPrmd() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPrmd", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PRMD");
      addColumn(DESCRIPTION, "acces total", StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("bean", "permitAll", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
   }

   private void addPagma() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagma", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PAGMA");
      addColumn("consultation", StringUtils.EMPTY, StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("recherche", StringUtils.EMPTY, StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("archivage_masse", StringUtils.EMPTY, StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("archivage_unitaire", StringUtils.EMPTY,
            StringSerializer.get(), StringSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   private void addPagmp() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagmp", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PAGMP");
      addColumn(DESCRIPTION, "acces pagmp full", StringSerializer.get(),
            StringSerializer.get(), updater);
      addColumn("prmd", "ACCES_FULL_PRMD", StringSerializer.get(),
            StringSerializer.get(), updater);

      cfTmpl.update(updater);
   }

   private void addPagm() {
      Pagm pagm = new Pagm();
      pagm.setCode("ACCES_FULL_PAGM");
      pagm.setDescription("Pagm accès total");
      pagm.setPagma("ACCES_FULL_PAGMA");
      pagm.setPagmp("ACCES_FULL_PAGMP");
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagm", StringSerializer.get(), StringSerializer
                  .get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("CS_ANCIEN_SYSTEME");
      addColumn("ACCES_FULL_PAGM", pagm, StringSerializer.get(), PagmSerializer
            .get(), updater);
      cfTmpl.update(updater);

   }

   private void addContratService() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitContratService", StringSerializer.get(),
            StringSerializer.get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("CS_ANCIEN_SYSTEME");
      addColumn("libelle", "accès ancien contrat de service", StringSerializer
            .get(), PagmSerializer.get(), updater);
      addColumn(DESCRIPTION, "accès ancien contrat de service",
            StringSerializer.get(), PagmSerializer.get(), updater);
      addColumn("viDuree", Long.valueOf(DEFAULT_CONSERVATION), StringSerializer
            .get(), LongSerializer.get(), updater);
      addColumn("pki", "CN=IGC/A", StringSerializer.get(), StringSerializer
            .get(), updater);

      cfTmpl.update(updater);

   }

   private void addActionUnitaire(String identifiant, String description,
         ColumnFamilyTemplate<String, String> cfTmpl) {
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(identifiant);
      HColumn<String, String> column = HFactory.createColumn(DESCRIPTION,
            description, StringSerializer.get(), StringSerializer.get());
      updater.setColumn(column);

      cfTmpl.update(updater);
   }

   /**
    * Ajoute les paramètres nécéssaires à la maj du RND
    */
   public void addRndParameters() {
      LOG.info("Création des paramètres de maj du RND");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Parameters", StringSerializer.get(), StringSerializer
                  .get());

      checkAndAddRndParameter(cfTmpl, "VERSION_RND_NUMERO", "");
      checkAndAddRndParameter(cfTmpl, "VERSION_RND_DATE_MAJ", getRndDate());

   }

   /**
    * Ajoute les paramètres nécessaires à la traçabilité SAE
    */
   public void addTracabiliteParameters() {

      LOG.info("Création des paramètres de traçabilité");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Parameters", StringSerializer.get(), StringSerializer
                  .get());

      Date debutTracabilite = getTracabiliteDerniereDateTraitee();

      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_TECH_DUREE", Integer
            .valueOf(10));
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_SECU_DUREE", Integer
            .valueOf(10));
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EXPLOIT_DUREE", Integer
            .valueOf(10));
      checkAndAddTracabiliteParameter(cfTmpl, "PURGE_EVT_DUREE", Integer
            .valueOf(10));

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

      // On démarre la traçabilité SAE au 01/02/2013
      // La dernière date des traitements est positionné au 31/01/2013
      // Car les traitements font un J+1 à partir des valeurs de paramètres

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

   /**
    * Initialisation du référentiel des événements en V1
    */
   public void addReferentielEvenementV1() {

      LOG.info("Initialisation du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // WS_RECHERCHE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_RECHERCHE|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_CAPTURE_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_CAPTURE_MASSE|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_CAPTURE_UNITAIRE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_CAPTURE_UNITAIRE|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_CONSULTATION|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_CONSULTATION|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_PING_SECURE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_PING_SECURE|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // CAPTURE_MASSE|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("CAPTURE_MASSE|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // DFCE_DEPOT_DOC|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("DFCE_DEPOT_DOC|OK");
      addColumn("JOURN_EVT", allInfos, StringSerializer.get(), ListSerializer
            .get(), updater);
      cfTmpl.update(updater);

      // DFCE_SUPPRESSION_DOC|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("DFCE_SUPPRESSION_DOC|OK");
      addColumn("JOURN_EVT", allInfos, StringSerializer.get(), ListSerializer
            .get(), updater);
      cfTmpl.update(updater);

      // WS_LOAD_CERTS_ACRACINE|OK
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_LOAD_CERTS_ACRACINE|OK");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_LOAD_CRLS|OK
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_LOAD_CRLS|OK");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V2 Ajout de l'évenement MAJ_VERSION_RND|OK
    * (Automatisation RND)
    */
   public void addReferentielEvenementV2() {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // MAJ_VERSION_RND|OK
      // dans le journal des événements SAE avec all_infos
      updater = cfTmpl.createUpdater("MAJ_VERSION_RND|OK");
      addColumn("JOURN_EVT", allInfos, StringSerializer.get(), ListSerializer
            .get(), updater);
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      updater = cfTmpl.createUpdater("DFCE_MODIF_DOC|OK");
      addColumn("JOURN_EVT", allInfos, StringSerializer.get(), ListSerializer
            .get(), updater);

      cfTmpl.update(updater);

   }

   /**
    * Ajout des droits spécifiques GED :
    * <ul>
    * <li>modification</li>
    * <li>suppression</li>
    * <li>transfert</li>
    * </ul>
    */
   public void addDroitsGed() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("modification", "modification", cfTmpl);
      addActionUnitaire("suppression", "suppression", cfTmpl);
      addActionUnitaire("transfert", "transfert", cfTmpl);
   }

   /**
    * Référentiel des événements en V3 Ajout de l'évenement ORDO_ECDE_DISPO|KO
    */
   public void addReferentielEvenementV3() {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // ORDO_ECDE_DISPO|KO
      // dans le registre de surveillance technique avec all_infos
      // dans le registre d'exploitation avec all_infos
      updater = cfTmpl.createUpdater("ORDO_ECDE_DISPO|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      addColumn("REG_EXPLOITATION", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V4 Ajout des évenements IGC_LOAD_CRLS|KO,
    * WS_LOAD_CRLS|KO, ERREUR_IDENT_FORMAT_FICHIER|INFO et
    * ERREUR_VALID_FORMAT_FICHIER |INFO
    */
   public void addReferentielEvenementV4() {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // IGC_LOAD_CRLS|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("IGC_LOAD_CRLS|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // WS_LOAD_CRLS|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_LOAD_CRLS|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // ERREUR_IDENT_FORMAT_FICHIER|INFO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("ERREUR_IDENT_FORMAT_FICHIER|INFO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // ERREUR_VALID_FORMAT_FICHIER|INFO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("ERREUR_VALID_FORMAT_FICHIER|INFO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // META_VAL_ESPACE|INFO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("META_VAL_ESPACE|INFO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Référentiel des événements en V5 Ajout des évenements : <li>
    * DFCE_TRANSFERT_DOC|OK</li> <li>WS_TRANSFERT|KO</li>
    * 
    * @since 06/10/2014
    * @author Michael PAMBO OGNANA
    */
   public void addReferentielEvenementV5() {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // -- DFCE_TRANSFERT_DOC|OK
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("DFCE_TRANSFERT_DOC|OK");
      addColumn("JOURN_EVT", allInfos, StringSerializer.get(), ListSerializer
            .get(), updater);
      cfTmpl.update(updater);

      // -- WS_TRANSFERT|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_TRANSFERT|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);
   }

   /**
    * Référentiel des événements en V5 Ajout des évenements : <li>
    * WS_SUPPRESSION|KO</li> <li>WS_MODIFICATION|KO</li> <li>
    * WS_RECUPERATION_METAS|KO</li>
    */
   public void addReferentielEvenementV6() {

      LOG.info("Mise à jour du référentiel des événements");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "TraceDestinataire", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      List<String> allInfos = Arrays.asList("all_infos");

      // -- WS_SUPPRESSION|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_SUPPRESSION|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // -- WS_MODIFICATION|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_MODIFICATION|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);

      // -- WS_RECUPERATION_METAS|KO
      // dans le registre de surveillance technique avec all_infos
      updater = cfTmpl.createUpdater("WS_RECUPERATION_METAS|KO");
      addColumn("REG_TECHNIQUE", allInfos, StringSerializer.get(),
            ListSerializer.get(), updater);
      cfTmpl.update(updater);
   }

   /**
    * Ajout des données dans le référentiel des formats : <li>fmt/354</li> <li>
    * crtl/1</li>
    */
   public void addReferentielFormat() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      updater = cfTmpl.createUpdater("fmt/354");
      addColumn("idFormat", "fmt/354", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
      addColumn("description", "PDF/A 1b", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("extension", "pdf", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
      addColumn("identifieur", "pdfaIdentifierImpl", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("typeMime", "application/pdf", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("validator", "pdfaValidatorImpl", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("visualisable", Boolean.TRUE, StringSerializer.get(),
            BooleanSerializer.get(), updater);
      cfTmpl.update(updater);

      updater = cfTmpl.createUpdater("crtl/1");
      addColumn("idFormat", "crtl/1", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
      addColumn("description",
            "Journal SAE, XML basé sur XSD, compressé en tar.gz",
            StringSerializer.get(), StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("extension", "tar.gz", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
      addColumn("typeMime", "application/x-gzip", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("visualisable", Boolean.FALSE, StringSerializer.get(),
            BooleanSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * Ajout des données dans le référentiel des formats en V2 : <li>fmt/353</li>
    */
   public void addReferentielFormatV2() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "ReferentielFormat", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      LOG.info("Mise à jour du référentiel des formats");

      updater = cfTmpl.createUpdater("fmt/353");
      addColumn("idFormat", "fmt/353", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
      addColumn("description", "Fichier TIFF", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("extension", "tif", StringSerializer.get(), StringSerializer
            .get(), updater);
      cfTmpl.update(updater);
      addColumn("typeMime", "image/tiff", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("visualisable", Boolean.FALSE, StringSerializer.get(),
            BooleanSerializer.get(), updater);
      cfTmpl.update(updater);
      addColumn("convertisseur", "tiffToPdfConvertisseurImpl", StringSerializer
            .get(), StringSerializer.get(), updater);
      cfTmpl.update(updater);
      LOG.info("Format ajouté : fmt/353");

   }

   /**
    * Les profils de controle pour les formats.<br/>
    * Ajout de 3 éléments pour le format fmt/354:
    * <ul>
    * <li>Identification seule</li>
    * <li>Validation seule</li>
    * <li>Identification et validation</li>
    * </ul>
    */
   public void addFormatControleProfil() {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitFormatControlProfil", StringSerializer.get(),
            StringSerializer.get());

      ColumnFamilyUpdater<String, String> updater;

      updater = cfTmpl.createUpdater("IDENT_FMT_354");
      addColumn(
            "description",
            "format de controle gérant exclusivement l'identification du fmt/354",
            StringSerializer.get(), StringSerializer.get(), updater);

      FormatProfil profil = new FormatProfil();
      profil.setFileFormat("fmt/354");
      profil.setFormatIdentification(true);
      profil.setFormatValidation(false);
      profil.setFormatValidationMode("STRICT");
      addColumn("controlProfil", profil, StringSerializer.get(),
            FormatProfilSerializer.get(), updater);
      cfTmpl.update(updater);

      updater = cfTmpl.createUpdater("VALID_FMT_354");
      addColumn("description",
            "format de controle gérant exclusivement la validation du fmt/354",
            StringSerializer.get(), StringSerializer.get(), updater);

      profil = new FormatProfil();
      profil.setFileFormat("fmt/354");
      profil.setFormatIdentification(false);
      profil.setFormatValidation(true);
      profil.setFormatValidationMode("STRICT");
      addColumn("controlProfil", profil, StringSerializer.get(),
            FormatProfilSerializer.get(), updater);
      cfTmpl.update(updater);

      updater = cfTmpl.createUpdater("VALID_FMT_354");
      addColumn("description",
            "format de controle gérant exclusivement la validation du fmt/354",
            StringSerializer.get(), StringSerializer.get(), updater);

      updater = cfTmpl.createUpdater("IDENT_VALID_FMT_354");
      profil = new FormatProfil();
      profil.setFileFormat("fmt/354");
      profil.setFormatIdentification(true);
      profil.setFormatValidation(true);
      profil.setFormatValidationMode("STRICT");
      addColumn("controlProfil", profil, StringSerializer.get(),
            FormatProfilSerializer.get(), updater);
      cfTmpl.update(updater);
   }

   /**
    * Ajout de la colonne 'dispo' pour chaque metadonnée fournit en paramètre.
    * 
    * @param listeRows
    *           liste des codes long des métadonnée
    */
   public void addColumnClientAvailableMetadata(List<String> listeRows) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Metadata", StringSerializer.get(), StringSerializer
                  .get());

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

}
