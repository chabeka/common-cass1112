package fr.urssaf.image.sae.lotinstallmaj.service.cqlimpl;

import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-lotinstallmaj-multiple-cf-test.xml"})
public class CreateTracesTablesTest {



	@Test
	public void createAllTablesTraces() {
		
		//fail("problème de connection au keyspace SAE");
		
	}
	


}
