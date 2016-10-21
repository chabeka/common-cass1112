package fr.urssaf.image.sae.test.dfce17;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.Nullable;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.AuthenticationCredentials;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.SimpleAuthenticationCredentials;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ByteBufferRange;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Composite;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.CheckpointManager;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.query.RowSliceQuery;
import com.netflix.astyanax.recipes.reader.AllRowsReader;
import com.netflix.astyanax.retry.ExponentialBackoff;
import com.netflix.astyanax.serializers.BytesArraySerializer;
import com.netflix.astyanax.serializers.CompositeSerializer;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.RangeBuilder;

@RunWith(BlockJUnit4ClassRunner.class)
public class FinalDateIndexationAstyanaxTest {
   
   private static final Logger LOGGER = LoggerFactory.getLogger(FinalDateIndexationAstyanaxTest.class);
   
   // Développement
   //private String hosts = "cer69imageint10.cer69.recouv:9160";
   
   // Integration cliente GNT
   //private String hosts = "cnp69intgntcas1.gidn.recouv:9160,cnp69intgntcas2.gidn.recouv:9160,cnp69intgntcas3.gidn.recouv:9160";
   
   // Integration cliente GNS
   //private String hosts = "hwi69intgnscas1.gidn.recouv:9160,hwi69intgnscas2.gidn.recouv:9160";
   
   // Integration nationale GNT
   //private String hosts = "cnp69gingntcas1.cer69.recouv:9160,cnp69gingntcas2.cer69.recouv:9160";
   
   // Integration nationale GNS
   //private String hosts = "hwi69ginsaecas1.cer69.recouv:9160,hwi69ginsaecas2.cer69.recouv:9160";
   
   // Validation nationale GNT
   //private String hosts = "cnp69givngntcas1.cer69.recouv:9160,cnp69givngntcas2.cer69.recouv:9160,cnp69givngntcas3.cer69.recouv:9160";
   
   // Validation nationale GNS
   //private String hosts = "hwi69givnsaecas1.cer69.recouv:9160,hwi69givnsaecas2.cer69.recouv:9160";
   
   // Pre-prod MOE
   private String hosts = "cnp69pprodsaecas1.cer69.recouv:9160,cnp69pprodsaecas2.cer69.recouv:9160,cnp69pprodsaecas3.cer69.recouv:9160,cnp69pprodsaecas4.cer69.recouv:9160,cnp69pprodsaecas5.cer69.recouv:9160,cnp69pprodsaecas6.cer69.recouv:9160";
   
   // Pré-prod nationale GNT
   //private String hosts = "cnp69pregntcas1.cer69.recouv:9160,cnp69pregntcas2.cer69.recouv:9160,cnp69pregntcas3.cer69.recouv:9160";
   
   // Pré-prod nationale GNS
   //private String hosts = "cnp69pregnscas1.cer69.recouv:9160,cnp69pregnscas2.cer69.recouv:9160,cnp69pregnscas3.cer69.recouv:9160,cnp69pregnscas4.cer69.recouv:9160,cnp69pregnscas5.cer69.recouv:9160,cnp69pregnscas6.cer69.recouv:9160";
   
   // Prod nationale GNT
   //private String hosts = "cnp69gntcas1.cer69.recouv:9160,cnp69gntcas2.cer69.recouv:9160,cnp69gntcas3.cer69.recouv:9160";
   
   // Prod nationale GNS
   //private String hosts = "cnp69saecas1.cer69.recouv:9160,cnp69saecas2.cer69.recouv:9160,cnp69saecas3.cer69.recouv:9160,cnp69saecas4.cer69.recouv:9160,cnp69saecas5.cer69.recouv:9160,cnp69saecas6.cer69.recouv:9160";
   
   private static final ColumnFamily<String, String> BASES_REFERENCE = ColumnFamily.newColumnFamily("BasesReference", StringSerializer.get(), StringSerializer.get());
   
   private static final ColumnFamily<Composite, Composite> TERM_INFO_RANGE_DATETIME = ColumnFamily.newColumnFamily("TermInfoRangeDatetime", CompositeSerializer.get(), CompositeSerializer.get());
   
   private static final ColumnFamily<byte[], Composite> TERM_INFO = ColumnFamily.newColumnFamily("TermInfo", BytesArraySerializer.get(), CompositeSerializer.get());
   
   private static final Composite START_SM_FINAL_DATE = new Composite(new Object[] { "", new UUID(0L, 0L), "0.0.0" });
   private static final Composite END_SM_FINAL_DATE = new Composite(new Object[] { "", new UUID(-1L, -1L), "0.0.0" });
   private static final ByteBufferRange range = new RangeBuilder().setLimit(200).setStart(START_SM_FINAL_DATE, CompositeSerializer.get()).setEnd(END_SM_FINAL_DATE, CompositeSerializer.get()).build();
   
   private AstyanaxContext<Keyspace> astyanaxContext;
   
   private Keyspace getKeyspaceDocubaseFromKeyspace() {
      
      ConnectionPoolConfigurationImpl connectionPoolConfigurationImpl = new ConnectionPoolConfigurationImpl(
            "SimpleConfig");

      connectionPoolConfigurationImpl.setSeeds(hosts);
      connectionPoolConfigurationImpl.setPort(9160);
      connectionPoolConfigurationImpl.setSocketTimeout(20000);
      connectionPoolConfigurationImpl.setMaxConnsPerHost(6);

      AuthenticationCredentials credentials = new SimpleAuthenticationCredentials(
            "root", "regina4932");

      connectionPoolConfigurationImpl.setAuthenticationCredentials(credentials);
      LOGGER.info("Cassandra authentication enable for user : "
            + credentials.getUsername());

      AstyanaxConfigurationImpl astyanaxConfigurationImpl = new AstyanaxConfigurationImpl();
      astyanaxConfigurationImpl
            .setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE);
      astyanaxConfigurationImpl.setDiscoveryType(NodeDiscoveryType.NONE);
      astyanaxConfigurationImpl.setRetryPolicy(new ExponentialBackoff(1000, 3));
      astyanaxConfigurationImpl.setDefaultReadConsistencyLevel(ConsistencyLevel.CL_QUORUM);
      astyanaxConfigurationImpl
            .setDefaultWriteConsistencyLevel(ConsistencyLevel.CL_QUORUM);

      astyanaxConfigurationImpl.setAsyncExecutor(Executors.newFixedThreadPool(
            30,
            new ThreadFactoryBuilder().setDaemon(true)
                  .setNameFormat("AstyanaxAsync-%d").build()));

      this.astyanaxContext = new AstyanaxContext.Builder()
            .forKeyspace("Docubase")
            .withConnectionPoolConfiguration(connectionPoolConfigurationImpl)
            .withAstyanaxConfiguration(astyanaxConfigurationImpl)
            .buildKeyspace(ThriftFamilyFactory.getInstance());

      this.astyanaxContext.start();
      return this.astyanaxContext.getClient();
   }

   private List<UUID> getBases(Keyspace keyspaceDocubase) throws Exception {
      // CopyOnWriteArrayList est une ArrayList thread safe 
      final List<UUID> basesId = new CopyOnWriteArrayList<UUID>();
      
      boolean result = new AllRowsReader.Builder<String, String>(keyspaceDocubase,
            BASES_REFERENCE).withPageSize(100) // Read 100 rows at a time
            .withConcurrencyLevel(10) // Split entire token range into 10.
                                      // Default is by number of nodes.
            .withPartitioner(null) // this will use keyspace's partitioner
            .withColumnSlice("uuid")
            .forEachRow(new Function<Row<String,String>, Boolean>() {
               @Override
               public Boolean apply(@Nullable Row<String, String> row) {
                  // Process the row here ...
                  // This will be called from multiple threads so make sure your
                  // code is thread safe
                  
                  if (row.getColumns() != null && !row.getColumns().isEmpty()) {
                     basesId.add(row.getColumns().getUUIDValue("uuid", new UUID(0L, 0L)));
                  }
                  
                  return true;
               }
            }).build().call();
      
      return basesId;
   }
   
   
   @Test
   public void getTermInfoRangeToDelete() throws IOException, Exception {
      
      Keyspace ks = getKeyspaceDocubaseFromKeyspace();
      
      // boucle de comptage sur chaque bases
      for (UUID idBase : getBases(ks)) {
         LOGGER.info("Start counting TermInfoRangeDateTime for {}...", idBase.toString());
         
         // TODO : faire une methode pour lire la CF IndexReference pour connaitre les ranges
         Composite range0 = new Composite(new Object[] { "", "SM_FINAL_DATE", idBase, Integer.valueOf(0) });
         
         LOGGER.info("key : {}", bytesToHex(CompositeSerializer.get().toBytes(range0)));
      
         AtomicLong counter = new AtomicLong(0L);
         // comptage du nombre colonnes
         try {
            RowQuery<Composite, Composite> pageQuery = ks
                  .prepareQuery(TERM_INFO_RANGE_DATETIME).getRow(range0)
                  .autoPaginate(true).withColumnRange(range);
            
            ColumnList<Composite> columns;
            while (!(columns = (ColumnList<Composite>) pageQuery.execute()
                  .getResult()).isEmpty()) {
               for (Column<Composite> column : columns) {
                  String date = (String) ((Composite) column.getName()).get(0,
                        StringSerializer.get());
                  if (StringUtils.isEmpty(date)) {
                     long incrementAndGet = counter.incrementAndGet();
                     if (incrementAndGet % 50000L == 0L)
                        LOGGER.info("Current Index Counter : " + incrementAndGet);
                  }

               }
            }
         } catch (ConnectionException e) {
            LOGGER.error(e.getMessage());
         }
         
         LOGGER.info("Index Counter for {} : {}", idBase.toString(), counter.get());
      }
      
   }
   
   public static String bytesToHex(byte[] bytes) {
      final char[] hexArray = "0123456789ABCDEF".toCharArray();
      char[] hexChars = new char[bytes.length * 2];
      for (int j = 0; j < bytes.length; j++) {
         int v = bytes[j] & 0xFF;
         hexChars[j * 2] = hexArray[v >>> 4];
         hexChars[j * 2 + 1] = hexArray[v & 0x0F];
      }
      return new String(hexChars);
   }
   
   @Test
   public void trace() {
      Composite range0 = new Composite(new Object[] { "", "SM_FINAL_DATE", UUID.fromString("f573ae93-ac6a-4615-a23b-150fd621b5a0"), Integer.valueOf(0) });
      //Composite range0 = new Composite(new Object[] { "", "SM_UUID", UUID.fromString("f573ae93-ac6a-4615-a23b-150fd621b5a0"), Integer.valueOf(0) });
      
      /*final StringBuilder builder = new StringBuilder();
      for(byte b : CompositeSerializer.get().toBytes(range0)) {
          builder.append(String.format("%02x", b));
      }*/
      System.out.println(bytesToHex(CompositeSerializer.get().toBytes(range0)));
   }
   
   @Test
   public void getTermInfoToDelete() throws IOException, Exception {
      
      Keyspace ks = getKeyspaceDocubaseFromKeyspace();
      
      // boucle de comptage sur chaque bases
      for (UUID idBase : getBases(ks)) {
         LOGGER.info("Start counting TermInfo for {}...", idBase.toString());
         
         // TODO : faire une methode pour lire la CF IndexReference pour connaitre les ranges
         Composite rowKey = new Composite(new Object[] { "", "SM_FINAL_DATE", "" });
         
         Composite debutRange = new Composite(new Object[] { idBase, new UUID(0L, 0L), "0.0.0" });
         Composite finRange = new Composite(new Object[] { idBase, new UUID(-1L, -1L), "0.0.0" });
         ByteBufferRange columnRange = new RangeBuilder().setLimit(200).setStart(debutRange, CompositeSerializer.get()).setEnd(finRange, CompositeSerializer.get()).build();
         
         AtomicLong counter = new AtomicLong(0L);
         // comptage du nombre colonnes
         try {
            RowQuery<byte[], Composite> pageQuery = ks
                  .prepareQuery(TERM_INFO).getRow(CompositeSerializer.get().toBytes(rowKey))
                  .autoPaginate(true).withColumnRange(columnRange);

            ColumnList<Composite> columns;
            while (!(columns = (ColumnList<Composite>) pageQuery.execute()
                  .getResult()).isEmpty()) {
               for (Column<Composite> column : columns) {
                  long incrementAndGet = counter.incrementAndGet();
                  if (incrementAndGet % 50000L == 0L)
                     LOGGER.debug("Current Index Counter : " + incrementAndGet);
               }
            }
         } catch (ConnectionException e) {
            LOGGER.error(e.getMessage());
         }
         
         LOGGER.info("Index Counter for {} : {}", idBase.toString(), counter.get());
      }
 
   }
   
   protected class MyCheckpointManager implements CheckpointManager {
      
      private ThreadLocal<String> localToken;
      
      private SortedMap<String, String> checkPoints = new TreeMap<String, String>();
      
      private SortedMap<String, String> columnCheckPoints = new TreeMap<String, String>();
      
      public MyCheckpointManager() {
         super();
         
         this.checkPoints.put("0", "7303281609858639500663810551048405102");
         this.checkPoints.put("106338239662793269832304564822427566075", "119194668153079139623852195080544800431");
         this.checkPoints.put("127605887595351923798765477786913079290", "143581753208707387309058070467296050237");
         this.checkPoints.put("148873535527910577765226390751398592505", "159417960728488304456551148772276796073");
         this.checkPoints.put("21267647932558653966460912964485513215", "34016982470157237097914154381258796487");
         this.checkPoints.put("42535295865117307932921825928971026430", "53732422928499941831062571434364808438");
         this.checkPoints.put("63802943797675961899382738893456539645", "72734513273685678379573351846904019796");
         this.checkPoints.put("85070591730234615865843651857942052860", "92458288930182834377007427020246563510");
         
         this.columnCheckPoints.put("42535295865117307932921825928971026430", "0010F573AE93AC6A4615A23B150FD621B5A00000108CB4E961DFE7481FAB9924AE5D9CA195000005302E302E3000");
         this.columnCheckPoints.put("85070591730234615865843651857942052860", "0010F573AE93AC6A4615A23B150FD621B5A000001018B29587A2A64444B82AAAF9413292C2000005302E302E3000");
         this.columnCheckPoints.put("63802943797675961899382738893456539645", "0010F573AE93AC6A4615A23B150FD621B5A0000010E8A5457A9FB34A14942FC80973C372BF000005302E302E3000");
         
         this.localToken = new ThreadLocal();
      }

      @Override
      public void trackCheckpoint(String startToken, String checkpointToken)
            throws Exception {
         // TODO Auto-generated method stub
         
      }

      @Override
      public SortedMap<String, String> getCheckpoints() throws Exception {
         return checkPoints;
      }
      
      @Override
      public String getCheckpoint(String startToken) throws Exception {
         this.localToken.set(startToken);
         return checkPoints.get(startToken);
      }
      
      public String getToken() {
         return this.localToken.get();
      }
      
      public byte[] getColumnCheckpoint() {
         byte[] checkpointColumn = null;
         String token = (String)this.localToken.get();
         
         if (columnCheckPoints.get(token) != null) {
            checkpointColumn = DatatypeConverter.parseHexBinary(StringUtils.defaultIfEmpty(columnCheckPoints.get(token), null));
         }
         
         return checkpointColumn;
       }
   }
   
   @Test
   public void testSmFinalDate() throws Exception {
      final Keyspace ks = getKeyspaceDocubaseFromKeyspace();
      
      final ColumnFamily<Composite, Composite> TERM_INFO_2 = ColumnFamily.newColumnFamily("TermInfo", CompositeSerializer.get(), CompositeSerializer.get());
      
      final MyCheckpointManager checkpointManager = new MyCheckpointManager();
      
      
      new AllRowsReader.Builder<Composite, Composite>(ks, TERM_INFO_2).withColumnRange(null, null, false, 1).withConcurrencyLevel(8).withPartitioner(null).withCheckpointManager(checkpointManager).withPageSize(1).forEachRow(new Function<Row<Composite, Composite>, Boolean>()
      {
        public Boolean apply(Row<Composite, Composite> input) 
        {
           Composite key = (Composite) input.getKey();
           if (key.size() >= 2) {
              LOGGER.info("{} - Ici: {}", checkpointManager.getToken(), StringSerializer.get().fromByteBuffer((ByteBuffer) key.get(1)));
              
              /*RangeBuilder rangeBuilder = new RangeBuilder().setLimit(50);
              if (checkpointManager.getColumnCheckpoint() != null) {
                rangeBuilder.setStart(checkpointManager.getColumnCheckpoint());
              }
              
              RowQuery pageQuery = ks.prepareQuery(TERM_INFO_2).getRow(key).autoPaginate(true).withColumnRange(rangeBuilder.build());
              
              try {
              
                 ColumnList columns;
                 long compteur = 0;
                 while (!(columns = (ColumnList)pageQuery.execute().getResult()).isEmpty())
                 {
                    if (!columns.isEmpty()) {
                       compteur++;
                       if (compteur % 1000 == 0) {
                          LOGGER.info("Ici2: {} - {}", StringSerializer.get().fromByteBuffer((ByteBuffer) key.get(1)), compteur);
                       }
                    }
                 }
                 
                 LOGGER.info("Ici3: {} - {}", StringSerializer.get().fromByteBuffer((ByteBuffer) key.get(1)), compteur);
                 
              } catch (ConnectionException ex) {
                 LOGGER.error(ex.getMessage());
              }*/
              
              
              
           }
           return Boolean.TRUE;
        }
      }).build().call();
   }
   
   
   @Test
   @Ignore
   public void deleteTermInfoRangeToDelete() throws IOException, Exception {
      
      Keyspace ks = getKeyspaceDocubaseFromKeyspace();
      
      ThreadPoolExecutor pool = (ThreadPoolExecutor) this.astyanaxContext.getAstyanaxConfiguration().getAsyncExecutor();
      pool.setRejectedExecutionHandler(new RejectedExecutionHandler() {
         @Override
         public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            /*try {
               Thread.sleep(2000);
            } catch (InterruptedException e) {
               LOGGER.error("Erreur : {}", e.getMessage());
            }
            executor.execute(r);*/
            throw new RuntimeException("Un element rejecte : " + executor.getCompletedTaskCount());
         }
      });
      
      // boucle de comptage sur chaque bases
      for (UUID idBase : getBases(ks)) {
         LOGGER.info("Start counting TermInfoRangeDateTime for {}...", idBase.toString());
         
         // TODO : faire une methode pour lire la CF IndexReference pour connaitre les ranges
         Composite range0 = new Composite(new Object[] { "", "SM_FINAL_DATE", idBase, Integer.valueOf(0) });
      
         AtomicLong counter = new AtomicLong(0L);
         // comptage du nombre colonnes
         try {
            RowQuery<Composite, Composite> pageQuery = ks
                  .prepareQuery(TERM_INFO_RANGE_DATETIME).getRow(range0)
                  .autoPaginate(true).withColumnRange(range);

            ColumnList<Composite> columns;
            while (!(columns = (ColumnList<Composite>) pageQuery.execute()
                  .getResult()).isEmpty()) {
               for (Column<Composite> column : columns) {
                  String date = (String) ((Composite) column.getName()).get(0,
                        StringSerializer.get());
                  if (StringUtils.isEmpty(date)) {
                     long incrementAndGet = counter.incrementAndGet();
                     if (incrementAndGet % 50000L == 0L) {
                        LOGGER.info("Deleting index #" + incrementAndGet + " : key + \", column : \" + columnName);");
                     }
                     ks.prepareColumnMutation(TERM_INFO_RANGE_DATETIME, range0, column.getName()).deleteColumn().execute();
                     if (incrementAndGet > 1000) {
                        throw new Exception("On arrete ici");
                     }
                  }

               }
            }
         } catch (ConnectionException e) {
            LOGGER.error(e.getMessage());
         }
         
         LOGGER.info("Index Counter for {} : {}", idBase.toString(), counter.get());
      }
      
   }
}

