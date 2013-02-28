/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

import fr.urssaf.image.sae.lotinstallmaj.modele.Pagm;
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
    * Ajoute les paramètres nécessaires à la traçabilité SAE
    */
   public void addTracabiliteParameters() {

      LOG.info("Création des paramètres de traçabilité");

      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "Parameters", StringSerializer.get(), StringSerializer
                  .get());

      Date debutTracabilite = getTracabiliteDerniereDateTraitee();

      addTracabiliteParameter(cfTmpl, "PURGE_TECH_DUREE", new Integer(10));
      addTracabiliteParameter(cfTmpl, "PURGE_SECU_DUREE", new Integer(10));
      addTracabiliteParameter(cfTmpl, "PURGE_EXPLOIT_DUREE", new Integer(10));
      addTracabiliteParameter(cfTmpl, "PURGE_EVT_DUREE", new Integer(10));

      addTracabiliteParameter(cfTmpl, "PURGE_TECH_IS_RUNNING", Boolean.FALSE);
      addTracabiliteParameter(cfTmpl, "PURGE_SECU_IS_RUNNING", Boolean.FALSE);
      addTracabiliteParameter(cfTmpl, "PURGE_EXPLOIT_IS_RUNNING", Boolean.FALSE);
      addTracabiliteParameter(cfTmpl, "PURGE_EVT_IS_RUNNING", Boolean.FALSE);

      addTracabiliteParameter(cfTmpl, "PURGE_TECH_DATE", debutTracabilite);
      addTracabiliteParameter(cfTmpl, "PURGE_SECU_DATE", debutTracabilite);
      addTracabiliteParameter(cfTmpl, "PURGE_EXPLOIT_DATE", debutTracabilite);
      addTracabiliteParameter(cfTmpl, "PURGE_EVT_DATE", debutTracabilite);

      addTracabiliteParameter(cfTmpl, "JOURNALISATION_EVT_DATE",
            debutTracabilite);
      addTracabiliteParameter(cfTmpl, "JOURNALISATION_EVT_IS_RUNNING",
            Boolean.FALSE);

      addTracabiliteParameter(cfTmpl,
            "JOURNALISATION_EVT_ID_JOURNAL_PRECEDENT",
            "00000000-0000-0000-0000-000000000000");
      addTracabiliteParameter(cfTmpl,
            "JOURNALISATION_EVT_HASH_JOURNAL_PRECEDENT",
            "0000000000000000000000000000000000000000");

   }

   /**
    * @param name
    * @param cfTmpl
    */
   private void addTracabiliteParameter(
         ColumnFamilyTemplate<String, String> cfTmpl, String subname,
         Object valeur) {

      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("parametresTracabilite");

      HColumn<String, Object> column = HFactory.createColumn(subname, valeur,
            StringSerializer.get(), ObjectSerializer.get());
      updater.setColumn(column);

      cfTmpl.update(updater);

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

   }

}
