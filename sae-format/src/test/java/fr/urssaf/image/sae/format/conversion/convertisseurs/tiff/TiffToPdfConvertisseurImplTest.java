package fr.urssaf.image.sae.format.conversion.convertisseurs.tiff;

import java.io.File;
import java.io.IOException;

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
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionRuntimeException;
import junit.framework.Assert;

/**
 * Classe testant les services de la classe {@link TiffToPdfConvertisseurImpl}
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/applicationContext-sae-format-test.xml"})
public class TiffToPdfConvertisseurImplTest {

   @Autowired
   private TiffToPdfConvertisseurImpl tiffToPdfConvertisseurImpl;

   @Test
   public void convertirFichierFile_success() throws ConversionParametrageException, IOException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource("/conversion/fichier.TIF");

      // Appel de la méthode à tester
      final byte[] result = tiffToPdfConvertisseurImpl.convertirFichier(ressource.getFile(), null, null);

      Assert.assertNotNull("Le fichier aurait dû être converti", result);
      Assert.assertTrue("Le fichier aurait dû être converti", result.length > 0);
   }

   @Test
   public void convertirCorruptedFile_success() throws ConversionParametrageException, IOException {

      // Il s'agit d'un fichier tiff généré par rightFax, qui ne respecte pas la norme tiff
      final ClassPathResource ressource = new ClassPathResource("/conversion/duexfax.tif");

      // Appel de la méthode à tester
      final byte[] result = tiffToPdfConvertisseurImpl.convertirFichier(ressource.getFile(), null, null);

      Assert.assertNotNull("Le fichier aurait dû être converti", result);
      Assert.assertTrue("Le fichier aurait dû être converti", result.length > 0);
   }

   @Test(expected = ConversionParametrageException.class)
   public void convertirFichierFile_erreurParametrage()
         throws ConversionParametrageException, IOException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource(
                                                                "/conversion/fichier.TIF");

      // Appel de la méthode à tester
      tiffToPdfConvertisseurImpl.convertirFichier(ressource.getFile(),
                                                  null,
                                                  Integer.valueOf(0));

      Assert.fail("Une erreur de paramètrage aurait du apparaitre");
   }

   @Test(expected = ConversionRuntimeException.class)
   public void convertirFichierFile_erreurRuntime()
         throws ConversionParametrageException, IOException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource(
                                                                "/conversion/Test.doc");

      // Appel de la méthode à tester
      tiffToPdfConvertisseurImpl.convertirFichier(ressource.getFile(),
                                                  null,
                                                  Integer.valueOf(0));

      Assert.fail("Une erreur Runtime aurait du apparaitre");
   }

   @Test
   public void convertirFichierFile_erreurConversion()
         throws ConversionParametrageException, IOException, FormatConversionParametrageException, FormatConversionException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource(
                                                                "/conversion/fichier.TIF");

      final FormatConversionService serviceConversion = EasyMock.createMock(FormatConversionService.class);

      EasyMock.expect(serviceConversion.conversionTiffToPdf((File) EasyMock.anyObject(), (Integer) EasyMock.anyObject(), (Integer) EasyMock.anyObject()))
              .andThrow(new FormatConversionException("test-unitaire"))
              .once();

      EasyMock.replay(serviceConversion);

      final TiffToPdfConvertisseurImpl convertisseur = new TiffToPdfConvertisseurImpl(serviceConversion);

      // Appel de la méthode à tester
      final byte[] result = convertisseur.convertirFichier(ressource.getFile(),
                                                           null,
                                                           null);

      Assert.assertNull("Le fichier n'aurait pas dû être converti", result);

      EasyMock.reset(serviceConversion);
   }

   @Test
   public void convertirFichierByte_success()
         throws ConversionParametrageException, IOException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource(
                                                                "/conversion/fichier.TIF");

      final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
                                                                              .getFile()));

      // Appel de la méthode à tester
      final byte[] result = tiffToPdfConvertisseurImpl.convertirFichier(fichier,
                                                                        null,
                                                                        null);

      Assert.assertNotNull("Le fichier aurait dû être converti", result);
      Assert
            .assertTrue("Le fichier aurait dû être converti", result.length > 0);
   }

   @Test(expected = ConversionParametrageException.class)
   public void convertirFichierByte_erreurParametrage()
         throws ConversionParametrageException, IOException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource(
                                                                "/conversion/fichier.TIF");

      final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
                                                                              .getFile()));

      // Appel de la méthode à tester
      tiffToPdfConvertisseurImpl.convertirFichier(fichier,
                                                  null,
                                                  Integer
                                                         .valueOf(0));

      Assert.fail("Une erreur de paramètrage aurait du apparaitre");
   }

   @Test(expected = ConversionRuntimeException.class)
   public void convertirFichierByte_erreurRuntime()
         throws ConversionParametrageException, IOException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource(
                                                                "/conversion/Test.doc");

      final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
                                                                              .getFile()));

      // Appel de la méthode à tester
      tiffToPdfConvertisseurImpl.convertirFichier(fichier,
                                                  null,
                                                  Integer
                                                         .valueOf(0));

      Assert.fail("Une erreur Runtime aurait du apparaitre");
   }

   @Test
   public void convertirFichierByte_erreurConversion()
         throws ConversionParametrageException, IOException, FormatConversionParametrageException, FormatConversionException {

      // Récupération du fichier de test depuis les ressources
      final ClassPathResource ressource = new ClassPathResource(
                                                                "/conversion/fichier.TIF");

      final byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
                                                                              .getFile()));

      final FormatConversionService serviceConversion = EasyMock.createMock(FormatConversionService.class);

      EasyMock.expect(serviceConversion.conversionTiffToPdf((byte[]) EasyMock.anyObject(), (Integer) EasyMock.anyObject(), (Integer) EasyMock.anyObject()))
              .andThrow(new FormatConversionException("test-unitaire"))
              .once();

      EasyMock.replay(serviceConversion);

      final TiffToPdfConvertisseurImpl convertisseur = new TiffToPdfConvertisseurImpl(serviceConversion);

      // Appel de la méthode à tester
      final byte[] result = convertisseur.convertirFichier(fichier,
                                                           null,
                                                           null);

      Assert.assertNull("Le fichier n'aurait pas dû être converti", result);

      EasyMock.reset(serviceConversion);
   }
}
