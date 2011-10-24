package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.SecurityConfiguration;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ConsultationFailureTest {

   @Autowired
   private ConsultationService service;

   @Before
   public void before() {

      AuthenticateUtils.authenticate("ROLE_TOUS");
   }

   @After
   public final void after() {

      SecurityConfiguration.cleanSecurityContext();
   }

   private static final String AXIS_FAULT = "AxisFault non attendue";

   private static void assertAxisFault(AxisFault axisFault, String expectedMsg,
         String expectedType, String expectedPrefix) {

      assertEquals(AXIS_FAULT, expectedMsg, axisFault.getMessage());
      assertEquals(AXIS_FAULT, expectedType, axisFault.getFaultCode()
            .getLocalPart());
      assertEquals(AXIS_FAULT, expectedPrefix, axisFault.getFaultCode()
            .getPrefix());
   }

   @Test
   public void consultation_failure_ArchiveNonTrouve() throws RemoteException {
      try {

         service.consultation("a08addbb-f948-4489-a8a4-70fcb19feb9f");

         fail("le test doit échouer car l'archive n'existe dans le SAE");

      } catch (AxisFault fault) {

         assertAxisFault(
               fault,
               "Il n'existe aucun document pour l'identifiant d'archivage 'a08addbb-f948-4489-a8a4-70fcb19feb9f'",
               "ArchiveNonTrouvee", "sae");

      }
   }

}
