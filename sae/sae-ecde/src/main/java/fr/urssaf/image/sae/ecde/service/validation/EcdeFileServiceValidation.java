package fr.urssaf.image.sae.ecde.service.validation;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;

/**
 * Classe EcdeFileServiceValidation
 * 
 * Classe de validation des arguments en entrée des implémentations du service
 * EcdeFileServiceValidation *
 * 
 */
@Aspect
public class EcdeFileServiceValidation {

   private static String ARG_RENSEIGNE = "L'argument '%s' doit être renseigné.";
   private static String AUCUN_ECDE = "Aucun ECDE n'est transmis en paramètre.";
   private static String ATTRIBUT_NR = "L'attribut '%s' de l'ECDE No %s n'est pas renseigné.";

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode convertFileToURI de l'interface service.EcdeFileService sont bien
    * correct.
    * 
    * @param ecdeFile
    *           fichier a convertir
    * @param sources
    *           liste des ecde
    */
   @Before("execution(java.net.URI fr.urssaf.image.sae.ecde.service.EcdeFileService.convertFileToURI(*,*)) && args(ecdeFile,sources)")
   public final void convertFileToURI(File ecdeFile, EcdeSource... sources) {

      // curseur pour parcourir la liste ecdeSource afin de recuperer l'index
      int curseur = 0;
      if (ecdeFile == null) {
         throw new IllegalArgumentException(String.format(ARG_RENSEIGNE,
               "ecdeFile"));
      }
      if (ArrayUtils.isEmpty(sources)) {
         throw new IllegalArgumentException(AUCUN_ECDE);
      }
      for (EcdeSource variable : sources) {
         verifierEcdeSource(variable, curseur);
         curseur++;
      }
   }

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode convertURIToFile de l'interface service.EcdeFileService sont bien
    * correct.
    * 
    * @param ecdeURL
    *           url a convertir
    * @param sources
    *           liste des ecde
    */
   @Before("execution(java.io.File fr.urssaf.image.sae.ecde.service.EcdeFileService.convertURIToFile(*,*)) && args(ecdeURL,sources)")
   public final void convertURIToFile(URI ecdeURL, EcdeSource... sources) {

      // curseur pour parcourir la liste ecdeSource afin de recuperer l'index
      int curseur = 0;
      if (ecdeURL == null) {
         throw new IllegalArgumentException(String.format(ARG_RENSEIGNE,
               "ecdeURL"));
      }
      if (ArrayUtils.isEmpty(sources)) {
         throw new IllegalArgumentException(AUCUN_ECDE);
      }
      for (EcdeSource variable : sources) {
         verifierEcdeSource(variable, curseur);
         curseur++;
      }
   }

   private void verifierEcdeSource(EcdeSource variable, int curseur) {

      // Si l'attribut Host de l'ECDE n'est pas renseigné
      if (StringUtils.isBlank(variable.getHost())) {
         throw new IllegalArgumentException(String.format(ATTRIBUT_NR, "Host",
               curseur));
      }

      // Si l'attribut basePath de l'ECDE n'est pas renseigné
      if (variable.getBasePath() == null) {
         throw new IllegalArgumentException(String.format(ATTRIBUT_NR,
               "BasePath", curseur));
      }

   }
}