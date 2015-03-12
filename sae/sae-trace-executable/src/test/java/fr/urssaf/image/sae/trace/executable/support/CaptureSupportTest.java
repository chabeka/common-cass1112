package fr.urssaf.image.sae.trace.executable.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.cassandra.helper.CassandraServerBean;
import fr.urssaf.image.commons.cassandra.support.clock.JobClockSupport;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.service.ParametersService;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.rnd.dao.support.RndSupport;
import fr.urssaf.image.sae.rnd.modele.TypeCode;
import fr.urssaf.image.sae.rnd.modele.TypeDocument;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationFactory;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-trace-executable-test.xml" })
public class CaptureSupportTest {

   @Autowired
   private CaptureSupport support;

   @Autowired
   private ParametersService paramService;

   @Autowired
   @Qualifier("saeConsultationService")
   private SAEConsultationService consultationService;

   @Autowired
   private CassandraServerBean serverBean;
   
   @Autowired
   private ParametersService parametersService;
   @Autowired 
   private RndSupport rndSupport;
   @Autowired
   private JobClockSupport jobClockSupport;

   @After
   public void after() throws Exception {
      serverBean.resetData();
   }

   @Test
   public void testTitreObligatoire() {
      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         checkExceptionParametreInexistant(exception,
               "JOURNALISATION_EVT_META_TITRE");

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   private void checkExceptionParametreInexistant(
         TraceExecutableException exception, String parametre) {

      Assert.assertEquals(
            "le message d'erreur d'origine doit etre un parametre non trouvé",
            ParameterNotFoundException.class, exception.getCause().getCause()
                  .getClass());

      String messageAttendu = "le paramètre " + parametre + " n'existe pas";
      String messageObtenu = exception.getCause().getCause().getMessage();
      Assert.assertEquals("le message d'erreur doit etre correct",
            messageAttendu, messageObtenu);

   }

   @Test
   public void testAppliProdObligatoire() {

      paramService.setJournalisationEvtMetaTitre("JournalisationTest");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         checkExceptionParametreInexistant(exception,
               "JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE");

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testAppliObligatoire() {

      paramService.setJournalisationEvtMetaTitre("JournalisationTest");
      paramService.setJournalisationEvtMetaApplProd("Appli prod");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         checkExceptionParametreInexistant(exception,
               "JOURNALISATION_EVT_META_CODE_ORGA");

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testRndObligatoire() {

      paramService.setJournalisationEvtMetaTitre("JournalisationTest");
      paramService.setJournalisationEvtMetaApplProd("Appli prod");
      paramService.setJournalisationEvtMetaCodeOrga("Code orga");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         checkExceptionParametreInexistant(exception,
               "JOURNALISATION_EVT_META_CODE_RND");

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testAppliTraitementObligatoire() {

      paramService.setJournalisationEvtMetaTitre("JournalisationTest");
      paramService.setJournalisationEvtMetaApplProd("Appli prod");
      paramService.setJournalisationEvtMetaCodeOrga("Code orga");
      paramService.setJournalisationEvtMetaCodeRnd("Code rnd");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         checkExceptionParametreInexistant(exception,
               "JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT");

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testAppliFichierNonTrouvéObligatoire() {

      paramService.setJournalisationEvtMetaTitre("JournalisationTest");
      paramService.setJournalisationEvtMetaApplProd("Appli prod");
      paramService.setJournalisationEvtMetaCodeOrga("Code orga");
      paramService.setJournalisationEvtMetaCodeRnd("Code rnd");
      paramService.setJournalisationEvtMetaApplTrait("Appli traitement");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         Assert.assertEquals(
               "le message d'erreur d'origine doit etre un fichier non trouvé",
               FileNotFoundException.class, exception.getCause().getCause()
                     .getClass());

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testSucces() throws IOException, TraceExecutableException,
         SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {
      
      // Paramétrage du RND
      parametersService.setVersionRndDateMaj(new Date());
      parametersService.setVersionRndNumero("11.2");
      
      TypeDocument typeDocCree = new TypeDocument();
      typeDocCree.setCloture(false);
      typeDocCree.setCode("7.7.8.8.1");
      typeDocCree.setCodeActivite("7");
      typeDocCree.setCodeFonction("7");
      typeDocCree.setDureeConservation(1825);
      typeDocCree.setLibelle("ATTESTATION DE VIGILANCE");
      typeDocCree.setType(TypeCode.ARCHIVABLE_AED);
      
      rndSupport.ajouterRnd(typeDocCree, jobClockSupport.currentCLock());

      paramService.setJournalisationEvtMetaTitre("JournalisationTest");
      paramService.setJournalisationEvtMetaApplProd("SAE");
      paramService.setJournalisationEvtMetaCodeOrga("UR750");
      paramService.setJournalisationEvtMetaCodeRnd("7.7.8.8.1");
      paramService.setJournalisationEvtMetaApplTrait("SAET");

      ClassPathResource resource = new ClassPathResource(
            "capture/resultat_attendu.xml.gz");
      File file = new File(resource.getURI());

      authentification();
      
      UUID uuid = support.capture(file.getAbsolutePath(), new Date());
      Assert.assertNotNull("l'uuid doit etre non null", uuid);
      
      //-- Paramètres de consultation
      String[] metadatas = {"Hash", "DomaineTechnique"};
      ConsultParams consulparams = new ConsultParams(uuid, Arrays.asList(metadatas));
      
      UntypedDocument archive = consultationService.consultation(consulparams);
      Assert.assertNotNull("le document doit etre non null", archive);
      
      List<UntypedMetadata> uMetadatas = archive.getUMetadatas();
      
      //-- On recherche dans les métas du documents
      String hashValue = null;
      String dteValue = null;
      for (int i = 0; i < uMetadatas.size(); i++) {
         if(hashValue != null && uMetadatas.get(i).getLongCode().equals("Hash")){
            hashValue = uMetadatas.get(i).getValue();
         }
         
         if(dteValue != null && uMetadatas.get(i).getLongCode().equals("DomaineTechnique")){
            dteValue = uMetadatas.get(i).getValue();
         }
      }
      
      if (hashValue == null) {
         Assert.fail("la propriété hash n'est pas trouvée");
      } else {
         Assert.assertEquals("le hash contenu doit etre correct",
               "bca50fcd3aa69f927df1a0775fe63e6882dc8913", hashValue);
      }

      if (dteValue == null) {
         Assert.fail("la propriété DomaineTechnique n'est pas trouvée");
      } else {
         Assert.assertEquals("le flag DomaineTechnique n'est pas correct",
               true, dteValue);
      }
   }

   private void authentification() {
      // initialisation du contexte de sécurité
      VIContenuExtrait viExtrait = new VIContenuExtrait();
      viExtrait.setCodeAppli("SAE");
      viExtrait.setIdUtilisateur("TRACE EXECUTABLE");

      SaeDroits saeDroits = new SaeDroits();
      List<SaePrmd> saePrmds = new ArrayList<SaePrmd>();
      SaePrmd saePrmd = new SaePrmd();
      saePrmd.setValues(new HashMap<String, String>());
      Prmd prmd = new Prmd();
      prmd.setBean("permitDomaineTechnique");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_unitaire", "consultation" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("consultation", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles);
      AuthenticationContext.setAuthenticationToken(token);

   }

}
