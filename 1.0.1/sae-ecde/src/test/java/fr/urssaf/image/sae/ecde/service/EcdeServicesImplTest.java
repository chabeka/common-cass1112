package fr.urssaf.image.sae.ecde.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocumentOnError;
import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.exception.EcdeXsdException;
import fr.urssaf.image.sae.ecde.modele.commun_sommaire_et_resultat.DocumentVirtuelType;
import fr.urssaf.image.sae.ecde.modele.resultats.Resultats;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;

/**
 * Classe permettant de tester que les liens se font bien avec
 * <br>
 * les differents services.
 * 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings({"PMD.MethodNamingConventions","PMD.NcssMethodCount","PMD.ExcessiveMethodLength", "PMD.ExcessiveImports"})
public class EcdeServicesImplTest {

   private static final String MESSAGE_INNATENDU = "message inattendu";
   private static final String ECDE = "ecde";
   private static final String ATTESTATION = "/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";
   private static final String SOMMAIRE = "/DCL001/19991231/3/sommaire.xml";
   private static final String TEST_UNIT = "ecde.testunit.recouv";
   private static final File ATTESTATION_FILE = new File("/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf");
   private static final String SEPARATOR = "://";
   private static final String FILE_SEPARATOR = System.getProperty("file.separator");
   
   private static File repertoireTemp = new File(""), repertoireFinal = new File("");
   private static final String FILE_SOM3 = "/sommaire/sommaire-test003.xml";
   private static final String BATCH_MODE = "TOUT_OU_RIEN";
   private static final String FILE_TEMP = "/1/20110101/3/documents/repertoire/testunitaire.txt";
   private static final String SOM = "/sommaire.xml";
   private static final String SOM_SOM = "/1/20110101/3/sommaire.xml";
   
   @Autowired
   private EcdeServices ecdeServices;
   @Autowired
   public EcdeSources ecdeSources;
   
   public static Resultats resultat = new Resultats();
   private static String ecdeDirectory = "";
   
   
   private void createAbo() throws URISyntaxException, IOException {
      
      ecdeSources.getSources();
      for (EcdeSource ecde : ecdeSources.getSources()) {
         if (TEST_UNIT.equals(ecde.getHost())) {
            repertoireTemp = ecde.getBasePath();
         }
      }
      repertoireFinal = new File(repertoireTemp.getAbsolutePath() + "/1/20110101/3/");
      ecdeDirectory = repertoireFinal.getAbsolutePath();
      FileUtils.forceMkdir(repertoireFinal);
   }
   private static void createSom(String sommaire, String som) throws URISyntaxException, IOException {
      ClassPathResource classPath = new ClassPathResource(sommaire);
      File sommaire1 = classPath.getFile();
      
      File somCopy1 = new File(repertoireFinal.getAbsolutePath() + som);
      FileUtils.copyFile(sommaire1, somCopy1);
      
      somCopy1.createNewFile();
   }
   private static void createFileTemp() throws URISyntaxException, IOException {
      
      ClassPathResource classPath = new ClassPathResource(FILE_TEMP);
      File sommaire1 = classPath.getFile();
     
      File rep = new File(repertoireFinal.getAbsolutePath() + FILE_SEPARATOR + "documents" + FILE_SEPARATOR + "repertoire");
      FileUtils.forceMkdir(rep);
      
      
      File somCopy1 = new File(rep.getAbsolutePath() + FILE_SEPARATOR + "testunitaire.txt");
      FileUtils.copyFile(sommaire1, somCopy1);
      
      somCopy1.createNewFile();      
   }
   
   private static void initialiseResultats() {
      
      resultat.setBatchMode("TOUT_OU_RIEN");
      resultat.setEcdeDirectory(ecdeDirectory);
      resultat.setInitialDocumentsCount(1);
      resultat.setInitialVirtualDocumentsCount(0);
      resultat.setIntegratedDocumentsCount(1);
      resultat.setIntegratedVirtualDocumentsCount(0);
      resultat.setNonIntegratedDocuments(new ArrayList<UntypedDocumentOnError>());
      resultat.setNonIntegratedVDocuments(new ArrayList<DocumentVirtuelType>());
      resultat.setNonIntegratedDocumentsCount(0);
      resultat.setNonIntegratedVirtualDocumentsCount(0);
      
   }
   
   
   @Test
   public void convertFileToURITest() throws EcdeBadFileException {
      URI uri = ecdeServices.convertFileToURI(ATTESTATION_FILE);
      String resultatAttendu = "ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";
      String resultatObtenu = uri.getScheme() + SEPARATOR + uri.getAuthority() + uri.getPath();
      
      assertEquals(MESSAGE_INNATENDU, resultatAttendu, resultatObtenu);
   }

   @Test
   public final void convertSommaireToFileTest() throws EcdeBadURLException, EcdeBadURLFormatException, URISyntaxException {
      
      URI uri = new URI(ECDE, "ecde.cer69.recouv", SOMMAIRE, null);
      File messageObtenu = ecdeServices.convertSommaireToFile(uri);
      File messageAttendu = new File("/ecde/ecde_lyon/DCL001/19991231/3/sommaire.xml");
      
      assertEquals(MESSAGE_INNATENDU, messageObtenu, messageAttendu);
   }

   @Test
   public void convertURIToFileTest() throws EcdeBadURLException, EcdeBadURLFormatException, URISyntaxException {
      
      URI uri = new URI(ECDE, "ecde.cer69.recouv", ATTESTATION, null);
      File messageObtenu = ecdeServices.convertURIToFile(uri);
      File messageAttendu = new File("/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf");
      
      assertEquals(MESSAGE_INNATENDU, messageObtenu, messageAttendu);
   }

   @Test
   public void fetchSommaireByUriTest() throws EcdeGeneralException, URISyntaxException, IOException {
      
      URI uri2 = new URI(ECDE, TEST_UNIT, SOM_SOM, null);
      createAbo();
      createSom(FILE_SOM3, SOM);
      createFileTemp();
      Sommaire sommaire = ecdeServices.fetchSommaireByUri(uri2);
      assertEquals(MESSAGE_INNATENDU, BATCH_MODE, sommaire.getBatchMode());
   }

   @Test
   public void persistResultatTest() throws EcdeXsdException, IOException, URISyntaxException {
      
      createAbo();
      initialiseResultats();
      ecdeServices.persistResultat(resultat);
      File resultatXml = new File(repertoireFinal + FILE_SEPARATOR + "resultats.xml");
      assertEquals("Fichier non Existant!", true, resultatXml.exists());
   }
   
   
}
