package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;

import org.apache.axis2.AxisFault;
import org.junit.BeforeClass;
import org.junit.Test;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.ArchivageUnitairePJTest;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.TransfertMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.TransfertMasseResponse;

public class TransfertMasseTest {

   /**
    * Nom du fichier properties contenant l'URL du service web SAE
    */
   private static final String NOM_FICHIER_PROP = "sae-client-demo-frontal.properties";

   private final static Properties prop = new Properties();

   @BeforeClass
   public static void setUpBeforeClass() {

      try {
         prop.load(ResourceUtils.loadResource(new ArchivageUnitairePJTest(), NOM_FICHIER_PROP));
      }
      catch (final IOException e) {
         throw new DemoRuntimeException(e);
      }
   }
   /**
    * Exemple de consommation de l'opération transfertMasse du service web
    * SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void transfertMasse_success() throws RemoteException {

      // Pré-requis pour le transfert de masse :
      // - Un répertoire de traitement a été créé dans l'ECDE dans la bonne
      // arborescence
      // par l'application cliente.
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20170217/Traitement001_TransfertMasse
      // - Le fichier sommaire.xml a été déposé dans ce répertoire.
      // Exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20170217/Traitement001_TransfertMasse/sommaire.xml
      // - Le hash SHA-1 du sommaire.xml a été calculé
      //
      // L'URL ECDE correspondant au sommaire.xml est :
      // =>
      // ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20170217/Traitement001_TransfertMasse/sommaire.xml
      // Le hash SHA-1 du sommaire.xml est :
      // => 2afa38ae6610eb0507760198f02d81268e269761

      // URL ECDE du fichier sommaire.xml
      //String urlEcdeSommaire = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20170217/Traitement001_TransfertMasse/sommaire.xml";

      // Hash SHA-1 du fichier sommaire.xml
      String typeHash = "SHA-1";
      //String hash = "2afa38ae6610eb0507760198f02d81268e269761";
     // String hash = "b89305051b7f24bb9b2e2f4eceb7476a7b5f6ec6";
      String hash = "ac9eb7d35c02daeb16016709eb1ce30c2d4efbe5";
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération transfertMasse,
      // avec les objets modèle générés par Axis2.
      TransfertMasse paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeTransfertMasse(prop.getProperty("URLECDE_SOM_TRANSMASS"), hash, typeHash);

      // Appel de l'opération tranfertMasse
      // => en attendu, l'identifiant unique de traitement de masse affecté par
      // le SAE
      TransfertMasseResponse reponse = saeService.transfertMasse(paramsEntree);
      String idTraitementSae = reponse.getTransfertMasseResponse().getUuid();

      // sysout
      System.out
            .println("La demande de prise en compte du transfert de masse a été envoyée");
      System.out.println("URL ECDE du sommaire.xml : " + prop.getProperty("URLECDE_SOM_TRANSMASS"));
      System.out.println("Hash SHA-1 du sommaire.xml : " + hash);
      System.out
            .println("Identifiant unique du traitement de masse affecté par le SAE : "
                  + idTraitementSae);

   }
   
   /**
    * Exemple de consommation de l'opération transfertMasse du service web SaeService<br>
    * <br>
    * Cas avec erreur : Le hash du fichier sommaire.xml est incorrect<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:HashSommaireIncorrect</li>
    *    <li>Message : Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : 2afa38ae6610eb0507760198f02d81268e269761 (type de hash : SHA-1)</li>
    * </ul>
    */
   @Test
   public void transfertMasse_failure() {
      
      // URL ECDE du fichier sommaire.xml
      //String urlEcdeSommaire = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20170217/Traitement001_TransfertMasse/sommaire.xml";

      // Hash SHA-1 du fichier sommaire.xml
      String typeHash = "SHA-1";
      String hash = "HASHPASBON";
      
   // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
   // Construction du paramètre d'entrée de l'opération transfertMasse,
      // avec les objets modèle générés par Axis2.
      TransfertMasse paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeTransfertMasse(prop.getProperty("URLECDE_SOM_TRANSMASS"), hash, typeHash);
      
      // Appel de l'opération archivageMasseAvecHash
      try {
      
         // Appel de l'opération archivageMasseAvecHash
         saeService.transfertMasse(paramsEntree);
         
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
         
      } catch (AxisFault fault) {
      
         // sysout
         TestUtils.sysoutAxisFault(fault);
         
         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
               fault,
               "urn:frontal:faultcodes",
               "ns1",
               "sae:HashSommaireIncorrect",
               "Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : ac9eb7d35c02daeb16016709eb1ce30c2d4efbe5 (type de hash : SHA-1)");
         
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }


}
