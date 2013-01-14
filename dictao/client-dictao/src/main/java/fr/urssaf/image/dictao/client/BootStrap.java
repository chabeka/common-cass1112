package fr.urssaf.image.dictao.client;

import java.net.URL;

import fr.urssaf.image.dictao.client.service.SignatureService;
import fr.urssaf.image.dictao.client.service.TimestampService;
import fr.urssaf.image.dictao.client.service.VerifierSignatureService;
import fr.urssaf.image.dictao.client.utils.Utils;

public class BootStrap {

	// URL du service web Dictao de vérification de signature
	// String urlWs = "https://cnp69devdxsapp1.cer69.recouv:24943";
	String urlWs;

	// Paramètres pour le SSL : Certificat client
	URL keystoreURL;
	String keyStorePassword;

	// Paramètres pour le SSL : TrustStore
	URL trustStoreURL;
	String trustStorePassword;

	public static void main(String[] args) throws Exception {

		// Pour le cas d'une vérification de signature :
		// 1er argument = "DVS"
		// 2ème argument = nombre d'itérations d'appel au WS de vérification de signature
		// 3ème argument = un flag indiquant si on veut afficher dans la console le soap de sortie
		// 4ème argument = un flag indiquant si l'on veut continuer en cas d'échec pour test netscaler
		// 5ème argument = environnement à attaquer : "DEV" ou "PROD"
		// 6ème argument (facultatif) = chemin complet du fichier contenant la signature à vérifier (XML)
		// 7ème argument (facultatif) = le SHA256 encodé en base64 du fichier qui a été signé

		// Exemples de valeurs pour les tests
		// args[0] = DVS
		// args[1] = 2
		// args[2] = true
		// args[3] = false
		// args[4] = "DEV"
		// args[5] = c:/toto.sig.xml
		// args[6] = "Vd0T5ShUx4er0PJ8Z7WU2XDmzPozvQAdmN6C7lHthyI="


		// Pour le cas d'une signature :
		// 1er argument = "D2S"
		// 2ème argument = nombre d'itérations d'appel au WS de signature
		// 3ème argument = un flag indiquant si on veut afficher dans la console le soap de sortie
		// 4ème argument = un flag indiquant si l'on veut continuer en cas d'échec pour test netscaler
		// 5ème argument = environnement à attaquer : "DEV" ou "PROD"

		// Pour le cas d'un timestamp :
		// 1er argument = "TSA"
		// 2ème argument = nombre d'itérations d'appel au service de timestamp
		// 3ème argument = environnement à attaquer : "DEV" ou "PROD"
		
		String typeTraitement = args[0];

		BootStrap bootStrap = new BootStrap();
		if (typeTraitement.equals("DVS")) {
			bootStrap.executeDVS(args);
		}
		else if (typeTraitement.equals("D2S")) {
			bootStrap.executeD2S(args);
		}
		else if (typeTraitement.equals("TSA")) {
			bootStrap.executeTSA(args);
		}
		else {
			System.out.println("Type de traitement inconnu : " + typeTraitement);
		}
	}
	
	public BootStrap() {
		// Désactivation du cache DNS (cf http://www.rgagnon.com/javadetails/java-0445.html)
		java.security.Security.setProperty("networkaddress.cache.ttl" , "0");
		java.security.Security.setProperty("networkaddress.cache.negative.ttl" , "0");
	}
	
	protected final void setEnvironnement(String typeTraitement, String env) {
		if (env.startsWith("PROD")) {
			keystoreURL = Utils.ressourcePathToURL("/pkcs12/APPCLI-SIGN-TEST.p12");
			keyStorePassword = "T2NHJKTt73cBkm9FfxQSaznZ";
			trustStoreURL = Utils.ressourcePathToURL("/truststore/truststore_prod.jks");
			trustStorePassword = "changeit";
			if (env.equals("PROD")) {
				urlWs = typeTraitement.equals("DVS")? "https://dvs.cnp.recouv:24943" : "https://d2s.cnp.recouv:22943"; 
			}
			else if (env.equals("PROD31")) {
				urlWs = typeTraitement.equals("DVS")? "https://cnp31dxs.cer31.recouv:24943" : "https://cnp31dxs.cer31.recouv:22943"; 
			}
			else if (env.equals("PROD75")) {
				urlWs = typeTraitement.equals("DVS")? "https://cnp75dxs.ur750.recouv:24943" : "https://cnp75dxs.ur750.recouv:22943"; 
			}
			else if (env.equals("PROD69")) {
				urlWs = typeTraitement.equals("DVS")? "https://cnp69dxs.cer69.recouv:24943" : "https://cnp69dxs.cer69.recouv:22943"; 
			}
			else if (env.equals("PROD31VIP")) {
				urlWs = typeTraitement.equals("DVS")? "https://10.207.30.164:24943" : "https://10.207.30.164:22943"; 
			}
			else if (env.equals("PROD75VIP")) {
				urlWs = typeTraitement.equals("DVS")? "https://10.208.30.164:24943" : "https://10.208.30.164:22943"; 
			}
			else if (env.equals("PROD69VIP")) {
				urlWs = typeTraitement.equals("DVS")? "https://10.203.30.164:24943" : "https://10.203.30.164:22943"; 
			}
			else {
				throw new IllegalStateException("Environnement " + env + " inconnu");
			}
		}
		else if (env.equals("DEV")) {
			urlWs = typeTraitement.equals("DVS")? "https://cnp69devdxsapp1.cer69.recouv:24943" : "https://cnp69devdxsapp1.cer69.recouv:22943";
			keystoreURL = Utils.ressourcePathToURL("/pkcs12/DEMAT-TIRAGES-APP.p12");
			keyStorePassword = "AmBmwPpYZn";
			trustStoreURL = Utils.ressourcePathToURL("/truststore/truststore_dev.jks");
			trustStorePassword = "changeit";
		} else {
			throw new IllegalStateException("Environnement " + env + " inconnu");
		}
	}

	protected final void executeDVS(String[] args) throws Exception {

		// Récupère les arguments de la ligne de commande
		String typeTraitement = args[0];
		int nbIterations = Integer.parseInt(args[1]);
		boolean sysoutSoapEnSortie = Boolean.parseBoolean(args[2]);
		boolean modeSansEchec = Boolean.parseBoolean(args[3]);
		String environnement = args[4];
		
		String sha256;
		String signatureAverifier;
		if (args.length > 5) {
			String cheminFichierSignature = args[5];
			sha256 = args[3];
			signatureAverifier = Utils.getContentFichierSignature(cheminFichierSignature);
		}
		else {
			if (typeTraitement.equals("DEV")) {
				signatureAverifier = Utils.getFileContent(Utils.ressourcePathToURL("/signatures/BouncyCastle.Crypto.dll.sig.dev.xml"));
			}
			else {
				signatureAverifier = Utils.getFileContent(Utils.ressourcePathToURL("/signatures/BouncyCastle.Crypto.dll.sig.prod.xml"));
			}
			sha256 = "Vd0T5ShUx4er0PJ8Z7WU2XDmzPozvQAdmN6C7lHthyI=";
		}
		
		// On se positionne sur le bon environnement
		setEnvironnement(typeTraitement, environnement);
		
		// Instancie la classe de service
		VerifierSignatureService service = new VerifierSignatureService();


		// Appel de la méthode de service
		if (modeSansEchec) {
			service.verifierSignatureSansEchec(urlWs, keystoreURL,
					keyStorePassword, trustStoreURL, trustStorePassword,
					signatureAverifier, sha256, nbIterations,
					sysoutSoapEnSortie);

		} else {
			service.verifierSignature(urlWs, keystoreURL, keyStorePassword,
					trustStoreURL, trustStorePassword, signatureAverifier,
					sha256, nbIterations, sysoutSoapEnSortie);
		}

	}
	
	protected final void executeD2S(String[] args) throws Exception {

		// Récupère les arguments de la ligne de commande
		String typeTraitement = args[0];
		int nbIterations = Integer.parseInt(args[1]);
		boolean sysoutSoapEnSortie = Boolean.parseBoolean(args[2]);
		boolean modeSansEchec = Boolean.parseBoolean(args[3]);
		String environnement = args[4];

		// On se positionne sur le bon environnement
		setEnvironnement(typeTraitement, environnement);
		
		// Instancie la classe de service
		SignatureService service = new SignatureService();

		// Appel de la méthode de service
		if (modeSansEchec) {
			service.signatureEnMasseSansEchec(urlWs,keystoreURL,
					keyStorePassword, trustStoreURL, trustStorePassword,
					nbIterations,
					sysoutSoapEnSortie);

		} else {
			throw new IllegalArgumentException("Pour D2S, le mode avec echec n'est pas implémenté");
		}

	}

	protected final void executeTSA(String[] args) throws Exception {

		// Récupère les arguments de la ligne de commande
		String typeTraitement = args[0];
		int nbIterations = Integer.parseInt(args[1]);
		String environnement = args[2];

		// On se positionne sur le bon environnement
		String url = getURLTSA(environnement);
		
		// Instancie la classe de service
		TimestampService service = new TimestampService();

		// Appel de la méthode de service
		service.tryTimestamperEnMasse(url, nbIterations);
	}

	private String getURLTSA(String env) {
		if (env.startsWith("PROD")) {
			if (env.equals("PROD")) {
				return "http://tsa.cnp.recouv/tsa"; 
			}
			else if (env.equals("PROD31")) {
				return "http://cnp31tsa.cer31.recouv/tsa"; 
			}
			else if (env.equals("PROD75")) {
				return "http://cnp75tsa.ur750.recouv/tsa"; 
			}
			else if (env.equals("PROD69")) {
				return "http://cnp69tsa.cer69.recouv/tsa"; 
			}
			else if (env.equals("PROD31VIP")) {
				return "http://10.207.30.165/tsa"; 
			}
			else if (env.equals("PROD75VIP")) {
				return "http://10.208.30.165/tsa"; 
			}
			else if (env.equals("PROD69VIP")) {
				return "http://10.203.30.165/tsa"; 
			}
			else {
				throw new IllegalStateException("Environnement " + env + " inconnu");
			}
		}
		else if (env.equals("DEV")) {
			return "http://cnp69devtsa.cer69.recouv/tsa";
		} else {
			throw new IllegalStateException("Environnement " + env + " inconnu");
		}	
	}

}
