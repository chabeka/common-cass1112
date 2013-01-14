package fr.urssaf.image.dictao.client.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URL;
import java.security.MessageDigest;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TSPValidationException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;

public class TimestampService {

	public TimestampService() {
		Security.addProvider(new BouncyCastleProvider());		
	}

	public void tryTimestamperEnMasse(String url, int nbInteration) throws Exception {
		int nbOK = 0;
		int nbKO = 0;
		Date dateDebutIndisponibilite = null;
		Date dateFinIndisponibilite = null;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String host = new URL(url).getHost();

		for (int i = 0; i < nbInteration; i++) {
			Date currentDate = new Date();
			String requestId = "Timestamp de masse " + Integer.toString(i + 1);
			System.out.print(dateFormat.format(currentDate) + " " + requestId + " ... ");
			InetAddress inet = InetAddress.getByName(host);
			System.out.print(String.format("(IP: %s)...", inet.toString()));
			boolean ok = true;
			try {
				ok = tryTimestamper(url);
			}
			catch (Exception e) {
				ok = false;
			}
			if (ok) {
				nbOK ++;
				System.out.println("OK");
			}
			else {
				if (dateDebutIndisponibilite == null) dateDebutIndisponibilite = currentDate;
				dateFinIndisponibilite = currentDate;
				nbKO ++;
				System.out.println("KO");
			}
			long diffInSeconds = 0;
			if (dateFinIndisponibilite != null) diffInSeconds = (dateFinIndisponibilite.getTime() - dateDebutIndisponibilite.getTime()) / 1000;			
			System.out.println(String.format("OK: %d,KO: %d, Durée indispo:%d s",
					nbOK, nbKO, diffInSeconds));
			Thread.sleep(1000);
			
		}
	}

	/**
	 * Test le service de timpstamp sur l'url fournie. Renvoie false ou une exception en cas d'erreur
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public boolean tryTimestamper(String url) throws Exception {
		TimeStampRequestGenerator timeStampRequestGenerator = new TimeStampRequestGenerator();

		Random rand = new Random();
		int nonce = rand.nextInt();
		byte[] digestBytes = new byte[20];
		MessageDigest dig = MessageDigest.getInstance(TSPAlgorithms.SHA1, "BC");
		dig.update(digestBytes);
		byte[] digest = dig.digest();
		TimeStampRequest timeStampRequest = timeStampRequestGenerator.generate(TSPAlgorithms.SHA1, digest, BigInteger.valueOf(nonce));

		// create a singular HttpClient object
		HttpClient client = new HttpClient();

		//establish a connection within 5 seconds
		client.setConnectionTimeout(5000);
		//client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);			
		PostMethod method = new PostMethod(url);
		method.setParameter("http.socket.timeout", "5000");
		method.setRequestHeader("Content-Type", "application/timestamp-query");
		method.setRequestBody(new ByteArrayInputStream(timeStampRequest.getEncoded()));
		//method.setRequestEntity(new InputStreamRequestEntity(new ByteArrayInputStream(timeStampRequest.getEncoded())));
		//method.setContentChunked(true);
		InputStream input = null;
		ByteArrayOutputStream baos = null;
		byte[] replyBytes = null;
		try {
			client.executeMethod(method);
			if (method.getStatusCode() == HttpStatus.SC_OK) {
				replyBytes = method.getResponseBody();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
			if (input != null) input.close();
			if (baos != null) baos.close();
		}   

		if (replyBytes != null) {
			try {
				TimeStampResponse timeStampResponse = new TimeStampResponse(replyBytes);
				timeStampResponse.validate(timeStampRequest);
				//System.out.println(timeStampResponse);
				//System.out.println("OK !");
			} catch (TSPValidationException e) {
				e.printStackTrace();
			} catch (TSPException e) {
				e.printStackTrace();
			}			
		} else {
			System.out.println("No reply bytes received, is TSA down?");
			return false;
		}
		return true;
	}
	
}
