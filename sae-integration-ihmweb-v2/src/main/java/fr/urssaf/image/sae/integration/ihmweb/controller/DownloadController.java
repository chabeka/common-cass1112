package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.urssaf.image.sae.integration.ihmweb.exception.IntegrationRuntimeException;
import fr.urssaf.image.sae.integration.ihmweb.utils.Base64Utils;


/**
 * Contrôleur pour le téléchargement de fichier<br>
 * <br>
 * On s'attend à récupérer dans le GET un paramètre nommée "filename",
 * indiquant le nom du fichier à télécharger.<br>
 * <br>
 * Ce fichier doit se trouver dans le répertoire temporaire du système.
 */
@Controller
@RequestMapping(value = "download")
public class DownloadController {

   
   /**
    * Pour le GET avec en paramètre un "filename" qui représente un nom de fichier
    * se trouvant dans le répertoire temporaire du serveur
    * 
    * @param filename le nom du fichier que l'on veut télécharger
    * @param response la réponse Http
    */
   @RequestMapping(method = RequestMethod.GET, params="filename")
   public final void getTempFile(
      @RequestParam String filename,
      HttpServletResponse response) {
      
      // Le paramètre "filename" est un nom de fichier, fichier qui doit
      //  se trouver dans le répertoire temporaire du système
      // On construit le chemin complet du fichier 
      String tempDir = System.getProperty("java.io.tmpdir");
      String nomFicComplet = FilenameUtils.concat(tempDir, filename);
      
      // Renvoi le contenu du fichier dans la réponse
      sendFileAsResponse(response,nomFicComplet);

   }
   
   
   
   /**
    * Pour le GET avec en paramètre un "ecdefilename" qui représente un chemin complet
    * de fichier dans l'ECDE, ce chemin étant encodé en base64 
    * 
    * @param fichierEcde chemin complet local d'un fichier de l'ECDE, le chemin étant
    *                     encodé en base64.
    * @param response la réponse de la servlet
    */
   @RequestMapping(method = RequestMethod.GET, params="ecdefilename")
   public final void getEcdeFile(
      @RequestParam(value="ecdefilename") String fichierEcde,
      HttpServletResponse response) {
      
      // ecdefilename est un chemin complet de fichier encodé en base64
      // On commence donc par décoder la base64
      String fichierEcdeDecode = Base64Utils.decode(fichierEcde);
      
      // Renvoie le contenu du fichier dans la réponse HTTP
      sendFileAsResponse(response,fichierEcdeDecode);
      
   }
   
   
   private void sendFileAsResponse(
         HttpServletResponse response,
         String cheminCompletFic) {
      try {
         File file = new File(cheminCompletFic);
         FileInputStream fileInputStream = new FileInputStream(file);
         IOUtils.copy(fileInputStream, response.getOutputStream());
         response.flushBuffer();
      } catch (IOException e) {
         throw new IntegrationRuntimeException(e);
      }
   }
   
   
}
