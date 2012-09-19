package fr.urssaf.image.commons.maquette.tool;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;


/**
 * Tests unitaires de la classe {@link MaquetteConstant}
 *
 */
@SuppressWarnings("PMD")
public class MaquetteConstantTest {
   

   /**
    * Test unitaire du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException 
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(MaquetteConstant.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }

}
