package sae.client.demo.webservicebyfrontal;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPBinding;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import sae.client.demo.exception.DemoRuntimeException;
import sae.client.demo.util.ResourceUtils;
import sae.client.demo.utils.TestUtils;
import sae.client.demo.webservice.factory.SaeServiceStubFactory;
import sae.client.demo.webservice.modele.ArchivageUnitairePJRequestType;
import sae.client.demo.webservice.modele.ArchivageUnitairePJResponseType;
import sae.client.demo.webservice.modele.DataFileType;
import sae.client.demo.webservice.modele.ListeMetadonneeType;
import sae.client.demo.webservice.modele.MetadonneeType;
import sae.client.demo.webservice.modele.SaeService;
import sae.client.demo.webservice.modele.SaeServicePortType;

public class ArchivageUnitairePJTest {

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
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService,
    * en passant en paramètre d'entrée une URL ECDE<br>
    * <br>
    * Cas sans erreur
    *
    * @throws IOException
    */
   @Test
   public void archivageUnitairePJ_avecUrlEcde_success() throws IOException {

      // Pré-requis pour le fichier à archiver :
      // - Un répertoire de traitement a été créé dans l'ECDE dans la bonne arborescence
      // par l'application cliente.
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/
      // - Le fichier à archiver a été déposé dans le sous-répertoire "documents"
      // de ce répertoire de traitement
      // Dans cet exemple :
      // [RacineEcdeDuMontageNfsCoteClient]/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/documents/doc1.pdf
      //
      // L'URL ECDE correspondant à ce fichier "doc1.PDF" est :
      // => ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/documents/doc1.pdf

      // URL ECDE du fichier à archiver
      // final String urlEcdeFichier = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/Traitement001_CaptureUnitaire/documents/doc1.pdf";
      // final String urlEcdeFichier = "ecde://cnp69devecde.cer69.recouv/UFT-TF/20171103/documents/documents/doc1.PDF";
      // Métadonnées associées au document à archiver
      final ArchivageUnitairePJRequestType paramsEntree = getParamsEntree();
      paramsEntree.setEcdeUrl(prop.getProperty("URLECDE_FICHIER_DOC1_PDF"));

      // Ajout entete de securité
      final SaeService saeService = SaeServiceStubFactory.createStubAvecAuthentification();
      // Appel de l'opération archivageUnitairePJ
      final SaeServicePortType port = saeService.getSaeServicePort();
      ArchivageUnitairePJResponseType response;

      response = port.archivageUnitairePJ(paramsEntree);
      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueArchivage = response.getIdArchive();
      System.out.println(idUniqueArchivage);

   }

   /**
    * @return
    */
   private ArchivageUnitairePJRequestType getParamsEntree() {
      final ListeMetadonneeType listMetasType = new ListeMetadonneeType();
      MetadonneeType metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("ApplicationProductrice");
      metaType.setValeur("ADELAIDE");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("CodeOrganismeGestionnaire");
      metaType.setValeur("CER69");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("CodeOrganismeProprietaire");
      metaType.setValeur("CER69");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("CodeRND");
      metaType.setValeur("2.3.1.1.12");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("DateCreation");
      metaType.setValeur("2011-09-01");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("FormatFichier");
      metaType.setValeur("fmt/354");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("Hash");
      metaType.setValeur("a2f93f1f121ebba0faef2c0596f2f126eacae77b");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("NbPages");
      metaType.setValeur("2");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("Titre");
      metaType.setValeur("Attestation de vigilance");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("TypeHash");
      metaType.setValeur("SHA-1");
      listMetasType.getMetadonnee().add(metaType);

      metaType = new MetadonneeType();
      // Métadonnées obligatoires
      metaType.setCode("Siren");
      metaType.setValeur("123456777");
      listMetasType.getMetadonnee().add(metaType);
      // ...

      final ArchivageUnitairePJRequestType paramsEntree = new ArchivageUnitairePJRequestType();
      paramsEntree.setMetadonnees(listMetasType);

      return paramsEntree;
   }

   /**
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService,
    * en passant en paramètre d'entrée un contenu, sans activer l'optimisation MTOM<br>
    * <br>
    * Cas sans erreur
    *
    * @throws IOException
    */
   @Test
   public void archivageUnitairePJ_avecContenu_sansMtom_success() throws IOException {

      archivageUnitairePJ_avecContenu_success(false);

   }

   /**
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService,
    * en passant en paramètre d'entrée un contenu, en activant l'optimisation MTOM<br>
    * <br>
    * Cas sans erreur
    *
    * @throws IOException
    */
   @Test
   public void archivageUnitairePJ_avecContenu_avecMtom_success() throws IOException {

      archivageUnitairePJ_avecContenu_success(true);

   }

   private void archivageUnitairePJ_avecContenu_success(final boolean avecMtom) throws IOException {

      // Métadonnées obligatoires
      final ArchivageUnitairePJRequestType paramsEntree = getParamsEntree();
      paramsEntree.setDataFile(getDataFileType());

      // Construction du Stub
      final SaeService saeService = SaeServiceStubFactory.createStubAvecAuthentification();
      final SaeServicePortType port = saeService.getSaeServicePort();

      // Activation de l'optimisation MTOM si demandée
      if (avecMtom) {
         final BindingProvider bp = (BindingProvider) port;
         // BindingProvider bp declared previously
         final SOAPBinding binding = (SOAPBinding) bp.getBinding();
         binding.setMTOMEnabled(avecMtom);
      }

      ArchivageUnitairePJResponseType response;

      response = port.archivageUnitairePJ(paramsEntree);
      // Affichage de l'identifiant unique d'archivage dans la console
      final String idUniqueArchivage = response.getIdArchive();
      System.out.println(idUniqueArchivage);

   }

   /**
    * Exemple de consommation de l'opération archivageUnitairePJ du service web SaeService<br>
    * <br>
    * Cas avec erreur : La métadonnée obligatoire CodeRND est omise<br>
    * <br>
    * Le SAE renvoie la SoapFault suivante :<br>
    * <ul>
    * <li>Code : sae:CaptureUrlEcdeFichierIntrouvable</li>
    * <li>Message : Le fichier pointé par l'URL ECDE est introuvable (ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/TraitementInexistant/documents/doc1.pdf)</li>
    * </ul>
    *
    * @throws IOException
    */
   @Test
   public void archivageUnitairePJ_failure() {

      // URL ECDE du fichier à archiver
      // L'URL pointe sur un fichier qui n'existe pas
      // final String urlEcdeFichier = "ecde://cnp69intgnsecde.gidn.recouv/CS_DEV_TOUTES_ACTIONS/20120120/TraitementInexistant/documents/doc1.pdf";

      // Métadonnées associées au document à archiver
      final ArchivageUnitairePJRequestType paramsEntree = getParamsEntree();
      paramsEntree.setEcdeUrl(prop.getProperty("URLECDE_FICHIER_ARCH_DOC1_INEXISTANT"));

      try {
         // Ajout entete de securité
         final SaeService saeService = SaeServiceStubFactory.createStubAvecAuthentification();
         // Appel de l'opération archivageUnitairePJ
         final SaeServicePortType port = saeService.getSaeServicePort();
         port.archivageUnitairePJ(paramsEntree);
         // Si on a passé l'appel, le test est en échec
         fail("La SoapFault attendue n'a pas été renvoyée");
      }
      catch (final SOAPFaultException fault) {
         // TODO Auto-generated catch block

         // sysout
         TestUtils.sysoutSoapFault(fault);

         // Vérification de la SoapFault
         TestUtils.assertSoapFault(
                                   fault,
                                   "urn:frontal:faultcodes",
                                   "ns1",
                                   "sae:CaptureUrlEcdeFichierIntrouvable",
                                   "Le fichier pointé par l'URL ECDE est introuvable (" + prop.getProperty("URLECDE_FICHIER_ARCH_DOC1_INEXISTANT") + ")");
      }
      catch (final IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   public DataFileType getDataFileType() {
      // Nom et contenu du fichier
      final String nomFichier = "attestation1234.pdf";
      final InputStream contenu = ResourceUtils.loadResource(this, "archivageUnitairePJ/pj1.pdf");

      final DataFileType dataFile = new DataFileType();
      dataFile.setFileName(nomFichier);
      byte[] contenuBytes;
      try {
         contenuBytes = IOUtils.toByteArray(contenu);
         dataFile.setFile(contenuBytes);
         dataFile.setFileName(nomFichier);
      }
      catch (final IOException e) {
         throw new DemoRuntimeException(e);
      }

      // Renvoie du paramètre d'entrée de l'opération archivageUnitairePJ
      return dataFile;

   }

}
