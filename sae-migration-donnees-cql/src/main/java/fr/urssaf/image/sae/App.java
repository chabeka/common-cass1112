package fr.urssaf.image.sae;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.javers.common.collections.Arrays;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.ListCompareAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.sae.commons.MigrationParameters;
import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.commons.utils.Constantes;
import fr.urssaf.image.sae.droit.MigrationActionUnitaire;
import fr.urssaf.image.sae.droit.MigrationContratService;
import fr.urssaf.image.sae.droit.MigrationFormatControlProfil;
import fr.urssaf.image.sae.droit.MigrationPagm;
import fr.urssaf.image.sae.droit.MigrationPagma;
import fr.urssaf.image.sae.droit.MigrationPagmf;
import fr.urssaf.image.sae.droit.MigrationPagmp;
import fr.urssaf.image.sae.droit.MigrationPrmd;
import fr.urssaf.image.sae.format.MigrationReferentielFormat;
import fr.urssaf.image.sae.jobspring.MigrationJobExecution;
import fr.urssaf.image.sae.jobspring.MigrationJobExecutionToJobStep;
import fr.urssaf.image.sae.jobspring.MigrationJobExecutions;
import fr.urssaf.image.sae.jobspring.MigrationJobExecutionsRunning;
import fr.urssaf.image.sae.jobspring.MigrationJobInstance;
import fr.urssaf.image.sae.jobspring.MigrationJobInstancesByName;
import fr.urssaf.image.sae.jobspring.MigrationJobStep;
import fr.urssaf.image.sae.jobspring.MigrationJobSteps;
import fr.urssaf.image.sae.jobspring.MigrationJobinstanceToJobExecution;
import fr.urssaf.image.sae.metadata.MigrationDictionary;
import fr.urssaf.image.sae.metadata.MigrationMetadata;
import fr.urssaf.image.sae.modeapi.ModeAPI;
import fr.urssaf.image.sae.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.piletravaux.MigrationJobHistory;
import fr.urssaf.image.sae.piletravaux.MigrationJobQueue;
import fr.urssaf.image.sae.piletravaux.MigrationJobRequest;
import fr.urssaf.image.sae.rnd.MigrationCorrespondancesRnd;
import fr.urssaf.image.sae.rnd.MigrationRnd;
import fr.urssaf.image.sae.spring.batch.MigrationSequences;
import fr.urssaf.image.sae.trace.MigrationTraceDestinataire;
import fr.urssaf.image.sae.trace.MigrationTraceJournalEvt;
import fr.urssaf.image.sae.trace.MigrationTraceRegExploitation;
import fr.urssaf.image.sae.trace.MigrationTraceRegSecurite;
import fr.urssaf.image.sae.trace.MigrationTraceRegTechnique;

/**
 * Classe permettant de faire les migration des colonnes famillies (CF) La
 * classe prend trois parametres en "entrée" à savoir:<br>
 * -- Le chemin du fichier de configuration de context spring:
 * <b>Obligation</b><br>
 * -- Le nom de la CF: <b>Obligation</b><br>
 * -- Le sens de la migration: <b>Obligation</b><br>
 * Le sens de la migration est soit de <b>Thrift vers cql</b> (nom de variable
 * <b>THRIFT_TO_CQL</b>) ou de <b>cql vers Thrift</b> (nom de variable
 * <b>CQL_TO_THRIFT</b>)<br>
 * Le nom des CF correspond aux noms des CF de l'api Thrift c'est à dire les nom
 * des tables avant la migration.
 */
public class App {

  /**
   * LOGGER
   */
  private static Logger LOG = null;

  /**
   * sens de la migration de la table thrift vers la table cql
   */
  private static final String THRIFT_TO_CQL = "THRIFT_TO_CQL";

  /**
   * sens de la migration de la table cql vers la table thrift
   */
  private static final String CQL_TO_THRIFT = "CQL_TO_THRIFT";

  private static String MESSAGE_DONNEES_DIFF = "Les donnees thrift et cql pour {} sont différentes";
  private static String MESSAGE_NON_IMPL="Le  traitement pour  la  table {} n'est pas implémenté";

  public static void main(final String[] args) throws Exception {

    // System.setProperty(ContextInitializer.CONFIG_FILE_PROPERTY, "/hawai/data/ged/sae-lotinstallmaj/logback-sae-migration-donnees.xml");
    LOG = LoggerFactory.getLogger(App.class);
    final long debutMigration = Calendar.getInstance().getTimeInMillis();


    App.LOG.info(" ___________________________");
    App.LOG.info("|                           |");
    App.LOG.info("|  ENTREE PROGRAMME - MAIN  |");
    App.LOG.info("|___________________________|");

    try {
      if (args.length == 3) {
        // Extrait les infos de la ligne de commandes
        final String cheminFicConfSae = args[0];

        // Démarrage du contexte spring
        final ApplicationContext context = App.startContextSpring(cheminFicConfSae);

        //Build Javers instance avec algorithme simple

        final Javers javers = JaversBuilder
            .javers()
            .withListCompareAlgorithm(ListCompareAlgorithm.SIMPLE)
            .build();

        // le nom de colonne familly
        final String cfName = args[1];
        final String migrateTo = args[2]; // le sens de la migration ==> de cql vers thrift ou contraire

        if (!App.THRIFT_TO_CQL.equals(migrateTo) && !App.CQL_TO_THRIFT.equals(migrateTo)) {
          App.LOG.info(" ______________________________________________________________");
          App.LOG.info("|                                                              |");
          App.LOG.info("|           ERREUR SUR LE NOM DU SENS DE LA MIGRATION          |");
          App.LOG.info("|______________________________________________________________|");
          App.LOG.info("|   Le string designant le sens  de la migration est incorrect.|");
          App.LOG.info("|   Les valeurs possibles sont:                                |");
          App.LOG.info("|    - THRIFT_TO_CQL                                           |");
          App.LOG.info("|    - CQL_TO_THRIFT                                           |");
          App.LOG.info("|______________________________________________________________|");

          // on sort du programme
          App.finProgramme();
        }
        final boolean all = "ALL".equals(cfName);

        // ############################################################################################
        // ################### Initialisation des flags modeAPI en mode DUAL THRIFT
        // ###################
        // ############################################################################################

        final ModeApiCqlSupport modeApiCqlSupport = context.getBean(ModeApiCqlSupport.class);

        App.LOG.info(" _____________________________________________");
        App.LOG.info("|                                             |");
        App.LOG.info("|  DEBUT MIGRATION                            |");
        App.LOG.info("|_____________________________________________|");

        // Les tables sont dans un ordre spécifique que l'on modifier
        final String[] tabCfName = { Constantes.CF_DROIT_ACTION_UNITAIRE, Constantes.CF_DROIT_CONTRAT_SERVICE,
                                     Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL, Constantes.CF_DROIT_PAGM, Constantes.CF_DROIT_PAGMA,
                                     Constantes.CF_DROIT_PAGMF, Constantes.CF_DROIT_PAGMP, Constantes.CF_DROIT_PRMD,
                                     Constantes.CF_PARAMETERS, Constantes.CF_METADATA, Constantes.CF_DICTIONARY,
                                     Constantes.CF_REFERENTIEL_FORMAT, Constantes.CF_RND, Constantes.CF_CORRESPONDANCES_RND,
                                     Constantes.CF_SEQUENCES, Constantes.CF_JOB_HISTORY, Constantes.CF_JOB_REQUEST,
                                     Constantes.CF_JOBS_QUEUE, Constantes.CF_JOBINSTANCE, Constantes.CF_JOBINSTANCES_BY_NAME,
                                     Constantes.CF_JOBEXECUTION, Constantes.CF_JOBEXECUTIONS, Constantes.CF_JOBINSTANCE_TO_JOBEXECUTION,
                                     Constantes.CF_JOBSTEP, Constantes.CF_JOBSTEPS, Constantes.CF_JOBEXECUTION_TO_JOBSTEP,
                                     Constantes.CF_JOBEXECUTIONS_RUNNING, Constantes.CF_TRACE_DESTINATAIRE,
                                     Constantes.CF_TRACE_REG_EXPLOITATION, Constantes.CF_TRACE_REG_EXPLOITATION_INDEX,
                                     Constantes.CF_TRACE_JOURNAL_EVT, Constantes.CF_TRACE_JOURNAL_EVT_INDEX,
                                     Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC, Constantes.CF_TRACE_REG_TECHNIQUE,
                                     Constantes.CF_TRACE_REG_TECHNIQUE_INDEX,
                                     Constantes.CF_TRACE_REG_SECURITE, Constantes.CF_TRACE_REG_SECURITE_INDEX };

        final int nbTablesAMigrer = tabCfName.length;


        int nbTablesTraitees = 0;
        // Migration de toutes les tables ou d'une table spécifique
        if (all) {
          // On migre toutes les tables
          for (final String cfNameTemp : tabCfName) {
            nbTablesTraitees += 1;
            App.migrationCfName(context, migrateTo, modeApiCqlSupport, cfNameTemp, javers);
          }
        } else {
          if (Arrays.asList(tabCfName).contains(cfName)) {
            nbTablesTraitees = 1;
            App.migrationCfName(context, migrateTo, modeApiCqlSupport, cfName, javers);
          } else {
            App.LOG.info(" _________________________________________________________");
            App.LOG.info("|                                                         |");
            App.LOG.info("|  ERREUR: PROBLEME DE PARAMETRE                          |");
            App.LOG.info("|_________________________________________________________|");
            App.LOG.info("|             Cfname incorrect                            |");
            App.LOG.info("|_________________________________________________________|");
          }
        }
        final List<ModeAPI> modeAPIs = modeApiCqlSupport.findAll();
        App.resumeMigration(nbTablesAMigrer, nbTablesTraitees, modeAPIs, debutMigration);
      } else {

        App.LOG.info(" _________________________________________________________");
        App.LOG.info("|                                                         |");
        App.LOG.info("|  ERREUR: PROBLEME DE FICHIERS DE CONGIGURATION          |");
        App.LOG.info("|_________________________________________________________|");
        App.LOG.info("|  Il faut préciser, dans la ligne de commande, le chemin |");
        App.LOG.info("|  complet du fichier de configuration du SAE.            |");
        App.LOG.info("|_________________________________________________________|");
        // on sort du programme

      }

      App.finProgramme();
    } catch (final Exception e) {
      App.LOG.info("Exception:" + e.getCause());
      System.exit(1);
    }
  }

  /**
   * @param context
   * @param cfName
   * @param migrateTo
   * @param modeApiCqlSupport
   * @param cfNameTemp
   */
  private static void migrationCfName(final ApplicationContext context, final String migrateTo, final ModeApiCqlSupport modeApiCqlSupport,
                                      final String cfName, final Javers javers) {

    // On effectue la migration que si le mode n'est pas cql (dual ou non)
    if (modeApiCqlSupport.isModeThriftOrDualThrift(cfName)) {
      final DiffM diffM = App.migration(context, cfName, modeApiCqlSupport, migrateTo, javers);
      if (diffM.getDiff() != null && !diffM.getDiff().hasChanges() || diffM.isResultCompare()) {
        if (diffM.getDiff() != null && !diffM.getDiff().hasChanges()) {
          App.LOG.info("Les donnees thrift et cql sont identiques pour la table: {}", cfName);
        }
        // On passe en mode dual cql
        App.setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_CQL);
        // temporisation
        App.temporisation();
        modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, cfName);
        App.logMode(cfName, MODE_API.DATASTAX);

      } else {
        if (diffM.getDiff() != null) {
          App.LOG.warn(App.MESSAGE_DONNEES_DIFF, cfName);
          App.LOG.warn(diffM.getDiff().prettyPrint());
        } else {
          if (!diffM.isResultCompare() && !diffM.getMessage().isEmpty()) {
            App.LOG.warn(diffM.getMessage(),cfName);
          } else {
            App.LOG.warn(App.MESSAGE_DONNEES_DIFF, cfName);
          }
        }
      }
    } else {
      App.logTableDejaMigree(cfName);
    }
  }

  /**
   * 
   */
  private static void finProgramme() {
    App.LOG.info(" ___________________________");
    App.LOG.info("|                           |");
    App.LOG.info("|    FIN PROGRAMME - MAIN   |");
    App.LOG.info("|___________________________|");
    System.exit(0);
  }

  /**
   * Set le modeAPI de la CF spécifiée
   * 
   * @param modeApiCqlSupport
   * @param cfName
   */
  private static void setModeApiCF(final ModeApiCqlSupport modeApiCqlSupport, final String cfName,
                                   final String modeAPI) {
    /*
     * App.LOG.info(" _____________________________________________");
     * App.LOG.info("|                                             |");
     * App.LOG.info("|          MODE {}       |", modeAPI);
     * App.LOG.info("|_____________________________________________|");
     */
    App.logMode(cfName, modeAPI);
    modeApiCqlSupport.updateModeApi(modeAPI, cfName);
  }

  /**
   * On logge la table migrée
   * 
   * @param cfName
   */
  private static void logTableDejaMigree(final String cfName) {
    final String fin = "                                                 |";
    App.LOG.info(" _____________________________________________________________________________");
    App.LOG.info("|                                                                             |");
    App.LOG.info("|          CF DEJA MIGREE: {}{}", cfName, fin.substring(cfName.length() - 2, fin.length()));
    App.LOG.info("|_____________________________________________________________________________|");

  }

  /**
   * On logge le mode
   * 
   * @param cfName
   */
  private static void logMode(final String cfName, final String mode) {
    final String fin = "                                                     |";
    App.LOG.info(" _____________________________________________________________________________");
    App.LOG.info("|                                                                             |");
    App.LOG.info("|                MODE: {} {}{}", mode, cfName, fin.substring(cfName.length() + mode.length() - 1, fin.length()));
    App.LOG.info("|_____________________________________________________________________________|");

  }

  /**
   * 
   */
  private static void temporisation() {
    App.LOG.info(" _____________________________________________");
    App.LOG.info("|                                             |");
    App.LOG.info("|  TEMPORISATION 1 min                        |");
    App.LOG.info("|_____________________________________________|");
    try {
      Thread.sleep(60000);
    } catch (final InterruptedException e) {
      System.out.println("InterruptedException : " + e.getMessage());
    }
  }

  /**
   * Démarage du contexte Spring
   *
   * @param cheminFicConfSae le chemin du fichier de configuration principal du
   *                         sae (sae-config.properties)
   * @return le contexte Spring
   */
  protected static ApplicationContext startContextSpring(final String cheminFicConfSae) {

    final String contextConfig = "/applicationContext-cassandra-poc.xml";

    return ContextFactory.createSAEApplicationContext(contextConfig, cheminFicConfSae);

  }

  private static DiffM migration(final ApplicationContext context, final String cfName,
                                 final ModeApiCqlSupport modeApiCqlSupport, final String migrateTo, final Javers javers) {

    final DiffM diffM = new DiffM();

    try {
      // On passe en mode dual thrift pour que l'écriture se fasse aussi en cql
      App.setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_THRIFT);
      // temporisation
      App.temporisation();

      /*
       * Pour chaque table spécifiée on effectue la migration, et on n'effectue la comparaison pour vérifier les données
       * que sur les tables avec un faible volume de données. On n'effectue donc pas la comparaison pour toutes tables de
       * trace (excepté TraceDestinataires) ainsi que pour JobHistory.
       * Pour les tables suivantes: correspondancesrnd, dictionary, droitactionunitaire,
       * droitcontratservice, droitformatcontrolprofil,droitpagm, droitpagma, droitpagmf,
       * droitpagmp, droitprmd, metadata,parameters,referentielformat, rnd, sequences,
       * la comparaison se fait à la suite de la migration (dans la même méthode et c'est Javers qui est utilisé pour
       * la comparaison)
       */
      switch (cfName) {

      // Droits
      case Constantes.CF_DROIT_ACTION_UNITAIRE:
        final MigrationActionUnitaire migrationActionUnitaire = context.getBean(MigrationActionUnitaire.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationActionUnitaire.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationActionUnitaire.migrationFromCqlTothrift(javers));
        }
        break;

      case Constantes.CF_DROIT_CONTRAT_SERVICE:
        final MigrationContratService migrationContratService = context.getBean(MigrationContratService.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationContratService.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationContratService.migrationFromCqlTothrift(javers));
        }
        break;

      case Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL:
        final MigrationFormatControlProfil migrationFormatControlProfil = context
        .getBean(MigrationFormatControlProfil.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationFormatControlProfil.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationFormatControlProfil.migrationFromCqlTothrift(javers));
        }
        break;

      case Constantes.CF_DROIT_PAGM:
        final MigrationPagm migrationPagm = context.getBean(MigrationPagm.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationPagm.migrationFromThriftToCql());

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationPagm.migrationFromCqlTothrift());
        }
        break;

      case Constantes.CF_DROIT_PAGMA:
        final MigrationPagma migrationPagma = context.getBean(MigrationPagma.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationPagma.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationPagma.migrationFromCqlTothrift(javers));
        }
        break;

      case Constantes.CF_DROIT_PAGMF:
        final MigrationPagmf migrationPagmf = context.getBean(MigrationPagmf.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationPagmf.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationPagmf.migrationFromCqlTothrift(javers));
        }
        break;

      case Constantes.CF_DROIT_PAGMP:
        final MigrationPagmp migrationPagmp = context.getBean(MigrationPagmp.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationPagmp.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationPagmp.migrationFromCqlTothrift(javers));
        }
        break;

      case Constantes.CF_DROIT_PRMD:
        final MigrationPrmd migrationPrmd = context.getBean(MigrationPrmd.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationPrmd.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationPrmd.migrationFromCqlTothrift(javers));
        }
        break;

        // Parameters
      case Constantes.CF_PARAMETERS:
        final MigrationParameters migrationParameters = context.getBean(MigrationParameters.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationParameters.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationParameters.migrationFromCqlTothrift(javers));
        }
        break;

        // Format
      case Constantes.CF_REFERENTIEL_FORMAT:
        final MigrationReferentielFormat migrationReferentielFormat = context.getBean(MigrationReferentielFormat.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationReferentielFormat.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationReferentielFormat.migrationFromCqlTothrift(javers));
        }
        break;

        // Metadata
      case Constantes.CF_METADATA:
        final MigrationMetadata migrationMetadata = context.getBean(MigrationMetadata.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationMetadata.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationMetadata.migrationFromCqlTothrift(javers));
        }
        break;

        // Dictionary
      case Constantes.CF_DICTIONARY:
        final MigrationDictionary migrationDictionary = context.getBean(MigrationDictionary.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationDictionary.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationDictionary.migrationFromCqlTothrift(javers));
        }
        break;

        // Rnd
      case Constantes.CF_RND:
        final MigrationRnd migrationRnd = context.getBean(MigrationRnd.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationRnd.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationRnd.migrationFromCqlTothrift(javers));
        }
        break;

      case Constantes.CF_CORRESPONDANCES_RND:
        final MigrationCorrespondancesRnd migrationCorrespondancesRnd = context
        .getBean(MigrationCorrespondancesRnd.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          diffM.setDiff(migrationCorrespondancesRnd.migrationFromThriftToCql(javers));

        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setDiff(migrationCorrespondancesRnd.migrationFromCqlTothrift(javers));
        }
        break;

        // Séquences

      case Constantes.CF_SEQUENCES:
        final MigrationSequences migrationSequences = context.getBean(MigrationSequences.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationSequences.migrationFromThriftToCql();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationSequences.migrationFromCqlTothrift();
        }
        diffM.setDiff(migrationSequences.compareSequences());
        break;

        // Piles de travaux

      case Constantes.CF_JOB_HISTORY:
        final MigrationJobHistory migrationJobHistory = context.getBean(MigrationJobHistory.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobHistory.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationJobHistory.compareJobHistoryCql();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobHistory.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationJobHistory.compareJobHistoryCql();
        }
        break;

      case Constantes.CF_JOB_REQUEST:
        final MigrationJobRequest migrationJobRequest = context.getBean(MigrationJobRequest.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobRequest.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          diffM.setResultCompare(migrationJobRequest.compareJobRequestCql());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobRequest.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobRequest.compareJobRequestCql());
        }
        break;

      case Constantes.CF_JOBS_QUEUE:
        final MigrationJobQueue migrationJobQueue = context.getBean(MigrationJobQueue.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobQueue.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobQueue.compareJobQueueCql());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobQueue.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobQueue.compareJobQueueCql());
        }
        break;

        // Jobs Spring Batch

      case Constantes.CF_JOBINSTANCE:
        final MigrationJobInstance migrationJobInstance = context.getBean(MigrationJobInstance.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobInstance.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobInstance.compareJobInstance());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobInstance.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobInstance.compareJobInstance());
        }
        break;

      case Constantes.CF_JOBINSTANCES_BY_NAME:
        final MigrationJobInstancesByName migrationJobInstancesByName = context
        .getBean(MigrationJobInstancesByName.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobInstancesByName.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobInstancesByName.compareJobInstanceByName());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobInstancesByName.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobInstancesByName.compareJobInstanceByName());
        }
        break;

      case Constantes.CF_JOBEXECUTION:
        final MigrationJobExecution migrationJobExecution = context.getBean(MigrationJobExecution.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobExecution.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecution.compareJobExecution());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobExecution.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecution.compareJobExecution());
        }
        break;

      case Constantes.CF_JOBEXECUTIONS:
        final MigrationJobExecutions migrationJobExecutions = context.getBean(MigrationJobExecutions.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobExecutions.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecutions.compareJobExecutions());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobExecutions.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecutions.compareJobExecutions());
        }
        break;

      case Constantes.CF_JOBSTEP:
        final MigrationJobStep migrationJobStep = context.getBean(MigrationJobStep.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobStep.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobStep.compareJobStepCql());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobStep.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobStep.compareJobStepCql());
        }
        break;

      case Constantes.CF_JOBSTEPS:
        final MigrationJobSteps migrationJobSteps = context.getBean(MigrationJobSteps.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobSteps.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobSteps.compareJobStepsCql());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobSteps.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobSteps.compareJobStepsCql());
        }
        break;

      case Constantes.CF_JOBEXECUTION_TO_JOBSTEP:
        final MigrationJobExecutionToJobStep migrationJobExecutionToJobStep = context
        .getBean(MigrationJobExecutionToJobStep.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobExecutionToJobStep.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecutionToJobStep.compareJobExecutionsToStep());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobExecutionToJobStep.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecutionToJobStep.compareJobExecutionsToStep());
        }
        break;

      case Constantes.CF_JOBINSTANCE_TO_JOBEXECUTION:
        final MigrationJobinstanceToJobExecution migrationJobinstanceToJobExecution = context
        .getBean(MigrationJobinstanceToJobExecution.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobinstanceToJobExecution.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobinstanceToJobExecution.compareJobInstanceToExecution());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobinstanceToJobExecution.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobinstanceToJobExecution.compareJobInstanceToExecution());
        }
        break;

      case Constantes.CF_JOBEXECUTIONS_RUNNING:
        final MigrationJobExecutionsRunning migrationJobExecutionsRunning = context
        .getBean(MigrationJobExecutionsRunning.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationJobExecutionsRunning.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecutionsRunning.compareJobExecutionsRunning());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationJobExecutionsRunning.migrationFromCqlTothrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(migrationJobExecutionsRunning.compareJobExecutionsRunning());
        }
        break;

        // Traces

      case Constantes.CF_TRACE_DESTINATAIRE:
        final MigrationTraceDestinataire migrationTraceDestinataire = context
        .getBean(MigrationTraceDestinataire.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          // On compare seulement la taille
          diffM.setResultCompare(migrationTraceDestinataire.migrationFromThriftToCql().isResultCompare());
          diffM.setResultMigration(true);
          // diffM.setResultCompare(migrationTraceDestinataire.compareTraceDestinataireFromCQlandThrift());
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          diffM.setResultCompare(migrationTraceDestinataire.migrationFromCqlTothrift().isResultCompare());
          diffM.setResultMigration(true);

        }
        break;

      case Constantes.CF_TRACE_REG_EXPLOITATION:
        final MigrationTraceRegExploitation migrationTraceRegExploitation = context
        .getBean(MigrationTraceRegExploitation.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceRegExploitation.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegExploitation.traceComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceRegExploitation.migrationFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegExploitation.traceComparator();
        }
        break;

      case Constantes.CF_TRACE_REG_EXPLOITATION_INDEX:
        final MigrationTraceRegExploitation migrationTraceRegExploitationIndex = context
        .getBean(MigrationTraceRegExploitation.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceRegExploitationIndex.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result=migrationTraceRegExploitationIndex.traceComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceRegExploitationIndex.migrationFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result=migrationTraceRegExploitationIndex.traceComparator();
        }
        break;

      case Constantes.CF_TRACE_JOURNAL_EVT:
        final MigrationTraceJournalEvt migrationTraceJournalEvt = context
        .getBean(MigrationTraceJournalEvt.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceJournalEvt.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceJournalEvt.traceComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceJournalEvt.migrationFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceJournalEvt.traceComparator();
        }
        break;

      case Constantes.CF_TRACE_JOURNAL_EVT_INDEX:
        final MigrationTraceJournalEvt migrationTraceJournalEvtIndex = context
        .getBean(MigrationTraceJournalEvt.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceJournalEvtIndex.migIndexFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceJournalEvtIndex.indexComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceJournalEvtIndex.migrationIndexFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceJournalEvtIndex.indexComparator();
        }
        break;

      case Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC:
        final MigrationTraceJournalEvt migrationTraceJournalEvtIndexDoc = context
        .getBean(MigrationTraceJournalEvt.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceJournalEvtIndexDoc.migrationIndexDocFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceJournalEvtIndexDoc.indexDocComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceJournalEvtIndexDoc.migrationIndexDocFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceJournalEvtIndexDoc.indexDocComparator();
        }
        break;

      case Constantes.CF_TRACE_REG_TECHNIQUE:
        final MigrationTraceRegTechnique migrationTraceRegTechnique = context
        .getBean(MigrationTraceRegTechnique.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceRegTechnique.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegTechnique.traceComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceRegTechnique.migrationFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegTechnique.traceComparator();
        }
        break;

      case Constantes.CF_TRACE_REG_TECHNIQUE_INDEX:
        final MigrationTraceRegTechnique migrationTraceRegTechniqueIndex = context
        .getBean(MigrationTraceRegTechnique.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceRegTechniqueIndex.migrationIndexFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegTechniqueIndex.indexComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceRegTechniqueIndex.migrationIndexFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegTechniqueIndex.indexComparator();
        }
        break;

      case Constantes.CF_TRACE_REG_SECURITE:
        final MigrationTraceRegSecurite migrationTraceRegSecurite = context
        .getBean(MigrationTraceRegSecurite.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceRegSecurite.migrationFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegSecurite.traceComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceRegSecurite.migrationFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegSecurite.traceComparator();
        }
        break;

      case Constantes.CF_TRACE_REG_SECURITE_INDEX:
        final MigrationTraceRegSecurite migrationTraceRegSecuriteIndex = context
        .getBean(MigrationTraceRegSecurite.class);
        if (App.THRIFT_TO_CQL.equals(migrateTo)) {
          migrationTraceRegSecuriteIndex.migrationIndexFromThriftToCql();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegSecuriteIndex.indexComparator();
        } else if (App.CQL_TO_THRIFT.equals(migrateTo)) {
          migrationTraceRegSecuriteIndex.migrationIndexFromCqlToThrift();
          diffM.setResultMigration(true);
          diffM.setResultCompare(true);
          // result = migrationTraceRegSecuriteIndex.indexComparator();
        }
        break;

      default:
        diffM.setResultMigration(false);
        diffM.setResultCompare(false);
        diffM.setMessage(App.MESSAGE_NON_IMPL);

        break;
      }


    } catch (final Exception e) {
      diffM.setResultMigration(false);
      diffM.setResultCompare(false);
      diffM.setMessage("Exception: " + e.getMessage());
      App.LOG.error(e.getMessage());
    }
    return diffM;
  }

  private static void resumeMigration(final int nbTablesAMigrer, final int nbTablesTraitees, final List<ModeAPI> listModeAPI, final long debutMigration) {
    final String messageTablesAMigrer = "Nb theorique de tables a migrer:";
    final String messageTablesNonMigrees = "Nb de tables non migrees:";
    final String messageTablesEnCoursMigration = "Nb de tables en cours de migration:";
    final String messageTablesTraitees = "Nb de tables traitees:";
    final String messageTablesMigrees = "Nb de tables migrees:";
    final String messageDuree = "Duree de la migration :";
    final String fin = "                                                                        |";
    final String finTablesAmigrer = fin.substring(String.valueOf(nbTablesAMigrer).length() + messageTablesAMigrer.length(), fin.length());
    final String finTablesTraitees = fin.substring(String.valueOf(nbTablesTraitees).length() + messageTablesTraitees.length(), fin.length());


    final List<ModeAPI> listModeAPIMigrees = new ArrayList<>();
    final List<ModeAPI> listModeAPINonMigrees = new ArrayList<>();
    final List<ModeAPI> listModeAPIEnCoursMigration = new ArrayList<>();

    for (final ModeAPI modeAPI : listModeAPI)
    {
      if (modeAPI.getMode().equals(MODE_API.DATASTAX)) {
        listModeAPIMigrees.add(modeAPI);
      } else if (modeAPI.getMode().equals(MODE_API.HECTOR)) {
        listModeAPINonMigrees.add(modeAPI);
      } else if (modeAPI.getMode().equals(MODE_API.DUAL_MODE_READ_THRIFT)
          ) {
        listModeAPIEnCoursMigration.add(modeAPI);
      }
    }
    final String finTablesMigrees = fin.substring(String.valueOf(listModeAPIMigrees.size()).length() + messageTablesMigrees.length(),
                                                  fin.length());
    final String finTablesNonMigrees = fin.substring(String.valueOf(listModeAPINonMigrees.size()).length() + messageTablesNonMigrees.length(), fin.length());
    final String finTablesEnCoursMigration = fin.substring(String.valueOf(listModeAPIEnCoursMigration.size()).length() + messageTablesEnCoursMigration.length(),
                                                           fin.length());
    final long duree = (Calendar.getInstance().getTimeInMillis() - debutMigration) / 1000;

    final long dureeS = duree % 60;
    final long dureeM = duree / 60 % 60;
    final long dureeH = duree / 3600;
    final String chaineDuree = String.format("%d h %d min %d s", dureeH, dureeM, dureeS);
    final String finDuree = fin.substring(String.valueOf(chaineDuree).length() + messageDuree.length(), fin.length());

    App.LOG.info(" _____________________________________________________________________________");
    App.LOG.info("|                                                                             |");
    App.LOG.info("|                               RESUME MIGRATION                              |");
    App.LOG.info("|                                                                             |");
    App.LOG.info("|    {} {}{}", messageTablesAMigrer, nbTablesAMigrer, finTablesAmigrer);
    App.LOG.info("|    {} {}{}", messageTablesTraitees, nbTablesTraitees, finTablesTraitees);
    App.LOG.info("|    {} {}{}", messageTablesMigrees, listModeAPIMigrees.size(), finTablesMigrees);
    App.LOG.info("|    {} {}{}", messageTablesNonMigrees, listModeAPINonMigrees.size(), finTablesNonMigrees);
    App.LOG.info("|    {} {}{}", messageTablesEnCoursMigration, listModeAPIEnCoursMigration.size(), finTablesEnCoursMigration);
    App.LOG.info("|    {} {}{}", messageDuree, chaineDuree, finDuree);
    App.LOG.info("|_____________________________________________________________________________|");

  }


}
