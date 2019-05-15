/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest;

import java.io.File;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.cassandra.config.CFMetaData;
import org.apache.cassandra.db.Cell;
import org.apache.cassandra.db.CounterCell;
import org.apache.cassandra.db.DecoratedKey;
import org.apache.cassandra.db.DeletedCell;
import org.apache.cassandra.db.ExpiringCell;
import org.apache.cassandra.db.OnDiskAtom;
import org.apache.cassandra.db.columniterator.OnDiskAtomIterator;
import org.apache.cassandra.db.composites.CellName;
import org.apache.cassandra.db.marshal.AbstractCompositeType.CompositeComponent;
import org.apache.cassandra.db.marshal.AbstractType;
import org.apache.cassandra.db.marshal.CompositeType;
import org.apache.cassandra.db.marshal.LongType;
import org.apache.cassandra.db.marshal.UTF8Type;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.RandomPartitioner;
import org.apache.cassandra.io.sstable.CQLSSTableWriter;
import org.apache.cassandra.io.sstable.Component;
import org.apache.cassandra.io.sstable.Descriptor;
import org.apache.cassandra.io.sstable.Descriptor.Type;
import org.apache.cassandra.io.sstable.ISSTableScanner;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * Classe permettant de créer une sstable à partir d'une sstable source, et en filtrant les données.
 * Sert pour nettoyer le contenu de term_info_range_datetime corrompu par des splits en échec
 */
public class CleanSSTables {

   final String schema = "CREATE TABLE dfce.term_info_range_datetime (\r\n" +
         "    index_code text,\r\n" +
         "    metadata_name text,\r\n" +
         "    base_uuid uuid,\r\n" +
         "    range_index_id varint,\r\n" +
         "    metadata_value text,\r\n" +
         "    document_uuid uuid,\r\n" +
         "    document_version text,\r\n" +
         "    serialized_document blob,\r\n" +
         "    PRIMARY KEY ((index_code, metadata_name, base_uuid, range_index_id), metadata_value, document_uuid, document_version)\r\n" +
         ") WITH COMPACT STORAGE\r\n" +
         "    AND CLUSTERING ORDER BY (metadata_value ASC, document_uuid ASC, document_version ASC)\r\n" +
         "    AND bloom_filter_fp_chance = 0.1\r\n" +
         "    AND caching = '{\"keys\":\"ALL\", \"rows_per_partition\":\"NONE\"}'\r\n" +
         "    AND comment = ''\r\n" +
         "    AND compaction = {'sstable_size_in_mb': '200', 'class': 'org.apache.cassandra.db.compaction.LeveledCompactionStrategy'}\r\n" +
         "    AND compression = {'sstable_compression': 'org.apache.cassandra.io.compress.SnappyCompressor'}\r\n" +
         "    AND dclocal_read_repair_chance = 0.1\r\n" +
         "    AND default_time_to_live = 0\r\n" +
         "    AND gc_grace_seconds = 1728000\r\n" +
         "    AND max_index_interval = 2048\r\n" +
         "    AND memtable_flush_period_in_ms = 0\r\n" +
         "    AND min_index_interval = 128\r\n" +
         "    AND read_repair_chance = 0.0\r\n" +
         "    AND speculative_retry = '99.0PERCENTILE';";

   @Test
   public void createTest() throws Exception {
      final String schema = "CREATE TABLE myKs.myTable ("
            + "  k int PRIMARY KEY,"
            + "  v1 text,"
            + "  v2 int"
            + ")";
      final String insert = "INSERT INTO myKs.myTable (k, v1, v2) VALUES (?, ?, ?)";
      final String path = "D:\\temp\\SSTablesOut";
      // Creates a new writer. You need to provide at least the directory where to write the created sstable,
      // the schema for the sstable to write and a (prepared) insert statement to use. If you do not use the
      // default partitioner (Murmur3Partitioner), you will also need to provide the partitioner in use, see
      // CQLSSTableWriter.Builder for more details on the available options.
      final CQLSSTableWriter writer = CQLSSTableWriter.builder()
                                                      .inDirectory(path)
                                                      .withPartitioner(new RandomPartitioner())
                                                      .forTable(schema)
                                                      .using(insert)
                                                      .build();
      // Adds a number of rows to the resulting sstable
      writer.addRow(0, "test1", 24);
      writer.addRow(1, "test2", null);
      writer.addRow(2, "test3", 42);

      // Close the writer, finalizing the sstable
      writer.close();

   }

   @Test
   public void createTest2() throws Exception {

      final CQLSSTableWriter writer = getWriter();

      // Adds a number of rows to the resulting sstable
      writer.addRow("",
                    "SM_ARCHIVAGE_DATE",
                    UUID.fromString("f573ae93-ac6a-4615-a23b-150fd621b5a0"),
                    BigInteger.valueOf(20),
                    "20140404195132984",
                    UUID.fromString("fe3947c2-9d67-48d9-8cbb-afe987297917"),
                    "0.0.0",
                    ByteBuffer.wrap(new byte[100]));

      // Close the writer, finalizing the sstable
      writer.close();

   }

   private CQLSSTableWriter getWriter() {
      final String insert = "INSERT INTO dfce.term_info_range_datetime (index_code, metadata_name, base_uuid, range_index_id, metadata_value, document_uuid, document_version, serialized_document) VALUES (?, ?, ?, ?, ?, ?, ?, ?) using TIMESTAMP ?";
      final String path = "D:\\temp\\SSTablesOut";
      final File directory = new File(path);
      if (!directory.exists()) {
         directory.mkdir();
      }

      // Creates a new writer. You need to provide at least the directory where to write the created sstable,
      // the schema for the sstable to write and a (prepared) insert statement to use. If you do not use the
      // default partitioner (Murmur3Partitioner), you will also need to provide the partitioner in use, see
      // CQLSSTableWriter.Builder for more details on the available options.
      final CQLSSTableWriter writer = CQLSSTableWriter.builder()
                                                      .inDirectory(path)
                                                      .withPartitioner(new RandomPartitioner())
                                                      .forTable(schema)
                                                      .withBufferSizeInMB(2500)
                                                      .using(insert)
                                                      .build();
      return writer;
   }

   @Test
   /**
    * Ce script permet de lire une SSTable de type term_info_range_datetime, de filtrer son contenu,
    * et d'écrire une nouvelle SSTable avec le contenu filtré.
    * Permet de supprimer les données sans créer de tombstones.
    * 
    * @throws Exception
    */
   public void filterSSTableTest() throws Exception {
      System.setProperty("cassandra.config", "file:///D:/temp/SSTables/cassandra.yaml");
      final String path = "D:\\temp\\SSTables";
      final Descriptor descriptor = new Descriptor("ka", new File(path), "dfce", "term_info_range_datetime", 1239, Type.FINAL);

      final Set<Component> components = new HashSet<Component>();
      components.add(new Component(Component.Type.DATA));
      components.add(new Component(Component.Type.COMPRESSION_INFO));
      components.add(new Component(Component.Type.FILTER));
      components.add(new Component(Component.Type.PRIMARY_INDEX));
      // components.add(new Component(Component.Type.STATS));
      components.add(new Component(Component.Type.SUMMARY));
      // components.add(new Component(Component.Type.TOC));
      // components.add(new Component(Component.Type.DIGEST));

      final CFMetaData metadata = CFMetaData.compile(schema, "dfce");
      final IPartitioner partitioner = new RandomPartitioner();
      final SSTableReader inputSStable = SSTableReader.open(descriptor, components, metadata, partitioner, false, false);
      final CQLSSTableWriter writer = getWriter();

      final ISSTableScanner currentScanner = inputSStable.getScanner();
      while (currentScanner.hasNext()) {
         final OnDiskAtomIterator row = currentScanner.next();
         final DecoratedKey key = row.getKey();
         final String keyAsString = metadata.getKeyValidator().getString(key.getKey());
         final List<AbstractType<?>> keyTypes = metadata.getKeyValidator().getComponents();
         final List<CompositeComponent> keyElements = CompositeType.getInstance(keyTypes).deconstruct(key.getKey());

         final ByteBuffer indexCode = keyElements.get(0).value;
         final String indexCodeAsString = (String) keyElements.get(0).comparator.getSerializer().deserialize(indexCode);

         final ByteBuffer metaName = keyElements.get(1).value;
         final String metaNameAsString = (String) keyElements.get(1).comparator.getSerializer().deserialize(metaName);

         final ByteBuffer baseUUID = keyElements.get(2).value;
         final UUID baseUUIDAsUUID = (UUID) keyElements.get(2).comparator.getSerializer().deserialize(baseUUID);

         final ByteBuffer rangeId = keyElements.get(3).value;
         final BigInteger rangeIdAsBigInt = (BigInteger) keyElements.get(3).comparator.getSerializer().deserialize(rangeId);

         System.out.println("keyAsString=" + keyAsString);
         System.out.println("indexCodeAsString=" + indexCodeAsString);
         System.out.println("metaNameAsString=" + metaNameAsString);
         System.out.println("baseUUIDAsUUID=" + baseUUIDAsUUID);
         System.out.println("rangeIdAsInt=" + rangeIdAsBigInt);

         int counter = 0;
         int ignoreCounter = 0;
         while (row.hasNext()) {
            final OnDiskAtom atom = row.next();
            if (atom instanceof Cell) {
               final Cell cell = (Cell) atom;

               if (cell instanceof DeletedCell || cell instanceof ExpiringCell || cell instanceof CounterCell) {
                  throw new Exception("TODO");
               }
               final long cellTimestamp = cell.timestamp();
               final ByteBuffer cellTimestampAsBuffer = LongType.instance.decompose(cellTimestamp);

               final CellName cellName = cell.name();

               final ByteBuffer metadataValue = cellName.get(0);
               final String metadataValueAsString = UTF8Type.instance.getString(metadataValue);
               // System.out.println("metadataValueAsString=" + metadataValueAsString);

               final ByteBuffer documentUUID = cellName.get(1);
               // final UUID documentUUIDAsUUID = UUIDType.instance.getSerializer().deserialize(documentUUID);
               // System.out.println("documentUUIDAsUUID=" + documentUUIDAsUUID);

               final ByteBuffer documentVersion = cellName.get(2);
               // final String documentVersionAsString = UTF8Type.instance.getString(documentVersion);
               // System.out.println("documentVersionAsString=" + documentVersionAsString);

               final ByteBuffer serializedDocument = cell.value();
               // final ByteArrayInputStream bis = new ByteArrayInputStream(serializedDocument.array());
               // final ObjectInputStream ois = new ObjectInputStream(bis);
               // final Object o = ois.readObject();
               // System.out.println("o=" + o);

               boolean shouldIgnore = false;
               /*
               if (":SM_CREATION_DATE:f573ae93-ac6a-4615-a23b-150fd621b5a0:128".equals(keyAsString)) {
                  if (StringUtils.compare(metadataValueAsString, "20171228000000000") < 0 ||
                        StringUtils.compare(metadataValueAsString, "20180105000000000") >= 0) {
                     // On ignore cette colonne
                     ignoreCounter++;
                     shouldIgnore = true;
                  }
               }
               */
               /*
               if (":SM_LIFE_CYCLE_REFERENCE_DATE:f573ae93-ac6a-4615-a23b-150fd621b5a0:97".equals(keyAsString)) {
                  if (StringUtils.compare(metadataValueAsString, "20120131000000000") >= 0) {
                     // System.out.println("ignoré=" + metadataValueAsString);
                     // On ignore cette colonne
                     ignoreCounter++;
                     shouldIgnore = true;
                  }
               }
               */

               if (":SM_LIFE_CYCLE_REFERENCE_DATE:f573ae93-ac6a-4615-a23b-150fd621b5a0:99".equals(keyAsString)) {
                  if (StringUtils.compare(metadataValueAsString, "20131110044847402") < 0 ||
                        StringUtils.compare(metadataValueAsString, "20131214060939110") >= 0) {
                     // On ignore cette colonne
                     ignoreCounter++;
                     shouldIgnore = true;
                  }
               }

               if (!shouldIgnore) {
                  writer.rawAddRow(indexCode,
                                   metaName,
                                   baseUUID,
                                   rangeId,
                                   metadataValue,
                                   documentUUID,
                                   documentVersion,
                                   serializedDocument,
                                   cellTimestampAsBuffer);
               }

               counter++;
               if (counter % 100000 == 0) {
                  System.out.println("metadataValueAsString=" + metadataValueAsString);
                  System.out.println("counter=" + counter);
               }
            }
         }
         System.out.println("Nombre de colonnes : " + counter);
         System.out.println("Nombre de colonnes ignorées : " + ignoreCounter);
      }
      writer.close();
   }

}
