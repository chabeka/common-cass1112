/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.formulaire.PileTravauxFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.service.piletravaux.PileTravauxService;

/**
 * 
 * Controller pour l'affichage de la pile des travaux
 */
@Controller
@RequestMapping(value = "piletravaux")
public class PileTravauxController {

   
   @Autowired
   private PileTravauxService pileService;
   
   
   /**
    * Le GET
    * 
    * @param model
    *           le modèle
    * 
    * @return le nom de la vue
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {

      // Création de la classe de formulaire
      PileTravauxFormulaire form = new PileTravauxFormulaire();
      model.addAttribute("formulaire",form);
      
      // TODO : Serveurs Zookeeper et Cassandra
      // form.setServeursZookeeper(zookeeperBean.getHosts());
      // form.setServeursCassandra(cassandraBean.getHosts());

      // Lecture de la pile des travaux
      form.setTravaux(pileService.getAllJobs(100));
      
      // Renvoie le nom de la vue
      return "piletravaux";
      
   }

   
   
}
