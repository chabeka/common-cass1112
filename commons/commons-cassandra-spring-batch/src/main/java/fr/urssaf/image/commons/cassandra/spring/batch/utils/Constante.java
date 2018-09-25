/**
 *  TODO (AC75095028) Description du fichier
 */
package fr.urssaf.image.commons.cassandra.spring.batch.utils;

/**
 * TODO (AC75095028) Description du type
 */
public class Constante {

  // Clés constantes
  public static final String ALL_JOBS_KEY = "_all";
  public static final String JOB_STEPS_KEY = "jobSteps";
  public static final String UNRESERVED_KEY = "_unreserved";
  
  // Nom des tables thrift
  // Le nom des tables cql = Nom des tables  thrift en minuscule
  public static final String JOBINSTANCE_CFNAME = "jobinstance";
  public static final String JOBINSTANCES_BY_NAME_CFNAME = "JobInstancesByName";
  public static final String JOBINSTANCE_TO_JOBEXECUTION_CFNAME = "JobInstanceToJobExecution";
  public static final String JOBEXECUTION_CFNAME = "JobExecution";
  public static final String JOBEXECUTIONS_CFNAME = "JobExecutions";
  public static final String JOBEXECUTIONS_RUNNING_CFNAME = "JobExecutionsRunning";
  public static final String JOBEXECUTION_TO_JOBSTEP_CFNAME = "JobExecutionToJobStep";
  public static final String JOBSTEP_CFNAME = "JobStep";
  public static final String JOBSTEPS_CFNAME = "JobSteps";
  
  // Nouvelles tables
  public static final String MODE_API_CFNAME = "modeapi";
  public static final String SEQUENCES_CFNAME = "sequences";
}
