package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Properties;

import javax.xml.ws.soap.SOAPFaultException;

import org.junit.BeforeClass;
import org.junit.Test;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.ArchivageMasseAvecHashRequestType;
import sae.client.demo.webservice.modele.ArchivageMasseAvecHashResponseType;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

public class ArchivageMasseAvecHashTest {

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
    * Exemple de consommation de l'opération archivageMasseAvecHash du service web SaeService<br>
    * <br>
    * Cas sans erreur
    * @throws IOException 
    */
   @Test
   public void archivageMasseAvecHash_success() throws IOException {
      
      // Pré-requis pour la capture de masse :
      //  - Un répertoire de traitement a été créé dans l'ECDE dans la bonne arborescence
      //    par l'application cliente.
      //    Dans cet exemple :
      //      [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse
      //  - Le fichier sommaire.xml a été déposé dans ce répertoire.
      //    Exemple :
      //      [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml
      //  - Les fichiers à archiver, référencés dans sommaire.xml, ont été déposés dans
      //    le sous-répertoire "documents" du répertoire de traitement.
      //    Dans cet exemple :
      //     [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/documents/attestation1.pdf
      //     [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/documents/attestation2.pdf
      //  - Le hash SHA-1 du sommaire.xml a été calculé 
      // 
      // L'URL ECDE correspondant au sommaire.xml est :
      //  => ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml
      // Le hash SHA-1 du sommaire.xml est :
      //  => bbf4df5e743c1dace7f50034c4f3863d9a9f0d43
      
      // URL ECDE du fichier sommaire.xml
      //String urlEcdeSommaire = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml";
      
      // Hash SHA-1 du fichier sommaire.xml
      String typeHash = "SHA-1";
      String hash = "428fea120ddb49eee2a9e2d4fde1b9d26dbd2195";
      
      // Construction du Stub
      SaeService saeService = StubFactory.createStubAvecAuthentification();
      SaeServicePortType port = saeService.getSaeServicePort();
      
      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash, 
      //  avec les objets modèle générés par Axis2.
      ArchivageMasseAvecHashRequestType paramsEntree = new ArchivageMasseAvecHashRequestType();
      paramsEntree.setHash(hash);
      paramsEntree.setTypeHash(typeHash);
      paramsEntree.setUrlSommaire(prop.getProperty("URLECDE_SOM_ARCHIMASS_SUCCES"));
      
      // Appel de l'opération archivageMasseAvecHash
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      ArchivageMasseAvecHashResponseType reponse =  port.archivageMasseAvecHash(paramsEntree);
      String idTraitementSae = reponse.getUuid();

      // sysout
      System.out.println("La demande de prise en compte de l'archivage de masse a été envoyée");
      System.out.println("URL ECDE du sommaire.xml : " + prop.getProperty("URLECDE_SOM_ARCHIMASS_SUCCES"));
      System.out.println("Hash SHA-1 du sommaire.xml : " + hash);
      System.out.println("Identifiant unique du traitement de masse affecté par le SAE : " + idTraitementSae);
      
   }
   
   
   /**
    * Exemple de consommation de l'opération archivageMasse du service web SaeService<br>
    * <br>
    * Cas avec erreur : Le hash du fichier sommaire.xml est incorrect<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:HashSommaireIncorrect</li>
    *    <li>Message : Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : bbf4df5e743c1dace7f50034c4f3863d9a9f0d43 (type de hash : SHA-1)</li>
    * </ul>
    * @throws IOException 
    */
   @Test
   public void archivageMasse_failure()  {
      
      // URL ECDE du fichier sommaire.xml
      //String urlEcdeSommaire = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml";
      
      // Hash SHA-1 du fichier sommaire.xml.
      // Le hash est faux
      String typeHash = "SHA-1";
      String hash = "HASHPASBON";
      
      
      
      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash, 
      //  avec les objets modèle générés par Axis2.
      ArchivageMasseAvecHashRequestType paramsEntree = new ArchivageMasseAvecHashRequestType();
      paramsEntree.setHash(hash);
      paramsEntree.setTypeHash(typeHash);
      paramsEntree.setUrlSommaire(prop.getProperty("URLECDE_SOM_ARCHIMASS_SUCCES"));
      
      // Appel de l'opération archivageMasseAvecHash
      try {
         
      // Construction du Stub
         SaeService saeService = StubFactory.createStubAvecAuthentification();
         SaeServicePortType port = saeService.getSaeServicePort();
         // Appel de l'opération archivageMasseAvecHash
         port.archivageMasseAvecHash(paramsEntree);
         
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
         
      }catch (final SOAPFaultException  e) {
         // TODO Auto-generated catch block
         final SOAPFaultException fault = (SOAPFaultException) e;
      
         // sysout
         TestUtils.sysoutAxisFault(fault);
         
         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
               fault,
               "urn:frontal:faultcodes",
               "ns1",
               "sae:HashSommaireIncorrect",
               "Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : "+ prop.getProperty("HASH_ARCH_MASSE_AVEC_HASH")+" (type de hash : SHA-1)");
         
      } catch (IOException exception) {
         
         fail("Une exception a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }
   
}
