package fr.urssaf.image.commons.itext.service;

import java.io.File;

import fr.urssaf.image.commons.itext.exception.FormatConversionException;
import fr.urssaf.image.commons.itext.exception.FormatConversionParametrageException;

/**
 * Service permettant de réaliser des conversions de fichiers.
 * 
 */
public interface FormatConversionService {

   /**
    * Convertit un fichier TIFF en PDF (à partir d'un objet de type File)
    * 
    * @param fichier
    *           le fichier à convertir
    * @param numeroPage
    *           le numéro de page de départ
    * @param nombrePages
    *           le nombre de pages souhaités
    * @return le fichier converti
    * @throws FormatConversionException
    *            erreur de conversion
    * @throws FormatConversionParametrageException
    *            erreur de paramétrage
    */
   byte[] conversionTiffToPdf(File fichier, Integer numeroPage,
         Integer nombrePages) throws FormatConversionException,
         FormatConversionParametrageException;

   /**
    * Convertit un fichier TIFF en PDF (à partir d’un objet de type byte[])
    * 
    * @param fichier
    *           le fichier à convertir
    * @param numeroPage
    *           le numéro de page de départ
    * @param nombrePages
    *           le nombre de pages souhaités
    * @return le fichier converti
    * @throws FormatConversionException
    *            erreur de conversion
    * @throws FormatConversionParametrageException
    *            erreur de paramétrage
    */
   byte[] conversionTiffToPdf(byte[] fichier, Integer numeroPage,
         Integer nombrePages) throws FormatConversionException,
         FormatConversionParametrageException;
}
