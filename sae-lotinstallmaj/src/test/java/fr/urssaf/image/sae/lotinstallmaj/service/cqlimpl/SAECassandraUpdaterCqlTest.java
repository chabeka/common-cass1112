package fr.urssaf.image.sae.lotinstallmaj.service.cqlimpl;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.lotinstallmaj.cql.UtilsColunmFalmilly;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.SAECassandraUpdaterCQL;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.SAEKeyspaceConnecter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-multiple-cf-test.xml"})
public class SAECassandraUpdaterCqlTest {

	@Autowired 
	SAEKeyspaceConnecter saecf;

	@Autowired
	SAECassandraUpdaterCQL saeUpd;
	
	String keyspaceName = "\"SAE\"";
	
	@Before
	public void before() {
		if(saecf.getCcf().getStartLocal()) {
			keyspaceName = saecf.getCcf().getKeyspace();
		}
	}
	
	@After
	public void after() throws Exception {
		//saecf.getCcf().getServer().resetData(true, MODE_API.DATASTAX);
	}
	
	@Test
	public void addTableTracesToSAE() {
		
		// on considere qu'au debut  le cassandra local est demarré avec chargement de toutes les cf job traces
		
		try {
			
			deleteAllTablesTraces();
			
			List<String> tableNamesBefore = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
			
			saeUpd.createTablesTraces();
			
			List<String> tableNamesAfter = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
			Assert.assertEquals("Les 9 tables de traces aurait du être crées en plus", tableNamesAfter.size(), tableNamesBefore.size() +9); 
			Assert.assertTrue("La base doit contenir la tables tracedestinatairecql", tableNamesAfter.contains("tracedestinatairecql"));
			
		}catch (Exception e) {
			fail("problème de creation des tables traces dans le keyspace SAE");
		}
	}
	
	
	private void deleteAllTablesTraces() {
		
		try {
			List<String> tableNamesBefore = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
			// suppression des tables
			saeUpd.deleteTablesTraces();
			
			List<String> tableNamesAfter = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
			Assert.assertEquals("Les 9 tables de traces doivent être supprimées de la base", tableNamesAfter.size(), tableNamesBefore.size() - 9); 
			
		}catch (Exception e) {
			fail("problème de suppression des tables traces");
		}
	}
	
	@Test
	public void connectTOSAE() {
		
		try {
			
			List<String> tableNames = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
			Assert.assertTrue("Le nombre de table doit être different de zero", tableNames.size() > 0); 
			Assert.assertTrue("La base doit contenir la tables sequences", tableNames.contains("sequences"));
			
		}catch (Exception e) {
			fail("problème de connection au keyspace SAE");
		}
	}

	@Test
	public void createdeleteTablesModeapi() {
		
		try {
			
			saeUpd.createTablesModeapi();
			
			List<String> tableNames = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
			Assert.assertTrue("La base doit contenir la tables modeapi", tableNames.contains("modeapi"));
			
			saeUpd.deleteTablesModeapi();
			
			List<String> tableNamesAfter = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
			Assert.assertFalse("La base ne doit pas contenir la tables modeapi", tableNamesAfter.contains("modeapi"));
			
		}catch (Exception e) {
			fail("problème de creation de la table modeapi");
		}
	}
	
	@Test
	public void createAndDeleteJobSpringTables() {

		// on considere qu'au debut  le cassandra local est demarré avec chargement de toutes les cf job spring
		List<String> tableNamesBefore = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
		
		saeUpd.deleteTablesJobSpring();
		
		List<String> tableAfterDelete = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
		Assert.assertEquals("La base doit contenir les 9 tables job Spring", tableAfterDelete.size(), tableNamesBefore.size() - 9);
		
		saeUpd.createTablesJobSpring();
		
		List<String> tablesAfterCreate = UtilsColunmFalmilly.getTablesNames(saecf.getSession(), keyspaceName);
		Assert.assertEquals("La base doit contenir les 9 tables job Spring", tablesAfterCreate.size(), tableAfterDelete.size() + 9);
		
	}
}
