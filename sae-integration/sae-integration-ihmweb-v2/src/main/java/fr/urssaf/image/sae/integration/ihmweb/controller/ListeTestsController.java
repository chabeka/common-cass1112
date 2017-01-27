package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.integration.ihmweb.service.listetests.ListTestsService;

@Controller
@RequestMapping(value = "listeTests")
public class ListeTestsController {

   @Autowired
   ListTestsService service;

   private File f;

   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultview(Model model) {

      return "listeTests";
   }

   // Post charger un fichier
   @RequestMapping(method = RequestMethod.POST, params = { "action=lancerTest" })
   public final String lancerTest(@RequestParam("file") MultipartFile file,
         Model model) throws XMLStreamException, SAXException, IOException,
         ParserConfigurationException {
      File convFile = new File(file.getOriginalFilename());
      file.transferTo(convFile);
      // envoyer fonction lance test regression
      Map<String, String> resTest = service.lancerTest(convFile);

      // retourner jsp des test de regression
      model.addAttribute("resTest", resTest);
      model.addAttribute("testName", file.getOriginalFilename());
      model.addAttribute("fileTest", convFile);

      f = convFile;

      return "resultatTestCharge";
   }

   @RequestMapping(method = RequestMethod.POST, params = { "action=sauvegarderTest" })
   public final String sauvegarderTest(Model model)
         throws IllegalStateException, IOException {

      boolean res = service.sauvegarderTest(f);

      // model.addAttribute("testName", f.getName());
      model.addAttribute("resSauvegarde", res);
      String resFalse = "un problème est survenu lors de l'enregistrement du fichier "
            + f.getName();
      String resTrue = "Le fichier " + f.getName()
            + " a bien été enregistré sur le serveur";
      if (res)
         model.addAttribute("description", resTrue);
      else
         model.addAttribute("description", resFalse);

      return "listeTests";
   }

}
