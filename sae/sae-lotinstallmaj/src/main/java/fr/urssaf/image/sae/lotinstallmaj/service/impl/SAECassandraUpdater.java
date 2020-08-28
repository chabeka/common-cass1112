package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.SAECassandraUpdaterCQL;
import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.ddl.ColumnDefinition;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

/**
 * Classe permettant la mise à jour du schéma du keyspace SAE dans cassandra
 * 
 */
@Component
public class SAECassandraUpdater {

   private static final int VERSION_2 = 2;
   private static final int VERSION_3 = 3;
   private static final int VERSION_4 = 4;
   private static final int VERSION_5 = 5;
   private static final int VERSION_6 = 6;
   private static final int VERSION_7 = 7;
   private static final int VERSION_8 = 8;
   private static final int VERSION_9 = 9;
   private static final int VERSION_10 = 10;
   private static final int VERSION_11 = 11;
   private static final int VERSION_12 = 12;
   private static final int VERSION_13 = 13;
   private static final int VERSION_14 = 14;
   private static final int VERSION_15 = 15;
   private static final int VERSION_16 = 16;
   public static final int VERSION_17 = 17;
   private static final int VERSION_18 = 18;
   private static final int VERSION_19 = 19;
   private static final int VERSION_20 = 20;
   private static final int VERSION_21 = 21;
   private static final int VERSION_22 = 22;
   private static final int VERSION_23 = 23;
   private static final int VERSION_24 = 24;
   private static final int VERSION_25 = 25;
   private static final int VERSION_26 = 26;
   private static final int VERSION_27 = 27;
   private static final int VERSION_28 = 28;
   private static final int VERSION_29 = 29;
   private static final int VERSION_30 = 30;
   private static final int VERSION_31 = 31;
   private static final int VERSION_32 = 32;
   private static final int VERSION_33 = 33;

   private static final String DROIT_PAGMF = "DroitPagmf";
   private static final String REFERENTIEL_FORMAT = "ReferentielFormat";
   private static final String DROIT_FORMAT_CONTROL_PROFIL = "DroitFormatControlProfil";

   private final String ksName;
   private final Cluster cluster;
   private final SAECassandraService saeCassandraService;

   private static final Logger LOG = LoggerFactory
         .getLogger(SAECassandraUpdater.class);


   private final SAECassandraDao saeDao;

   @Autowired
   private RefMetaInitialisationService refMetaInitService;

   @Autowired
   private DroitService droitService;

   private final InsertionDonneesImpl donnees;

   @Autowired
   private SAECassandraUpdaterCQL saeCassandraUpdaterCQL;

   /**
    * Getter sur le service d'initialisation des métas
    * 
    * @return Service d'initialisation du référentiel des métadonnée
    */
   public RefMetaInitialisationService getRefMetaInitService() {
      return refMetaInitService;
   }

   /**
    * Constructeur
    * 
    * @param saeCassandraService
    *           services pour CASSANDRA
    */
   @Autowired
   public SAECassandraUpdater(final SAECassandraService saeCassandraService,
         final SAECassandraDao saeDao,
         final InsertionDonneesImpl donnees) {
      this.saeCassandraService = saeCassandraService;
      cluster = saeCassandraService.getCluster();
      ksName = saeCassandraService.getKeySpaceName();
      saeDao.connectToKeySpace();
      this.saeDao = saeDao;
      this.donnees = donnees;
      this.donnees.setKeyspace(saeDao.getKeyspace());
   }

   public void beforeCreate() {
      LOG.info("Before Create");
      // On crée le keyspace "SAE" n'existe pas déjà.
      final KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         // Create the keyspace definition
         // Le facteur de réplication utilisé est le même que celui utilisé pour
         // le keyspace "Docubase", soit
         // 3 pour l'environnement de production, et de 1 à 3 pour les autres.
         final int replicationFactor = saeDao.getDocubaseReplicationFactor(cluster);

         final KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition(
               ksName,
               ThriftKsDef.DEF_STRATEGY_CLASS,
               replicationFactor,
               new ArrayList<ColumnFamilyDefinition>());
         // Add the schema to the cluster.
         // "true" as the second param means that Hector will block until all
         // nodes see the change.
         saeDao.createNewKeySpace(newKeyspace, true);
      }

      // Creation de la table mode
      saeCassandraUpdaterCQL.createTableModeAPI();

      // On se connecte au keyspace maintenant qu'il existe
      saeDao.connectToKeySpace();

      // Parameters
      final List<ColumnFamilyDefinition> colDefs = new ArrayList<>();
      colDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "Parameters",
            ComparatorType.BYTESTYPE));
      // Création de la cf Parameters
      saeCassandraService.createColumnFamilyFromList(colDefs, true);
      saeDao.setDatabaseVersion(0L);
      LOG.info("End Before Create");
   }

   /**
    * Version 1 : création du keyspace SAE
    */
   public final void updateToVersion1(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= 1) {
         LOG.info("La base de données est déja en version {}", version);
         return;
      }

      LOG.info("Création du keyspace SAE en version 1");

      // Si le KeySpace SAE n'existe pas, on quitte
      // En effet, il aurait du être créé lors de l'operation BeforeCreate
      final KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      final List<ColumnFamilyDefinition> cfDefs = new ArrayList<>();

      // JobInstance
      // Cette CF a une colonne indexée : jobKey
      final BasicColumnDefinition jobKeyCol = new BasicColumnDefinition();
      jobKeyCol.setName(StringSerializer.get().toByteBuffer("jobKey"));
      jobKeyCol.setIndexName("jobKey_idx");
      jobKeyCol.setValidationClass("BytesType");
      jobKeyCol.setIndexType(ColumnIndexType.KEYS);
      final List<ColumnDefinition> colDefs = new ArrayList<>();
      colDefs.add(jobKeyCol);
      final ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
            ksName, "JobInstance", ComparatorType.BYTESTYPE, colDefs);
      cfDefs.add(cfDef);

      // JobInstancesByName
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "JobInstancesByName", ComparatorType.LONGTYPE));

      // JobInstanceToJobExecution
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "JobInstanceToJobExecution", ComparatorType.LONGTYPE));

      // JobExecution
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobExecution",
            ComparatorType.BYTESTYPE));

      // JobExecutions
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobExecutions",
            ComparatorType.LONGTYPE));

      // JobExecutionsRunning
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "JobExecutionsRunning", ComparatorType.LONGTYPE));

      // JobExecutionToJobStep
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "JobExecutionToJobStep", ComparatorType.LONGTYPE));

      // JobStep
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobStep",
            ComparatorType.BYTESTYPE));

      // JobSteps
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobSteps",
            ComparatorType.LONGTYPE));

      // Sequences
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "Sequences",
            ComparatorType.BYTESTYPE));

      // JobRequest
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobRequest",
            ComparatorType.BYTESTYPE));

      // JobsQueue
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobsQueue",
            ComparatorType.TIMEUUIDTYPE));

      // Parameters
      // cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "Parameters",
      // ComparatorType.BYTESTYPE));

      // Création des CF

      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // On positionne la version à 1
      if (!isRedo) {
         saeDao.setDatabaseVersion(1L);
      }

   }

   /**
    * Version 2 : Ajout d'une column family dans le Keyspace "SAE"
    */
   public final void updateToVersion2(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_2) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_2 - version > 1) {
         updateToVersion1(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 2");

      // Si le KeySpace SAE n'existe pas, on quitte
      // En effet, il aurait du être créé lors de l'install du lot SAE-120511
      final KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      final List<ColumnFamilyDefinition> cfDefs = new ArrayList<>();

      // JobHistory
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobHistory",
            ComparatorType.TIMEUUIDTYPE));

      // Création des CF

      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // On positionne la version à 2 si la mise à jour n'est pas un rejeu
      if (!isRedo) {
         saeDao.setDatabaseVersion(2L);
      }

   }

   /**
    * Version 3 : Ajout d'une column family dans le Keyspace "SAE"
    */
   public final void updateToVersion3(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_3) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_3 - version > 1) {
         updateToVersion2(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 3");

      // Si le KeySpace SAE n'existe pas, on quitte
      // En effet, il aurait du être créé lors de l'install du lot SAE-120511
      final KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      final List<ColumnFamilyDefinition> cfDefs = new ArrayList<>();

      // DroitContratService
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "DroitContratService", ComparatorType.UTF8TYPE));

      // DroitPagm
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "DroitPagm",
            ComparatorType.UTF8TYPE));

      // DroitPagma
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "DroitPagma",
            ComparatorType.UTF8TYPE));

      // DroitPagmp
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "DroitPagmp",
            ComparatorType.UTF8TYPE));

      // DroitActionUnitaire
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "DroitActionUnitaire", ComparatorType.UTF8TYPE));

      // DroitPrmd
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "DroitPrmd",
            ComparatorType.UTF8TYPE));

      // Création des CF
      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      donnees.addDroits();

      // On positionne la version à 3
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_3);
      }

   }

   /**
    * Version 4 :
    * <ul>
    * <li>ajout des CF de traces dans le keyspace "SAE"</li>
    * <li>initialisation des paramètres de purge des registres</li>
    * <li>initialisation du référentiel des événements</li>
    * </ul>
    */
   public void updateToVersion4(final boolean isRedo) {
      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_4) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_4 - version > 1) {
         updateToVersion3(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 4");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      final List<ColumnFamilyDefinition> cfDefs = new ArrayList<>();

      // TraceDestinataire
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceDestinataire", ComparatorType.UTF8TYPE));

      // TraceRegSecurite
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceRegSecurite", ComparatorType.UTF8TYPE));
      // TraceRegSecuriteIndex
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceRegSecuriteIndex", ComparatorType.TIMEUUIDTYPE));

      // TraceRegExploitation
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceRegExploitation", ComparatorType.UTF8TYPE));
      // TraceRegExploitationIndex
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceRegExploitationIndex", ComparatorType.TIMEUUIDTYPE));

      // TraceRegTechnique
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceRegTechnique", ComparatorType.UTF8TYPE));
      // TraceRegTechniqueIndex
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceRegTechniqueIndex", ComparatorType.TIMEUUIDTYPE));

      // TraceJournalEvt
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceJournalEvt", ComparatorType.UTF8TYPE));
      // TraceJournalEvtIndex
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceJournalEvtIndex", ComparatorType.TIMEUUIDTYPE));

      // Création des CF
      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      donnees.addTracabiliteParameters();
      donnees.addReferentielEvenementV1();

      // On positionne la version à 4
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_4);
      }

   }

   /**
    * Version 5 :
    * <ul>
    * <li>ajout des CF de dictionnaire et métadonnées dans le keyspace "SAE"</li>
    * <li>ajout des CF de mise à jour du RND dans le keyspace "SAE"</li>
    * <li>MAJ du référentiel des événements</li>
    * </ul>
    */
   public void updateToVersion5(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_5) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_5 - version > 1) {
         updateToVersion4(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 5");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      final List<ColumnFamilyDefinition> cfDefs = new ArrayList<>();

      // Metadata
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "Metadata",
            ComparatorType.UTF8TYPE));

      // Dictionary
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "Dictionary",
            ComparatorType.UTF8TYPE));

      // Rnd
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "Rnd",
            ComparatorType.UTF8TYPE));

      // CorrespondancesRnd : Liste des correspondances codes temporaires /
      // codes définitifs
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "CorrespondancesRnd", ComparatorType.UTF8TYPE));

      // Création des CF
      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // Insertion de données
      // final MAJ_VERSION_RND|OK

      donnees.addReferentielEvenementV2();
      donnees.addRndParameters();

      // Initialisation du référentiel des métadonnées
      // suite au passage à un stockage du référentiel en bdd
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 5
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_5);
      }

   }

   /**
    * Version 5 :
    * <ul>
    * <li>ajout des CF de dictionnaire et métadonnées dans le keyspace "SAE"</li>
    * <li>ajout des CF de mise à jour du RND dans le keyspace "SAE"</li>
    * <li>MAJ du référentiel des événements</li>
    * </ul>
    */
   public void updateToVersion6(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_6) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_6 - version > 1) {
         updateToVersion5(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 6");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Initialisation du référentiel des métadonnées
      // suite au passage à un stockage du référentiel en bdd
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // Enrichissement du référentiel des événements

      donnees.addReferentielEvenementV3();

      // On positionne la version à 6
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_6);
      }

   }

   /**
    * Version 7 : Ajout d'une column family dans le Keyspace "SAE"<br>
    * :
    * <ul>
    * <li>ReferentielFormat</li>
    * </ul>
    * 
    */
   public final void updateToVersion7(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();

      if (!isRedo && version >= VERSION_7) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_7 - version > 1) {
         updateToVersion6(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 7 pour référentiel des formats");

      // Si le KeySpace SAE n'existe pas, on quitte
      // En effet, il aurait du être créé lors de l'install du lot SAE-140400
      final KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      final List<ColumnFamilyDefinition> cfDefs = new ArrayList<>();
      // ReferentielFormat
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            REFERENTIEL_FORMAT, ComparatorType.UTF8TYPE));
      // DroitPagmf
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, DROIT_PAGMF,
            ComparatorType.UTF8TYPE));
      // DroitFormatControlProfil
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            DROIT_FORMAT_CONTROL_PROFIL, ComparatorType.UTF8TYPE));
      // Création des CF
      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // Insertion des données initiales
      // Les formats reconnus dans le référentiel des formats

      donnees.addReferentielFormat();

      // Profils de contrôle du fmt/354
      donnees.addFormatControleProfil();

      // ???
      donnees.addDroits();

      // ajout de la colonne dispo et trim gauche/droite pour les metadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // List<String> listeRows =
      // getRowsToUpdateMetaForVersion7("metadata140400.txt");
      // donnees.addColumnClientAvailableMetadata(listeRows);

      // Enrichissement du référentiel des événements

      donnees.addReferentielEvenementV4();

      // On positionne la version à 7
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_7);
      }

   }

   /**
    * Version 8 : <li>MAJ du référentiel des événements</li>
    */
   public void updateToVersion8(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_8) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_8 - version > 1) {
         updateToVersion7(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 8");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Initialisation du référentiel des métadonnées
      // suite au passage à un stockage du référentiel en bdd
      // refMet//refMetaInitService.initialiseRefMeta(saeDao.getKeyspace()); // -- Liste contenant la définition des column families à créer
      final List<ColumnFamilyDefinition> cfDefs = new ArrayList<>();

      // -- TraceJournalEvtIndexDoc
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            "TraceJournalEvtIndexDoc", ComparatorType.UUIDTYPE));

      // -- Création des CF
      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // -- Enrichissement du référentiel des événements
      // donnees.addTracabiliteParameters();

      donnees.addReferentielEvenementV5();

      // Ajout du format fmt/353
      donnees.addReferentielFormatV2();

      // On positionne la version à 8
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_8);
      }

   }

   /**
    * Version 9 : <li>Création des metadonnées Sicomor</li>
    */
   public void updateToVersion9(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_9) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_9 - version > 1) {
         updateToVersion8(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 9");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Initialisation du référentiel des métadonnées
      // suite au passage à un stockage du référentiel en bdd
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 9
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_9);
      }
   }

   /**
    * Version 10 : <li>Création des metadonnées Groom et suppression des trim
    * gauche et droite pour la méta boolean ControleComptable</li>
    */
   public void updateToVersion10(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_10) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_10 - version > 1) {
         updateToVersion9(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 10");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Initialisation du référentiel des métadonnées
      // suite au passage à un stockage du référentiel en bdd
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 10
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_10);
      }
   }

   /**
    * Version 11 : <li>Création des metadonnées Groom et suppression des trim
    * gauche et droite pour la méta boolean ControleComptable</li>
    */
   public void updateToVersion11(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_11) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_11 - version > 1) {
         updateToVersion10(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 11");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Enrichissement du référentiel des événements

      donnees.addReferentielEvenementV6();

      // On positionne la version à 11
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_11);
      }
   }

   /**
    * Version 12 : <li>Création de la métadonnée Note</li> <li>Ajout de
    * WS_AJOUT_NOTE|KO dans le référentiel des évenements</li>
    */
   public void updateToVersion12(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_12) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_12 - version > 1) {
         updateToVersion11(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 12");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Enrichissement du référentiel des événements

      donnees.addReferentielEvenementV7();

      // Ajout de l'action unitaire ajoutNote
      donnees.addActionUnitaireNote();

      // Ajout de la métadonnée Note
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 12
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_12);
      }
   }

   /**
    * Version 13 : <li>Création de la métadonnée ApplicationMetier</li>
    */
   public void updateToVersion13(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_13) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_13 - version > 1) {
         updateToVersion12(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 13");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout de la métadonnée Note
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 13
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_13);
      }
   }

   /**
    * Version 14 : <li>Création des métadonnées Scribe</li> <li>Création de
    * l'action unitaire recherche_iterateur</li> <li>Remplacement de l'action
    * unitaire ajoutNote par ajout_note</li>
    */
   public void updateToVersion14(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_14) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_14 - version > 1) {
         updateToVersion13(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 14");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // Ajout de l'action unitaire recherche_iterateur

      donnees.addActionUnitaireRechercheParIterateur();
      donnees.modifyActionUnitaireAjoutNote();

      // Ajout d'un convertisseur au format fmt/354 (splitter de pdf)
      donnees.modifyReferentielFormatFmt354();
      // Ajout du format x-fmt/111 (cold)
      donnees.addReferentielFormatV3();

      // On positionne la version à 14
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_14);
      }

   }

   /**
    * Version 15 : <li>Suite Remplacement de l'action unitaire ajoutNote par
    * ajout_note car oubli de la création suite à suppresion</li> <li>
    * Suppression trim gauche et droite pour l'IdGed</li> <li>Passage de la Note
    * en non transférable car le transfert de la note ne passe pas par les
    * métadonnées</li>
    */
   public void updateToVersion15(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_15) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_15 - version > 1) {
         updateToVersion14(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 15");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Modif des métadonnées des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // Ajout de l'action unitaire ajout_note

      donnees.addActionUnitaireNote2();

      // On positionne la version à 15
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_15);
      }
   }

   /**
    * Version 16 : <li>Ajout de l'action unitaire ajout_doc_attache</li>
    */
   public void updateToVersion16(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_16) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_16 - version > 1) {
         updateToVersion15(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 16");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Enrichissement du référentiel des événements

      donnees.addReferentielEvenementV8();

      // Ajout de l'action unitaire ajout_doc_attache
      donnees.addActionUnitaireAjoutDocAttache();

      // On positionne la version à 16
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_16);
      }
   }

   /**
    * Version 17 : <li>Création des métadonnées pour WATT</li> <li>Ajout des
    * événements DFCE_DEPOT_ATTACH|OK</li>
    */
   public void updateToVersion17(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_17) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_17 - version > 1) {
         updateToVersion16(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version 17");

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // Insertion des données initiales
      // Ajout du format pdf (pour DEA)

      donnees.addReferentielFormatV4();

      // Ajout des évenements DFCE_DEPOT_ATTACH|OK
      donnees.addReferentielEvenementV9();

      // On positionne la version à 17
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_17);
      }
   }

   /**
    * Version 18 : <li>Ajout d'un nouveau droit pour la suppression de masse et
    * la restore de masse</li> <li>Ajout des événements WS_SUPPRESSION_MASSE|KO,
    * WS_RESTORE_MASSE|KO, SUPPRESSION_MASSE|KO, RESTORE_MASSE_KO,
    * DFCE_CORBEILLE_DOC|OK, DFCE_RESTORE_DOC|OK</li>
    */
   public void updateToVersion18(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_18) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_18 - version > 1) {
         updateToVersion17(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_18);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // Insertion des données initiales
      // Ajout de l'action unitaire restore_masse et suppression_masse

      donnees.addActionUnitaireTraitementMasse();

      // Ajout des évenements WS_SUPPRESSION_MASSE|KO, WS_RESTORE_MASSE|KO,
      // SUPPRESSION_MASSE|KO, RESTORE_MASSE_KO, DFCE_CORBEILLE_DOC|OK,
      // DFCE_RESTORE_DOC|OK
      donnees.addReferentielEvenementV10();

      // On échappe tous les . des valeurs des métadonnées des PRMD suite
      // passage aux expressions régulières
      droitService.majPrmdExpReguliere160600(saeDao.getKeyspace());

      // On positionne la version à 18
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_18);
      }
   }

   /**
    * Version 19 : <li>Ajout de la nouvelle metadonnee ATransfererScribe pour
    * les besoins de SCRIBE</li>
    */
   public void updateToVersion19(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_19) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_19 - version > 1) {
         updateToVersion18(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_19);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 19
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_19);
      }
   }

   /**
    * Version 20 : <li>Passage sur 7 de la taille de NumeroIntControle</li> <li>
    * Ajout de la métadonnée DomaineRSI pour les besoins d'ODP</li>
    */
   public void updateToVersion20(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_20) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_20 - version > 1) {
         updateToVersion19(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_20);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 20
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_20);
      }
   }

   /**
    * Version 21 : <li>Ajout évenement WS_ETAT_TRAITEMENTS_MASSE|KO</li>
    */
   public void updateToVersion21(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_21) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_21 - version > 1) {
         updateToVersion20(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_21);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Insertion des données initiales
      // Ajout de l'évenement WS_ETAT_TRAITEMENTS_MASSE|KO

      donnees.addReferentielEvenementV11();

      // Ajout du format fmt/13 (PNG) et fmt/44 (JPG)
      donnees.addReferentielFormatV5();

      // Ajout des paramètres pour la purge de la corbeille
      donnees.addCorbeilleParameters();

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 21
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_21);
      }
   }

   /**
    * Version 22 : <li>Ajout de l'action unitaire suppression et modification pour la GNS</li>
    */
   public void updateToVersion22(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_22) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_22 - version > 1) {
         updateToVersion21(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_22);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout de l'action unitaire suppression et modification

      donnees.addActionUnitaireSuppressionModification();

      // On positionne la version à 22
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_22);
      }
   }

   /**
    * Version 23 : <li>Ajout de l'action unitaire copie</li>
    */
   public void updateToVersion23(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_23) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_23 - version > 1) {
         updateToVersion22(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_23);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout de l'action unitaire copie

      donnees.addActionUnitaireCopie();

      // On positionne la version à 23
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_23);
      }
   }

   public void updateToVersion24(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_24) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_24 - version > 1) {
         updateToVersion23(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_24);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout des nouveaux formats à gérer

      donnees.addReferentielFormatV6();

      // Modification du format fmt/353 (extension .tiff)
      donnees.modifyReferentielFormatFmt353();

      // Modification du format fmt/44 (extension .jpeg)
      donnees.modifyReferentielFormatFmt44();

      // Ajout de la colonne "Autorisé en GED" dans le referentiel des formats
      donnees.addColumnAutoriseGEDReferentielFormat();

      // On positionne la version à 24
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_24);
      }

   }

   public void updateToVersion25(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_25) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_25 - version > 1) {
         updateToVersion24(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_25);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout des nouveaux formats à gérer

      donnees.addReferentielFormatV6Bis();

      // Modification du format fmt/353 (extension .tiff)
      donnees.modifyReferentielFormatCrtl1();


      // On positionne la version à 25
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_25);
      }
   }

   public void updateToVersion26(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_26) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_26 - version > 1) {
         updateToVersion25(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_26);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout droits unitaires nouveaux traitements de masse.

      donnees.addActionUnitaireTraitementMasse2();

      // Ajout des évenements
      donnees.addReferentielEvenementV12();

      // On positionne la version à 26
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_26);
      }
   }

   public void updateToVersion27(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_27) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_27 - version > 1) {
         updateToVersion26(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_27);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // Ajout droit unitaire reprise des traitements de masse.

      donnees.addActionUnitaireRepriseMasse();

      // Ajout des évenements
      donnees.addReferentielEvenementV13();

      // Modification du format png
      donnees.addReferentielFormatV7();

      // Mise à jour du CS_V2 pour ajouter l'action reprise_masse à tous les PAGM
      droitService.majPagmCsV2AjoutActionReprise170900(saeDao.getKeyspace());

      // On positionne la version à 27
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_27);
      }
   }

   public void updateToVersion28(final boolean isRedo) {
      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_28) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_28 - version > 1) {
         updateToVersion27(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_28);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout d'une colonne indexée jobRKey à la CF JobRequest.
      final BasicColumnDefinition jobKeyCol = new BasicColumnDefinition();
      jobKeyCol.setName(StringSerializer.get().toByteBuffer("jobRKey"));
      jobKeyCol.setIndexName("jobRKey_idx");
      jobKeyCol.setValidationClass("BytesType");
      jobKeyCol.setIndexType(ColumnIndexType.KEYS);
      final List<ColumnDefinition> colDefs = new ArrayList<>();
      colDefs.add(jobKeyCol);
      final ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
            ksName, "JobRequest", ComparatorType.BYTESTYPE, colDefs);

      saeCassandraService.updateColumnFamilyFromDefinition(cfDef);

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // Mise à jour des contrat de service pour passage à la PKI nationale
      droitService.majPagmCsPourPKINationale180300(saeDao.getKeyspace());

      // On positionne la version à 28
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_28);
      }
   }


   public void updateToVersion29(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_29) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_29 - version > 1) {
         updateToVersion28(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_29);

      // -- Ajout des métadonnées
      // refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());


      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Ajout des évenements

      donnees.addReferentielEvenementV14();

      // On positionne la version à 29
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_29);
      }
   }

   // Passage de la méta NumeroLot sur une taille de 40
   public void updateToVersion30(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_30) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_30 - version > 1) {
         updateToVersion29(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_30);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // On positionne la version à 30
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_30);
      }
   }


   // Création de la métadonnée DateLimite
   public void updateToVersion31(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_31) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_31 - version > 1) {
         updateToVersion30(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_31);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // On positionne la version à 31
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_31);
      }
   }	

   // Création des métadonnées CodeCaisseTI et CodeServiceContentieuxTI
   public void updateToVersion32(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_32) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_32 - version > 1) {
         updateToVersion31(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_32);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // On positionne la version à 32
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_32);
      }

   }

   // Création des métadonnées NomContact et PrenomContact
   public void updateToVersion33(final boolean isRedo) {

      final long version = saeDao.getDatabaseVersion();
      if (!isRedo && version >= VERSION_33) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      if (!isRedo && VERSION_33 - version > 1) {
         updateToVersion32(isRedo);
      }

      LOG.info("Mise à jour du keyspace SAE en version " + VERSION_33);

      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();

      // On positionne la version à 33
      if (!isRedo) {
         saeDao.setDatabaseVersion(VERSION_33);
      }
   }	

   /**
    * Methode permettant de modifier la version de la base de données.
    * 
    * @param version
    *           version de la base de données.
    */
   public void updateDatabaseVersion(final int version) {
      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();
      // On positionne la version de la base de données
      saeDao.setDatabaseVersion(version);
   }

   /**
    * Met à jour la version de la base DFCE
    * 
    * @param version
    */
   public void updateDatabaseVersionDFCE(final int version) {
      // -- On se connecte au keyspace
      saeDao.connectToKeySpace();
      // On positionne la version de la base de données
      saeDao.setDatabaseVersionDFCE(version);
   }

   /**
    * Recupère la version actuelle de la base SAE
    * 
    * @return
    */
   public long getDatabaseVersion() {
      return saeDao.getDatabaseVersion();
   }

   /**
    * Recupère la version courante de la base de données DFCE
    * 
    * @return
    */
   public long getDatabaseVersionDFCE() {
      return saeDao.getDatabaseVersionDFCE();
   }

   public void deleteDatabaseVersionDFCE() {
      saeDao.connectToKeySpace();
      saeDao.deleteDatabaseVersionDFCE();
   }

}
