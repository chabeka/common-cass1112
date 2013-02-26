/**
 * 
 */
package fr.urssaf.image.sae.trace.service;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.model.JournalisationType;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.model.PurgeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-test.xml" })
public class StatusServiceDatasTest {

   @Autowired
   private StatusService service;

   @Autowired
   private ParametersService paramService;

   @Test
   public void testIsPurgeRunning() throws ParameterNotFoundException {

      List<ParameterType> parameters = Arrays.asList(
            ParameterType.PURGE_EVT_IS_RUNNING,
            ParameterType.PURGE_EXPLOIT_IS_RUNNING,
            ParameterType.PURGE_SECU_IS_RUNNING,
            ParameterType.PURGE_TECH_IS_RUNNING);
      createParameters(parameters, Boolean.TRUE);

      List<PurgeType> purges = Arrays.asList(PurgeType.PURGE_EXPLOITATION,
            PurgeType.PURGE_EVT, PurgeType.PURGE_SECURITE,
            PurgeType.PURGE_TECHNIQUE);
      for (PurgeType purgeType : purges) {
         Assert.assertEquals("la valeur doit etre correcte pour la purge "
               + purgeType, Boolean.TRUE, service.isPurgeRunning(purgeType));
      }

   }

   @Test
   public void testSavePurge() {
      List<PurgeType> purgesTrue = Arrays.asList(PurgeType.PURGE_EXPLOITATION,
            PurgeType.PURGE_EVT, PurgeType.PURGE_SECURITE);

      List<PurgeType> purgesFalse = Arrays.asList(PurgeType.PURGE_TECHNIQUE);

      for (PurgeType purgeType : purgesTrue) {
         service.updatePurgeStatus(purgeType, Boolean.TRUE);

         Assert.assertEquals("la valeur doit etre correcte pour la purge "
               + purgeType, Boolean.TRUE, service.isPurgeRunning(purgeType));
      }

      for (PurgeType purgeType : purgesFalse) {
         service.updatePurgeStatus(purgeType, Boolean.FALSE);

         Assert.assertEquals("la valeur doit etre correcte pour la purge "
               + purgeType, Boolean.FALSE, service.isPurgeRunning(purgeType));
      }
   }

   @Test
   public void testIsJournalisationRunning() throws ParameterNotFoundException {

      List<ParameterType> parameters = Arrays
            .asList(ParameterType.JOURNALISATION_EVT_IS_RUNNING);
      createParameters(parameters, Boolean.TRUE);

      Assert
            .assertEquals(
                  "la valeur doit etre correcte pour la journalisation "
                        + JournalisationType.JOURNALISATION_EVT,
                  Boolean.TRUE,
                  service
                        .isJournalisationRunning(JournalisationType.JOURNALISATION_EVT));

   }

   @Test
   public void testSaveJournalisation() {

      service.updateJournalisationStatus(JournalisationType.JOURNALISATION_EVT,
            Boolean.FALSE);

      Assert
            .assertEquals(
                  "la valeur doit etre correcte pour la journalisation "
                        + JournalisationType.JOURNALISATION_EVT,
                  Boolean.FALSE,
                  service
                        .isJournalisationRunning(JournalisationType.JOURNALISATION_EVT));
   }

   private void createParameters(List<ParameterType> parameters, Object value) {

      Parameter parameter;
      for (ParameterType parameterType : parameters) {
         parameter = new Parameter(parameterType, value);
         paramService.saveParameter(parameter);
      }

   }
}
