package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.SuppressionResponse;
import fr.urssaf.image.sae.webservices.util.SoapTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class SuppressionTest {

   @Autowired
   private SuppressionService suppressionService;
   
   @Test
   public void suppression_success() throws RemoteException {
      
      String idArchive = "D99D899A-C82F-43ED-AA38-11DDD179FB85";
      
      SuppressionResponse response = suppressionService.suppression(idArchive);
      
      Assert.assertNotNull("La réponse ne doit pas être null", response);
   }
   
   @Test
   public void suppression_failure() {
      
      String idArchive = "00000000-0000-0000-0000-000000000000";
      
      try {
         
         suppressionService.suppression(idArchive);
         
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
         
      } catch (AxisFault fault) {
         
         SoapTestUtils
         .assertAxisFault(
               fault,
               "Il n'existe aucun document pour l'identifiant d'archivage 00000000-0000-0000-0000-000000000000",
               "SuppressionArchiveNonTrouvee",
               SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);
       
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }
   
}
