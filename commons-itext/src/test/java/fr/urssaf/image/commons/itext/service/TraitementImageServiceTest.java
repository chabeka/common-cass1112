package fr.urssaf.image.commons.itext.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.itext.exception.ExtractionException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-itext-test.xml" })
public class TraitementImageServiceTest {

   @Autowired
   private TraitementImageService traitementImageService;

   private File createTemporaryDirectory() throws IOException {
      File tempDirectory = FileUtils.getTempDirectory();
      File repertoireOut = new File(tempDirectory, "extract-" + UUID.randomUUID()
            .toString());
      
      if (!repertoireOut.mkdir()) {
         throw new IOException(
               "Impossible de creer le repertoire temporaire : "
                     + repertoireOut.getAbsolutePath());
      }
      return repertoireOut;
   }
   
   @Test
   public void extractImages_file_non_trouve() {
      File fichier = new File(
            "src/test/resources/pdf/fichier-inexistant.pdf");
      
      File repertoireOut = null;
      try { 
         // creation du repertoire temporaire
         repertoireOut = createTemporaryDirectory();
         
         // extraction des images
         traitementImageService.extractImages(fichier, repertoireOut.getAbsolutePath());
         
         Assert.fail("Une exception aurait du être levée car le fichier est inexistant");
         
      } catch (ExtractionException e) {
         Assert.assertEquals("L'exception n'est pas celle attendue", FileNotFoundException.class.getName(), e.getCause().getClass().getName());
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      } finally {
         if (repertoireOut != null) {
            try {
               FileUtils.deleteDirectory(repertoireOut);
            } catch (IOException e) {
               // rien a faire
            }
         }
      }
   }
   
   @Test
   public void extractImages_file_runtime() {
      File fichier = new File(
            "src/test/resources/pdf/fichierCorrompu.pdf");
      
      File repertoireOut = null;
      try { 
         // creation du repertoire temporaire
         repertoireOut = createTemporaryDirectory();
         
         // extraction des images
         traitementImageService.extractImages(fichier, repertoireOut.getAbsolutePath());
         
         Assert.fail("Une exception aurait du être levée car le fichier est inexistant");
         
      } catch (ExtractionException e) {
         Assert.assertTrue("L'exception n'est pas celle attendue", RuntimeException.class.isAssignableFrom(e.getCause().getClass()));
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      } finally {
         if (repertoireOut != null) {
            try {
               FileUtils.deleteDirectory(repertoireOut);
            } catch (IOException e) {
               // rien a faire
            }
         }
      }
   }

   @Test
   public void extractImages_file_success() {
      File fichier = new File(
            "src/test/resources/pdf/Dossier-4-Pdf-ScanMarseille.pdf");
      
      try { 
         // creation du repertoire temporaire
         File repertoireOut = createTemporaryDirectory();
         
         // extraction des images
         traitementImageService.extractImages(fichier, repertoireOut.getAbsolutePath());
         
         // recupere le nombre d'image jpg
         File[] files = repertoireOut.listFiles();
         Assert.assertEquals("Le nombre de fichiers extraits n'est pas correct", 26, files.length);
         
         // suppression du repertoire
         FileUtils.deleteDirectory(repertoireOut);
         
      } catch (ExtractionException e) {
         Assert.fail(e.getMessage());
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
   
   @Test
   public void extractImages_byte_runtime() {
      byte[] fichier = null;
      
      File repertoireOut = null;
      try {
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichierCorrompu.pdf"));
         
         // creation du repertoire temporaire
         repertoireOut = createTemporaryDirectory();
         
         // extraction des images
         traitementImageService.extractImages(fichier, repertoireOut.getAbsolutePath());
         
         Assert.fail("Une exception aurait du être levée car le fichier est inexistant");
         
      } catch (ExtractionException e) {
         Assert.assertTrue("L'exception n'est pas celle attendue", RuntimeException.class.isAssignableFrom(e.getCause().getClass()));
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      } finally {
         if (repertoireOut != null) {
            try {
               FileUtils.deleteDirectory(repertoireOut);
            } catch (IOException e) {
               // rien a faire
            }
         }
      }
   }

   @Test
   public void extractImages_byte_success() {
      byte[] fichier = null;
      
      try { 
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichier.pdf"));
         
         // creation du repertoire temporaire
         File repertoireOut = createTemporaryDirectory();
         
         // extraction des images
         traitementImageService.extractImages(fichier, repertoireOut.getAbsolutePath());
         
         // recupere le nombre d'image jpg
         File[] files = repertoireOut.listFiles();
         Assert.assertEquals("Le nombre de fichiers extraits n'est pas correct", 5, files.length);
         
         // suppression du repertoire
         FileUtils.deleteDirectory(repertoireOut);
         
      } catch (ExtractionException e) {
         Assert.fail(e.getMessage());
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
   
   @Test
   public void isFullImagePdf_file_success_full_screen() {
      File fichier = new File(
            "src/test/resources/pdf/Dossier-4-Pdf-ScanMarseille.pdf");
      
      // test si le pdf comporte que des images plein ecran
      boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
      
      // le document ne doit comporter que des images pleins ecrans
      Assert.assertTrue("Le document ne devait comporter que des images plein ecran", isFullScreen);
   }
   
   @Test
   public void isFullImagePdf_file_non_trouve() {
      File fichier = new File(
            "src/test/resources/pdf/fichier-inexistant.pdf");
      
      // test si le pdf comporte que des images plein ecran
      boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
      
      // le document n'existe pas
      Assert.assertFalse("Le document ne devait comporter que des images plein ecran", isFullScreen);
   }
   
   @Test
   public void isFullImagePdf_file_runtime() {
      File fichier = new File(
            "src/test/resources/pdf/fichierCorrompu.pdf");
      
      // test si le pdf comporte que des images plein ecran
      boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
      
      // le document n'existe pas
      Assert.assertFalse("Le document ne devait comporter que des images plein ecran", isFullScreen);
   }
   
   @Test
   public void isFullImagePdf_file_success_with_3_images() {
      File fichier = new File(
            "src/test/resources/pdf/fichier.pdf");
      
      // test si le pdf comporte que des images plein ecran
      boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
      
      // le document ne doit comporter que des images pleins ecrans
      Assert.assertFalse("Le document ne devait comporter que des images plein ecran", isFullScreen);
   }
   
   @Test
   public void isFullImagePdf_file_success_non_full_screen() {
      File fichier = new File(
            "src/test/resources/pdf/fichier-image-trop-petite.pdf");
      
      // test si le pdf comporte que des images plein ecran
      boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
      
      // le document ne doit comporter que des images pleins ecrans
      Assert.assertFalse("Le document ne devait comporter que des images plein ecran", isFullScreen);
   }
   
   @Test
   public void isFullImagePdf_byte_success_full_screen() {
      byte[] fichier = null;
      
      try { 
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/Dossier-4-Pdf-ScanMarseille.pdf"));
         
         // test si le pdf comporte que des images plein ecran
         boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
         
         // le document ne doit comporter que des images pleins ecrans
         Assert.assertTrue("Le document ne devait comporter que des images plein ecran", isFullScreen);
         
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
   
   @Test
   public void isFullImagePdf_byte_success_non_full_screen() {
      byte[] fichier = null;
      
      try { 
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichier-image-trop-petite.pdf"));
         
         // test si le pdf comporte que des images plein ecran
         boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
         
         // le document ne doit comporter que des images pleins ecrans
         Assert.assertFalse("Le document ne devait comporter que des images plein ecran", isFullScreen);
         
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
   
   @Test
   public void isFullImagePdf_byte_success_with_3_images() {
      byte[] fichier = null;
      
      try { 
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichier.pdf"));
         
         // test si le pdf comporte que des images plein ecran
         boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
         
         // le document ne doit comporter que des images pleins ecrans
         Assert.assertFalse("Le document ne devait comporter que des images plein ecran", isFullScreen);
         
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
   
   @Test
   public void isFullImagePdf_byte_runtime() {
      byte[] fichier = null;
      
      try { 
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichierCorrompu.pdf"));
         
         // test si le pdf comporte que des images plein ecran
         boolean isFullScreen = traitementImageService.isFullImagePdf(fichier);
         
         // le document ne doit comporter que des images pleins ecrans
         Assert.assertFalse("Le document ne devait comporter que des images plein ecran", isFullScreen);
         
      } catch (IOException e) {
         Assert.fail(e.getMessage());
      }
   }
}
