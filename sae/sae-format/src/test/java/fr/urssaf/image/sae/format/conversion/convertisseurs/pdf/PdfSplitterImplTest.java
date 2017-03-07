package fr.urssaf.image.sae.format.conversion.convertisseurs.pdf;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.schlichtherle.io.FileInputStream;
import fr.urssaf.image.commons.itext.exception.FormatConversionException;
import fr.urssaf.image.commons.itext.exception.FormatConversionParametrageException;
import fr.urssaf.image.commons.itext.service.FormatConversionService;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionRuntimeException;

/**
 * 
 * Classe testant les services de la classe {@link PdfSplitterImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class PdfSplitterImplTest {

   @Autowired
   private PdfSplitterImpl pdfSplitterImpl;

   @Test
   public void convertirFichierFile_success()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.pdf");

      // Appel de la méthode à tester
      byte[] result = pdfSplitterImpl.convertirFichier(ressource
            .getFile(), null, null);

      Assert.assertNotNull("Le fichier aurait dû être converti", result);
      Assert
            .assertTrue("Le fichier aurait dû être converti", result.length > 0);
   }

   @Test(expected = ConversionParametrageException.class)
   public void convertirFichierFile_erreurParametrage()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.pdf");

      // Appel de la méthode à tester
      pdfSplitterImpl.convertirFichier(ressource.getFile(), null,
            Integer.valueOf(0));

      Assert.fail("Une erreur de paramètrage aurait du apparaitre");
   }
   
   @Test(expected = ConversionException.class)
   public void convertirFichierFile_erreurConversionFichier()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources (il s'agit d'un txt renommé en PDF)
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier_txt.pdf");

      // Appel de la méthode à tester
      pdfSplitterImpl.convertirFichier(ressource.getFile(), null,
            Integer.valueOf(0));

      Assert.fail("Une erreur de conversion aurait du apparaitre");
   }

   @Test(expected = ConversionRuntimeException.class)
   public void convertirFichierFile_erreurRuntime()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichierCorrompu.pdf");

      // Appel de la méthode à tester
      pdfSplitterImpl.convertirFichier(ressource.getFile(), null,
            Integer.valueOf(0));

      Assert.fail("Une erreur Runtime aurait du apparaitre");
   }
   
   @Test(expected = ConversionException.class)
   public void convertirFichierFile_erreurConversion()
         throws ConversionParametrageException, IOException, FormatConversionParametrageException, FormatConversionException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.pdf");
      
      FormatConversionService serviceConversion = EasyMock.createMock(FormatConversionService.class);
      
      EasyMock.expect(serviceConversion.splitPdf((File) EasyMock.anyObject(), (Integer) EasyMock.anyObject(), (Integer) EasyMock.anyObject())).andThrow(new FormatConversionException("test-unitaire")).once();
      
      EasyMock.replay(serviceConversion);
      
      PdfSplitterImpl convertisseur = new PdfSplitterImpl(serviceConversion);

      // Appel de la méthode à tester
      byte[] result = convertisseur.convertirFichier(ressource.getFile(), null,
            null);

      Assert.fail("Une erreur de conversion aurait du apparaitre");
      
      EasyMock.reset(serviceConversion);
   }

   @Test
   public void convertirFichierByte_success()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.pdf");

      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      byte[] result = pdfSplitterImpl.convertirFichier(fichier,
            null, null);

      Assert.assertNotNull("Le fichier aurait dû être converti", result);
      Assert
            .assertTrue("Le fichier aurait dû être converti", result.length > 0);
   }

   @Test(expected = ConversionParametrageException.class)
   public void convertirFichierByte_erreurParametrage()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.pdf");

      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      pdfSplitterImpl.convertirFichier(fichier, null, Integer
            .valueOf(0));

      Assert.fail("Une erreur de paramètrage aurait du apparaitre");
   }

   
   @Test(expected = ConversionException.class)
   public void convertirFichierByte_erreurConversionFichier()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources (il s'agit d'un txt renommé en PDF)
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier_txt.pdf");

      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      pdfSplitterImpl.convertirFichier(fichier, null, Integer
            .valueOf(1));

      Assert.fail("Une erreur de conversion aurait du apparaitre");
   }
   
   @Test(expected = ConversionRuntimeException.class)
   public void convertirFichierByte_erreurRuntime()
         throws ConversionParametrageException, IOException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichierCorrompu.pdf");

      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      pdfSplitterImpl.convertirFichier(fichier, null, Integer
            .valueOf(0));

      Assert.fail("Une erreur Runtime aurait du apparaitre");
   }
   
   @Test(expected = ConversionException.class)
   public void convertirFichierByte_erreurConversion()
         throws ConversionParametrageException, IOException, FormatConversionParametrageException, FormatConversionException, ConversionException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.pdf");
      
      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));
      
      FormatConversionService serviceConversion = EasyMock.createMock(FormatConversionService.class);
      
      EasyMock.expect(serviceConversion.splitPdf((byte[]) EasyMock.anyObject(), (Integer) EasyMock.anyObject(), (Integer) EasyMock.anyObject())).andThrow(new FormatConversionException("test-unitaire")).once();
      
      EasyMock.replay(serviceConversion);
      
      PdfSplitterImpl convertisseur = new PdfSplitterImpl(serviceConversion);

      // Appel de la méthode à tester
      byte[] result = convertisseur.convertirFichier(fichier, null,
            null);

      Assert.fail("Une erreur de conversion aurait du apparaitre");
      
      EasyMock.reset(serviceConversion);
   }
}
