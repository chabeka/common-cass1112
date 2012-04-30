package fr.urssaf.image.sae.pile.travaux.ihmweb.controller;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;

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
import fr.urssaf.image.sae.pile.travaux.ihmweb.utils.ConfigUtils;

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
   public final String getDefaultView(
         Model model,
         HttpSession session) {

      // Création de la classe de formulaire
      PileTravauxFormulaire form = new PileTravauxFormulaire();
      form.setConnexionConfig(ConfigUtils.defaultConfig());
      model.addAttribute("formulaire", form);

      // Lecture de la pile des travaux
      metPileDansModel(form.getConnexionConfig(), model, session);

      // Renvoie le nom de la vue
      return NOM_VUE;

   }
   
   
   @RequestMapping(method = RequestMethod.POST)
   public final String post(
         Model model,
         @ModelAttribute("formulaire") PileTravauxFormulaire formulaire,
         HttpSession session) {

      // Appel de la sous-méthode
      metPileDansModel(formulaire.getConnexionConfig(), model, session);
      
      // Renvoie le nom de la vue à afficher
      return NOM_VUE;

   }
   

   
   private void metPileDansModel(
         CassandraEtZookeeperConfig config,
         Model model,
         HttpSession session) {

      List<JobRequest> jobs = pileService.getAllJobs(config,DEFAULT_NB_JOBS);
      Collections.sort(jobs, new JobRequestComparator());
      model.addAttribute("jobs", jobs);
      
      // Mémorise la dernière configuration en variable de session
      // Sert à initialiser l'écran d'affichage de l'historique
      ConfigUtils.putConfigInSession(session, config);
      
   }

}

