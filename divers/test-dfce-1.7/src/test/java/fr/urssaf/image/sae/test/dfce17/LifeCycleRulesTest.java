package fr.urssaf.image.sae.test.dfce17;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import me.prettyprint.cassandra.model.ConfigurableConsistencyLevel;
import me.prettyprint.cassandra.serializers.BooleanSerializer;
import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.DateSerializer;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.ObjectSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.cassandra.service.FailoverPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.HConsistencyLevel;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.Composite;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.SliceQuery;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.springframework.batch.core.JobInstance;
import org.springframework.security.core.codec.Hex;

import com.google.common.primitives.Longs;

@RunWith(BlockJUnit4ClassRunner.class)
public class LifeCycleRulesTest {

   // Developpement 
   private String hosts = "cer69imageint10.cer69.recouv";
   
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
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl,
            failoverPolicy, credentials);
   }
   
   @Test
   public void findLifeCycleRules() {
      
      String codeRnd = "1.2.1.3.6";
      
      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      SliceQuery<String,byte[],byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, StringSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("LifeCycleRules").setKey(codeRnd);
      
      AllColumnsIterator<byte[], byte[]> iterateur = new AllColumnsIterator<byte[], byte[]>(queryDocubase);
      
      while (iterateur.hasNext()) {
         HColumn<byte[], byte[]> column = iterateur.next();
         
         try {
            Composite composite = CompositeSerializer.get().fromBytes(column.getName());
            if (composite != null && composite.size() > 0) {
               // composite
               Iterator<Object> iter = composite.iterator();
               StringBuffer buffer = new StringBuffer();
               while (iter.hasNext()) {
                  String element = StringSerializer.get().fromByteBuffer((ByteBuffer)iter.next());
                  if (element.length() > 1) {
                     buffer.append(element);
                  } else {
                     buffer.append(element.getBytes()[0]);
                  }
                  buffer.append("|");
               }
               System.out.println(buffer.toString());
            }
         } catch (IllegalArgumentException ex) {
            // gestion par l'erreur
            String nomColonne = StringSerializer.get().fromBytes(column.getName());
            System.out.println(nomColonne);
         }
      }
   }
   
   @Test
   public void getKeyDocInfo() {
      String uuid = "7f7abcc3-72ff-40f2-9e33-73bf286fdcb9";
      Composite composite = new Composite();
      composite.add(UUID.fromString(uuid));
      composite.add("0.0.0");
      
      String valeurHexa = new String(Hex.encode(CompositeSerializer.get().toBytes(composite)));
      System.out.println(valeurHexa);
   }
   
   @Test
   public void convertToBoolean() {
      String valeur = "01";
      BigInteger bi = new BigInteger(valeur, 16);
      
      System.out.println(BooleanSerializer.get().fromBytes(Longs.toByteArray(bi.longValue()))); 
   }
   
   @Test
   public void convertToDate() {
      String valeur = "0000014db9f2e96b";
      BigInteger bi = new BigInteger(valeur, 16);
      
      System.out.println(new Date(bi.longValue()));
      
      System.out.println(DateSerializer.get().fromBytes(Longs.toByteArray(bi.longValue()))); 
   }
   
   @Test
   public void convertToLong() {
      String valeur = "00019000";
      BigInteger bi = new BigInteger(valeur, 16);
      
      System.out.println(bi.longValue());
      
      System.out.println(LongSerializer.get().fromBytes(Longs.toByteArray(bi.longValue())));
   }
   
   @Test
   public void getKeyTermInfo() {
      Composite composite = new Composite();
      composite.add("");
      composite.add("SM_LIFE_CYCLE_REFERENCE_DATE");
      composite.add("20150528134330713");
      
      String valeurHexa = new String(Hex.encode(CompositeSerializer.get().toBytes(composite)));
      System.out.println(valeurHexa);
   }
   
   public static byte[] hexStringToByteArray(String s) {
      int len = s.length();
      byte[] data = new byte[len / 2];
      for (int i = 0; i < len; i += 2) {
          data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                               + Character.digit(s.charAt(i+1), 16));
      }
      return data;
  }
   
   @Test
   public void getValueTermInfo() throws IOException, ClassNotFoundException {
      String valeur = "aced0005737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000003077080000004000000028740010534d5f4352454154494f4e5f44415445737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a6578700000000177040000000a740011323031333031313430303030303030303078740013534d5f4449474553545f414c474f524954484d7371007e00030000000177040000000a7400055348412d3178740015534d5f4b45595f5245464552454e43455f555549447371007e00030000000177040000000a74000078740007534d5f53495a457371007e00030000000177040000000a7400133030303030303030303030303030353635383778740003646f6d7371007e00030000000177040000000a7400013278740003636f707371007e00030000000177040000000a740005555237353078740003636f747371007e00030000000177040000000a7400047472756578740012534d5f5245504f5349544f52595f4e414d457371007e00030000000177040000000a74000963617373616e6472617874000d534d5f46494e414c5f444154457371007e00030000000177040000000a71007e000b78740010534d5f444953504f53414c5f444154457371007e00030000000177040000000a71007e000b7874000b534d5f46494c454e414d457371007e00030000000177040000000a740004646f63317874000d534d5f53544152545f504147457371007e00030000000177040000000a74000a2d30303030303030303178740014534d5f4d4f44494649434154494f4e5f444154457371007e00030000000177040000000a740011323031353035323831333432313933393578740011534d5f4152434849564147455f444154457371007e00030000000177040000000a74001132303135303532383133343231393339357874000364656e7371007e00030000000177040000000a74002454657374203232312d436170747572654d6173736549442d4f4b2d546f722d3530303030787400036666697371007e00030000000177040000000a740007666d742f333534787400036e72657371007e00030000000177040000000a74000533363737387874000c534d5f46494c455f555549447371007e00030000000177040000000a74002464363432636636362d633962622d343939372d613837622d61303162666466346465636278740009534d5f4449474553547371007e00030000000177040000000a74002861326639336631663132316562626130666165663263303539366632663132366561636165373762787400036170727371007e00030000000177040000000a7400084144454c414944457874000b534d5f4841535f4e4f54457371007e00030000000177040000000a74000566616c7365787400036e62707371007e00030000000177040000000a74000a303030303030303030327874001c534d5f4c4946455f4359434c455f5245464552454e43455f444154457371007e00030000000177040000000a74001132303135303532383133343333303731337874000c534d5f455854454e53494f4e7371007e00030000000177040000000a7400035044467874000a534d5f56455253494f4e7371007e00030000000177040000000a740005302e302e307874000a534d5f424153455f49447371007e00030000000177040000000a7400075341452d494e547874000c534d5f424153455f555549447371007e00030000000177040000000a74002463343735383437362d613765372d343438302d383863632d6538373630663838643164617874000376726e7371007e00030000000177040000000a74000531312e31327874000d534d5f494e4445585f434f44457371007e00030000000177040000000a71007e000b787400036373657371007e00030000000177040000000a74001143535f414e4349454e5f53595354454d4578740008534d5f5449544c457371007e00030000000177040000000a7400184174746573746174696f6e20646520766967696c616e636578740007534d5f555549447371007e00030000000177040000000a74002435613536316464612d366562362d346262382d613234362d663733316237356333303832787400036466637371007e00030000000177040000000a74000832303138303532377874000b534d5f454e445f504147457371007e00030000000177040000000a74000a2d30303030303030303178740010534d5f444f43554d454e545f545950457371007e00030000000177040000000a74000a322e332e312e312e313278740003636f677371007e00030000000177040000000a74000543455236397874000a534d5f5649525455414c7371007e00030000000177040000000a71007e003f787400036163747371007e00030000000177040000000a740001337874000c534d5f49535f46524f5a454e7371007e00030000000177040000000a71007e000b787400036974697371007e00030000000177040000000a74002431336431333564302d303533642d313165352d626461612d6638623135363939326438627878";
      
      ByteArrayInputStream stream = new ByteArrayInputStream(hexStringToByteArray(valeur));
      ObjectInputStream ois = new ObjectInputStream(stream);
      
      Map<String, List<String>> map = (Map<String, List<String>>) ois.readObject();
      
      for (String code : map.keySet()) {
         System.out.println(code + " " + map.get(code));
      }
   }
   
   @Test
   public void getKeyTermInfoRange() {
      Composite composite = new Composite();
      composite.add("");
      composite.add("SM_UUID");
      composite.add(UUID.fromString("c4758476-a7e7-4480-88cc-e8760f88d1da"));
      composite.add(2);
      
      String valeurHexa = new String(Hex.encode(CompositeSerializer.get().toBytes(composite)));
      System.out.println(valeurHexa);
   }
   
   @Test
   public void getKeyJobInstance() throws NoSuchAlgorithmException {

      String parameters = "end.date=1427420092018;type=SYSTEM;";
      
      // calcul le digest md5 des parametres
      MessageDigest m = MessageDigest.getInstance("MD5");
      m.reset();
      m.update(parameters.getBytes());
      byte[] digest = m.digest();
      BigInteger bigInt = new BigInteger(1,digest);
      String hashtext = String.format("%032x", new Object[] { bigInt });
      
      Composite composite = new Composite();
      composite.add("clearEventJob");
      composite.add(hashtext);
      
      String valeurHexa = new String(Hex.encode(CompositeSerializer.get().toBytes(composite)));
      System.out.println(valeurHexa);
   }
   
   @Test
   public void getValueJobInstanceSer() throws IOException, ClassNotFoundException {
      String valeur = "aced00057372002a6f72672e737072696e676672616d65776f726b2e62617463682e636f72652e4a6f62496e7374616e63654ea9b794e5554d210200024c00076a6f624e616d657400124c6a6176612f6c616e672f537472696e673b4c000d6a6f62506172616d657465727374002e4c6f72672f737072696e676672616d65776f726b2f62617463682f636f72652f4a6f62506172616d65746572733b787200256f72672e737072696e676672616d65776f726b2e62617463682e636f72652e456e746974796d08afa24bcf13cd0200024c000269647400104c6a6176612f6c616e672f4c6f6e673b4c000776657273696f6e7400134c6a6176612f6c616e672f496e74656765723b78707372000e6a6176612e6c616e672e4c6f6e673b8be490cc8f23df0200014a000576616c7565787200106a6176612e6c616e672e4e756d62657286ac951d0b94e08b020000787000000000000000a9737200116a6176612e6c616e672e496e746567657212e2a0a4f781873802000149000576616c75657871007e00080000000074000d636c6561724576656e744a6f627372002c6f72672e737072696e676672616d65776f726b2e62617463682e636f72652e4a6f62506172616d6574657273e3117842782ff4090200014c000a706172616d657465727374000f4c6a6176612f7574696c2f4d61703b7870737200176a6176612e7574696c2e4c696e6b6564486173684d617034c04e5c106cc0fb0200015a000b6163636573734f72646572787200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c77080000001000000002740004747970657372002b6f72672e737072696e676672616d65776f726b2e62617463682e636f72652e4a6f62506172616d65746572f8f568faaeeb437b0200024c0009706172616d657465727400124c6a6176612f6c616e672f4f626a6563743b4c000d706172616d657465725479706574003b4c6f72672f737072696e676672616d65776f726b2f62617463682f636f72652f4a6f62506172616d6574657224506172616d65746572547970653b787074000653595354454d7e7200396f72672e737072696e676672616d65776f726b2e62617463682e636f72652e4a6f62506172616d6574657224506172616d657465725479706500000000000000001200007872000e6a6176612e6c616e672e456e756d00000000000000001200007870740006535452494e47740008656e642e646174657371007e00147372000e6a6176612e7574696c2e44617465686a81014b597419030000787077080000014c58de1672787e71007e0019740004444154457800";
      
      ByteArrayInputStream stream = new ByteArrayInputStream(hexStringToByteArray(valeur));
      ObjectInputStream ois = new ObjectInputStream(stream);
      
      JobInstance job = (JobInstance) ois.readObject();
      
      System.out.println(job);
   }
   
   @Test
   public void getValueTermInfoRange() throws IOException, ClassNotFoundException {
      String valeur = "aced0005737200116a6176612e7574696c2e486173684d61700507dac1c31660d103000246000a6c6f6164466163746f724900097468726573686f6c6478703f4000000000000c77080000001000000006740008757365726e616d657372001a6a6176612e7574696c2e4172726179732441727261794c697374d9a43cbecd8806d20200015b0001617400135b4c6a6176612f6c616e672f4f626a6563743b7870757200135b4c6a6176612e6c616e672e537472696e673badd256e7e91d7b470200007870000000017400065f41444d494e7400096576656e7455554944737200236a6176612e7574696c2e436f6c6c656374696f6e732453696e676c65746f6e4c6973742aef29103ca79b970200014c0007656c656d656e747400124c6a6176612f6c616e672f4f626a6563743b787074002435306363646537302d356631302d343537342d613464352d6634383030356439353436337400096576656e74446174657371007e000a74000d313437303237313839303735327400106576656e744465736372697074696f6e7371007e000a74000b6f70656e53657373696f6e74000a61747472696275746573737200136a6176612e7574696c2e41727261794c6973747881d21d99c7619d03000149000473697a6578700000000277040000000a7400177468726f77efbfbf4261642063726564656e7469616c7374000d61726730efbfbf5f41444d494e7874000b6576656e745374617475737371007e000a7400074641494c55524578";
      
      ByteArrayInputStream stream = new ByteArrayInputStream(hexStringToByteArray(valeur));
      ObjectInputStream ois = new ObjectInputStream(stream);
      
      Map<String, List<String>> map = (Map<String, List<String>>) ois.readObject();
      
      for (String code : map.keySet()) {
         System.out.println(code + " " + map.get(code));
      }
   }
}
