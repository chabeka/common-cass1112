package fr.urssaf.image.sae.commons;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.TraceJournalEvtIndexSerializer;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceJournalEvtIndexCqlDao;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;
import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.cql.dao.IGenericIndexDAO;
import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.trace.commons.TraceFieldsName;
import fr.urssaf.image.sae.trace.dao.TraceJournalEvtIndexDao;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvt;
import fr.urssaf.image.sae.trace.dao.model.TraceJournalEvtIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceJournalEvtIndexCql;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;

@Repository
public class CompareTraceJournalEvt implements ICompareTrace<TraceJournalEvt, TraceJournalEvtCql, TraceJournalEvtIndex, TraceJournalEvtIndexCql> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompareTraceJournalEvt.class);
	@Autowired
	TraceJournalEvtIndexDao indexthrift;
	  
	@Autowired
	ITraceJournalEvtCqlDao tracejdao;
	
	@Autowired
	ITraceJournalEvtIndexCqlDao indexDao;
	  
	@Override
	public TraceJournalEvt createNewInstance(UUID idTrace, Date timestamp) {
		return new TraceJournalEvt(idTrace, timestamp);
	}

	@Override
	public void completeTraceFromResult(TraceJournalEvt trace, Row<UUID, String, byte[]> row) {
	  
	  String contexte = "";
	  Map<String, Object> infos = new HashMap<>();
	  
	   //COL_CONTEXTE("contexte"),
      HColumn<String, byte[]> coHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_CONTEXTE.getName());
      if(coHl != null) {
    	  contexte = StringSerializer.get().fromBytes(coHl.getValue());
    	  trace.setContexte(contexte);
      }   
      // COL_INFOS("infos"),
      HColumn<String, byte[]> iHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_INFOS.getName());
      if(iHl != null) {
	      Map<String, Object> map = MapSerializer.get().fromBytes(iHl.getValue());
	      for (final Map.Entry<String, Object> entry : map.entrySet()) {
	          final String infosKey = entry.getKey();
	          final String value = entry.getValue() != null ? entry.getValue().toString() : "";
	          infos.put(infosKey, value);
	      } 
	      trace.setInfos(infos);
      }
	}

	// TABLE INDEX
	
	@Override
	public TraceJournalEvtIndex createNewInstanceIndex() {
		return new TraceJournalEvtIndex();
	}

	@Override
	public TraceJournalEvtCql createTraceFromObjectThrift(TraceJournalEvt trThrift) {
		return UtilsTraceMapper.createTraceJournalEvtFromThriftToCql(trThrift);
	}

	@Override
	public Keyspace getKeySpace() {
		return indexthrift.getKeyspace();
	}

	@Override
	public TraceJournalEvtIndexCql createIndexFromObjectThrift(TraceJournalEvtIndex indexThrift, String key) {
		return UtilsTraceMapper.createJournalIndexFromThriftToCql(indexThrift, key);
	}

	@Override
	public JacksonSerializer<TraceJournalEvtIndex> getIndexSerializer() {
		return TraceJournalEvtIndexSerializer.get();
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public IGenericDAO<TraceJournalEvtCql, UUID> getTraceDaoType() {
		return tracejdao;
	}

	@Override
	public IGenericDAO<TraceJournalEvtIndexCql, String> getIndexDaoType() {
		return indexDao;
	}

	@Override
	public String getTraceClasseName() {
		return TraceJournalEvt.class.getSimpleName();
	}

	@Override
	public String getIndexClasseName() {
		return TraceJournalEvtIndex.class.getSimpleName();
	}	
}
