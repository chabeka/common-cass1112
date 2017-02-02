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

@Controller
@RequestMapping(value = "testRegression")
public class TestRegressionController {

   @Autowired
   private TestRegressionService testRegressionService;

   @Autowired
   private TestConfig testConfig;

   TestProprietes test;

   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {

      File repertoireXml = new File(testConfig.getTestRegression());
      File[] filesXml = repertoireXml.listFiles();
      List<String> records = new ArrayList<String>();
      String str = null;
      for (File f : filesXml) {
         str = f.getName().substring(0, f.getName().indexOf("_"));
         records.add(str);
      }
      Set<String> set = new HashSet<String>();
      set.addAll(records);
      ArrayList<String> distinctList = new ArrayList<String>(set);
      Collections.sort(distinctList);
      model.addAttribute("isOk", true);
      model.addAttribute("listeTestXml", distinctList);
      return "testRegression";
   }

   @RequestMapping(method = RequestMethod.POST, params = { "action=lancerTest" })
   public final String lancerTest(
         @RequestParam("checkboxName") String[] checkboxValue, Model model)
         throws IOException, XMLStreamException, SAXException,
         ParserConfigurationException, InterruptedException {
      Map<String, Map<String, String>> resStub = new LinkedHashMap<String, Map<String, String>>();
      test = new TestProprietes();
      String[] checkChange = new String[checkboxValue.length];
      int i = 0;
      for (String str : checkboxValue) {
         checkChange[i] = str + ".txt";
         i++;
      }
      test = testRegressionService.testRegression(checkChange);

      model.addAttribute("resRegression", test);
      return "resultatRegression";

   }

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

   //methode retour au test a faire
   @RequestMapping(method = RequestMethod.POST, params = { "action=retourTest" })
   public String retourTest(@RequestParam("myValue") String detailTest,
         Model model) {
   
      model.addAttribute("resRegression", test);
      System.out.println("RETOUR TEST");
      return "resultatRegression";
   }
   
   @RequestMapping(method = RequestMethod.POST, params = { "action=detailTest" })
   public String detailTest(@RequestParam("myValue") String detailTest,
         Model model) {

      System.out.println("DETAIL DU TEST " + detailTest);
      Map<String, String> msg = new LinkedHashMap<String, String>();
      String msgIn = "";
      String msgOut = "";

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
