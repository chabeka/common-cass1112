package fr.urssaf.image.sae.pile.travaux.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.pile.travaux.ihmweb.formulaire.HistoryFormulaire;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.CassandraEtZookeeperConfig;
import fr.urssaf.image.sae.pile.travaux.ihmweb.modele.JobHistory;
import fr.urssaf.image.sae.pile.travaux.ihmweb.service.PileTravauxService;
import fr.urssaf.image.sae.pile.travaux.ihmweb.utils.ConfigUtils;

/**
 * Controller pour l'affichage de l'historique d'un job
 */
@Controller
@RequestMapping(value = "history")
public class HistoryController {

   public static final String NOM_REQ_IDJOB = "idJobReq";
   
   
   @Autowired
   private PileTravauxService pileService;
   
   private static final String NOM_VUE = "history";
   
   
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(
         Model model,
         HttpSession session,
         HttpServletRequest request) {

      // Création de la classe de formulaire
      CassandraEtZookeeperConfig config = ConfigUtils.getConfigFromSession(session);
      HistoryFormulaire form = new HistoryFormulaire();
      form.setConnexionConfig(config);
      form.setIdJob(request.getParameter(NOM_REQ_IDJOB));
      model.addAttribute("formulaire", form);
      
      // Lecture de l'historique
      metHistoryDansModel(form.getConnexionConfig(),model,form.getIdJob());

      // Renvoie le nom de la vue
      return NOM_VUE;

   }
   
   
   private void metHistoryDansModel(
         CassandraEtZookeeperConfig config,
         Model model,
         String idJob) {

      List<JobHistory> history;
      
      if (StringUtils.isBlank(idJob) || (config==null) || (StringUtils.isBlank(config.getCassandraHosts()))) {
         
         history = new ArrayList<JobHistory>();
         
      } else {
         
         history = pileService.getJobHistory(config, UUID.fromString(idJob));
         
      }
      
      model.addAttribute("history", history);
      
   }

   
    @RequestMapping(method = RequestMethod.POST)
    public final String post(
          Model model,
          @ModelAttribute("formulaire") HistoryFormulaire formulaire) {
   
       // Appel de la sous-méthode
       metHistoryDansModel(formulaire.getConnexionConfig(), model, formulaire.getIdJob());
       
       // Renvoie le nom de la vue à afficher
       return NOM_VUE;
   
    }
   
}

