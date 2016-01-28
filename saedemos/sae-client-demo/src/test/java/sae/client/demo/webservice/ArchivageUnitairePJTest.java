package sae.client.demo.webservice;

import static org.junit.Assert.fail;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.junit.Test;

import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJ;
import sae.client.demo.webservice.modele.SaeServiceStub.ArchivageUnitairePJResponse;



public class ArchivageUnitairePJTest {

   /**
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService,
    * en passant en paramètre d'entrée une URL ECDE<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void archivageUnitairePJ_avecUrlEcde_success() throws RemoteException {
      
      // Pré-requis pour le fichier à archiver :
      //  - Un répertoire de traitement a été créé dans l'ECDE dans la bonne arborescence
      //    par l'application cliente.
      //    Dans cet exemple :
      //      [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/
      //  - Le fichier à archiver a été déposé dans le sous-répertoire "documents"
      //    de ce répertoire de traitement
      //    Dans cet exemple :
      //     [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/documents/doc1.pdf
      //
      // L'URL ECDE correspondant à ce fichier "doc1.PDF" est :
      //  => ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/documents/doc1.pdf
      
      // URL ECDE du fichier à archiver
      String urlEcdeFichier = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/documents/doc1.pdf";
      
      // Métadonnées associées au document à archiver
      HashMap<String,String> metadonnees = new HashMap<String,String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "CER69");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      // Des métadonnées spécifiables à l'archivage
      metadonnees.put("Siren","123456777");
      // ...
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageUnitairePJ, 
      //  avec les objets modèle générés par Axis2.
      ArchivageUnitairePJ paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageUnitairePJavecUrlEcde(
            urlEcdeFichier,metadonnees);
      
      // Appel de l'opération archivageUnitairePJ
      ArchivageUnitairePJResponse reponse = saeService.archivageUnitairePJ(paramsEntree);
      
      // Affichage de l'identifiant unique d'archivage dans la console
      String idUniqueArchivage = reponse.getArchivageUnitairePJResponse().getIdArchive().toString();
      System.out.println(idUniqueArchivage);
      
   }
   
   
   
   
   /**
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService,
    * en passant en paramètre d'entrée un contenu, sans activer l'optimisation MTOM<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void archivageUnitairePJ_avecContenu_sansMtom_success() throws RemoteException {

      archivageUnitairePJ_avecContenu_success(false);
      
   }
   
   
   
   
   /**
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService,
    * en passant en paramètre d'entrée un contenu, en activant l'optimisation MTOM<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException 
    */
   @Test
   public void archivageUnitairePJ_avecContenu_avecMtom_success() throws RemoteException {

      archivageUnitairePJ_avecContenu_success(true);
      
   }
   
   
   
   private void archivageUnitairePJ_avecContenu_success(boolean avecMtom) throws RemoteException {
      
      // Fichier à archiver
      String nomFichier = "attestation1234.pdf";
      InputStream contenu = ResourceUtils.loadResource(this,"archivageUnitairePJ/pj1.pdf");
      
      // Métadonnées associées au document à archiver
      HashMap<String,String> metadonnees = new HashMap<String,String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "CER69");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      // Des métadonnées spécifiables à l'archivage
      metadonnees.put("Siren","123456780");
      // ...
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();
      
      // Activation de l'optimisation MTOM si demandée
      if (avecMtom) {
         saeService._getServiceClient().getOptions().setProperty(
               Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
      }

      // Construction du paramètre d'entrée de l'opération archivageUnitairePJ, 
      //  avec les objets modèle générés par Axis2.
      ArchivageUnitairePJ paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageUnitairePJavecContenu(
            nomFichier,contenu,metadonnees);
      
      // Appel de l'opération archivageUnitairePJ
      ArchivageUnitairePJResponse reponse = saeService.archivageUnitairePJ(paramsEntree);
      
      // Affichage de l'identifiant unique d'archivage dans la console
      String idUniqueArchivage = reponse.getArchivageUnitairePJResponse().getIdArchive().toString();
      System.out.println(idUniqueArchivage);
      
   }
   
   
   
   /**
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService<br>
    * <br>
    * Cas avec erreur : La métadonnée obligatoire CodeRND est omise<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    *    <li>Code : sae:CaptureUrlEcdeFichierIntrouvable</li>
    *    <li>Message : Le fichier pointé par l'URL ECDE est introuvable (ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/TraitementInexistant/documents/doc1.pdf)</li>
    * </ul>
    */
   @Test
   public void archivageUnitairePJ_failure() {
      
      // URL ECDE du fichier à archiver
      // L'URL pointe sur un fichier qui n'existe pas
      String urlEcdeFichier = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/TraitementInexistant/documents/doc1.pdf";
      
      // Métadonnées associées au document à archiver
      HashMap<String,String> metadonnees = new HashMap<String,String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "CER69");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      
      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération archivageUnitairePJ, 
      //  avec les objets modèle générés par Axis2.
      ArchivageUnitairePJ paramsEntree = Axis2ObjectFactory.contruitParamsEntreeArchivageUnitairePJavecUrlEcde(
            urlEcdeFichier,metadonnees);
      
      // Appel de l'opération archivageUnitaire
      try {
         
         // Appel de l'opération archivageUnitaire
         // On ne récupère pas la réponse de l'opération, puisqu'on est censé obtenir une SoapFault
         saeService.archivageUnitairePJ(paramsEntree);
         
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
               "CaptureUrlEcdeFichierIntrouvable",
               "Le fichier pointé par l'URL ECDE est introuvable (ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/TraitementInexistant/documents/doc1.pdf)");
         
      } catch (RemoteException exception) {
         
         fail("Une RemoteException a été levée, alors qu'on attendait une AxisFault\r\n" + exception);
         
      }
      
      
   }
   
   
}
