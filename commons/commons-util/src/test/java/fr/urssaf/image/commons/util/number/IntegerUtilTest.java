package fr.urssaf.image.commons.util.number;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link IntegerUtil}  
 */
@SuppressWarnings("PMD")
public class IntegerUtilTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(IntegerUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Tests unitaires de la méthode {@link IntegerUtil#sup(Integer, Integer)}
    */
   @Test
   public void sup() {
   
      Boolean result;
      Integer num1;
      Integer num2;
      
      num1 = null;
      num2 = null;
      result = IntegerUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1;
      num2 = null;
      result = IntegerUtil.sup(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = null;
      num2 = 1;
      result = IntegerUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1;
      num2 = 2;
      result = IntegerUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 2;
      num2 = 1;
      result = IntegerUtil.sup(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = 1;
      num2 = 1;
      result = IntegerUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link IntegerUtil#inf(Integer, Integer)}
    */
   @Test
   public void inf() {
   
      Boolean result;
      Integer num1;
      Integer num2;
      
      num1 = null;
      num2 = null;
      result = IntegerUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1;
      num2 = null;
      result = IntegerUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = null;
      num2 = 1;
      result = IntegerUtil.inf(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = 1;
      num2 = 2;
      result = IntegerUtil.inf(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = 2;
      num2 = 1;
      result = IntegerUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1;
      num2 = 1;
      result = IntegerUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link IntegerUtil#positif(Integer)}
    */
   @Test
   public void positif() {
      
      Integer num;
      Boolean result;
      
      num = null;
      result = IntegerUtil.positif(num);
      assertFalse("La comparaison a échoué",result);
      
      num = 1;
      result = IntegerUtil.positif(num);
      assertTrue("La comparaison a échoué",result);
      
      num = -1;
      result = IntegerUtil.positif(num);
      assertFalse("La comparaison a échoué",result);
      
      // TODO : revoir ce cas
      num = 0;
      result = IntegerUtil.positif(num);
      assertFalse("La comparaison a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link IntegerUtil#negatif(Integer)}
    */
   @Test
   public void negatif() {
      
      Integer num;
      Boolean result;
      
      // TODO : revoir ce cas
      num = null;
      result = IntegerUtil.negatif(num);
      assertTrue("La comparaison a échoué",result);
      
      num = 1;
      result = IntegerUtil.negatif(num);
      assertFalse("La comparaison a échoué",result);
      
      num = -1;
      result = IntegerUtil.negatif(num);
      assertTrue("La comparaison a échoué",result);
      
      num = 0;
      result = IntegerUtil.negatif(num);
      assertFalse("La comparaison a échoué",result);
      
   }
   
}
