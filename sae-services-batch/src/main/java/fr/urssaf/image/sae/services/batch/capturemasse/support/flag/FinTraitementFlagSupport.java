/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.flag;

import java.io.File;

/**
 * Composant pour les fichiers fin_traitement.flag créés lors d'un traitement de
 * capture de masse
 * 
 */
public interface FinTraitementFlagSupport {

   /**
    * Ecriture d'un fichier fin_traitement.flag créé à la fin d'un traitement de
    * capture de masse
    * 
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement de l'ECDE
    */
   void writeFinTraitementFlag(File ecdeDirectory);

}
