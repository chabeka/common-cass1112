package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.DeblocageFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.DeblocageResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Deblocage;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.DeblocageResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;

@Service
public class DeblocageTestService {
   
   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;
   
   private DeblocageResultat appelWsOpDeblocage(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         DeblocageFormulaire formulaire, WsTestListener wsListener) {
      
      DeblocageResponse  response = null;
      DeblocageResultat res = null;
      
   // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);
      
   // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelDeblocage(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);
      
      try {
         
         Deblocage paramsService = SaeServiceObjectFactory.buildDeblocageRequest(formulaire.getIdJob().get(0));
         
         response = service.deblocage(paramsService);
         
         res = fromDeblocage(response);
         
      // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
         
      // Log de la réponse obtenue
         log.appendLogLn("Détails de la réponse obtenue de l'opération \"deblocage\" :");
         SaeServiceLogUtils.logResultatDeblocage(resultatTest, res);

         
      }catch (AxisFault fault) {
         // Appel du listener
         wsListener.onSoapFault(resultatTest, fault, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
    } 
         catch (RemoteException e) {
         // Appel du listener
         wsListener.onRemoteException(resultatTest, e, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
     }

      // Ajoute le timestamp en 1ère ligne du log
      log.insertTimestamp();

      // Renvoi du résultat
      return res;
      
   }
   
   public final DeblocageResultat appelWsOpDeblocageTestLibre(String urlServiceWeb,
         DeblocageFormulaire formulaire, ViFormulaire viParams) {
      
      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      return appelWsOpDeblocage(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire,testLibre);
      
   }
   
   private DeblocageResultat fromDeblocage(DeblocageResponse response){
      
      DeblocageResultat result = new DeblocageResultat();
      result.setEtat(response.getDeblocageResponse().getEtat());
      result.setIdTraitement(response.getDeblocageResponse().getUuid());
     
      return result;
    
   }

}
