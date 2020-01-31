package fr.urssaf.astyanaxtest.dao;

import com.netflix.astyanax.Keyspace;

public class CategoriesReference {
	private Keyspace keyspace;

	public CategoriesReference(Keyspace keyspace) {
		this.keyspace = keyspace;
	}

	/**
	 * Renvoie le type de la catégorie
	 * @return "STRING", "DATE", "DOUBLE", "BOOLEAN", "UUID", "DATETIME"
	 * @throws Exception
	 */
	public String getCategoryType(String meta) throws Exception {
		if (meta.startsWith("SM_")) {
			if (meta.contains("_DATE")) return "DATETIME";
			if (meta.equals("SM_UUID")) return "UUID";
			if (meta.equals("SM_IS_FROZEN")) return "BOOLEAN";
			return "STRING";
		}
		else {
			if (meta.contains("&")) return "STRING";
			CategoriesReferenceDao dao = new CategoriesReferenceDao(keyspace, meta);
			return dao.getCategoryType();
		}
	}
	
}
