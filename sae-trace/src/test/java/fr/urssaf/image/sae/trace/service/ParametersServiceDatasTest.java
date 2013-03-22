/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class ParametersServiceDatasTest {

   private static final Date DATE = new Date();
   private static final Boolean IS_RUNNING = Boolean.FALSE;
   private static final Integer DUREE = 5;

   @Autowired
   private ParametersService service;

   @Test
   public void testLoadCodeObligatoire() {
      createParameters();

      try {
         service.loadParameter(ParameterType.PURGE_SECU_DATE);
         Assert.fail("une exception est attendue");
      } catch (ParameterNotFoundException exception) {
         Assert
               .assertTrue("le message concerne le bon paramètre", exception
                     .getMessage().contains(
                           ParameterType.PURGE_SECU_DATE.toString()));
      }

      Parameter param;

      try {
         param = service.loadParameter(ParameterType.PURGE_EXPLOIT_DATE);
         Assert.assertNotNull("le paramètre doit etre trouvé", param);
         Assert.assertTrue(
               "le paramètre doit etre une instance de la bonne classe : "
                     + Date.class.getName(), param.getValue() instanceof Date);
         Assert.assertEquals("la valeur doit etre correcte", DATE, (Date) param
               .getValue());
      } catch (ParameterNotFoundException exception) {
         Assert.fail("aucune erreur attendue");
      }

      try {
         param = service.loadParameter(ParameterType.PURGE_EXPLOIT_DUREE);
         Assert.assertNotNull("le paramètre doit etre trouvé", param);
         Assert.assertTrue(
               "le paramètre doit etre une instance de la bonne classe : "
                     + Integer.class.getName(),
               param.getValue() instanceof Integer);
         Assert.assertEquals("la valeur doit etre correcte", DUREE,
               (Integer) param.getValue());
      } catch (ParameterNotFoundException exception) {
         Assert.fail("aucune erreur attendue");
      }

      try {
         param = service.loadParameter(ParameterType.PURGE_EXPLOIT_IS_RUNNING);
         Assert.assertNotNull("le paramètre doit etre trouvé", param);
         Assert.assertTrue(
               "le paramètre doit etre une instance de la bonne classe : "
                     + Boolean.class.getName(),
               param.getValue() instanceof Boolean);
         Assert.assertEquals("la valeur doit etre correcte", IS_RUNNING,
               (Boolean) param.getValue());
      } catch (ParameterNotFoundException exception) {
         Assert.fail("aucune erreur attendue");
      }
   }

   private void createParameters() {
      Parameter param = new Parameter(ParameterType.PURGE_EXPLOIT_DATE, DATE);
      service.saveParameter(param);
      param = new Parameter(ParameterType.PURGE_EXPLOIT_DUREE, DUREE);
      service.saveParameter(param);
      param = new Parameter(ParameterType.PURGE_EXPLOIT_IS_RUNNING, IS_RUNNING);
      service.saveParameter(param);
   }
}
