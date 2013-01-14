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

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.Phase;
import org.apache.commons.httpclient.protocol.Protocol;

import fr.urssaf.image.dictao.client.handler.SoapHandler;
import fr.urssaf.image.dictao.client.modele.wsdl.D2SStub;
import fr.urssaf.image.dictao.client.modele.wsdl.D2SStub.SignatureEx;
import fr.urssaf.image.dictao.client.modele.wsdl.D2SStub.DataString;
import fr.urssaf.image.dictao.client.modele.wsdl.D2SStub.DataType;
import fr.urssaf.image.dictao.client.modele.wsdl.D2SStub.SignatureExResponse;
import fr.urssaf.image.dictao.client.utils.PatchedAuthSSLProtocolSocketFactory;

public class SignatureService {

	private static String TRANSACTION_ID = "SignatureXadesDetach";


	/**
	 * Lance des signature en masse.
	 * On continue la boucle même en cas d'échec. Utilisé pour tester la haute dispo netscaler
	 * @throws InterruptedException 
	 * @throws Exception 
	 */
	public void signatureEnMasseSansEchec(String urlWs, URL keystoreURL,
			String keyStorePassword, URL trustStoreURL,
			String trustStorePassword, int nbIterationsAppelWs, boolean sysoutSoapEnSortie) throws InterruptedException, Exception {

		// Mise en place du SSL
		initSsl(keystoreURL, keyStorePassword, trustStoreURL,
				trustStorePassword);
		String host = new URL(urlWs).getHost();

		// Création des paramètres d'entrée
		SignatureEx paramEntree = createParamEntree();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		int nbOK = 0;
		int nbKO = 0;
		Date dateDebutIndisponibilite = null;
		Date dateFinIndisponibilite = null;
		
		// Boucle d'appel au WS
		for (int iter = 0; iter < nbIterationsAppelWs; iter++) {

			// Création du stub d'accès au WS
			D2SStub stub = createStub(urlWs, sysoutSoapEnSortie);

			Date currentDate = new Date();
			String requestId = "Signature de masse " + Integer.toString(iter + 1);
			System.out.print(dateFormat.format(currentDate) + " " + requestId + " ... ");
			paramEntree.setRequestId(requestId);
			
			InetAddress inet = InetAddress.getByName(host);
			System.out.print(String.format("(IP: %s)...", inet.toString()));

			boolean ok = true;
			SignatureExResponse response;
			try {
				response = stub.signatureEx(paramEntree);
				int opStatus = response.getSignatureExResult().getOpStatus();
				int globalStatus = response.getSignatureExResult()
						.getD2SStatus();
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
			stub.cleanup();
			Thread.sleep(1000);
		}
	}

	
	private SignatureEx createParamEntree() {

		String sha256 = "SiIF7rIeYc5TE3vmAjHwJAYYZP3xPE5cA56jY32Zo2U=";
		String signatureParameter = String.format(
                "<Parameters>" +
                " <Manifest>" +
                "  <Reference>" +
                "   <DigestValue>%s</DigestValue>" +
                "   <DigestMethod>SHA256</DigestMethod>" +
                "   <URI/>" +
                "  </Reference>" +
                " </Manifest>" +
                "</Parameters>", sha256);

		String tag = "Tag signature de masse";

		DataType businessData = null;
		DataType dataToSign = new DataType();
		DataString dataString = new DataString();
		dataString.setDataFormat(null);
		dataString.setString("<Manifest/>");
		dataToSign.setValue(dataString);
		
		SignatureEx paramEntree = new SignatureEx();
		paramEntree.setTransactionId(TRANSACTION_ID);
		paramEntree.setTag(tag);
		paramEntree.setBusinessData(businessData);
		paramEntree.setKeyContainerParameter(null);
		paramEntree.setDataToSign(dataToSign);
		paramEntree.setSignatureParameter(signatureParameter);
		paramEntree.setSignatureFormat("XADES");
		paramEntree.setSignatureType("MANIFEST");
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

	private D2SStub createStub(String urlWs, boolean avecSortieSoap) {
		D2SStub stub;
		if (!avecSortieSoap) {

			try {
				stub = new D2SStub(urlWs);
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
				stub = new D2SStub(configContext, urlWs);
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
