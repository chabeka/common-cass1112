package fr.urssaf.image.sae.integration.ihmweb.service.tests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.AjoutNoteFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ViFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTestLog;
import fr.urssaf.image.sae.integration.ihmweb.modele.SoapFault;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.modele.SaeServiceStub.AjoutNote;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.security.ViStyle;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceLogUtils;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceObjectFactory;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.utils.SaeServiceStubUtils;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.WsTestListener;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplLibre;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners.impl.WsTestListenerImplSoapFault;

/**
 * Service de test de l'opération "consultation" du service web SaeService
 */
@SuppressWarnings( { "PMD.CyclomaticComplexity", "PMD.NPathComplexity",
      "PMD.ExcessiveMethodLength" })
@Service
public class AjoutNoteTestService {

   @Autowired
   private SaeServiceStubUtils saeServiceStubUtils;
   
   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   private boolean appelWsOpAjoutNote(String urlServiceWeb,
         ViStyle viStyle, ViFormulaire viParams,
         AjoutNoteFormulaire formulaire, WsTestListener wsListener) {
      
      boolean result = true;

      // Vide le résultat du test précédent
      ResultatTest resultatTest = formulaire.getResultats();
      resultatTest.clear();
      ResultatTestLog log = resultatTest.getLog();
      wsListener.onSetStatusInitialResultatTest(resultatTest);

      // Ajout d'un log appel ws
      SaeServiceLogUtils.logAppelAjoutNote(log, formulaire);

      // Récupération du stub du service web
      SaeServiceStub service = saeServiceStubUtils.getServiceStub(
            urlServiceWeb, viStyle, viParams);

      // Appel du service web et gestion de erreurs
      try {
         
         String idDoc = formulaire.getIdArchivage(); 
         String noteTxt = formulaire.getNote();

         AjoutNote ajoutNoteRequest = 
            SaeServiceObjectFactory.buildAjoutNoteRequest(idDoc, noteTxt);
         
         //-- Appel du service web
         service.ajoutNote(ajoutNoteRequest);

         //-- Appel du listener
         wsListener.onRetourWsSansErreur(resultatTest, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());

         //-- Log de la réponse obtenue
         log.appendLogLn("Détails de la réponse obtenue de l'opération \"ajoutNote\" :");

      } catch (AxisFault fault) {

         // Appel du listener
         wsListener.onSoapFault(resultatTest, fault, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
         
         result = false;
      } catch (RemoteException e) {

         // Appel du listener
         wsListener.onRemoteException(resultatTest, e, service
               ._getServiceClient().getServiceContext()
               .getConfigurationContext(), formulaire.getParent());
         
         result = false;
      }

      //-- Ajoute le timestamp en 1ère ligne du log
      log.insertTimestamp();

      // Renvoie du résultat
      return result;
   }

   /**
    * Test libre de l'appel à l'opération "ajoutnote" du service web
    * SaeService.<br>
    * 
    * @param urlServiceWeb
    *           l'URL du service web SaeService
    * @param formulaire
    *           le formulaire
    */
   public final void appelWsOpAjoutNoteTestLibre(String urlServiceWeb, 
         ViFormulaire viParams, AjoutNoteFormulaire formulaire) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui ne s'attend pas à un quelconque résultat (test libre)
      WsTestListener testLibre = new WsTestListenerImplLibre();
      
      appelWsOpAjoutNote(urlServiceWeb, ViStyle.VI_OK, viParams, formulaire, testLibre);
      
      formulaire.getResultats().getLog().appendLog("Note ajoutée");
      
   }
   
   
   public final void appelWsOpAjoutNoteTestSoapFault(String urlServiceWeb,
         AjoutNoteFormulaire formulaire, ViStyle viStyle, ViFormulaire viParams,
         String idSoapFaultAttendu, final Object[] argsMsgSoapFault) {

      // Création de l'objet qui implémente l'interface WsTestListener
      // et qui s'attend à recevoir une certaine SoapFault
      SoapFault faultAttendue = refSoapFault.findSoapFault(idSoapFaultAttendu);
      WsTestListener wsListener = new WsTestListenerImplSoapFault(faultAttendue,
            argsMsgSoapFault);

      // Appel de la méthode "générique" de test
      appelWsOpAjoutNote(urlServiceWeb, viStyle, viParams, formulaire, wsListener);

   }

   

}
