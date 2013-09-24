package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.security.VIHandler;

/**
 * Factory de création du Stub pour appeler le service web SaeService
 */
public class StubFactory {

   private static final String NOM_FICHIER_PROP = "sae-client-demo.properties";

   private static final String CLE_URL_SERVICE = "server.url";

   private static String litUrlSaeService() {
      Properties properties = new Properties();
      try {
         properties.load(ResourceUtils.loadResource(new StubFactory(),
               NOM_FICHIER_PROP));
         return properties.getProperty(CLE_URL_SERVICE);
      } catch (IOException e) {
         throw new DemoRuntimeException(e);
      }
   }

   /**
    * Création d'un Stub paramétré avec l'authentification au service web du SAE
    * 
    * @param issuer
    *           l'issuer à mettre dans le VI
    * @param pagm
    *           le pagm à mettre dans le VI
    * @return le Stub
    */
   public static SaeServiceStub createStubAvecAuthentification() {
      return createStubAvecAuthentification(null, null);
   }

   /**
    * Création d'un Stub paramétré avec l'authentification au service web du SAE
    * 
    * @param issuer
    *           l'issuer à mettre dans le VI
    * @param pagm
    *           le pagm à mettre dans le VI
    * @return le Stub
    */
   public static SaeServiceStub createStubAvecAuthentification(String issuer,
         String pagm) {

      // Création d'une configuration Axis2 par défaut
      ConfigurationContext configContext;
      try {
         configContext = ConfigurationContextFactory
               .createConfigurationContextFromFileSystem(null, null);
      } catch (AxisFault fault) {
         throw new DemoRuntimeException(fault);
      }

      // Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
      AxisConfiguration axisConfig = configContext.getAxisConfiguration();
      List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
      Phase messageOut = findPhaseByName(outFlowPhases, "MessageOut");
      messageOut.addHandler(new VIHandler(issuer, pagm));

      // Lecture du l'URL du service web du SAE depuis le fichier properties
      String urlSaeService = litUrlSaeService();

      // Création de l'objet Stub
      SaeServiceStub service;
      try {
         service = new SaeServiceStub(configContext, urlSaeService);
      } catch (AxisFault e) {
         throw new DemoRuntimeException(e);
      }

      // Renvoie l'objet Stub
      return service;

   }

   /**
    * Création d'un Stub sans authentification
    * 
    * @return le Stub
    */
   public static SaeServiceStub createStubSansAuthentification() {
      try {

         String urlSaeService = litUrlSaeService();

         return new SaeServiceStub(urlSaeService);

      } catch (AxisFault e) {
         throw new DemoRuntimeException(e);
      }
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
