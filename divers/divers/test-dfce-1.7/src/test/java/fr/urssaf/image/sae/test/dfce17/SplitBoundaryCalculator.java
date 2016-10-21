package fr.urssaf.image.sae.test.dfce17;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.ExceptionCallback;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.mapping.MappingCache;
import com.netflix.astyanax.mapping.MappingUtil;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

public class SplitBoundaryCalculator {
   
   private final static ColumnFamily<String, String> cf = new ColumnFamily<String, String>(
         "DocInfo", StringSerializer.get(), StringSerializer.get());

   /**
    * Représente le keyspace cassandra sur lequel on travaille
    */
   Keyspace keyspace;

   /**
    * Facilite le mapping cassandra<->entité
    */
   MappingUtil mapper;

   /**
    * La où on veut dumper
    */
   PrintStream sysout;

   @Before
   public void init() throws Exception {
      String servers;
      // servers =
      // "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
      // //GIVN
      // servers = "cnp69saecas1:9160, cnp69saecas2:9160, cnp69saecas3:9160";
      // // Production
      // servers = "hwi54saecas1.cve.recouv:9160"; // CNH
      // servers = "cer69imageint9.cer69.recouv:9160";
      // servers = "cer69imageint10.cer69.recouv:9160";
      // servers = "10.203.34.39:9160"; // Noufnouf
      // servers =
      // "hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160";
      // servers = "hwi69ginsaecas2.cer69.recouv:9160";
      // servers = "cer69-saeint3:9160";
      //servers = "cnp69pprodsaecas1:9160,cnp69pprodsaecas2:9160,cnp69pprodsaecas3:9160"; // Préprod
      //servers = "cnp69pregnscas1.cer69.recouv:9160,cnp69pregnscas2.cer69.recouv:9160,cnp69pregnscas3.cer69.recouv:9160,cnp69pregnscas4.cer69.recouv:9160,cnp69pregnscas5.cer69.recouv:9160,cnp69pregnscas6.cer69.recouv:9160"; // Préprod CNP
      servers = "cnp6saecvecas1.cve.recouv:9160,cnp3saecvecas1.cve.recouv:9160,cnp7saecvecas1.cve.recouv:9160"; // Charge

      AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            "root", "regina4932");

      AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
            .forCluster("Docubase")
            .forKeyspace("Docubase")
            .withAstyanaxConfiguration(
                  new AstyanaxConfigurationImpl()
                        .setDiscoveryType(NodeDiscoveryType.NONE)
                        .setDefaultReadConsistencyLevel(
                              ConsistencyLevel.CL_ONE)
                        .setDefaultWriteConsistencyLevel(
                              ConsistencyLevel.CL_QUORUM))
            .withConnectionPoolConfiguration(
                  new ConnectionPoolConfigurationImpl("MyConnectionPool")
                        .setPort(9160).setMaxConnsPerHost(1)
                        .setSeeds(servers)
                        .setAuthenticationCredentials(credentials))
            .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      keyspace = context.getEntity();
      mapper = new MappingUtil(keyspace, new MappingCache());

      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("c:/tmp/out.txt");

   }

   @Test
   public void testCalculateSplits_SM_ARCHIVAGE_DATE() throws Exception {
      final int nbDocsToRead = 2000000;
      final int nbBuckets = 2000000;  
      final int nbSplits = 70;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "SM_ARCHIVAGE_DATE");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplitsOfRange_SM_ARCHIVAGE_DATE() throws Exception {
      final int nbDocsToRead = 80000;
      final int nbBuckets = 80000;
      final int nbSplits = 105;
      String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "SM_ARCHIVAGE_DATE", "20150902135428761", "max_upper_bound");
      System.out.println("Splits : " + splits);
   }

   @Test
   public void testCalculateSplits_Siret() throws Exception {
      final int nbDocsToRead = 2000000;
      final int nbBuckets = 2000000;  
      final int nbSplits = 300;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "srt");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplitsOfRange_Siret() throws Exception {
      final int nbDocsToRead = 20000;
      final int nbBuckets = 20000;  
      final int nbSplits = 4;
      //String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "srt", "48250", "48475");
      //String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "srt", "48025", "48250");
      //String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "srt", "78850", "79075");
      //String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "srt", "49150", "49375");
      //String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "srt", "79300", "79525");
      String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "srt", "48925", "49150");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplitsOfRange_List_Siret() throws Exception {
      final int nbDocsToRead = 20000;
      final int nbBuckets = 20000;  
      final int nbSplits = 4;
      
      String[] ranges = { "52975|53200",
            "53650|53875",
            "53425|53650"
      };
      
      List<String> newRanges = new ArrayList<String>(); 
      for (String range : ranges) {
         
         String borneMin = range.split("\\|")[0];
         String borneMax = range.split("\\|")[1];
         
         String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "srt", borneMin, borneMax);
         newRanges.add(splits);
      }

      for (String splits : newRanges) {
         System.out.println("Splits : " + splits);
      }
   }

   @Test
   public void testCalculateSplits_Siren() throws Exception {
      final int nbDocsToRead = 1000000;
      final int nbBuckets = 1000000;  
      final int nbSplits = 200;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "srn");
      System.out.println("Splits : " + splits);
   }

   @Test
   public void testCalculateSplits_NumeroCompte() throws Exception {
      final int nbDocsToRead = 84000;
      final int nbBuckets = 8000;  
      final int nbSplits = 42;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "nce");
      System.out.println("Splits : " + splits);
   }

   @Test
   public void testCalculateSplits_ArchivageDate() throws Exception {
      final int nbDocsToRead = 84000;
      final int nbBuckets = 8000;  
      final int nbSplits = 42;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "SM_ARCHIVAGE_DATE");
      System.out.println("Splits : " + splits);
   }

   @Test
   public void testCalculateSplits_SM_LIFE_CYCLE_REFERENCE_DATE() throws Exception {
      final int nbDocsToRead = 2000000;
      final int nbBuckets = 2000000;  
      final int nbSplits = 54;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "SM_LIFE_CYCLE_REFERENCE_DATE");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplitsOfRange_SM_LIFE_CYCLE_REFERENCE_DATE() throws Exception {
      final int nbDocsToRead = 80000;
      final int nbBuckets = 80000;
      //final int nbSplits = 8;
      //final int nbSplits = 11;
      final int nbSplits = 16;
      //String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "SM_LIFE_CYCLE_REFERENCE_DATE", "20120202000000000", "20120529091102320");
      //String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "SM_LIFE_CYCLE_REFERENCE_DATE", "20150917113514699", "20160317152131126");
      String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "SM_LIFE_CYCLE_REFERENCE_DATE", "20160329145255326", "max_upper_bound");
      System.out.println("Splits : " + splits);
   }

   @Test
   public void testCalculateSplits_SM_CREATION_DATE() throws Exception {
      final int nbDocsToRead = 2000000;
      final int nbBuckets = 2000000;  
      final int nbSplits = 70;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "SM_CREATION_DATE");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_SM_MODIFICATION_DATE() throws Exception {
      final int nbDocsToRead = 2000000;
      final int nbBuckets =    2000000;  
      final int nbSplits = 70;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "SM_MODIFICATION_DATE");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplitsOfRange_SM_MODIFICATION_DATE() throws Exception {
      final int nbDocsToRead = 80000;
      final int nbBuckets = 80000;
      final int nbSplits = 105;
      String splits = calculateSplitsOfRange(nbDocsToRead, nbBuckets, nbSplits, "SM_MODIFICATION_DATE", "20160322122557342", "max_upper_bound");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_nce() throws Exception {
      final int nbDocsToRead = 2000000;
      final int nbBuckets =    2000000;  
      final int nbSplits = 300;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "nce");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_npe() throws Exception {
      final int nbDocsToRead = 500000;
      final int nbBuckets =    500000;  
      final int nbSplits = 70;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "npe");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_nci() throws Exception {
      final int nbDocsToRead = 1000000;
      final int nbBuckets =    1000000;  
      final int nbSplits = 150;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "nci");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_den() throws Exception {
      final int nbDocsToRead = 500000;
      final int nbBuckets = 500000;  
      final int nbSplits = 100;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "den");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_rib() throws Exception {
      final int nbDocsToRead = 500000;
      final int nbBuckets = 500000;  
      final int nbSplits = 35;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "rib");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_rum() throws Exception {
      final int nbDocsToRead = 500000;
      final int nbBuckets = 500000;  
      final int nbSplits = 40;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "rum");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_nre() throws Exception {
      final int nbDocsToRead = 500000;
      final int nbBuckets = 500000;  
      final int nbSplits = 27;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "nre");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_psi() throws Exception {
      final int nbDocsToRead = 500000;
      final int nbBuckets = 500000;  
      final int nbSplits = 82;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "psi");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_nne() throws Exception {
      final int nbDocsToRead = 500000;
      final int nbBuckets = 500000;  
      final int nbSplits = 14;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "nne");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_nic() throws Exception {
      final int nbDocsToRead = 200000;
      final int nbBuckets = 200000;  
      final int nbSplits = 27;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "nic");
      System.out.println("Splits : " + splits);
   }
   
   @Test
   public void testCalculateSplits_rdo() throws Exception {
      final int nbDocsToRead = 200000;
      final int nbBuckets = 200000;  
      final int nbSplits = 35;
      String splits = calculateSplits(nbDocsToRead, nbBuckets, nbSplits, "rdo");
      System.out.println("Splits : " + splits);
   }

   /**
    * Calcul les ranges des splits pour un index
    * @param nbDocsToRead  : nombre de documents à lire (taille de l'échantillon)
    * @param nbBuckets     : nombre de buckets à garder en mémoire (ex : 1000)
    * @param nbSplits      : nombre de splits à faire
    * @param columnName : nom de la colonne correspondant à l'index
    * @return les splits, par exemple : [min_lower_bound TO 20130124012514529[|[20130124012514529 TO 20131130031701620[|[20131130031701620 TO max_upper_bound]
    * @throws Exception
    */
   private String calculateSplits(int nbDocsToRead, int nbBuckets, int nbSplits, String columnName) throws Exception {
      OperationResult<Rows<String, String>> rows = keyspace
            .prepareQuery(cf).getAllRows()
            .setRowLimit(200)
            // This is the page size
            // .withColumnRange(new RangeBuilder().setLimit(10).build())
            .withColumnSlice(columnName)
            .setExceptionCallback(new ExceptionCallback() {
               public boolean onException(ConnectionException e) {
                  Assert.fail(e.getMessage());
                  return true;
               }
            }).execute();

      int counter = 0;
      String bucketLowerLimits[] = new String[nbBuckets];      // Limite basse incluse du bucket
      int bucketDocCount[] = new int[nbBuckets];
      
      // Parcours des documents. On s'arrête lorsqu'on a atteint nbDocsToRead documents.
      for (Row<String, String> row : rows.getResult()) {
         //String key = row.getKey();
         ColumnList<String> columns = row.getColumns();
         if (columns.size() == 0) continue;
         Column<String> value = row.getColumns().getColumnByIndex(0);
         
         // On se sert des premiers documents pour délimiter les buckets
         if (counter < nbBuckets - 1) {
            bucketLowerLimits[counter] = StringUtils.stripAccents(value.getStringValue().toLowerCase());
            if (counter == nbBuckets - 2) {
               // On fixe arbitrairement la limite du dernier bucket
               bucketLowerLimits[counter + 1] = "";
               // On trie les buckets
               Arrays.sort(bucketLowerLimits);
               // On vérifie qu'il n'y a pas de doublons
               for(int i = 0; i < nbBuckets - 1; i++) {
                  if (bucketLowerLimits[i] == bucketLowerLimits[i+1]) throw new Exception("Doublon de bucket en position " + i);
               }
               // Chaque bucket a déjà un document, sauf le premier
               for(int i = 1; i < nbBuckets; i++) {
                  bucketDocCount[i] = 1;
               }
            }
         }
         // On classe les documents suivants dans les buckets
         else {
            // On trouve le bucket concerné par binary search
            int bucket = Arrays.binarySearch(bucketLowerLimits, value.getStringValue().toLowerCase());
            if (bucket < 0) bucket = -2 - bucket;
            bucketDocCount[bucket]++;
         }

         counter++;
         if (counter % 1000 == 0) {
            System.out.println("Counter : " + counter);
         }
         
         if (counter > nbDocsToRead) {
            // On a fini le parcours de l'échantillon de documents
            break;
         }
      }
      
      // Le parcours des documents est fini. On agrège les buckets pour former la délimitation des splits.
      String splitLowerLimits[] = new String[nbSplits];     // Limite basse incluse du split
      splitLowerLimits[0] = "";
      float docsPerSplit = (float) nbDocsToRead / nbSplits;
      int nextBucket = 0;
      int currentDocCount = 0;
      for (int i = 1; i < nbSplits; i++) {
         float desiredDocCount = docsPerSplit * i;
         while (true) {
            // On regarde s'il faut inclure le prochain bucket dans le split
            float delta1 = Math.abs(currentDocCount - desiredDocCount); 
            float delta2 = Math.abs(currentDocCount + bucketDocCount[nextBucket] - desiredDocCount);
            if (delta2 <= delta1) {
               // Oui, il faut inclure ce bucket dans le split
               currentDocCount += bucketDocCount[nextBucket];
               nextBucket ++;
            }
            else {
               // Non, on s'arrête ici
               splitLowerLimits[i] = bucketLowerLimits[nextBucket - 1];
               System.out.println("Split " + i + " : nextBucket = " + nextBucket + " - currentDocCount=" + currentDocCount);
               break;
            }
         }
      }
      
      // On vérifie qu'on n'a pas de doublons dans les limites des splits
      for(int i = 0; i < nbSplits - 1; i++) {
         if (splitLowerLimits[i] == splitLowerLimits[i+1]) throw new Exception("Doublon de split en position " + i);
      }
      
      // Mise en forme des splits
      String s = "[min_lower_bound TO " + splitLowerLimits[1] + "[";
      for(int i = 1; i < nbSplits - 1; i++) {
         s += "|[" + splitLowerLimits[i] + " TO " + splitLowerLimits[i+1] + "[";
      }
      s += "|[" + splitLowerLimits[nbSplits - 1] + " TO max_upper_bound]";
      return s;
   }
   
   /**
    * Calcul les ranges des splits pour un range d'un index
    * @param nbDocsToRead  : nombre de documents à lire (taille de l'échantillon)
    * @param nbBuckets     : nombre de buckets à garder en mémoire (ex : 1000)
    * @param nbSplits      : nombre de splits à faire
    * @param columnName : nom de la colonne correspondant à l'index
    * @param rangeMinVal : valeur minimum du range a splitter
    * @param rangeMaxVal : valeur maximum du range a splitter
    * @return les splits, par exemple : [min_lower_bound TO 20130124012514529[|[20130124012514529 TO 20131130031701620[|[20131130031701620 TO max_upper_bound]
    * @throws Exception
    */
   private String calculateSplitsOfRange(int nbDocsToRead, int nbBuckets, int nbSplits, String columnName, String rangeMinVal, String rangeMaxVal) throws Exception {
      OperationResult<Rows<String, String>> rows = keyspace
            .prepareQuery(cf).getAllRows()
            .setRowLimit(200)
            // This is the page size
            // .withColumnRange(new RangeBuilder().setLimit(10).build())
            .withColumnSlice(columnName)
            .setExceptionCallback(new ExceptionCallback() {
               public boolean onException(ConnectionException e) {
                  Assert.fail(e.getMessage());
                  return true;
               }
            }).execute();

      int counter = 0;
      int realCounter = 0;
      String bucketLowerLimits[] = new String[nbBuckets];      // Limite basse incluse du bucket
      int bucketDocCount[] = new int[nbBuckets];
      
      // Parcours des documents. On s'arrête lorsqu'on a atteint nbDocsToRead documents.
      for (Row<String, String> row : rows.getResult()) {
         //String key = row.getKey();
         ColumnList<String> columns = row.getColumns();
         if (columns.size() == 0) continue;
         Column<String> value = row.getColumns().getColumnByIndex(0);
         
         boolean docInRange = false;
         if ("min_lower_bound".equals(rangeMinVal) && "max_upper_bound".equals(rangeMaxVal)) {
            // tous les docs 
            docInRange = true;
         } else if ("min_lower_bound".equals(rangeMinVal) && value.getStringValue().toLowerCase().compareTo(rangeMaxVal) < 0) {
            // le doc est entre la borne min et rangeMaxVal
            docInRange = true;
         } else if (value.getStringValue().toLowerCase().compareTo(rangeMinVal) >= 0 && "max_upper_bound".equals(rangeMaxVal)) {
            // le doc est entre la borne rangeMinVal et max
            docInRange = true;
         } else if (value.getStringValue().toLowerCase().compareTo(rangeMinVal) >= 0 && value.getStringValue().toLowerCase().compareTo(rangeMaxVal) < 0) {
            // le doc est entre la borne rangeMinVal et rangeMaxVal
            docInRange = true;
         }
         
         if (docInRange) {
         
            // On se sert des premiers documents pour délimiter les buckets
            if (counter < nbBuckets - 1) {
               bucketLowerLimits[counter] = StringUtils.stripAccents(value.getStringValue().toLowerCase());
               if (counter == nbBuckets - 2) {
                  // On fixe arbitrairement la limite du dernier bucket
                  bucketLowerLimits[counter + 1] = "";
                  // On trie les buckets
                  Arrays.sort(bucketLowerLimits);
                  // On vérifie qu'il n'y a pas de doublons
                  for(int i = 0; i < nbBuckets - 1; i++) {
                     if (bucketLowerLimits[i] == bucketLowerLimits[i+1]) throw new Exception("Doublon de bucket en position " + i);
                  }
                  // Chaque bucket a déjà un document, sauf le premier
                  for(int i = 1; i < nbBuckets; i++) {
                     bucketDocCount[i] = 1;
                  }
               }
            }
            // On classe les documents suivants dans les buckets
            else {
               // On trouve le bucket concerné par binary search
               int bucket = Arrays.binarySearch(bucketLowerLimits, value.getStringValue().toLowerCase());
               if (bucket < 0) bucket = -2 - bucket;
               bucketDocCount[bucket]++;
            }
   
            counter++;
            if (counter % 1000 == 0) {
               System.out.println("Counter : " + counter);
            }
            
            if (counter > nbDocsToRead) {
               // On a fini le parcours de l'échantillon de documents
               break;
            }
         }
         realCounter++;
         if (realCounter % 10000 == 0) {
            System.out.println("Real counter : " + realCounter);
         }
      }
      
      // Le parcours des documents est fini. On agrège les buckets pour former la délimitation des splits.
      String splitLowerLimits[] = new String[nbSplits];     // Limite basse incluse du split
      splitLowerLimits[0] = "";
      float docsPerSplit = (float) nbDocsToRead / nbSplits;
      int nextBucket = 0;
      int currentDocCount = 0;
      for (int i = 1; i < nbSplits; i++) {
         float desiredDocCount = docsPerSplit * i;
         while (true) {
            // On regarde s'il faut inclure le prochain bucket dans le split
            float delta1 = Math.abs(currentDocCount - desiredDocCount); 
            float delta2 = Math.abs(currentDocCount + bucketDocCount[nextBucket] - desiredDocCount);
            if (delta2 <= delta1) {
               // Oui, il faut inclure ce bucket dans le split
               currentDocCount += bucketDocCount[nextBucket];
               nextBucket ++;
            }
            else {
               // Non, on s'arrête ici
               splitLowerLimits[i] = bucketLowerLimits[nextBucket - 1];
               System.out.println("Split " + i + " : nextBucket = " + nextBucket + " - currentDocCount=" + currentDocCount);
               break;
            }
         }
      }
      
      // On vérifie qu'on n'a pas de doublons dans les limites des splits
      for(int i = 0; i < nbSplits - 1; i++) {
         if (splitLowerLimits[i] == splitLowerLimits[i+1]) throw new Exception("Doublon de split en position " + i);
      }
      
      // Mise en forme des splits
      String s = "[" + rangeMinVal + " TO " + splitLowerLimits[1] + "[";
      for(int i = 1; i < nbSplits - 1; i++) {
         s += "|[" + splitLowerLimits[i] + " TO " + splitLowerLimits[i+1] + "[";
      }
      s += "|[" + splitLowerLimits[nbSplits - 1] + " TO " + rangeMaxVal + "]";
      return s;
   }
}
