/**
 * 
 */
package fr.urssaf.image.sae.trace.dao.support;

import java.util.Date;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class ParametersSupportTest {

   private static final Integer DUREE = 5;
   private static final Date DATE = new Date();
   private static final Boolean IS_RUNNING = Boolean.FALSE;

   @Autowired
   private ParametersSupport support;

   @Autowired
   private CassandraServerBean server;

   @After
   public void after() throws Exception {
      server.resetData();
   }

   @Test
   public void testCreateFindSuccess() {

      Parameter param = null;
      try {
         createParameters();
         param = support.find(ParameterType.PURGE_SECU_DATE);
         Assert.fail("une exception est attendue");
      } catch (ParameterNotFoundException exception) {
         Assert
               .assertTrue("le message concerne le bon parametre", exception
                     .getMessage().contains(
                           ParameterType.PURGE_SECU_DATE.toString()));
      }

      try {
         param = support.find(ParameterType.PURGE_EXPLOIT_DATE);
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
         param = support.find(ParameterType.PURGE_EXPLOIT_DUREE);
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
         param = support.find(ParameterType.PURGE_EXPLOIT_IS_RUNNING);
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
      Parameter parameter = new Parameter(ParameterType.PURGE_EXPLOIT_DATE,
            DATE);
      support.create(parameter, new Date().getTime());
      parameter = new Parameter(ParameterType.PURGE_EXPLOIT_DUREE, DUREE);
      support.create(parameter, new Date().getTime());
      parameter = new Parameter(ParameterType.PURGE_EXPLOIT_IS_RUNNING,
            IS_RUNNING);
      support.create(parameter, new Date().getTime());
   }
}
