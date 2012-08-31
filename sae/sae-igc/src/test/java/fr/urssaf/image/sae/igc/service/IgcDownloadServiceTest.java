package fr.urssaf.image.sae.igc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.igc.exception.IgcDownloadException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.util.TextUtils;

@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class IgcDownloadServiceTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private IgcDownloadService service;

   @Before
   public void before() throws MalformedURLException {

      service = new IgcDownloadService() {

         @Override
         public int telechargeCRLs(IgcConfigs igcConfigs)
               throws IgcDownloadException {

            return 0;
         }

      };

   }

   @Test
   public void telechargeCRLs_success() throws IgcDownloadException {

      IgcConfigs igcConfigs = new IgcConfigs();
      assertNotNull("les arguments doivent être valides", service
            .telechargeCRLs(igcConfigs));
   }

   @Test
   public void telechargeCRLs_failure_igcConfig_required()
         throws IgcDownloadException {

      try {
         service.telechargeCRLs(null);
         fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {

         assertEquals("erreur la cause de l'exception", TextUtils.getMessage(
               TextUtils.ARG_EMPTY, "igcConfig"), e.getMessage());
      }
   }
}
