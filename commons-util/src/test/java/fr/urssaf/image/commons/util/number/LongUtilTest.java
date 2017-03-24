package fr.urssaf.image.commons.util.number;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link LongUtil} 
 */
@SuppressWarnings("PMD")
public class LongUtilTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(LongUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Tests unitaires de la méthode {@link LongUtil#sup(Long, Long)}
    */
   @Test
   public void sup() {
   
      Boolean result;
      Long num1;
      Long num2;
      
      num1 = null;
      num2 = null;
      result = LongUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = (long) 1;
      num2 = null;
      result = LongUtil.sup(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = null;
      num2 = (long) 1;
      result = LongUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = (long) 1;
      num2 = (long) 2;
      result = LongUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = (long) 2;
      num2 = (long) 1;
      result = LongUtil.sup(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = (long) 1;
      num2 = (long) 1;
      result = LongUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link LongUtil#inf(Long, Long)}
    */
   @Test
   public void inf() {
   
      Boolean result;
      Long num1;
      Long num2;
      
      num1 = null;
      num2 = null;
      result = LongUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = (long)1;
      num2 = null;
      result = LongUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = null;
      num2 = (long)1;
      result = LongUtil.inf(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = (long)1;
      num2 = (long)2;
      result = LongUtil.inf(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = (long)2;
      num2 = (long)1;
      result = LongUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = (long)1;
      num2 = (long)1;
      result = LongUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
   }
   
}
