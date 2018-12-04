package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.service.regression.GestionTestRegressionService;

/**
 * Controller de la partie permettant la gestion des tests de non regression Il
 * reçoit les requetes envoyé depuis la JSP gestionTestNonRegression
 * 
 */
@Controller
@RequestMapping(value = "gestionTestNonRegression")
public class GestionTestNonRegression {

   // bean contenant toute les informations de la coniguration de l'IHM
   @Autowired
   private TestConfig config;

   // Bean de la couche service
   @Autowired
   private GestionTestRegressionService service;

   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultview(Model model) {

      return "gestionTestNonRegression";
   }

   /**
    * Intercepte request POST pour la sauvegarde des fichiers XML utilisé pour
    * les tests de non regression
    * 
    * @param file
    * @param model
    * @return
    * @throws IllegalStateException
    * @throws IOException
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=sauvegarderTestXml" })
   public final String sauvegarderTestXml(
         @RequestParam("file") MultipartFile[] file, Model model)
         throws IllegalStateException, IOException {

      // Recupere l'ensemble des fichiers recus dans une liste de file
      List<File> listFile = new LinkedList<File>();
      for (MultipartFile f : file) {
         File convFile = new File(f.getOriginalFilename());
         f.transferTo(convFile);
         listFile.add(convFile);
      }

      // appelle à la couche service
      boolean res = service.sauvegarderTest(listFile, config.getTestXml());

      String description = "";
      // si res = ok, la sauvegarde s'est bien déroulé
      if (res)
         description = "Le ou les fichiers ont bien été enregistré sur le serveur";
      else
         description = "Une erreur est survenue durant la sauvegarde";

      // add res to model
      model.addAttribute("resSauvegarde", res);
      model.addAttribute("description", description);

      return "gestionTestNonRegression";
   }

   /**
    * Intercepte request POST pour la sauvegarde des fichiers contenant
    * l'attendu utilisé pour les tests de non regression
    * 
    * @param file
    * @param model
    * @return
    * @throws IllegalStateException
    * @throws IOException
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action2=sauvegarderTestAttendu" })
   public final String sauvegarderTestAttendu(
         @RequestParam("file2") MultipartFile[] file, Model model)
         throws IllegalStateException, IOException {

      // Recupere l'ensemble des fichiers recus dans une liste de file
      List<File> listFile = new LinkedList<File>();
      for (MultipartFile f : file) {
         File convFile = new File(f.getOriginalFilename());
         f.transferTo(convFile);
         listFile.add(convFile);
      }

      // appelle à la couche service
      boolean res = service.sauvegarderTest(listFile, config.getTestAttendu());

      String description = "";
      // si res = ok, la sauvegarde s'est bien déroulé
      if (res)
         description = "Le ou les fichiers ont bien été enregistré sur le serveur";
      else
         description = "Une erreur est survenue durant la sauvegarde";

      // add res to model
      model.addAttribute("resSauvegarde", res);
      model.addAttribute("description", description);

      return "gestionTestNonRegression";
   }

   /**
    * Intercepte request POST pour la sauvegarde des fichiers txt contenant le
    * deroulement du test de non regression
    * 
    * @param file
    * @param model
    * @return
    * @throws IllegalStateException
    * @throws IOException
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action3=sauvegarderTestRegression" })
   public final String sauvegarderTestRegression(
         @RequestParam("file3") MultipartFile[] file, Model model)
         throws IllegalStateException, IOException {

      // Recupere l'ensemble des fichiers recus dans une liste de file
      List<File> listFile = new LinkedList<File>();
      for (MultipartFile f : file) {
         File convFile = new File(f.getOriginalFilename());
         f.transferTo(convFile);
         listFile.add(convFile);
      }

      // appelle à la couche service
      boolean res = service.sauvegarderTest(listFile,
            config.getTestRegression());

      String description = "";
      // si res = ok, la sauvegarde s'est bien déroulé
      if (res)
         description = "Le ou les fichiers ont bien été enregistré sur le serveur";
      else
         description = "Une erreur est survenue durant la sauvegarde";

      // add res to model
      model.addAttribute("resSauvegarde", res);
      model.addAttribute("description", description);

      return "gestionTestNonRegression";
   }
}
