package fr.urssaf.image.dao.astyanax;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import junit.framework.Assert;

import org.cassandraunit.DataLoader;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolType;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.ConsistencyLevel;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

import fr.urssaf.image.dao.ActiviteDao;
import fr.urssaf.image.dao.astyanax.ActiviteAstyanaxDao;
import fr.urssaf.image.model.Activite;

@RunWith(value=BlockJUnit4ClassRunner.class)
public class ActiviteAstyanaxDaoTest {

	@Before
	public void before() throws Exception {

		/* start an embedded cassandra */
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();

		/* load data */
		DataLoader dataLoader = new DataLoader("MonCluster", "localhost:9171");
		dataLoader.load(new ClassPathXmlDataSet("local-dataset-activite.xml"));
	}
	
	private Keyspace getKeySpace() {
      String clusterName = "MonCluster";
      String host = "localhost:9171";
      
      AstyanaxContext<Keyspace> context = new AstyanaxContext.Builder()
       .forCluster(clusterName)
       .forKeyspace("Boutique")
       .withAstyanaxConfiguration(new AstyanaxConfigurationImpl()      
           .setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
           .setConnectionPoolType(ConnectionPoolType.TOKEN_AWARE)
           .setDefaultReadConsistencyLevel(ConsistencyLevel.CL_QUORUM)
           .setDefaultWriteConsistencyLevel(ConsistencyLevel.CL_QUORUM)
       )
       .withConnectionPoolConfiguration(new ConnectionPoolConfigurationImpl("MyConnectionPool")
           .setPort(9171)
           .setMaxConnsPerHost(3)
           .setSeeds(host)
        )
       .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
       .buildKeyspace(ThriftFamilyFactory.getInstance());

      context.start();
      return context.getClient();
   }
	
	@Test
	public void findLimitedByClient() {
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		ActiviteDao dao = new ActiviteAstyanaxDao(getKeySpace());
		
		List<Activite> activites = dao.findLimitedByClient(Long.valueOf(0));
		
		Assert.assertNotNull("Des activites auraient du etre trouve", activites);
		Assert.assertEquals("Le nombre d'activites n'est pas correct", 4, activites.size());
		
		Activite activite = activites.get(0);
		Assert.assertNotNull("L'activite ne devrait pas du etre null", activite);
		Assert.assertEquals("La date de l'activite n'est pas bonne", "01/12/2015 18:26:56", formatter.format(activite.getDate()));
		
		Assert.assertEquals("Le type de l'activite n'est pas bon", "achat", activite.getInfos().get("type"));
		Assert.assertEquals("Le produit lie a l'activite n'est pas bon", "Sac à main", activite.getInfos().get("produit"));
		Assert.assertEquals("Le modele lie a l'activite n'est pas bon", "Exotic Croco", activite.getInfos().get("modele"));
		Assert.assertEquals("La marque liee a l'activite n'est pas bonne", "Lancaster", activite.getInfos().get("marque"));
		Assert.assertEquals("Le prix lie a l'activite n'est pas bon", "275,00", activite.getInfos().get("prix"));
	}
	
	@Test
   public void findByClient() {
      
      GregorianCalendar initDate = new GregorianCalendar(2016, 0, 1, 0, 0, 0);
      HashMap<String, String> infos = new HashMap<String, String>();
      infos.put("type", "achat");
      infos.put("produit", "Sac à main");
      infos.put("modele", "Exotic Croco");
      
      ActiviteDao dao = new ActiviteAstyanaxDao(getKeySpace());
      
      for (int index = 0; index < 2000; index++) {
         
         Activite nouveau = new Activite();
         nouveau.setDate(initDate.getTime());
         nouveau.setInfos(infos);
         
         dao.insert(Long.valueOf(0), nouveau);
         
         initDate.add(GregorianCalendar.MINUTE, 10);
      }
      
      List<Activite> activites = dao.findLimitedByClient(Long.valueOf(0));
      
      Assert.assertNotNull("Des activites auraient du etre trouve", activites);
      Assert.assertEquals("Le nombre d'activites n'est pas correct", 1000, activites.size());
      
      List<Activite> activites2 = dao.findByClient(Long.valueOf(0));
      
      Assert.assertNotNull("Des activites auraient du etre trouve", activites2);
      Assert.assertEquals("Le nombre d'activites n'est pas correct", 2004, activites2.size());
   }
}
