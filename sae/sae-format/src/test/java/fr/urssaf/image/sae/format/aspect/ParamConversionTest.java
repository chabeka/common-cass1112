package fr.urssaf.image.sae.format.aspect;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.format.conversion.convertisseurs.tiff.TiffToPdfConvertisseurImpl;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConvertisseurInitialisationException;
import fr.urssaf.image.sae.format.conversion.service.ConversionService;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-format-test.xml" })
public class ParamConversionTest {

   private static final String MESSAGE_REF_RUNTIME = "Une exception ReferentielRuntimeException aurait dû être levée";
   private static final String MESSAGE_EXCEPT_INCORRECT = "Le message de l'exception est incorrect";
   private static final String TEST = "test";
   private static final String FICHIER_OBLIG_NUL = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [fichier].";
   private static final String BYTE_OBLIG_NUL = "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [byte].";

   @Autowired
   private TiffToPdfConvertisseurImpl convertisseur;

   @Autowired
   private ConversionService conversionService;

   @Test
   public void validConvertirFichierFileNull()
         throws ConversionParametrageException {
      try {
         convertisseur.convertirFichier((File) null, null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void validConvertirFichierFileNotExist()
         throws ConversionParametrageException {
      try {
         convertisseur.convertirFichier(new File(TEST), null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void validConvertirFichierByteNull()
         throws ConversionParametrageException {
      try {
         convertisseur.convertirFichier((byte[]) null, null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, BYTE_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void conversionServiceConvertirFichierFileIdFormatNullFileNotExist()
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {
      try {
         conversionService.convertirFichier(null, new File(TEST), null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, fichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void conversionServiceConvertirFichierFileFileNotExist()
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {
      try {
         conversionService.convertirFichier("fmt/353", new File(TEST), null,
               null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void conversionServiceConvertirFichierFileFileNull()
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {

      try {
         conversionService.convertirFichier("fmt/353", (File) null, null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, FICHIER_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void conversionServiceConvertirFichierFileIdFormatNullFileNull()
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {

      try {
         conversionService.convertirFichier(null, (File) null, null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, fichier].",
                     ex.getMessage());
      }
   }

   @Test
   public void conversionServiceConvertirFichierByteIdFormatNullByteNull()
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {
      try {
         conversionService.convertirFichier(null, (byte[]) null, null, null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat, byte].",
                     ex.getMessage());
      }
   }

   @Test
   public void conversionServiceConvertirFichierByteByteNull()
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {
      try {
         conversionService.convertirFichier("fmt/353", (byte[]) null, null,
               null);
         Assert.fail(MESSAGE_REF_RUNTIME);
      } catch (ReferentielRuntimeException ex) {
         Assert.assertEquals(MESSAGE_EXCEPT_INCORRECT, BYTE_OBLIG_NUL, ex
               .getMessage());
      }
   }

   @Test
   public void conversionServiceConvertirFichierByteIdFormatNull()
         throws ConvertisseurInitialisationException, UnknownFormatException,
         ConversionParametrageException {
      try {

         byte[] buffer = new byte[1];

         conversionService.convertirFichier(null, buffer, null, null);

         Assert.fail(MESSAGE_REF_RUNTIME);

      } catch (ReferentielRuntimeException ex) {
         Assert
               .assertEquals(
                     MESSAGE_EXCEPT_INCORRECT,
                     "La valeur d'un ou plusieurs paramètres obligatoires est nulle ou vide : [idFormat].",
                     ex.getMessage());
      }
   }

}
