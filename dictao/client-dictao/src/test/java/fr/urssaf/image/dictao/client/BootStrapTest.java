package fr.urssaf.image.dictao.client;

import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Ignore;
import org.junit.Test;

import fr.urssaf.image.dictao.client.service.TimestampService;


/**
 * Ce ne sont pas vraiment des tests, mais plutôt des lanceurs
 *
 */
@SuppressWarnings("unused")
public class BootStrapTest {

	@Test
	@Ignore
	public void testDVSDevSam() throws Exception {
		//String showSoap = "false";
		String showSoap = "true";
		//String modeSansEchec = "true";
		String modeSansEchec = "false";

		BootStrap bootStrap = new BootStrap();
		String[] args2 = new String[] {
				"DVS",
				"2",
				showSoap, modeSansEchec, "DEV"};

		bootStrap.executeDVS(args2);
	}
	
	@Test
	@Ignore
	public void testDVSProdSam() throws Exception {
		//String showSoap = "true";
		String showSoap = "false";
		String modeSansEchec = "true";
		//String modeSansEchec = "false";

		BootStrap bootStrap = new BootStrap();
		String[] args2 = new String[] {
				"DVS",
				"2",
				showSoap, modeSansEchec, "PROD" };

		bootStrap.executeDVS(args2);
	}
	
	@Test
	@Ignore
	public void testD2SDevSam() throws Exception {
		//String showSoap = "true";
		String showSoap = "false";
		String modeSansEchec = "true";

		BootStrap bootStrap = new BootStrap();
		String[] args2 = new String[] {
				"D2S", "2", 
				showSoap, modeSansEchec, "DEV" };

		bootStrap.executeD2S(args2);
	}
	
	@Test
	@Ignore
	public void testD2SProdSam() throws Exception {
		//String showSoap = "true";
		String showSoap = "false";
		String modeSansEchec = "true";

		BootStrap bootStrap = new BootStrap();
		String[] args2 = new String[] {
				"D2S", "3", 
				showSoap, modeSansEchec, "PROD" };

		bootStrap.executeD2S(args2);
	}

	@Test
	@Ignore
	public void testTSP() throws Exception {
		//TimestampService service = new TimestampService();
		//service.tryTimestamper("http://cnp69tsa.cer69.recouv/tsa");
		//service.tryTimestamper("http://cnp31tsa.cer31.recouv/tsa");
		//service.tryTimestamperEnMasse("http://cnp31tsa.cer31.recouv/tsa", 30);
		
		BootStrap bootStrap = new BootStrap();
		String[] args2 = new String[] {
				"TSA", "2", "PROD" };
		bootStrap.executeTSA(args2);
	}

}
