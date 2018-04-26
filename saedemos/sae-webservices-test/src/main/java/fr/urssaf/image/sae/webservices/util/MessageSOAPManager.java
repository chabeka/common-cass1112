package fr.urssaf.image.sae.webservices.util;

import java.util.List;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;

import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.service.handler.LogInMessageHandler;

/**
 * Classe de gestion des messages SOAP
 * 
 *
 */

public final class MessageSOAPManager {

   private MessageSOAPManager() {
      
   }
   
   /**
    * Log du message SOAP de response
    * 
    * @return SaeServiceStub stub
    * @throws AxisFault 
    */
   public static SaeServiceStub getSOAPResponse() throws AxisFault {
   
      // Création d'une configuration Axis2 par défaut
      ConfigurationContext configContext = 
         ConfigurationContextFactory.createConfigurationContextFromFileSystem(null , null) ;
      
      AxisConfiguration axisConfig = configContext.getAxisConfiguration();
      
      
      List<Phase> inFlowPhases = axisConfig.getInFlowPhases();
      Phase dispatch = findPhaseByName(inFlowPhases,"Dispatch");
      dispatch.addHandler(new LogInMessageHandler());
      
      List<Phase> inFaultPhases = axisConfig.getInFaultFlowPhases();
      dispatch = findPhaseByName(inFaultPhases,"Dispatch");
      dispatch.addHandler(new LogInMessageHandler());
   
      // Création du Stub
      SaeServiceStub service = new SaeServiceStub(configContext);
      
      // Renvoie du Stub
      return service;
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
