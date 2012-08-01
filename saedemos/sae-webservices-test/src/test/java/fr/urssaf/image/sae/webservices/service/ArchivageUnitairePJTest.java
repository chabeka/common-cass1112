package fr.urssaf.image.sae.webservices.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis2.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.webservices.configuration.EcdeManager;
import fr.urssaf.image.sae.webservices.configuration.SecurityConfiguration;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJ;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJRequestType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJRequestTypeChoice_type0;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJResponse;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ArchivageUnitairePJResponseType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.DataFileType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.ListeMetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeCodeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeType;
import fr.urssaf.image.sae.webservices.modele.SaeServiceStub.MetadonneeValeurType;
import fr.urssaf.image.sae.webservices.service.factory.ObjectModelFactory;
import fr.urssaf.image.sae.webservices.service.model.Metadata;
import fr.urssaf.image.sae.webservices.util.AuthenticateUtils;


/**
 * Tests de l'opération "archivageUnitairePJ" pour lesquels on attend une réponse
 * correcte (pas de SoapFault)<br>
 * <br>
 * Il faut penser à configurer son SAE local pour établir une configuration ECDE entre :<br>
 *  - DNS : ecde.cer69.recouv
 *  - Point de montage : REPERTOIRE_TEMP_UTILISATEUR\ecde
 *                       Exemple : C:\DOCUME~1\CER699~1\LOCALS~1\Temp\ecde 
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-webservices.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class ArchivageUnitairePJTest {

   private static final Logger LOG = LoggerFactory
         .getLogger(ArchivageUnitairePJTest.class);

   @Autowired
   @Qualifier("secureStub") 
   private SaeServiceStub service;
   
   @Autowired
   private ArchivageUnitairePJService archivagePJ;

   @Autowired
   private ConsultationService consultation;

   @BeforeClass
   public static void beforeClass() throws ConfigurationException, IOException {

      EcdeManager.cleanEcde();
   }

   @After
   public final void after() {

      SecurityConfiguration.cleanSecurityContext();
   }  
   
   @Test
   public void archivageUnitairePJ_success_AvecContenu_SansMtom() throws URISyntaxException,
         FileNotFoundException, IOException {

      AuthenticateUtils.authenticate("ROLE_TOUS");
      String fileName = "NomFichier.txt";
      byte[] contenu = new byte[20];
      
      String att_hash = DigestUtils.shaHex(contenu);

      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
            "ADELAIDE"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/354"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-01-01"));
      metadatas.add(ObjectModelFactory.createMetadata("Titre",
            "Attestation de vigilance"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", att_hash));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            "1999-11-25"));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            "2011-09-02"));

      
      ArchivageUnitairePJResponseType archivagePJResponse = archivagePJ
            .archivageUnitairePJ(fileName, contenu, metadatas);

      // récupération de l'uuid d'archivage
      String idArchive = archivagePJResponse.getIdArchive().getUuidType();

      LOG.debug("UUID du document archivé: " + idArchive.toLowerCase());

      Assert.assertNotNull("L'identifiant d'archivage doit être renseigné",
            idArchive);

      // vérification du contenu du document et des métadonnées

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("Titre", "Attestation de vigilance");
      expectedMetadatas.put("DateCreation", "2012-01-01");
      expectedMetadatas.put("DateReception", "1999-11-25");
      expectedMetadatas.put("CodeOrganismeGestionnaire", "UR750");
      expectedMetadatas.put("CodeOrganismeProprietaire", "CER69");
      expectedMetadatas.put("CodeRND", "2.3.1.1.12");
      expectedMetadatas.put("NomFichier", "NomFichier.txt");
      expectedMetadatas.put("FormatFichier", "fmt/354");
      expectedMetadatas.put("ContratDeService", "TESTS_UNITAIRES");
      expectedMetadatas.put("Hash", att_hash);
      expectedMetadatas.put("TailleFichier", ""+contenu.length);

      ConsultationUtilsTest consultationTest = new ConsultationUtilsTest();

      consultationTest.assertConsultationResponse(consultation.consultation(idArchive),
            expectedMetadatas);

   }
   
   
   
   @Test
   public void archivageUnitairePJ_success_AvecUrlEcde() throws URISyntaxException,
         FileNotFoundException, IOException {

      AuthenticateUtils.authenticate("ROLE_TOUS");

      // enregistrement du fichier dans l'ECDE
      File srcFile = new File("src/test/resources/storage/attestation.pdf");
      EcdeManager.copyFile(srcFile,
            "DCL001/19991231/3/documents/attestation.pdf");

      // affichage du Hash du fichier
      String hash = DigestUtils.shaHex(new FileInputStream(srcFile));

      LOG
            .debug("le hash du document 'attestation.pdf' à archiver est: "
                  + hash);

      // appel du service archivage unitaire

      List<Metadata> metadatas = new ArrayList<Metadata>();

      metadatas.add(ObjectModelFactory.createMetadata("ApplicationProductrice",
            "ADELAIDE"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeProprietaire", "CER69"));
      metadatas.add(ObjectModelFactory.createMetadata(
            "CodeOrganismeGestionnaire", "UR750"));
      metadatas.add(ObjectModelFactory.createMetadata("CodeRND", "2.3.1.1.12"));
      metadatas.add(ObjectModelFactory.createMetadata("VersionRND", "11.1"));
      metadatas.add(ObjectModelFactory.createMetadata("NbPages", "2"));
      metadatas.add(ObjectModelFactory.createMetadata("FormatFichier",
            "fmt/354"));
      metadatas.add(ObjectModelFactory.createMetadata("DateCreation",
            "2012-01-01"));
      metadatas.add(ObjectModelFactory.createMetadata("Titre",
            "Attestation de vigilance"));
      metadatas.add(ObjectModelFactory.createMetadata("TypeHash", "SHA-1"));
      metadatas.add(ObjectModelFactory.createMetadata("Hash", hash));
      metadatas.add(ObjectModelFactory.createMetadata("DateReception",
            "1999-11-25"));
      metadatas.add(ObjectModelFactory.createMetadata("DateDebutConservation",
            "2011-09-02"));

      URI urlEcde = URI
            .create("ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation.pdf");

      ArchivageUnitairePJResponseType archivageResponse = archivagePJ
            .archivageUnitairePJ(urlEcde, metadatas);

      // récupération de l'uuid d'archivage
      String idArchive = archivageResponse.getIdArchive().getUuidType();

      LOG.debug("UUID du document archivé: " + idArchive.toLowerCase());

      Assert.assertNotNull("L'identifiant d'archivage doit être renseigné",
            idArchive);

      // vérification du contenu du document et des métadonnées

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("Titre", "Attestation de vigilance");
      expectedMetadatas.put("DateCreation", "2012-01-01");
      expectedMetadatas.put("DateReception", "1999-11-25");
      expectedMetadatas.put("CodeOrganismeGestionnaire", "UR750");
      expectedMetadatas.put("CodeOrganismeProprietaire", "CER69");
      expectedMetadatas.put("CodeRND", "2.3.1.1.12");
      expectedMetadatas.put("NomFichier", "attestation.pdf");
      expectedMetadatas.put("FormatFichier", "fmt/354");
      expectedMetadatas.put("ContratDeService", "TESTS_UNITAIRES");
      expectedMetadatas.put("Hash", hash);
      expectedMetadatas.put("TailleFichier", Long.toString(FileUtils
            .sizeOf(srcFile)));

      ConsultationUtilsTest consultationTest = new ConsultationUtilsTest();

      consultationTest.assertConsultationResponse(consultation.consultation(idArchive),
            expectedMetadatas, srcFile);

   }
   
   @Test
   public void archivageUnitairePJ_success_AvecContenu_AvecMtom() throws IOException {
     
      service._getServiceClient().getOptions().setProperty(
            Constants.Configuration.ENABLE_MTOM, Constants.VALUE_TRUE);
      
      AuthenticateUtils.authenticate("ROLE_TOUS");
      String fileName = "NomFichier.txt";
      //byte[] contenu = new byte[20];
      
      // String att_hash = DigestUtils.shaHex(contenu);
      
      String att_hash = "4bf2ddbd82d5fd38e821e6aae434ac989972a043";

      // Creation du Data Handler pour le fichier. 
      DataHandler dataHandler = new DataHandler(
            new FileDataSource(new File("src/test/resources/storage/attestation.pdf")));
      
      DataFileType dataFileType = new DataFileType();
      dataFileType.setFile(dataHandler);
      dataFileType.setFileName(fileName);
      
      ArchivageUnitairePJRequestTypeChoice_type0 requestType = new ArchivageUnitairePJRequestTypeChoice_type0();
      requestType.setDataFile(dataFileType);
      
      ArchivageUnitairePJRequestType request = new ArchivageUnitairePJRequestType();
      request.setArchivageUnitairePJRequestTypeChoice_type0(requestType);
      ListeMetadonneeType listeMetadonneType = createListeMetadonneeType(att_hash);
      request.setMetadonnees(listeMetadonneType);
      
      ArchivageUnitairePJ archivageUnitairePJ = new ArchivageUnitairePJ();
      archivageUnitairePJ.setArchivageUnitairePJ(request);
      
      ArchivageUnitairePJResponse archivagePJResponse = service.archivageUnitairePJ(archivageUnitairePJ);
      
      // récupération de l'uuid d'archivage
      String idArchive = archivagePJResponse.getArchivageUnitairePJResponse().getIdArchive().getUuidType();

      LOG.debug("UUID du document archivé: " + idArchive.toLowerCase());

      Assert.assertNotNull("L'identifiant d'archivage doit être renseigné",
            idArchive);

      // vérification du contenu du document et des métadonnées

      Map<String, Object> expectedMetadatas = new HashMap<String, Object>();

      expectedMetadatas.put("Titre", "Attestation de vigilance");
      expectedMetadatas.put("DateCreation", "2012-01-01");
      expectedMetadatas.put("DateReception", "1999-11-25");
      expectedMetadatas.put("CodeOrganismeGestionnaire", "UR750");
      expectedMetadatas.put("CodeOrganismeProprietaire", "CER69");
      expectedMetadatas.put("CodeRND", "2.3.1.1.12");
      expectedMetadatas.put("NomFichier", "NomFichier.txt");
      expectedMetadatas.put("FormatFichier", "fmt/354");
      expectedMetadatas.put("ContratDeService", "TESTS_UNITAIRES");
      expectedMetadatas.put("Hash", att_hash);
      expectedMetadatas.put("TailleFichier", "73791");

      ConsultationUtilsTest consultationTest = new ConsultationUtilsTest();

      consultationTest.assertConsultationResponse(consultation.consultation(idArchive),
            expectedMetadatas);

   }
   
   private ListeMetadonneeType createListeMetadonneeType(String att_hash) {
      
      ListeMetadonneeType listeMetaType = new ListeMetadonneeType();
      
      MetadonneeType meta = new MetadonneeType();
      MetadonneeCodeType code = new MetadonneeCodeType();
      code.setMetadonneeCodeType("ApplicationProductrice");
      meta.setCode(code);
      MetadonneeValeurType valeur = new MetadonneeValeurType();
      valeur.setMetadonneeValeurType("ADELAIDE");
      meta.setValeur(valeur);
      listeMetaType.addMetadonnee(meta);
      
      MetadonneeType meta2 = new MetadonneeType();
      MetadonneeCodeType code2 = new MetadonneeCodeType();
      code2.setMetadonneeCodeType("CodeOrganismeProprietaire");
      meta2.setCode(code2);
      MetadonneeValeurType valeur2 = new MetadonneeValeurType();
      valeur2.setMetadonneeValeurType("CER69");
      meta2.setValeur(valeur2);
      listeMetaType.addMetadonnee(meta2);
      
      MetadonneeType meta3 = new MetadonneeType();
      MetadonneeCodeType code3 = new MetadonneeCodeType();
      code3.setMetadonneeCodeType("CodeOrganismeGestionnaire");
      meta3.setCode(code3);
      MetadonneeValeurType valeur3 = new MetadonneeValeurType();
      valeur3.setMetadonneeValeurType("UR750");
      meta3.setValeur(valeur3);
      listeMetaType.addMetadonnee(meta3);
      
      MetadonneeType meta4 = new MetadonneeType();
      MetadonneeCodeType code4 = new MetadonneeCodeType();
      code4.setMetadonneeCodeType("CodeRND");
      meta4.setCode(code4);
      MetadonneeValeurType valeur4 = new MetadonneeValeurType();
      valeur4.setMetadonneeValeurType("2.3.1.1.12");
      meta4.setValeur(valeur4);
      listeMetaType.addMetadonnee(meta4);
      
      MetadonneeType meta5 = new MetadonneeType();
      MetadonneeCodeType code5 = new MetadonneeCodeType();
      code5.setMetadonneeCodeType("VersionRND");
      meta5.setCode(code5);
      MetadonneeValeurType valeur5 = new MetadonneeValeurType();
      valeur5.setMetadonneeValeurType("11.1");
      meta5.setValeur(valeur5);
      listeMetaType.addMetadonnee(meta5);
      
      MetadonneeType meta6 = new MetadonneeType();
      MetadonneeCodeType code6 = new MetadonneeCodeType();
      code6.setMetadonneeCodeType("NbPages");
      meta6.setCode(code6);
      MetadonneeValeurType valeur6 = new MetadonneeValeurType();
      valeur6.setMetadonneeValeurType("2");
      meta6.setValeur(valeur6);
      listeMetaType.addMetadonnee(meta6);
      
      MetadonneeType meta7 = new MetadonneeType();
      MetadonneeCodeType code7 = new MetadonneeCodeType();
      code7.setMetadonneeCodeType("FormatFichier");
      meta7.setCode(code7);
      MetadonneeValeurType valeur7 = new MetadonneeValeurType();
      valeur7.setMetadonneeValeurType("fmt/354");
      meta7.setValeur(valeur7);
      listeMetaType.addMetadonnee(meta7);
      
      MetadonneeType meta8 = new MetadonneeType();
      MetadonneeCodeType code8 = new MetadonneeCodeType();
      code8.setMetadonneeCodeType("DateCreation");
      meta8.setCode(code8);
      MetadonneeValeurType valeur8 = new MetadonneeValeurType();
      valeur8.setMetadonneeValeurType("2012-01-01");
      meta8.setValeur(valeur8);
      listeMetaType.addMetadonnee(meta8);
      
      MetadonneeType meta9 = new MetadonneeType();
      MetadonneeCodeType code9 = new MetadonneeCodeType();
      code9.setMetadonneeCodeType("Titre");
      meta9.setCode(code9);
      MetadonneeValeurType valeur9 = new MetadonneeValeurType();
      valeur9.setMetadonneeValeurType("Attestation de vigilance");
      meta9.setValeur(valeur9);
      listeMetaType.addMetadonnee(meta9);
      
      MetadonneeType meta10 = new MetadonneeType();
      MetadonneeCodeType code10 = new MetadonneeCodeType();
      code10.setMetadonneeCodeType("TypeHash");
      meta10.setCode(code10);
      MetadonneeValeurType valeur10 = new MetadonneeValeurType();
      valeur10.setMetadonneeValeurType("SHA-1");
      meta10.setValeur(valeur10);
      listeMetaType.addMetadonnee(meta10);
      
      MetadonneeType meta11 = new MetadonneeType();
      MetadonneeCodeType code11 = new MetadonneeCodeType();
      code11.setMetadonneeCodeType("Hash");
      meta11.setCode(code11);
      MetadonneeValeurType valeur11 = new MetadonneeValeurType();
      valeur11.setMetadonneeValeurType(att_hash);
      meta11.setValeur(valeur11);
      listeMetaType.addMetadonnee(meta11);
      
      MetadonneeType meta12 = new MetadonneeType();
      MetadonneeCodeType code12 = new MetadonneeCodeType();
      code12.setMetadonneeCodeType("DateReception");
      meta12.setCode(code12);
      MetadonneeValeurType valeur12 = new MetadonneeValeurType();
      valeur12.setMetadonneeValeurType("1999-11-25");
      meta12.setValeur(valeur12);
      listeMetaType.addMetadonnee(meta12);
      
      MetadonneeType meta13 = new MetadonneeType();
      MetadonneeCodeType code13 = new MetadonneeCodeType();
      code13.setMetadonneeCodeType("DateDebutConservation");
      meta13.setCode(code13);
      MetadonneeValeurType valeur13 = new MetadonneeValeurType();
      valeur13.setMetadonneeValeurType("2011-09-02");
      meta13.setValeur(valeur13);
      listeMetaType.addMetadonnee(meta13);
      
      return listeMetaType;
   }
   
   
   
   
}
