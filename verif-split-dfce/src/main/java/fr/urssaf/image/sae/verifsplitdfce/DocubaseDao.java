package fr.urssaf.image.sae.verifsplitdfce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.CountQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocubaseDao {
   
   private final static Logger LOGGER = LoggerFactory.getLogger(DocubaseDao.class); 
   
   private final String hosts;
   
   private final String username;
   
   private final String password;
   
   private final String baseName;
   
   public DocubaseDao(String hosts, String username, String password, String baseName) {
      super();
      this.hosts = hosts;
      this.username = username;
      this.password = password;
      this.baseName = baseName;
   }

   private Keyspace getKeyspaceDocubaseFromKeyspace() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", username);
      credentials.put("password", password);
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            hosts);
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      @SuppressWarnings("rawtypes")
      FailoverPolicy failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private UUID getBaseUUIDByName(String nomBase, Keyspace keyspaceDocubase) {
      UUID idBase = null;
      
      SliceQuery<String, String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("BasesReference");
      queryDocubase.setKey(nomBase);
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if ("uuid".equals(colonne.getName())) {
            idBase = UUIDSerializer.get().fromBytes(colonne.getValue());
         }
      }
      return idBase;
   }
   
   public boolean verifExistanceIndex(String indexName) {
      boolean exist = false;
      if (indexName.equals("SM_UUID") 
          || indexName.equals("SM_CREATION_DATE")
          || indexName.equals("SM_ARCHIVAGE_DATE")
          || indexName.equals("SM_LIFE_CYCLE_REFERENCE_DATE")
          || indexName.equals("SM_MODIFICATION_DATE")) {
         exist = true;
      } else {
         // on controle
            
         // creation de la rowKey
         StringBuffer buffer = new StringBuffer();
         buffer.append(baseName);
         buffer.append((char) 65535);
         buffer.append(indexName);
         
         Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
         SliceQuery<byte[], String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily("BaseCategoriesReference");
         queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
         queryDocubase.setRange(null, null, false, 1);
         
         QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase.execute();
         if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
            exist = true;
         }
      }
      return exist;
   }
   
   public String getTypeIndex(String indexName) {
      String type = "";
      if (indexName.equals("SM_UUID")) {
         type = "UUID";
      } else if (indexName.equals("SM_CREATION_DATE")
          || indexName.equals("SM_ARCHIVAGE_DATE")
          || indexName.equals("SM_LIFE_CYCLE_REFERENCE_DATE")
          || indexName.equals("SM_MODIFICATION_DATE")) {
         type = "DATETIME";
      } else {
         // on recupere l'index
         
         Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
         SliceQuery<String,String,String> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
         queryDocubase.setColumnFamily("CategoriesReference").setKey(indexName).setColumnNames("categoryTypeENUM_ATTRIBUTE_VALUE_SUFFIXE");
         QueryResult<ColumnSlice<String,String>> resultDocubase = queryDocubase.execute();
         if (resultDocubase.get() != null && !resultDocubase.get().getColumns().isEmpty()) {
            type = resultDocubase.get().getColumnByName("categoryTypeENUM_ATTRIBUTE_VALUE_SUFFIXE").getValue();
         }
      }
      return type;
   }

   public void checkIndexInIndexReference(String indexName) throws JSONException {
      
      boolean error = false;
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(baseName, keyspaceDocubase);
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(idBase.toString());
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      Long nbRange = null;
      List<Long> listNumRow = new ArrayList<Long>();
      Map<String, String[]> bornes = new TreeMap<String, String[]>();
      List<Long> rangesClosed = new ArrayList<Long>();
      String[] firstBorne = null;
      Long maxKey = Long.valueOf(-1);
      Long numRowMaxKey = Long.valueOf(-1);
      Map<Long, String> bornesMin = new TreeMap<Long, String>();
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            listNumRow.add(ConvertUtils.convertByteToLong(colonne.getValue()));
            
            Long key = Long.valueOf(colonne.getName().split("\\.")[1]);
            if (key.longValue() > maxKey.longValue()) {
               maxKey = key;
               numRowMaxKey = ConvertUtils.convertByteToLong(colonne.getValue());
            }
         } else if (colonne.getName().equals("rangeIndexes.size")) {
            nbRange = ConvertUtils.convertByteToLong(colonne.getValue());
         } else if (colonne.getName().matches("rangeIndexes.[0-9]*.value")) {
            JSONObject object = new JSONObject(StringSerializer.get().fromBytes(colonne.getValue()));
            Long id = object.getLong("ID");
            String borneMin = object.getString("LOWER_BOUND");
            String borneMax = object.getString("UPPER_BOUND");
            String etat = object.getString("STATE"); 
            if (!etat.equals("CLOSED")) {
               if (borneMin.equals("min_lower_bound")) {
                  firstBorne = new String[] {borneMin, borneMax};
               } else {
                  bornes.put(borneMin, new String[] {borneMin, borneMax} );
               }
               bornesMin.put(id, borneMin);
            } else {
               rangesClosed.add(id);
            }
         }
      }
      
      // suppression des ranges closed
      for (Long numRow : rangesClosed) {
         LOGGER.warn("Ne tient pas compte du range {} fermé", numRow);
         listNumRow.remove(numRow);
      }
      
      if (maxKey.longValue() > 0) {
         // on est sur d'avoir plusieurs ranges, donc d'avoir splitter
         // on ignore la numRow de la max key
         //LOGGER.warn("Ne tient pas compte du range {} de la toute dernière key", numRowMaxKey);
         listNumRow.remove(numRowMaxKey);
         if (!listNumRow.contains(numRowMaxKey)) {
            //LOGGER.warn("Le range {} n'est plus reference, donc on supprime les bornes", numRowMaxKey);
            if (bornesMin.get(numRowMaxKey) != null) {
               bornes.remove(bornesMin.get(numRowMaxKey));
            }
         }
      }
      
      // verifie les doublons
      Map<Long, Long> doublons = new TreeMap<Long, Long>();
      for (Long numRow : listNumRow) {
         if (doublons.containsKey(numRow)) {
            doublons.put(numRow, doublons.get(numRow) + 1);
         } else {
            doublons.put(numRow, Long.valueOf(1));
         }
      }
      for (Long numRow : doublons.keySet()) {
         int index = 0;
         if (doublons.get(numRow) == 2) {
            LOGGER.warn("Doublon du range {} pour l'index {}. Ce n'est pas un problème", numRow, indexName);
            index = 1;
         } else if (doublons.get(numRow) > 2) {
            LOGGER.error("Trop de ranges({}) pour la row {} pour l'index {}", new Object[] { doublons.get(numRow), numRow, indexName });
            index = doublons.get(numRow).intValue() - 1;
            error = true;
         }
         // elimination des doublons
         while (index >= 1) {
            listNumRow.remove(numRow);
            index--;
         }
      }
      
      // tri la liste des numero de row
      Collections.sort(listNumRow);
      
      // verifie le nombre de range
      if (nbRange < listNumRow.size()) {
         LOGGER.error("Le nombre de range n'est pas correct pour l'index {}: {} au lieu de {}", new Object[] { indexName, nbRange, listNumRow.size() });
         error = true;
      }
      
      String bornePrecedente = "";
      // verifie la premiere borne
      if (firstBorne == null) {
         LOGGER.error("Pas de borne min");
      } else {
         bornePrecedente = firstBorne[1];
      }
      // verifie les bornes suivantes
      for (String borneMin : bornes.keySet()) {
         if (!bornePrecedente.equals("")) {
            if (!bornes.get(borneMin)[0].equalsIgnoreCase(bornePrecedente)) {
               LOGGER.error("Valeur differente entre la borne precedente et le nouveau range : {} - {}", bornePrecedente, bornes.get(borneMin)[0]);
               error = true;
            }
         }
         bornePrecedente = bornes.get(borneMin)[1];
      }
      // verifie la derniere borne
      if (!bornePrecedente.equalsIgnoreCase("max_upper_bound")) {
         LOGGER.error("Pas de borne max : {}", bornePrecedente);
         error = true;
      }
      
      if (error) {
         LOGGER.error("Le découpage de cet index est en erreur. Veuillez contacter la MOE SAE.");
      } else {
         LOGGER.info("Le découpage de cet index s'est bien effectué");
      }
   }
   
   public void verifRangesInTermInfoRange(String indexName) {
      
      String cfName = "";
      
      String type = getTypeIndex(indexName);
      if (type.equals("UUID")) {
         cfName = "TermInfoRangeUUID";
      } else if (type.equals("DATETIME")) {
         cfName = "TermInfoRangeDatetime";
      } else if (type.equals("DATE")) {
         cfName = "TermInfoRangeDate";
      } else if (type.equals("DOUBLE")) {
         cfName = "TermInfoRangeDouble";
      } else if (type.equals("FLOAT")) {
         cfName = "TermInfoRangeFloat";
      } else if (type.equals("INTEGER")) {
         cfName = "TermInfoRangeInteger";
      } else if (type.equals("LONG")) {
         cfName = "TermInfoRangeLong";
      } else {
         cfName = "TermInfoRangeString";
      }
      
      // on cree la connexion cassandra
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(baseName, keyspaceDocubase);
      
      // On recupere le numero des rows indexees
      List<Long> numRows = getIndexReferenceByIndexName(indexName, idBase, keyspaceDocubase);
      
      LOGGER.info("Lancement du comptage de l'index {} pour la base {} ({})", new String[] { indexName, baseName, idBase.toString() });
      
      long total = 0;
      for (Long numRow : numRows) {
         LOGGER.debug("Traitement de la row : {}", numRow);
         
         // l'index a au moins une valeur
         // la cle de l'index est compose de :
         // - le nom de l'index
         // - le nom de la categorie
         // - l'uuid de la base
         // - le numero de row de la categorie
         Composite compositeKey = new Composite();
         compositeKey.add(0, "");
         compositeKey.add(1, indexName);
         compositeKey.add(2, idBase);
         compositeKey.add(3, ConvertUtils.toByteArray(numRow));
         
         // on va verifie l'indexation des documents
         CountQuery<byte[], Composite> queryDocubaseTerm = HFactory.createCountQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get());
         queryDocubaseTerm.setColumnFamily(cfName);
         queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
         
         queryDocubaseTerm.setRange(null, null, 1000000000);
         
         Integer nbCompteur = queryDocubaseTerm.execute().get();
         
         LOGGER.info("{} docs indexés sur la row {} de l'index {}", new String[] { Long.toString(nbCompteur), numRow.toString(), indexName });
         total += nbCompteur;
      }
      
      LOGGER.info("{} docs indexés au total sur l'index {}", new String[] { Long.toString(total), indexName });
   }
   
   private List<Long> getIndexReferenceByIndexName(String indexName, UUID baseId, Keyspace keyspaceDocubase) {
      
      StringBuffer buffer = new StringBuffer();
      buffer.append(indexName);
      buffer.append((char) 65535);
      buffer.append(baseId.toString());
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("IndexReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      
      List<Long> listNumRow = new ArrayList<Long>();
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            listNumRow.add(ConvertUtils.convertByteToLong(colonne.getValue()));
         } 
      }
      
      // tri la liste des numero de row
      Collections.sort(listNumRow);
      
      return listNumRow;
   }
}
