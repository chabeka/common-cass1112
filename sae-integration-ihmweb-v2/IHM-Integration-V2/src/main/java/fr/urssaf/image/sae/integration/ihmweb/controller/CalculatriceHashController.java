package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "calculatriceHash")
public class CalculatriceHashController {

   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {

      return "calculatriceHash";

   }

   @RequestMapping(method = RequestMethod.POST, params = { "action=lancerCalcul" })
   public final String calculHash(
         @RequestParam("cheminFichier") String cheminFichier, Model model) {

      File fileEcde = new File(cheminFichier);
      String hashCode;
      // récupération du contenu pour le calcul du HASH
      byte[] content;
      try {
         content = FileUtils.readFileToByteArray(fileEcde);

         // calcul du Hash
         hashCode = DigestUtils.shaHex(content);
      } catch (IOException e) {
         hashCode = "Probleme dans le calcul du hash";
      }

      model.addAttribute("hashCode", hashCode);

      return "calculatriceHash";
   }

}
