package fr.urssaf.image.sae.webservices.service;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ModificationResponse;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;
import fr.urssaf.image.sae.webservices.util.SoapTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ModificationTest {

   @Autowired
   private ModificationService modificationService;

   @Test
   public void modification_success() throws RemoteException {

      String idArchive = "5A06E1C2-048A-4E46-B7F2-9A93D48300AB";

      List<Metadata> metadatas = new ArrayList<Metadata>();
      metadatas.add(ObjectModelFactory.createMetadata("NumeroIntControle",
            "1234"));
      metadatas.add(ObjectModelFactory
            .createMetadata("Denomination", "Mr TOTO"));
      metadatas.add(ObjectModelFactory.createMetadata("PseudoSiret", ""));
      metadatas.add(ObjectModelFactory.createMetadata("Siren", ""));
      metadatas.add(ObjectModelFactory.createMetadata("Siret", "1186768767"));

      ModificationResponse response = modificationService.modification(
            idArchive, metadatas);

      Assert.assertNotNull("La réponse ne doit pas être null", response);
   }

   @Test
   public void modification_failure_archiveNonTrouvee() {

      String idArchive = "00000000-0000-0000-0000-000000000000";

      List<Metadata> metadatas = new ArrayList<Metadata>();
      metadatas.add(ObjectModelFactory.createMetadata("NumeroIntControle",
            "1234"));
      metadatas.add(ObjectModelFactory
            .createMetadata("Denomination", "Mr TOTO"));
      metadatas.add(ObjectModelFactory.createMetadata("PseudoSiret", ""));
      metadatas.add(ObjectModelFactory.createMetadata("Siren", ""));
      metadatas.add(ObjectModelFactory.createMetadata("Siret", "1186768767"));

      try {

         modificationService.modification(idArchive, metadatas);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "Il n'existe aucun document pour l'identifiant d'archivage 00000000-0000-0000-0000-000000000000",
                     "ModificationArchiveNonTrouvee",
                     SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      } catch (RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n"
               + exception);

      }

   }

   @Test
   public void modification_failure_metadonneeNonModifiable() {

      String idArchive = "5A06E1C2-048A-4E46-B7F2-9A93D48300AB";

      List<Metadata> metadatas = new ArrayList<Metadata>();
      metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
            "Toto"));

      try {

         modificationService.modification(idArchive, metadatas);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      } catch (AxisFault fault) {

         SoapTestUtils
               .assertAxisFault(
                     fault,
                     "La ou les métadonnées suivantes ne sont pas modifiables : ApplicationProductrice",
                     "ModificationMetadonneeNonModifiable",
                     SoapTestUtils.SAE_NAMESPACE, SoapTestUtils.SAE_PREFIX);

      } catch (RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n"
               + exception);

      }

   }

}
