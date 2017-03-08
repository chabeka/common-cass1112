package fr.urssaf.image.sae.format.conversion.service;

import java.io.File;

import fr.urssaf.image.sae.format.conversion.exceptions.ConversionException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConvertisseurInitialisationException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;

/**
 * Interface décrivant les opérations de conversion possibles
 */
public interface ConversionService {

   /**
    * Méthode permettant de convertir un fichier (à partir du fichier) dans un
    * format affichable.
    * 
    * @param idFormat
    *           Identifiant du format du fichier à convertir
    * @param fichier
    *           Nom du fichier à convertir
    * @param numeroPage
    *           Le numéro de la page à partir de laquelle on découpe le fichier
    * @param nombrePages
    *           Le nombre de pages souhaité
    * @return Le flux correspondant au fichier au format affichable
    * @throws UnknownFormatException
    *            Le format demandé n’existe pas en base
    * @throws ConvertisseurInitialisationException
    *            Impossible d’instancier le convertisseur
    * @throws ConversionParametrageException
    *            Erreur de paramètrage de la conversion
    * @throws ConversionException erreur lors de la conversion
    */
   byte[] convertirFichier(String idFormat, File fichier, Integer numeroPage,
         Integer nombrePages) throws ConvertisseurInitialisationException,
         UnknownFormatException, ConversionParametrageException, ConversionException;

   /**
    * Méthode permettant de convertir un fichier (à partir du flux) dans un
    * format affichable.
    * 
    * @param idFormat
    *           Identifiant du format du fichier à convertir
    * @param fichier
    *           Flux correspondant au fichier à convertir
    * @param numeroPage
    *           Le numéro de la page à partir de laquelle on découpe le fichier
    * @param nombrePages
    *           Le nombre de pages souhaité
    * @return Le flux correspondant au fichier au format affichable
    * @throws UnknownFormatException
    *            Le format demandé n’existe pas en base
    * @throws ConvertisseurInitialisationException
    *            Impossible d’instancier le convertisseur
    * @throws ConversionParametrageException
    *            Erreur de paramètrage de la conversion
    * @throws ConversionException erreur lors de la conversion
    */
   byte[] convertirFichier(String idFormat, byte[] fichier, Integer numeroPage,
         Integer nombrePages) throws ConvertisseurInitialisationException,
         UnknownFormatException, ConversionParametrageException, ConversionException;
}
