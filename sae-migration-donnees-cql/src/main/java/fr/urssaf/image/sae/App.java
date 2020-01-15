package fr.urssaf.image.sae;

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
import fr.urssaf.image.sae.jobspring.MigrationJobExecution;
import fr.urssaf.image.sae.jobspring.MigrationJobExecutionToJobStep;
import fr.urssaf.image.sae.jobspring.MigrationJobExecutions;
import fr.urssaf.image.sae.jobspring.MigrationJobExecutionsRunning;
import fr.urssaf.image.sae.jobspring.MigrationJobInstance;
import fr.urssaf.image.sae.jobspring.MigrationJobInstancesByName;
import fr.urssaf.image.sae.jobspring.MigrationJobStep;
import fr.urssaf.image.sae.jobspring.MigrationJobSteps;
import fr.urssaf.image.sae.jobspring.MigrationJobinstanceToJobExecution;
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
				// ################### Initialisation des flags modeAPI en mode DUAL THRIFT
				// ###################
				// ############################################################################################

				final ModeApiCqlSupport modeApiCqlSupport = context.getBean(ModeApiCqlSupport.class);

				LOG.info(" _____________________________________________");
				LOG.info("|                                             |");
				LOG.info("|  DEBUT MIGRATION                            |");
				LOG.info("|_____________________________________________|");

				final String[] tabCfName = { Constantes.CF_DROIT_ACTION_UNITAIRE, Constantes.CF_DROIT_CONTRAT_SERVICE,
						Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL, Constantes.CF_DROIT_PAGM, Constantes.CF_DROIT_PAGMA,
						Constantes.CF_DROIT_PAGMF, Constantes.CF_DROIT_PAGMP, Constantes.CF_DROIT_PRMD,
						Constantes.CF_PARAMETERS, Constantes.CF_METADATA, Constantes.CF_DICTIONARY,
						Constantes.CF_REFERENTIEL_FORMAT, Constantes.CF_RND, Constantes.CF_CORRESPONDANCES_RND,
						Constantes.CF_SEQUENCES, Constantes.CF_JOB_HISTORY, Constantes.CF_JOB_REQUEST,
						Constantes.CF_JOBS_QUEUE, Constantes.CF_JOBINSTANCE, Constantes.CF_JOBINSTANCES_BY_NAME,
						Constantes.CF_JOBEXECUTION, Constantes.CF_JOBEXECUTIONS, Constantes.CF_JOBSTEP,
						Constantes.CF_JOBSTEPS, Constantes.CF_JOBEXECUTION_TO_JOBSTEP,
						Constantes.CF_JOBEXECUTIONS_RUNNING, Constantes.CF_TRACE_DESTINATAIRE,
						Constantes.CF_TRACE_REG_EXPLOITATION, Constantes.CF_TRACE_REG_EXPLOITATION_INDEX,
						Constantes.CF_TRACE_DESTINATAIRE, Constantes.CF_TRACE_JOURNAL_EVT,
						Constantes.CF_TRACE_JOURNAL_EVT_INDEX, Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC,
						Constantes.CF_TRACE_REG_TECHNIQUE, Constantes.CF_TRACE_REG_TECHNIQUE_INDEX,
						Constantes.CF_TRACE_REG_SECURITE, Constantes.CF_TRACE_REG_SECURITE_INDEX };

				if (all) {
					// On migre toutes les tables
					for (final String cfNameTemp : tabCfName) {
						// On effectue la migration que si le mode n'est pas cql (dual ou non)
						if (modeApiCqlSupport.isModeThriftOrDualThrift(cfNameTemp)) {
							final DiffM diffM = migration(context, cfNameTemp, modeApiCqlSupport, migrateTo);
							if (diffM.getDiff() != null && !diffM.getDiff().hasChanges() || diffM.isResult()) {
								// On passe en mode dual cql
								setModeApiCF(modeApiCqlSupport, cfNameTemp, MODE_API.DUAL_MODE_READ_CQL);
								// temporisation
								temporisation();
								setTableCql(cfName, modeApiCqlSupport);
							} else {
								if (diffM.getDiff() != null) {
									LOG.warn(diffM.getDiff().toString());
								}
							}
						} else {
							logTableMigree(cfName);
						}
					}
				} else {
					// On migre la table spécifié par cfName que si le mode n'est pas cql (dual ou
					// non)
					if (!modeApiCqlSupport.isModeThriftOrDualThrift(cfName)) {
						migration(context, cfName, modeApiCqlSupport, migrateTo);
					} else {
						logTableMigree(cfName);
					}
				}
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
		} catch (final Exception e) {
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
	private static void setModeApiCF(final ModeApiCqlSupport modeApiCqlSupport, final String cfName,
			final String modeAPI) {
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

	private static void setTableCql(final String cfName, final ModeApiCqlSupport modeApiCqlSupport) {

		switch (cfName) {

		case Constantes.CF_TRACE_REG_EXPLOITATION:
			break;

		case Constantes.CF_TRACE_REG_EXPLOITATION_INDEX:
			if (modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_REG_EXPLOITATION)
					&& modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_REG_EXPLOITATION_INDEX)) {
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_REG_EXPLOITATION);
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_REG_EXPLOITATION_INDEX);
			}
			break;

		case Constantes.CF_TRACE_JOURNAL_EVT:
			break;

		case Constantes.CF_TRACE_JOURNAL_EVT_INDEX:
			break;

		case Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC:
			if (modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_JOURNAL_EVT)
					&& modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_JOURNAL_EVT_INDEX)
					&& modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC)) {
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_JOURNAL_EVT);
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_JOURNAL_EVT_INDEX);
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC);
			}
			break;

		case Constantes.CF_TRACE_REG_TECHNIQUE:
			break;

		case Constantes.CF_TRACE_REG_TECHNIQUE_INDEX:
			if (modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_REG_TECHNIQUE)
					&& modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_REG_TECHNIQUE_INDEX)) {
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_REG_TECHNIQUE);
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_REG_TECHNIQUE_INDEX);
			}
			break;

		case Constantes.CF_TRACE_REG_SECURITE:

		case Constantes.CF_TRACE_REG_SECURITE_INDEX:
			if (modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_REG_SECURITE)
					&& modeApiCqlSupport.isModeDualCql(Constantes.CF_TRACE_REG_SECURITE_INDEX)) {
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_REG_SECURITE);
				modeApiCqlSupport.updateModeApi(MODE_API.DATASTAX, Constantes.CF_TRACE_REG_SECURITE_INDEX);
			}
			break;

		default:

			break;
		}

	}

	private static DiffM migration(final ApplicationContext context, final String cfName,
			final ModeApiCqlSupport modeApiCqlSupport, final String migrateTo) {

		boolean result = false;
		final DiffM diffM = new DiffM();

		try {
			// On passe en mode dual thrift
			setModeApiCF(modeApiCqlSupport, cfName, MODE_API.DUAL_MODE_READ_THRIFT);
			// temporisation
			temporisation();

			switch (cfName) {

			// Droits

			case Constantes.CF_DROIT_ACTION_UNITAIRE:
				final MigrationActionUnitaire migrationActionUnitaire = context.getBean(MigrationActionUnitaire.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationActionUnitaire.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationActionUnitaire.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_DROIT_CONTRAT_SERVICE:
				final MigrationContratService migrationContratService = context.getBean(MigrationContratService.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationContratService.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationContratService.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_DROIT_FORMAT_CONTROL_PROFIL:
				final MigrationFormatControlProfil migrationFormatControlProfil = context
						.getBean(MigrationFormatControlProfil.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationFormatControlProfil.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationFormatControlProfil.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_DROIT_PAGM:
				final MigrationPagm migrationPagm = context.getBean(MigrationPagm.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationPagm.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationPagm.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_DROIT_PAGMA:
				final MigrationPagma migrationPagma = context.getBean(MigrationPagma.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationPagma.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationPagma.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_DROIT_PAGMF:
				final MigrationPagmf migrationPagmf = context.getBean(MigrationPagmf.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationPagmf.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationPagmf.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_DROIT_PAGMP:
				final MigrationPagmp migrationPagmp = context.getBean(MigrationPagmp.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationPagmp.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationPagmp.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_DROIT_PRMD:
				final MigrationPrmd migrationPrmd = context.getBean(MigrationPrmd.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationPrmd.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationPrmd.migrationFromCqlTothrift());
				}
				break;

			// Parameters
			case Constantes.CF_PARAMETERS:
				final MigrationParameters migrationParameters = context.getBean(MigrationParameters.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationParameters.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationParameters.migrationFromCqlTothrift());
				}
				break;

			// Rnd
			case Constantes.CF_RND:
				final MigrationRnd migrationRnd = context.getBean(MigrationRnd.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationRnd.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationRnd.migrationFromCqlTothrift());
				}
				break;

			case Constantes.CF_CORRESPONDANCES_RND:
				final MigrationCorrespondancesRnd migrationCorrespondancesRnd = context
						.getBean(MigrationCorrespondancesRnd.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					diffM.setDiff(migrationCorrespondancesRnd.migrationFromThriftToCql());

				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					diffM.setDiff(migrationCorrespondancesRnd.migrationFromCqlTothrift());
				}
				break;

			// Séquences

			case Constantes.CF_SEQUENCES:
				final MigrationSequences migrationSequences = context.getBean(MigrationSequences.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationSequences.migrationFromThriftToCql();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationSequences.migrationFromCqlTothrift();
				}
				diffM.setDiff(migrationSequences.compareSequences());
				break;

			// Piles de travaux

			case Constantes.CF_JOB_HISTORY:
				final MigrationJobHistory migrationJobHistory = context.getBean(MigrationJobHistory.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobHistory.migrationFromThriftToCql();
					result = migrationJobHistory.compareJobHistoryCql();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobHistory.migrationFromCqlTothrift();
					result = migrationJobHistory.compareJobHistoryCql();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOB_REQUEST:
				final MigrationJobRequest migrationJobRequest = context.getBean(MigrationJobRequest.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobRequest.migrationFromThriftToCql();
					result = migrationJobRequest.compareJobRequestCql();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobRequest.migrationFromCqlTothrift();
					result = migrationJobRequest.compareJobRequestCql();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBS_QUEUE:
				final MigrationJobQueue migrationJobQueue = context.getBean(MigrationJobQueue.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobQueue.migrationFromThriftToCql();
					result = migrationJobQueue.compareJobQueueCql();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobQueue.migrationFromCqlTothrift();
					result = migrationJobQueue.compareJobQueueCql();
				}
				diffM.setResult(result);
				break;

			// Jobs Spring Batch

			case Constantes.CF_JOBINSTANCE:
				final MigrationJobInstance migrationJobInstance = context.getBean(MigrationJobInstance.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobInstance.migrationFromThriftToCql();
					result = migrationJobInstance.compareJobInstance();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobInstance.migrationFromCqlTothrift();
					result = migrationJobInstance.compareJobInstance();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBINSTANCES_BY_NAME:
				final MigrationJobInstancesByName migrationJobInstancesByName = context
						.getBean(MigrationJobInstancesByName.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobInstancesByName.migrationFromThriftToCql();
					result = migrationJobInstancesByName.compareJobInstanceByName();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobInstancesByName.migrationFromCqlTothrift();
					result = migrationJobInstancesByName.compareJobInstanceByName();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBEXECUTION:
				final MigrationJobExecution migrationJobExecution = context.getBean(MigrationJobExecution.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobExecution.migrationFromThriftToCql();
					result = migrationJobExecution.compareJobExecution();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobExecution.migrationFromCqlTothrift();
					result = migrationJobExecution.compareJobExecution();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBEXECUTIONS:
				final MigrationJobExecutions migrationJobExecutions = context.getBean(MigrationJobExecutions.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobExecutions.migrationFromThriftToCql();
					result = migrationJobExecutions.compareJobExecutions();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobExecutions.migrationFromCqlTothrift();
					result = migrationJobExecutions.compareJobExecutions();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBSTEP:
				final MigrationJobStep migrationJobStep = context.getBean(MigrationJobStep.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobStep.migrationFromThriftToCql();
					result = migrationJobStep.compareJobStepCql();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobStep.migrationFromCqlTothrift();
					result = migrationJobStep.compareJobStepCql();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBSTEPS:
				final MigrationJobSteps migrationJobSteps = context.getBean(MigrationJobSteps.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobSteps.migrationFromThriftToCql();
					result = migrationJobSteps.compareJobStepsCql();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobSteps.migrationFromCqlTothrift();
					result = migrationJobSteps.compareJobStepsCql();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBEXECUTION_TO_JOBSTEP:
				final MigrationJobExecutionToJobStep migrationJobExecutionToJobStep = context
						.getBean(MigrationJobExecutionToJobStep.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobExecutionToJobStep.migrationFromThriftToCql();
					result = migrationJobExecutionToJobStep.compareJobExecutionsToStep();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobExecutionToJobStep.migrationFromCqlTothrift();
					result = migrationJobExecutionToJobStep.compareJobExecutionsToStep();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBINSTANCE_TO_JOBEXECUTION:
				final MigrationJobinstanceToJobExecution migrationJobinstanceToJobExecution = context
						.getBean(MigrationJobinstanceToJobExecution.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobinstanceToJobExecution.migrationFromThriftToCql();
					result = migrationJobinstanceToJobExecution.compareJobInstanceToExecution();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobinstanceToJobExecution.migrationFromCqlTothrift();
					result = migrationJobinstanceToJobExecution.compareJobInstanceToExecution();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_JOBEXECUTIONS_RUNNING:
				final MigrationJobExecutionsRunning migrationJobExecutionsRunning = context
						.getBean(MigrationJobExecutionsRunning.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationJobExecutionsRunning.migrationFromThriftToCql();
					result = migrationJobExecutionsRunning.compareJobExecutionsRunning();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationJobExecutionsRunning.migrationFromCqlTothrift();
					result = migrationJobExecutionsRunning.compareJobExecutionsRunning();
				}
				diffM.setResult(result);
				break;

			// Traces

			case Constantes.CF_TRACE_DESTINATAIRE:
				final MigrationTraceDestinataire migrationTraceDestinataire = context
						.getBean(MigrationTraceDestinataire.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceDestinataire.migrationFromThriftToCql();
					result = migrationTraceDestinataire.compareTraceDestinataireFromCQlandThrift();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceDestinataire.migrationFromCqlTothrift();
					result = migrationTraceDestinataire.compareTraceDestinataireFromCQlandThrift();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_REG_EXPLOITATION:
				final MigrationTraceRegExploitation migrationTraceRegExploitation = context
						.getBean(MigrationTraceRegExploitation.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceRegExploitation.migrationFromThriftToCql();
					result = migrationTraceRegExploitation.traceComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					// migrationTraceRegExploitation.migrationFromCqlTothrift(); TODO
					result = migrationTraceRegExploitation.traceComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_REG_EXPLOITATION_INDEX:
				final MigrationTraceRegExploitation migrationTraceRegExploitationIndex = context
						.getBean(MigrationTraceRegExploitation.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceRegExploitationIndex.migrationFromThriftToCql();
					result = migrationTraceRegExploitationIndex.indexComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					// migrationTraceRegExploitation.migrationFromCqlTothrift(); TODO
					result = migrationTraceRegExploitationIndex.indexComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_JOURNAL_EVT:
				final MigrationTraceJournalEvt migrationTraceJournalEvt = context
						.getBean(MigrationTraceJournalEvt.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceJournalEvt.migrationFromThriftToCql();
					result = migrationTraceJournalEvt.traceComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceJournalEvt.migrationFromCqlToThrift();
					result = migrationTraceJournalEvt.traceComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_JOURNAL_EVT_INDEX:
				final MigrationTraceJournalEvt migrationTraceJournalEvtIndex = context
						.getBean(MigrationTraceJournalEvt.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceJournalEvtIndex.migIndexFromThriftToCql();
					result = migrationTraceJournalEvtIndex.indexComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceJournalEvtIndex.migrationIndexFromCqlToThrift();
					result = migrationTraceJournalEvtIndex.indexComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_JOURNAL_EVT_INDEX_DOC:
				final MigrationTraceJournalEvt migrationTraceJournalEvtIndexDoc = context
						.getBean(MigrationTraceJournalEvt.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceJournalEvtIndexDoc.migrationIndexDocFromThriftToCql();
					result = migrationTraceJournalEvtIndexDoc.indexDocComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceJournalEvtIndexDoc.migrationIndexDocFromCqlToThrift();
					result = migrationTraceJournalEvtIndexDoc.indexDocComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_REG_TECHNIQUE:
				final MigrationTraceRegTechnique migrationTraceRegTechnique = context
						.getBean(MigrationTraceRegTechnique.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceRegTechnique.migrationFromThriftToCql();
					result = migrationTraceRegTechnique.traceComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceRegTechnique.migrationFromCqlToThrift();
					result = migrationTraceRegTechnique.traceComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_REG_TECHNIQUE_INDEX:
				final MigrationTraceRegTechnique migrationTraceRegTechniqueIndex = context
						.getBean(MigrationTraceRegTechnique.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceRegTechniqueIndex.migrationFromThriftToCql();
					result = migrationTraceRegTechniqueIndex.indexComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceRegTechniqueIndex.migrationFromCqlToThrift();
					result = migrationTraceRegTechniqueIndex.indexComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_REG_SECURITE:
				final MigrationTraceRegSecurite migrationTraceRegSecurite = context
						.getBean(MigrationTraceRegSecurite.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceRegSecurite.migrationFromThriftToCql();
					result = migrationTraceRegSecurite.traceComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceRegSecurite.migrationFromCqlToThrift();
					result = migrationTraceRegSecurite.traceComparator();
				}
				diffM.setResult(result);
				break;

			case Constantes.CF_TRACE_REG_SECURITE_INDEX:
				final MigrationTraceRegSecurite migrationTraceRegSecuriteIndex = context
						.getBean(MigrationTraceRegSecurite.class);
				if (THRIFT_TO_CQL.equals(migrateTo)) {
					migrationTraceRegSecuriteIndex.migrationFromThriftToCql();
					result = migrationTraceRegSecuriteIndex.indexComparator();
				} else if (CQL_TO_THRIFT.equals(migrateTo)) {
					migrationTraceRegSecuriteIndex.migrationFromCqlToThrift();
					result = migrationTraceRegSecuriteIndex.indexComparator();
				}
				diffM.setResult(result);
				break;
			default:
				break;
			}

		} catch (final Exception e) {
			LOG.error(e.getMessage());
		}
		return diffM;
	}

}
