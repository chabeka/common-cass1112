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
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasseResponse;

public class ModificationMasseTest {

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
    * Exemple de consommation de l'opération modificationMasse du service web
    * SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void modificationMasse_success() throws RemoteException {

      // Pré-requis pour la capture de masse :
      // - Un répertoire de traitement a été créé dans l'ECDE dans la bonne arborescence
      // par l'application cliente.
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse
      // - Le fichier sommaire.xml a été déposé dans ce répertoire.
      // Exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml
      // - Les fichiers à archiver, référencés dans sommaire.xml, ont été déposés dans
      // le sous-répertoire "documents" du répertoire de traitement.
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/documents/attestation1.pdf
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/documents/attestation2.pdf
      // - Le hash SHA-1 du sommaire.xml a été calculé
      //
      // L'URL ECDE correspondant au sommaire.xml est :
      // => ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml
      // Le hash SHA-1 du sommaire.xml est :
      // => bbf4df5e743c1dace7f50034c4f3863d9a9f0d43

      // URL ECDE du fichier sommaire.xml
      // String urlEcdeSommaire =
      // "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml";
      // String urlEcdeSommaire = "ecde://cnp69devecde.cer69.recouv/doc_5000_AUG/CS_DEV_TOUTES_ACTIONS/20170426/Traitement001_ModificationMasse_passant/sommaire.xml";

      // Hash SHA-1 du fichier sommaire.xml
      final String typeHash = prop.getProperty("TYPE_HASH");
      // String hash = "29ff24a0ec2474463f1c904ddf1e8a3c671198e9";
      final String hash = "e314a757969d4dabf57fbe9eee85e1f27fb01f4b";
      final String codeTraitement = "UR827";

      // Construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash,
      // avec les objets modèle générés par Axis2.
      final ModificationMasse paramsEntree = Axis2ObjectFactory
                                                               .contruitParamsEntreeModificationMasse(prop.getProperty("URLECDE_SOM_MODIFMASS_SUCCES"),
                                                                                                      typeHash,
                                                                                                      hash,
                                                                                                      codeTraitement);

      // Appel de l'opération archivageMasseAvecHash
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      final ModificationMasseResponse reponse = saeService
                                                          .modificationMasse(paramsEntree);
      final String idTraitementSae = reponse.getModificationMasseResponse().getUuid();

      // sysout
      System.out.println("La demande de prise en compte de l'archivage de masse a été envoyée");
      System.out.println("URL ECDE du sommaire.xml : " + prop.getProperty("URLECDE_SOM_MODIFMASS_SUCCES"));
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
    * <li>Code : sae:HashSommaireIncorrect</li>
    * <li>Message : Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : bbf4df5e743c1dace7f50034c4f3863d9a9f0d43 (type de hash : SHA-1)</li>
    * </ul>
    */
   @Test
   public void modificationMasse_failure() {

      // URL ECDE du fichier sommaire.xml
      // String urlEcdeSommaire =
      // "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml";
      // String urlEcdeSommaire = "ecde://cnp69devecde.cer69.recouv/doc_5000_AUG/CS_DEV_TOUTES_ACTIONS/20170426/Traitement001_ModificationMasse_passant/sommaire.xml";

      // Hash SHA-1 du fichier sommaire.xml.
      // Le hash est faux
      final String typeHash = prop.getProperty("TYPE_HASH");
      final String hash = "HASHPASBON";
      final String codeTraitement = "UR827";

      // Construction du Stub
      final SaeServiceStub saeService = SaeServiceStubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash,
      // avec les objets modèle générés par Axis2.
      final ModificationMasse paramsEntree = Axis2ObjectFactory
                                                               .contruitParamsEntreeModificationMasse(prop.getProperty("URLECDE_SOM_MODIFMASS_SUCCES"),
                                                                                                      typeHash,
                                                                                                      hash,
                                                                                                      codeTraitement);

      // Appel de l'opération modificationMasse
      try {

         // Appel de l'opération modificationMasse
         saeService.modificationMasse(paramsEntree);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      }
      catch (final AxisFault fault) {

         // sysout
         TestUtils.sysoutAxisFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
                                   fault,
                                   "urn:frontal:faultcodes",
                                   "sae:HashSommaireIncorrect",
                                   "Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : e314a757969d4dabf57fbe9eee85e1f27fb01f4b (type de hash : SHA-1)");

      }
      catch (final RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

}