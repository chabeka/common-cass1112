package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.axis2.AxisFault;
import org.junit.Test;

import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitaire;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitaireResponse;


public class ArchivageUnitaireTest {

   
   /**
    * Exemple de consommation de l'opération archivageUnitaire du service web SaeService<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void archivageUnitaire_success() throws RemoteException {
      
      // Pré-requis pour le fichier à archiver :
      //  - Un répertoire de traitement a été créé dans l'ECDE dans la bonne arborescence
      //    par l'application cliente.
      //    Dans cet exemple :
      //      [RacineEcdeDuMontageNfsCoteClient]/le_contrat_service/20120120/Traitement001_ArchivageUnitaire/
      //  - Le fichier à archiver a été déposé dans le sous-répertoire "documents"
      //    de ce répertoire de traitement
      //    Dans cet exemple :
      //     [RacineEcdeDuMontageNfsCoteClient]/le_contrat_service/20120120/Traitement001_ArchivageUnitaire/documents/doc1.PDF
      //
      // L'URL ECDE correspondant à ce fichier "doc1.PDF" est :
      //  => ecde://cer69-ecdeint.cer69.recouv/le_contrat_service/20120120/Traitement001_ArchivageUnitaire/documents/doc1.PDF
      
      // URL ECDE du fichier à archiver
      String urlEcdeFichier = "ecde://cer69-ecdeint.cer69.recouv/le_contrat_service/20120120/Traitement001_ArchivageUnitaire/documents/doc1.PDF";
      
      // Métadonnées associées au document à archiver
      HashMap<String,String> metadonnees = new HashMap<String,String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "UR750");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      // Des métadonnées spécifiables à l'archivage
      metadonnees.put("Siren","123456789");
      // ...
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageUnitaire, 
      //  avec les objets modèle générés par Axis2.
      ArchivageUnitaire paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageUnitaire(
            urlEcdeFichier,metadonnees);
      
      // Appel de l'opération archivageUnitaire
      ArchivageUnitaireResponse reponse = saeService.archivageUnitaire(paramsEntree);
      
      // Affichage de l'identifiant unique d'archivage dans la console
      String idUniqueArchivage = reponse.getArchivageUnitaireResponse().getIdArchive().toString();
      System.out.println(idUniqueArchivage);
      
   }
   
   
   
   /**
    * Exemple de consommation de l'opération archivageUnitaire du service web SaeService<br>
    * <br>
    * Cas avec erreur : La métadonnée obligatoire CodeRND est omise<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:CaptureMetadonneesArchivageObligatoire</li>
    *    <li>Message : La ou les métadonnées suivantes, obligatoires lors de l'archivage, ne sont pas renseignées : CodeRND</li>
    * </ul>
    */
   @Test
   public void archivageUnitaire_failure() {
      
      // URL ECDE du fichier à archiver
      String urlEcdeFichier = "ecde://cer69-ecdeint.cer69.recouv/le_contrat_service/20120120/Traitement001_ArchivageUnitaire/documents/doc1.PDF";
      
      // Métadonnées associées au document à archiver
      HashMap<String,String> metadonnees = new HashMap<String,String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "UR750");
      // Pour provoquer l'erreur, on ne spécifie pas le CodeRND
      // metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageUnitaire, 
      //  avec les objets modèle générés par Axis2.
      ArchivageUnitaire paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageUnitaire(
            urlEcdeFichier,metadonnees);
      
      // Appel de l'opération archivageUnitaire
      try {
         
         // Appel de l'opération archivageUnitaire
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.archivageUnitaire(paramsEntree);
         
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
               "CaptureMetadonneesArchivageObligatoire",
               "La ou les métadonnées suivantes, obligatoires lors de l'archivage, ne sont pas renseignées : CodeRND");
         
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
      
   }
   
   
}
