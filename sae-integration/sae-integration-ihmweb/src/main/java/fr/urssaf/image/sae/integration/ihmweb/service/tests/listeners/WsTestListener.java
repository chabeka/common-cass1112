package fr.urssaf.image.sae.integration.ihmweb.service.tests.listeners;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import fr.urssaf.image.sae.integration.ihmweb.modele.ResultatTest;

/**
 * Ecouteur utilisé de manière générale lors des tests d'appel à une opération du
 * service web SaeService, qui expose les 4 comportements qui diffèrent selon le
 * cas de test
 */
public interface WsTestListener {

   
   /**
    * Définition du status initial du test
    * 
    * @param resultatTest l'objet contenant le résultat du test
    */
   void onSetStatusInitialResultatTest(
         ResultatTest resultatTest);
   
   
   /***
    * Lorsque l'opération renvoie une réponse (pas de SoapFault, pas d'exception quelconque)
    * 
    * @param resultatTest l'objet contenant le résultat du test
    */
   void onRetourWsSansErreur(
         ResultatTest resultatTest);
   
   
   /**
    * Lorsque l'opération renvoie une SoapFault
    * 
    * @param resultatTest l'objet contenant le résultat du test
    * @param faultObtenue la SoapFault levée
    */
   void onSoapFault(
         ResultatTest resultatTest,
         AxisFault faultObtenue);
   
   
   /**
    * Lorsque l'opération renvoie une exception de type RemoteException
    * 
    * @param resultatTest l'objet contenant le résultat du test
    * @param exception l'exception levée
    */
   void onRemoteException(
         ResultatTest resultatTest, 
         RemoteException exception);
   
}
