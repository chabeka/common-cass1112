package fr.urssaf.image.sae.pile.travaux.ihmweb.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.pile.travaux.ihmweb.formulaire.PileTravauxFormulaire;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraEtZookeeperConfig;
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
   


   
   private static final String NOM_VUE = "piletravaux";
   
   private static final int DEFAULT_NB_JOBS = 200;

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
      form.setConnexionConfig(defaultConfig());
      model.addAttribute("formulaire", form);

      // Lecture de la pile des travaux
      metPileDansModel(defaultConfig(),model);

      // Renvoie le nom de la vue
      return NOM_VUE;

   }
   
   
   private CassandraEtZookeeperConfig defaultConfig() {
      
      CassandraEtZookeeperConfig config = new CassandraEtZookeeperConfig();
      
      // config.setZookeeperHosts("cer69-ds4int.cer69.recouv:2181");
      config.setZookeeperHosts("hwi69devsaeapp1.cer69.recouv,hwi69devsaeapp2.cer69.recouv");
      config.setZookeeperNamespace("SAE");
      
      // config.setCassandraHosts("cer69imageint9.cer69.recouv:9160");
      config.setCassandraHosts("hwi69devsaecas1.cer69.recouv:9160,hwi69devsaecas2.cer69.recouv:9160");
      config.setCassandraUserName("root");
      config.setCassandraPassword("regina4932");
      config.setCassandraKeySpace("SAE");
      
      return config;
      
   }
   
   
   @RequestMapping(method = RequestMethod.POST)
   public final String post(
         Model model,
         @ModelAttribute("formulaire") PileTravauxFormulaire formulaire) {

      // Appel de la sous-méthode
      metPileDansModel(formulaire.getConnexionConfig(), model);
      
      // Renvoie le nom de la vue à afficher
      return NOM_VUE;

   }
   

   
   private void metPileDansModel(
         CassandraEtZookeeperConfig config,
         Model model) {


      List<JobRequest> jobs = pileService.getAllJobs(config,DEFAULT_NB_JOBS);
      Collections.sort(jobs, new JobRequestComparator());
      model.addAttribute("jobs", jobs);
      
   }

}

