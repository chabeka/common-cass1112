package fr.urssaf.image.sae.lotinstallmaj.service.utils;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyUpdater;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.sae.lotinstallmaj.modele.Pagm;
import fr.urssaf.image.sae.lotinstallmaj.serializer.PagmSerializer;

/**
 * Classe utilitaire de service de modification des droits.
 * 
 */
public class DroitsServiceUtils {

   /**
    * Description.
    */
   private static final String DESCRIPTION = "description";

   /**
    * Conservation par defaut.
    */
   private static final int DEFAULT_CONSERVATION = 7200;

   /**
    * Insertion de données de droits.
    * 
    * @param keyspace
    *           Keyspace
    */
   public static final void addDroits(Keyspace keyspace) {
      addActionsUnitaires(keyspace);
      addPrmd(keyspace);
      addPagma(keyspace);
      addPagmp(keyspace);
      addPagm(keyspace);
      addContratService(keyspace);
   }

   /**
    * Methode permettant de'ajouter des actions unitaires.
    * 
    * @param keyspace
    *           Keyspace
    */
   private static void addActionsUnitaires(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("consultation", "consultation", cfTmpl);
      addActionUnitaire("recherche", "recherche", cfTmpl);
      addActionUnitaire("archivage_masse", "archivage de masse", cfTmpl);
      addActionUnitaire("archivage_unitaire", "archivage unitaire", cfTmpl);

   }

   /**
    * 
    * Methode permettant d'ajouter des PRMD.
    * 
    * @param keyspace
    *           Keyspace
    */
   private static void addPrmd(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPrmd", StringSerializer.get(),
            StringSerializer.get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PRMD");
      CassandraUtils.addColumn(DESCRIPTION, "acces total",
            StringSerializer.get(),
            StringSerializer.get(), updater);
      CassandraUtils.addColumn("bean", "permitAll", StringSerializer.get(),
            StringSerializer.get(), updater);
      cfTmpl.update(updater);
   }

   /**
    * 
    * Methode permettant d'ajouter des PAGMA.
    * 
    * @param keyspace
    *           Keyspace
    */
   private static void addPagma(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagma", StringSerializer.get(),
            StringSerializer.get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PAGMA");
      CassandraUtils.addColumn("consultation", StringUtils.EMPTY,
            StringSerializer.get(),
            StringSerializer.get(), updater);
      CassandraUtils.addColumn("recherche", StringUtils.EMPTY,
            StringSerializer.get(),
            StringSerializer.get(), updater);
      CassandraUtils.addColumn("archivage_masse", StringUtils.EMPTY,
            StringSerializer.get(),
            StringSerializer.get(), updater);
      CassandraUtils.addColumn("archivage_unitaire", StringUtils.EMPTY,
            StringSerializer.get(), StringSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * 
    * Methode permettant d'ajouter des PAGMP.
    * 
    * @param keyspace
    *           Keyspace
    */
   private static void addPagmp(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagmp", StringSerializer.get(),
            StringSerializer.get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("ACCES_FULL_PAGMP");
      CassandraUtils.addColumn(DESCRIPTION, "acces pagmp full",
            StringSerializer.get(),
            StringSerializer.get(), updater);
      CassandraUtils.addColumn("prmd", "ACCES_FULL_PRMD",
            StringSerializer.get(),
            StringSerializer.get(), updater);

      cfTmpl.update(updater);
   }

   /**
    * 
    * Methode permettant d'ajouter des PAGM.
    * 
    * @param keyspace
    *           Keyspace
    */
   private static void addPagm(Keyspace keyspace) {
      Pagm pagm = new Pagm();
      pagm.setCode("ACCES_FULL_PAGM");
      pagm.setDescription("Pagm accès total");
      pagm.setPagma("ACCES_FULL_PAGMA");
      pagm.setPagmp("ACCES_FULL_PAGMP");
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitPagm", StringSerializer.get(),
            StringSerializer.get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("CS_ANCIEN_SYSTEME");
      CassandraUtils.addColumn("ACCES_FULL_PAGM", pagm, StringSerializer.get(),
            PagmSerializer.get(), updater);
      cfTmpl.update(updater);

   }

   /**
    * 
    * Methode permettant d'ajouter des contrats de service.
    * 
    * @param keyspace
    *           Keyspace
    */
   private static void addContratService(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitContratService", StringSerializer.get(),
            StringSerializer.get());
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater("CS_ANCIEN_SYSTEME");
      CassandraUtils.addColumn("libelle", "accès ancien contrat de service",
            StringSerializer.get(), PagmSerializer.get(), updater);
      CassandraUtils.addColumn(DESCRIPTION, "accès ancien contrat de service",
            StringSerializer.get(), PagmSerializer.get(), updater);
      CassandraUtils.addColumn("viDuree", Long.valueOf(DEFAULT_CONSERVATION),
            StringSerializer.get(), LongSerializer.get(), updater);
      CassandraUtils.addColumn("pki", "CN=IGC/A", StringSerializer.get(),
            StringSerializer.get(), updater);

      cfTmpl.update(updater);

   }

   /**
    * 
    * Methode permettant d'ajouter des actions unitaires.
    * 
    * @param identifiant
    *           Identifiant de l'action
    * @param description
    *           Description de l'action
    * @param cfTmpl
    *           Template updater
    */
   private static void addActionUnitaire(String identifiant,
         String description,
         ColumnFamilyTemplate<String, String> cfTmpl) {
      ColumnFamilyUpdater<String, String> updater = cfTmpl
            .createUpdater(identifiant);
      HColumn<String, String> column = HFactory.createColumn(DESCRIPTION,
            description, StringSerializer.get(), StringSerializer.get());
      updater.setColumn(column);

      cfTmpl.update(updater);
   }

   /**
    * Ajout des droits spécifiques GED :
    * <ul>
    * <li>modification</li>
    * <li>suppression</li>
    * <li>transfert</li>
    * </ul>
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addDroitsGed(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("modification", "modification", cfTmpl);
      addActionUnitaire("suppression", "suppression", cfTmpl);
      addActionUnitaire("transfert", "transfert", cfTmpl);
   }

   /**
    * Ajout de l'action unitaire ajoutNote
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addActionUnitaireNote(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("ajoutNote", "ajoutNote", cfTmpl);
   }

   /**
    * Ajout de l'action unitaire recherche_iterateur
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addActionUnitaireRechercheParIterateur(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("recherche_iterateur", "Recherche par iterateur",
            cfTmpl);
   }

   /**
    * Ajout de l'action unitaire ajout_doc_attache
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addActionUnitaireAjoutDocAttache(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("ajout_doc_attache", "Ajout de document attache",
            cfTmpl);
   }

   /**
    * Ajout de l'action unitaire ajout_note car oublé dans
    * modifyActionUnitaireAjoutNote
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addActionUnitaireNote2(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("ajout_note", "Ajout de notes", cfTmpl);
   }

   /**
    * Ajout de l'action unitaire suppression_masse et restore_masse
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addActionUnitaireTraitementMasse(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("suppression_masse", "Suppression de masse", cfTmpl);
      addActionUnitaire("restore_masse", "Restore de masse", cfTmpl);

   }

   /**
    * Ajout de l'action unitaire modification_masse, transfert_masse et
    * deblocage
    * 
    * @param keyspace
    *           Keyspace
    */
   public static void addActionUnitaireTraitementMasseBis(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("modification_masse", "modification en masse", cfTmpl);
      addActionUnitaire("transfert_masse", "transfert de masse", cfTmpl);
      addActionUnitaire("deblocage", "deblocage de traitement de masse", cfTmpl);

   }

   /**
    * 
    * Ajout de l'action unitaire Copie Ajout de l'action unitaire
    */
   public static void addActionUnitaireCopie(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("copie", "copie d'un document", cfTmpl);

   }

   /**
    * Ajout de l'action unitaire suppression et modification Ajout de l'action
    * unitaire
    */
   public static void addActionUnitaireSuppressionModification(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      addActionUnitaire("suppression", "Suppression unitaire", cfTmpl);
      addActionUnitaire("modification", "Modification unitaire", cfTmpl);

   }

   /**
    * On remplace l'action unitaire ajoutNote par ajout_note afin d'être
    * homogène !!! L'AJOUT a été oublié, fait dans la méthode
    * addActionUnitaireNote2 Ajout de l'action unitaire
    */
   public static void modifyActionUnitaireAjoutNote(Keyspace keyspace) {
      ColumnFamilyTemplate<String, String> cfTmpl = new ThriftColumnFamilyTemplate<String, String>(
            keyspace, "DroitActionUnitaire", StringSerializer.get(),
            StringSerializer.get());
      deleteActionUnitaire("ajoutNote", cfTmpl);
   }

   /**
    * 
    * Methode permettant de supprimer des actions unitaires.
    * 
    * @param identifiant
    *           identifiant
    * @param cfTmpl
    *           Column family
    */
   public static void deleteActionUnitaire(String identifiant,
         ColumnFamilyTemplate<String, String> cfTmpl) {
      cfTmpl.deleteRow(identifiant);
   }

}
