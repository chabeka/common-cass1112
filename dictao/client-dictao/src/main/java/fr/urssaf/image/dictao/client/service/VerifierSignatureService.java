package fr.urssaf.image.dictao.client.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.RemoteException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.apache.commons.httpclient.protocol.Protocol;

import fr.urssaf.image.dictao.client.handler.SoapHandler;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.DVSDetailedStatusStruct;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.DataString;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.DataType;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.VerifySignatureEx;
import fr.urssaf.image.dictao.client.modele.wsdl.DVSStub.VerifySignatureExResponse;
import fr.urssaf.image.dictao.client.utils.PatchedAuthSSLProtocolSocketFactory;

public class VerifierSignatureService {

	private static String TRANSACTION_ID = "SignatureXadesDetach";
	private static int REFRESH_CRL = 1;

	public void verifierSignature(String urlWs, URL keystoreURL,
			String keyStorePassword, URL trustStoreURL,
			String trustStorePassword, String signatureAverifier,
			String sha256, int nbIterationsAppelWs, boolean sysoutSoapEnSortie) {

		// Mise en place du SSL
		initSsl(keystoreURL, keyStorePassword, trustStoreURL,
				trustStorePassword);

		// Création du stub d'accès au WS
		DVSStub stub = createStub(urlWs, sysoutSoapEnSortie);

		// Création des paramètres d'entrée
		VerifySignatureEx paramEntree = createParamEntree(signatureAverifier, sha256);

		// Boucle d'appel au WS
		for (int iter = 0; iter < nbIterationsAppelWs; iter++) {

			String requestId = "Vérification de masse "
					+ Integer.toString(iter + 1);
			System.out.println(requestId);
			paramEntree.setRequestId(requestId);

			VerifySignatureExResponse response;
			try {
				response = stub.verifySignatureEx(paramEntree);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}

			int opStatus = response.getVerifySignatureExResult().getOpStatus();
			int globalStatus = response.getVerifySignatureExResult()
					.getDVSGlobalStatus();

			System.out.println(String.format("OpStatus : %d, GlobalStatus: %d",
					opStatus, globalStatus));

			DVSDetailedStatusStruct[] details = response
					.getVerifySignatureExResult().getDVSDetailedStatus()
					.getDVSDetailedStatusStruct();
			for (DVSDetailedStatusStruct detail : details) {

				System.out.println(String.format(
						"SubjectName : %s, DVSStatus: %s",
						detail.getSubjectName(), detail.getDVSStatus()));
			}

			System.out.println();

		}
	}

	/**
	 * Lance des vérifications en masse.
	 * On continue la boucle même en cas d'échec. Utilisé pour tester la haute dispo netscaler
	 * @throws InterruptedException 
	 */
	public void verifierSignatureSansEchec(String urlWs, URL keystoreURL,
			String keyStorePassword, URL trustStoreURL,
			String trustStorePassword, String signatureAverifier,
			String sha256, int nbIterationsAppelWs, boolean sysoutSoapEnSortie) throws Exception {

		// Mise en place du SSL
		initSsl(keystoreURL, keyStorePassword, trustStoreURL,
				trustStorePassword);
		String host = new URL(urlWs).getHost();

		// Création des paramètres d'entrée
		VerifySignatureEx paramEntree = createParamEntree(signatureAverifier, sha256);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		int nbOK = 0;
		int nbKO = 0;
		Date dateDebutIndisponibilite = null;
		Date dateFinIndisponibilite = null;

		// Création du stub d'accès au WS
		DVSStub stub = createStub(urlWs, sysoutSoapEnSortie);

		// Boucle d'appel au WS
		for (int iter = 0; iter < nbIterationsAppelWs; iter++) {

			Date currentDate = new Date();
			String requestId = "Vérification de masse " + Integer.toString(iter + 1);
			System.out.print(dateFormat.format(currentDate) + " " + requestId + " ... ");
			paramEntree.setRequestId(requestId);

			InetAddress inet = InetAddress.getByName(host);
			System.out.print(String.format("(IP: %s)...", inet.toString()));

			boolean ok = true;
			VerifySignatureExResponse response;
			try {
				response = stub.verifySignatureEx(paramEntree);
				int opStatus = response.getVerifySignatureExResult().getOpStatus();
				int globalStatus = response.getVerifySignatureExResult()
						.getDVSGlobalStatus();
				//System.out.println(String.format("OpStatus : %d, GlobalStatus: %d", opStatus, globalStatus));
				ok = opStatus == 0 && globalStatus == 0;
			} catch (RemoteException e) {
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
		stub.cleanup();
	}

	
	private VerifySignatureEx createParamEntree(String signatureAverifier, String sha256) {
		DataType signature = new DataType();

		DataString sigXml = new DataString();

		// sigXml.setDataFormat(DataEncoding.gzEnc); // Ne pas spécifier le
		// dataFormat

		sigXml.setString(signatureAverifier);

		signature.setValue(sigXml);

		String signedDataHash = String.format("<SignedDataHash>"
				+ " <Manifest>" + "  <Ref>"
				+ "   <DigestMethod>SHA256</DigestMethod>"
				+ "   <DigestValue>%s</DigestValue>" + "  </Ref>"
				+ " </Manifest>" + "</SignedDataHash>", sha256);

		String tag = "Tag vérification de masse";

		DataType businessData = null;
		DataType signedData = null;
		// PluginParameterStruct[] pluginParameter = null;
		String properties = null;

		VerifySignatureEx paramEntree = new VerifySignatureEx();
		paramEntree.setTransactionId(TRANSACTION_ID);
		paramEntree.setRefreshCRLs(REFRESH_CRL);
		paramEntree.setTag(tag);
		paramEntree.setBusinessData(businessData);
		paramEntree.setSignature(signature);
		paramEntree.setSignedData(signedData);
		paramEntree.setSignedDataHash(signedDataHash);
		paramEntree.setVerifySignatureExChoice_type0(null);
		paramEntree.setProperties(properties);
		paramEntree.setPluginParameter(null);
		return paramEntree;

	}

	private void initSsl(URL keystoreURL, String keyStorePassword,
			URL trustStoreURL, String trustStorePassword) {

		// TODO: Revoir la mise en place du SSL pour ne plus utiliser du code
		// déprécié

		PatchedAuthSSLProtocolSocketFactory sslFactory;
		try {

			sslFactory = new PatchedAuthSSLProtocolSocketFactory(keystoreURL,
					keyStorePassword, trustStoreURL, trustStorePassword);

		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Protocol authhttps = new Protocol("https", sslFactory, 443);
		Protocol.registerProtocol("https", authhttps);

	}

	private DVSStub createStub(String urlWs, boolean avecSortieSoap) {
		DVSStub stub;

		if (!avecSortieSoap) {

			try {
				stub = new DVSStub(urlWs);
			} catch (AxisFault e) {
				throw new RuntimeException(e);
			}

		} else {

			// Création d'une configuration Axis2 par défaut
			ConfigurationContext configContext;
			try {
				configContext = ConfigurationContextFactory
						.createConfigurationContextFromFileSystem(null, null);
			} catch (AxisFault fault) {
				throw new RuntimeException(fault);
			}

			// Ajout d'un Handler lors de la phase "MessageOut" pour capter les messages soap
			AxisConfiguration axisConfig = configContext.getAxisConfiguration();
			List<Phase> outFlowPhases = axisConfig.getOutFlowPhases();
			Phase messageOut = findAxis2PhaseByName(outFlowPhases, "MessageOut");
			messageOut.addHandler(new SoapHandler());

			// Création de l'objet Stub
			try {
				stub = new DVSStub(configContext, urlWs);
			} catch (AxisFault e) {
				throw new RuntimeException(e);
			}
		}
		// Désactivation du chunking
	    //stub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
		
		// Renvoie l'objet Stub
		return stub;
	}

	private static Phase findAxis2PhaseByName(List<Phase> phases,
			String nomPhaseRecherchee) {

		Phase result = null;

		for (Phase phase : phases) {
			if (phase.getName().equals(nomPhaseRecherchee)) {
				result = phase;
				break;
			}
		}

		return result;

	}

}
