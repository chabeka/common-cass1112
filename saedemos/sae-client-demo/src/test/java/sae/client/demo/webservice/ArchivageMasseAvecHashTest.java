package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasseAvecHash;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageMasseAvecHashResponse;

public class ArchivageMasseAvecHashTest {

   
   /**
    * Exemple de consommation de l'opération archivageMasseAvecHash du service web SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void archivageMasseAvecHash_success() throws RemoteException {
      
      // Pré-requis pour la capture de masse :
      //  - Un répertoire de traitement a été créé dans l'ECDE dans la bonne arborescence
      //    par l'application cliente.
      //    Dans cet exemple :
      //      [RacineEcdeDuMontageNfsCoteClient]/le_contrat_service/20120120/Traitement004_ArchivageMasseAvecHashAvecHash
      //  - Le fichier sommaire.xml a été déposé dans ce répertoire.
      //    Exemple :
      //      [RacineEcdeDuMontageNfsCoteClient]/le_contrat_service/20120120/Traitement004_ArchivageMasseAvecHash/sommaire.xml
      //  - Les fichiers à archiver, référencés dans sommaire.xml, ont été déposés dans
      //    le sous-répertoire "documents" du répertoire de traitement.
      //    Dans cet exemple :
      //     [RacineEcdeDuMontageNfsCoteClient]/le_contrat_service/20120120/Traitement004_ArchivageMasseAvecHash/documents/attestation1.pdf
      //     [RacineEcdeDuMontageNfsCoteClient]/le_contrat_service/20120120/Traitement004_ArchivageMasseAvecHash/documents/attestation2.pdf
      //  - Le hash SHA-1 du sommaire.xml a été calculé 
      // 
      // L'URL ECDE correspondant au sommaire.xml est :
      //  => ecde://cer69-ecdeint.cer69.recouv/le_contrat_service/20120120/Traitement004_ArchivageMasseAvecHash/sommaire.xml
      // Le hash SHA-1 du sommaire.xml est :
      //  => 97e5ea774a7510384b04d0d10e68da955588e11d
      
      // URL ECDE du fichier sommaire.xml
      String urlEcdeSommaire = "ecde://cer69-ecdeint.cer69.recouv/le_contrat_service/20120120/Traitement004_ArchivageMasseAvecHash/sommaire.xml";
      
      // Hash SHA-1 du fichier sommaire.xml
      String typeHash = "SHA-1";
      String hash = "97e5ea774a7510384b04d0d10e68da955588e11d";
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash, 
      //  avec les objets modèle générés par Axis2.
      ArchivageMasseAvecHash paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageMasseAvecHash(
            urlEcdeSommaire, typeHash, hash);
      
      // Appel de l'opération archivageMasseAvecHash
      // => en attendu, l'identifiant unique de traitement de masse affecté par le SAE
      ArchivageMasseAvecHashResponse reponse =  saeService.archivageMasseAvecHash(paramsEntree);
      String idTraitementSae = reponse.getArchivageMasseAvecHashResponse().getUuid();

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
    *    <li>Message : Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : 97e5ea774a7510384b04d0d10e68da955588e11d (type de hash : SHA-1)</li>
    * </ul>
    */
   @Test
   public void archivageMasse_failure() {
      
      // URL ECDE du fichier sommaire.xml
      String urlEcdeSommaire = "ecde://cer69-ecdeint.cer69.recouv/le_contrat_service/20120120/Traitement004_ArchivageMasseAvecHash/sommaire.xml";
      
      // Hash SHA-1 du fichier sommaire.xml.
      // Le hash est faux
      String typeHash = "SHA-1";
      String hash = "HASHPASBON";
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Construction du paramètre d'entrée de l'opération archivageMasseAvecHash, 
      //  avec les objets modèle générés par Axis2.
      ArchivageMasseAvecHash paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageMasseAvecHash(
            urlEcdeSommaire, typeHash, hash);
      
      // Appel de l'opération archivageMasseAvecHash
      try {
      
         // Appel de l'opération archivageMasseAvecHash
         saeService.archivageMasseAvecHash(paramsEntree);
         
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
               "Le hash du fichier sommaire.xml attendu : HASHPASBON est différent de celui obtenu : 97e5ea774a7510384b04d0d10e68da955588e11d (type de hash : SHA-1)");
         
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
   }
   
}
