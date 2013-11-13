package sae.client.demo.webservice.factory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;

import fr.urssaf.image.sae.client.vi.VIHandler;
import fr.urssaf.image.sae.client.vi.signature.KeyStoreInterface;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.security.MyKeyStore;

/**
 * Factory de création du Stub pour appeler le service web SaeService
 */
public class StubFactory {

   /**
    * Nom du fichier properties contenant l'URL du service web SAE
    */
   private static final String NOM_FICHIER_PROP = "sae-client-demo.properties";

   /**
    * Clé de la propriété dans le fichier {@link #NOM_FICHIER_PROP} qui contient
    * l'URL du service web SAE
    */
   private static final String CLE_URL_SERVICE = "server.url";

   /**
    * Identifiant du Contrat de Service à utiliser dans le Vecteur
    * d'Identification
    */
   private static final String VI_ID_CONTRAT_SERVICE = "CS_DEV_TOUTES_ACTIONS";

   /**
    * PAGM à utiliser dans le Vecteur d'Identification
    */
   private static final List<String> VI_PAGMS = Arrays
         .asList("PAGM_TOUTES_ACTIONS");

   /**
    * Lecture dans le fichier properties de l'URL pointant sur le service web
    * SAE
    * 
    * @return URL du service web SAE
    */
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

      // Lecture du l'URL du service web du SAE depuis le fichier properties
      String urlSaeService = litUrlSaeService();

      // Création d'une configuration Axis2 par défaut
      ConfigurationContext configContext;
      try {
         configContext = ConfigurationContextFactory
               .createConfigurationContextFromFileSystem(null, null);
      } catch (AxisFault e) {
         throw new DemoRuntimeException(e);
      }

      // Création de l'objet Stub
      SaeServiceStub service;
      try {
         service = new SaeServiceStub(configContext, urlSaeService);
      } catch (AxisFault e) {
         throw new DemoRuntimeException(e);
      }

      // Récupération de l'objet exposant le PKCS#12
      // Dans un cas "normal", on récupère l'instance créée au démarrage
      // de l'application.
      KeyStoreInterface keystore = new MyKeyStore();

      // Instancie l'objet de génération du VI
      VIHandler handler = new VIHandler(keystore, VI_PAGMS,
            VI_ID_CONTRAT_SERVICE);

      // Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
      AxisConfiguration axisConfig = configContext.getAxisConfiguration();
      List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
      Phase messageOut = findPhaseByName(outFlowPhases, "MessageOut");
      messageOut.addHandler(handler);

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
