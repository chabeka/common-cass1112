package fr.urssaf.image.parser_opencsv.application.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.parser_opencsv.application.model.entity.JobEntity;
import fr.urssaf.image.parser_opencsv.application.repository.JobRepository;
import fr.urssaf.image.parser_opencsv.application.service.IJobService;

/**
 * Implémentation du service
 */
@Service
public class JobServiceImpl implements IJobService {

   @Autowired
   private JobRepository jobRepository;

   /**
    * {@inheritDoc}
    */
   @Override
   public List<JobEntity> getAllJobs() {
      return jobRepository.findAll();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JobEntity getJobById(final Integer idJob) {
      return jobRepository.getOne(idJob);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JobEntity saveJob(final JobEntity job) {
      return jobRepository.save(job);
   }

}
