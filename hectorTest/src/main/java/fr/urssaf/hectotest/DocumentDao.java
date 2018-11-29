package fr.urssaf.hectotest;

import java.io.File;
import java.io.FileOutputStream;

import me.prettyprint.cassandra.serializers.BytesArraySerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.SliceQuery;

public class DocumentDao {

   /***
    * Extrait le corps du document dont le file-uuid est passé en
    * paramètre
    * 
    * @param fileUuid
    *           : uuid du fichier à extraire
    * @param fileName
    *           : fichier à créer
    * @throws Exception
    */
   public static void ExtractOneDocumentFromFileUUID(final Keyspace keyspace,
                                                     final String fileUuid, final String fileName)
         throws Exception {
      final String key = fileUuid.toLowerCase();

      final StringSerializer stringSerializer = StringSerializer.get();
      final BytesArraySerializer bytesSerializer = BytesArraySerializer.get();

      final SliceQuery<String, String, byte[]> sliceQuery = HFactory
                                                                    .createSliceQuery(keyspace,
                                                                                      stringSerializer,
                                                                                      stringSerializer,
                                                                                      bytesSerializer);
      sliceQuery.setColumnFamily("documents");
      sliceQuery.setKey(key);
      ColumnSliceIterator<String, String, byte[]> sliceIterator;
      sliceIterator = new ColumnSliceIterator<String, String, byte[]>(
                                                                      sliceQuery,
                                                                      "chunk_0",
                                                                      "chunk_9",
                                                                      false);

      // sysout.println("Création du fichier " + fileName + "...");
      final File someFile = new File(fileName);
      final FileOutputStream fos = new FileOutputStream(someFile);

      while (sliceIterator.hasNext()) {
         final HColumn<String, byte[]> col = sliceIterator
                                                          .next();
         fos.write(col.getValue());
      }
      fos.flush();
      fos.close();
   }
}
