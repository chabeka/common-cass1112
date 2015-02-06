package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.GelDocumentFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.service.dfce.DfceService;


/**
 * Gel-Document-TestLibre
 */
@Controller
@RequestMapping(value = "gelDocument")
public class GelDocumentController {
   
   @Autowired
   DfceService dfceService;
   
   @Autowired
   private TestConfig testConfig;
   
   private final String NOM_VUE = "gelDocument";
   
   /**
    * Action GET de la page du formulaire
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {
      GelDocumentFormulaire formulaire = new GelDocumentFormulaire();
      model.addAttribute("formulaire", formulaire);
      return NOM_VUE;
   }
   
   /**
    * Action POST du formulaire
    */
   @RequestMapping(method = RequestMethod.POST)
   public final String doPost(Model model, GelDocumentFormulaire form) {
      
      if(form.getIdDocument() != null){
         //-- Gel du document
         final String idDoc = form.getIdDocument().toString();
         if(StringUtils.isNotEmpty(idDoc) && StringUtils.isNotBlank(idDoc)){
            String base = testConfig.getDfceBase();
            dfceService.freezeDocument(form.getIdDocument(), base);
            form.setResultats("Gel du document terminé sans erreur.");
         }else{
            form.setResultats("L'identifiant du document est vide ou non renseigné.");
         }
      } else {
         form.setResultats("L'identifiant du document est obligatoire.");
      }
      
      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }
}
