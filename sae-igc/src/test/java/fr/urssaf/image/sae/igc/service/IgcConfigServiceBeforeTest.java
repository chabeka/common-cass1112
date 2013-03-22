package fr.urssaf.image.sae.igc.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;

import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.sae.igc.component.IgcConfigServiceValidate;
import fr.urssaf.image.sae.igc.exception.IgcConfigException;
import fr.urssaf.image.sae.igc.modele.IgcConfigs;
import fr.urssaf.image.sae.igc.util.TextUtils;

/**
 * Test sur les arguments entrée de {@link IgcConfigService}
 * 
 * 
 */
@SuppressWarnings( { "PMD.MethodNamingConventions" })
public class IgcConfigServiceBeforeTest {

   private static final String FAIL_MESSAGE = "le test doit échouer";

   private IgcConfigService service;

   @Before
   public void before() throws MalformedURLException {

      service = new IgcConfigService() {

         @Override
         public IgcConfigs loadConfig(String pathConfigFile)
               throws IgcConfigException {

            IgcConfigs igcConfigs = new IgcConfigs();

            return igcConfigs;
         }
      };

   }

   @Test
   public void loadConfig_failure_pathconfig_required()
         throws IgcConfigException {

      String pathConfig = " ";

      try {
         service.loadConfig(pathConfig);
         fail(FAIL_MESSAGE);
      } catch (IllegalArgumentException e) {

         assertEquals("erreur la cause de l'exception", TextUtils.getMessage(
               TextUtils.ARG_EMPTY, "pathConfigFile"), e.getMessage());
      }

   }

   @Test
   public void loadConfig_failure_pathconfig_notexist()
         throws IgcConfigException {

      String pathConfig = "/notExist/igcConfig/igcConfig.xml";

      try {
         service.loadConfig("/notExist/igcConfig/igcConfig.xml");
         fail(FAIL_MESSAGE);
      } catch (IgcConfigException e) {

         assertEquals("erreur la cause de l'exception", TextUtils.getMessage(
               IgcConfigServiceValidate.IGC_CONFIG_NOTEXIST, pathConfig), e
               .getMessage());
      }
   }

}
