package fr.urssaf.image.parser_opencsv.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.parser_opencsv.application.model.entity.JobEntity;

@Repository
public interface JobRepository extends JpaRepository<JobEntity, Integer> {

}
