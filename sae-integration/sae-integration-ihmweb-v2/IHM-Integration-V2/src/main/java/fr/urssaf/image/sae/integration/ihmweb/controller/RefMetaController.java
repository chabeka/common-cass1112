package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.modele.MetadonneeDefinition;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;


/**
 * Affichage du référentiel des métadonnées stocké dans ce programme de test
 */
@Controller
@RequestMapping(value = "refMetas")
public class RefMetaController {
   
   
   @Autowired
   private ReferentielMetadonneesService refMetaService;
   
   
   @RequestMapping(method = RequestMethod.GET)
   protected final String getDefaultView(Model model)
   {
      
      // Récupère le référentiel des métadonnées, et le met dans le modèle
      List<MetadonneeDefinition> refMetas = refMetaService.getReferentielMetadonnees();
      model.addAttribute("refMetas",refMetas);
           
      // Renvoie le nom de la vue à afficher
      return "refMetas";
      
   }
   

}
