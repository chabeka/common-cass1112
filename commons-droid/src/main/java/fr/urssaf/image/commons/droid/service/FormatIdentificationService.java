package fr.urssaf.image.commons.droid.service;

import java.io.File;
import java.io.IOException;

/**
 * Service d'identification de format avec DROID
 */
public interface FormatIdentificationService {

   /**
    * Identifie le format d'un fichier avec DROID
    * 
    * @param file
    *           le fichier dont il faut identifier le format
    * @return l'identifiant PRONOM du format, ou null si le format n'a pas été
    *         identifié
    */
   String identifie(File file) throws IOException;

}
