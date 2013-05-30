package fr.urssaf.image.sae.client.vi.test;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.Suppression;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.SuppressionRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.UuidType;

public class MethodCallsTest {

   private static final String ERROR_END_POINT = "The endpoint reference (EPR) for the Operation not found";

   @Test
   public void testConsultation() {
      try {
         SaeServiceStub service = getStub();

         Consultation consultation = new Consultation();
         ConsultationRequestType type = new ConsultationRequestType();
         UuidType uuidType = new UuidType();
         uuidType.setUuidType(UUID.randomUUID().toString());
         type.setIdArchive(uuidType);

         ListeMetadonneeCodeType listeType = new ListeMetadonneeCodeType();
         MetadonneeCodeType meta = new MetadonneeCodeType();
         meta.setMetadonneeCodeType("IdTraitementMasseInterne");
         MetadonneeCodeType[] tabCode = new MetadonneeCodeType[] { meta };
         listeType.setMetadonneeCode(tabCode);
         type.setMetadonnees(listeType);
         consultation.setConsultation(type);

         service.consultation(consultation);

      } catch (AxisFault exception) {
         if (exception.getMessage().contains(ERROR_END_POINT)) {
            Assert.fail("impossible de joindre le service demandé");
         }
         exception.printStackTrace();

      } catch (RemoteException exception) {
         Assert.fail("l'appel à échoué");
      }
   }

   @Test
   public void testSuppression() {
      Suppression suppression = new Suppression();
      SuppressionRequestType type = new SuppressionRequestType();
      UuidType uuidType = new UuidType();
      uuidType.setUuidType(UUID.randomUUID().toString());
      type.setUuid(uuidType);
      suppression.setSuppression(type);

      try {
         getStub().suppression(suppression);

      } catch (AxisFault exception) {
         if (exception.getMessage().contains(ERROR_END_POINT)) {
            Assert.fail("impossible de joindre le service demandé");
         }
         exception.printStackTrace();

      } catch (RemoteException exception) {
         Assert.fail("erreur de connexion");
      }
   }

   private SaeServiceStub getStub() throws AxisFault {
      // Création d'une configuration Axis2 par défaut
      ConfigurationContext configContext = ConfigurationContextFactory
            .createConfigurationContextFromFileSystem(null, null);

      // Récupération de l'URL des services web SAE depuis le fichier
      // properties
      String urlServiceWeb = "http://hwi69devsaeweb.cer69.recouv/sae/services/SaeService/";

      // Création du Stub
      SaeServiceStub service = new SaeServiceStub(configContext, urlServiceWeb);

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
