package fr.urssaf.image.sae.integration.ihmweb.controller;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.EscapeXmlFormulaire;


/**
 * class permettant la gestion de la convertion des chaines XML
 * 
 *
 */


@Controller
@RequestMapping(value = "escapeXml")
public class EscapeXmlController {

   private final String NOM_VUE ="escapeXml";
   private StringEscapeUtils escape;
   @RequestMapping(method = RequestMethod.GET)
   public final String defaultView(Model model, HttpSession session) {
      EscapeXmlFormulaire xmlFormulaire = new EscapeXmlFormulaire();
      xmlFormulaire.setEscapedString(null);
      model.addAttribute("escapeFormulaire", xmlFormulaire);
      return NOM_VUE;
   }
   
   
   @RequestMapping(method = RequestMethod.POST)      
   public final String escapeXml(@ModelAttribute("escapeFormulaire") EscapeXmlFormulaire formulaire,
         HttpSession session) {
      
      formulaire.setEscapedString(escape.escapeXml(formulaire.getXmlString()));
      
      return null;  
   }
}
