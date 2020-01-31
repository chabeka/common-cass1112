package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;

public class CategoriesReferenceDao {
	private Keyspace keyspace;
	public String meta;

	public CategoriesReferenceDao(Keyspace keyspace, String meta) {
		this.keyspace = keyspace;
		this.meta = meta;		
	}

	/**
	 * Renvoie le type de la catégorie
	 * @return "STRING", "DATE", "DOUBLE", "BOOLEAN", "UUID", "DATETIME"
	 * @throws Exception
	 */
	public String getCategoryType() throws Exception {
		 OperationResult<ColumnList<String>> cols = keyspace
				.prepareQuery(CategoriesReferenceCF.get()).getKey(meta)
				.execute();
		ColumnList<String> result = cols.getResult();
		Column<String> col = result.getColumnByName("categoryTypeENUM_ATTRIBUTE_VALUE_SUFFIXE");
		return col.getStringValue();
	}
}
