package fr.urssaf.image.commons.droid.support;

import java.io.IOException;

import org.springframework.core.io.Resource;

import uk.gov.nationalarchives.droid.core.BinarySignatureIdentifier;

/**
 * Classe dérivée de la classe DROID BinarySignatureIdentifier, permettant le
 * chargement du fichier des signatures non plus obligatoirement à partir d'un
 * fichier physique et de son chemin complet, mais à partir d'un objet Resource
 * Spring.<br>
 * <br>
 * Cela permettant par exemple de charger un fichier de signatures depuis les
 * ressources du JAR.
 */
public class MyBinarySignatureIdentifier extends BinarySignatureIdentifier {

   /**
    * Constructeur
    * 
    * @param signatures
    *           l'objet Resource pointant sur les signatures binaires DROID
    */
   public MyBinarySignatureIdentifier(Resource signatures) {
      super();
      try {
        setSignatureFile(signatures.getFile().getAbsolutePath());
      } catch (final IOException e) {
        // Do nothing
      }
   }

}
