/**
 *  TODO (ac75007394) Description du fichier
 */
package fr.urssaf.javaDriverTest;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

import java.io.PrintStream;
import java.math.BigInteger;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.select.Select;

import fr.urssaf.javaDriverTest.dao.BaseDAO;
import fr.urssaf.javaDriverTest.dao.CassandraSessionFactory;
import fr.urssaf.javaDriverTest.dao.IndexReference;
import fr.urssaf.javaDriverTest.helper.Dumper;

/**
 * TODO (ac75007394) Description du type
 */
public class CleanIndex {

   CqlSession session;

   PrintStream sysout;

   Dumper dumper;

   @Before
   public void init() throws Exception {
      String servers;
      // servers = "cnp69saecas1,cnp69saecas2,cnp69saecas3";
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
      servers = "cnp6gntcvecas1.cve.recouv,cnp6gntcvecas2.cve.recouv"; // Charge GNT
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

      final String cassandraLocalDC = "DC6";
      session = CassandraSessionFactory.getSession(servers, "root", "regina4932", cassandraLocalDC);
      sysout = new PrintStream(System.out, true, "UTF-8");
      // Pour dumper sur un fichier plutôt que sur la sortie standard
      sysout = new PrintStream("d:/temp/out.txt");
      sysout = System.out;
      dumper = new Dumper(sysout);
   }

   @After
   public void close() throws Exception {
      session.close();
   }

   @Test
   public void cleanIndexTest() throws Exception {
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String index = "SM_MODIFICATION_DATE";
      final int rangeId = 2;
      final Select query = QueryBuilder.selectFrom("dfce", "term_info_range_datetime")
                                       .columns("metadata_value", "document_uuid")
                                       .whereColumn("index_code")
                                       .isEqualTo(literal(""))
                                       .whereColumn("base_uuid")
                                       .isEqualTo(literal(baseId))
                                       .whereColumn("metadata_name")
                                       .isEqualTo(literal(index))
                                       .whereColumn("range_index_id")
                                       .isEqualTo(literal(rangeId));
      // .and(QueryBuilder.gt("metadata_value", "20150520110809498"))

      final ResultSet rs = session.execute(query.build());
      int counter = 0;
      int ok = 0;
      int ko = 0;
      for (final Row row : rs) {
         final String metadataValue = row.getString(0);
         final UUID docUUID = row.getUuid(1);
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
      final Select query = QueryBuilder.selectFrom("dfce", "doc_info")
                                       .columns("metadata_value")
                                       .whereColumn("document_uuid")
                                       .isEqualTo(literal(docUUID))
                                       .whereColumn("document_version")
                                       .isEqualTo(literal("0.0.0"))
                                       .whereColumn("metadata_name")
                                       .isEqualTo(literal(index));
      final Row row = session.execute(query.build()).one();
      return Dumper.getColAsString(row, row.getColumnDefinitions().iterator().next());
   }

   @Test
   public void removeOneIndexEntryTest() throws Exception {
      final String indexCode = "";
      final String metaName = "SM_ARCHIVAGE_DATE";
      final UUID baseId = BaseDAO.getBaseUUID(session);
      final String metaValue = "20171103143909609";
      final UUID docUUID = UUID.fromString("b3d7221e-df2f-425b-94a6-45b24f7de6cb");
      final String docVersion = "0.0.0";
      final IndexReference indexReference = new IndexReference();
      indexReference.readIndexReference(session, baseId, metaName, "NOMINAL");
      final int rangeId = indexReference.metaToRangeId(metaValue);
      final String baseName = BaseDAO.getBaseName(session);
      final String indexTable = BaseDAO.getIndexTable(session, baseName, metaName);

      // Affichage avant suppression
      System.out.println("RangeId=" + rangeId);
      System.out.println("baseId=" + baseId);
      System.out.println("indexTable=" + indexTable);
      final Select selectQuery = QueryBuilder.selectFrom("dfce", indexTable)
                                             // .columns("metadata_value", "document_uuid")
                                             .all()
                                             .whereColumn("index_code")
                                             .isEqualTo(literal(indexCode))
                                             .whereColumn("metadata_name")
                                             .isEqualTo(literal(metaName))
                                             .whereColumn("base_uuid")
                                             .isEqualTo(literal(baseId))
                                             .whereColumn("range_index_id")
                                             .isEqualTo(literal(rangeId))
                                             .whereColumn("metadata_value")
                                             .isEqualTo(literal(metaValue))
                                             .whereColumn("document_uuid")
                                             .isEqualTo(literal(docUUID))
                                             .whereColumn("document_version")
                                             .isEqualTo(literal(docVersion))
                                             .limit(100);
      final ResultSet rs = session.execute(selectQuery.build());
      dumper.dumpRows(rs);

      final boolean doDelete = true;
      if (doDelete) {
         final SimpleStatement deleteQuery = SimpleStatement.newInstance("DELETE FROM dfce." + indexTable
               + " WHERE index_code=? AND metadata_name=? AND base_uuid=? AND range_index_id=?"
               + " AND metadata_value= ? AND document_uuid= ? AND document_version=?",
                                                                         indexCode,
                                                                         metaName,
                                                                         baseId,
                                                                         BigInteger.valueOf(rangeId),
                                                                         metaValue,
                                                                         docUUID,
                                                                         docVersion);
         final ResultSet result = session.execute(deleteQuery);
         System.out.println("Errors=" + result.getExecutionInfo().getErrors().toString());
         System.out.println("fini");
      }
   }
}
