package fr.urssaf.image.commons.birt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.urssaf.image.commons.birt.exception.MissingConstructorParamBirtRenderException;
import fr.urssaf.image.commons.birt.exception.MissingParamBirtRenderException;

public class BirtRenderTest {
	
	private BirtRender _br = null ;
	private String _reportEnginePath = "./src/test/resources/ReportEngine/" ;
	private String _logsPath = "./logs" ;
	private String _outputPath = "./output" ;
	private String _outputFilename = "/monRapportGenereEn" ;
	
	private String _reportPath = "./src/test/resources/reports" ;
	private String _reportFileName = "/monPremierRapport.rptdesign" ;

	@Before
	public void setUp() throws Exception {
		_br = new BirtRender( _reportEnginePath, _logsPath, _outputPath, _outputFilename ) ;
	}

	@After
	public void tearDown() throws Exception {
		File fPdf = new File( _outputPath + _outputFilename + ".pdf" );
		if ( fPdf.exists() ) 
		{// nettoyage
			fPdf.delete();
		}
		
		File fHtml = new File( _outputPath + _outputFilename + ".html" );
		if ( fHtml.exists() ) 
		{// nettoyage
			fHtml.delete();
		}
		
		_br = null ;
	}

	/**
	 * On change le niveau de log
	 */
	@Test
	public void testDoChangeLogLevel() {
		Boolean success = null ;
		success = _br.doChangeLogLevel( Level.INFO );
		
		assertTrue("Le changement de niveau de log s'est mal pass�", success );
		assertEquals("La valeur du niveau de log n'est pas celle attendu (" + Level.INFO.intValue() + " != " + _br.getLogLevel().intValue() + ")", 
				Level.INFO.intValue(), _br.getLogLevel().intValue() );
	}

	/**
	 * On d�marre le moteur, on l'arr�te et on v�rifie l'�tat du moteur � chaque �tape
	 */
	@Test
	public void testStopEngine() {
		assertFalse("Le moteur ne semble pas d�marr�", _br.isStopped()) ;
		_br.stopEngine();
		assertTrue("Le moteur ne semble pas stopp�", _br.isStopped()) ;
	}
	
	/**
	 * Le statut du moteur doit �tre � stopp� : isStopped == false
	 */
	@Test
	public void testIsStopped_1() {
		assertFalse("Le moteur ne semble pas d�marr�", _br.isStopped()) ;
	}
	
	/**
	 * Le statut du moteur doit �tre � d�marr� : isStopped == true
	 */
	@Test
	public void testIsStopped_2() {
		_br.stopEngine();
		assertTrue("Le moteur ne semble pas stopp�", _br.isStopped()) ;
	}
	
	/**
	 * V�rification des valeurs pass�s au constructeur constructeur
	 */
	@Test
	public void testBirtRender_1() {
		assertTrue( _br.getReportEnginePath() + " attendu, " + _reportEnginePath + "obtenu", 
				_br.getReportEnginePath().equals( _reportEnginePath ) ) ;
		assertTrue( _br.getLogPath() + " attendu, " + _logsPath + "obtenu", 
				_br.getLogPath().equals( _logsPath ) );
		assertTrue( _br.getOutputPath() + " attendu, " + _outputPath + "obtenu", 
				_br.getOutputPath().equals( _outputPath ) ) ;
		assertTrue( _br.getOutputFilename() + " attendu, " + _outputFilename + "obtenu", 
				_br.getOutputFilename().equals( _outputFilename ) ) ;
		assertFalse( "Le moteur devrait �tre d�marr�", _br.isStopped() ) ;
	}
	
	/**
	 * Test de l'exception r�cup�r�e par un mauvais passage de param�tre
	 */
	@Test
	public void testBirtRender_2() {
		_br = null ;
		try
		{
			_br = new BirtRender( null, _logsPath, _outputPath, _outputFilename ) ;
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}catch( MissingConstructorParamBirtRenderException mcpbre )
		{
			// ok
		}
		catch( Exception e )
		{
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}
	}
	
	/**
	 * Test de l'exception r�cup�r�e par un mauvais passage de param�tre
	 */
	@Test
	public void testBirtRender_3() {
		_br = null ;
		try
		{
			_br = new BirtRender( _reportEnginePath, null, _outputPath, _outputFilename ) ;
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}catch( MissingConstructorParamBirtRenderException mcpbre )
		{
			// ok
		}
		catch( Exception e )
		{
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}
	}
	
	/**
	 * Test de l'exception r�cup�r�e par un mauvais passage de param�tre
	 */
	@Test
	public void testBirtRender_4() {
		_br = null ;
		try
		{
			_br = new BirtRender( _reportEnginePath, _logsPath, null, _outputFilename ) ;
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}catch( MissingConstructorParamBirtRenderException mcpbre )
		{
			// ok
		}
		catch( Exception e )
		{
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}
	}
	
	/**
	 * Test de l'exception r�cup�r�e par un mauvais passage de param�tre
	 */
	@Test
	public void testBirtRender_5() {
		_br = null ;
		try
		{
			_br = new BirtRender( _reportEnginePath, _logsPath, _outputPath, null ) ;
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}catch( MissingConstructorParamBirtRenderException mcpbre )
		{
			// ok
		}
		catch( Exception e )
		{
			fail("On devrait obtenir une MissingConstructorParamBirtRenderException");
		}
	}
	
	/**
	 * Test de la g�n�ration d'un rapport : pas d'exception et fichier pdf pr�sent
	 */
	@Test
	public void testDoRenderStringIntMap_1() {
		HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);
		
		try {
			_br.doRender( _reportPath + _reportFileName, 1, paramValues );
			File f = new File( _outputPath + _outputFilename + ".pdf" );
			assertTrue( "Le fichier pdf n'a pas �t� cr��", f.exists() ) ;
		} catch (Exception e) {
			fail("Exception : " + e.getClass().toString() ) ;
		}
	}
	
	/**
	 * Test de la g�n�ration d'un rapport : pas d'exception et fichier html pr�sent
	 */
	@Test
	public void testDoRenderStringIntMap_2() {
		HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);
		
		try {
			_br.doRender( _reportPath + _reportFileName, 2, paramValues );
			File f = new File( _outputPath + _outputFilename + ".html" );
			assertTrue( "Le fichier html n'a pas �t� cr��", f.exists() ) ;
		} catch (Exception e) {
			fail("Exception : " + e.getClass().toString() ) ;
		}
	}
	
	/**
	 * Test de la g�n�ration d'un rapport : pas d'exception et fichier pdf (d�faut) pr�sent
	 */
	@Test
	public void testDoRenderStringIntMap_3() {
		HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);
		
		try {
			_br.doRender( _reportPath + _reportFileName, 9999, paramValues );
			File f = new File( _outputPath + _outputFilename + ".pdf" );
			assertTrue( "Le fichier par d�faut pdf n'a pas �t� cr��", f.exists() ) ;
		} catch (Exception e) {
			fail("Exception : " + e.getClass().toString() ) ;
		}
	}

	/**
	 * Test de la g�n�ration d'un rapport : pas d'exception et fichier pdf pr�sent
	 */
	@Test
	public void testDoRenderStringStringIntMap_1() {
		HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		paramValues.put("monParametreTitreDePage", "Titre de ma page");
		paramValues.put("CustomerNumberParam", 200);
		String filename = "monFichierGenere" ;
		
		try {
			_br.doRender( _reportPath + _reportFileName, filename, 1, paramValues );
			File f = new File( _outputPath + "/" + filename + ".pdf" );
			assertTrue( "Le fichier pdf n'a pas �t� cr��", f.exists() ) ;
			f.delete() ; // nettoyage
		} catch (Exception e) {
			fail("Exception : " + e.getClass().toString() ) ;
		}
	}
	
	/**
	 * Test de la g�n�ration d'un rapport : mauvais passage de param�tre, exception r�cup�r�e
	 */
	@Test
	public void testDoRenderStringStringIntMap_2() {
		HashMap<Object, Object> paramValues = new HashMap<Object, Object>() ;
		
		try {
			_br.doRender( _reportPath + _reportFileName, null, 1, paramValues );
			fail("On devrait obtenir une MissingParamBirtRenderException" ) ;
		} catch (MissingParamBirtRenderException mpbre)
		{
			// ok
		} catch (Exception e) {
			fail("On devrait obtenir une MissingParamBirtRenderException" ) ;
		}
	}

}
