package fr.urssaf.image.sae.ecde.util.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.SystemUtils;

import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSources;


/**
 * Classe permettant, pour les tests unitaires, le chargement d'une configuration
 * ECDE pointant sur une arborescence du répertoire temporaire de l'OS.
 *
 */
public class EcdeTestConfig {

   
   /**
    * DNS de l'ECDE disponible pour les tests unitaires.
    */
   public static final String DNS_ECDE_TU = "ecde.testunit.recouv";
   
   
   /**
    * Mise en place d'une configuration ECDE pour les tests unitaires.<br>
    * <br>
    * Cette méthode doit être appelée par une factory-method dans un
    * fichier de configuration Spring.<br>
    * 
    * @return l'objet EcdeSources contenant une configuration ECDE pour les TU
    */
   public final EcdeSources load() {

      // On récupère le répertoire temporaire de l'OS
      File repTempOs = SystemUtils.getJavaIoTmpDir();
      
      // On construit un nom unique de répertoire pour le point
      // de montage d'un ECDE pour les tests unitaires
      String ecdeUniqueRepName = EcdeTestTools.getTemporaryFileName("ecde", null);
      
      // On construit le chemin complet du point de montage de l'ECDE
      // pour les tests unitaires
      File pointMontageEcde = new File(repTempOs,ecdeUniqueRepName);
      
      // On créé le répertoire
      try {
         FileUtils.forceMkdir(pointMontageEcde);
      } catch (IOException e) {
         throw new EcdeRuntimeException(e);
      }
      
      // Création de 3 ECDE
      //  - 1 pour les tests unitaires 
      //  - 2 pour le plaisir
      
      // L'ECDE pour les tests unitaires sera manipulable par l'URL ECDE :
      //  ecde://ecde.testunit.recouv/.......
      
      EcdeSources ecdeSources = new EcdeSources();
      
      EcdeSource ecde1 = new EcdeSource(
            DNS_ECDE_TU, 
            pointMontageEcde);
      
      EcdeSource ecde2 = new EcdeSource(
            "ecde.cer69.recouv", 
            new File("/ecde/ecde_lyon"));
      
      EcdeSource ecde3 = new EcdeSource(
            "ecde.bidon2.recouv", 
            new File("/ecde/ECDE_PARIS/"));
      
      EcdeSource[] source = new EcdeSource[]{ecde1, ecde2, ecde3};
      ecdeSources.setSources(source);
      
      
      return ecdeSources;
      
   }
   
   
}
