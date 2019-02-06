package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.RepriseFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.RepriseResultat;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.Reprise;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.RepriseResponse;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;

@Service
public class RepriseTestService {

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;

   private RepriseResultat appelWsOpReprise(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams, RepriseFormulaire formulaire,
         WsTestListener wsListener) {

      RepriseResponse response = null;
      RepriseResultat res = null;

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log de résultat
      SaeServiceLogUtils.logAppelReprise(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      try {

         Reprise paramsService = new Reprise();
         if (formulaire.getIdJob().size() != 0)
            paramsService = SaeServiceObjectFactory
                  .buildRepriseRequest(formulaire.getIdJob().get(0));
         else
            paramsService = SaeServiceObjectFactory.buildRepriseRequest("");

         response = service.reprise(paramsService);

         res = fromReprise(response);

         // Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         // Log de la réponse obtenue
         log.appendLogLn("Détails de la réponse obtenue de l'opération \"reprise\" :");
         SaeServiceLogUtils.logResultatReprise(resultatTest, res);

      } catch (AxisFault fault) {
         // Appel du listener
         wsListener.onSoapFault(resultatTest, fault, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
      } catch (RemoteException e) {
         // Appel du listener
         wsListener.onRemoteException(resultatTest, e, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
      }

      // Ajoute le timestamp en 1ère ligne du log
      log.insertTimestamp();

      return res;
   }

   public final RepriseResultat appelWsOpRepriseTestLibre(String urlServiceWeb,
         RepriseFormulaire formulaire, ViFormulaire viParams) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();

      // Appel de la méthode "générique" de test
      return appelWsOpReprise(urlServiceWeb, ViStyle.VI_OK, viParams,
            formulaire, testLibre);

   }

   private RepriseResultat fromReprise(RepriseResponse response) {

      RepriseResultat result = new RepriseResultat();

      result.setIdTraitement(response.getRepriseResponse().getUuid());

      return result;

   }
}
