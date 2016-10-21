package fr.urssaf.image.sae.test.divers.dfce;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.test.divers.cassandra.AllRowsIterator;
import fr.urssaf.image.sae.test.divers.cassandra.AllColumnsIterator;

@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-dev.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-interne-gnt.xml" })
@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-integ-cliente-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-giin69-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-givn-gns.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-pre-prod-gnt.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod.xml" })
//@ContextConfiguration(locations = { "/applicationContext-commons-dfce-test-prod-gnt.xml" })
public class DisableCompositeIndexTest {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(DisableCompositeIndexTest.class);

   /**
    * Injecte le keyspace SAE.
    */
   @Autowired
   private Keyspace keyspace;

   /**
    * Recuperation
    */
   @Autowired
   private CassandraServerBean cassandraServer;

   /**
    * Desactive un index composite
    */
   @Test
   @Ignore("L'index composite n'est plus créé par le script de création")
   public void disableCompositeIndexNff() throws Exception {
      String nomIndexComposite = "cpt&sco&SM_DOCUMENT_TYPE&nff&";
      boolean dryRun = true;

      LOGGER.debug("Lancement en mode dry run (aucun update) : {}", dryRun);

      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();

      // suppression de l'index dans la CF TermInfoRangeString
      deleteIndexInTermInfoRangeString(keyspaceDocubase, nomIndexComposite, dryRun);
   }

   private void deleteIndexInTermInfoRangeString(Keyspace keyspaceDocubase, 
         String nomIndexComposite, boolean dryRun)
         throws Exception {
      
      String cfName = "TermInfoRangeString";
      
      RangeSlicesQuery<Composite, byte[], byte[]> query = HFactory
            .createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer
                  .get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      query.setColumnFamily(cfName).setKeys(null, null).setReturnKeysOnly();
      query.setRowCount(5000);
      
      AllRowsIterator<Composite, byte[], byte[]> iterateur = new AllRowsIterator<Composite, byte[], byte[]>(query);
      
      while (iterateur.hasNext()) {
         Row<Composite, byte[], byte[]> row = iterateur.next();
         
         // la key est compose de :
         // - l'espace de stockage de l'index ("" pour l'index par defaut, RB pour la corbeille)
         // - le nom de l'index
         // - l'uuid de la base
         // - le numero du range
         
         if (row.getKey().size() == 1) {
            // ignore les anciennes index de TermInfoRangeString (voir CRTL-132)
            continue;
         }
         
         String nomIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
         if (nomIndex.equals(nomIndexComposite)) {
            if (!dryRun) {
               LOGGER.info("Suppression de l'indexation dans la CF {} : {}", cfName, nomIndex);
               Mutator<Composite> mutator = HFactory.createMutator(
                     keyspaceDocubase, CompositeSerializer.get());
               mutator.addDeletion(row.getKey(), cfName);
               mutator.execute();
            } else {
               LOGGER.debug(
                     "Mode dry run : On devrait supprimer l'indexation dans la CF {} : {}",
                     cfName, nomIndex);
            }
         }
      }
   }
   
   private void deleteIndexInTermInfoRangeString(Keyspace keyspaceDocubase, 
         String nomIndexComposite, UUID idBase, Long numeroRange, boolean dryRun)
         throws Exception {
      
      String cfName = "TermInfoRangeString";
      
      // la cle de l'index est compose de :
      // - le nom de l'index
      // - le nom de la categorie
      // - l'uuid de la base
      // - le numero de row de la categorie
      Composite compositeKey = new Composite();
      compositeKey.add(0, "");
      compositeKey.add(1, nomIndexComposite);
      compositeKey.add(2, idBase);
      compositeKey.add(3, toByteArrayNumRow(numeroRange));
      
      SliceQuery<byte[], Composite, byte[]> queryDocubaseTerm = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), CompositeSerializer.get(), BytesArraySerializer.get());
      queryDocubaseTerm.setColumnFamily(cfName);
      queryDocubaseTerm.setKey(CompositeSerializer.get().toBytes(compositeKey));
      queryDocubaseTerm.setRange(null, null, false, 10);
      
      QueryResult<ColumnSlice<Composite, byte[]>> resultatQuery = queryDocubaseTerm.execute();
      if (resultatQuery.get() != null && !resultatQuery.get().getColumns().isEmpty()) {
         if (!dryRun) {
            LOGGER.info("Suppression du range {} de l'index {} pour la base {} dans la CF {}", new String[] { numeroRange.toString(), nomIndexComposite, idBase.toString(), cfName });
            Mutator<byte[]> mutator = HFactory.createMutator(
                  keyspaceDocubase, BytesArraySerializer.get());
            mutator.addDeletion(CompositeSerializer.get().toBytes(compositeKey), cfName);
            mutator.execute();
         } else {
            LOGGER.debug(
                  "Mode dry run : On devrait supprimer le range {} de l'index {} pour la base {} dans la CF {}",
                  new String[] { numeroRange.toString(), nomIndexComposite, idBase.toString(), cfName });
         }
      }
   }
   
   private byte[] toByteArrayNumRow(long valeur) {
      int nbOctet;
      if (valeur <= Byte.MAX_VALUE) {
         nbOctet = 1;
      } else if (valeur > Byte.MAX_VALUE && valeur <= Short.MAX_VALUE) {
         nbOctet = 2;
      } else if (valeur > Short.MAX_VALUE && valeur <= Integer.MAX_VALUE) {
         nbOctet = 4;
      } else if (valeur > Integer.MAX_VALUE && valeur <= Long.MAX_VALUE) {
         nbOctet = 8;
      } else {
         throw new IllegalArgumentException("valeur trop grande");
      }
      byte[] result = new byte[nbOctet];
      for (int i = nbOctet - 1; i >= 0; i--) {
        result[i] = (byte) (valeur & 0xffL);
        valeur >>= 8;
      }
      return result;
   }
   
   /**
    * Desactive un index composite
    */
   @Test
   //@Ignore
   public void disableCompositeIndexMontants() throws Exception {
      String indexesComposite[] = { "cpt&sco&SM_DOCUMENT_TYPE&mre&",
            "cpt&sco&SM_DOCUMENT_TYPE&mde&" };
      boolean dryRun = true;

      LOGGER.debug("Lancement en mode dry run (aucun update) : {}", dryRun);

      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();

      for (String nomIndexComposite : indexesComposite) {
         
         // TODO : dev non fait de la suppression de TermInfo
         
         // suppression de l'index dans la CF TermInfoRangeString
         deleteIndexInTermInfoRangeString(keyspaceDocubase, nomIndexComposite, dryRun);
         
         // suppression de l'index dans la CF CompositeIndexesReference
         deleteIndexInCompositeIndexesReference(keyspaceDocubase,
               nomIndexComposite, dryRun);
         
         // suppression de l'index dans la CF IndexReference
         deleteIndexInIndexReference(keyspaceDocubase,
               nomIndexComposite, dryRun);
      }
   }

   private void deleteIndexInCompositeIndexesReference(
         Keyspace keyspaceDocubase, String nomIndexComposite, boolean dryRun) {
      String cfName = "CompositeIndexesReference";

      SliceQuery<String, String, String> queryIndexComposite = HFactory
            .createSliceQuery(keyspaceDocubase,
                  StringSerializer.get(), StringSerializer.get(),
                  StringSerializer.get());
      queryIndexComposite.setColumnFamily(cfName);
      queryIndexComposite.setKey(nomIndexComposite);
      queryIndexComposite.setRange(null, null, false, 10); // recupere 10 colonnes
      QueryResult<ColumnSlice<String,String>> resultIndexComposite = queryIndexComposite
            .execute();
      
      if (resultIndexComposite.get() != null && !resultIndexComposite.get().getColumns().isEmpty()) {
         if (!dryRun) {
            LOGGER.info("Suppression de l'index composite dans la CF {} : {}",
                  cfName, nomIndexComposite);
            Mutator<String> mutator = HFactory.createMutator(
                  keyspaceDocubase, StringSerializer.get());
            mutator.addDeletion(nomIndexComposite, cfName);
            mutator.execute();
         } else {
            LOGGER
                  .debug(
                        "Mode dry run : On devrait supprimer l'index composite dans la CF {} : {}",
                        cfName, nomIndexComposite);
         }
      }      
   }
   
   /**
    * Desactive un index composite
    */
   @Test
   @Ignore("Ne doit plus être lancé suite a une décision avec sicomor de ne plus faire de recherche contenant mais commencant par")
   public void disableCompositeIndexNomFournisseur() throws Exception {
      String indexesComposite[] = { "cpt&sco&SM_DOCUMENT_TYPE&nfo&" };
      boolean dryRun = true;

      LOGGER.debug("Lancement en mode dry run (aucun update) : {}", dryRun);

      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();

      for (String nomIndexComposite : indexesComposite) {

         // TODO : dev non fait de la suppression de TermInfo
         
         // suppression de l'index dans la CF TermInfoRangeString
         deleteIndexInTermInfoRangeString(keyspaceDocubase, nomIndexComposite, dryRun);
         
         // suppression de l'index dans la CF CompositeIndexesReference
         deleteIndexInCompositeIndexesReference(keyspaceDocubase,
               nomIndexComposite, dryRun);
         
         // suppression de l'index dans la CF IndexReference
         deleteIndexInIndexReference(keyspaceDocubase,
               nomIndexComposite, dryRun);
      }
   }
   
   /**
    * Desactive un index composite
    */
   @Test
   public void disableCompositeIndexGroom() throws Exception {
      String indexesComposite[] = { "cop&SM_CREATION_DATE&",
            "cop&nma&",
            "cop&nma&frd&",
            "cop&npa&SM_CREATION_DATE&",
            "cop&pag&SM_CREATION_DATE&"};
      boolean dryRun = true;

      LOGGER.debug("Lancement en mode dry run (aucun update) : {}", dryRun);

      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();

      for (String nomIndexComposite : indexesComposite) {
         
         // TODO : dev non fait de la suppression de TermInfo
         
         // suppression de l'index dans la CF TermInfoRangeString
         deleteIndexInTermInfoRangeString(keyspaceDocubase, nomIndexComposite, dryRun);
         
         // suppression de l'index dans la CF CompositeIndexesReference
         deleteIndexInCompositeIndexesReference(keyspaceDocubase,
               nomIndexComposite, dryRun);
         
         // suppression de l'index dans la CF IndexReference
         deleteIndexInIndexReference(keyspaceDocubase,
               nomIndexComposite, dryRun);
      }
   }
   
   /**
    * Desactive un index composite
    */
   @Test
   public void disableCompositeIndexWatt() throws Exception {
      String indexesComposite[] = { "cot&cop&swa&cpr&ctr&SM_ARCHIVAGE_DATE&",
            "cot&cop&swa&SM_ARCHIVAGE_DATE&" };
      boolean dryRun = true;

      LOGGER.debug("Lancement en mode dry run (aucun update) : {}", dryRun);

      Keyspace keyspaceDocubase = getKeyspaceDocubaseFromKeyspace();
      
      // 1) suppression de l'index dans la CF TermInfo
      deleteIndexInTermInfo(keyspaceDocubase, Arrays.asList(indexesComposite), dryRun);
      
      // 2) Traitement de la CF TermInfoRangeString pour chaque index
      Map<UUID, String> bases = getAllBases(keyspaceDocubase);
      for (UUID idBase : bases.keySet()) {
         LOGGER.info("Analyse de la base {} ({})", new String[] { bases.get(idBase), idBase.toString() });
         
         for (String nomIndexComposite : indexesComposite) {
            List<Long> ranges = getAllRangesInIndexReferenceByNameAndBase(
                  keyspaceDocubase, nomIndexComposite, idBase.toString());
            for (Long numeroRange : ranges) {
               LOGGER.info("Analyse du range {} de l'index {}", new String[] { numeroRange.toString(), nomIndexComposite });
               
               // suppression de l'index dans la CF TermInfoRangeString
               deleteIndexInTermInfoRangeString(keyspaceDocubase, nomIndexComposite, idBase, numeroRange, dryRun);
            }
            
            // suppression de l'index dans la CF IndexReference
            deleteIndexInIndexReference(keyspaceDocubase,
                  nomIndexComposite, idBase.toString(), dryRun);
         }
         
         LOGGER.info("Fin d'analyse de la base {}", bases.get(idBase));
      }
      
      // 3) Suppression des CF CompositeIndexesReference
      for (String nomIndexComposite : indexesComposite) {

         // suppression de l'index dans la CF CompositeIndexesReference
         deleteIndexInCompositeIndexesReference(keyspaceDocubase,
               nomIndexComposite, dryRun);
      }
   }
   
   private Map<UUID, String> getAllBases(Keyspace keyspaceDocubase) {
      Map<UUID, String> resultat = new HashMap<UUID, String>();
      
      RangeSlicesQuery<String, String, byte[]> queryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, StringSerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily("BasesReference");
      queryDocubase.setKeys(null, null);
      queryDocubase.setRowCount(5000);
      queryDocubase.setColumnNames("uuid");
      
      QueryResult<OrderedRows<String, String, byte[]>> resultatQuery = queryDocubase.execute();
      if (resultatQuery.get() != null) {
         for (Row<String, String, byte[]> row : resultatQuery.get().getList()) {
            HColumn<String, byte[]> colonne = row.getColumnSlice().getColumnByName("uuid");
            if (colonne != null) {
               UUID idBase = UUIDSerializer.get().fromBytes(colonne.getValue());
               resultat.put(idBase, row.getKey());
            }
         }
      }
      return resultat;
   }
   
   private void deleteIndexInTermInfo(Keyspace keyspaceDocubase, 
         List<String> indexesComposite, boolean dryRun) {
      
      String cfName = "TermInfo";
      long compteur = 0;
      
      RangeSlicesQuery<Composite,byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, CompositeSerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily(cfName).setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(5000);
      
      AllRowsIterator<Composite, byte[], byte[]> iterateur = new AllRowsIterator<Composite, byte[], byte[]>(rangeQueryDocubase);
      
      while (iterateur.hasNext()) {
         Row<Composite, byte[], byte[]> row = iterateur.next();
         
         // la key est compose de :
         // - l'espace de stockage de l'index ("" pour l'index par defaut, RB pour la corbeille)
         // - le nom de l'index
         // - la valeur de l'index
         
         //String typeIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(0));
         String termField = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
         //String valeurIndex = StringSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(2));
         //LOGGER.debug("Row key {}:{}:{}", new String[] { typeIndex, termField, valeurIndex });
         
         if (indexesComposite.contains(termField)) {
            if (!dryRun) {
               LOGGER.info("Suppression de l'indexation dans la CF {} : {}", cfName, termField);
               Mutator<Composite> mutator = HFactory.createMutator(
                     keyspaceDocubase, CompositeSerializer.get());
               mutator.addDeletion(row.getKey(), cfName);
               mutator.execute();
            } else {
               LOGGER.debug(
                     "Mode dry run : On devrait supprimer l'indexation dans la CF {} : {}",
                     cfName, termField);
            }
         }
         
         compteur++;
         if (compteur % 100000 == 0) {
            LOGGER.info("{} index analysés dans la CF {}", compteur, cfName);
         }
      }
   }
   
   private Long convertByteToLong(byte[] bytes) {
      long value = 0;
      for (int i = 0; i < bytes.length; i++) {
         value = (value << 8) + (bytes[i] & 0xff);
      }
      return Long.valueOf(value);
   }
   
   private List<Long> getAllRangesInIndexReferenceByNameAndBase(Keyspace keyspaceDocubase, String nomIndexComposite, String idBase) {
      String cfName = "IndexReference";
      char separateur = 65535;
      
      List<Long> ranges = new ArrayList<Long>();
      
      StringBuffer rowKey = new StringBuffer();
      rowKey.append(nomIndexComposite);
      rowKey.append(separateur);
      rowKey.append(idBase);
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(cfName);
      queryDocubase.setKey(StringSerializer.get().toBytes(rowKey.toString()));
      
      AllColumnsIterator<String,byte[]> iterateur = new AllColumnsIterator<String, byte[]>(queryDocubase);
      while (iterateur.hasNext()) {
         HColumn<String, byte[]> colonne = iterateur.next();
         if (colonne.getName().matches("rangeIndexes.[0-9]*.key")) {
            ranges.add(convertByteToLong(colonne.getValue()));
         } 
      }
      
      Collections.sort(ranges);
      return ranges;
   }
   
   private void deleteIndexInIndexReference(Keyspace keyspaceDocubase, 
         String nomIndexComposite, boolean dryRun) {
      
      String cfName = "IndexReference";
      char separateur = 65535;
      
      RangeSlicesQuery<byte[],byte[],byte[]> rangeQueryDocubase = HFactory.createRangeSlicesQuery(keyspaceDocubase, BytesArraySerializer.get(), BytesArraySerializer.get(), BytesArraySerializer.get());
      rangeQueryDocubase.setColumnFamily(cfName).setKeys(null, null);
      rangeQueryDocubase.setReturnKeysOnly();
      rangeQueryDocubase.setRowCount(5000);

      AllRowsIterator<byte[],byte[],byte[]> iterateur = new AllRowsIterator<byte[], byte[], byte[]>(rangeQueryDocubase);
      
      while (iterateur.hasNext()) {
         Row<byte[], byte[], byte[]> row = iterateur.next();
         
         // la key est compose de :
         // - le nom de l'index
         // - l'id de la base
         
         String rowKey = StringSerializer.get().fromBytes(row.getKey());
         
         String nomIndex = rowKey.split(Character.toString(separateur))[0];
         //UUID idBase = UUIDSerializer.get().fromByteBuffer((ByteBuffer) row.getKey().get(1));
         
         if (nomIndex.equals(nomIndexComposite)) {
            if (!dryRun) {
               LOGGER.info("Suppression de l'indexation dans la CF {} : {}", cfName, nomIndex);
               Mutator<byte[]> mutator = HFactory.createMutator(
                     keyspaceDocubase, BytesArraySerializer.get());
               mutator.addDeletion(row.getKey(), cfName);
               mutator.execute();
            } else {
               LOGGER.debug(
                     "Mode dry run : On devrait supprimer l'indexation dans la CF {} : {}",
                     cfName, nomIndex);
            }
         }
      }
   }
   
   private void deleteIndexInIndexReference(Keyspace keyspaceDocubase, 
         String nomIndexComposite, String idBase, boolean dryRun) {
      
      String cfName = "IndexReference";
      char separateur = 65535;
      
      StringBuffer rowKey = new StringBuffer();
      rowKey.append(nomIndexComposite);
      rowKey.append(separateur);
      rowKey.append(idBase);
      
      SliceQuery<byte[],String,byte[]> queryDocubase = HFactory.createSliceQuery(keyspaceDocubase, BytesArraySerializer.get(), StringSerializer.get(), BytesArraySerializer.get());
      queryDocubase.setColumnFamily(cfName);
      queryDocubase.setKey(StringSerializer.get().toBytes(rowKey.toString()));
      queryDocubase.setRange(null, null, false, 10); // recupere 10 colonnes
      QueryResult<ColumnSlice<String,byte[]>> resultIndexComposite = queryDocubase
            .execute();
      
      if (resultIndexComposite.get() != null && !resultIndexComposite.get().getColumns().isEmpty()) {
         if (!dryRun) {
            LOGGER.info("Suppression de l'index de reference de l'index {} pour la base {} dans la CF {}", new String[] { nomIndexComposite, idBase, cfName });
            Mutator<byte[]> mutator = HFactory.createMutator(
                  keyspaceDocubase, BytesArraySerializer.get());
            mutator.addDeletion(StringSerializer.get().toBytes(rowKey.toString()), cfName);
            mutator.execute();
         } else {
            LOGGER.debug(
                  "Mode dry run : On devrait supprimer l'index de reference de l'index {} pour la base {} dans la CF {}",
                  new String[] { nomIndexComposite, idBase, cfName });
         }
      }
   }

   private Keyspace getKeyspaceDocubaseFromKeyspace() {
      ConfigurableConsistencyLevel ccl = new ConfigurableConsistencyLevel();
      ccl.setDefaultReadConsistencyLevel(HConsistencyLevel.QUORUM);
      ccl.setDefaultWriteConsistencyLevel(HConsistencyLevel.QUORUM);
      HashMap<String, String> credentials = new HashMap<String, String>();
      credentials.put("username", "root");
      credentials.put("password", "regina4932");
      CassandraHostConfigurator hostConfigurator = new CassandraHostConfigurator(
            cassandraServer.getHosts());
      Cluster cluster = HFactory.getOrCreateCluster("ClusterName-"
            + new Date().getTime(), hostConfigurator);
      FailoverPolicy failoverPolicy;
      failoverPolicy = FailoverPolicy.ON_FAIL_TRY_ALL_AVAILABLE;
      return HFactory.createKeyspace("Docubase", cluster, ccl, failoverPolicy,
            credentials);
   }

}
