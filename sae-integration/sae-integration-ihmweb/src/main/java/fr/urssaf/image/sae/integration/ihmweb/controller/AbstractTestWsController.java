package fr.urssaf.image.sae.integration.ihmweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.formulaire.TestWsParentFormulaire;
import fr.urssaf.image.sae.integration.ihmweb.modele.CasTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTest;
import fr.urssaf.image.sae.integration.ihmweb.modele.ecde.EcdeTests;
import fr.urssaf.image.sae.integration.ihmweb.saeservice.service.SaeServiceTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielCasTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielSoapFaultService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.AjoutNoteTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.CaptureMasseTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.CaptureUnitaireTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.ConsultationAffichableTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.ConsultationTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.GetDocFormatOrigineTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.ModificationTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.RechercheAvecNbResTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.RechercheParIterateurTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.RechercheTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.RestoreMasseTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.StockageUnitaireTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.SuppressionTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.SuppressionMasseTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.TransfertTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.utils.TestsMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.utils.ControllerUtils;
import fr.urssaf.image.sae.integration.ihmweb.utils.ModelUtils;

/**
 * Classe mère pour tous les contrôleurs pour les tests du service web
 * SaeService
 * 
 * @param <T>
 *           l'objet formulaire associé au contrôleur
 */
public abstract class AbstractTestWsController<T extends TestWsParentFormulaire> {

   @Autowired
   private TestConfig testConfig;

   @Autowired
   private ModelUtils modelUtils;

   @Autowired
   private SaeServiceTestService saeWsTestUtils;

   @Autowired
   private ReferentielSoapFaultService refSoapFault;

   @Autowired
   private ReferentielMetadonneesService refMetas;

   @Autowired
   private ReferentielCasTestService refCasTestService;

   @Autowired
   private TestsMetadonneesService testMetasService;

   @Autowired
   private EcdeService ecdeService;

   @Autowired
   private ConsultationTestService consultTestServ;
   
   @Autowired
   private GetDocFormatOrigineTestService getDocFormatOriginetTestServ;
 
   @Autowired
   private ConsultationAffichableTestService consultAffichableTestServ;

   @Autowired
   private CaptureUnitaireTestService captUnitTestServ;

   @Autowired
   private StockageUnitaireTestService stockUnitTestServ;
   
   @Autowired
   private CaptureMasseTestService captMassTestServ;

   @Autowired
   private RechercheTestService rechTestServ;
   
   @Autowired
   private RechercheAvecNbResTestService rechWithNbResTestServ;
   
   @Autowired
   private RechercheParIterateurTestService rechParIterateurTestServ;
   
   @Autowired
   private ModificationTestService modifTestServ;
   
   @Autowired
   private SuppressionTestService suppressionTestServ;
   
   @Autowired
   private TransfertTestService transfertTestServ;
   
   @Autowired
   private EcdeTests ecdeTests;

   @Autowired
   private AjoutNoteTestService ajoutNoteTestServ;

   @Autowired
   private SuppressionMasseTestService suppressionMasseTestServ;

   @Autowired
   private RestoreMasseTestService restoreMasseTestServ;


   /**
    * Service utilitaires pour les tests du service web SaeService
    * 
    * @return Service utilitaires pour les tests du service web SaeService
    */
   public final SaeServiceTestService getSaeWsTestUtils() {
      return this.saeWsTestUtils;
   }

   /**
    * Service du référentiel des soap fault
    * 
    * @return Service du référentiel des soap fault
    */
   public final ReferentielSoapFaultService getRefSoapFault() {
      return this.refSoapFault;
   }

   /**
    * Service du référentiel des métadonnées
    * 
    * @return Service du référentiel des métadonnées
    */
   public final ReferentielMetadonneesService getRefMetas() {
      return this.refMetas;
   }

   /**
    * Service de tests des métadonnées
    * 
    * @return Service de tests des métadonnées
    */
   public final TestsMetadonneesService getTestsMetasService() {
      return this.testMetasService;
   }

   /**
    * Service de manipulation de l'ECDE
    * 
    * @return Service de manipulation de l'ECDE
    */
   public final EcdeService getEcdeService() {
      return ecdeService;
   }

   /**
    * Service des tests de l'opération "consultation" du service web SaeService
    * 
    * @return Service des tests de l'opération "consultation" du service web
    *         SaeService
    */
   public final ConsultationTestService getConsultationTestService() {
      return consultTestServ;
   }

   /**
    * Service des tests de l'opération "getDocFormatOrigine" du service web SaeService
    * 
    * @return Service des tests de l'opération "getDocFormatOrigine" du service web
    *         SaeService
    */
   public final GetDocFormatOrigineTestService getGetDocFormatOrigineTestService() {
      return getDocFormatOriginetTestServ;
   }

   /**
    * Service des tests de l'opération "consultationAffichable" du service web SaeService
    * 
    * @return Service des tests de l'opération "consultationAffichable" du service web
    *         SaeService
    */
   public final ConsultationAffichableTestService getConsultationAffichableTestService() {
      return consultAffichableTestServ;
   }
   
   /**
    * Service des tests de l'opération "capture unitaire" du service web
    * SaeService
    * 
    * @return Service des tests de l'opération "capture unitaire" du service web
    *         SaeService
    */
   public final CaptureUnitaireTestService getCaptureUnitaireTestService() {
      return captUnitTestServ;
   }

   /**
    * Service des tests de l'opération "stockage unitaire" du service web
    * SaeService
    * 
    * @return Service des tests de l'opération "stockage unitaire" du service web
    *         SaeService
    */
   public final StockageUnitaireTestService getStockageUnitaireTestService() {
      return stockUnitTestServ;
   }
   
   /**
    * Service des tests de la fonctionnalité "Capture de masse"
    * 
    * @return Service des tests de la fonctionnalité "Capture de masse"
    */
   public final CaptureMasseTestService getCaptureMasseTestService() {
      return this.captMassTestServ;
   }

   /**
    * Service des tests de la fonctionnalité "Recherche"
    * 
    * @return Service des tests de la fonctionnalité "Recherche"
    */
   public final RechercheTestService getRechercheTestService() {
      return this.rechTestServ;
   }
   
   /**
    * Service des tests de la fonctionnalité "Modification"
    * 
    * @return Service des tests de la fonctionnalité "Modification"
    */
   public final ModificationTestService getModificationTestService() {
      return this.modifTestServ;
   }

   /**
    * Service des tests de la fonctionnalité "Suppression"
    * 
    * @return Service des tests de la fonctionnalité "Suppression"
    */
   public final SuppressionTestService getSuppressionTestService() {
      return this.suppressionTestServ;
   }
   
   /**
    * Service des tests de la fonctionnalité "Transfert"
    * 
    * @return Service des tests de la fonctionnalité "Transfert"
    */
   public final TransfertTestService getTransfertTestService() {
      return this.transfertTestServ;
   }
   
   
   /**
    * Service des tests de la fonctionnalité "Recherche avec nombre de résultats"
    * 
    * @return Service des tests de la fonctionnalité "Recherche avec nombre de résultats"
    */
   public final RechercheAvecNbResTestService getRechAvecNbResTestService() {
      return this.rechWithNbResTestServ;
   }
   
   /**
    * Service des tests de la fonctionnalité "Recherche par itérateur"
    * 
    * @return Service des tests de la fonctionnalité "Recherche par itérateur"
    */
   public final RechercheParIterateurTestService getRechercheParIterateurTestService() {
      return this.rechParIterateurTestServ;
   }
   
   /**
    * Service des tests de l'opération "ajoutNote" du service web SaeService
    * 
    * @return Service des tests de l'opération "ajoutNote" du service web
    *         SaeService
    */
   public final AjoutNoteTestService getAjoutNoteTestService() {
      return this.ajoutNoteTestServ;
   }

   /**
    * Service des tests de l'opération "suppressionMasse" du service web SaeService
    * 
    * @return Service des tests de l'opération "suppressionMasse" du service web
    *         SaeService
    */
   public final SuppressionMasseTestService getSuppressionMasseTestService() {
      return this.suppressionMasseTestServ;
   }
   

   /**
    * Service des tests de l'opération "restoreMasse" du service web SaeService
    * 
    * @return Service des tests de l'opération "restoreMasse" du service web
    *         SaeService
    */
   public final RestoreMasseTestService getRestoreMasseTestService() {
      return this.restoreMasseTestServ;
   }   
   
   /**
    * Renvoie le numéro du test
    * 
    * @return le numéro du test
    */
   protected abstract String getNumeroTest();

   /**
    * Renvoie le formulaire à mettre dans le GET
    * 
    * @return le formulaire à mettre dans le GET
    */
   protected abstract T getFormulairePourGet();

   protected String getNomVue() {
      return "test" + getNumeroTest();
   }

   /**
    * Déclare les classes de transtypage
    * 
    * @param binder
    *           l'objet dans lequel déclarer les classes de transtypage
    */
   @InitBinder
   public final void initBinder(WebDataBinder binder) {
      ControllerUtils.addAllBinders(binder);
   }

   /**
    * Le GET
    * 
    * @param model
    *           le modèle
    * 
    * @return le nom de la vue
    */
   @RequestMapping(method = RequestMethod.GET)
   protected final String getDefaultView(int id, Model model) {

      // Ajoute la description du cas de test dans le modèle
      modelUtils.ajouteCasTestDansModele(getNumeroTest(), model);

      // Initialise le modèle
      TestWsParentFormulaire formulaire = getFormulairePourGet();
      model.addAttribute("formulaire", formulaire);
      model.addAttribute("id", id);

      // Définition de l'URL par défaut du service web SaeService
      formulaire.setUrlServiceWeb(testConfig.getUrlSaeService());

      // Renvoie le nom de la vue à afficher
      return getNomVue();

   }

   /**
    * Le POST
    * 
    * @param model
    *           le modèle
    * @param formulaire
    *           l'objet formulaire
    * @return le nom de la vue
    */
   @RequestMapping(method = RequestMethod.POST)
   protected final String post(int id, Model model,
         @ModelAttribute("formulaire") T formulaire) {

      // Ajoute la description du cas de test dans le modèle
      modelUtils.ajouteCasTestDansModele(getNumeroTest(), model);
      
      model.addAttribute("id", id);
      
      // Appel de la méthode de traitement
      doPost(formulaire);

      // Renvoie le nom de la vue à afficher
      return this.getNomVue();

   }

   /**
    * Effectue les actions du POST
    * 
    * @param formulaire
    *           l'objet formulaire
    */
   protected abstract void doPost(T formulaire);

   /**
    * Le service du référentiel des cas de test
    * 
    * @return Le service du référentiel des cas de test
    */
   public final ReferentielCasTestService getRefCasTestService() {
      return refCasTestService;
   }

   /**
    * Renvoie l'objet décrivant le cas de test
    * 
    * @return l'objet représentant le cas de test
    */
   public final CasTest getCasTest() {
      return refCasTestService.getCasTest(getNumeroTest());
   }

   /**
    * @return the testConfig
    */
   protected final TestConfig getTestConfig() {
      return testConfig;
   }
   
   /**
    * Renvoie l'objet décrivant le test injecté.
    * 
    * @return EcdeTest
    */
   public final EcdeTest getEcdeTest() {
      EcdeTest retour = null;
      
      // recupère le cas de test
      CasTest casTest = getCasTest();
      
      // parcours la liste test injecté pour trouvé le bon
      for (EcdeTest testCourant : ecdeTests.getListTests()) {
         if (testCourant.getName().equals(casTest.getCode())) {
            retour = testCourant;
            break;
         }
      }
      return retour;
   }

}
