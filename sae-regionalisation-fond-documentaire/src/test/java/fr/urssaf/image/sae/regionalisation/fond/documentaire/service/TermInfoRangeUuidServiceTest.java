/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.netflix.astyanax.Serializer;
import com.netflix.astyanax.connectionpool.OperationResult;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.common.Constants;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.TermInfoRangeUuidDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidColumn;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.cf.TermInfoRangeUuidKey;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.CassandraException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
      "/applicationContext-sae-regionalisation-cassandra-test.xml",
      "/applicationContext-sae-regionalisation-dao-mock-test.xml" })
public class TermInfoRangeUuidServiceTest {

   @Autowired
   private TermInfoRangeUuidService service;

   @Autowired
   private TermInfoRangeUuidDao termInfoRangeUuidDao;

   /* mocks */
   private Column<TermInfoRangeUuidColumn> column;
   private Iterator<Column<TermInfoRangeUuidColumn>> iterator;
   private ColumnList<TermInfoRangeUuidColumn> columnList;
   private OperationResult<ColumnList<TermInfoRangeUuidColumn>> operationResult;
   private RowQuery<TermInfoRangeUuidKey, TermInfoRangeUuidColumn> rowQuery;

   /* liste des variables utilisées */
   private List<TermInfoRangeUuidColumn> uuidColumnsList;
   private List<UUID> uuidsList;
   private List<Map<String, List<String>>> mapsList;

   @After
   public void end() {
      // replay
      EasyMock.reset(column, iterator, columnList, operationResult, rowQuery,
            termInfoRangeUuidDao);
   }

   @Before
   public void init() {
      uuidsList = Arrays.asList(UUID.randomUUID(), UUID.randomUUID(), UUID
            .randomUUID());

      mapsList = new ArrayList<Map<String, List<String>>>();
      Map<String, List<String>> map = new HashMap<String, List<String>>();
      map.put(Constants.CODE_ORG_GEST, Arrays.asList("UR123"));
      map.put(Constants.CODE_ORG_PROP, Arrays.asList("UR123"));
      mapsList.add(map);
      map = new HashMap<String, List<String>>();
      map.put(Constants.CODE_ORG_GEST, Arrays.asList("UR124"));
      map.put(Constants.CODE_ORG_PROP, Arrays.asList("UR125"));
      mapsList.add(map);
      map = new HashMap<String, List<String>>();
      map.put(Constants.CODE_ORG_GEST, Arrays.asList("UR125"));
      map.put(Constants.CODE_ORG_PROP, Arrays.asList("UR123"));
      mapsList.add(map);

      uuidColumnsList = new ArrayList<TermInfoRangeUuidColumn>();
      TermInfoRangeUuidColumn column;
      for (int i = 0; i < uuidsList.size(); i++) {
         column = new TermInfoRangeUuidColumn();
         column.setDocumentUUID(uuidsList.get(i));
         column.setCategoryValue("cat_" + i);
         column.setDocumentVersion("version_" + i);
         uuidColumnsList.add(column);
      }

   }

   @Test
   public void testGetInfosDoc() throws ConnectionException, CassandraException {

      initMock();

      List<Map<String, String>> codes = service.getInfosDoc();

      // verify
      EasyMock.verify(column, iterator, columnList, operationResult, rowQuery,
            termInfoRangeUuidDao);

      Assert.assertEquals(
            "le nombre d'enregistrements retourné doit être correct", 3, codes
                  .size());

      Map<String, String> values;
      for (int i = 0; i < codes.size(); i++) {
         values = codes.get(i);
         Assert.assertEquals("l'UUID doit être correct", uuidsList.get(i)
               .toString(), values.get(Constants.UUID));
         Assert.assertEquals("le cog doit être correct", mapsList.get(i).get(
               Constants.CODE_ORG_GEST).get(0), values
               .get(Constants.CODE_ORG_GEST));
         Assert.assertEquals("le cop doit être correct", mapsList.get(i).get(
               Constants.CODE_ORG_PROP).get(0), values
               .get(Constants.CODE_ORG_PROP));
      }

   }

   @SuppressWarnings("unchecked")
   private void initMock() throws ConnectionException {

      // initialisation du comportement de colummn
      column = EasyMock.createMock(Column.class);
      EasyMock.expect(column.getName()).andReturn(uuidColumnsList.get(0))
            .andReturn(uuidColumnsList.get(1))
            .andReturn(uuidColumnsList.get(2));
      EasyMock.expect(column.getValue(EasyMock.anyObject(Serializer.class)))
            .andReturn(mapsList.get(0)).andReturn(mapsList.get(1)).andReturn(
                  mapsList.get(2));

      // initialisation du comportement de iterator
      iterator = EasyMock.createMock(Iterator.class);
      EasyMock.expect(iterator.hasNext()).andReturn(true).times(3).andReturn(
            false).once();
      EasyMock.expect(iterator.next()).andReturn(column).times(3);

      // initialisation du comportement de ColumnList
      columnList = EasyMock.createMock(ColumnList.class);
      EasyMock.expect(columnList.iterator()).andReturn(iterator).once();

      // initialisation du comportement de OperationResult
      operationResult = EasyMock.createMock(OperationResult.class);
      EasyMock.expect(operationResult.getResult()).andReturn(columnList).once();

      // initialisation du comportement de rowQuery
      rowQuery = EasyMock.createMock(RowQuery.class);
      EasyMock.expect(rowQuery.execute()).andReturn(operationResult).once();

      // initialisation du comportement de la DAO
      EasyMock.expect(termInfoRangeUuidDao.getAllUuidColumns()).andReturn(
            rowQuery).once();

      // replay
      EasyMock.replay(column, iterator, columnList, operationResult, rowQuery,
            termInfoRangeUuidDao);

   }
}
