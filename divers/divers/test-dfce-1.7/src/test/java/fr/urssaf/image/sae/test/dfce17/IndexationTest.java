package fr.urssaf.image.sae.test.dfce17;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BigIntegerSerializer;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DoubleSerializer;
import me.prettyprint.cassandra.serializers.FloatSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.serializers.UUIDSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.AbstractComposite.ComponentEquality;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import net.docubase.toolkit.model.recordmanager.DocEventLogType;
import net.docubase.toolkit.model.recordmanager.RMDocEvent;
import net.docubase.toolkit.service.ServiceProvider;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

@RunWith(BlockJUnit4ClassRunner.class)
public class IndexationTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(IndexationTest.class);
   
   private SimpleDateFormat FORMATTER = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss,SSS");
   
   private static String CHEMINREP = "c:/divers";
   private static String NOMFICHIER = "fonds_doc_prod_gnt.csv";
   private static String NOMFICHIER_TERM_INFO = "term_info_prod_gnt.csv";
   private static String NOMFICHIER_TERM_INFO_RANGE = "term_info_range_prod_gnt.csv";
   private static String NOMFICHIER_DOCS_ERREUR_TERM_INFO_RANGE = "docs_erreur_range_prod_gnt.csv";
   
   //private static String NOMFICHIER = "fonds_doc_preprod_gnt.csv";
   //private static String NOMFICHIER_TERM_INFO = "term_info_preprod_gnt.csv";
   //private static String NOMFICHIER_TERM_INFO_RANGE = "term_info_range_preprod_gnt.csv";
   //private static String NOMFICHIER_DOCS_ERREUR_TERM_INFO_RANGE = "docs_erreur_range_preprod_gnt.csv";
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   //private String nomBase = "GNT-INT";
   
   // Integration cliente GNS
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   //private String nomBase = "SAE-INT";
   //private String url = "http://hwi69intgnsapp1.gidn.recouv:8080/dfce-webapp/toolkit/";
   
   // Developpement 
   //private String hosts = "cer69imageint10.cer69.recouv";
   //private String nomBase = "SAE-INT";
   
   // Pré-prod nationale GNT
   //private String hosts = "cnp69pregntcas1.cer69.recouv:9160,cnp69pregntcas2.cer69.recouv:9160,cnp69pregntcas3.cer69.recouv:9160";
   //private String nomBase = "GNT-PROD";
   //private String url = "http://hwi69pregntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";
   
   // Prod nationale GNT
   private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   private String nomBase = "GNT-PROD";
   private String url = "http://hwi69gntappli1.cer69.recouv:8080/dfce-webapp/toolkit/";

   private Keyspace getKeyspaceDocubaseFromKeyspace() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            hosts);
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-" + new Date().getTime(),
            hostConfigurator);
      @SuppressWarnings("rawtypes")
      FailoverPolicy failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   private String getDate(long value) {
      Date date = new Date(value / 1000);
      return FORMATTER.format(date);
   }
   
   private UUID getBaseUUIDByName(String nomBase) {
      UUID idBase = null;
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
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
   
   private Map<String, String[]> getDocInfoById(Keyspace keyspaceDocubase, UUID idDocToFind) {
      Map<String, String[]> indexes = new HashMap<String, String[]>();

      // recupere la liste des indexes
      Composite keyToFind = new Composite();
      keyToFind.addComponent(0, UUIDSerializer.get().toByteBuffer(idDocToFind), ComponentEquality.EQUAL);
      keyToFind.addComponent(1, StringSerializer.get().toByteBuffer("0.0.0"), ComponentEquality.EQUAL);
      
      SliceQuery<byte[], String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("DocInfo");
      queryDocubase.setKey(CompositeSerializer.get().toBytes(keyToFind));
      AllColumnsIterator<String, byte[]> iterColonne = new AllColumnsIterator<String, byte[]>(queryDocubase);
      while (iterColonne.hasNext()) {
         HColumn<String, byte[]> colonne = iterColonne.next();
         indexes.put(colonne.getName(), new String[] { StringSerializer.get().fromBytes(colonne.getValue()), getDate(colonne.getClock()) });
      }
      return indexes;
   }
   
   @Test
   public void extractDocInfo() {
      
      Writer writer = null;
      
      // Création du répertoire de sortie s'il n'existe pas déjà
      File rep = new File(CHEMINREP);
      if (!rep.exists()) {
         rep.mkdir();
      }
      
      long nbRow = 0;
      try {
         File fichier = new File(rep, NOMFICHIER);
         writer = new FileWriter(fichier);
         
         Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
         RangeSlicesQuery<byte[], String, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily("DocInfo");
         queryDocubase.setColumnNames("SM_BASE_ID"); // ne recupere que le nom de la base
         AllRowsIterator<byte[], String, byte[]> iterRow = new AllRowsIterator<byte[], String, byte[]>(queryDocubase);
         while (iterRow.hasNext()) {
            Row<byte[], String, byte[]> row = iterRow.next();
   
            // les rows sans colonnes sont des rows "supprimees"
            if (!row.getColumnSlice().getColumns().isEmpty()) {
               
               HColumn<String, byte[]> colonneBase = row.getColumnSlice().getColumnByName("SM_BASE_ID");
               String currentBase = StringSerializer.get().fromBytes(colonneBase.getValue());
               if (currentBase.equals(nomBase)) {
            
                  // la cle de DocInfo est composee de :
                  // - id du doc
                  // - la valeur 0.0.0
                  Composite compositeKey = CompositeSerializer.get().fromBytes(row.getKey());
                  String idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(0)).toString();
                  
                  writer.write(idDoc);
                  writer.write("\n");
                  
                  nbRow++;
                  if (nbRow % 1000 == 0) {
                     LOGGER.debug("Nombre de docs traités {}", nbRow);
                  }
                  
                 
               }
            }
         }
      } catch (IOException exception) {
         LOGGER.error(exception.getMessage());

      } finally {
         closeWriter(writer);
      }
      LOGGER.debug("Nombre total de docs traités : {}", (nbRow-1));
   }
   
   private void closeWriter(Writer writer) {
      try {
         if (writer != null) {
            writer.close();
         }
      } catch (IOException exception) {
         System.err.println("impossible de fermer le flux");
      }
   }
   
   @Test
   public void extractTermInfoToAnalyze() throws IOException {
      
      boolean extractValue = true;
      
      List<String> docs = new ArrayList<String>();
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
         docs.add(nextLine[0]);
      }
      reader.close();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase);
      
      Writer writer = null;
      try {
         File fichier = new File(CHEMINREP, NOMFICHIER_TERM_INFO);
         writer = new FileWriter(fichier);
         
         Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
         RangeSlicesQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
         queryDocubase.setColumnFamily("TermInfo");
         queryDocubase.setRange(null, null, false, 1);
         AllRowsIterator<byte[], Composite, byte[]> iterRow = new AllRowsIterator<byte[], Composite, byte[]>(queryDocubase);
         while (iterRow.hasNext()) {
            Row<byte[], Composite, byte[]> row = iterRow.next();

            // les rows sans colonnes sont des rows "supprimees"
            if (!row.getColumnSlice().getColumns().isEmpty()) {
               
               // l'index a au moins une valeur
               // la cle de cette index est composee de :
               // - une chaine vide
               // - le nom de l'index
               // - la valeur de l'index
               Composite compositeKey = CompositeSerializer.get().fromBytes(row.getKey());
               String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(1));
               String value = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(2));
               
               boolean skip = false;
               if (indexName.equals("SM_FINAL_DATE") && StringUtils.isEmpty(value)) {
                  // bug indexation du final date vide
                  skip = true;
               }
               
               // on va verifie l'indexation des documents
               
               if (!skip) {
                  boolean trace = true;
                  
                  SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
                  queryDocubaseTerm.setColumnFamily("TermInfo");
                  queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
                  AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm);
                  while (iterColonneTerm.hasNext()) {
                     HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
                     
                     if (trace) {
                        LOGGER.debug("Verif de l'indexation ({}|{})", new String[] {indexName, value});
                        trace = false;
                     }
                     
                     // Le nom de la colonne est composé de 
                     // - l'uuid de la base
                     // - l'uuid du document
                     // - 0.0.0
                     UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
                     // on verifie que l'indexation est de la bonne base
                     if (idBase.toString().equals(baseId.toString())) {
                        UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
                        
                        if (!docs.contains(idDoc.toString())) {
                           writer.write(idDoc.toString());
                           writer.write(";");
                           writer.write("(" + indexName + "|" + value + ")");
                           if (extractValue) {
                              writer.write(";");
                              writer.write(IndexationTest.getHexString(colonne.getValue()));
                           }
                           writer.write("\n");
                        }
                     }
                  }
               }
            }
         }
         
      } catch (IOException exception) {
         LOGGER.error(exception.getMessage());

      } finally {
         closeWriter(writer);
      }
   }
   
   @Test
   public void getTermInfoByIndexName() {
      
      String indexName = "nid";
      String value = "9123";
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase);
      
      // l'index a au moins une valeur
      // la cle de cette index est composee de :
      // - une chaine vide
      // - le nom de l'index
      // - la valeur de l'index
      Composite compositeKey = new Composite();
      compositeKey.add("");
      compositeKey.add(indexName);
      compositeKey.add(value);
      
      SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
      queryDocubaseTerm.setColumnFamily("TermInfo");
      queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
      AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm);
      while (iterColonneTerm.hasNext()) {
         HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
         
         // Le nom de la colonne est composé de 
         // - l'uuid de la base
         // - l'uuid du document
         // - 0.0.0
         UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0));
         // on verifie que l'indexation est de la bonne base
         if (idBase.toString().equals(baseId.toString())) {
            UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
            LOGGER.info("{}", idDoc.toString());
         }
      }
   }
   
   @Test
   public void verifExtractTermInfo() throws IOException {
      
      boolean viewCreationAndDeletionEvents = false;
      boolean extractValue = false;
      
      Map<String, List<String>> errorsIndexation = new HashMap<String, List<String>>();
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER_TERM_INFO)), ';');
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
         if (!errorsIndexation.containsKey(nextLine[0])) {
            errorsIndexation.put(nextLine[0], new ArrayList<String>());
         }
         if (extractValue) {
            errorsIndexation.get(nextLine[0]).add(nextLine[1] + "@@" + nextLine[2]);
         } else {
            errorsIndexation.get(nextLine[0]).add(nextLine[1]);            
         }
      }
      reader.close();
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      // boucle sur les resultats
      for (String idDoc : errorsIndexation.keySet()) {
         // reverifie si le doc existe (cas ou le doc aurait ete cree entre l'extraction des docs et la verif)
         Map<String, String[]> infoDoc = getDocInfoById(keyspaceDocubase, UUID.fromString(idDoc));
         if (!(infoDoc != null && !infoDoc.keySet().isEmpty())) {
            // le doc n'existe vraiment pas
            Map<String, String> events = null;
            if (viewCreationAndDeletionEvents) {
               events = getEventDocByUUID(provider, idDoc);
               LOGGER.debug("doc inexistant : {} - cree le {}, supprime le {}", new String[] { idDoc, events.get("CREATE_DOCUMENT"), events.get("DELETE_DOCUMENT") });
            } else {
               LOGGER.debug("doc inexistant : {}", new String[] { idDoc });
            }
            
            for (String valueIndex : errorsIndexation.get(idDoc)) {
               String indexName;
               String indexValue;
               if (extractValue) {
                  indexName = valueIndex.split("@@")[0];
                  indexValue = valueIndex.split("@@")[1];
                  LOGGER.debug("    {} : {}", new String[] {indexName, indexValue});
               } else {
                  indexName = valueIndex;
                  indexValue = "";
                  LOGGER.debug("    {}", new String[] {indexName});
               }
            }
         }
      }
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void extractTermInfoRangeToAnalyze() throws IOException {
      
      boolean extractValue = true;
      
      List<String> docs = new ArrayList<String>();
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER)), ';');
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
         docs.add(nextLine[0]);
      }
      reader.close();
      
      // On recupere l'identifiant de la base
      UUID idBase = getBaseUUIDByName(nomBase);
      
      Writer writer = null;
      try {
         File fichier = new File(CHEMINREP, NOMFICHIER_TERM_INFO_RANGE);
         writer = new FileWriter(fichier);
         
         String[] cfRanges = {
               "TermInfoRangeDate",
               "TermInfoRangeDatetime", 
               "TermInfoRangeDouble",
               "TermInfoRangeFloat",
               "TermInfoRangeInteger",
               "TermInfoRangeLong",
               "TermInfoRangeString",
               "TermInfoRangeUUID",
         };
         
         for (String cfName : cfRanges) {
            
            Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
            RangeSlicesQuery<byte[], Composite, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
            queryDocubase.setColumnFamily(cfName);
            queryDocubase.setRange(null, null, false, 1);
            AllRowsIterator<byte[], Composite, byte[]> iterRow = new AllRowsIterator<byte[], Composite, byte[]>(queryDocubase);
            while (iterRow.hasNext()) {
               Row<byte[], Composite, byte[]> row = iterRow.next();

               // les rows sans colonnes sont des rows "supprimees"
               if (!row.getColumnSlice().getColumns().isEmpty()) {
                  
                  // l'index a au moins une valeur
                  // la cle de l'index est compose de :
                  // - le nom de l'index
                  // - le nom de la categorie
                  // - l'uuid de la base
                  // - le nombre de row de la categorie
                  Composite compositeKey = CompositeSerializer.get().fromBytes(row.getKey());
                  //String indexName = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(0));
                  String catName = StringSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(1));
                  UUID baseId = UUIDSerializer.get().fromByteBuffer((ByteBuffer) compositeKey.get(2));
                  
                  boolean skip = false;
                  if (!idBase.toString().equals(baseId.toString())) {
                     // pas la bonne base, on skip
                     skip = true;
                  } 
                  
                  // on va verifie l'indexation des documents
                  
                  if (!skip) {
                     SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
                     queryDocubaseTerm.setColumnFamily(cfName);
                     queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
                     AllColumnsIterator<Composite, byte[]> iterColonneTerm = new AllColumnsIterator<Composite, byte[]>(queryDocubaseTerm);
                     while (iterColonneTerm.hasNext()) {
                        HColumn<Composite, byte[]> colonne = iterColonneTerm.next();
                        
                        // Le nom de la colonne est composé de 
                        // - la valeur
                        // - l'uuid du document
                        // - 0.0.0
                        String valeur;
                        if (cfName.equals("TermInfoRangeDate") || cfName.equals("TermInfoRangeDatetime") || cfName.equals("TermInfoRangeString") || cfName.equals("TermInfoRangeUUID")) {
                           // la valeur est serialiser en string
                           valeur = StringSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)); 
                        } else if (cfName.equals("TermInfoRangeDouble")) {
                           valeur = DoubleSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)).toString();
                        } else if (cfName.equals("TermInfoRangeFloat")) {
                           valeur = FloatSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)).toString();
                        } else if (cfName.equals("TermInfoRangeInteger")) {
                           valeur = BigIntegerSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)).toString();
                        } else if (cfName.equals("TermInfoRangeLong")) {
                           valeur = LongSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(0)).toString();
                        } else {
                           valeur = "";
                        }
                        
                        boolean skipFinalDate = false;
                        if (catName.equals("SM_FINAL_DATE") && StringUtils.isEmpty(valeur)) {
                           // bug indexation du final date vide
                           skipFinalDate = true;
                        }
                        
                        if (!skipFinalDate) {
                           UUID idDoc = UUIDSerializer.get().fromByteBuffer((ByteBuffer) colonne.getName().get(1));
                           
                           LOGGER.debug("Verif de l'indexation ({}|{})", new String[] {catName, valeur});
                           
                           if (!docs.contains(idDoc.toString())) {
                              writer.write(idDoc.toString());
                              writer.write(";");
                              writer.write(cfName);
                              writer.write(";");
                              writer.write("(" + catName + "|" + valeur + ")");
                              if (extractValue) {
                                 writer.write(";");
                                 writer.write(IndexationTest.getHexString(colonne.getValue()));
                              }
                              writer.write("\n");
                           }
                        }
                     }
                  }
               }
            }
         }
         
      } catch (IOException exception) {
         LOGGER.error(exception.getMessage());

      } finally {
         closeWriter(writer);
      }
   }
   
   @Test
   public void verifExtractTermInfoRange() throws IOException {
      
      boolean viewCreationAndDeletionEvents = false;
      boolean extractValue = true;
      
      Map<String, List<String>> errorsIndexation = new HashMap<String, List<String>>();
      CSVReader reader = new CSVReader(new FileReader(new File(CHEMINREP, NOMFICHIER_TERM_INFO_RANGE)), ';');
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
         if (!errorsIndexation.containsKey(nextLine[0])) {
            errorsIndexation.put(nextLine[0], new ArrayList<String>());
         }
         if (extractValue) {
            errorsIndexation.get(nextLine[0]).add(nextLine[2] + "@@" + nextLine[3]);
         } else {
            errorsIndexation.get(nextLine[0]).add(nextLine[2]);            
         }
      }
      reader.close();
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      LOGGER.debug("Ouverture de la connexion à DFCE");
      ServiceProvider provider = ServiceProvider.newServiceProvider();
      provider.connect("_ADMIN", "DOCUBASE", url, 3 * 60 * 1000);
      
      Writer writer = null;
      long compteurDoc = 0;
      long compteurDocSicomor = 0;
      long compteurDocGroom = 0;
      try {
         File fichier = new File(CHEMINREP, NOMFICHIER_DOCS_ERREUR_TERM_INFO_RANGE);
         writer = new FileWriter(fichier);
      
         // boucle sur les resultats
         for (String idDoc : errorsIndexation.keySet()) {
            // reverifie si le doc existe (cas ou le doc aurait ete cree entre l'extraction des docs et la verif)
            Map<String, String[]> infoDoc = getDocInfoById(keyspaceDocubase, UUID.fromString(idDoc));
            boolean sicomor = false;
            boolean groom = false;
            if (!(infoDoc != null && !infoDoc.keySet().isEmpty())) {
               // le doc n'existe vraiment pas 
               LOGGER.debug("doc inexistant : {}", idDoc);
               writer.write(idDoc);
               writer.write(';');
               if (viewCreationAndDeletionEvents) {
                  Map<String, String> events = getEventDocByUUID(provider, idDoc);
                  writer.write(events.get("CREATE_DOCUMENT"));
                  writer.write(';');
                  writer.write(events.get("DELETE_DOCUMENT"));
                  writer.write(';');
               }
               boolean first = true;
               for (String valueIndex : errorsIndexation.get(idDoc)) {
                  String indexName;
                  String indexValue;
                  if (extractValue) {
                     indexName = valueIndex.split("@@")[0];
                     indexValue = valueIndex.split("@@")[1];
                  } else {
                     indexName = valueIndex;
                     indexValue = "";
                  }
                  LOGGER.debug("    {}", new String[] {indexName});
                  if (!first) {
                     writer.write(';');
                  }
                  writer.write(indexName);
                  first = false;
                  
                  if (indexName.startsWith("(cpt&")) {
                     sicomor = true;
                  }
                  if (indexName.startsWith("(drh&")) {
                     groom = true;
                  }
                  
                  if (extractValue) {
                     writer.write(';');
                     writer.write(indexValue);
                  }
               }
               writer.write('\n');
            }
            compteurDoc++;
            if (sicomor) {
               compteurDocSicomor++;
            }
            if (groom) {
               compteurDocGroom++;
            }
         }
      } catch (IOException exception) {
         LOGGER.error(exception.getMessage());

      } finally {
         closeWriter(writer);
      }
      LOGGER.debug("nb doc en erreur d'indexation: {}", compteurDoc);
      LOGGER.debug("nb doc sicomor: {}", compteurDocSicomor);
      LOGGER.debug("nb doc groom: {}", compteurDocGroom);
      
      LOGGER.debug("Fermeture de la connexion à DFCE");
      provider.disconnect();
   }
   
   @Test
   public void findBaseByName() {
      LOGGER.debug("Recuperation de l'id de la base {}", nomBase);
      UUID baseUUID = getBaseUUIDByName(nomBase);
      LOGGER.debug("Id de la base : {}", baseUUID.toString());
   }
   
   private Map<String, String> getEventDocByUUID(ServiceProvider provider, String idDoc) {
      Map<String, String> events = new HashMap<String, String>();
      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
      
      List<RMDocEvent> listEvents = provider.getRecordManagerService().getDocumentEventLogsByUUID(UUID.fromString(idDoc));
      for (RMDocEvent event : listEvents) {
         if (event.getEventType() == DocEventLogType.CREATE_DOCUMENT) {
            events.put(DocEventLogType.CREATE_DOCUMENT.name(), formatter.format(event.getEventDate()));
         } else if (event.getEventType() == DocEventLogType.DELETE_DOCUMENT) {
            events.put(DocEventLogType.DELETE_DOCUMENT.name(), formatter.format(event.getEventDate()));
         }
      }
      
      return events;
   }
   
   /**
    * Renvoie la représentation hexadécimale d'un tableau de bytes
    * 
    * @param bytes
    *           tableau de bytes
    * @return
    * @throws Exception
    */
   public static String getHexString(byte[] bytes) {
      String result = "";
      for (int i = 0; i < bytes.length; i++) {
         result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
      }
      return result;
   }
   
   @Test
   public void isMetaIndexedAndComputed() {
      
      String indexName = "nid";
      
      // creation de la rowKey
      StringBuffer buffer = new StringBuffer();
      buffer.append(nomBase);
      buffer.append((char) 65535);
      buffer.append(indexName);
      
      LOGGER.info("Recuperation de la categorie {} pour la base {}", indexName, nomBase);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<byte[], String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("BaseCategoriesReference");
      queryDocubase.setKey(StringSerializer.get().toBytes(buffer.toString()));
      queryDocubase.setColumnNames("indexed", "computed");
      
      QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase.execute();
      if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
         HColumn<String, byte[]> isIndexed = resultat.get().getColumnByName("indexed");
         if (isIndexed != null) {
            boolean valeur = BooleanSerializer.get().fromBytes(isIndexed.getValue());
            LOGGER.info("Est ce que la méta {} est indexée : {}", indexName, valeur);
         }
         HColumn<String, byte[]> isComputed = resultat.get().getColumnByName("computed");
         if (isComputed != null) {
            boolean valeur = BooleanSerializer.get().fromBytes(isComputed.getValue());
            LOGGER.info("Est ce que l'index de la méta {} est 'actif' : {}", indexName, valeur);
         }
      }
   }
   
   @Test
   public void isCompositeIndexComputed() {
      
      String indexName = "cpt&sco&SM_DOCUMENT_TYPE&nor&";
      
      LOGGER.info("Recuperation de l'index composite {}", indexName);
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String, String, byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("CompositeIndexesReference");
      queryDocubase.setKey(indexName);
      queryDocubase.setColumnNames("computed");
      
      QueryResult<ColumnSlice<String, byte[]>> resultat = queryDocubase.execute();
      if (resultat.get() != null && !resultat.get().getColumns().isEmpty()) {
         HColumn<String, byte[]> isComputed = resultat.get().getColumnByName("computed");
         if (isComputed != null) {
            boolean valeur = BooleanSerializer.get().fromBytes(isComputed.getValue());
            LOGGER.info("Est ce que l'index composite {} est 'actif' : {}", indexName, valeur);
         }
      }
   }
}

