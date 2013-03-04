/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.support;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.droit.dao.model.Prmd;
import fr.urssaf.image.sae.droit.model.SaeDroits;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.services.consultation.SAEConsultationService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.trace.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.trace.executable.exception.TraceExecutableException;
import fr.urssaf.image.sae.trace.model.Parameter;
import fr.urssaf.image.sae.trace.model.ParameterType;
import fr.urssaf.image.sae.trace.service.ParametersService;
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
   private ParametersService parametersService;

   @Autowired
   @Qualifier("saeConsultationService")
   private SAEConsultationService consultationService;

   @Autowired
   private CassandraServerBean serverBean;

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
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     ParameterNotFoundException.class, exception.getCause()
                           .getCause().getClass());
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le paramètre "
                     + ParameterType.JOURNALISATION_EVT_META_TITRE.toString()
                     + " n'existe pas", exception.getCause().getCause()
                     .getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testAppliProdObligatoire() {

      createParameter(ParameterType.JOURNALISATION_EVT_META_TITRE, "JournalisationTest");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     ParameterNotFoundException.class, exception.getCause()
                           .getCause().getClass());
         Assert
               .assertEquals(
                     "le message d'erreur doit etre correct",
                     "le paramètre "
                           + ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE
                                 .toString() + " n'existe pas", exception
                           .getCause().getCause().getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testAppliObligatoire() {
      createParameter(ParameterType.JOURNALISATION_EVT_META_TITRE, "JournalisationTest");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            "Appli prod");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     ParameterNotFoundException.class, exception.getCause()
                           .getCause().getClass());
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le paramètre "
                     + ParameterType.JOURNALISATION_EVT_META_CODE_ORGA
                           .toString() + " n'existe pas", exception.getCause()
                     .getCause().getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testRndObligatoire() {
      createParameter(ParameterType.JOURNALISATION_EVT_META_TITRE, "JournalisationTest");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            "Appli prod");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_ORGA,
            "Code orga");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     ParameterNotFoundException.class, exception.getCause()
                           .getCause().getClass());
         Assert.assertEquals("le message d'erreur doit etre correct",
               "le paramètre "
                     + ParameterType.JOURNALISATION_EVT_META_CODE_RND
                           .toString() + " n'existe pas", exception.getCause()
                     .getCause().getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testAppliTraitementObligatoire() {
      createParameter(ParameterType.JOURNALISATION_EVT_META_TITRE, "JournalisationTest");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            "Appli prod");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_ORGA,
            "Code orga");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_RND,
            "Code rnd");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     ParameterNotFoundException.class, exception.getCause()
                           .getCause().getClass());
         Assert
               .assertEquals(
                     "le message d'erreur doit etre correct",
                     "le paramètre "
                           + ParameterType.JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT
                                 .toString() + " n'existe pas", exception
                           .getCause().getCause().getMessage());

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testAppliFichierNonTrouvéObligatoire() {
      createParameter(ParameterType.JOURNALISATION_EVT_META_TITRE, "JournalisationTest");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            "Appli prod");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_ORGA,
            "Code orga");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_RND,
            "Code rnd");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT,
            "Appli traitement");

      try {
         support.capture("", new Date());
         Assert.fail("une exception TraceExecutableException est attendue");

      } catch (TraceExecutableException exception) {
         Assert
               .assertEquals(
                     "le message d'erreur d'origine doit etre un parametre non trouvé",
                     FileNotFoundException.class, exception.getCause()
                           .getCause().getClass());

      } catch (Exception exception) {
         Assert.fail("une exception TraceExecutableException est attendue");
      }
   }

   @Test
   public void testSucces() throws IOException, TraceExecutableException,
         SAEConsultationServiceException, UnknownDesiredMetadataEx,
         MetaDataUnauthorizedToConsultEx {
      createParameter(ParameterType.JOURNALISATION_EVT_META_TITRE, "JournalisationTest");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_PRODUCTRICE,
            "SAE");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_ORGA, "UR750");
      createParameter(ParameterType.JOURNALISATION_EVT_META_CODE_RND,
            "7.7.8.8.1");
      createParameter(
            ParameterType.JOURNALISATION_EVT_META_APPLICATION_TRAITEMENT,
            "SAET");
      ClassPathResource resource = new ClassPathResource(
            "capture/resultat_attendu.xml.ext.gz");
      File file = new File(resource.getURI());

      authentification();

      UUID uuid = support.capture(file.getAbsolutePath(), new Date());
      Assert.assertNotNull("l'uuid doit etre non null", uuid);

      UntypedDocument archive = consultationService.consultation(uuid);
      Assert.assertNotNull("le document doit etre non null", archive);

      List<UntypedMetadata> uMetadatas = archive.getUMetadatas();
      int index = 0;
      while (!"Hash".equals(uMetadatas.get(index).getLongCode())
            && index < uMetadatas.size()) {
         index++;
      }

      if (index == uMetadatas.size()) {
         Assert.fail("la propriété hash n'est pas trouvée");
      } else {
         Assert.assertEquals("le hash contenu doit etre correct",
               "bca50fcd3aa69f927df1a0775fe63e6882dc8913", uMetadatas
                     .get(index).getValue());
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
      prmd.setBean("permitAll");
      prmd.setCode("default");
      saePrmd.setPrmd(prmd);
      String[] roles = new String[] { "archivage_unitaire", "consultation" };
      saePrmds.add(saePrmd);

      saeDroits.put("archivage_unitaire", saePrmds);
      saeDroits.put("consultation", saePrmds);
      viExtrait.setSaeDroits(saeDroits);
      AuthenticationToken token = AuthenticationFactory.createAuthentication(
            viExtrait.getIdUtilisateur(), viExtrait, roles, viExtrait
                  .getSaeDroits());
      AuthenticationContext.setAuthenticationToken(token);

   }

   @Test
   public void captureSucces() {

   }

   private void createParameter(ParameterType type, Object value) {
      Parameter parameter = new Parameter(type, value);
      parametersService.saveParameter(parameter);
   }
}
