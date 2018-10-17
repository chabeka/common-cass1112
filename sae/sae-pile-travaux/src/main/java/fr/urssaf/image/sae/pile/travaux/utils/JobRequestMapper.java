/**
 *
 */
package fr.urssaf.image.sae.pile.travaux.utils;

import fr.urssaf.image.sae.pile.travaux.model.JobRequest;
import fr.urssaf.image.sae.pile.travaux.model.JobState;
import fr.urssaf.image.sae.pile.travaux.modelcql.JobRequestCql;

/**
 *
 *
 */
public class JobRequestMapper {

   public static JobRequestCql mapJobRequestThriftToJobRequestCql(final JobRequest jobRequestThrift) {

      final JobRequestCql jobRequestcql = new JobRequestCql();

      jobRequestcql.setIdJob(jobRequestThrift.getIdJob());

      jobRequestcql.setType(jobRequestThrift.getType());

      jobRequestcql.setParameters(jobRequestThrift.getParameters());

      jobRequestcql.setState(jobRequestThrift.getState().name());

      jobRequestcql.setReservedBy(jobRequestThrift.getReservedBy());

      jobRequestcql.setCreationDate(jobRequestThrift.getCreationDate());

      jobRequestcql.setReservationDate(jobRequestThrift.getReservationDate());

      jobRequestcql.setStartingDate(jobRequestThrift.getStartingDate());

      jobRequestcql.setEndingDate(jobRequestThrift.getEndingDate());

      jobRequestcql.setMessage(jobRequestThrift.getMessage());

      jobRequestcql.setSaeHost(jobRequestThrift.getSaeHost());

      jobRequestcql.setClientHost(jobRequestThrift.getClientHost());

      jobRequestcql.setPid(jobRequestThrift.getPid());

      jobRequestcql.setDocCount(jobRequestThrift.getDocCount());

      jobRequestcql.setDocCountTraite(jobRequestThrift.getDocCountTraite());

      jobRequestcql.setToCheckFlag(jobRequestThrift.getToCheckFlag());

      jobRequestcql
                   .setToCheckFlagRaison(jobRequestThrift.getToCheckFlagRaison());

      jobRequestcql.setVi(jobRequestThrift.getVi());

      jobRequestcql.setJobParameters(jobRequestThrift.getJobParameters());

      jobRequestcql.setJobKey(jobRequestThrift.getJobKey());

      return jobRequestcql;

   }

   public static JobRequest mapJobRequestCqlToJobRequestThrift(final JobRequestCql jobRequestcql) {

      final JobRequest jobRequestThrift = new JobRequest();

      jobRequestThrift.setIdJob(jobRequestThrift.getIdJob());

      jobRequestThrift.setType(jobRequestThrift.getType());

      jobRequestThrift.setParameters(jobRequestThrift.getParameters());

      jobRequestThrift.setState(JobState.valueOf(jobRequestThrift.getState().name()));

      jobRequestThrift.setReservedBy(jobRequestThrift.getReservedBy());

      jobRequestThrift.setCreationDate(jobRequestThrift.getCreationDate());

      jobRequestThrift.setReservationDate(jobRequestThrift.getReservationDate());

      jobRequestThrift.setStartingDate(jobRequestThrift.getStartingDate());

      jobRequestThrift.setEndingDate(jobRequestThrift.getEndingDate());

      jobRequestThrift.setMessage(jobRequestThrift.getMessage());

      jobRequestThrift.setSaeHost(jobRequestThrift.getSaeHost());

      jobRequestThrift.setClientHost(jobRequestThrift.getClientHost());

      jobRequestThrift.setPid(jobRequestThrift.getPid());

      jobRequestThrift.setDocCount(jobRequestThrift.getDocCount());

      jobRequestThrift.setDocCountTraite(jobRequestThrift.getDocCountTraite());

      jobRequestThrift.setToCheckFlag(jobRequestThrift.getToCheckFlag());

      jobRequestThrift
                      .setToCheckFlagRaison(jobRequestThrift.getToCheckFlagRaison());

      jobRequestThrift.setVi(jobRequestThrift.getVi());

      jobRequestThrift.setJobParameters(jobRequestThrift.getJobParameters());

      jobRequestThrift.setJobKey(jobRequestThrift.getJobKey());

      return jobRequestThrift;

   }
}
