package fr.urssaf.image.sae.ecde.service.impl;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.ecde.exception.EcdeBadFileException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLException;
import fr.urssaf.image.sae.ecde.exception.EcdeBadURLFormatException;
import fr.urssaf.image.sae.ecde.exception.EcdeRuntimeException;
import fr.urssaf.image.sae.ecde.modele.source.EcdeSource;
import fr.urssaf.image.sae.ecde.service.EcdeFileService;
import fr.urssaf.image.sae.ecde.util.MessageRessourcesUtils;

/**
 * Service de manipulation des URL ECDE et des chemins de fichiers.
 * 
 * {@link EcdeFileService}
 * 
 */
@Service
public class EcdeFileServiceImpl implements EcdeFileService {

   /**
    * Recupération des Constantes
    */
   private static final String ECDE = "ecde";
   private static final String EXPR_REG = "ecde://.*/.*/(19|20)[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])/.*/documents/.+";
   private static final String EXPR_REG_SOM = "ecde://.*/.*/(19|20)[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])/.*/sommaire.xml";
   
   /**
    * Implémentation de la méthode convertFileToURI
    * 
    * @param ecdeFile
    *           fichier ecde
    * @param sources
    *           liste des ecdes
    *           
    * @throws EcdeBadFileException Mauvais chemin de fichier
    * 
    * @return URI uri converti
    */
   @Override
   public final URI convertFileToURI(File ecdeFile, EcdeSource... sources)
         throws EcdeBadFileException {
      
      // boolean pour signaler que le debut du fichier est bien trouvé dans sources
      boolean trouve = false;
      // recupération host
      String host = "";
      //valeur retournée
      URI uriRetournee = null;
      EcdeSource ecdeSource = new EcdeSource("", new File(""));
      
      //Adapter le separateur du systeme Linux ou Windows
      String nomFichier = FilenameUtils.separatorsToSystem(ecdeFile.getPath());

      // parcourir les ECDE Sources
      // comparer avec le debut du fichier
      // si retrouver alors remplacer le debut de fichier par ecde://point de montage
      for (EcdeSource variable : sources) {
          // copie du bean
          BeanUtils.copyProperties(variable, ecdeSource);
          String path = FilenameUtils.separatorsToSystem(ecdeSource.getBasePath().getPath());
          if (FilenameUtils.wildcardMatch(nomFichier, path+ File.separator + "*")) {
             nomFichier = StringUtils.removeStart(nomFichier,path);
             trouve = true;
             host = ecdeSource.getHost();
          }
      } 
      // levée d'exception car aucune correspondance
      if ( !trouve ){
         throw new EcdeBadFileException(MessageRessourcesUtils.recupererMessage("ecdeBadFileException.message", ecdeFile));
      }
      // Construction de l'URI adequate
      try {
         //TODO
         /**
          * private static char separateurFichier = System.getProperty("file.separator").charAt(0);
          * 
          * chemin = StringUtils.replaceChars(chemin, '/', separateurFichier);
            chemin = StringUtils.replaceChars(chemin, '\\', separateurFichier);
          * 
          * */
         String fichier = nomFichier.replace("\\", "/");
         uriRetournee = new URI(ECDE, host, fichier, null);
      } catch (URISyntaxException e) {
         throw new EcdeRuntimeException(e);
      }
      return uriRetournee;
   }

   
   /**
    * Implémentation de la méthode convertURIToFile
    * 
    * @param ecdeURL
    *           url a convertir<br>
    *           Cette URL doit verifier le format {@value #EXPR_REG}
    * @param sources
    *           liste des ecdes
    *           
    * @throws EcdeBadURLException mauvaise url 
    * @throws EcdeBadURLFormatException mauvais format d'url
    * 
    * @return File file converti
    */
   @Override
   public final File convertURIToFile(URI ecdeURL, EcdeSource... sources)
         throws EcdeBadURLException, EcdeBadURLFormatException {

      // basePath recuperer a partir de ecdeSource
      String basePath = "";
      // boolean pour signaler que authority de l'uri bien trouvé dans sources
      boolean trouve = false;
      // pour la copie du bean
      EcdeSource ecdeSource = new EcdeSource("", new File(""));

      // Il faut commencer par vérifier que le ecdeURL respecte le format URL ECDE
      // ecde://ecde.cer69.recouv/numeroCS/dateTraitement/idTraitement/documents/nom_du_fichier
      if ( ! validateURL(ecdeURL, EXPR_REG) ) {
         throw new EcdeBadURLFormatException(MessageRessourcesUtils.recupererMessage("ecdeBadUrlFormatException.message", ecdeURL));
      }
      
      // il faut maintenant venir parcourir la liste sources afin de recuperer l'ECDE correspondant
      // Parcours donc de la liste sources
      for (EcdeSource variable : sources) {
         // copie du bean
         org.springframework.beans.BeanUtils.copyProperties(variable, ecdeSource);
         if ( ecdeURL.getAuthority().equals(ecdeSource.getHost()) ) {
             //concordance entre uri et ecdesource donné en paramètre
            basePath = ecdeSource.getBasePath().getPath();
            trouve = true;
         }
      }
      // levée d'exception car uri non trouve dans sources
      if ( !trouve ){
         throw new EcdeBadURLException(MessageRessourcesUtils.recupererMessage("ecdeBadUrlException.message", ecdeURL));
      }
      // Construire le chemin absolu du fichier
      return new File(basePath.concat(ecdeURL.getPath()));
   }
   
   
   
   
   /**
    * Implémentation de la méthode convertURIToFile
    * 
    * @param sommaireURL
    *           url a convertir<br>
    *           Cette URL doit verifier le format {@value #EXPR_REG_SOM}
    * @param sources
    *           liste des ecdes
    *           
    * @throws EcdeBadURLException mauvaise url 
    * @throws EcdeBadURLFormatException mauvais format d'url
    * 
    * @return File file converti
    */
   @Override
   public final File convertSommaireToFile(URI sommaireURL, EcdeSource... sources)
         throws EcdeBadURLException, EcdeBadURLFormatException {

      // basePath recuperer a partir de ecdeSource
      String basePath = "";
      // boolean pour signaler que authority de l'uri bien trouvé dans sources
      boolean trouve = false;
      // pour la copie du bean
      EcdeSource ecdeSource = new EcdeSource("", new File(""));

      // Il faut commencer par vérifier que le sommaireURL respecte le format URL ECDE
      // ecde://ecde.cer69.recouv/numeroCS/dateTraitement/idTraitement/sommaire.xml
      if ( ! validateURL(sommaireURL, EXPR_REG_SOM) ) {
         throw new EcdeBadURLFormatException(MessageRessourcesUtils.recupererMessage("sommaireBadUrlFormatException.message", sommaireURL));
      }
      
      // il faut maintenant venir parcourir la liste sources afin de recuperer l'ECDE correspondant
      // Parcours donc de la liste sources
      for (EcdeSource variable : sources) {
         // copie du bean
         org.springframework.beans.BeanUtils.copyProperties(variable, ecdeSource);
         if ( sommaireURL.getAuthority().equals(ecdeSource.getHost()) ) {
             //concordance entre uri et ecdesource donné en paramètre
            basePath = ecdeSource.getBasePath().getPath();
            trouve = true;
         }
      }
      // levée d'exception car uri non trouve dans sources
      if ( !trouve ){
         throw new EcdeBadURLException(MessageRessourcesUtils.recupererMessage("sommaireBadUrlException.message", sommaireURL));
      }
      // Construire le chemin absolu du fichier
      return new File(basePath.concat(sommaireURL.getPath()));
   }
   
   /**
    * Methode permettant de renvoyer un boolean pour signaler que l'url donnée en 
    * paramètre verifie bien le format contenu dans l'expression reguliere.
    * <br>
    *  
    * 
    * @param ecdeURL que l'on veut verifier
    * @param expReg indiquant le format de l'expression a verifier suivant {@value #EXPR_REG}
    * @return boolean true ou false
    */
   public final boolean validateURL(URI ecdeURL, String expReg) {
      return ecdeURL.toString().matches(expReg);
   }
}
