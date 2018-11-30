/**
 * 
 */
package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.controller.components.EcdeTestDisplayed;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.ListeCasDeTestFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTests;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.file.EcdeTestInterface;

/**
 * Jeux de tests à injecter : Configuration
 */
@Controller
@RequestMapping(value = "jeuxTestInjectionConfig")
public class JeuxTestInjectionConfigController {

   private static final String NOM_VUE = "jeuxTestInjectionConfig";
   
   private static final String ECDE_LIST = "ecdeListe";

   @Autowired
   private EcdeTests ecdeTests;

   @Autowired
   private EcdeTestInterface ecdeTestInterface;

   /**
    * Initialisation de la page
    * 
    * @param model
    *           données spring
    * @return la page de redirection
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String getDefaultView(Model model) {

      ListeCasDeTestFormulaire testFormulaire = new ListeCasDeTestFormulaire();

      testFormulaire.setEcdeTests(ecdeTests);

      model.addAttribute("formulaire", testFormulaire);

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
         ListeCasDeTestFormulaire form) {

      List<EcdeTest> listEcde = form.getEcdeTests().getListTests();
      listEcde.add(form.getEcdeTest());

      form.setEcdeTest(new EcdeTest());

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
         ListeCasDeTestFormulaire form, Integer idSup) {

      List<EcdeTest> listEcde = form.getEcdeTests().getListTests();
      listEcde.remove(idSup.intValue());

      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }

   /**
    * Sauvergarde des cas de test
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
         HttpSession session, ListeCasDeTestFormulaire form,
         BindingResult errors) throws Exception {

      ecdeTestInterface.generateFile(form.getEcdeTests().getListTests());

      ecdeTests.setListTests(form.getEcdeTests().getListTests());

      List<EcdeTestDisplayed> testDisplayeds = new ArrayList<EcdeTestDisplayed>();

      EcdeTestDisplayed testDisplayed;
      for (EcdeTest ecdeTest : ecdeTests.getListTests()) {
         testDisplayed = new EcdeTestDisplayed(ecdeTest);
         testDisplayed.setChecked(true);
         testDisplayeds.add(testDisplayed);
      }

      session.getServletContext().setAttribute(ECDE_LIST, testDisplayeds);

      model.addAttribute("formulaire", form);

      return NOM_VUE;
   }
}
