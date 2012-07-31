package fr.urssaf.image.sae.webservices.security;

import java.util.List;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import fr.urssaf.image.sae.vi.service.WebServiceVICreateService;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;

/**
 * Classe de configuration des services sécurisés
 * 
 * 
 */
public final class SecurityConfiguration {

   private WebServiceVICreateService viService;
   
   public SecurityConfiguration(WebServiceVICreateService viService) {
      this.viService = viService;

   }

   // private SaeServiceStub service;

   // private static final String SECURITY_PATH = "src/main/resources/META-INF";

   /**
    * / méthode à appeller dans le before des tests
    * 
    * @return instance de {@link SaeServiceStub}
    */
   // public static SaeServiceStub createSaeServiceStub() {
   //
   // Configuration config;
   // try {
   // config = new PropertiesConfiguration("sae-webservices-test.properties");
   // } catch (ConfigurationException e) {
   // throw new IllegalStateException(e);
   // }
   //
   // try {
   // ConfigurationContext ctx = ConfigurationContextFactory
   // .createConfigurationContextFromFileSystem(SECURITY_PATH,
   // SECURITY_PATH + "/axis2.xml");
   // return new SaeServiceStub(ctx, config.getString("urlServiceWeb"));
   // } catch (AxisFault e) {
   // throw new IllegalStateException(e);
   // }
   //
   // }

   /**
    * méthode à appeller dans le before des tests
    * 
    * @param viService
    *           service d'opérations sur le VI
    * 
    * @return instance de {@link SaeServiceStub}
    * 
    * @return le stub
    */
   public final SaeServiceStub createSaeServiceStub() {

      Configuration config;
      try {
         config = new PropertiesConfiguration("sae-webservices-test.properties");
      } catch (ConfigurationException e) {
         throw new IllegalStateException(e);
      }

      try {

         // Création d'une configuration Axis2 par défaut
         ConfigurationContext configContext = ConfigurationContextFactory
               .createConfigurationContextFromFileSystem(null, null);

         // ----------------------------------------------
         // Gestion du VI + Log du message SOAP de request
         // ----------------------------------------------

         // Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
         AxisConfiguration axisConfig = configContext.getAxisConfiguration();
         List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
         Phase messageOut = findPhaseByName(outFlowPhases, "MessageOut");
         messageOut.addHandler(new SamlTokenHandler(viService));

         // ----------------------------------------------
         // Log du message SOAP de response
         // ----------------------------------------------

         // List<Phase> inFlowPhases = axisConfig.getInFlowPhases();
         // Phase dispatch = findPhaseByName(inFlowPhases,"Dispatch");
         // dispatch.addHandler(new LogInMessageHandler());
         //         
         // List<Phase> inFaultPhases = axisConfig.getInFaultFlowPhases();
         // dispatch = findPhaseByName(inFaultPhases,"Dispatch");
         // dispatch.addHandler(new LogInMessageHandler());

         // Création du Stub
         SaeServiceStub service = new SaeServiceStub(configContext, config
               .getString("urlServiceWeb"));

         // Renvoie du Stub
         return service;

      } catch (Exception e) {
         throw new RuntimeException(e);
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
