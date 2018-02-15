package fr.urssaf.image.commons.birt;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;


/**
 * Tests unitaires de la classe {@link BirtEngineFactoryKeys}
 *
 */
public class BirtEngineFactoryKeysTest {

   /**
    * Test unitaire du constructeur privé, pour le code coverage
    * @throws TestConstructeurPriveException en cas d'erreur lors du test du constructeur privé
    */
   @Test
   public void testConstructeurPrive() throws TestConstructeurPriveException
   {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(BirtEngineFactoryKeys.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
}