package sae.client.demo.webservicebyfrontal;

import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Properties;

import org.apache.axis2.Constants;
import org.junit.BeforeClass;
import org.junit.Test;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.webservice.ArchivageUnitairePJTest;
import sae.client.demo.webservice.factory.Axis2ObjectFactory;
import sae.client.demo.webservice.factory.StubFactory;
import sae.client.demo.webservice.modele.SaeServiceStub;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaire;
import sae.client.demo.webservice.modele.SaeServiceStub.StockageUnitaireResponse;

public class StockageUnitaireTest {

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
      // final String urlEcdeFichier = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc.pdf";

      // URL ECDE du fichier au format d'origine
      // final String urlEcdeFichierFormatOrigine = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement003_StockageUnitaire/documents/doc_format_origine.xlsx";

      // Métadonnées associées au document à archiver
      final HashMap<String, String> metadonnees = new HashMap<String, String>();
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
      metadonnees.put("Siren", "123456789");
      // ...

      // Construction du Stub
      final SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

      // Construction du paramètre d'entrée de l'opération stockageUnitaire,
      // avec les objets modèle générés par Axis2.
      final StockageUnitaire paramsEntree = Axis2ObjectFactory
                                                              .contruitParamsEntreeStockageUnitaireAvecUrlEcde(prop.getProperty("URLECDE_FICHIER_DOC1_PDF"),
                                                                                                               prop.getProperty("URLECDE_FICHIER_ARCH_DOC1_ORIGINE"),
                                                                                                               metadonnees);

      // Appel de l'opération stockageUnitaire
      final StockageUnitaireResponse reponse = saeService
                                                         .stockageUnitaire(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueArchivage = reponse.getStockageUnitaireResponse()
                                              .getIdGed()
                                              .toString();
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

   private void stockageUnitaire_avecContenu_success(final boolean avecMtom)
         throws RemoteException {

      // Fichier à archiver
      final String nomFichier = "doc.pdf";
      final InputStream contenu = ResourceUtils.loadResource(this,
                                                             "stockageUnitaire/doc.pdf");

      // Fichier au format d'origine à rattacher
      final String nomFichierFormatOrigine = "doc_format_origine.xlsx";
      final InputStream contenuFormatOrigine = ResourceUtils.loadResource(this,
                                                                          "stockageUnitaire/doc_format_origine.xlsx");

      // Métadonnées associées au document à archiver
      final HashMap<String, String> metadonnees = new HashMap<String, String>();
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
      final SaeServiceStub saeService = StubFactory.createStubAvecAuthentification();

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
      final StockageUnitaire paramsEntree = Axis2ObjectFactory
                                                              .contruitParamsEntreeStockageUnitaireavecContenu(nomFichier,
                                                                                                               contenu,
                                                                                                               nomFichierFormatOrigine,
                                                                                                               contenuFormatOrigine,
                                                                                                               metadonnees);

      // Appel de l'opération stockageUnitaire
      final StockageUnitaireResponse reponse = saeService
                                                         .stockageUnitaire(paramsEntree);

      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueArchivage = reponse.getStockageUnitaireResponse()
                                              .getIdGed()
                                              .toString();
      System.out.println(idUniqueArchivage);

   }

}
