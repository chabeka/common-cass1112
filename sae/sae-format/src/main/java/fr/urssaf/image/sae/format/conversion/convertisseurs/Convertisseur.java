package fr.urssaf.image.sae.format.conversion.convertisseurs;

import java.io.File;

import fr.urssaf.image.sae.format.conversion.exceptions.ConversionException;
import fr.urssaf.image.sae.format.conversion.exceptions.ConversionParametrageException;

/**
 * Interface décrivant les opérations de conversion possibles.
 * 
 */
public interface Convertisseur {

   /**
    * Méthode permettant de convertir un fichier (à partir du fichier) dans un
    * format affichable.
    * 
    * @param fichier
    *           Nom du fichier à convertir
    * @param numeroPage
    *           Le numéro de la page à partir de laquelle on découpe le fichier
    * @param nombrePages
    *           Le nombre de pages souhaité
    * @return Le flux correspondant au fichier au format affichable
    * @throws ConversionParametrageException
    *            erreur de paramètrage
    * @throws ConversionException
    *            erreur lors de la conversion
    */
   byte[] convertirFichier(File fichier, Integer numeroPage, Integer nombrePages)
         throws ConversionParametrageException, ConversionException;

   /**
    * Méthode permettant de convertir un fichier (à partir du flux) dans un
    * format affichable.
    * 
    * @param fichier
    *           Flux correspondant au fichier à convertir
    * @param numeroPage
    *           Le numéro de la page à partir de laquelle on découpe le fichier
    * @param nombrePages
    *           Le nombre de pages souhaité
    * @return Le flux correspondant au fichier au format affichable
    * @throws ConversionParametrageException
    *            erreur de paramètrage
    * @throws ConversionException
    *            erreur lors de la conversion
    */
   byte[] convertirFichier(byte[] fichier, Integer numeroPage,
         Integer nombrePages) throws ConversionParametrageException,
         ConversionException;
}
