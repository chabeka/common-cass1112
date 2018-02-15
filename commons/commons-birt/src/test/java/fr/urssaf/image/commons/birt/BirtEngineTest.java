package fr.urssaf.image.commons.birt;


import static org.junit.Assert.*; //NOPMD

import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtException;
import fr.urssaf.image.commons.birt.exception.NoInstanceBirtEngineException;
import fr.urssaf.image.commons.birt.exception.NullFactoryBirtEngineException;


@SuppressWarnings("PMD")
public class BirtEngineTest {
	
   private BirtEngine birtEngine = null ;
	
	private String reportEngineHome = null ;
	private final String logsPath = System.getProperty("java.io.tmpdir") ;
	
	@Before
	public void setUp() throws Exception {

	   // setup REPORTENGINE_HOME
	   reportEngineHome = BirtTools.getBirtHomeFromEnvVar() ;
	   	      
	   birtEngine = BirtEngine.getInstance( reportEngineHome, logsPath, null ) ;
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
    * Test unitaire de la méthode {@link BirtEngine#doChangeLogLevel(Level)}<br>
    * <br>
    * Cas de test : cas normal d'utilisation<br>
    * <br>
    * Résultat attendu : le niveau de log doit changer
    * 
    */
	@Test
	public void doChangeLogLevel_Test1() {
		
	   Boolean success = birtEngine.doChangeLogLevel( Level.INFO );
		
		assertTrue("Le changement de niveau de log s'est mal passé", success );
		
		assertEquals(
		      "La valeur du niveau de log n'est pas celle attendu (" + 
		      Level.INFO.intValue() + 
		      " != " + 
		      birtEngine.getLogLevel().intValue() 
		      + ")", 
		      Level.INFO.intValue(),
		      birtEngine.getLogLevel().intValue() );
	}
	
	
	/**
	 * Test unitaire de la méthode {@link BirtEngine#doChangeLogLevel(Level)}<br>
	 * <br>
	 * Cas de test : le moteur BIRT est arrêté<br>
	 * <br>
	 * Résultat attendu : le changement de niveau de log doit échouer
	 * 
	 */
	@Test
   public void doChangeLogLevel_Test2() {
	   birtEngine.stopEngine();
	   Boolean success = birtEngine.doChangeLogLevel(Level.FINE);
	   assertFalse("Le changement du niveau de log aurait dû échoué",success);
	}

	/**
	 * On démarre le serveur, on l'arrête et on vérifie l'état du moteur à chaque étape
	 */
	@Test
	public void testStopEngine() {
	   
		assertFalse("Le serveur ne semble pas être démarré", birtEngine.isStopped()) ;
		birtEngine.stopEngine();
		assertTrue("Le serveur ne semble pas stoppé", birtEngine.isStopped()) ;
	}
	
	/**
	 * Le statut du moteur doit être démarré : isStopped == false
	 */
	@Test
	public void testIsStopped1() {
		assertFalse("Le moteur est arrété alors qu'il devrait être démarré", 
		      birtEngine.isStopped()) ;
	}
	
	/**
	 * Le statut du moteur doit être stoppé : isStopped == true
	 */
	@Test
	public void testIsStopped2() {
	   birtEngine.stopEngine();
		assertTrue("Le moteur ne semble pas stoppé", birtEngine.isStopped()) ;
	}
	
	/**
	 * Vérification des valeurs passées au constructeur
	 */
	@Test
	public void testBirtEngine() {
		assertTrue( reportEngineHome + " attendu, " + birtEngine.getReportEnginePath() + "obtenu", 
		      birtEngine.getReportEnginePath().equals( reportEngineHome ) ) ;
		assertTrue( logsPath + " attendu, " + birtEngine.getLogPath() + "obtenu", 
		      birtEngine.getLogPath().equals( logsPath ) );
		assertNull( "NULL attendu", 
		      birtEngine.getServletContext() ) ;
		assertFalse( "Le moteur est arrété alors qu'il devrait être démarré", 
		      birtEngine.isStopped() ) ;
	}
	
	/**
    * Test du getInstance sans instance
	 * @throws NoInstanceBirtEngineException 
    */
   @Test(expected = NoInstanceBirtEngineException.class)
   public void testGetInstance1() throws NoInstanceBirtEngineException {

      killBirtEngineInstance();
      BirtEngine.getInstance() ;
   }
   
   /**
    * Test du getInstance avec instance
    * @throws NoInstanceBirtEngineException 
    */
   @Test
   public void testGetInstance2() throws NoInstanceBirtEngineException {
      assertNotNull("testGetInstance2",BirtEngine.getInstance());
   }
   
   /**
    * Test du getInstance avec de mauvais paramètre
    * @throws NullFactoryBirtEngineException 
    * @throws BirtException 
    * @throws MissingConstructorParamBirtException 
    */
   @Test(expected = MissingConstructorParamBirtException.class)
   public void testGetInstance3() 
   throws MissingConstructorParamBirtException, 
      BirtException, 
      NullFactoryBirtEngineException {
      
      killBirtEngineInstance();
      BirtEngine.getInstance(null, null, null) ;
   }
   
   /**
    * Test du getInstance avec de mauvais paramètre
    * le premier paramètre ne peut être vide si le dernier est null
    * @throws NullFactoryBirtEngineException 
    * @throws BirtException 
    * @throws MissingConstructorParamBirtException 
    */
   @Test(expected = MissingConstructorParamBirtException.class)
   public void testGetInstance4() 
   throws MissingConstructorParamBirtException, 
      BirtException, 
      NullFactoryBirtEngineException {
      
      killBirtEngineInstance();
      BirtEngine.getInstance("", null, null) ;
   }
   
   /**
    * Test du getInstance avec de mauvais paramètre
    * le dernier paramètre peut etre null si le premier n'est pas vide
    * @throws NullFactoryBirtEngineException 
    * @throws BirtException 
    * @throws MissingConstructorParamBirtException 
    */
   @Test
   public void testGetInstance5() 
   throws MissingConstructorParamBirtException, 
      BirtException, 
      NullFactoryBirtEngineException {
      
      killBirtEngineInstance();
      BirtEngine engine = BirtEngine.getInstance(reportEngineHome, null, null) ;
      assertNotNull("testGetInstance5",engine);
   }
   
   
   /**
    * Test unitaire de la méthode {@link BirtEngine#setServletContext}
    */
   @Test
   public void setServletContext()
   {
      birtEngine.setServletContext(null);
   }
   

}
