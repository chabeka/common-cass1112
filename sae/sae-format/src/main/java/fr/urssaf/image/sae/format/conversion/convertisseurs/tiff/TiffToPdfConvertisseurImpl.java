package fr.urssaf.image.sae.format.conversion.convertisseurs.tiff;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.commons.itext.exception.FormatConversionException;
import fr.urssaf.image.commons.itext.exception.FormatConversionParametrageException;
import fr.urssaf.image.commons.itext.exception.FormatConversionRuntimeException;
import fr.urssaf.image.commons.itext.service.FormatConversionService;
import fr.urssaf.image.sae.format.conversion.convertisseurs.Convertisseur;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionRuntimeException;

/**
 * Implémentation de la conversion d’un fichier TIFF en PDF
 * 
 */
@Service
public class TiffToPdfConvertisseurImpl implements Convertisseur {

   private static final String LOG_DEBUT = "{} - Début";
   private static final String LOG_FIN = "{} - Fin";

   private static final Logger LOGGER = LoggerFactory
         .getLogger(TiffToPdfConvertisseurImpl.class);

   /**
    * Service permettant de réaliser la conversion d’un fichier TIFF en PDF.
    */
   private final FormatConversionService formatConversionService;

   /**
    * Constructeur avec parametres.
    * 
    * @param formatConversionService
    *           service de conversion tiff en pdf
    */
   @Autowired
   public TiffToPdfConvertisseurImpl(
         FormatConversionService formatConversionService) {
      super();
      this.formatConversionService = formatConversionService;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final byte[] convertirFichier(File fichier, Integer numeroPage,
         Integer nombrePages) throws ConversionParametrageException,
         ConversionException {
      byte[] resultat = null;

      // Traces debug - entrée méthode
      String prefixeTrc = "convertirFichier()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      try {
         resultat = formatConversionService.conversionTiffToPdf(fichier,
               numeroPage, nombrePages);
      } catch (FormatConversionRuntimeException e) {
         throw new ConversionRuntimeException(
               "Une erreur inattendu s'est produite", e);
      } catch (FormatConversionException e) {
         LOGGER.error("Une erreur de conversion s'est produite : {}",
               e.getMessage());
         throw new ConversionException(e.getMessage(), e);
      } catch (FormatConversionParametrageException e) {
         throw new ConversionParametrageException(e.getMessage(), e);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      return resultat;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final byte[] convertirFichier(byte[] fichier, Integer numeroPage,
         Integer nombrePages) throws ConversionParametrageException,
         ConversionException {
      byte[] resultat = null;

      // Traces debug - entrée méthode
      String prefixeTrc = "convertirFichier()";
      LOGGER.debug(LOG_DEBUT, prefixeTrc);

      try {
         resultat = formatConversionService.conversionTiffToPdf(fichier,
               numeroPage, nombrePages);
      } catch (FormatConversionException e) {
         LOGGER.error("Une erreur de conversion s'est produite : {}",
               e.getMessage());
         throw new ConversionException(e.getMessage(), e);
      } catch (FormatConversionParametrageException e) {
         throw new ConversionParametrageException(e.getMessage(), e);
      } catch (FormatConversionRuntimeException e) {
         throw new ConversionRuntimeException(
               "Une erreur inattendu s'est produite", e);
      }

      // Traces debug - sortie méthode
      LOGGER.debug(LOG_FIN, prefixeTrc);

      return resultat;
   }

}
