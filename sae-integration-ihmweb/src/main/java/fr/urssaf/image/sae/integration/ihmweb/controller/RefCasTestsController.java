package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.modele.listetests.ListeCategoriesType;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielCasTestService;


/**
 * Affichage du référentiel des cas de tests stocké dans ce programme de test
 */
@Controller
@RequestMapping(value = "refCasTest")
public class RefCasTestsController {
   
   
   @Autowired
   private ReferentielCasTestService refCasTestService;
   
   
   @RequestMapping(method = RequestMethod.GET)
   protected final String getDefaultView(Model model)
   {
      
      // Récupère le référentiel des cas de test, et le met dans le modèle
      ListeCategoriesType refCasTest = refCasTestService.getListeTests();
      model.addAttribute("listeTests",refCasTest);
           
      // Renvoie le nom de la vue à afficher
      return "refCasTest";
      
   }
   

}
