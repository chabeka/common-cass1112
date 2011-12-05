package fr.urssaf.image.sae.ecde.service.impl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeBadSummaryException;
import fr.urssaf.image.sae.ecde.exception.EcdeGeneralException;
import fr.urssaf.image.sae.ecde.exception.EcdeInvalidBatchModeException;
import fr.urssaf.image.sae.ecde.modele.sommaire.Sommaire;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;
import fr.urssaf.image.sae.ecde.service.SommaireService;

/**
 * Classe permettant de tester l'implémentation
 * des méthodes de la classe SommaireXmlServiceImpl
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
public class SommaireServiceImplTest {

   @Autowired
   public EcdeSources ecdeSources;
         
   private static File repertoireFinal = new File("");
   
   @Autowired
   private SommaireService sommaireService;
   
   /**
    * Creation de l'arborescence dans le rep temp de l'OS
    * 
    * @throws URISyntaxException
    * @throws IOException
    */
   private void createAbo() throws URISyntaxException, IOException {
      File repertoireTemp = new File("");
      
      ecdeSources.getSources();
      for (EcdeSource ecde : ecdeSources.getSources()) {
         if ("ecde.testunit.recouv".equals(ecde.getHost())) {
            repertoireTemp = ecde.getBasePath();
         }
      }
      repertoireFinal = new File(repertoireTemp, "/1/20110101/3/");
      FileUtils.forceMkdir(repertoireFinal);
   }
   
   /**
    * Creation du chemin de fichier du sommaire.xml
    * 
    * @param sommaire
    * @param som
    * @throws URISyntaxException
    * @throws IOException
    */
   private void createSom(String sommaire) throws URISyntaxException, IOException {
      ClassPathResource classPath = new ClassPathResource(sommaire);
      File sommaire1 = classPath.getFile();
      
      File somCopy1 = new File(repertoireFinal, "sommaire.xml");
      FileUtils.copyFile(sommaire1, somCopy1);
      
      somCopy1.createNewFile();
      somCopy1.exists();
   }
   
   /**
    * Creation du chemin de fichier du repertoire documents
    * @param sommaire
    * @param som
    * @throws URISyntaxException
    * @throws IOException
    */
   private void createFileTemp() throws URISyntaxException, IOException {
      
      ClassPathResource classPath = new ClassPathResource("/1/20110101/3/documents/repertoire/testunitaire.txt");
      File sommaire1 = classPath.getFile();
     
      File rep = new File(repertoireFinal.getAbsolutePath() + System.getProperty("file.separator") + "documents" + System.getProperty("file.separator"), "repertoire");
      FileUtils.forceMkdir(rep);
      rep.exists();
      
      File somCopy1 = new File(rep.getAbsolutePath() + System.getProperty("file.separator") + "testunitaire.txt");
      somCopy1.createNewFile();
      FileUtils.copyFile(sommaire1, somCopy1);
               
      somCopy1.exists();
   }
   
// Le test doit echouer car notre objet numérique a un fichier qui n'existe pas d'ou fichier introuvable
// Le fichier resultats.xml est généré mais contenant un message d'erreur.
// Ici le sommaire.xml contient plusieurs documents en erreur mais on s'arrete des la premiere erreur pour afficher l'erreur   
   @Test(expected = EcdeBadSummaryException.class)
   public void fetchSommaireByUriFailurePlsrFileNotFound() throws EcdeGeneralException, URISyntaxException, IOException  {
      URI uri = new URI("ecde", "ecde.testunit.recouv", getSommaire(), null);
      createAbo();
      createSom("/sommaire/sommaire-test001.xml");
      sommaireService.fetchSommaireByUri(uri);
   }
// Le test doit echouer car notre objet numérique a un fichier qui n'existe pas d'ou fichier introuvable
// Le fichier resultats.xml est généré mais contenant un message d'erreur.
// Ici le sommaire.xml contient un document en erreur   
   @Test(expected = EcdeBadSummaryException.class)
   public void fetchSommaireByUriFailureOneFileNotFound() throws EcdeGeneralException, URISyntaxException, IOException  {
      URI uri = new URI("ecde", "ecde.testunit.recouv", getSommaire(), null);
      createAbo();
      createSom("/sommaire/sommaire-test001Bis.xml");
      sommaireService.fetchSommaireByUri(uri);
   }
   
   
// Verification que l'objet resultat cree est correct.
   @Test
   public void fetchSommaireByUriSuccess() throws EcdeGeneralException, URISyntaxException, IOException  {
      
      
      createAbo();
      
      createSom("/sommaire/sommaire-test003.xml");
      
      URI uri = new URI("ecde", "ecde.testunit.recouv", getSommaire(), null);
      
      createFileTemp();    
      
      Sommaire sommaire = sommaireService.fetchSommaireByUri(uri);
      
      assertEquals("L'objet Sommaire n'a pas été crée.", "TOUT_OU_RIEN", sommaire.getBatchMode());
   }
  
   
// Le sommaire XMl n'a pas son batchMode a TOUT ou RIEN
// Erreur genéré SAXParseException : non respect de l'enumeration TOUT_OU_RIEN ou PARTIEL 
// Cette exception est catché et genere une exception de type EcdeBadSummaryException dans le code   
   @Test(expected = EcdeInvalidBatchModeException.class)
   public void fetchSommaireByUriFailureBM() throws EcdeGeneralException, URISyntaxException, IOException  {
      URI uri = new URI("ecde", "ecde.testunit.recouv", getSommaire(), null);
      createAbo();
      createSom("/sommaire/sommaire-test001BM.xml");
      createFileTemp();
      sommaireService.fetchSommaireByUri(uri);
   }
   
// Le sommaire XML ne respecte pas la syntaxe XSD
// Test doit echouer car erreur de structure  car manque nombreDePages 
   @Test(expected = EcdeBadSummaryException.class)
   public void fetchSommaireByUriFailureStructure() throws EcdeGeneralException, URISyntaxException, IOException  {
      URI uri = new URI("ecde", "ecde.testunit.recouv", getSommaire(), null);
      createAbo();
      createSom("/sommaire/sommaire-test001Bis.xml");
      createFileTemp();
      sommaireService.fetchSommaireByUri(uri);
   }
   
// Le sommaire XML contient un objet numérique avec une syntaxe de fichier en "\" -- séparateur de fichier Unix
   @Test
   public void fetchSommaireByUriSuccessFileObjetNum() throws EcdeGeneralException, URISyntaxException, IOException  {
      URI uri = new URI("ecde", "ecde.testunit.recouv", getSommaire(), null);
      createAbo();
      createSom("/sommaire/sommaire-test004.xml");
      createFileTemp();
      sommaireService.fetchSommaireByUri(uri);
   }
   
   // Resultats.xml crée mais avec trois fichiers en erreur
   @Test(expected = EcdeBadSummaryException.class)
   public void fetchSommaireByUriSuccessFilesOnError() throws EcdeGeneralException, URISyntaxException, IOException  {
      URI uri = new URI("ecde", "ecde.testunit.recouv", getSommaire(), null);
      createAbo();
      createSom("/sommaire/sommaire-test007.xml");
      createFileTemp();
      sommaireService.fetchSommaireByUri(uri);
   }

   /**
    * Recupere le chemain du fichier sommaire.xml
    */
   private static String getSommaire() {
      return "/1/20110101/3/sommaire.xml";
   }

}
