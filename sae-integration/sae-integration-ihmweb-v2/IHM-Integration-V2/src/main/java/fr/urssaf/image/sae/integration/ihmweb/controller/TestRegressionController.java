package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.modele.TestProprietes;
import fr.urssaf.image.sae.integration.ihmweb.service.regression.TestRegressionService;

/**
 * Controller de la partie pour les test de non regressions
 * 
 */
@Controller
@RequestMapping(value = "testRegression")
public class TestRegressionController {

   /**
    * Injection de la couche service des tests de non regression
    */
   @Autowired
   private TestRegressionService testRegressionService;

   /**
    * Injection du bean contenant toutes les informations sur la configuration de l'IHM
    * ex : chemin vers dossiers des tests, URL des web services du SAE ...
    */
   @Autowired
   private TestConfig testConfig;

   TestProprietes test;

   /**
    * Fonction requete GET renvoyant le nom des différents services pour la JSP
    * testRegression
    * 
    * @param model
    * @return
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {

      // Recuperation des noms des tests pour affichage
      File repertoireXml = new File(testConfig.getTestRegression());
      File[] filesXml = repertoireXml.listFiles();
      List<String> records = new ArrayList<String>();
      String str = null;
      for (File f : filesXml) {
         str = f.getName().substring(0, f.getName().indexOf("_"));
         records.add(str);
      }
      // tri dans l'ordre alphabétique
      Set<String> set = new HashSet<String>();
      set.addAll(records);
      ArrayList<String> distinctList = new ArrayList<String>(set);
      Collections.sort(distinctList);
      model.addAttribute("isOk", true);
      // ajout de la liste des test dans le model pour l'affichage
      model.addAttribute("listeTestXml", distinctList);
      return "testRegression";
   }

   /**
    * Fonction permettant de lancer les tests via la couche service, apres la
    * selection dans la checkbox
    * 
    * @param checkboxValue
    * @param model
    * @return
    * @throws IOException
    * @throws XMLStreamException
    * @throws SAXException
    * @throws ParserConfigurationException
    * @throws InterruptedException
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=lancerTest" })
   public final String lancerTest(
         @RequestParam("checkboxName") String[] checkboxValue, Model model)
         throws IOException, XMLStreamException, SAXException,
         ParserConfigurationException, InterruptedException {
      Map<String, Map<String, String>> resStub = new LinkedHashMap<String, Map<String, String>>();
      test = new TestProprietes();

      // ajout du type de fichier pour la recherche dans le dossier des tests de
      // non regression
      String[] checkChange = new String[checkboxValue.length];
      int i = 0;
      for (String str : checkboxValue) {
         checkChange[i] = str + ".txt";
         i++;
      }

      // appelle a la couche service pour lancer les tests selectionnés dans la
      // checkbox
      test = testRegressionService.testRegression(checkChange);

      // ajout des resultats dans le modele pour affichage dans la jsp
      model.addAttribute("resRegression", test);
      return "resultatRegression";

   }

   /**
    * fonction permettant le tri des tests pour l'affichage dans la checkboxs
    * 
    * @param checkboxValue
    * @param model
    * @return
    * @throws IOException
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=checkboxTest" })
   public String checkboxTest(@RequestParam("myValue") String checkboxValue,
         Model model) throws IOException {
      System.out.println(checkboxValue);

      // recuperer list nom test regression
      File repertoireXml = new File(testConfig.getTestRegression());
      File[] filesXml = repertoireXml.listFiles();
      List<String> records = new ArrayList<String>();
      for (File f : filesXml) {
         // check if contains checkbox value
         if (f.getName().contains(checkboxValue + "_")) {
            // put in list
            String str = StringUtils.remove(f.getName(), ".txt");
            records.add(str);
         }
      }

      // add list to model
      model.addAttribute("checkboxRegression", records);
      // add checkboxValue to model
      model.addAttribute("checkboxValue", checkboxValue);
      return "checkboxRegression";
   }

   /**
    * fonction permettant de gérer le retour au resultat des test depuis la JSP
    * detailRegression
    * 
    * @param detailTest
    * @param model
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=retourTest" })
   public String retourTest(@RequestParam("myValue") String detailTest,
         Model model) {
      // permet de retourner au resultat des tests depuis la JSP de détail des
      // resultats
      model.addAttribute("resRegression", test);
      System.out.println("RETOUR TEST");
      return "resultatRegression";
   }

   /**
    * fonction permettant d'afficher le contenu des messages envoyés et recus pour
    * chaque test
    * 
    * @param detailTest
    * @param model
    * @return
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=detailTest" })
   public String detailTest(@RequestParam("myValue") String detailTest,
         Model model) {

      Map<String, String> msg = new LinkedHashMap<String, String>();
      String msgIn = "";
      String msgOut = "";

      // recuperation des message envoyé et recu des tests depuis le bean
      // TestPropriete contenant les informations des tests réalisés
      for (Map.Entry<String, Map<String, Map<String, String>>> mp : test
            .getMessageInOut().entrySet()) {
         for (Map.Entry<String, Map<String, String>> mp2 : mp.getValue()
               .entrySet()) {
            if (mp2.getKey().equals(detailTest)) {
               System.out.println("MP2 KEY : " + mp2.getKey());
               for (Map.Entry<String, String> mp3 : mp2.getValue().entrySet()) {
                  msgOut = mp3.getKey();
                  msgIn = mp3.getValue();
                  break;
               }
            }
         }
      }

      // Ajout des resultats dans le model
      model.addAttribute("messageOut", msgOut);
      model.addAttribute("messageIn", msgIn);
      model.addAttribute("resRegression", test);
      return "detailTest";
   }

   // @RequestMapping(method = RequestMethod.POST, params = {
   // "action=downloadTest" })
   // public void downloadTest(@RequestParam("myValue") String checkboxValue,
   // HttpServletResponse response) throws IOException {
   //
   // String fullPath = testConfig.getTestRegression() + checkboxValue;
   // File downloadFile = new File(fullPath);
   // FileInputStream inputStream = new FileInputStream(downloadFile);
   //
   // String mimeType = "application/octet-stream";
   //
   // response.setContentType(mimeType);
   // response.setContentLength((int) downloadFile.length());
   //
   // String headerKey = "Content-Disposition";
   // String headerValue = String.format("attachment; filename=\"%s\"",
   // downloadFile.getName());
   // response.setHeader(headerKey, headerValue);
   //
   // OutputStream outStream = response.getOutputStream();
   //
   // byte[] buffer = new byte[4096];
   // int bytesRead = -1;
   //
   // while ((bytesRead = inputStream.read(buffer)) != -1) {
   // outStream.write(buffer, 0, bytesRead);
   // }
   // inputStream.close();
   // outStream.close();
   // }
   //
   // @RequestMapping(method = RequestMethod.POST, params = {
   // "action=sauvegarderTest" })
   // public void sauvegarderTest(@RequestParam("file") MultipartFile file,
   // Model model) throws IllegalStateException, IOException{
   // File convFile = new File(file.getOriginalFilename());
   // file.transferTo(convFile);
   //
   // boolean res = testRegressionService.sauvegarderTest(convFile);
   //
   // model.addAttribute("resSauvegarde", res);
   // String resFalse =
   // "un problème est survenu lors de l'enregistrement du fichier "
   // + convFile.getName();
   // String resTrue = "Le fichier " + convFile.getName()
   // + " a bien été enregistré sur le serveur";
   // if (res)
   // model.addAttribute("description", resTrue);
   // else
   // model.addAttribute("description", resFalse);
   // }
}
