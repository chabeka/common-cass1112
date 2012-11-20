package fr.urssaf.image.sae.webservices.security;

import java.util.List;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;

import fr.urssaf.image.sae.vi.service.WebServiceVICreateService;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.util.ConfigurationUtils;

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

   public final SaeServiceStub createSaeServiceStub() {

      // Récupération de l'URL des services web SAE depuis le fichier properties
      String urlServiceWeb = ConfigurationUtils
            .litUrlServiceWebDuFichierProperties();

      // Instanciation du Handler par défaut pour ajouter le VI au message Soap
      SamlTokenHandler samlTokenHandler = new SamlTokenHandler(viService);

      // Appel de la méthode privée pour créer le stub
      return createSaeServiceStubInternal(urlServiceWeb, samlTokenHandler);

   }

   public final SaeServiceStub createSaeServiceStub(String issuer,
         List<String> pagms) {

      // Récupération de l'URL des services web SAE depuis le fichier properties
      String urlServiceWeb = ConfigurationUtils
            .litUrlServiceWebDuFichierProperties();

      // Instanciation du Handler par défaut pour ajouter le VI au message Soap
      SamlTokenHandler samlTokenHandler = new SamlTokenHandler(viService,
            issuer, pagms);

      // Appel de la méthode privée pour créer le stub
      return createSaeServiceStubInternal(urlServiceWeb, samlTokenHandler);

   }

   public final SaeServiceStub createSaeServiceStub(String issuer,
         List<String> pagms, MyKeyStore myKeyStore) {

      // Récupération de l'URL des services web SAE depuis le fichier properties
      String urlServiceWeb = ConfigurationUtils
            .litUrlServiceWebDuFichierProperties();

      // Instanciation du Handler par défaut pour ajouter le VI au message Soap
      SamlTokenHandler samlTokenHandler = new SamlTokenHandler(viService,
            issuer, pagms, myKeyStore);

      // Appel de la méthode privée pour créer le stub
      return createSaeServiceStubInternal(urlServiceWeb, samlTokenHandler);

   }

   public final SaeServiceStub createSaeServiceStubInternal(
         String urlServiceWeb, SamlTokenHandler samlTokenHandler) {

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
         messageOut.addHandler(samlTokenHandler);

         // ----------------------------------------------
         // Log du message SOAP de response
         // ----------------------------------------------

         // List<Phase> inFlowPhases = axisConfig.getInFlowPhases();
         // Phase dispatch = findPhaseByName(inFlowPhases, "Dispatch");
         // dispatch.addHandler(new LogInMessageHandler());
         //
         // List<Phase> inFaultPhases = axisConfig.getInFaultFlowPhases();
         // dispatch = findPhaseByName(inFaultPhases, "Dispatch");
         // dispatch.addHandler(new LogInMessageHandler());

         // Création du Stub
         SaeServiceStub service = new SaeServiceStub(configContext,
               urlServiceWeb);

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
