/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.astyanax.Serializer;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.model.Rows;
import com.netflix.astyanax.query.AllRowsQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocInfoDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.DocInfoKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-cassandra-test.xml",
      "/applicationContext-sae-regionalisation-dao-mock-test.xml" })
public class DocInfoServiceTest {

   @Autowired
   private DocInfoService service;

   @Autowired
   private DocInfoDao docInfoDao;

   private AllRowsQuery<DocInfoKey, String> query;
   private ColumnList<String> columnList;
   private Row<DocInfoKey, String> row;
   private Iterator<Row<DocInfoKey, String>> iterator;
   private Rows<DocInfoKey, String> rows;
   private OperationResult<Rows<DocInfoKey, String>> operation;

   @After
   public void end() {
      EasyMock.reset(docInfoDao, query, columnList, row, iterator, rows,
            operation);
   }

   @Test
   public void testGetOrganisme() throws ConnectionException,
         CassandraException {
      initMock();

      List<String> liste = service.getCodesOrganismes();

      EasyMock.verify(docInfoDao, query, columnList, row, iterator, rows,
            operation);

      List<String> attendus = Arrays.asList("UR123", "UR124", "UR125", "UR126");

      Assert.assertEquals("il est attendu 4 éléments", 4, liste.size());

      for (String attendu : attendus) {
         Assert.assertTrue("l'élément " + attendu
               + " doit etre présent dans la liste", liste.contains(attendu));
      }

   }

   @SuppressWarnings("unchecked")
   private void initMock() throws ConnectionException {

      columnList = EasyMock.createMock(ColumnList.class);
      EasyMock.expect(columnList.getColumnNames()).andReturn(
            Arrays.asList("cop", "cog")).times(3);

      EasyMock.expect(
            columnList.getValue(EasyMock.anyObject(String.class), EasyMock
                  .anyObject(Serializer.class), EasyMock
                  .anyObject(String.class))).andReturn("UR123").andReturn(
            "UR123").andReturn("UR124").andReturn("UR125").andReturn("UR123")
            .andReturn("UR126");

      row = EasyMock.createMock(Row.class);
      EasyMock.expect(row.getColumns()).andReturn(columnList).times(9);

      iterator = EasyMock.createMock(Iterator.class);
      EasyMock.expect(iterator.hasNext()).andReturn(true).times(3).andReturn(
            false).times(1);
      EasyMock.expect(iterator.next()).andReturn(row).times(3);

      rows = EasyMock.createMock(Rows.class);
      EasyMock.expect(rows.iterator()).andReturn(iterator).once();

      operation = EasyMock.createMock(OperationResult.class);

      EasyMock.expect(operation.getResult()).andReturn(rows);
      // initialisation de la query
      query = EasyMock.createMock(AllRowsQuery.class);
      EasyMock.expect(query.execute()).andReturn(operation);

      EasyMock.expect(docInfoDao.getQuery()).andReturn(query);

      EasyMock.replay(columnList, row, iterator, rows, operation, query,
            docInfoDao);

   }
}
