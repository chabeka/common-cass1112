package fr.urssaf.image.sae.services.batch.suppression.support.lucene.batch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-services-suppressionmasse-test.xml" })
public class CheckDroitRequeteLuceneTaskletTest {

   @Autowired
   @Qualifier("suppressionLauncher")
   private JobLauncherTestUtils launcher;
   
   private ExecutionContext context;
   
   @Before
   public void init() {
      context = new ExecutionContext();
      context.put(Constantes.SUPPRESSION_EXCEPTION,
            new ConcurrentLinkedQueue<Exception>());
   }
   
   private void setAuthentification(Prmd prmd) {
      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("TESTS_UNITAIRES");
      viExtrait.setIdUtilisateur("UTILISATEUR TEST");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "suppression_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("suppression_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }
   
   /**
    * Lancer un test avec une requete lucene valide
    * 
    */
   @Test
   public void testRequeteLuceneCorrecte() {

      String requete = "Siret:123456";
      
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);
      
      this.context.put(Constantes.REQ_LUCENE_SUPPRESSION, requete);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleDroitRequeteLuceneStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());
      
      Assert.assertNotNull("La requete lucene finale doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_SUPPRESSION));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertTrue("Aucune exception n'aurait du être levée",
            listeErreurs.isEmpty());
   }
   
   /**
    * Lancer un test avec une requete lucene valide et un prmd restreint
    * 
    */
   @Test
   public void testRequeteLuceneCorrectePrmdRestreint() {

      String requete = "Siret:123456";
      
      Prmd prmd = new Prmd();
      prmd.setLucene("ApplicationTraitement:TOTO AND DomaineCotisant:true");
      setAuthentification(prmd);
      
      this.context.put(Constantes.REQ_LUCENE_SUPPRESSION, requete);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleDroitRequeteLuceneStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());
      
      Assert.assertNotNull("La requete lucene finale doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_SUPPRESSION));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertTrue("Aucune exception n'aurait du être levée",
            listeErreurs.isEmpty());
   }
   
   /**
    * Lancer un test avec une requete lucene pour une metadonnee inconnue
    * 
    */
   @Test
   public void testRequeteLuceneUnknownMetadata() {

      String requete = "Metadata:1234";
      
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);
      
      this.context.put(Constantes.REQ_LUCENE_SUPPRESSION, requete);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleDroitRequeteLuceneStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus());
      
      Assert.assertNull("La requete lucene finale ne doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_SUPPRESSION));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
   }
   
   /**
    * Lancer un test avec une requete lucene pour une metadonnee non rechercheable
    * 
    */
   @Test
   public void testRequeteLuceneNonSearcheableMetadata() {

      String requete = "NomFichier:1234";
      
      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);
      
      this.context.put(Constantes.REQ_LUCENE_SUPPRESSION, requete);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleDroitRequeteLuceneStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus());
      
      Assert.assertNull("La requete lucene finale ne doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_SUPPRESSION));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.SUPPRESSION_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
   }
}
