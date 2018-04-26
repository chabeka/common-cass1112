package fr.urssaf.image.cassandra.administration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.factory.HFactory;

import org.cassandraunit.dataset.DataSet;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.cassandraunit.model.ColumnFamilyModel;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.administration.modele.ColumnFamilies;
import fr.urssaf.image.administration.modele.ColumnFamily;
import fr.urssaf.image.administration.modele.DataBaseModel;
import fr.urssaf.image.cassandra.SAEHector;
import fr.urssaf.image.sae.cassandra.dao.JobDAO;
import fr.urssaf.image.sae.cassandra.dao.exception.CassandraEx;

/**
 * Exemple de classe pour lancer la création d'une base de données Cassandra à
 * partir d'un modele de données.
 * La création se fait par l'API Hector.
 * 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-hector.xml" })
public class CreateDataBaseTest {

   /** le répertoire de base */
   private static final String BASE_DIR = "src/main/resources/";

   /** le fichier xml de la base */
   public static final File BASE_XML_FILE = new File(BASE_DIR + "model/"
         + "dataModel.xml");
   @Autowired
   SAEHector saeHector;
   @Autowired
   @Qualifier("jobDAO")
   JobDAO jobDAO;

   private static String clusterName = "TestCluster";
   private static String host = "localhost:9171";
   private DataBaseModel dataModele;

   @BeforeClass
   public static void beforeClass() throws Exception {
      EmbeddedCassandraServerHelper.startEmbeddedCassandra();
   }

   @Test
   public void createCassandraSchema() throws UnsupportedEncodingException,
         FileNotFoundException, CassandraEx {
      String newKeySpace = saeHector
            .createCassandraSchema(mappingToDataModele(getDataSet()));
      Assert.assertEquals(newKeySpace.trim(), getDataSet().getKeyspace()
            .getName().trim());
   }

   private DataBaseModel mappingToDataModele(DataSet dataSet) {
      DataBaseModel dataModel = new DataBaseModel();

      Cluster cluster = HFactory.getOrCreateCluster(clusterName, host);
      dataModel.setCluster(cluster);
      dataModel.setKeyspace(dataSet.getKeyspace().getName());
      dataModel.setReplicationFactor(dataSet.getKeyspace()
            .getReplicationFactor());
      dataModel.setStrategy(dataSet.getKeyspace().getStrategy().value());
      ColumnFamilies dataColumnFamilies = new ColumnFamilies();
      List<ColumnFamily> listColumnFamily = new ArrayList<ColumnFamily>();
      List<ColumnFamilyModel> columnFamiliesFromDataSet = dataSet.getKeyspace()
            .getColumnFamilies();
      for (ColumnFamilyModel columnFamilyModel : columnFamiliesFromDataSet) {
         ColumnFamily columnFamily = new ColumnFamily();
         columnFamily.setComparatorType(columnFamilyModel
               .getComparatorTypeAlias());
         columnFamily.setName(columnFamilyModel.getName());
         listColumnFamily.add(columnFamily);
      }
      dataColumnFamilies.setColumnFamily(listColumnFamily);
      dataModel.setColumnFamilies(dataColumnFamilies);
      return dataModel;
   }

   /**
    * @return the saeHector
    */
   public SAEHector getSaeHector() {
      return saeHector;
   }

   /**
    * @param saeHector
    *           the saeHector to set
    */
   public void setSaeHector(SAEHector saeHector) {
      this.saeHector = saeHector;
   }

   public DataSet getDataSet() {
      return new ClassPathXmlDataSet("xml/dataModelDataset.xml");
   }

}
