package fr.urssaf.image.dao.hector;

import java.util.List;

import junit.framework.Assert;
import me.prettyprint.cassandra.model.QuorumAllConsistencyLevelPolicy;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import org.cassandraunit.DataLoader;
import org.cassandraunit.dataset.xml.ClassPathXmlDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import fr.urssaf.image.dao.ClientDao;
import fr.urssaf.image.model.Client;

@RunWith(value=BlockJUnit4ClassRunner.class)
public class ClientHectorDaoTest {

	@Before
	public void before() throws Exception {

		/* start an embedded cassandra */
		EmbeddedCassandraServerHelper.startEmbeddedCassandra();

		/* load data */
		DataLoader dataLoader = new DataLoader("MonCluster", "localhost:9171");
		dataLoader.load(new ClassPathXmlDataSet("local-dataset-client.xml"));
	}

	private Keyspace getKeySpace() {
		String clusterName = "MonCluster";
		String host = "localhost:9171";
		Cluster cluster = HFactory.getOrCreateCluster(clusterName, host);
		Keyspace keyspace = HFactory.createKeyspace("Boutique", cluster, new QuorumAllConsistencyLevelPolicy());
		return keyspace;
	}

	@Test
	public void findById() {

		ClientDao dao = new ClientHectorDao(getKeySpace());
		
		Client client = dao.findById(Long.valueOf(0));
		
		Assert.assertNotNull("Le client aurait du etre trouve", client);
		Assert.assertEquals("L'identifiant du client n'est pas correct", 0, client.getId().longValue());
		Assert.assertEquals("Le nom du client n'est pas correct", "Chetai", client.getNom());
		Assert.assertEquals("Le prenom du client n'est pas correct", "Emma", client.getPrenom());
		Assert.assertEquals("L'email du client n'est pas correct", "emma.chetai@gmail.com", client.getEmail());
		Assert.assertEquals("Le numero de carte fidelite du client n'est pas correct", "123456", client.getNumeroCarteFidelite());
	}
	
	@Test
	public void findAllLimited() {

		ClientDao dao = new ClientHectorDao(getKeySpace());
		
		List<Client> clients = dao.findAllLimited(false);
		
		Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients);
		Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients.isEmpty());
		Assert.assertEquals("La liste des clients aurait du contenir 2  éléments", 2, clients.size());
	}
	
	@Test
	public void insert() {
		
		Client nouveau = new Client();
		nouveau.setId(Long.valueOf(9));
		nouveau.setNom("Feet");
		nouveau.setPrenom("Neo");
		nouveau.setEmail("neo.feet@gmail.com");
		
		ClientDao dao = new ClientHectorDao(getKeySpace());
		
		List<Client> clients = dao.findAll(false);
		Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients);
		Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients.isEmpty());
		Assert.assertEquals("La liste des clients aurait du contenir 2 elements", 2, clients.size());
		
		dao.insert(nouveau);
		
		List<Client> clients2 = dao.findAll(false);
		Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients2);
		Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients2.isEmpty());
		Assert.assertEquals("La liste des clients aurait du contenir 3 elements", 3, clients2.size());
	}
	
	@Test
	public void update() {
		
		ClientDao dao = new ClientHectorDao(getKeySpace());
		
		Client client = dao.findById(Long.valueOf(0));
		
		Assert.assertNotNull("Le client aurait du etre trouve", client);
		Assert.assertEquals("L'identifiant du client n'est pas correct", 0, client.getId().longValue());
		Assert.assertEquals("Le nom du client n'est pas correct", "Chetai", client.getNom());
		Assert.assertEquals("Le prenom du client n'est pas correct", "Emma", client.getPrenom());
		Assert.assertEquals("L'email du client n'est pas correct", "emma.chetai@gmail.com", client.getEmail());
		Assert.assertEquals("Le numero de carte fidelite du client n'est pas correct", "123456", client.getNumeroCarteFidelite());
		
		// on change le num�ro de carte fidelite
		client.setNumeroCarteFidelite("987654321");
		
		dao.update(client);
		
		Client client2 = dao.findById(Long.valueOf(0));
		
		Assert.assertNotNull("Le client aurait du etre trouve", client2);
		Assert.assertEquals("L'identifiant du client n'est pas correct", 0, client2.getId().longValue());
		Assert.assertEquals("Le nom du client n'est pas correct", "Chetai", client2.getNom());
		Assert.assertEquals("Le prenom du client n'est pas correct", "Emma", client2.getPrenom());
		Assert.assertEquals("L'email du client n'est pas correct", "emma.chetai@gmail.com", client2.getEmail());
		Assert.assertEquals("Le numero de carte fidelite du client n'est pas correct", "987654321", client2.getNumeroCarteFidelite());
	}
	
	@Test
	public void delete() {
		
		Client nouveau = new Client();
		nouveau.setId(Long.valueOf(9));
		nouveau.setNom("Feet");
		nouveau.setPrenom("Neo");
		nouveau.setEmail("neo.feet@gmail.com");
		
		ClientDao dao = new ClientHectorDao(getKeySpace());
		
		List<Client> clients = dao.findAll(true);
		Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients);
		Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients.isEmpty());
		Assert.assertEquals("La liste des clients aurait du contenir 2 elements", 2, clients.size());
		
		dao.insert(nouveau);
		
		List<Client> clients2 = dao.findAll(true);
		Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients2);
		Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients2.isEmpty());
		Assert.assertEquals("La liste des clients aurait du contenir 3 elements", 3, clients2.size());
		
		dao.delete(nouveau);
		
		List<Client> clients3 = dao.findAll(false);
		Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients3);
		Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients3.isEmpty());
		Assert.assertEquals("La liste des clients aurait du contenir 2 elements", 2, clients3.size());
		
		List<Client> clientsWithEmptyRow = dao.findAll(true);
		Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clientsWithEmptyRow);
		Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clientsWithEmptyRow.isEmpty());
		Assert.assertEquals("La liste des clients aurait du contenir 3 elements", 3, clientsWithEmptyRow.size());
	}
	
	@Test
	public void findAll() {
	   
	   ClientDao dao = new ClientHectorDao(getKeySpace());
	   
	   // insert 2000 clients
	   for (int index = 0; index < 2000; index++) {
	      
   	   Client nouveau = new Client();
         nouveau.setId(Long.valueOf(index + 10));
         nouveau.setNom("Feet");
         nouveau.setPrenom("Neo");
         nouveau.setEmail("neo.feet@gmail.com");
         
         dao.insert(nouveau);
	   }
	   
	   List<Client> clients = dao.findAllLimited(false);
	   Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients);
      Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients.isEmpty());
      Assert.assertEquals("La liste des clients aurait du contenir 1000 elements", 1000, clients.size());
	   
      List<Client> clients2 = dao.findAll(false);
      Assert.assertNotNull("La liste des clients n'aurait pas du etre null", clients2);
      Assert.assertFalse("La liste des clients n'aurait pas du etre vide", clients2.isEmpty());
      Assert.assertEquals("La liste des clients aurait du contenir 2002 elements", 2002, clients2.size());
	}
}
