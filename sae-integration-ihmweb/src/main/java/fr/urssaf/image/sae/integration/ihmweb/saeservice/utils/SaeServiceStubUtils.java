package fr.urssaf.image.sae.integration.ihmweb.saeservice.utils;

import java.util.List;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.LogInMessageHandler;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.VIHandler;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViService;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;

/**
 * Méthodes utilitaires pour l'instanciation du Stub du service web SaeService
 */
@Component
public final class SaeServiceStubUtils {

   @Autowired
   private ViService viService;

   /**
    * Renvoie le stub du service web, branché sur l'URL fournie en argument, et
    * configuré pour ajouter à l'en-tête SOAP le VI
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param viStyle
    *           le style de VI à intégrer au message SOAP
    * @return le stub
    */
   public SaeServiceStub getServiceStub(String urlServiceWeb,
         ViStyle viStyle) {
      
      return getServiceStub(urlServiceWeb, viStyle, null);
      
   }
   
   /**
    * Renvoie le stub du service web, branché sur l'URL fournie en argument, et
    * configuré pour ajouter à l'en-tête SOAP le VI
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param viStyle
    *           le style de VI à intégrer au message SOAP
    * @param viParams
    *           les paramètres du VI. Si null, alors on construit un VI par défaut. Peut dépendre de viStyle
    * @return le stub
    */
   public SaeServiceStub getServiceStub(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams) {

      try {

         // Création d'une configuration Axis2 par défaut
         ConfigurationContext configContext = 
            ConfigurationContextFactory.createConfigurationContextFromFileSystem(null , null) ;
         
         // ----------------------------------------------
         // Gestion du VI + Log du message SOAP de request
         // ----------------------------------------------
         
         // 1) Ajout de 2 propriétés dans lesquelles on met le style du fichier de VI
         //    à inclure dans le message SOAP ainsi que les propriétés éventuelles.
         //    L'inclusion sera faite dans un handler
         configContext.setProperty(VIHandler.PROP_STYLE_VI, viStyle);
         configContext.setProperty(VIHandler.PROP_PARAMS_VI, viParams);
         
         // 2) Ajout d'un Handler lors de la phase "MessageOut" pour insérer le VI
         AxisConfiguration axisConfig = configContext.getAxisConfiguration();
         List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
         Phase messageOut = findPhaseByName(outFlowPhases,"MessageOut");
         messageOut.addHandler(new VIHandler(viService));
         
         
         // ----------------------------------------------
         // Log du message SOAP de response
         // ----------------------------------------------
         
         List<Phase> inFlowPhases = axisConfig.getInFlowPhases();
         Phase dispatch = findPhaseByName(inFlowPhases,"Dispatch");
         dispatch.addHandler(new LogInMessageHandler());
         
         List<Phase> inFaultPhases = axisConfig.getInFaultFlowPhases();
         dispatch = findPhaseByName(inFaultPhases,"Dispatch");
         dispatch.addHandler(new LogInMessageHandler());
         
         
         // Création du Stub
         SaeServiceStub service = new SaeServiceStub(configContext,
               urlServiceWeb);
         
         // Renvoie du Stub
         return service;

      } catch (Exception e) {
         throw new IntegrationRuntimeException(e);
      }

   }

   /**
    * Renvoie le stub du service web, branché sur l'URL fournie en argument, et
    * configuré pour ajouter à l'en-tête SOAP le VI ne contenant pas les
    * informations d'authentification.
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @return le stub
    */
   public SaeServiceStub getServiceStubSansVi(String urlServiceWeb) {
      return getServiceStub(urlServiceWeb, ViStyle.VI_SANS);
   }

   /**
    * Renvoie le stub du service web, branché sur l'URL fournie en argument, et
    * configuré pour ajouter à l'en-tête SOAP le VI contenant des éléments
    * valides pour l'authentification.
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @return le stub
    */
   public SaeServiceStub getServiceStubAvecViOk(String urlServiceWeb) {
      return getServiceStub(urlServiceWeb, ViStyle.VI_OK);
   }
   
   
   
   private static Phase findPhaseByName(List<Phase> phases, String nomPhaseRecherchee) {
      
      Phase result = null;
      
      for(Phase phase: phases) {
         if (phase.getName().equals(nomPhaseRecherchee)) {
            result = phase;
            break;
         }
      }
      
      return result;
      
   }

}
