package fr.urssaf.image.sae.ecde.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.service.EcdeFileService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-sae-ecde-test.xml")
@SuppressWarnings({"PMD.MethodNamingConventions","PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals"})
public class EcdeFileServiceImplTest {
   
   @Autowired
   private EcdeFileService ecdeFileService;
   
   
   // l'url est bien en concordance avec la liste ECDESource donnee en paramètre
   @Test
   public void convertUrlToFile_success() throws URISyntaxException, EcdeBadURLException, EcdeBadURLFormatException {
      
      EcdeSource ecde1,ecde2,ecde3;
      ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
      ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
      ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
      
      URI uri = new URI("ecde", "ecde.cer69.recouv", "/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf", null);
      File messageObtenu = ecdeFileService.convertURIToFile(uri, ecde1, ecde2, ecde3);
      File messageAttendu = new File("/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf");
      
      assertEquals("Le message d'erreur retourné n'est pas correct!", messageObtenu, messageAttendu);
   }
   // exception levée si l'URI est absente de la liste des ECDESources donnee en param
   @Test
   public void convertUrlToFile_failure_UriNotExist() throws EcdeBadURLFormatException, URISyntaxException {
      try {
         EcdeSource ecde1,ecde3;
         ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
         ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
         
         URI uri = new URI("ecde", "ecde.cer69.recouv", "/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf", null);
         ecdeFileService.convertURIToFile(uri, ecde1, ecde3);
         fail("Une exception était attendue! L'exception EcdeBadURLException");
      }catch (EcdeBadURLException e) {
         assertEquals("Le message d'erreur retourné n'est pas correct!","L'URL ECDE ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf n'appartient à aucun ECDE transmis en paramètre du service.",e.getMessage());
      }
   }
   // test pour montrer que la conversion ne fonctionne pas si dans l'URL ne respecte pas le format
   // ecde/authority/numeroCS/dateTraitement/idTraitement/documents/nom_du_fichier -- scheme ne vaut pas ecde
   @Test
   public void convertUrlToFile_failure_BadURLFormat() throws EcdeBadURLException, URISyntaxException {
      try {
         EcdeSource ecde1,ecde2,ecde3;
         ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
         ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
         ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
         
         URI uri = new URI("ecd", "ecde.cer69.recouv", "/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf", null);
         ecdeFileService.convertURIToFile(uri, ecde1, ecde2, ecde3);
         fail("exception attendue!"+EcdeBadURLFormatException.class);
      }catch (EcdeBadURLFormatException e) {
         assertEquals("Le message d'erreur retourné n'est pas correct!","L'URL ECDE ecd://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf est incorrecte.",e.getMessage());
      }      
   } 
   // document n'est pas égal à la constante 'documents'
   @Test
   public void convertUrlToFile_failure_BadURLFormatPath_Document() throws EcdeBadURLException, URISyntaxException {
      try {
         EcdeSource ecde1,ecde2,ecde3;
         ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
         ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
         ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
         
         URI uri = new URI("ecde", "ecde.cer69.recouv", "/DCL001/19991231/3/document/attestation/1990/attestation1.pdf", null);
         ecdeFileService.convertURIToFile(uri, ecde1, ecde2, ecde3);
         fail("exception attendue!"+EcdeBadURLFormatException.class);
      }catch (EcdeBadURLFormatException e) {  
         assertEquals("Le message d'erreur retourné n'est pas correct!","L'URL ECDE ecde://ecde.cer69.recouv/DCL001/19991231/3/document/attestation/1990/attestation1.pdf est incorrecte.",e.getMessage());
      }
   }
   // date traitement ne respecte pas le format AAAAMMJJ erreur sur le mois 13
   @Test(expected = EcdeBadURLFormatException.class)
   public void convertUrlToFile_failure_BadURLFormat_PathDate() throws URISyntaxException, EcdeBadURLException, EcdeBadURLFormatException {
      EcdeSource ecde1,ecde2,ecde3;
      ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
      ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
      ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
      
      URI uri = new URI("ecde", "ecde.cer69.recouv", "/DCL001/19991331/3/documents/attestation/1990/attestation1.pdf", null);
      ecdeFileService.convertURIToFile(uri, ecde1, ecde2, ecde3);
   }
   // date traitement ne respecte pas le format AAAAMMJJ erreur sur le jour 00
   @Test
   public void convertUrlToFile_failure_BadURLFormat_PathDate2() throws EcdeBadURLException, URISyntaxException {
      try {
         EcdeSource ecde1,ecde2,ecde3;
         ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
         ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
         ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
         
         URI uri = new URI("ecde", "ecde.cer69.recouv", "/DCL001/19991200/3/documents/attestation/1990/attestation1.pdf", null);
         ecdeFileService.convertURIToFile(uri, ecde1, ecde2, ecde3);
         fail("exception attendue!"+EcdeBadURLFormatException.class);
      }catch (EcdeBadURLFormatException e) {  
         assertEquals("Le message d'erreur retourné n'est pas correct!",
               "L'URL ECDE ecde://ecde.cer69.recouv/DCL001/19991200/3/documents/attestation/1990/attestation1.pdf est incorrecte.",
               e.getMessage());
      }
   }
   //-------------------------- FILE TO URI -------------------------------------------------
   //-------- Conversion OK  -----------------------------------------------------------
   @Test
   public void convertFileToURITest() throws EcdeBadFileException {
      EcdeSource ecde1,ecde2,ecde3;
      ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
      ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
      ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
      
      URI uri = ecdeFileService.convertFileToURI(new File("/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf"), ecde1, ecde2, ecde3);
      String resultatAttendu = "ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";
      
      
      assertEquals("Le message d'erreur retourné n'est pas correct!", resultatAttendu, uri.toString());
   }
   //----------Chemin de fichier non présent dans ECDE sources donne en param -------------
   @Test(expected = EcdeBadFileException.class)
   public void convertFileToURI_failure_EcdeNotExist() throws EcdeBadFileException {
      EcdeSource ecde1,ecde3;
      ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
      ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
      
      ecdeFileService.convertFileToURI(new File("/ecde/ecde_lyon/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf"), ecde1, ecde3);
   }
   //--------- Conversion OK avec /mnt/ai/ecde/ecde_lyon
   @Test
   public void convertFileToURI_success_2Test() throws EcdeBadFileException {
      EcdeSource ecde1,ecde2,ecde3, ecde4;
      ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
      ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
      ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
      ecde4 = new EcdeSource("ecde.tatoine2.recouv", new File("/mnt/ai/ecde/ecde_lokmen/"));
      
      URI uri = ecdeFileService.convertFileToURI(new File("/mnt/ai/ecde/ecde_lokmen/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf"), ecde1, ecde2, ecde3, ecde4);
      String resultatAttendu = "ecde://ecde.tatoine2.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";
      
      assertEquals("Le message d'erreur retourné n'est pas correct!", resultatAttendu, uri.toString());
   }
   //----------Test avec un nom de fichier avec des \ et des /
   @Test
   public void convertFileToURI_success_SlashTest() throws EcdeBadFileException {
      EcdeSource ecde1,ecde2,ecde3;
      ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
      ecde2 = new EcdeSource("ecde.cer69.recouv", new File("/ecde/ecde_lyon/"));
      ecde3 = new EcdeSource("ecde.tatoine.recouv", new File("/ecde/ecde_tatoine/"));
      
      URI uri = ecdeFileService.convertFileToURI(new File("\\ecde/ecde_lyon/DCL001\\19991231/3/documents/attestation/1990\\attestation1.pdf"), ecde1, ecde2, ecde3);
      
      String resultatAttendu = "ecde://ecde.cer69.recouv/DCL001/19991231/3/documents/attestation/1990/attestation1.pdf";
      
      assertEquals("Le message d'erreur retourné n'est pas correct!", resultatAttendu, uri.toString());
   }
   //-------- nom fichier : /temp/text.txt  ECDESOURCE.BasePath = /temp
   @Test
   public void convertFileToURI_success() throws EcdeBadFileException {
      EcdeSource ecde5 = new EcdeSource("ecde.temp.recouv", new File("/temp/"));
      
      URI uri = ecdeFileService.convertFileToURI(new File("/temp/text.txt"), ecde5);
      
      String resultatAttendu = "ecde://ecde.temp.recouv/text.txt";
      
      assertEquals("Le message d'erreur retourné n'est pas correct!", resultatAttendu, uri.toString());
   }
   //-------- nom fichier : archive/temp/text.txt  ECDESOURCE.BasePath = /temp
   @Test(expected = EcdeBadFileException.class)
   public void convertFileToURI_failure() throws EcdeBadFileException {
      EcdeSource ecde5 = new EcdeSource("ecde.temp.recouv", new File("/temp/"));
      
      ecdeFileService.convertFileToURI(new File("archive/temp/text.txt"), ecde5);
   }
   //-------- nom fichier : archive/temp/text.txt  ECDESOURCE.BasePath = /temp
   @Test
   public void convertFileToURI_failure_badURLFormat() {
      try {
         EcdeSource ecde6 = new EcdeSource("ecde.temp.recouv", new File("temp"));
         
         ecdeFileService.convertFileToURI(new File("archive/temp/text.txt"), ecde6);
         fail("Une exception était attendue! " + EcdeBadFileException.class);
      }catch (EcdeBadFileException e) {
         assertEquals("Le message d'erreur retourné n'est pas correct!","Le chemin du document 'archive"+File.separator+"temp"+File.separator+"text.txt' n'appartient à aucun ECDE transmis en paramètre du service.",e.getMessage());
      }
   }
   //---------- URI ne respectant pas le format RFC3986 -------------
   @Test
   public void convertFileToURI_failure_URIFormat() throws EcdeBadFileException {
         try {
            EcdeSource ecde1, ecde7;
            
            ecde1 = new EcdeSource("ecde.hoth.recouv", new File("/ecde/ecde_host/"));
            ecde7 = new EcdeSource("ecde._.recouv", new File("/ecde_lokmen/ecde_lyon/"));
            
            ecdeFileService.convertFileToURI(new File("/ecde_lokmen/ecde_lyon/DCL001/"), ecde1, ecde7);
            fail("Erreur attendu de type ECDERuntimeException.");
         }catch (EcdeRuntimeException e) {
            assertEquals("exception inattendu", URISyntaxException.class, e.getCause().getClass());
         }
   }   
}