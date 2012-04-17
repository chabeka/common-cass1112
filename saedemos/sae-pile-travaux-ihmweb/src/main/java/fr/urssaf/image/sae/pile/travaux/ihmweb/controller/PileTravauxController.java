package fr.urssaf.image.sae.pile.travaux.ihmweb.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.pile.travaux.ihmweb.formulaire.PileTravauxFormulaire;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraConfig;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.JobRequest;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.JobRequestComparator;
import fr.urssaf.image.sae.pile.travaux.ihmweb.service.PileTravauxService;

/**
 * Controller pour l'affichage de la pile des travaux
 */
@Controller
@RequestMapping(value = "piletravaux")
public class PileTravauxController {

   @Autowired
   private PileTravauxService pileService;

   @Autowired
   private CassandraConfig cassandraConfig;

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
      model.addAttribute("formulaire", form);

      // Serveurs Zookeeper et Cassandra
      form.setServeursZookeeper(cassandraConfig.getZookeeperHosts());
      form.setServeursCassandra(cassandraConfig.getCassandraHosts());

      // Lecture de la pile des travaux (200 jobs maxi)
      List<JobRequest> jobs = pileService.getAllJobs(200);
      Collections.sort(jobs, new JobRequestComparator());
      form.setTravaux(jobs);

      // Renvoie le nom de la vue
      return "piletravaux";

   }

}
