package fr.urssaf.image.sae.services.batch.restore.support.lucene.batch;

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
@ContextConfiguration(locations = { "/applicationContext-sae-services-restoremasse-test.xml" })
public class CheckDroitRestoreTaskletTest {

   @Autowired
   @Qualifier("restoreLauncher")
   private JobLauncherTestUtils launcher;
   
   private ExecutionContext context;
   
   @Before
   public void init() {
      context = new ExecutionContext();
      context.put(Constantes.RESTORE_EXCEPTION,
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
      String[] roles = new String[] { "restore_masse" };
      saePrmds.add(saePrmd);

      saeDroits.put("restore_masse", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);
   }
   
   /**
    * Lancer un test avec un identifiant de restore de masse valide
    * 
    */
   @Test
   public void testIdSuppressionMasseCorrecte() {

      Prmd prmd = new Prmd();
      prmd.setBean("permitAll");
      prmd.setCode("default");
      setAuthentification(prmd);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      
      mapParameter.put(Constantes.ID_TRAITEMENT_A_RESTORER, new JobParameter(UUID.randomUUID().toString()));
      
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);
      
      JobExecution execution = launcher.launchStep(
            "controleDroitRestoreStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());
      
      Assert.assertNotNull("La requete lucene finale doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_TRT_MASSE));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.RESTORE_EXCEPTION);
      Assert.assertTrue("Aucune exception n'aurait du être levée",
            listeErreurs.isEmpty());
   }
   
   /**
    * Lancer un test avec une requete lucene valide et un prmd restreint
    * 
    */
   @Test
   public void testIdSuppressionMassePrmdRestreint() {

      Prmd prmd = new Prmd();
      prmd.setLucene("ApplicationTraitement:TOTO AND DomaineCotisant:true");
      setAuthentification(prmd);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      
      mapParameter.put(Constantes.ID_TRAITEMENT_A_RESTORER, new JobParameter(UUID.randomUUID().toString()));
      
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleDroitRestoreStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status COMPLETED attendu", ExitStatus.COMPLETED,
            step.getExitStatus());
      
      Assert.assertNotNull("La requete lucene finale doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_TRT_MASSE));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.RESTORE_EXCEPTION);
      Assert.assertTrue("Aucune exception n'aurait du être levée",
            listeErreurs.isEmpty());
   }
   
   /**
    * Lancer un test avec une requete lucene pour une metadonnee inconnue
    * 
    */
   @Test
   public void testIdSuppressionMasseUnknownMetadata() {

      Prmd prmd = new Prmd();
      // test moche : pour provoquer cette erreur, on met un prmd restreint sur une metadonnee inconnue
      prmd.setLucene("Metadata:1234");
      setAuthentification(prmd);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      
      mapParameter.put(Constantes.ID_TRAITEMENT_A_RESTORER, new JobParameter(UUID.randomUUID().toString()));
      
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleDroitRestoreStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus());
      
      Assert.assertNull("La requete lucene finale ne doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_TRT_MASSE));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.RESTORE_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
      
      int nbDocsRestores = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_RESTORES);
      Assert.assertEquals("Aucun document n'aurait du être restoré", 0,
            nbDocsRestores);
   }
   
   /**
    * Lancer un test avec une requete lucene pour une metadonnee non rechercheable
    * 
    */
   @Test
   public void testIdSuppressionMasseNonSearcheableMetadata() {

      Prmd prmd = new Prmd();
      // test moche : pour provoquer cette erreur, on met un prmd restreint sur une metadonnee non rechercheable
      prmd.setLucene("NomFichier:1234");
      setAuthentification(prmd);
      
      // permet juste de rendre unique le job au niveau spring-batch
      Map<String, JobParameter> mapParameter = new HashMap<String, JobParameter>();
      
      mapParameter.put(Constantes.ID_TRAITEMENT_A_RESTORER, new JobParameter(UUID.randomUUID().toString()));
      
      mapParameter.put("id", new JobParameter(UUID.randomUUID().toString()));
      JobParameters parameters = new JobParameters(mapParameter);

      JobExecution execution = launcher.launchStep(
            "controleDroitRestoreStep", parameters, this.context);

      Collection<StepExecution> steps = execution.getStepExecutions();
      List<StepExecution> list = new ArrayList<StepExecution>(steps);

      StepExecution step = list.get(0);
      Assert.assertEquals("status FAILED attendu", ExitStatus.FAILED,
            step.getExitStatus());
      
      Assert.assertNull("La requete lucene finale ne doit être présent dans le contexte d'execution",
            step.getJobExecution().getExecutionContext().get(Constantes.REQ_FINALE_TRT_MASSE));
      
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> listeErreurs = (ConcurrentLinkedQueue<Exception>) step.getJobExecution()
            .getExecutionContext().get(Constantes.RESTORE_EXCEPTION);
      Assert.assertFalse("Une exception aurait du être levée",
            listeErreurs.isEmpty());
      
      int nbDocsRestores = step.getJobExecution().getExecutionContext()
            .getInt(Constantes.NB_DOCS_RESTORES);
      Assert.assertEquals("Aucun document n'aurait du être restoré", 0,
            nbDocsRestores);
   }
}
