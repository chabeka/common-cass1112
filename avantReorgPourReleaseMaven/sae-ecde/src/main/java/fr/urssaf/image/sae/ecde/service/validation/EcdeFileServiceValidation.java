package fr.urssaf.image.sae.ecde.service.validation;

import java.io.File;
import java.net.URI;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;
/**
 * Classe EcdeFileServiceValidation
 * 
 * Classe de validation des arguments en entrée des implémentations du service
 * EcdeFileServiceValidation *
 * 
 */
@Aspect
public class EcdeFileServiceValidation {
   
   private static final String ECDECLASS = "fr.urssaf.image.sae.ecde.service.EcdeFileService.";

   private static final String PARAMCONVERTFILE = "execution(URI "+ECDECLASS+"convertFileToURI(*,*))" +
   		                                         "&& args(ecdeFile,sources)";
   
   private static final String PARAMCONVERTURI = "execution(java.io.File "+ECDECLASS+"convertURIToFile(*,*))" +
                                                 "&& args(ecdeURL,sources)";
   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la methode convertFileToURI de l'interface
    * service.EcdeFileService sont bien correct.
    * 
    * @param ecdeFile fichier a convertir
    * @param sources liste des ecde
    */
   @Before(PARAMCONVERTFILE)
   public final void convertFileToURI(File ecdeFile, EcdeSource... sources) {
      
      // curseur pour parcourir la liste ecdeSource afin de recuperer l'index
      int curseur = 0;
      if(ecdeFile == null) { 
         throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessageObject("ecdeFileorUrl.nonRenseigne", "ecdeFile"));
      }
      if(ArrayUtils.isEmpty(sources)) {
         throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessage("ecdeFileNotExist", null));
      }
      for(EcdeSource variable : sources){
        verifierEcdeSource(variable, curseur);
        curseur ++;
      }
   }
   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la methode convertURIToFile de l'interface
    * service.EcdeFileService sont bien correct.
    * 
    * @param ecdeURL url a convertir
    * @param sources liste des ecde
    */
   @Before(PARAMCONVERTURI)
   public final void convertURIToFile(URI ecdeURL, EcdeSource... sources) {
      
      // curseur pour parcourir la liste ecdeSource afin de recuperer l'index
      int curseur = 0;
      if(ecdeURL == null) { 
         throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessageObject("ecdeFileorUrl.nonRenseigne", "ecdeURL"));
      }
      if(ArrayUtils.isEmpty(sources)) {
         throw new IllegalArgumentException(MessageRessourcesUtils.recupererMessage("ecdeFileNotExist", null));
      }
      for(EcdeSource variable : sources){
        verifierEcdeSource(variable, curseur);
        curseur ++;
      }
   }
   
   // verifier les parametres de EcdeSources, renvoie une exception 
   private void verifierEcdeSource(EcdeSource variable, int curseur){
      // Si l'attribut Host de l'ECDE n'est pas renseigné
      if (  StringUtils.isBlank(variable.getHost())  ) {
         throw new IllegalArgumentException(MessageRessourcesUtils.recupMessageNonRenseigneException("Host", curseur));
      }
      // Si l'attribut basePath de l'ECDE n'est pas renseigné
      if ( variable.getBasePath() == null ) {
         throw new IllegalArgumentException(MessageRessourcesUtils.recupMessageNonRenseigneException("Base Path", curseur));
      }
   }
}