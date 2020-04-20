package fr.urssaf.image.sae.vi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.vi.exception.VIFormatTechniqueException;
import fr.urssaf.image.sae.vi.exception.VIInvalideException;
import fr.urssaf.image.sae.vi.exception.VISignatureException;
import fr.urssaf.image.sae.vi.exception.VIVerificationException;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;
import fr.urssaf.image.sae.vi.modele.VISignVerifParams;
import fr.urssaf.image.sae.vi.testutils.TuGenererVi;
import fr.urssaf.image.sae.vi.testutils.TuUtils;
import fr.urssaf.image.sae.vi.util.XMLUtils;

@SuppressWarnings( { "PMD.MethodNamingConventions", "PMD.TooManyMethods",
      "PMD.ExcessiveImports" })
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-sae-vi-full-test.xml" })
public class WebServiceVIServiceTest {

   @Autowired
   private WebServiceVIService service;

   @Test
   @SuppressWarnings("PMD.JUnitAssertionsShouldIncludeMessage")
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_success() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_success.xml");

      VIContenuExtrait extrait = service.verifierVIdeServiceWeb(identification,
            TuGenererVi.SERVICE_VISE, TuUtils.buildSignVerifParamsOK(), true);

      assertEquals(TuGenererVi.ID_UTILISATEUR, extrait.getIdUtilisateur());
      assertEquals("les actions unitaires ne sont pas toutes présentes", 4,
            extrait.getSaeDroits().keySet());

      

      assertEquals("Portail Image", extrait.getCodeAppli());

   }

   @Test(expected = VIFormatTechniqueException.class)
   public void verifierVIdeServiceWeb_failure_format() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_format.xml");

      service.verifierVIdeServiceWeb(identification, TuGenererVi.SERVICE_VISE,
            new VISignVerifParams(), true);

   }

   @Test(expected = VISignatureException.class)
   public void verifierVIdeServiceWeb_failure_sign() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_sign.xml");

      service.verifierVIdeServiceWeb(identification, TuGenererVi.SERVICE_VISE,
            new VISignVerifParams(), true);

   }

   @Test
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_failure_id_1() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_id_1.xml");

      try {

         service
               .verifierVIdeServiceWeb(identification,
                     TuGenererVi.SERVICE_VISE,
                     TuUtils.buildSignVerifParamsOK(), true);

         fail("Une exception de type VIInvalideException était attendue");

      } catch (VIInvalideException ex) {

         assertEquals(
               "Vérification du message de l'exception",
               "L'ID de l'assertion doit être un UUID correct (ce qui n'est pas le cas de 'bad id')",
               ex.getMessage());

      }

   }

   @Test
   @Ignore("Désactivation du test dans l'attente d'un processus de mise à jour des CRL")
   public void verifierVIdeServiceWeb_failure_id_2() throws IOException,
         SAXException, VIVerificationException {

      Element identification = XMLUtils
            .parse("src/test/resources/webservice/vi_failure_id_2.xml");

      try {

         service
               .verifierVIdeServiceWeb(identification,
                     TuGenererVi.SERVICE_VISE,
                     TuUtils.buildSignVerifParamsOK(), true);

         fail("Une exception de type VIInvalideException était attendue");

      } catch (VIInvalideException ex) {

         assertEquals(
               "Vérification du message de l'exception",
               "L'ID de l'assertion doit être un UUID correct (ce qui n'est pas le cas de 'pfx5d541dee-4468-74d2-7cbe-03078ef284e7')",
               ex.getMessage());

      }

   }

}
