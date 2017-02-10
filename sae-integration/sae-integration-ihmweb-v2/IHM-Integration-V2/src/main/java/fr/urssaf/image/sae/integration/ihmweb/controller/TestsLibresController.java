package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.modele.listetests.CategorieType;
import fr.urssaf.image.sae.integration.ihmweb.modele.listetests.ListeCategoriesType;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielCasTestService;

@Controller
@RequestMapping(value="testsLibres.do")
public class TestsLibresController {

   @Autowired
   private ReferentielCasTestService casTestService;
   
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {
   // Met la liste des cas de test dans le modèle
      ListeCategoriesType listeTests = casTestService.getListeTests();
      model.addAttribute("listeTests", listeTests);
      return "testsLibres";
   }
   
   @RequestMapping(method = RequestMethod.GET, params = "action=detail")
   protected final String getDetailView(Model model, int id)
   {
      
      // Met la liste des cas de test dans le modèle
      ListeCategoriesType listeTests = casTestService.getListeTests();
      
      ListeCategoriesType sended = new ListeCategoriesType();
      
      for (CategorieType categorieType : listeTests.getCategorie()) {
         if (categorieType.getId() == id) {
            sended.getCategorie().add(categorieType);
         }
      }
      
      model.addAttribute("listeTests", sended);
      model.addAttribute("id", id);
      
      // Renvoie le nom de la vue à afficher
      return "listeDetailTests";
   }
}
