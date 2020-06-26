package fr.urssaf.astyanaxtest.dao.sae;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.ColumnList;

public class TraceJournalEvtDao {
   private final Keyspace keyspace;

   public TraceJournalEvtDao(final Keyspace keyspace) {
      this.keyspace = keyspace;
   }

   public boolean traceExists(final UUID traceId) throws Exception {
      final OperationResult<ColumnList<String>> cols = keyspace
            .prepareQuery(TraceJournalEvtCF.get())
            .getKey(traceId)
            .execute();
      final ColumnList<String> result = cols.getResult();
      final Collection<String> colNames = result.getColumnNames();
      return colNames.contains("codeEvt");
   }

   public void updateInfos(final UUID traceId, final String infos) throws Exception {
      if (!traceExists(traceId)) {
         throw new Exception("Le trace " + traceId + " n'est pas trouvé dans la cf TraceJournalEvtCF");
      }
      final MutationBatch batch = keyspace.prepareMutationBatch();
      batch.withRow(TraceJournalEvtCF.get(), traceId).putColumn("infos", infos);
      final OperationResult<Void> result = batch.execute();
      System.out.println("Batch exécuté en : " + result.getLatency(TimeUnit.MILLISECONDS) + " ms");
   }

}
