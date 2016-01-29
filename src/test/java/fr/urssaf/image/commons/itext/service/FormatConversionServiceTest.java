package fr.urssaf.image.commons.itext.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.lowagie.text.ExceptionConverter;

import fr.urssaf.image.commons.itext.exception.FormatConversionException;
import fr.urssaf.image.commons.itext.exception.FormatConversionParametrageException;
import fr.urssaf.image.commons.itext.exception.FormatConversionRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-commons-itext-test.xml" })
public class FormatConversionServiceTest {

   @Autowired
   private FormatConversionService conversionService;
   
   @Test
   public void conversionTiffToPdf_file_success() throws FormatConversionException, FormatConversionParametrageException {
      
      File fichier = new File("src/test/resources/tiff/fichier.TIF");
      
      byte[] fichierConverti = conversionService.conversionTiffToPdf(fichier, null, null);
      Assert.assertNotNull("Le tableau de byte aurait du être non null", fichierConverti);
      Assert.assertTrue("Le tableau de byte aurait du être non vide", fichierConverti.length > 0);
   }
   
   @Test
   public void conversionTiffToPdf_file_non_trouve()  {
      
      File fichier = new File("fichier-inexistant.tif");
      
      try {
         conversionService.conversionTiffToPdf(fichier, null, null);
         Assert.fail("Ce test aurait du renvoyer une exception");
      } catch (FormatConversionException e) {
         Assert.assertEquals("La cause de l'exception aurait du être de type FileNotFoundException", FileNotFoundException.class.getName() , e.getCause().getClass().getName());
      } catch (FormatConversionParametrageException e) {
         Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionException");
      }
   }
   
   @Test
   public void conversionTiffToPdf_file_runtime()  {
      
      File fichier = new File("src/test/resources/tiff/fichierCorrompu.TIF");
      
      try {
         conversionService.conversionTiffToPdf(fichier, null, null);
         Assert.fail("Ce test aurait du renvoyer une exception");
      } catch (FormatConversionException e) {
         Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
      } catch (FormatConversionParametrageException e) {
         Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
      } catch (FormatConversionRuntimeException e) {
         Assert.assertEquals("La cause de la runtime aurait du être de type ExceptionConverter", ExceptionConverter.class.getName() , e.getCause().getClass().getName());
      }
   }
   
   @Test
   public void conversionTiffToPdf_byte_success() throws FormatConversionException, FormatConversionParametrageException {
      byte[] fichier;
      try {
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/tiff/fichier.TIF"));
         byte[] fichierConverti = conversionService.conversionTiffToPdf(fichier, null, null);
         Assert.assertNotNull("Le tableau de byte aurait du être non null", fichierConverti);
         Assert.assertTrue("Le tableau de byte aurait du être non vide", fichierConverti.length > 0);

      } catch (IOException e) {
         Assert.fail("La conversion du fichier en byte n'aurait pas du echouee");
      }
   }
   
   @Test
   public void conversionTiffToPdf_byte_runtime()  {
      byte[] fichier;
      try {
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/tiff/fichierCorrompu.TIF"));
      
         try {
            conversionService.conversionTiffToPdf(fichier, null, null);
            Assert.fail("Ce test aurait du renvoyer une exception");
         } catch (FormatConversionException e) {
            Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
         } catch (FormatConversionParametrageException e) {
            Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
         } catch (FormatConversionRuntimeException e) {
            Assert.assertEquals("La cause de la runtime aurait du être de type ExceptionConverter", ExceptionConverter.class.getName() , e.getCause().getClass().getName());
         }
      } catch (IOException e) {
         Assert.fail("La conversion du fichier en byte n'aurait pas du echouee");
      }
   }
   
   @Test
   public void splitPdf_file_success() throws FormatConversionException, FormatConversionParametrageException {
      
      File fichier = new File("src/test/resources/pdf/fichier.pdf");
      
      byte[] fichierConverti = conversionService.splitPdf(fichier, Integer.valueOf(1), Integer.valueOf(2));
      Assert.assertNotNull("Le tableau de byte aurait du être non null", fichierConverti);
      Assert.assertTrue("Le tableau de byte aurait du être non vide", fichierConverti.length > 0);
   }
   
   @Test
   public void splitPdf_file_success_complet() throws FormatConversionException, FormatConversionParametrageException {
      
      File fichier = new File("src/test/resources/pdf/fichier.pdf");
      
      byte[] fichierConverti = conversionService.splitPdf(fichier, null, null);
      Assert.assertNotNull("Le tableau de byte aurait du être non null", fichierConverti);
      Assert.assertTrue("Le tableau de byte aurait du être non vide", fichierConverti.length > 0);
   }
   
   @Test
   public void splitPdf_file_non_trouve()  {
      
      File fichier = new File("fichier-inexistant.pdf");
      
      try {
         conversionService.splitPdf(fichier, Integer.valueOf(1), Integer.valueOf(2));
         Assert.fail("Ce test aurait du renvoyer une exception");
      } catch (FormatConversionException e) {
         Assert.assertEquals("La cause de l'exception aurait du être de type FileNotFoundException", FileNotFoundException.class.getName() , e.getCause().getClass().getName());
      } catch (FormatConversionParametrageException e) {
         Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionException");
      }
   }
   
   @Test
   public void splitPdf_file_runtime()  {
      
      File fichier = new File("src/test/resources/pdf/fichierCorrompu.pdf");
      
      try {
         conversionService.splitPdf(fichier, Integer.valueOf(1), Integer.valueOf(2));
         Assert.fail("Ce test aurait du renvoyer une exception");
      } catch (FormatConversionException e) {
         Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
      } catch (FormatConversionParametrageException e) {
         Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
      } catch (FormatConversionRuntimeException e) {
         Assert.assertEquals("La cause de la runtime aurait du être de type ExceptionConverter", NullPointerException.class.getName() , e.getCause().getClass().getName());
      }
   }
   
   @Test
   public void splitPdf_byte_success() throws FormatConversionException, FormatConversionParametrageException {
      byte[] fichier;
      try {
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichier.pdf"));
         byte[] fichierConverti = conversionService.splitPdf(fichier, Integer.valueOf(1), Integer.valueOf(2));
         Assert.assertNotNull("Le tableau de byte aurait du être non null", fichierConverti);
         Assert.assertTrue("Le tableau de byte aurait du être non vide", fichierConverti.length > 0);

      } catch (IOException e) {
         Assert.fail("La conversion du fichier en byte n'aurait pas du echouee");
      }
   }
   
   @Test
   public void splitPdf_byte_success_complet() throws FormatConversionException, FormatConversionParametrageException {
      byte[] fichier;
      try {
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichier.pdf"));
         byte[] fichierConverti = conversionService.splitPdf(fichier, null, null);
         Assert.assertNotNull("Le tableau de byte aurait du être non null", fichierConverti);
         Assert.assertTrue("Le tableau de byte aurait du être non vide", fichierConverti.length > 0);

      } catch (IOException e) {
         Assert.fail("La conversion du fichier en byte n'aurait pas du echouee");
      }
   }
   
   @Test
   public void splitPdf_byte_runtime()  {
      byte[] fichier;
      try {
         fichier = IOUtils.toByteArray(new FileInputStream("src/test/resources/pdf/fichierCorrompu.pdf"));
      
         try {
            conversionService.splitPdf(fichier, Integer.valueOf(1), Integer.valueOf(2));
            Assert.fail("Ce test aurait du renvoyer une exception");
         } catch (FormatConversionException e) {
            Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
         } catch (FormatConversionParametrageException e) {
            Assert.fail("Ce test aurait du renvoyer une exception de type FormatConversionRuntimeException");
         } catch (FormatConversionRuntimeException e) {
            Assert.assertEquals("La cause de la runtime aurait du être de type ExceptionConverter", NullPointerException.class.getName() , e.getCause().getClass().getName());
         }
      } catch (IOException e) {
         Assert.fail("La conversion du fichier en byte n'aurait pas du echouee");
      }
   }
}
