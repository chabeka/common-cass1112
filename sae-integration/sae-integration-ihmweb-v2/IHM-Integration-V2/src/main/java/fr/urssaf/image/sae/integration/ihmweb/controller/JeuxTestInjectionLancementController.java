package fr.urssaf.image.sae.integration.ihmweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.controller.components.EcdeTestDisplayed;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestInjectionFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTests;

/**
 * Jeux de tests à injecter : Lancement de l'injection
 */
@Controller
@RequestMapping(value = "jeuxTestInjectionLancement")
public class JeuxTestInjectionLancementController {

   private static final String ECDE_LIST = "ecdeListe";
   
   private static final String NOM_VUE = "jeuxTestInjectionLancement";

   @Autowired
   private TestConfig testConfig;

   @Autowired
   private EcdeTests ecdeTests;

   @Autowired
   private ThreadInitCasDeTest casDeTest;

   /**
    * initialisation
    * 
    * @param session
    *           session utilisateur
    * @return la page de redirection
    */
   @RequestMapping(method = RequestMethod.GET)
   public final String defaultView(HttpSession session) {

      if (!ThreadInitCasDeTest.EtatThread.RUNNING.equals(casDeTest.getEtat())
            && session.getServletContext().getAttribute(ECDE_LIST) == null) {
         List<EcdeTestDisplayed> testDisplayeds = new ArrayList<EcdeTestDisplayed>();

         EcdeTestDisplayed testDisplayed;
         for (EcdeTest ecdeTest : ecdeTests.getListTests()) {
            testDisplayed = new EcdeTestDisplayed(ecdeTest);
            testDisplayed.setChecked(true);
            testDisplayeds.add(testDisplayed);
         }

         session.getServletContext().setAttribute(ECDE_LIST, testDisplayeds);
      }

      return NOM_VUE;
   }

   /**
    * Retourne la liste des tests
    * 
    * @param model
    *           données spring
    * @param session
    *           session utilisateur
    * @return la liste des tests
    */
   @ResponseBody
   @RequestMapping(method = RequestMethod.GET, params = "action=getList")
   public final HashMap<String, Object> loadTable(Model model,
         HttpSession session) {

      List<EcdeTestDisplayed> testDisplayeds = (List<EcdeTestDisplayed>) session
            .getServletContext().getAttribute(ECDE_LIST);

      HashMap<String, Object> map = new HashMap<String, Object>();

      map.put("tests", testDisplayeds);

      return map;
   }

   /**
    * Retourne le status du traitement
    * 
    * @param model
    *           données spring
    * @return le status du traitement : true si en cours d'execution
    */
   @ResponseBody
   @RequestMapping(method = RequestMethod.GET, params = { "action=checkStatus" })
   public final HashMap<String, Object> checkStatus(Model model) {
      HashMap<String, Object> map = new HashMap<String, Object>();

      boolean status = ThreadInitCasDeTest.EtatThread.RUNNING.equals(casDeTest
            .getEtat());

      map.put("success", status);

      return map;
   }

   /**
    * Retourne l'url du WS ainsi que le status de traitement
    * 
    * @param model
    *           données Spring
    * @return l'url du WS ainsi que le status de traitement
    */
   @ResponseBody
   @RequestMapping(method = RequestMethod.GET, params = { "action=getUrl" })
   public final HashMap<String, Object> getUrl(Model model) {
      HashMap<String, Object> map = new HashMap<String, Object>();
      map.put("success", true);
      map.put("url", testConfig.getUrlSaeService());
      map.put("isRunning", ThreadInitCasDeTest.EtatThread.RUNNING
            .equals(casDeTest.getEtat()));

      return map;
   }

   /**
    * Lance le traitement
    * 
    * @param formulaire
    *           formulaire de données
    * @param session
    *           session utilisateur
    * @return état de lancement du traitement (true [lancé] ou false[non lancé])
    */
   @ResponseBody
   @RequestMapping(method = RequestMethod.POST, params = "action=launch")
   public final HashMap<String, Object> launchTreatment(
         TestInjectionFormulaire formulaire, HttpSession session) {

      HashMap<String, Object> map = new HashMap<String, Object>();

      if (casDeTest.getEtat() == null
            || ThreadInitCasDeTest.EtatThread.FINISHED.equals(casDeTest
                  .getEtat())) {

         List<String> listTraitement = formulaire.getTreatmentList();

         List<EcdeTestDisplayed> testDisplayeds = (List<EcdeTestDisplayed>) session
               .getServletContext().getAttribute(ECDE_LIST);

         if (listTraitement != null && !listTraitement.isEmpty()) {

            for (EcdeTestDisplayed ecdeTestDisplayed : testDisplayeds) {
               ecdeTestDisplayed.setErreur(null);

               if (listTraitement.contains(ecdeTestDisplayed.getUrl())) {
                  ecdeTestDisplayed.setChecked(true);
                  ecdeTestDisplayed
                        .setStatusTraitement(ThreadInitCasDeTest.WAITING);

               } else {
                  ecdeTestDisplayed.setChecked(false);
                  ecdeTestDisplayed
                        .setStatusTraitement(ThreadInitCasDeTest.NOT_CONCERNED);
               }
            }

            casDeTest.setSession(session);
            casDeTest.setUrlWs(formulaire.getUrl());
            casDeTest.run();

            map.put("success", true);
         } else {
            map.put("success", false);
         }
      } else {
         map.put("success", false);
      }

      return map;
   }
}
