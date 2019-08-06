package fr.urssaf.image.sae.lotinstallmaj.service.cqlimpl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.AssertTrue;

import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.thrift.Cassandra.system_add_column_family_args;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.ColumnMetadata;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TableMetadata;

import fr.urssaf.image.sae.lotinstallmaj.cql.UtilsCassandraUnitStartHelper;
import fr.urssaf.image.sae.lotinstallmaj.cql.UtilsColunmFalmilly;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.DFCECassandraUpdaterCQL;
import fr.urssaf.image.sae.lotinstallmaj.service.cql.impl.DFCEKeyspaceConnecter;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileLoader;
import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileSet;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-multiple-cf-test.xml"})
public class DFCEUpdaterCqlTest {

	private static final String DFCE = "DFCE";

	@Autowired 
	DFCEKeyspaceConnecter dcf;

	@Autowired
	DFCECassandraUpdaterCQL dfceUpd;
	
    @Before
    public void init() throws Exception, IOException,
    InterruptedException, ConfigurationException {
    	UtilsCassandraUnitStartHelper utils = new UtilsCassandraUnitStartHelper();
      // On démarre un serveur cassandra local
      //EmbeddedCassandraServerHelper.startEmbeddedCassandra();
      //utils.waitForServer();
    }
    
	@Test
	public void connectTODFCE() {
		
		try {
			List<String> tableNames = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
			Assert.assertTrue("", tableNames.size() > 0); 
		}catch (Exception e) {
			fail("problème de connection au keyspace DFCE");
		}		
	}
	
	@Test
	public void test() {
		
	    // Vérification de l'existence du keyspace
	    final String selectQuery = "SELECT * FROM system.schema_keyspaces;";
	    final ResultSet keys = dcf.getSession().execute(selectQuery);
	    Assert.assertTrue("Le resultat doit être different de null", keys != null); 
	}

	@Test
	public void update192ToVersion200() throws IOException {
		
		// AVANT MIG
		List<String> listCFBeforeUpdate = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
		// verifie que la colonne n'esite pas avant 
		List<String> ColNames = UtilsColunmFalmilly.getTableColunmNames(dcf.getSession(), DFCE,"life_cycle_step_history");
		Assert.assertFalse("La colonne 'pattern' ne doit pas être dans la table life_cycle_step_history", ColNames.contains("pattern"));
		
		
		dfceUpd.update192ToVersion200();
		
		
		// APRES
		// Après le passage du script, la colonne doit existé
		ColNames = UtilsColunmFalmilly.getTableColunmNames(dcf.getSession(), DFCE,"life_cycle_step_history");
		Assert.assertTrue("La colonne 'pattern'doit être dans la table life_cycle_step_history", ColNames.contains("pattern"));
		
		// test la presence de la table doc_text

		List<String> list = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
		Assert.assertEquals("Une nouvelle table en plus doit être ajouté à la liste des tables", listCFBeforeUpdate.size() + 1, list.size());
		Assert.assertTrue("La liste doit contenir le nom de la table", list.contains("doc_text"));
		
		
		// test sur l'ajout de la colonne full_text dans la table base

		List<String> baseColNames = UtilsColunmFalmilly.getTableColunmNames(dcf.getSession(), DFCE,"base");
		Assert.assertTrue("La colonne full_text doit être dans la table base ", baseColNames.contains("full_text"));
		
		// test sur l'insertion des données dans la table metadata
		
		String req = "select * from metadata where name='sm_file_uuid' and translation_code='DEFAULT';";
		ResultSet result = dcf.getSession().execute(req);
		Assert.assertEquals("la valeur name='sm_file_uuid' doit être présent dans la base", result.all().size(), 1);
		
		String req1 = "select * from metadata where name='sm_user_locale' and translation_code='DEFAULT';";
		ResultSet result1 = dcf.getSession().execute(req1);
		Assert.assertEquals("la valeur name='sm_user_locale' doit être présent dans la base", result1.all().size(), 1);
		
		// test sur l'insertion des données dans la table metadata_translation	
		String req2 = "select * from metadata_translation "
						+" where code='DEFAULT' AND metadata_name='sm_user_time_zone' AND scope='SYSTEM' AND domain='USER';";
		ResultSet result2 = dcf.getSession().execute(req2);
		Assert.assertEquals("la valeur name='sm_file_uuid' doit être présent dans la base", result2.all().size(), 1);
		
		// Suppression dans la table metadata_translation
		String req3 = "select * from metadata_translation "
					  +"where code='DEFAULT' AND metadata_name='sm_key_reference_uuid' AND domain='INDEXABLE' AND scope='SYSTEM';";
		ResultSet result3 = dcf.getSession().execute(req3);
		Assert.assertEquals("la valeur name='sm_user_locale' doit être présent dans la base", result3.all().size(), 0);
		
		
		// test pour voir si la derniere ligne a bien été executer
		
		String req4 = "select * from metadata_translation "
					+ "where code='DEFAULT' AND domain='INDEXABLE_DOCUMENT' AND scope='SYSTEM' AND metadata_name='sm_encrypted_key_uuid'";
		ResultSet result4 = dcf.getSession().execute(req4);
		Assert.assertEquals("la valeur name='sm_encrypted_key_uuid' doit être présent dans la table metadata_translation", result4.all().size(), 1);
	}
	
	@Test
	public void update200ToVersion210() throws IOException {
		
		// AVANT MIG	
		List<String> listCFBeforeUpdate = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
		
				
		dfceUpd.update200ToVersion210();
		
		
		// APRES MIG
		// Test la création des nouvelles tables
		List<String> list = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
		int nbCF = listCFBeforeUpdate.size() + 3;
		Assert.assertEquals("Trois nouvelles tables en plus doivent être ajoutée à la liste des tables", nbCF, list.size());
		
		// test d'insertion dans la table metadata
		String req1 = "select * from metadata where name='sm_document_overlay_name' and translation_code='DEFAULT';";
		ResultSet result1 = dcf.getSession().execute(req1);
		Assert.assertEquals("la valeur name='sm_document_overlay_name' doit être présent dans la base", result1.all().size(), 1);
		
		// test sur l'insertion des données dans la table metadata_translation	
		String req2 = "select * from metadata_translation "
						+" where code='DEFAULT' AND metadata_name='sm_document_overlay_name' AND scope='SYSTEM' AND domain='INDEXABLE_DOCUMENT';";
		ResultSet result2 = dcf.getSession().execute(req2);
		Assert.assertEquals("la valeur name='sm_file_uuid' doit être présent dans la base", result2.all().size(), 1);
	}
	
	@Test
	public void update210ToVersion230() throws IOException {
		
		// AVANT MIG
		List<String> listCFBeforeUpdate = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
		
		dfceUpd.update210ToVersion230();
		
		// APRES MIG
		// Test la création des nouvelles tables
		List<String> list = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
		int nbCF = listCFBeforeUpdate.size() + 2;
		Assert.assertEquals("Deux nouvelles tables en plus doivent être ajoutée à la liste des tables", nbCF, list.size());
		
		// test sur l'ajout de la colonne full_text dans la table base
		List<String> baseColNames = UtilsColunmFalmilly.getTableColunmNames(dcf.getSession(), DFCE, "action_by_day");
		Assert.assertTrue("La table action_by_day doit avoir une colonne: 'errors' ", baseColNames.contains("errors"));
		Assert.assertTrue("La table action_by_day doit avoir une colonne: 'creation_date' ", baseColNames.contains("creation_date"));
	}
	
	@Test
	public void update230ToVersion192() throws IOException {
		
		// AVANT MIG
		List<String> listCFBeforeUpdate = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
				
		dfceUpd.update230ToVersion192();
		
		// APRES MIG

		List<String> list = UtilsColunmFalmilly.getTablesNames(dcf.getSession(), DFCE);
		// Test la suppression des nouvelles tables
		int nbCF = list.size() + 6;
		Assert.assertEquals("Deux nouvelles tables en plus doivent être ajoutée à la liste des tables", nbCF, listCFBeforeUpdate);
		
		// test de suppression de la table suggestion
		Assert.assertFalse("La table 'suggestion' doit être supprimé", list.contains("suggestion"));
		Assert.assertFalse("La table 'suggestor'  doit être supprimé", list.contains("suggestor"));
		Assert.assertFalse("La table 'dictionary_terms'  doit être supprimé", list.contains("dictionary_terms"));
		Assert.assertFalse("La table 'doc_text'  doit être supprimé", list.contains("doc_text"));
		
		List<String> listColNames = UtilsColunmFalmilly.getTableColunmNames(dcf.getSession(), DFCE, "base");
		Assert.assertFalse("La colonne 'full_text' doit être supprimé de la table 'base'", listColNames.contains("full_text"));
		Assert.assertFalse("La colonne 'es_index'  doit être supprimé de la table 'base'", listColNames.contains("es_index"));
		
	}
	
}
