/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest;

import java.io.PrintStream;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.PlainTextAuthProvider;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import fr.urssaf.javaDriverTest.dao.BaseDAO;
import fr.urssaf.javaDriverTest.helper.Dumper;

/**
 * TODO (ac75007394) Description du type
 */
public class CleanIndex {
   Cluster cluster;

   Session session;

   PrintStream sysout;

   Dumper dumper;

   @Before
   public void init() throws Exception {
      String servers;
      servers = "cnp69saecas1,cnp69saecas2,cnp69saecas3";
      // servers = "cnp69saecas4.cer69.recouv, cnp69saecas5.cer69.recouv, cnp69saecas6.cer69.recouv";
      // servers = "cnp69gntcas1,cnp69gntcas2,cnp69gntcas3";
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cnp69pregntcas1, cnp69pregntcas2";
      // servers = "cnp69givngntcas1, cnp69givngntcas2";
      // servers = "hwi69gincleasaecas1.cer69.recouv,hwi69gincleasaecas2.cer69.recouv";
      // servers = "cnp69pprodsaecas1,cnp69pprodsaecas2,cnp69pprodsaecas3"; //Préprod
      // servers = "cnp69pprodsaecas6"; //Préprod
      // servers = "cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv,cnp69pregnscas1.cer69.recouv"; // Vrai préprod
      // servers = "10.213.82.56";
      // servers = "cnp6gnscvecas01.cve.recouv,cnp3gnscvecas01.cve.recouv,cnp7gnscvecas01.cve.recouv"; // Charge
      // servers = "cnp3gntcvecas1.cve.recouv,cnp6gntcvecas1.cve.recouv,cnp7gntcvecas1.cve.recouv"; // Charge GNT
      // servers = "cnp69intgntcas1.gidn.recouv,cnp69intgntcas2.gidn.recouv,cnp69intgntcas3.gidn.recouv";
      // servers = "cer69imageint9.cer69.recouv";
      // servers = "cer69imageint10.cer69.recouv";
      // servers = "10.207.81.29";
      // servers = "hwi69givnsaecas1.cer69.recouv,hwi69givnsaecas2.cer69.recouv";
      // servers = "hwi69devsaecas1.cer69.recouv,hwi69devsaecas2.cer69.recouv";
      // servers = "hwi69ginsaecas2.cer69.recouv";
      // servers = "cer69-saeint3";
      // servers = "cnp69devgntcas1.gidn.recouv, cnp69devgntcas2.gidn.recouv";
      // servers = "cnp69dev2gntcas1.gidn.recouv, cnp69dev2gntcas2.gidn.recouv";
      // servers = "cnp69miggntcas1.gidn.recouv,cnp69miggntcas2.gidn.recouv"; // Migration cassandra V2
      // servers = "cnp69dev2gntcas1.gidn.recouv";
      // servers = "cnp69devgntcas1.gidn.recouv,cnp69devgntcas2.gidn.recouv";
      // servers = "hwi69intgnscas1.gidn.recouv,hwi69intgnscas2.gidn.recouv";

      cluster = Cluster.builder()
                       .withClusterName("myCluster")
                       .addContactPoints(StringUtils.split(servers, ","))
                       .withoutJMXReporting()
                       .withAuthProvider(new PlainTextAuthProvider("root", "regina4932"))
                       .build();
      session = cluster.connect();

      sysout = new PrintStream(System.out, true, "UTF-8");
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("d:/temp/out.txt");
      dumper = new Dumper(sysout);
   }

   @After
   public void close() throws Exception {
      cluster.close();
   }

   @Test
   public void cleanIndexTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String index = "SM_MODIFICATION_DATE";
      final int rangeId = 2;
      final BuiltStatement query = QueryBuilder.select("metadata_value", "document_uuid")
                                               .from("dfce", "term_info_range_datetime")
                                               .where(QueryBuilder.eq("index_code", ""))
                                               .and(QueryBuilder.eq("base_uuid", baseId))
                                               .and(QueryBuilder.eq("metadata_name", index))
                                               // .and(QueryBuilder.gt("metadata_value", "20150520110809498"))
                                               .and(QueryBuilder.eq("range_index_id", rangeId));

      final ResultSet rs = session.execute(query);
      int counter = 0;
      int ok = 0;
      int ko = 0;
      for (final Row row : rs) {
         final String metadataValue = row.getString(0);
         final UUID docUUID = row.getUUID(1);
         final String realMetadataValue = getRealMetaValue(index, docUUID);
         if (metadataValue.equals(realMetadataValue)) {
            ok++;
         } else {
            sysout.println(docUUID + " - " + metadataValue + " - " + realMetadataValue);
            ko++;
         }
         counter++;
         if (counter % 1000 == 0) {
            System.out.println("counter=" + counter + " ok=" + ok + " ko=" + ko);
         }
      }

   }

   private String getRealMetaValue(final String index, final UUID docUUID) {
      final BuiltStatement query = QueryBuilder.select("metadata_value")
                                               .from("dfce", "doc_info")
                                               .where(QueryBuilder.eq("document_uuid", docUUID))
                                               .and(QueryBuilder.eq("document_version", "0.0.0"))
                                               .and(QueryBuilder.eq("metadata_name", index));
      final Row row = session.execute(query).one();
      return Dumper.getColAsString(row, row.getColumnDefinitions().iterator().next());
   }

}
