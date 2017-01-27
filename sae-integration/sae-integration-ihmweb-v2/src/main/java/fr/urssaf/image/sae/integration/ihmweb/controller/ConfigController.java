package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ConfigFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSource;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeSources;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.file.EcdeSourceManager;

/**
 * Contrôleur pour la configuration générale de l'application
 */
@Controller
@RequestMapping(value = "config")
public class ConfigController {

   private static final String NOM_VUE = "config";
   
   @Autowired
   private TestConfig testConfig;

   @Autowired
   private EcdeSourceManager ecdeSourceManager;

   @Autowired
   private EcdeSources ecdeSources;

   /**
    * Initialisation de la page
    * 
    * @param model
    *           données spring
    * @return la page de redirection
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String viewListeEcdeSources(Model model) {

      ConfigFormulaire form = new ConfigFormulaire();
      // try {
      // EcdeSources ecdeSources = ecdeSourceManager.load();
      form.setEcdeSources(ecdeSources);

      // } catch (Exception e) {
      // e.printStackTrace();
      // }

      form.setUrlWS(testConfig.getUrlSaeService());
      form.setCheminTest(testConfig.getTestRegression());
      form.setCheminTestXml(testConfig.getTestXml());
      model.addAttribute("formulaire", form);

      return NOM_VUE;

   }

   /**
    * Ajout d'un élément à la liste
    * 
    * @param model
    *           données spring
    * @param form
    *           formulaire soumis
    * @return la page de redirection
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=add" })
   public final String saveListeEcdeSources(Model model,
         ConfigFormulaire form) {

      List<EcdeSource> listEcde = form.getEcdeSources().getSources();
      listEcde.add(form.getSource());

      form.setSource(new EcdeSource());

      model.addAttribute("formulaire", form);
      return NOM_VUE;
   }

   /**
    * Suppression d'un élément de la liste
    * 
    * @param model
    *           données spring
    * @param form
    *           formulaire soumis
    * @param idSup
    *           index de l'élément à supprimer
    * @return la page de redirection
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=delete" })
   public final String deleteListeEcdeSources(Model model,
         ConfigFormulaire form, Integer idSup) {

      List<EcdeSource> listEcde = form.getEcdeSources().getSources();
      listEcde.remove(idSup.intValue());

      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }

   /**
    * Sauvergarde des sources
    * 
    * @param model
    *           données spring
    * @param form
    *           formulaire soumis
    * @param errors
    *           erreurs éventuelles de surface
    * @return la page de redirection
    * @throws Exception
    *            erreur lors du traitement
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=generate" })
   public final String generateListeEcdeSources(Model model,
         ConfigFormulaire form, BindingResult errors)
         throws Exception {

      ecdeSourceManager.generate(form.getEcdeSources().getSources());

      ecdeSources.setSources(form.getEcdeSources().getSources());

      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }

   /**
    * Sauvegarde l'adresse du WS en mémoire mais pas en dur
    * 
    * @param model
    *           données spring
    * @param form
    *           formulaire soumis
    * @param errors
    *           erreurs éventuelles de surface
    * @return la page de redirection
    * @throws Exception
    *            erreur lors du traitement
    */
   @RequestMapping(method = RequestMethod.POST, params = { "action=saveURL" })
   public final String saveURL(Model model, ConfigFormulaire form)
         throws Exception {

      testConfig.setUrlSaeService(form.getUrlWS());
      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }
   
   @RequestMapping(method = RequestMethod.POST, params = { "action=saveTestRegression" })
   public final String saveTestRegression(Model model, ConfigFormulaire form)
         throws Exception {

      testConfig.setTestRegression(form.getCheminTest());
      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }
   
   @RequestMapping(method = RequestMethod.POST, params = { "action=saveTestXml" })
   public final String saveTestXml(Model model, ConfigFormulaire form)
         throws Exception {

      testConfig.setTestXml(form.getCheminTestXml());
      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }
}
