package fr.urssaf.image.commons.dao.spring.util;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.util.tests.TestsUtils;
import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;


/**
 * Tests unitaires de la classe {@link CriteriaUtil}
 *
 */
@SuppressWarnings("PMD")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
public class CriteriaUtilTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException 
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(CriteriaUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
}
