/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import fr.urssaf.image.sae.pile.travaux.model.JobQueue;
import fr.urssaf.image.sae.pile.travaux.model.JobQueueCql;

/**
 * @author AC75007648
 *         Cette classe permet d'effectuer les mappings requis pour le fonctionnement
 *         des services
 */
public class JobsQueueMapper {

  /**
   * @param jobQueueCql
   * @return
   */
  public static JobQueue mapJobQueueCqlToJobQueue(final JobQueueCql jobQueueCql) {
    final JobQueue jobQueue = new JobQueue();
    jobQueue.setIdJob(jobQueueCql.getIdJob());
    jobQueue.setJobParameters(jobQueueCql.getJobParameters());
    jobQueue.setType(jobQueueCql.getType());
    return jobQueue;
  }

  /**
   * @param jobQueue
   * @return
   */
  public static JobQueueCql mapJobQueueToJobQueueCql(final JobQueue jobQueue) {
    final JobQueueCql jobQueueCql = new JobQueueCql();
    jobQueueCql.setIdJob(jobQueue.getIdJob());
    jobQueueCql.setJobParameters(jobQueue.getJobParameters());
    jobQueueCql.setType(jobQueue.getType());
    return jobQueueCql;
  }

  /**
   * @param jobsQueueCql
   * @return
   */
  public static List<JobQueue> mapListJobQueueToListJobQueueCql(final List<JobQueueCql> jobsQueueCql) {
    final List<JobQueue> jobsQueue = new ArrayList<>();

    for (final JobQueueCql jobQueueCql : jobsQueueCql) {
      final JobQueue jobQueue = JobsQueueMapper.mapJobQueueCqlToJobQueue(jobQueueCql);
      jobsQueue.add(jobQueue);
    }
    return jobsQueue;
  }

  /**
   * @param jobsQueueCql
   * @return
   */
  public static Iterator<JobQueue> mapIteratorJobQueueToIteratorJobQueueCql(final Iterator<JobQueueCql> jobsQueueCql) {
    final List<JobQueue> jobsQueueList = new ArrayList<>();
    while (jobsQueueCql.hasNext()) {
      jobsQueueList.add(JobsQueueMapper.mapJobQueueCqlToJobQueue(jobsQueueCql.next()));
    }
    final Iterator<JobQueue> itr = jobsQueueList.iterator();
    return itr;
  }

}
