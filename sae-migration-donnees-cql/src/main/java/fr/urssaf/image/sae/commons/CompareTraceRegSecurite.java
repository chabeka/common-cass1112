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
import fr.urssaf.image.sae.trace.dao.TraceRegSecuriteDao;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecurite;
import fr.urssaf.image.sae.trace.dao.model.TraceRegSecuriteIndex;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteCql;
import fr.urssaf.image.sae.trace.dao.modelcql.TraceRegSecuriteIndexCql;
import fr.urssaf.image.sae.trace.dao.serializer.MapSerializer;
import fr.urssaf.image.sae.trace.dao.serializer.TraceRegSecuriteIndexSerializer;
import fr.urssaf.image.sae.trace.daocql.ITraceRegExploitationCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegExploitationIndexCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteCqlDao;
import fr.urssaf.image.sae.trace.daocql.ITraceRegSecuriteIndexCqlDao;
import fr.urssaf.image.sae.trace.utils.UtilsTraceMapper;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;

@Repository
public class CompareTraceRegSecurite implements ICompareTrace<TraceRegSecurite, TraceRegSecuriteCql, TraceRegSecuriteIndex, TraceRegSecuriteIndexCql> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CompareTraceRegSecurite.class);
	@Autowired
	TraceRegSecuriteDao dao;
	
	@Autowired
	ITraceRegSecuriteCqlDao tracejdao;
	
	@Autowired
	ITraceRegSecuriteIndexCqlDao indexDao;
	
	@Override
	public TraceRegSecurite createNewInstance(UUID idTrace, Date timestamp) {
		return new TraceRegSecurite(idTrace, timestamp);
	}

	@Override
	public void completeTraceFromResult(TraceRegSecurite trace, Row<UUID, String, byte[]> row) {
		  
		  String contexte = "";
		  //COL_CONTEXTE("contexte"),
	      HColumn<String, byte[]> coHl = row.getColumnSlice().getColumnByName(TraceFieldsName.COL_CONTEXTE.getName());
	      if(coHl != null) {
	    	  contexte = StringSerializer.get().fromBytes(coHl.getValue());
	    	  trace.setContexte(contexte);
	      }

	      Map<String, Object> infos = new HashMap<String, Object>();
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

	@Override
	public TraceRegSecuriteCql createTraceFromObjectThrift(TraceRegSecurite trThrift) {
		return UtilsTraceMapper.createTraceRegSecuFromThriftToCql(trThrift);
	}

	@Override
	public TraceRegSecuriteIndexCql createIndexFromObjectThrift(TraceRegSecuriteIndex index, String key) {
		return UtilsTraceMapper.createTraceRegSecuIndexFromThriftToCql(index, key);
	}

	@Override
	public TraceRegSecuriteIndex createNewInstanceIndex() {
		return new TraceRegSecuriteIndex();
	}

	@Override
	public Keyspace getKeySpace() {
		return dao.getKeyspace();
	}

	@Override
	public JacksonSerializer<TraceRegSecuriteIndex> getIndexSerializer() {
		return TraceRegSecuriteIndexSerializer.get();
	}

	@Override
	public Logger getLogger() {
		return LOGGER;
	}

	@Override
	public IGenericDAO<TraceRegSecuriteCql, UUID> getTraceDaoType() {
		return tracejdao;
	}

	@Override
	public IGenericDAO<TraceRegSecuriteIndexCql, String> getIndexDaoType() {
		return indexDao;
	}

	@Override
	public String getTraceClasseName() {
		return TraceRegSecurite.class.getSimpleName();
	}

	@Override
	public String getIndexClasseName() {
		return TraceRegSecuriteIndex.class.getSimpleName();
	}

}
