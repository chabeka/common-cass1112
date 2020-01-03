package fr.urssaf.image.sae;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import fr.urssaf.image.commons.cassandra.helper.ModeGestionAPI.MODE_API;
import fr.urssaf.image.commons.cassandra.spring.batch.utils.Constante;
import fr.urssaf.image.sae.commons.MigrationParameters;
import fr.urssaf.image.sae.commons.context.ContextFactory;
import fr.urssaf.image.sae.droit.MigrationActionUnitaire;
import fr.urssaf.image.sae.droit.MigrationContratService;
import fr.urssaf.image.sae.droit.MigrationFormatControlProfil;
import fr.urssaf.image.sae.droit.MigrationPagm;
import fr.urssaf.image.sae.droit.MigrationPagma;
import fr.urssaf.image.sae.droit.MigrationPagmf;
import fr.urssaf.image.sae.droit.MigrationPagmp;
import fr.urssaf.image.sae.droit.MigrationPrmd;
import fr.urssaf.image.sae.droit.dao.support.ActionUnitaireSupport;
import fr.urssaf.image.sae.droit.dao.support.cql.ActionUnitaireCqlSupport;
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

    /*
     * final String cheminFicConfSae0 = args[0];
     * final ApplicationContext context0 = startContextSpring(cheminFicConfSae0);
     * final MigrationJobExecutionsRunning jobexe0 = context0.getBean(MigrationJobExecutionsRunning.class);
     * jobexe0.migrationFromCqlTothrift();
     */

    if (args.length == 3) {
      // Extrait les infos de la ligne de commandes
      final String cheminFicConfSae = args[0];

      // Démarrage du contexte spring
      final ApplicationContext context = startContextSpring(cheminFicConfSae);

      // final MigrationJobExecutionsRunning jobexe = context.getBean("MigrationJobExecutionsRunning");

      // final MigrationJobExecutionsRunning jobexe = context.getBean(MigrationJobExecutionsRunning.class);
      // jobexe.migrationFromCqlTothrift();
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
        LOG.info("|    - THRIFT_TO_CQL                                             |");
        LOG.info("|    - CQL_TO_THRIFT                                             |");
        LOG.info("|______________________________________________________________|");
      }
      final boolean all = "ALL".equals(cfName);

      // ############################################################################################
      // ################### Initialisation des flags modeAPI en mode DUAL THRIFT ###################
      // ############################################################################################

      LOG.info(" _____________________________________________");
      LOG.info("|                                             |");
      LOG.info("|  MODE DUAL THRIFT                           |");
      LOG.info("|_____________________________________________|");

      final ModeApiCqlSupport modeApiCqlSupport = context.getBean(ModeApiCqlSupport.class);

      modeApiCqlSupport.initTables(MODE_API.DUAL_MODE_READ_THRIFT);

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



      LOG.info(" _____________________________________________");
      LOG.info("|                                             |");
      LOG.info("|  DEBUT MIGRATION                            |");
      LOG.info("|_____________________________________________|");

      // ##########################################################################
      // ################################ Les droits ##############################
      // ##########################################################################

      if ("DroitActionUnitaire".equals(cfName) || all) {
        final MigrationActionUnitaire migrationActionUnitaire = context.getBean(MigrationActionUnitaire.class);
        final ActionUnitaireSupport supportThrift = context.getBean(ActionUnitaireSupport.class);
        final ActionUnitaireCqlSupport supportCql = context.getBean(ActionUnitaireCqlSupport.class);
        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationActionUnitaire.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationActionUnitaire.migrationFromCqlTothrift();
        }
      } else if ("ContratService".equals(cfName) || all) {
        final MigrationContratService migrationContratService = context.getBean(MigrationContratService.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationContratService.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationContratService.migrationFromCqlTothrift();
        }
      } else if ("FormatControlProfil".equals(cfName) || all) {

        final MigrationFormatControlProfil migrationFormatControlProfil = context.getBean(MigrationFormatControlProfil.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationFormatControlProfil.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationFormatControlProfil.migrationFromCqlTothrift();
        }
      } else if ("DroitPagm".equals(cfName) || all) {

        final MigrationPagm migrationPagm = context.getBean(MigrationPagm.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationPagm.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationPagm.migrationFromCqlTothrift();
        }
      } else if ("DroitPagma".equals(cfName) || all) {

        final MigrationPagma migrationPagma = context.getBean(MigrationPagma.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationPagma.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationPagma.migrationFromCqlTothrift();
        }
      } else if ("DroitPagmf".equals(cfName) || all) {

        final MigrationPagmf migrationPagmf = context.getBean(MigrationPagmf.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationPagmf.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationPagmf.migrationFromCqlTothrift();
        }
      } else if ("DroitPagmp".equals(cfName) || all) {

        final MigrationPagmp migrationPagmp = context.getBean(MigrationPagmp.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationPagmp.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationPagmp.migrationFromCqlTothrift();
        }
      } else if ("DroitPrmd".equals(cfName) || all) {

        final MigrationPrmd migrationPrmd = context.getBean(MigrationPrmd.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
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

        if (THRIFT_TO_CQL.equals(migrateTo)) {
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

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationMetadata.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationMetadata.migrationFromCqlTothrift();
        }
      } else if ("Dictionary".equals(cfName) || all) {

        final MigrationDictionary migrationDictionary = context.getBean(MigrationDictionary.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
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

        if (THRIFT_TO_CQL.equals(migrateTo)) {
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

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationRnd.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationRnd.migrationFromCqlTothrift();
        }
      } else if ("CorrespondancesRnd".equals(cfName) || all) {

        final MigrationCorrespondancesRnd migrationCorrespondancesRnd = context.getBean(MigrationCorrespondancesRnd.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
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

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migrationSequences.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migrationSequences.migrationFromCqlTothrift();
        }
      }

      // ##########################################################################
      // ###################### Les piles de travaux ##############################
      // ##########################################################################

      if ("JobHistory".equals(cfName) || all) {
        final MigrationJobHistory jobH = context.getBean(MigrationJobHistory.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobH.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobH.migrationFromCqlTothrift();
        }
      } else if ("JobRequest".equals(cfName) || all) {
        final MigrationJobRequest jobRequest = context.getBean(MigrationJobRequest.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobRequest.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobRequest.migrationFromCqlTothrift();
        }
      } else if ("JobQueue".equals(cfName) || all) {

        final MigrationJobQueue jobexeQueue = context.getBean(MigrationJobQueue.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobexeQueue.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobexeQueue.migrationFromCqlTothrift();
        }
      }
      // ##########################################################################
      // ###################### Les job spring batch ##############################
      // ##########################################################################

      if (Constante.JOBINSTANCE_CFNAME.equals(cfName) || all) {
        final MigrationJobInstance migJobInst = context.getBean(MigrationJobInstance.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          migJobInst.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          migJobInst.migrationFromCqlTothrift();
        }
      }

      if (Constante.JOBINSTANCES_BY_NAME_CFNAME.equals(cfName) || all) {
        final MigrationJobInstancesByName jobInstByName = context.getBean(MigrationJobInstancesByName.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobInstByName.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobInstByName.migrationFromCqlTothrift();
        }
      }

      if (Constante.JOBEXECUTION_CFNAME.equals(cfName) || all) {
        final MigrationJobExecution jobex = context.getBean(MigrationJobExecution.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobex.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobex.migrationFromCqlTothrift();
        }
      }

      if (Constante.JOBEXECUTIONS_CFNAME.equals(cfName) || all) {
        final MigrationJobExecutions jobExes = context.getBean(MigrationJobExecutions.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobExes.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobExes.migrationFromCqlTothrift();
        }
      }

      if (Constante.JOBSTEP_CFNAME.equals(cfName) || all) {
        final MigrationJobStep jobStep = context.getBean(MigrationJobStep.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobStep.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobStep.migrationFromCqlTothrift();
        }
      }
      if (Constante.JOBSTEPS_CFNAME.equals(cfName) || all) {
        final MigrationJobSteps jobStep = context.getBean(MigrationJobSteps.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobStep.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobStep.migrationFromCqlTothrift();
        }
      }
      if (Constante.JOBEXECUTION_TO_JOBSTEP_CFNAME.equals(cfName) || all) {
        final MigrationJobExecutionToJobStep jobexeToStep = context.getBean(MigrationJobExecutionToJobStep.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobexeToStep.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobexeToStep.migrationFromCqlTothrift();
        }
      }
      if (Constante.JOBINSTANCE_TO_JOBEXECUTION_CFNAME.equals(cfName) || all) {
        final MigrationJobinstanceToJobExecution jobexeToStep = context.getBean(MigrationJobinstanceToJobExecution.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobexeToStep.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobexeToStep.migrationFromCqlTothrift();
        }
      }
      if (Constante.JOBEXECUTIONS_RUNNING_CFNAME.equals(cfName) || all) {
        final MigrationJobExecutionsRunning jobexeRunn = context.getBean(MigrationJobExecutionsRunning.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          jobexeRunn.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          jobexeRunn.migrationFromCqlTothrift();
        }
      }


      // ##########################################################################
      // ################################ Les Traces ##############################
      // ##########################################################################

      // Trace destinataire

      if ("TraceDestinataire".equals(cfName) || all) {
        final MigrationTraceDestinataire mtrdesti = context.getBean(MigrationTraceDestinataire.class);

        if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtrdesti.migrationFromThriftToCql();
        } else if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtrdesti.migrationFromCqlTothrift();
        }

      }

      // Trace reg exploitation

      if ("TraceRegExploitation".equals(cfName) || all) {
        final MigrationTraceRegExploitation mtrex = context.getBean(MigrationTraceRegExploitation.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtrex.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtrex.migrationFromCqlToThrift();
        }
      }

      // Trace reg exploitation index

      if ("TraceRegExploitationIndex".equals(cfName) || all) {
        final MigrationTraceRegExploitation mtrex = context.getBean(MigrationTraceRegExploitation.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtrex.migrationIndexFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtrex.migrationIndexFromCqlToThrift();
        }

      }

      // trace reg journal
      if ("TraceJournalEvt".equals(cfName) || all) {
        final MigrationTraceJournalEvt mtjournal = context.getBean(MigrationTraceJournalEvt.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtjournal.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtjournal.migrationFromCqlToThrift();
        }

      }

      // trace reg journal index
      if ("TraceJournalEvtIndex".equals(cfName) || all) {
        final MigrationTraceJournalEvt mtjournal = context.getBean(MigrationTraceJournalEvt.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtjournal.migIndexFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtjournal.migrationIndexFromCqlToThrift();
        }

      }

      // trace reg journal index doc
      if ("TraceJournalEvtIndexDoc".equals(cfName) || all) {
        final MigrationTraceJournalEvt mtjournal = context.getBean(MigrationTraceJournalEvt.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {

          mtjournal.migrationIndexDocFromThriftToCql();

        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtjournal.migrationIndexDocFromCqlToThrift();
        }

      }

      // trace reg Technique
      if ("TraceRegTechnique".equals(cfName) || all) {
        final MigrationTraceRegTechnique mtrtech = context.getBean(MigrationTraceRegTechnique.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtrtech.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtrtech.migrationFromCqlToThrift();
        }
      }

      // trace reg Technique index
      if ("TraceRegTechnique".equals(cfName) || all) {
        final MigrationTraceRegTechnique mtrtech = context.getBean(MigrationTraceRegTechnique.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtrtech.migrationIndexFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtrtech.migrationIndexFromCqlToThrift();
        }

      }

      // trace reg Securité
      if ("TraceRegSecurite".equals(cfName) || all) {
        final MigrationTraceRegSecurite mtrsecu = context.getBean(MigrationTraceRegSecurite.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtrsecu.migrationFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtrsecu.migrationFromCqlToThrift();
        }
      }

      // trace reg Securité index
      if ("TraceRegSecurite".equals(cfName) || all) {
        final MigrationTraceRegSecurite mtrsecu = context.getBean(MigrationTraceRegSecurite.class);

        if (THRIFT_TO_CQL.equals(migrateTo)) {
          mtrsecu.migrationIndexFromThriftToCql();
        } else if (CQL_TO_THRIFT.equals(migrateTo)) {
          mtrsecu.migrationIndexFromCqlToThrift();
        }

      }
      // ##########################################################################
      // ###################### Initialisation des flags modeAPI en mode DUAL CQL #
      // ##########################################################################

      LOG.info(" _____________________________________________");
      LOG.info("|                                             |");
      LOG.info("|              MODE DUAL CQL                  |");
      LOG.info("|_____________________________________________|");

      modeApiCqlSupport.initTables(MODE_API.DUAL_MODE_READ_CQL);

      LOG.info(" _____________________________________________");
      LOG.info("|                                             |");
      LOG.info("|           TEMPORISATION 1 min               |");
      LOG.info("|_____________________________________________|");

      try {
        Thread.sleep(60000);
      }
      catch (final InterruptedException e) {
        System.out.println("InterruptedException : " + e.getMessage());
      }

      LOG.info(" _____________________________________________");
      LOG.info("|                                             |");
      LOG.info("|                MODE CQL                     |");
      LOG.info("|_____________________________________________|");

      modeApiCqlSupport.initTables(MODE_API.DATASTAX);

    } else {

      LOG.info(" _________________________________________________________");
      LOG.info("|                                                         |");
      LOG.info("|  ERREUR: PROBLEME DE FICHIERS DE CONGIGURATION          |");
      LOG.info("|_________________________________________________________|");
      LOG.info("|  Il faut préciser, dans la ligne de commande, le chemin |");
      LOG.info("|  complet du fichier de configuration du SAE.            |");
      LOG.info("|_________________________________________________________|");

    }

    LOG.info(" ___________________________");
    LOG.info("|                           |");
    LOG.info("|    FIN PROGRAMME - MAIN   |");
    LOG.info("|___________________________|");
  }

  /**
   * Démarage du contexte Spring
   *
   * @param cheminFicConfSae
   *           le chemin du fichier de configuration principal du sae
   *           (sae-config.properties)
   * @return le contexte Spring
   */
  protected static ApplicationContext startContextSpring(
                                                         final String cheminFicConfSae) {

    final String contextConfig = "/applicationContext-cassandra-poc.xml";

    return ContextFactory.createSAEApplicationContext(contextConfig,
                                                      cheminFicConfSae);

  }

}
