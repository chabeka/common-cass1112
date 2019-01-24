package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasse;
import sae.client.demo.webservice.modele.SaeServiceStub.ModificationMasseResponse;

public class ModificationMasseTest {


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
      // String urlEcdeSommaire =
      // "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml";
      String urlEcdeSommaire = "ecde://cnp69devecde.cer69.recouv/doc_5000_AUG/CS_DEV_TOUTES_ACTIONS/20170426/Traitement001_ModificationMasse_passant/sommaire.xml";

      // Hash SHA-1 du fichier sommaire.xml
      String typeHash = "SHA-1";
      String hash = "29ff24a0ec2474463f1c904ddf1e8a3c671198e9";
      String codeTraitement = "UR827";

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash, 
      //  avec les objets modèle générés par Axis2.
      ModificationMasse paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeModificationMasse(urlEcdeSommaire, typeHash,
                  hash, codeTraitement);

      // Appel de l'opération archivageMasseAvecHash
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      ModificationMasseResponse reponse = saeService
            .modificationMasse(paramsEntree);
      String idTraitementSae = reponse.getModificationMasseResponse().getUuid();

      // sysout
      System.out.println("La demande de prise en compte de l'archivage de masse a été envoyée");
      System.out.println("URL ECDE du sommaire.xml : " + urlEcdeSommaire);
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
    */
   @Test
   public void modificationMasse_failure() {

      // URL ECDE du fichier sommaire.xml
      // String urlEcdeSommaire =
      // "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement002_ArchivageMasse/sommaire.xml";
      String urlEcdeSommaire = "ecde://cnp69devecde.cer69.recouv/doc_5000_AUG/CS_DEV_TOUTES_ACTIONS/20170426/Traitement001_ModificationMasse_passant/sommaire.xml";

      // Hash SHA-1 du fichier sommaire.xml.
      // Le hash est faux
      String typeHash = "SHA-1";
      String hash = "HASHPASBON";
      String codeTraitement = "UR827";

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash, 
      //  avec les objets modèle générés par Axis2.
      ModificationMasse paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeModificationMasse(urlEcdeSommaire, typeHash,
                  hash, codeTraitement);

      // Appel de l'opération modificationMasse
      try {

         // Appel de l'opération modificationMasse
         saeService.modificationMasse(paramsEntree);

         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");

      } catch (AxisFault fault) {

         // sysout
         TestUtils.sysoutAxisFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
               fault,
               "urn:sae:faultcodes",
               "sae",
               "HashSommaireIncorrect",
               "Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : 29ff24a0ec2474463f1c904ddf1e8a3c671198e9 (type de hash : SHA-1)");

      } catch (RemoteException exception) {

         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);

      }

   }

}
