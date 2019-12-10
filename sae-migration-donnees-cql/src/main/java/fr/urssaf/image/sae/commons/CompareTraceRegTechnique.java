package fr.urssaf.image.sae.commons;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.cassandra.cql.dao.IGenericDAO;
import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.trace.commons.TraceFieldsName;
import fr.urssaf.image.sae.trace.dao.TraceRegTechniqueIndexDao;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechnique;
import fr.urssaf.image.sae.trace.dao.model.TraceRegTechniqueIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegTechniqueIndexCql;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegTechniqueIndexSerializer;
import fr.urssaf.image.sae.trace.daocql.ITraceRegExploitationCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegExploitationIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegTechniqueIndexCqlDao;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;

@Repository
public class CompareTraceRegTechnique implements ICompareTrace<TraceRegTechnique, TraceRegTechniqueCql, TraceRegTechniqueIndex, TraceRegTechniqueIndexCql> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompareTraceRegTechnique.class);
	 @Autowired
	 TraceRegTechniqueIndexDao thriftdao;
	 
	 @Autowired
	 ITraceRegTechniqueCqlDao tracejdao;
		
	 @Autowired
	 ITraceRegTechniqueIndexCqlDao indexDao;
	  
	@Override
	public TraceRegTechnique createNewInstance(UUID idTrace, Date timestamp) {
		return new TraceRegTechnique(idTrace, timestamp);
	}

	@Override
	public void completeTraceFromResult(TraceRegTechnique trace, Row<UUID, String, byte[]> row) {
		  
	  String contexte = "";
	  Map<String, Object> infos = new HashMap<>();
	  String stacktrace = "";
	  
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
      // COL_STACKTRACE("stacktrace"),
      HColumn<String, byte[]> stHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_STACKTRACE.getName());
      if(stHl != null) {
	      stacktrace = StringSerializer.get().fromBytes(stHl.getValue());
	      trace.setStacktrace(stacktrace);
      }
		
	}

	@Override
	public TraceRegTechniqueIndex createNewInstanceIndex() {
		return new TraceRegTechniqueIndex();
	}

	@Override
	public TraceRegTechniqueCql createTraceFromObjectThrift(TraceRegTechnique trThrift) {
		return UtilsTraceMapper.createTraceRegTechniqueFromThriftToCql(trThrift);
	}

	@Override
	public Keyspace getKeySpace() {
		return thriftdao.getKeyspace();
	}

	@Override
	public TraceRegTechniqueIndexCql createIndexFromObjectThrift(TraceRegTechniqueIndex index, String key) {
		return UtilsTraceMapper.createTraceRegTechniqueIndexFromThriftToCql(index, key);
	}

	@Override
	public JacksonSerializer<TraceRegTechniqueIndex> getIndexSerializer() {
		return TraceRegTechniqueIndexSerializer.get();
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public IGenericDAO<TraceRegTechniqueCql, UUID> getTraceDaoType() {
		return tracejdao;
	}

	@Override
	public IGenericDAO<TraceRegTechniqueIndexCql, String> getIndexDaoType() {
		return indexDao;
	}

	@Override
	public String getTraceClasseName() {
		return TraceRegTechnique.class.getSimpleName();
	}

	@Override
	public String getIndexClasseName() {
		return TraceRegTechniqueIndex.class.getSimpleName();
	}

}
