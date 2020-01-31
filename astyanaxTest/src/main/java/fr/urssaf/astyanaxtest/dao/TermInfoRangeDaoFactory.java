package fr.urssaf.astyanaxtest.dao;

import java.util.UUID;

import com.netflix.astyanax.Keyspace;


public class TermInfoRangeDaoFactory {

		
	public static TermInfoRangeDao get(Keyspace keyspace, String meta, UUID baseUUID, IndexReference indexReference) throws Exception {
		CategoriesReference cat = new CategoriesReference(keyspace);
		String metaType = cat.getCategoryType(meta);
		TermInfoRangeDao termInfoRangeDao;
		if (metaType.equals("DATETIME")) {
			termInfoRangeDao = new TermInfoRangeDatetimeDao(keyspace, meta, baseUUID, indexReference);
		}
		else if (metaType.equals("STRING")) {
			termInfoRangeDao = new TermInfoRangeStringDao(keyspace, meta, baseUUID, indexReference);
		}
		else if (metaType.equals("UUID")) {
			termInfoRangeDao = new TermInfoRangeUuidDao(keyspace, meta, baseUUID, indexReference);
		}
		else {
			throw new Exception("Type de la meta " + meta + " non pris en charge : " + metaType);
		}
		return termInfoRangeDao;
	}
	
	
}
