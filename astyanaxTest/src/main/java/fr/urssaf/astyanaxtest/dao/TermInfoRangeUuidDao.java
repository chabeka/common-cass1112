package fr.urssaf.astyanaxtest.dao;

import java.util.UUID;

import com.netflix.astyanax.Keyspace;

public class TermInfoRangeUuidDao extends TermInfoRangeDao {

	public TermInfoRangeUuidDao(Keyspace keyspace, String meta,
			UUID baseUUID, IndexReference indexReference) {
		super(keyspace, meta, baseUUID, indexReference);
		cf = TermInfoRangeCF.uuidCf;
	}

}
