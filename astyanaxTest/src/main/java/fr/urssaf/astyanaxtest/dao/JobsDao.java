package fr.urssaf.astyanaxtest.dao;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;

public class JobsDao {
	private Keyspace keyspace;

	public JobsDao(Keyspace keyspace) {
		this.keyspace = keyspace;
	}

	/**
	 * Renvoie le type de la catégorie
	 * @return "STRING", "DATE", "DOUBLE", "BOOLEAN", "UUID", "DATETIME"
	 * @throws Exception
	 */
	public boolean jobExists(String jobKey) throws Exception {
		 OperationResult<ColumnList<String>> cols = keyspace
				.prepareQuery(JobsCF.get()).getKey(jobKey)
				.execute();
		ColumnList<String> result = cols.getResult();
		Collection<String> colNames = result.getColumnNames();
		return colNames.contains("running");
	}
	
	public void setJobAsNonRunning(String jobKey) throws Exception {
		if (!jobExists(jobKey)) {
			throw new Exception("Le job " + jobKey + " n'est pas trouvé dans la cf Jobs");
		}
		MutationBatch batch = keyspace.prepareMutationBatch();
		batch.withRow(JobsCF.get(), jobKey).putColumn("running", false);
		OperationResult<Void> result = batch.execute();
		System.out.println("Batch exécuté en : " + result.getLatency(TimeUnit.MILLISECONDS) + " ms");
	}
	
}
