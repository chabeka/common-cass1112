package fr.urssaf.image.parser_opencsv.application.service;

import java.util.List;

import fr.urssaf.image.parser_opencsv.application.model.entity.JobEntity;

public interface IJobService {

   List<JobEntity> getAllJobs();

   JobEntity getJobById(Integer idJob);

   JobEntity saveJob(JobEntity job);
}
