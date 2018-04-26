package sae.client.demo.webservice;

import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;

import org.apache.axis2.Constants;
import org.junit.Test;

import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaire;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaireResponse;

public class StockageUnitaireTest {

   /**
    * Exemple de consommation de l'opération stockageUnitaire du service web
    * SaeService, en passant en paramètre d'entrée des URL ECDE<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void stockageUnitaire_avecUrlEcde_success() throws RemoteException {

      // Pré-requis pour le fichier à archiver :
      // - Un répertoire de traitement a été créé dans l'ECDE dans la bonne
      // arborescence
      // par l'application cliente.
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/
      //
      // - Le fichier à archiver a été déposé dans le sous-répertoire
      // "documents"
      // de ce répertoire de traitement
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc.pdf
      //
      // L'URL ECDE correspondant à ce fichier "doc.PDF" est :
      // =>
      // ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc.pdf
      //
      // - Le fichier au format d'originie a été déposé dans le sous-répertoire
      // "documents"
      // de ce répertoire de traitement
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc_format_origine.xlsx
      //
      // L'URL ECDE correspondant à ce fichier "doc.PDF" est :
      // =>
      // ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc_format_origine.xlsx

      
      // URL ECDE du fichier à archiver
      String urlEcdeFichier = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc.pdf";

      // URL ECDE du fichier au format d'origine
      String urlEcdeFichierFormatOrigine = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc_format_origine.xlsx";

      // Métadonnées associées au document à archiver
      HashMap<String, String> metadonnees = new HashMap<String, String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "CER69");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "e7d6744d466d8126b75ad92aead032b289ab5a15");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      // Des métadonnées spécifiables à l'archivage
      metadonnees.put("Siren", "123456777");
      // ...

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération stockageUnitaire,
      // avec les objets modèle générés par Axis2.
      StockageUnitaire paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeStockageUnitaireAvecUrlEcde(urlEcdeFichier,
                  urlEcdeFichierFormatOrigine, metadonnees);

      // Appel de l'opération stockageUnitaire
      StockageUnitaireResponse reponse = saeService
            .stockageUnitaire(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      String idUniqueArchivage = reponse.getStockageUnitaireResponse()
            .getIdGed().toString();
      System.out.println(idUniqueArchivage);

   }

   /**
    * Exemple de consommation de l'opération stockageUnitaire du service web
    * SaeService, en passant en paramètre d'entrée un contenu, sans activer
    * l'optimisation MTOM<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void stockageUnitaire_avecContenu_sansMtom_success()
         throws RemoteException {

      stockageUnitaire_avecContenu_success(false);

   }

   /**
    * Exemple de consommation de l'opération stockageUnitaire du service web
    * SaeService, en passant en paramètre d'entrée un contenu, en activant
    * l'optimisation MTOM<br>
    * <br>
    * Cas sans erreur
    * 
    * @throws RemoteException
    */
   @Test
   public void stockageUnitaire_avecContenu_avecMtom_success()
         throws RemoteException {

      stockageUnitaire_avecContenu_success(true);

   }

   private void stockageUnitaire_avecContenu_success(boolean avecMtom)
         throws RemoteException {

      // Fichier à archiver
      String nomFichier = "doc.pdf";
      InputStream contenu = ResourceUtils.loadResource(this,
            "stockageUnitaire/doc.pdf");

      // Fichier au format d'origine à rattacher
      String nomFichierFormatOrigine = "doc_format_origine.xlsx";
      InputStream contenuFormatOrigine = ResourceUtils.loadResource(this,
            "stockageUnitaire/doc_format_origine.xlsx");

      // Métadonnées associées au document à archiver
      HashMap<String, String> metadonnees = new HashMap<String, String>();
      // Métadonnées obligatoires
      metadonnees.put("ApplicationProductrice", "ADELAIDE");
      metadonnees.put("CodeOrganismeGestionnaire", "CER69");
      metadonnees.put("CodeOrganismeProprietaire", "CER69");
      metadonnees.put("CodeRND", "2.3.1.1.12");
      metadonnees.put("DateCreation", "2011-09-01");
      metadonnees.put("FormatFichier", "fmt/354");
      metadonnees.put("Hash", "e7d6744d466d8126b75ad92aead032b289ab5a15");
      metadonnees.put("NbPages", "2");
      metadonnees.put("Titre", "Attestation de vigilance");
      metadonnees.put("TypeHash", "SHA-1");
      // Des métadonnées spécifiables à l'archivage
      metadonnees.put("Siren", "123456780");
      // ...

      // Construction du Stub
      SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Activation de l'optimisation MTOM si demandée
      if (avecMtom) {
         saeService
               ._getServiceClient()
               .getOptions()
               .setProperty(Constants.Configuration.ENABLE_MTOM,
                     Constants.VALUE_TRUE);
      }

      // Construction du paramètre d'entrée de l'opération stockageUnitaire,
      // avec les objets modèle générés par Axis2.
      StockageUnitaire paramsEntree = Axis2ObjectFactory
            .contruitParamsEntreeStockageUnitaireavecContenu(nomFichier,
                  contenu, nomFichierFormatOrigine, contenuFormatOrigine,
                  metadonnees);

      // Appel de l'opération stockageUnitaire
      StockageUnitaireResponse reponse = saeService
            .stockageUnitaire(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      String idUniqueArchivage = reponse.getStockageUnitaireResponse()
            .getIdGed().toString();
      System.out.println(idUniqueArchivage);

   }


}
