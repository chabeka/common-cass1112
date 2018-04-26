package fr.urssaf.image.sae.cassandra.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.prettyprint.hector.api.Keyspace;

import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;
import fr.urssaf.image.sae.cassandra.dao.model.JobModel;

public interface JobDAO {

   void saveOrUpdate(JobModel paramJobModel) throws CassandraEx;

   void delete(UUID primaryKey) throws CassandraEx;

   JobModel load(UUID primaryKey) throws CassandraEx;

   List<JobModel> loadJobs(Date startDate) throws CassandraEx;

   void setClefColumnFamilyName(String clefClumnFamilyName);

   void setColumnFamilyName(String columnFamilyName);

   void setKeyspace(Keyspace keyspace);

}
