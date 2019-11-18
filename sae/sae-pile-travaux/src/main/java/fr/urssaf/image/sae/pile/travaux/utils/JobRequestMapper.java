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

      jobRequestThrift.setIdJob(jobRequestcql.getIdJob());

      jobRequestThrift.setType(jobRequestcql.getType());

      jobRequestThrift.setParameters(jobRequestcql.getParameters());

      jobRequestThrift.setState(JobState.valueOf(jobRequestcql.getState()));

      jobRequestThrift.setReservedBy(jobRequestcql.getReservedBy());

      jobRequestThrift.setCreationDate(jobRequestcql.getCreationDate());

      jobRequestThrift.setReservationDate(jobRequestcql.getReservationDate());

      jobRequestThrift.setStartingDate(jobRequestcql.getStartingDate());

      jobRequestThrift.setEndingDate(jobRequestcql.getEndingDate());

      jobRequestThrift.setMessage(jobRequestcql.getMessage());

      jobRequestThrift.setSaeHost(jobRequestcql.getSaeHost());

      jobRequestThrift.setClientHost(jobRequestcql.getClientHost());

      jobRequestThrift.setPid(jobRequestcql.getPid());

      jobRequestThrift.setDocCount(jobRequestcql.getDocCount());

      jobRequestThrift.setDocCountTraite(jobRequestcql.getDocCountTraite());

      jobRequestThrift.setToCheckFlag(jobRequestcql.getToCheckFlag());

      jobRequestThrift
                      .setToCheckFlagRaison(jobRequestcql.getToCheckFlagRaison());

      jobRequestThrift.setVi(jobRequestcql.getVi());

      jobRequestThrift.setJobParameters(jobRequestcql.getJobParameters());

      jobRequestThrift.setJobKey(jobRequestcql.getJobKey());

      return jobRequestThrift;

   }
}
