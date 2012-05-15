package fr.urssaf.image.commons.maquette.session;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.tool.MenuItem;
import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;


/**
 * Tests unitaires de la classe {@link SessionTools}
 *
 */
@SuppressWarnings("PMD")
public class SessionToolsTest {

   
   /**
    * Test du constructeur privé, pour le code coverage 
    * 
    * @throws TestConstructeurPriveException 
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(SessionTools.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test de la mise en session de la configuration de la maquette
    * 
    * @throws MaquetteThemeException
    */
   @Test
   public void maquetteConfig()
   throws
   MaquetteThemeException {
      
      // Création des objets mock
      MockHttpServletRequest request = new MockHttpServletRequest();
      MockFilterConfig filterConfig = new MockFilterConfig(); 
      
      // Variables
      MaquetteFilterConfig maquetteFilterConfigSess;
      
      // Test de la non-présence en session
      maquetteFilterConfigSess = SessionTools.getFilterConfig(request);
      assertNull("L'objet MaquetteConfig n'aurait pas dû être stocké en session",maquetteFilterConfigSess);
      
      // Test de la mise en session
      MaquetteFilterConfig maquetteFilterConfig = new MaquetteFilterConfig(filterConfig);
      SessionTools.storeFilterConfig(request, maquetteFilterConfig);
      maquetteFilterConfigSess = SessionTools.getFilterConfig(request);
      assertNotNull("L'objet MaquetteConfig aurait dû être stocké en session",maquetteFilterConfigSess);
      
   }
   
   
   
   /**
    * Test de la mise en session du menu en cours
    * 
    * @throws MaquetteFilterConfigException
    * @throws MaquetteThemeException
    */
   @Test
   public void selectedMenu() {
      
      // Création des objets mock
      MockHttpServletRequest request = new MockHttpServletRequest();
      
      // Variables
      MenuItem selectedMenuSess;
      
      // Test de la non-présence en session
      selectedMenuSess = SessionTools.getSelectedMenu(request);
      assertNull("Le menu en cours n'aurait pas dû être stocké en session",selectedMenuSess);
      
      // Test de la mise en session
      MenuItem selectedMenu = new MenuItem();
      SessionTools.storeSelectedMenu(request, selectedMenu);
      selectedMenuSess = SessionTools.getSelectedMenu(request);
      assertNotNull("Le menu en cours aurait dû être stocké en session",selectedMenuSess);
      
   }
   
   
}
