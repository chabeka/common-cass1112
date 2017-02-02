package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "sauvegarderFichier")
public class SauvegarderFichier {
   
   @RequestMapping(method = RequestMethod.GET)
   public final String sauvegarderFichier(Model model){
      
      return "listeTests";
   }

}
