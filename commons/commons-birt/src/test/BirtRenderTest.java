package fr.urssaf.image.commons.birt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtRenderException;
import fr.urssaf.image.commons.birt.exception.MissingParamBirtRenderException;
import fr.urssaf.image.commons.path.PathUtil;

@SuppressWarnings({"PMD.TooManyMethods","PMD.AvoidDuplicateLiterals"})
public class BirtRenderTest {
	
	private BirtRender birtRender = null ;
	
	private static final String REPORTENGINE_PATH = "./src/test/resources/ReportEngine/" ;
	private final String logsPath = System.getProperty("java.io.tmpdir") ;
	private final String outputPath = System.getProperty("java.io.tmpdir") ;
	private static final String OUTPUT_FILENAME = "/monRapportGenereEn" ;
	private static final String REPORT_PATH = "./src/test/resources/reports" ;
	private static final String REPORT_FILENAME = "/monPremierRapport.rptdesign" ;
	
	private static final String EXTENSION_PDF = ".pdf";
	
	@Before
	public void setUp() throws Exception {
	   birtRender = new BirtRender( REPORTENGINE_PATH, logsPath, outputPath, OUTPUT_FILENAME ) ;
	}

	@After
	public void tearDown() throws Exception {
		File fPdf = new File( PathUtil.combine(outputPath,OUTPUT_FILENAME) + EXTENSION_PDF );
		if ( fPdf.exists() ) 
		{// nettoyage
			fPdf.delete();
		}
		
		File fHtml = new File( PathUtil.combine(outputPath,OUTPUT_FILENAME) + ".html" );
		if ( fHtml.exists() ) 
		{// nettoyage
			fHtml.delete();
		}
		
	}

	/**
	 * On change le niveau de log
	 */
	@Test
	public void testDoChangeLogLevel()
	{
		
	   Boolean success = birtRender.doChangeLogLevel( Level.INFO );
		
		assertTrue("Le changement de niveau de log s'est mal passé", success );
		
		assertEquals(
		      "La valeur du niveau de log n'est pas celle attendu (" + 
		      Level.INFO.intValue() + 
		      " != " + 
		      birtRender.getLogLevel().intValue() 
		      + ")", 
		      Level.INFO.intValue(),
		      birtRender.getLogLevel().intValue() );
	}

	/**
	 * On démarre le moteur, on l'arrête et on vérifie l'état du moteur à chaque étape
	 */
	@Test
	public void testStopEngine() {
		assertFalse("Le moteur ne semble pas démarré", birtRender.isStopped()) ;
		birtRender.stopEngine();
		assertTrue("Le moteur ne semble pas stoppé", birtRender.isStopped()) ;
	}
	
	/**
	 * Le statut du moteur doit être à stoppé : isStopped == false
	 */
	@Test
	public void testIsStopped1() {
		assertFalse("Le moteur ne semble pas démarré", birtRender.isStopped()) ;
	}
	
	/**
	 * Le statut du moteur doit être à démarré : isStopped == true
	 */
	@Test
	public void testIsStopped2() {
		birtRender.stopEngine();
		assertTrue("Le moteur ne semble pas stoppé", birtRender.isStopped()) ;
	}
	
	/**
	 * Vérification des valeurs passées au constructeur constructeur
	 */
	@Test
	public void testBirtRender1() {
		assertTrue( birtRender.getReportEnginePath() + " attendu, " + REPORTENGINE_PATH + "obtenu", 
				birtRender.getReportEnginePath().equals( REPORTENGINE_PATH ) ) ;
		assertTrue( birtRender.getLogPath() + " attendu, " + logsPath + "obtenu", 
				birtRender.getLogPath().equals( logsPath ) );
		assertTrue( birtRender.getOutputPath() + " attendu, " + outputPath + "obtenu", 
				birtRender.getOutputPath().equals( outputPath ) ) ;
		assertTrue( birtRender.getOutputFilename() + " attendu, " + OUTPUT_FILENAME + "obtenu", 
				birtRender.getOutputFilename().equals( OUTPUT_FILENAME ) ) ;
		assertFalse( "Le moteur devrait être demarre", birtRender.isStopped() ) ;
	}
	
	/**
	 * Test de l'exception récupérée par un mauvais passage de paramètre
	 * @throws BirtException 
	 * @throws MissingConstructorParamBirtRenderException 
	 * @throws Exception 
	 */
	@Test(expected = MissingConstructorParamBirtRenderException.class)
   public void testBirtRender2() throws MissingConstructorParamBirtRenderException, BirtException
	{
      new BirtRender( null, logsPath, outputPath, OUTPUT_FILENAME ) ;
   }
	
	/**
	 * Test de l'exception récupérée par un mauvais passage de paramètre
	 * @throws BirtException 
	 * @throws MissingConstructorParamBirtRenderException 
	 */
	@Test(expected = MissingConstructorParamBirtRenderException.class)
	public void testBirtRender3() throws MissingConstructorParamBirtRenderException, BirtException {
		new BirtRender( REPORTENGINE_PATH, null, outputPath, OUTPUT_FILENAME ) ;
	}
	
	/**
	 * Test de l'exception récupérée par un mauvais passage de paramètre
	 * @throws BirtException 
	 * @throws MissingConstructorParamBirtRenderException 
	 */
	@Test(expected = MissingConstructorParamBirtRenderException.class)
	public void testBirtRender4() throws MissingConstructorParamBirtRenderException, BirtException {
		new BirtRender( REPORTENGINE_PATH, logsPath, null, OUTPUT_FILENAME ) ;
	}
	
	/**
	 * Test de l'exception récupérée par un mauvais passage de paramètre
	 * @throws BirtException 
	 * @throws MissingConstructorParamBirtRenderException 
	 */
	@Test(expected = MissingConstructorParamBirtRenderException.class)
	public void testBirtRender5() throws MissingConstructorParamBirtRenderException, BirtException {
		new BirtRender( REPORTENGINE_PATH, logsPath, outputPath, null ) ;
	}
	
	/**
	 * Test de la génération d'un rapport : pas d'exception et fichier pdf présent
	 * @throws MissingParamBirtRenderException 
	 * @throws EngineException 
	 */
	@Test
	public void testDoRenderStringIntMap1() throws EngineException, MissingParamBirtRenderException {
		
	   HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);
		
		String reportFilePath = PathUtil.combine(REPORT_PATH,REPORT_FILENAME) ;
		
		birtRender.doRender( reportFilePath, 1, paramValues );
		File file = new File( PathUtil.combine(outputPath,OUTPUT_FILENAME) + EXTENSION_PDF );
		assertTrue( "Le fichier pdf n'a pas été créé", file.exists() ) ;
		
	}
	
	/**
	 * Test de la génération d'un rapport : pas d'exception et fichier html présent
	 * @throws MissingParamBirtRenderException 
	 * @throws EngineException 
	 */
	@Test
	public void testDoRenderStringIntMap2() throws EngineException, MissingParamBirtRenderException {
		
	   HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);

		String reportFilePath = PathUtil.combine(REPORT_PATH,REPORT_FILENAME) ;
		
		birtRender.doRender( reportFilePath, 2, paramValues );
		File file = new File( PathUtil.combine(outputPath,OUTPUT_FILENAME) + ".html" );
		assertTrue( "Le fichier html n'a pas été créé", file.exists() ) ;
		
	}
	
	/**
	 * Test de la génération d'un rapport : pas d'exception et fichier pdf (défaut) présent
	 * @throws MissingParamBirtRenderException 
	 * @throws EngineException 
	 */
	@Test
	public void testDoRenderStringIntMap3() throws EngineException, MissingParamBirtRenderException {
		
	   HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);
		
		String reportFilePath = PathUtil.combine(REPORT_PATH,REPORT_FILENAME) ;
		
		birtRender.doRender( reportFilePath, 9999, paramValues );
		File file = new File( PathUtil.combine(outputPath,OUTPUT_FILENAME) + EXTENSION_PDF );
		assertTrue( "Le fichier par défaut pdf n'a pas été créé", file.exists() ) ;
		
	}

	/**
	 * Test de la génération d'un rapport : pas d'exception et fichier pdf présent
	 * @throws MissingParamBirtRenderException 
	 * @throws EngineException 
	 */
	@Test
	public void testDoRenderStringStringIntMap1() throws EngineException, MissingParamBirtRenderException {
		
	   HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
	   paramValues.put("monParametreTitreDePage", "Titre de ma page");
	   paramValues.put("CustomerNumberParam", 200);
	   String filename = "monFichierGenere" ;

	   String reportFilePath = PathUtil.combine(REPORT_PATH,REPORT_FILENAME) ;
	   
	   birtRender.doRender( reportFilePath, filename, 1, paramValues );
	   File file = new File( PathUtil.combine(outputPath,filename) + EXTENSION_PDF );
	   assertTrue( "Le fichier pdf n'a pas été créé", file.exists() ) ;
	   file.delete() ; // nettoyage
		
	}
	
	/**
	 * Test de la génération d'un rapport : mauvais passage de paramètre, exception récupérée
	 * @throws MissingParamBirtRenderException 
	 * @throws EngineException 
	 * @throws Exception 
	 */
	@Test(expected = MissingParamBirtRenderException.class)
	public void testDoRenderStringStringIntMap2() throws EngineException, MissingParamBirtRenderException {
		HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		String reportFilePath = PathUtil.combine(REPORT_PATH,REPORT_FILENAME) ;
		birtRender.doRender( reportFilePath, null, 1, paramValues );
	}

}
