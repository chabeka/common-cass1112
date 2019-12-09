package fr.urssaf.image.sae.lotinstallmaj.cql;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.lotinstallmaj.service.utils.cql.CQLDataFileSet;

public class ReadClqFileAndValidationTest {

	@Test
	public void test() {
		String fileLocation = "src/test/resources/cql/schema.cql";
		CQLDataFileSet cqlF = new CQLDataFileSet(fileLocation);
		List<String> lines = cqlF.getCQLStatements();
		Assert.assertEquals("le nombre d'éléments de la liste doit etre correct", 7, lines.size());
	}

}
