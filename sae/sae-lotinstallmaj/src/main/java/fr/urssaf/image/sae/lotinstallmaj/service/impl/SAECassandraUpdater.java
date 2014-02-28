package fr.urssaf.image.sae.lotinstallmaj.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.lotinstallmaj.dao.SAECassandraDao;
import fr.urssaf.image.sae.lotinstallmaj.exception.MajLotRuntimeException;

/**
 * Classe permettant la mise à jour du schéma du keyspace SAE dans cassandra
 * 
 */
@Component
public class SAECassandraUpdater {

   private static final int VERSION_3 = 3;
   private static final int VERSION_4 = 4;
   private static final int VERSION_5 = 5;
   private static final int VERSION_6 = 6;
   private static final int VERSION_7 = 7;
   
   private static final String REFERENTIEL_FORMAT = "ReferentielFormat";
   private static final String DROIT_PAGMF = "DroitPagmf";
   private static final String DROIT_FORMAT_CONTROL_PROFIL = "DroitFormatControlProfil";

   private final String ksName;
   private final Cluster cluster;
   private final SAECassandraService saeCassandraService;

   private static final Logger LOG = LoggerFactory
         .getLogger(SAECassandraUpdater.class);

   @Autowired
   private SAECassandraDao saeDao;

   @Autowired
   private RefMetaInitialisationService refMetaInitService;
   
   @Autowired
   private ApplicationContext context;

   /**
    * Constructeur
    * 
    * @param saeCassandraService
    *           services pour CASSANDRA
    */
   @Autowired
   public SAECassandraUpdater(SAECassandraService saeCassandraService) {
      this.saeCassandraService = saeCassandraService;
      cluster = saeCassandraService.getCluster();
      ksName = saeCassandraService.getKeySpaceName();

   }

   /**
    * Version 1 : création du keyspace SAE
    */
   public final void updateToVersion1() {

      long version = saeDao.getDatabaseVersion();
      if (version >= 1) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      LOG.info("Création du keyspace SAE en version 1");

      // On crée le keyspace "SAE" n'existe pas déjà.
      KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         // Create the keyspace definition
         // Le facteur de réplication utilisé est le même que celui utilisé pour
         // le keyspace "Docubase", soit
         // 3 pour l'environnement de production, et de 1 à 3 pour les autres.
         int replicationFactor = saeDao.getDocubaseReplicationFactor(cluster);

         KeyspaceDefinition newKeyspace = HFactory.createKeyspaceDefinition(
               ksName, ThriftKsDef.DEF_STRATEGY_CLASS, replicationFactor,
               new ArrayList<ColumnFamilyDefinition>());
         // Add the schema to the cluster.
         // "true" as the second param means that Hector will block until all
         // nodes see the change.
         saeDao.createNewKeySpace(newKeyspace, true);
      }

      // On se connecte au keyspace maintenant qu'il existe
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

      // JobInstance
      // Cette CF a une colonne indexée : jobKey
      BasicColumnDefinition jobKeyCol = new BasicColumnDefinition();
      jobKeyCol.setName(StringSerializer.get().toByteBuffer("jobKey"));
      jobKeyCol.setIndexName("jobKey_idx");
      jobKeyCol.setValidationClass("BytesType");
      jobKeyCol.setIndexType(ColumnIndexType.KEYS);
      List<ColumnDefinition> colDefs = new ArrayList<ColumnDefinition>();
      colDefs.add(jobKeyCol);
      ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
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
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "Parameters",
            ComparatorType.BYTESTYPE));

      // Création des CF

      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // On positionne la version à 1
      saeDao.setDatabaseVersion(1L);

   }

   /**
    * Version 2 : Ajout d'une column family dans le Keyspace "SAE"
    */
   public final void updateToVersion2() {

      long version = saeDao.getDatabaseVersion();
      if (version >= 2) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      LOG.info("Mise à jour du keyspace SAE en version 2");

      // Si le KeySpace SAE n'existe pas, on quitte
      // En effet, il aurait du être créé lors de l'install du lot SAE-120511
      KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

      // JobHistory
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, "JobHistory",
            ComparatorType.TIMEUUIDTYPE));

      // Création des CF

      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // On positionne la version à 2
      saeDao.setDatabaseVersion(2L);

   }

   /**
    * Version 3 : Ajout d'une column family dans le Keyspace "SAE"
    */
   public final void updateToVersion3() {

      long version = saeDao.getDatabaseVersion();
      if (version >= VERSION_3) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      LOG.info("Mise à jour du keyspace SAE en version 3");

      // Si le KeySpace SAE n'existe pas, on quitte
      // En effet, il aurait du être créé lors de l'install du lot SAE-120511
      KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

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

      // ajout des actions unitaires de base
      InsertionDonnees donnees = new InsertionDonnees(saeDao.getKeyspace());
      donnees.addDroits();

      // On positionne la version à 3
      saeDao.setDatabaseVersion(VERSION_3);

   }

   /**
    * Version 4 :
    * <ul>
    * <li>ajout des CF de traces dans le keyspace "SAE"</li>
    * <li>initialisation des paramètres de purge des registres</li>
    * <li>initialisation du référentiel des événements</li>
    * </ul>
    */
   public void updateToVersion4() {
      long version = saeDao.getDatabaseVersion();
      if (version >= VERSION_4) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      LOG.info("Mise à jour du keyspace SAE en version 4");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

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

      InsertionDonnees donnees = new InsertionDonnees(saeDao.getKeyspace());
      donnees.addTracabiliteParameters();
      donnees.addReferentielEvenementV1();

      // On positionne la version à 4
      saeDao.setDatabaseVersion(VERSION_4);

   }

   /**
    * Version 5 :
    * <ul>
    * <li>ajout des CF de dictionnaire et métadonnées dans le keyspace "SAE"</li>
    * <li>ajout des CF de mise à jour du RND dans le keyspace "SAE"</li>
    * <li>MAJ du référentiel des événements</li>
    * </ul>
    */
   public void updateToVersion5() {

      long version = saeDao.getDatabaseVersion();
      if (version >= VERSION_5) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      LOG.info("Mise à jour du keyspace SAE en version 5");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

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
      InsertionDonnees donnees = new InsertionDonnees(saeDao.getKeyspace());
      // Ajout de l'évènement MAJ_VERSION_RND|OK
      donnees.addReferentielEvenementV2();

      donnees.addRndParameters();

      // Initialisation du référentiel des métadonnées
      // suite au passage à un stockage du référentiel en bdd
      refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());

      // On positionne la version à 5
      saeDao.setDatabaseVersion(VERSION_5);

   }
   
   /**
    * Version 5 :
    * <ul>
    * <li>ajout des CF de dictionnaire et métadonnées dans le keyspace "SAE"</li>
    * <li>ajout des CF de mise à jour du RND dans le keyspace "SAE"</li>
    * <li>MAJ du référentiel des événements</li>
    * </ul>
    */
   public void updateToVersion6() {

      long version = saeDao.getDatabaseVersion();
      if (version >= VERSION_6) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      LOG.info("Mise à jour du keyspace SAE en version 6");

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Initialisation du référentiel des métadonnées
      // suite au passage à un stockage du référentiel en bdd
      refMetaInitService.initialiseRefMeta(saeDao.getKeyspace());
      
      // Enrichissement du référentiel des événements
      InsertionDonnees donnees = new InsertionDonnees(saeDao.getKeyspace());
      donnees.addReferentielEvenementV3();

      // On positionne la version à 6
      saeDao.setDatabaseVersion(VERSION_6);

   }
   
   
   /**
    * Version 7 : Ajout d'une column family dans le Keyspace "SAE"<br> :
    *    <ul>
    *       <li>ReferentielFormat</li>
    *    </ul>   
    *        
    */
   public final void updateToVersion7() {

      long version = saeDao.getDatabaseVersion();
      
      if (version >= VERSION_7) {
         LOG.info("La base de données est déja en version " + version);
         return;
      }

      LOG.info("Mise à jour du keyspace SAE en version 7 pour référentiel des formats");

      // Si le KeySpace SAE n'existe pas, on quitte
      // En effet, il aurait du être créé lors de l'install du lot SAE-140400
      KeyspaceDefinition keyspaceDef = saeDao.describeKeyspace();
      if (keyspaceDef == null) {
         throw new MajLotRuntimeException("Le Keyspace " + ksName
               + " n'existe pas !");
      }

      // On se connecte au keyspace
      saeDao.connectToKeySpace();

      // Liste contenant la définition des column families à créer
      List<ColumnFamilyDefinition> cfDefs = new ArrayList<ColumnFamilyDefinition>();

      // ReferentielFormat
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName,
            REFERENTIEL_FORMAT, ComparatorType.UTF8TYPE));
      // DroitPagmf
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, DROIT_PAGMF,
            ComparatorType.UTF8TYPE));

      // DroitFormatControlProfil
      cfDefs.add(HFactory.createColumnFamilyDefinition(ksName, DROIT_FORMAT_CONTROL_PROFIL,
            ComparatorType.UTF8TYPE));
      

      // Création des CF
      saeCassandraService.createColumnFamilyFromList(cfDefs, true);

      // ajout données pour le controle du format FMT/354
      InsertionDonnees donnees = new InsertionDonnees(saeDao.getKeyspace());
      donnees.addFormatControleProfil();
      donnees.addDroits();
      
      // ajout de la colonne dispo pour les metadonnées
      List<String> listeRows = getRowsToUpdateMetaForVersion7("metadata140400.txt");
      donnees.addColumnClientAvailableMetadata(listeRows);

      // On positionne la version à 7
      saeDao.setDatabaseVersion(VERSION_7);

   }

   /**
    * Methode permettant de lire le fichier passé en entrée et de retourner la
    * liste des codes long des Metadata. Cette liste servira pour l'ajout d'une
    * colonne pour ces metadonnées.
    * 
    * @param fichierlisteMeta
    *           nom du fichier à lire
    * @return List<String> : liste des code long des métadonnées
    */
   private List<String> getRowsToUpdateMetaForVersion7(String fichierlisteMeta) {
      InputStream stream = null;
      BufferedReader reader = null;
      List<String> listeCodeLong = new ArrayList<String>();

      try {
         stream = context.getResource(fichierlisteMeta).getInputStream();
         reader = new BufferedReader(new InputStreamReader(stream));
         
         // on lit le fichier ligne à ligne 
         // (en principe, il n'y a qu'une ligne)
         String ligne;
         while ((ligne=reader.readLine())!=null){
            // pour chaque ligne, on recupere chaque code long
            // (pour info, sur chaque ligne, les codes long sont séparés par des virgules)
            Collections.addAll(listeCodeLong, ligne.split(","));
         }
      } catch (IOException e) {
         LOG.warn("impossible de récupérer le fichier contenant les données");
      } finally {
         if (reader != null) {
            try {
               reader.close();
            } catch (IOException e) {
               LOG.debug("impossible de fermer le reader");
            }
         }

         if (stream != null) {
            try {
               stream.close();
            } catch (IOException e) {
               LOG.debug("impossible de fermer le flux de données");
            }

         }
      }
      return listeCodeLong;
   }
   
   
   
   
}
