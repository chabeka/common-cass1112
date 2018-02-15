package fr.urssaf.image.commons.util.object;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;

/**
 * Tests unitaires de la classe {@link ObjectUtil}
 */
@SuppressWarnings("PMD")
public class ObjectUtilTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(ObjectUtil.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Tests unitaires de la méthode {@link ObjectUtil#getDefaultValue(Class)}
    */
   @Test
   public void getDefaultValue() {
      
      Object result;
      
      result = ObjectUtil.getDefaultValue(int.class);
      assertEquals("La valeur par défaut est incorrecte",0,result);
      
      result = ObjectUtil.getDefaultValue(boolean.class);
      assertEquals("La valeur par défaut est incorrecte",false,result);
      
      result = ObjectUtil.getDefaultValue(float.class);
      assertEquals("La valeur par défaut est incorrecte",(float)0,result);
      
      result = ObjectUtil.getDefaultValue(double.class);
      assertEquals("La valeur par défaut est incorrecte",(double)0,result);
      
      result = ObjectUtil.getDefaultValue(byte.class);
      assertEquals("La valeur par défaut est incorrecte",(byte)0,result);
      
      result = ObjectUtil.getDefaultValue(long.class);
      assertEquals("La valeur par défaut est incorrecte",(long)0,result);
      
      result = ObjectUtil.getDefaultValue(char.class);
      assertEquals("La valeur par défaut est incorrecte",(char)0,result);
      
      result = ObjectUtil.getDefaultValue(short.class);
      assertEquals("La valeur par défaut est incorrecte",(Integer)0,result);
      
      result = ObjectUtil.getDefaultValue(void.class);
      assertNull("La valeur par défaut est incorrecte",result);
      
   }
   
}
