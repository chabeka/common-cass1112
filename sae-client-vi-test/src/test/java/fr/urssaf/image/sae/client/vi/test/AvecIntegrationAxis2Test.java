package fr.urssaf.image.sae.client.vi.test;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.webservices.factory.StubFactory;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureRequest;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureResponse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.UuidType;

public class AvecIntegrationAxis2Test {

   /**
    * Test de consommation de l'opération "PingSecure"
    * 
    * @throws RemoteException
    */
   @Test
   public void testPingSecure() throws RemoteException {

      SaeServiceStub service = StubFactory.getStub();

      PingSecureRequest request = new PingSecureRequest();

      PingSecureResponse response = service.pingSecure(request);

      String pingString = response.getPingString();

      Assert.assertEquals(
            "Les services du SAE sécurisés par authentification sont en ligne",
            pingString);

   }

   /**
    * Test de consommation de l'opération "consultation"
    * 
    * @throws RemoteException
    */
   @Test
   public void testConsultation() throws RemoteException {
      try {
         SaeServiceStub service = StubFactory.getStub();

         Consultation consultation = new Consultation();
         ConsultationRequestType type = new ConsultationRequestType();
         UuidType uuidType = new UuidType();
         uuidType.setUuidType("00000000-0000-0000-0000-000000000000");
         type.setIdArchive(uuidType);

         ListeMetadonneeCodeType listeType = new ListeMetadonneeCodeType();
         MetadonneeCodeType meta = new MetadonneeCodeType();
         meta.setMetadonneeCodeType("Siret");
         MetadonneeCodeType[] tabCode = new MetadonneeCodeType[] { meta };
         listeType.setMetadonneeCode(tabCode);
         type.setMetadonnees(listeType);
         consultation.setConsultation(type);

         service.consultation(consultation);

      } catch (AxisFault ex) {

         Assert
               .assertEquals(
                     "Il n'existe aucun document pour l'identifiant d'archivage '00000000-0000-0000-0000-000000000000'",
                     ex.getMessage());

      }
   }

}
