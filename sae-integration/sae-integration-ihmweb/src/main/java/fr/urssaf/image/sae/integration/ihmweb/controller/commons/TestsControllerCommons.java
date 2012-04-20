package fr.urssaf.image.sae.integration.ihmweb.controller.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.integration.ihmweb.config.TestConfig;
import fr.urssaf.image.sae.integration.ihmweb.modele.CasTest;
import fr.urssaf.image.sae.integration.ihmweb.service.ecde.EcdeService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielCasTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.referentiels.ReferentielMetadonneesService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.CaptureUnitaireTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.ConsultationTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.RechercheTestService;
import fr.urssaf.image.sae.integration.ihmweb.service.tests.utils.TestsMetadonneesService;

/**
 * Méthodes communes pour tous les tests
 */
@Component
public class TestsControllerCommons {

   @Autowired
   private EcdeService ecdeService;
   
   @Autowired
   private ReferentielCasTestService refCasTestService;
   
   @Autowired
   private ReferentielMetadonneesService refMetas;
   
   @Autowired
   private CaptureUnitaireTestService captUnitTestServ;
   
   @Autowired
   private RechercheTestService rechTestServ;
   
   @Autowired
   private TestConfig testConfig;
   
   @Autowired
   private ConsultationTestService consultTestServ;
   
   @Autowired
   private TestsMetadonneesService testMetasService;
   
   /**
    * Service de manipulation de l'ECDE
    * 
    * @return Service de manipulation de l'ECDE
    */
   public final EcdeService getEcdeService() {
      return ecdeService;
   }
   
   /**
    * Le service du référentiel des cas de test
    * 
    * @return Le service du référentiel des cas de test
    */
   public final ReferentielCasTestService getRefCasTestService() {
      return refCasTestService;
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
    * Service des tests de la fonctionnalité "Recherche"
    * 
    * @return Service des tests de la fonctionnalité "Recherche"
    */
   public final RechercheTestService getRechercheTestService() {
      return this.rechTestServ;
   }
   
   /**
    * @return the testConfig
    */
   protected final TestConfig getTestConfig() {
      return testConfig;
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
    * Service de tests des métadonnées
    * 
    * @return Service de tests des métadonnées
    */
   public final TestsMetadonneesService getTestsMetasService() {
      return this.testMetasService;
   }
   
   /**
    * Renvoie l'objet décrivant le cas de test
    * 
    * @return l'objet représentant le cas de test
    */
   public final CasTest getCasTest(String numeroTest) {
      return refCasTestService.getCasTest(numeroTest);
   }
   
}
