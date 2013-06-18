package fr.urssaf.image.sae.client.vi.test;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.junit.Assert;
import org.junit.Test;

import fr.urssaf.image.sae.client.vi.VIHandler;
import fr.urssaf.image.sae.client.vi.signature.DefaultKeystore;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Consultation;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ConsultationRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureRequest;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.PingSecureResponse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.UuidType;

public class MethodCallsTest {

   private static final String URL_WS = "http://cer69-saeint3.cer69.recouv:8080/sae/services/SaeService/";

   @Test
   public void testPingSecure() throws RemoteException {

      SaeServiceStub service = getStub();

      PingSecureRequest request = new PingSecureRequest();

      PingSecureResponse response = service.pingSecure(request);

      String pingString = response.getPingString();

      Assert.assertEquals(
            "Les services du SAE sécurisés par authentification sont en ligne",
            pingString);

   }

   @Test
   public void testConsultation() throws RemoteException {
      try {
         SaeServiceStub service = getStub();

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

   private SaeServiceStub getStub() throws AxisFault {
      // Création d'une configuration Axis2 par défaut
      ConfigurationContext configContext = ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(null, null);

      // Création du Stub
      SaeServiceStub service = new SaeServiceStub(configContext, URL_WS);

      KeyStoreInterface keystore = DefaultKeystore.getInstance();
      List<String> pagms = Arrays.asList("ROLE_TOUS;FULL");
      String issuer = "PNR";
      VIHandler handler = new VIHandler(keystore, pagms, issuer);

      // Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
      AxisConfiguration axisConfig = configContext.getAxisConfiguration();
      List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
      Phase messageOut = findPhaseByName(outFlowPhases, "MessageOut");
      messageOut.addHandler(handler);

      return service;
   }

   private static Phase findPhaseByName(List<Phase> phases,
         String nomPhaseRecherchee) {

      Phase result = null;

      for (Phase phase : phases) {
         if (phase.getName().equals(nomPhaseRecherchee)) {
            result = phase;
            break;
         }
      }

      return result;

   }

}
