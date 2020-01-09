package fr.urssaf.image.sae;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
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
import fr.urssaf.image.sae.modeapi.ModeApiCqlSupport;
import fr.urssaf.image.sae.piletravaux.MigrationJobRequest;

/**
 * Classe permettant de faire les migration des colonnes famillies (CF)
 * La classe prend trois parametres en "entrée" à savoir:<br>
 * -- Le chemin du fichier de configuration de context spring: <b>Obligation</b><br>
 * -- Le nom de la CF: <b>Obligation</b><br>
 * -- Le sens de la migration: <b>Obligation</b><br>
 * Le sens de la migration est soit de <b>Thrift vers cql</b> (nom de variable <b>THRIFT_TO_CQL</b>) ou de <b>cql vers Thrift</b> (nom de variable <b>CQL_TO_THRIFT</b>)<br>
 * Le nom des CF correspond aux noms des CF de l'api Thrift c'est à dire les nom des tables avant la migration.
 */
public class App {

  /**
   * LOGGER
   */
  private static final Logger LOG = LoggerFactory.getLogger(App.class);

  /**
   * sens de la migration de la table thrift vers la table cql
   */
  private static final String THRIFT_TO_CQL = "THRIFT_TO_CQL";

  /**
   * sens de la migration de la table cql vers la table thrift
   */
  private static final String CQL_TO_THRIFT = "CQL_TO_THRIFT";

  public static void main(final String[] args) throws Exception {

    LOG.info(" ___________________________");
    LOG.info("|                           |");
    LOG.info("|  ENTREE PROGRAMME - MAIN  |");
    LOG.info("|___________________________|");


    try {
      if (args.length == 3) {
        // Extrait les infos de la ligne de commandes
        final String cheminFicConfSae = args[0];

        // Démarrage du contexte spring
        final ApplicationContext context = startContextSpring(cheminFicConfSae);

        // le nom de colonne familly
        final String cfName = args[1];
        final String migrateTo = args[2]; // le sens de la migration ==> de cql vers thrift ou contraire

        if (!THRIFT_TO_CQL.equals(migrateTo) && !CQL_TO_THRIFT.equals(migrateTo)) {
          LOG.info(" ______________________________________________________________");
          LOG.info("|                                                              |");
          LOG.info("|           ERREUR SUR LE NOM DU SENS DE LA MIGRATION          |");
          LOG.info("|______________________________________________________________|");
          LOG.info("|   Le string designant le sens  de la migration est incorrect.|");
          LOG.info("|   Les valeurs possibles sont:                                |");
          LOG.info("|    - THRIFT_TO_CQL                                           |");
          LOG.info("|    - CQL_TO_THRIFT                                           |");
          LOG.info("|______________________________________________________________|");

          // on sort du programme
          finProgramme();
        }
        final boolean all = "ALL".equals(cfName);

        // ############################################################################################
        // ################### Initialisation des flags modeAPI en mode DUAL THRIFT ###################
        // ############################################################################################

        final ModeApiCqlSupport modeApiCqlSupport = context.getBean(ModeApiCqlSupport.class);

        LOG.info(" _____________________________________________");
        LOG.info("|                                             |");
        LOG.info("|  DEBUT MIGRATION                            |");
        LOG.info("|_____________________________________________|");


        final String[] tabCfName = {Constantes.CF_DROIT_ACTION_UNITAIRE,  Constantes.CF_DROIT_CONTRAT_SERVICE, 
                                    Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL, Constantes.CF_DROIT_PAGM, 
                                    Constantes.CF_DROIT_PAGMA,Constantes.CF_DROIT_PAGMF,  Constantes.CF_DROIT_PAGMP,
                                    Constantes.CF_DROIT_PRMD, Constantes.CF_PARAMETERS,Constantes.CF_METADATA,
                                    Constantes.CF_DICTIONARY,  Constantes.CF_REFERENTIEL_FORMAT,  Constantes.CF_RND,
                                    Constantes.CF_CORRESPONDANCES_RND,  Constantes.CF_SEQUENCES,  
                                    Constantes.CF_JOB_HISTORY,  Constantes.CF_JOB_REQUEST, Constantes.CF_JOBS_QUEUE,
                                    Constantes.CF_JOBINSTANCE, Constantes.CF_JOBINSTANCES_BY_NAME,
                                    Constantes.CF_JOBEXECUTION, Constantes.CF_JOBEXECUTIONS, Constantes.CF_JOBSTEP,
                                    Constantes.CF_JOBSTEPS, Constantes.CF_JOBEXECUTION_TO_JOBSTEP,
                                    Constantes.CF_JOBEXECUTIONS_RUNNING, Constantes.CF_TRACE_DESTINATAIRE,
                                    Constantes.CF_TRACE_REG_EXPLOITATION, Constantes.CF_TRACE_REG_EXPLOITATION_INDEX,
                                    Constantes.CF_TRACE_DESTINATAIRE,Constantes.CF_TRACE_JOURNAL_EVT,
                                    Constantes.CF_TRACE_JOURNAL_EVT_INDEX,Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC,
                                    Constantes.CF_TRACE_REG_TECHNIQUE, Constantes.CF_TRACE_REG_TECHNIQUE_INDEX,
                                    Constantes.CF_TRACE_REG_SECURITE, Constantes.CF_TRACE_REG_SECURITE_INDEX};

        if (all) {
          // On migre toutes les tables
          for (final String cfNameTemp : tabCfName) {
            // On effectue la migration que si le mode n'est pas cql (dual ou non)
            if (!modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfNameTemp)) {
              migration(context, cfNameTemp, modeApiCqlSupport, migrateTo);
            } else {
              logTableMigree(cfName);
            }
          }
        }else {
          // On migre la table spécifié par cfName que si le mode n'est pas cql (dual ou non)
          if (!modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migration(context, cfName, modeApiCqlSupport, migrateTo);
          } else {
            logTableMigree(cfName);
          }
        }

        // ##########################################################################
        // ################################ Les droits ##############################
        // ##########################################################################

        /*if ("DroitActionUnitaire".equals(cfName) || all) {

          final MigrationActionUnitaire migrationActionUnitaire = context.getBean(MigrationActionUnitaire.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            // On passe en mode dual thrift
            setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_THRIFT);
            // temporisation
            temporisation();
            // Migration
            migrationActionUnitaire.migrationFromThriftToCql();
            // On passe en mode dual cql si comparaison ok
            setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_CQL);
            // temporisation
            temporisation();
            // On passe en mode cql
            setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DATASTAX);
            //
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationActionUnitaire.migrationFromCqlTothrift();
          }

        } else if ("ContratService".equals(cfName) || all) {
          final MigrationContratService migrationContratService = context.getBean(MigrationContratService.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationContratService.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationContratService.migrationFromCqlTothrift();
          }
        } else if ("FormatControlProfil".equals(cfName) || all) {

          final MigrationFormatControlProfil migrationFormatControlProfil = context.getBean(MigrationFormatControlProfil.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationFormatControlProfil.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationFormatControlProfil.migrationFromCqlTothrift();
          }
        } else if ("DroitPagm".equals(cfName) || all) {

          final MigrationPagm migrationPagm = context.getBean(MigrationPagm.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationPagm.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationPagm.migrationFromCqlTothrift();
          }
        } else if ("DroitPagma".equals(cfName) || all) {

          final MigrationPagma migrationPagma = context.getBean(MigrationPagma.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationPagma.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationPagma.migrationFromCqlTothrift();
          }
        } else if ("DroitPagmf".equals(cfName) || all) {

          final MigrationPagmf migrationPagmf = context.getBean(MigrationPagmf.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationPagmf.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationPagmf.migrationFromCqlTothrift();
          }
        } else if ("DroitPagmp".equals(cfName) || all) {

          final MigrationPagmp migrationPagmp = context.getBean(MigrationPagmp.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationPagmp.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationPagmp.migrationFromCqlTothrift();
          }
        } else if ("DroitPrmd".equals(cfName) || all) {
          // Migration des données et comparaison THRIFT et CQL
          final MigrationPrmd migrationPrmd = context.getBean(MigrationPrmd.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationPrmd.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationPrmd.migrationFromCqlTothrift();
          }
        }
        // ##########################################################################
        // ###################### Les parameters (commons) ##############################
        // ##########################################################################

        if ("Parameters".equals(cfName) || all) {
          final MigrationParameters migrationParameters = context.getBean(MigrationParameters.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationParameters.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationParameters.migrationFromCqlTothrift();
          }
        }
        // ##########################################################################
        // ###################### Les metadata ##############################
        // ##########################################################################

        if ("Metadata".equals(cfName) || all) {
          final MigrationMetadata migrationMetadata = context.getBean(MigrationMetadata.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationMetadata.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationMetadata.migrationFromCqlTothrift();
          }
        } else if ("Dictionary".equals(cfName) || all) {

          final MigrationDictionary migrationDictionary = context.getBean(MigrationDictionary.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationDictionary.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationDictionary.migrationFromCqlTothrift();
          }
        }

        // ##########################################################################
        // ###################### Referentiel format ##############################
        // ##########################################################################

        if ("ReferentielFormat".equals(cfName) || all) {
          final MigrationReferentielFormat migrationReferentielFormat = context.getBean(MigrationReferentielFormat.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationReferentielFormat.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationReferentielFormat.migrationFromCqlTothrift();
          }
        }

        // ##########################################################################
        // ###################### Rnd ##############################
        // ##########################################################################

        if ("Rnd".equals(cfName) || all) {
          final MigrationRnd migrationRnd = context.getBean(MigrationRnd.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationRnd.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationRnd.migrationFromCqlTothrift();
          }
        } else if ("CorrespondancesRnd".equals(cfName) || all) {

          final MigrationCorrespondancesRnd migrationCorrespondancesRnd = context.getBean(MigrationCorrespondancesRnd.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationCorrespondancesRnd.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationCorrespondancesRnd.migrationFromCqlTothrift();
          }
        }

        // ##########################################################################
        // ###################### Sequences ##############################
        // ##########################################################################

        if ("Sequences".equals(cfName) || all) {
          final MigrationSequences migrationSequences = context.getBean(MigrationSequences.class);
          // Migration des données et comparaison THRIFT et CQL
          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationSequences.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationSequences.migrationFromCqlTothrift();
          }
        }

        // ##########################################################################
        // ###################### Les piles de travaux ##############################
        // ##########################################################################

        if ("JobHistory".equals(cfName) || all) {
          final MigrationJobHistory migrationJobHistory = context.getBean(MigrationJobHistory.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobHistory.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobHistory.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobHistory.compareJobHistoryCql();

        } else if (Constantes.CF_JOB_REQUEST.equals(cfName) || all) {
          final MigrationJobRequest migrationJobRequest = context.getBean(MigrationJobRequest.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {

            // On passe en mode dual thrift
            setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_THRIFT);
            // temporisation
            temporisation();
            migrationJobRequest.migrationFromThriftToCql();
            // Comparaison des données THRIFT et CQL
            if (migrationJobRequest.compareJobRequestCql()) {
              // On passe en mode dual cql si comparaison ok
              setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_CQL);
              // temporisation
              temporisation();
              // On passe en mode cql
              setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DATASTAX);
            }

          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobRequest.migrationFromCqlTothrift();
          }

        } else if (Constantes.CF_JOBS_QUEUE.equals(cfName) || all) {

          final MigrationJobQueue migrationJobQueue = context.getBean(MigrationJobQueue.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobQueue.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobQueue.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobQueue.compareJobQueueCql();

        }
        // ##########################################################################
        // ###################### Les job spring batch ##############################
        // ##########################################################################

        if (Constantes.CF_JOBINSTANCE.equals(cfName) || all) {
          final MigrationJobInstance migrationJobInstance = context.getBean(MigrationJobInstance.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobInstance.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobInstance.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobInstance.compareJobInstance();
        }

        if (Constantes.CF_JOBINSTANCES_BY_NAME.equals(cfName) || all) {
          final MigrationJobInstancesByName migrationJobInstancesByName = context.getBean(MigrationJobInstancesByName.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobInstancesByName.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobInstancesByName.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobInstancesByName.compareJobInstanceByName();
        }

        if (Constantes.CF_JOBEXECUTION.equals(cfName) || all) {
          final MigrationJobExecution migrationJobExecution = context.getBean(MigrationJobExecution.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobExecution.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobExecution.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobExecution.compareJobExecution();
        }

        if (Constantes.CF_JOBEXECUTIONS.equals(cfName) || all) {
          final MigrationJobExecutions migrationJobExecutions = context.getBean(MigrationJobExecutions.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobExecutions.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobExecutions.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobExecutions.compareJobExecutions();
        }

        if (Constantes.CF_JOBEXECUTIONS.equals(cfName) || all) {
          final MigrationJobStep migrationJobStep = context.getBean(MigrationJobStep.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobStep.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobStep.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobStep.compareJobStepCql();
        }
        if (Constantes.CF_JOBSTEPS.equals(cfName) || all) {
          final MigrationJobSteps migrationJobSteps = context.getBean(MigrationJobSteps.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobSteps.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobSteps.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobSteps.compareJobStepsCql();
        }
        if (Constantes.CF_JOBEXECUTION_TO_JOBSTEP.equals(cfName) || all) {
          final MigrationJobExecutionToJobStep migrationJobExecutionToJobStep = context.getBean(MigrationJobExecutionToJobStep.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobExecutionToJobStep.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobExecutionToJobStep.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobExecutionToJobStep.compareJobExecutionsToStep();
        }
        if (Constantes.CF_JOBINSTANCE_TO_JOBEXECUTION.equals(cfName) || all) {
          final MigrationJobinstanceToJobExecution migrationJobinstanceToJobExecution = context.getBean(MigrationJobinstanceToJobExecution.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobinstanceToJobExecution.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobinstanceToJobExecution.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobinstanceToJobExecution.compareJobInstanceToExecution();
        }
        if (Constantes.CF_JOBEXECUTIONS_RUNNING.equals(cfName) || all) {
          final MigrationJobExecutionsRunning migrationJobExecutionsRunning = context.getBean(MigrationJobExecutionsRunning.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationJobExecutionsRunning.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationJobExecutionsRunning.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationJobExecutionsRunning.compareJobExecutionsRunning();
        }

        // ##########################################################################
        // ################################ Les Traces ##############################
        // ##########################################################################

        // Trace destinataire

        if (Constantes.CF_TRACE_DESTINATAIRE.equals(cfName) || all) {
          final MigrationTraceDestinataire migrationTraceDestinataire = context.getBean(MigrationTraceDestinataire.class);

          if (CQL_TO_THRIFT.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceDestinataire.migrationFromThriftToCql();
          } else if (THRIFT_TO_CQL.equals(migrateTo)) {
            migrationTraceDestinataire.migrationFromCqlTothrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceDestinataire.comparTraceDestinataireFromCQlandThrift();

        }

        // Trace reg exploitation

        if (Constantes.CF_TRACE_REG_EXPLOITATION.equals(cfName) || all) {
          final MigrationTraceRegExploitation migrationTraceRegExploitation = context.getBean(MigrationTraceRegExploitation.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceRegExploitation.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceRegExploitation.migrationFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceRegExploitation.traceComparator();
        }

        // Trace reg exploitation index

        if (Constantes.CF_TRACE_REG_EXPLOITATION_INDEX.equals(cfName) || all) {
          final MigrationTraceRegExploitation migrationTraceRegExploitation = context.getBean(MigrationTraceRegExploitation.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceRegExploitation.migrationIndexFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceRegExploitation.migrationIndexFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceRegExploitation.indexComparator();
        }

        // trace reg journal
        if (Constantes.CF_TRACE_JOURNAL_EVT.equals(cfName) || all) {
          final MigrationTraceJournalEvt migrationTraceJournalEvt = context.getBean(MigrationTraceJournalEvt.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceJournalEvt.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceJournalEvt.migrationFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceJournalEvt.traceComparator();

        }

        // trace reg journal index
        if (Constantes.CF_TRACE_JOURNAL_EVT_INDEX.equals(cfName) || all) {
          final MigrationTraceJournalEvt migrationTraceJournalEvt = context.getBean(MigrationTraceJournalEvt.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceJournalEvt.migIndexFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceJournalEvt.migrationIndexFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceJournalEvt.indexComparator();

        }

        // trace reg journal index doc
        if (Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC.equals(cfName) || all) {
          final MigrationTraceJournalEvt migrationTraceJournalEvt = context.getBean(MigrationTraceJournalEvt.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {

            migrationTraceJournalEvt.migrationIndexDocFromThriftToCql();

          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceJournalEvt.migrationIndexDocFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceJournalEvt.indexDocComparator();
        }

        // trace reg Technique
        if (Constantes.CF_TRACE_REG_TECHNIQUE.equals(cfName) || all) {
          final MigrationTraceRegTechnique migrationTraceRegTechnique = context.getBean(MigrationTraceRegTechnique.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceRegTechnique.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceRegTechnique.migrationFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceRegTechnique.traceComparator();
        }

        // trace reg Technique index
        if (Constantes.CF_TRACE_REG_TECHNIQUE_INDEX.equals(cfName) || all) {
          final MigrationTraceRegTechnique migrationTraceRegTechnique = context.getBean(MigrationTraceRegTechnique.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceRegTechnique.migrationIndexFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceRegTechnique.migrationIndexFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceRegTechnique.indexComparator();
        }

        // trace reg Securité
        if (Constantes.CF_TRACE_REG_SECURITE.equals(cfName) || all) {
          final MigrationTraceRegSecurite migrationTraceRegSecurite = context.getBean(MigrationTraceRegSecurite.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceRegSecurite.migrationFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceRegSecurite.migrationFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceRegSecurite.traceComparator();
        }

        // trace reg Securité index
        if (Constantes.CF_TRACE_REG_SECURITE_INDEX.equals(cfName) || all) {
          final MigrationTraceRegSecurite migrationTraceRegSecurite = context.getBean(MigrationTraceRegSecurite.class);

          if (THRIFT_TO_CQL.equals(migrateTo) && !modeApiCqlSupport.isModeApiDualCqlOrModeApiCql(cfName)) {
            migrationTraceRegSecurite.migrationIndexFromThriftToCql();
          } else if (CQL_TO_THRIFT.equals(migrateTo)) {
            migrationTraceRegSecurite.migrationIndexFromCqlToThrift();
          }
          // Comparaison des données THRIFT et CQL
          migrationTraceRegSecurite.indexComparator();
        }
        // ##########################################################################
        // ###################### Initialisation des flags modeAPI en mode DUAL CQL #
        // ##########################################################################
         */
      } else {

        LOG.info(" _________________________________________________________");
        LOG.info("|                                                         |");
        LOG.info("|  ERREUR: PROBLEME DE FICHIERS DE CONGIGURATION          |");
        LOG.info("|_________________________________________________________|");
        LOG.info("|  Il faut préciser, dans la ligne de commande, le chemin |");
        LOG.info("|  complet du fichier de configuration du SAE.            |");
        LOG.info("|_________________________________________________________|");

      }

      finProgramme();
    }
    catch (final Exception e) {
      LOG.info("Exception:" + e.getCause());
      System.exit(1);
    }
  }

  /**
   * 
   */
  private static void finProgramme() {
    LOG.info(" ___________________________");
    LOG.info("|                           |");
    LOG.info("|    FIN PROGRAMME - MAIN   |");
    LOG.info("|___________________________|");
    System.exit(0);
  }

  /**
   * Set le modeAPI de la CF spécifiée
   * 
   * @param modeApiCqlSupport
   * @param cfName
   */
  private static void setModeApiCF(final ModeApiCqlSupport modeApiCqlSupport, final String cfName, final String modeAPI) {
    LOG.info(" _____________________________________________");
    LOG.info("|                                             |");
    LOG.info("|          MODE {}       |", modeAPI);
    LOG.info("|_____________________________________________|");

    modeApiCqlSupport.updateModeApi(modeAPI, cfName);
  }

  /**
   * On logge la table migrée
   * 
   * @param cfName
   */
  private static void logTableMigree(final String cfName) {
    final String fin = "                                                 |";
    LOG.info(" _____________________________________________________________________________");
    LOG.info("|                                                                             |");
    LOG.info("|          CF DEJA MIGREE: {}{}", cfName, fin.substring(cfName.length() - 2, fin.length()));
    LOG.info("|_____________________________________________________________________________|");

  }
  /**
   * 
   */
  private static void temporisation() {
    LOG.info(" _____________________________________________");
    LOG.info("|                                             |");
    LOG.info("|  TEMPORISATION 1 min                        |");
    LOG.info("|_____________________________________________|");
    try {
      Thread.sleep(60000);
    }
    catch (final InterruptedException e) {
      System.out.println("InterruptedException : " + e.getMessage());
    }
  }

  /**
   * Démarage du contexte Spring
   *
   * @param cheminFicConfSae
   *          le chemin du fichier de configuration principal du sae
   *          (sae-config.properties)
   * @return le contexte Spring
   */
  protected static ApplicationContext startContextSpring(
                                                         final String cheminFicConfSae) {

    final String contextConfig = "/applicationContext-cassandra-poc.xml";

    return ContextFactory.createSAEApplicationContext(contextConfig,
                                                      cheminFicConfSae);

  }

  private static boolean migration(final ApplicationContext context, final String cfName, final ModeApiCqlSupport modeApiCqlSupport, final String migrateTo) {

    boolean result = false;
    // On passe en mode dual thrift
    setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_THRIFT);
    // temporisation
    temporisation();

    switch (cfName) {

    case Constantes.CF_DROIT_ACTION_UNITAIRE:
      final MigrationActionUnitaire migrationActionUnitaire = context.getBean(MigrationActionUnitaire.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        result = migrationActionUnitaire.migrationFromThriftToCql();

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationActionUnitaire.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_DROIT_CONTRAT_SERVICE:
      final MigrationContratService migrationContratService = context.getBean(MigrationContratService.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        result = migrationContratService.migrationFromThriftToCql();

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationContratService.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL:
      final MigrationFormatControlProfil migrationFormatControlProfil = context.getBean(MigrationFormatControlProfil.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        result = migrationFormatControlProfil.migrationFromThriftToCql();

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationFormatControlProfil.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_DROIT_PAGM:
      final MigrationPagm migrationPagm = context.getBean(MigrationPagm.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        // result = migrationPagm.migrationFromThriftToCql(); TODO

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationPagm.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_DROIT_PAGMA:
      final MigrationPagma migrationPagma = context.getBean(MigrationPagma.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        result = migrationPagma.migrationFromThriftToCql();

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationPagma.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_DROIT_PAGMF:
      final MigrationPagmf migrationPagmf = context.getBean(MigrationPagmf.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        result = migrationPagmf.migrationFromThriftToCql();

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationPagmf.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_DROIT_PAGMP:
      final MigrationPagmp migrationPagmp = context.getBean(MigrationPagmp.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        result = migrationPagmp.migrationFromThriftToCql();

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationPagmp.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_DROIT_PRMD:
      final MigrationPrmd migrationPrmd = context.getBean(MigrationPrmd.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        result = migrationPrmd.migrationFromThriftToCql();

      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationPrmd.migrationFromCqlTothrift();
      }
      break;

    case Constantes.CF_JOB_REQUEST:
      final MigrationJobRequest migrationJobRequest = context.getBean(MigrationJobRequest.class);
      if (THRIFT_TO_CQL.equals(migrateTo)) {
        migrationJobRequest.migrationFromThriftToCql();
        result = migrationJobRequest.compareJobRequestCql();
      } else if (CQL_TO_THRIFT.equals(migrateTo)) {
        migrationJobRequest.migrationFromCqlTothrift();
      }
      break;

    default:
      break;
    }
    if (result) {
      // On passe en mode dual cql si comparaison ok
      setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_CQL);
      // temporisation
      temporisation();
      // On passe en mode cql
      setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DATASTAX);
    }
    return result;
  }

}
