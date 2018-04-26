package fr.urssaf.image.commons.util.number;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link DoubleUtil}
 */
@SuppressWarnings("PMD")
public class DoubleUtilTest {

   
   /**
    * Test du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(DoubleUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DoubleUtil#sup(Double, Double)}
    */
   @Test
   public void sup() {
   
      Boolean result;
      Double num1;
      Double num2;
      
      num1 = null;
      num2 = null;
      result = DoubleUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1.0;
      num2 = null;
      result = DoubleUtil.sup(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = null;
      num2 = 1.0;
      result = DoubleUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1.0;
      num2 = 2.0;
      result = DoubleUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 2.0;
      num2 = 1.0;
      result = DoubleUtil.sup(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = 1.0;
      num2 = 1.0;
      result = DoubleUtil.sup(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DoubleUtil#inf(Double, Double)}
    */
   @Test
   public void inf() {
   
      Boolean result;
      Double num1;
      Double num2;
      
      num1 = null;
      num2 = null;
      result = DoubleUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1.0;
      num2 = null;
      result = DoubleUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = null;
      num2 = 1.0;
      result = DoubleUtil.inf(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = 1.0;
      num2 = 2.0;
      result = DoubleUtil.inf(num1, num2);
      assertTrue("La comparaison a échoué",result);
      
      num1 = 2.0;
      num2 = 1.0;
      result = DoubleUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
      num1 = 1.0;
      num2 = 1.0;
      result = DoubleUtil.inf(num1, num2);
      assertFalse("La comparaison a échoué",result);
      
   }
   
   
   /**
    * Tests unitaires de la méthode {@link DoubleUtil#range(Double, Double, Double, boolean, boolean))}
    */
   @Test
   public void range() {
      
      Double num;
      Double min;
      Double max;
      boolean minClose;
      boolean maxClose;
      Boolean resultat;
      
      num = 1.5;
      min = 1.0;
      max = 2.0;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertTrue("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = 1.0;
      min = 1.0;
      max = 2.0;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertTrue("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = 1.0;
      min = 1.0;
      max = 2.0;
      minClose = false;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertFalse("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = 2.0;
      min = 1.0;
      max = 2.0;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertTrue("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = 2.0;
      min = 1.0;
      max = 2.0;
      minClose = true;
      maxClose = false;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertFalse("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = null;
      min = null;
      max = null;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertTrue("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = null;
      min = 1.0;
      max = null;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertFalse("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = null;
      min = null;
      max = 2.0;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertTrue("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = 1.5;
      min = null;
      max = null;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertFalse("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = 1.5;
      min = 1.0;
      max = null;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertFalse("La vérification de présence dans un intervalle a échoué",resultat);
      
      num = 1.5;
      min = null;
      max = 2.0;
      minClose = true;
      maxClose = true;
      resultat = DoubleUtil.range(num, min, max, minClose, maxClose);
      assertTrue("La vérification de présence dans un intervalle a échoué",resultat);
      
   }
   
}
