package fr.urssaf.image.sae.format.conversion.service;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.schlichtherle.io.FileInputStream;

import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConvertisseurInitialisationException;
import fr.urssaf.image.sae.format.conversion.service.impl.ConversionServiceImpl;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.dao.support.ReferentielFormatSupport;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.utils.Utils;

/**
 * 
 * Classe testant les services de la classe {@link ConversionServiceImpl}
 * 
 * Rappel : Pour les tests unitaires sur les paramètres, ces derniers sont
 * testés dans le package "aspect"
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ConversionServiceImplTest {

   @Autowired
   private ConversionService conversionService;
   
   @Autowired
   @Qualifier("referentielFormatSupport")
   private ReferentielFormatSupport refFormatSupport;
         
   @Autowired
   private JobClockSupport jobClock;
   
   @Test
   public void convertirFichierFile_success()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");

      // Appel de la méthode à tester
      byte[] result = conversionService.convertirFichier("fmt/353", ressource
            .getFile(), null, null);

      Assert.assertNotNull("Le fichier aurait dû être converti", result);
      Assert
            .assertTrue("Le fichier aurait dû être converti", result.length > 0);
   }
   
   @Test(expected=ConversionParametrageException.class)
   public void convertirFichierFile_erreur_parametres()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");

      // Appel de la méthode à tester
      conversionService.convertirFichier("fmt/353", ressource
            .getFile(), null, Integer.valueOf(0));
   }
   
   @Test(expected=UnknownFormatException.class)
   public void convertirFichierFile_format_inconnu()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");

      // Appel de la méthode à tester
      conversionService.convertirFichier("inconnu", ressource
            .getFile(), null, null);
   }
   
   @Test(expected=ConvertisseurInitialisationException.class)
   public void convertirFichierFile_format_bean_inexistant()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // 1 - CREATION DU FORMAT LAMBDA
      FormatFichier refFormat = Utils.genererRefFormatLambda();
      String idFormat = refFormat.getIdFormat();
      Assert.assertEquals("lambda", idFormat);
      refFormatSupport.create(refFormat, jobClock.currentCLock());
      
      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");

      // Appel de la méthode à tester
      conversionService.convertirFichier("lambda", ressource
            .getFile(), null, null);
      
      // 2 - SUPPRESSION DU FORMAT LAMBDA
      refFormatSupport.delete(idFormat, jobClock.currentCLock());
   }
   
   @Test
   public void convertirFichierByte_success()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");
      
      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      byte[] result = conversionService.convertirFichier("fmt/353", fichier, null, null);

      Assert.assertNotNull("Le fichier aurait dû être converti", result);
      Assert
            .assertTrue("Le fichier aurait dû être converti", result.length > 0);
   }
   
   @Test(expected=ConversionParametrageException.class)
   public void convertirFichierByte_erreur_parametres()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");
      
      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      conversionService.convertirFichier("fmt/353", fichier, null, Integer.valueOf(0));
   }
   
   @Test(expected=UnknownFormatException.class)
   public void convertirFichierByte_format_inconnu()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");
      
      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      conversionService.convertirFichier("inconnu", fichier, null, null);
   }
   
   @Test(expected=ConvertisseurInitialisationException.class)
   public void convertirFichierByte_format_bean_inexistant()
         throws ConversionParametrageException, UnknownFormatException, ConvertisseurInitialisationException, IOException {

      // 1 - CREATION DU FORMAT LAMBDA
      FormatFichier refFormat = Utils.genererRefFormatLambda();
      String idFormat = refFormat.getIdFormat();
      Assert.assertEquals("lambda", idFormat);
      refFormatSupport.create(refFormat, jobClock.currentCLock());
      
      // Récupération du fichier de test depuis les ressources
      ClassPathResource ressource = new ClassPathResource(
            "/conversion/fichier.TIF");
      
      byte[] fichier = IOUtils.toByteArray(new FileInputStream(ressource
            .getFile()));

      // Appel de la méthode à tester
      conversionService.convertirFichier("lambda", fichier, null, null);
      
      // 2 - SUPPRESSION DU FORMAT LAMBDA
      refFormatSupport.delete(idFormat, jobClock.currentCLock());
   }
}
