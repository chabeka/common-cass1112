/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.flag;

import java.io.File;

import fr.urssaf.image.sae.services.capturemasse.support.flag.model.DebutTraitementFlag;

/**
 * Composant pour les fichiers debut_traitement.flag créés lors d'un traitement
 * de capture de masse
 * 
 */
public interface DebutTraitementFlagSupport {

   /**
    * Ecriture d'un fichier debut_traitement.flag créé au commencement d'un
    * traitement de capture de masse
    * 
    * @param debutTraitementFlag
    *           propriétés sur le début du traitement de capture de masse
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement de l'ECDE
    */
   void writeDebutTraitementFlag(DebutTraitementFlag debutTraitementFlag,
         File ecdeDirectory);

}
