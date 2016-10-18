package fr.urssaf.image.dao.hector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import fr.urssaf.image.dao.ActiviteDao;
import fr.urssaf.image.iterator.hector.AllColumnsIterator;
import fr.urssaf.image.model.Activite;

public class ActiviteHectorDao implements ActiviteDao {
	
	private Keyspace keyspace;
	
	private String CF_NAME = "Activites";
	
	public ActiviteHectorDao(Keyspace keyspace) {
		super();
		this.keyspace = keyspace;
	}

	@SuppressWarnings("unchecked")
	public List<Activite> findLimitedByClient(Long id) {
		SliceQuery<Long, Date, String> query = HFactory.createSliceQuery(keyspace, LongSerializer.get(), DateSerializer.get(), StringSerializer.get());
		query.setColumnFamily(CF_NAME);
		// ne ramene que les 1000 premieres colonnes
		query.setRange(null, null, false, 1000);
		query.setKey(id);
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<Activite> activites = new ArrayList<Activite>();
		QueryResult<ColumnSlice<Date, String>> result = query.execute();
		if (result != null && result.get() != null 
				&& result.get().getColumns() != null 
				&& !result.get().getColumns().isEmpty()) {
			
			for (HColumn<Date, String> colonne : result.get().getColumns()) {
			
				Activite activite = new Activite();
				
				activite.setDate(colonne.getName());
				
				HashMap<String, String> infos = null;
				try {
					infos = mapper.readValue(colonne.getValue(), HashMap.class);
				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				activite.setInfos(infos);
				
				activites.add(activite);
			}
		}
		return activites;
	}

	@SuppressWarnings("unchecked")
   public List<Activite> findByClient(Long id) {
      SliceQuery<Long, Date, String> query = HFactory.createSliceQuery(keyspace, LongSerializer.get(), DateSerializer.get(), StringSerializer.get());
      query.setColumnFamily(CF_NAME);
      // ne ramene que les 1000 premieres colonnes
      query.setRange(null, null, false, 1000);
      query.setKey(id);
      
      AllColumnsIterator<Date, String> iterator = new AllColumnsIterator<Date, String>(query);
      
      ObjectMapper mapper = new ObjectMapper();
      
      List<Activite> activites = new ArrayList<Activite>();
         
      while (iterator.hasNext()) {
      
         HColumn<Date, String> colonne = iterator.next();
         
         Activite activite = new Activite();
         
         activite.setDate(colonne.getName());
         
         HashMap<String, String> infos = null;
         try {
            infos = mapper.readValue(colonne.getValue(), HashMap.class);
         } catch (JsonParseException e) {
            e.printStackTrace();
         } catch (JsonMappingException e) {
            e.printStackTrace();
         } catch (IOException e) {
            e.printStackTrace();
         }
         
         activite.setInfos(infos);
         
         activites.add(activite);
      }
      
      return activites;
   }
	
	public void insert(Long id, Activite activite) {
      
	   // creation du mutator 
      Mutator<Long> mutator = HFactory.createMutator(keyspace, LongSerializer.get());
      
      JSONObject object = new JSONObject(activite.getInfos());
      
      HColumn<Date, String> colNom = HFactory.createColumn(activite.getDate(), object.toString(), DateSerializer.get(), StringSerializer.get());
      mutator.addInsertion(id, CF_NAME, colNom);
      
      // execute les ecritures
      mutator.execute();
   }
}
