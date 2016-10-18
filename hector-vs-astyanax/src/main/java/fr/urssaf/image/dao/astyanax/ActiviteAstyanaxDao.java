package fr.urssaf.image.dao.astyanax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;

import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.MutationBatch;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.DateSerializer;
import com.netflix.astyanax.serializers.LongSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.util.RangeBuilder;

import fr.urssaf.image.dao.ActiviteDao;
import fr.urssaf.image.model.Activite;

public class ActiviteAstyanaxDao implements ActiviteDao {

   private Keyspace keyspace;
   
   private final static ColumnFamily<Long, Date> CF_ACTIVITES = new ColumnFamily<Long, Date>("Activites", LongSerializer.get(), DateSerializer.get(), StringSerializer.get());
   
   public ActiviteAstyanaxDao(Keyspace keyspace) {
      super();
      this.keyspace = keyspace;
   }

   @SuppressWarnings("unchecked")
   public List<Activite> findLimitedByClient(Long id) {
      ColumnList<Date> columns;
      
      ObjectMapper mapper = new ObjectMapper();
      List<Activite> activites = new ArrayList<Activite>();
      
      try {
          RowQuery<Long, Date> query = keyspace
              .prepareQuery(CF_ACTIVITES)
              .getKey(id)
              .autoPaginate(true)
              .withColumnRange(new RangeBuilder().setLimit(1000).build());
         
          while (!(columns = query.execute().getResult()).isEmpty()) {
              for (Column<Date> colonne : columns) {
                
                 Activite activite = new Activite();
                 
                 activite.setDate(colonne.getName());
                 
                 HashMap<String, String> infos = null;
                 try {
                    infos = mapper.readValue(colonne.getStringValue(), HashMap.class);
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
      } catch (ConnectionException e) {
         e.printStackTrace();
      }
      
      return activites;
   }

   @SuppressWarnings("unchecked")
   public List<Activite> findByClient(Long id) {
      ColumnList<Date> columns;
      
      ObjectMapper mapper = new ObjectMapper();
      List<Activite> activites = new ArrayList<Activite>();
      
      try {
          RowQuery<Long, Date> query = keyspace
              .prepareQuery(CF_ACTIVITES)
              .getKey(id)
              .autoPaginate(true)
              .withColumnRange(new RangeBuilder().setLimit(1000).build());
         
          while (!(columns = query.execute().getResult()).isEmpty()) {
              for (Column<Date> colonne : columns) {
                
                 Activite activite = new Activite();
                 
                 activite.setDate(colonne.getName());
                 
                 HashMap<String, String> infos = null;
                 try {
                    infos = mapper.readValue(colonne.getStringValue(), HashMap.class);
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
      } catch (ConnectionException e) {
         e.printStackTrace();
      }
      
      return activites;
   }

   public void insert(Long id, Activite activite) {
      
      MutationBatch m = keyspace.prepareMutationBatch();
      
      JSONObject object = new JSONObject(activite.getInfos());

      m.withRow(CF_ACTIVITES, id)
          .putColumn(activite.getDate(), object.toString(), null);

      try {
          m.execute();
      } catch (ConnectionException e) {
         e.printStackTrace();
      }
   }
}
