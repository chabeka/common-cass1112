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

      final SaeServiceStub service = StubFactory.getStub();

      final PingSecureRequest request = new PingSecureRequest();

      final PingSecureResponse response = service.pingSecure(request);

      final String pingString = response.getPingString();

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
         final SaeServiceStub service = StubFactory.getStub();

         final Consultation consultation = new Consultation();
         final ConsultationRequestType type = new ConsultationRequestType();
         final UuidType uuidType = new UuidType();
         uuidType.setUuidType("00000000-0000-0000-0000-000000000000");
         type.setIdArchive(uuidType);

         final ListeMetadonneeCodeType listeType = new ListeMetadonneeCodeType();
         final MetadonneeCodeType meta = new MetadonneeCodeType();
         meta.setMetadonneeCodeType("Siret");
         final MetadonneeCodeType[] tabCode = new MetadonneeCodeType[] {meta};
         listeType.setMetadonneeCode(tabCode);
         type.setMetadonnees(listeType);
         consultation.setConsultation(type);

         service.consultation(consultation);

      }
      catch (final AxisFault ex) {

         Assert
               .assertEquals(
                             "L'archive 00000000-0000-0000-0000-000000000000 n'a été trouvée dans aucune des instances de la GED.",
                             ex.getMessage());

      }
   }

}
