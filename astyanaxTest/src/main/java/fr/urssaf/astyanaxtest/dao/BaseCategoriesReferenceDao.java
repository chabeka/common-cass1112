package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.OperationResult;

import fr.urssaf.astyanaxtest.helper.ConvertHelper;

public class BaseCategoriesReferenceDao {
	private Keyspace keyspace;
	public String meta;
	private String baseName;

	public BaseCategoriesReferenceDao(Keyspace keyspace, String meta, String baseName) {
		this.keyspace = keyspace;
		this.meta = meta;		
		this.baseName = baseName;
	}

	private byte[] getKey() throws Exception {
		return ConvertHelper.getBytesFromReadableUTF8String(baseName + "\\xef\\xbf\\xbf" + meta);
	}
	
	/**
	 * Met à jour la colonne "computed"
	 * @param computed
	 * @return 
	 * @throws Exception
	 */
	public void writeComputed(boolean computed) throws Exception {
		MutationBatch batch = keyspace.prepareMutationBatch();
		byte[] key = getKey();
		byte[] value = new byte[] {(byte) (computed?1:0)};		
		batch.withRow(BaseCategoriesReferenceCF.get(), key).putColumn("computed", value);		
		OperationResult<Void> result = batch.execute();
	}
}
