package fr.urssaf.astyanaxtest.dao;

import java.util.UUID;

import com.netflix.astyanax.Keyspace;

public class TermInfoRangeStringDao extends TermInfoRangeDao {

	public TermInfoRangeStringDao(Keyspace keyspace, String meta,
			UUID baseUUID, IndexReference indexReference) {
		super(keyspace, meta, baseUUID, indexReference);
		cf = TermInfoRangeCF.stringCf;
	}

}
