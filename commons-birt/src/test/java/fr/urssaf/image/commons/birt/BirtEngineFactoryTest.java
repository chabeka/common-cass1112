package fr.urssaf.image.commons.birt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtException;
import fr.urssaf.image.commons.birt.exception.MissingKeyInHashMapBirtEngineFactoryException;
import fr.urssaf.image.commons.birt.exception.NullFactoryBirtEngineException;
import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;


@SuppressWarnings({"PMD.TooManyMethods","PMD.AvoidDuplicateLiterals"})
public class BirtEngineFactoryTest {
	
   private BirtEngine birtEngine = null ;
	
	private String reportEngineHome = null ;
	private final String logsPath = System.getProperty("java.io.tmpdir") ;
	
	@Before
	public void setUp() throws Exception {

	   // setup reportEngineHome
	   reportEngineHome = BirtTools.getBirtHomeFromEnvVar() ;
	}

	@After
	public void tearDown() throws Exception {
	
		killBirtEngineInstance();
		
	}
	
	private void killBirtEngineInstance()
	{
	   if( birtEngine != null )
      {
         BirtEngine.killInstance();
         birtEngine = null ; //NOPMD
      }
	}
	
	
	/**
	 * Test unitaire du constructeur privé, pour le code coverage
	 * @throws TestConstructeurPriveException en cas de problème dans le constructeur privé
	 */
	@Test
	public void testConstructeurPrive() throws TestConstructeurPriveException
	{
	   Boolean result = TestsUtils.testConstructeurPriveSansArgument(BirtEngineFactory.class);
	   assertTrue("Le constructeur privé n'a pas été trouvé",result);
	}
	
	
	/**
	 * @throws NullFactoryBirtEngineException 
	 * @throws BirtException 
	 * @throws MissingConstructorParamBirtException 
	 * @throws MissingKeyInHashMapBirtEngineFactoryException 
	 */
	@Test
	public void testgetBirtEngineInstance1() 
	throws MissingKeyInHashMapBirtEngineFactoryException, 
   	MissingConstructorParamBirtException, 
   	BirtException, 
   	NullFactoryBirtEngineException {
	   
	   // Démarrage du serveur Birt
      Map<String, Object> factoryParams = new HashMap<String,Object>();
      factoryParams.put(BirtEngineFactoryKeys.REPORT_ENGINE_HOME , reportEngineHome ) ;
      factoryParams.put(BirtEngineFactoryKeys.LOG_PATH, logsPath);
      
      birtEngine = BirtEngineFactory.getBirtEngineInstance(
            BirtEngineFactory.EnumTypeApplication.JAVA_APP,
            factoryParams ) ;
      
      assertNotNull("testgetBirtEngineInstance1",birtEngine);
      
	}
	
	/**
    * @throws NullFactoryBirtEngineException 
    * @throws BirtException 
    * @throws MissingConstructorParamBirtException 
    * @throws MissingKeyInHashMapBirtEngineFactoryException 
    */
   @Test
   public void testgetBirtEngineInstance2() 
   throws MissingKeyInHashMapBirtEngineFactoryException, 
      MissingConstructorParamBirtException, 
      BirtException, 
      NullFactoryBirtEngineException {
      
      // Démarrage du serveur Birt
      Map<String, Object> factoryParams = new HashMap<String,Object>();
      factoryParams.put(BirtEngineFactoryKeys.REPORT_ENGINE_HOME , "" ) ;
      factoryParams.put(BirtEngineFactoryKeys.LOG_PATH, logsPath);
      
      try {
         birtEngine = BirtEngineFactory.getBirtEngineInstance(
               BirtEngineFactory.EnumTypeApplication.JAVA_APP,
               factoryParams) ;
      } catch( MissingConstructorParamBirtException e ) {
         if( e.getMessage().contains( ": context" ) )
         {
            fail("Le message d'erreur ne devrait pas contenir ':context', mais ': reportEnginePath'") ;
         }
      }

   }
   
   /**
    * Test unitaire de la méthode {@link BirtEngineFactory#getBirtEngineInstance}<br>
    * <br>
    * Cas de test : en mode application non web, le paramètre obligatoire 
    * {@link BirtEngineFactoryKeys#REPORT_ENGINE_HOME} n'est pas transmis<br>
    * <br>
    * Résultat attendu : une exception MissingKeyInHashMapBirtEngineFactoryException est levée, 
    * avec comme message le nom du paramètre manquant 
    */
   @Test
   public void getBirtEngineInstance3() 
   throws 
   MissingKeyInHashMapBirtEngineFactoryException, 
   MissingConstructorParamBirtException, 
   BirtException, 
   NullFactoryBirtEngineException {

      try {
         
         // Demande une instance de BirtEngine sans transmettre le paramètre obligatoire
         // BirtEngineFactoryKeys.REPORT_ENGINE_HOME
         
         Map<String, Object> factoryParams = new HashMap<String,Object>();
         
         BirtEngineFactory.getBirtEngineInstance(
               BirtEngineFactory.EnumTypeApplication.JAVA_APP,
               factoryParams);
         
      }
      catch (MissingKeyInHashMapBirtEngineFactoryException ex) {
         
         // On vérifie le message de l'exception
         String sExpected = BirtEngineFactoryKeys.REPORT_ENGINE_HOME;
         String sActual = ex.getMessage();
         assertEquals("Le message de l'exception levée n'est pas celui attendu",sExpected,sActual);
         
         // Si on est OK jusque là, alors le test est OK
         return;
         
      }
      
      // Si on arrive ici, c'est que l'exception attendue n'a pas été levée
      fail("Une exception MissingKeyInHashMapBirtEngineFactoryException aurait dû être levée");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link BirtEngineFactory#getBirtEngineInstance}<br>
    * <br>
    * Cas de test : en mode application web, le paramètre obligatoire 
    * {@link BirtEngineFactoryKeys#SERVLET_CONTEXT} n'est pas transmis<br>
    * <br>
    * Résultat attendu : une exception MissingKeyInHashMapBirtEngineFactoryException est levée, 
    * avec comme message le nom du paramètre manquant 
    */
   @Test
   public void getBirtEngineInstance4() 
   throws 
   MissingKeyInHashMapBirtEngineFactoryException, 
   MissingConstructorParamBirtException, 
   BirtException, 
   NullFactoryBirtEngineException {

      try {
         
         // Demande une instance de BirtEngine sans transmettre le paramètre obligatoire
         // BirtEngineFactoryKeys.SERVLET_CONTEXT
         
         Map<String, Object> factoryParams = new HashMap<String,Object>();
         
         BirtEngineFactory.getBirtEngineInstance(
               BirtEngineFactory.EnumTypeApplication.WEB_APP,
               factoryParams);
         
      }
      catch (MissingKeyInHashMapBirtEngineFactoryException ex) {
         
         // On vérifie le message de l'exception
         String sExpected = BirtEngineFactoryKeys.SERVLET_CONTEXT;
         String sActual = ex.getMessage();
         assertEquals("Le message de l'exception levée n'est pas celui attendu",sExpected,sActual);
         
         // Si on est OK jusque là, alors le test est OK
         return;
         
      }
      
      // Si on arrive ici, c'est que l'exception attendue n'a pas été levée
      fail("Une exception MissingKeyInHashMapBirtEngineFactoryException aurait dû être levée");
      
   }
   

}
