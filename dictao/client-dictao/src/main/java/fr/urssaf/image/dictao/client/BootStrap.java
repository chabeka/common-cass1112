package fr.urssaf.image.dictao.client;

import java.net.URL;

import fr.urssaf.image.dictao.client.service.VerifierSignatureService;
import fr.urssaf.image.dictao.client.utils.Utils;


public class BootStrap {

   
   public static void main(String[] args) {
      
      // 1er argument = chemin complet du fichier contenant la signature à vérifier (XML)
      // 2ème argument = nombre d'itérations d'appel au WS de vérification de signature
      // 3ème argument = le SHA256 encodé en base64 du fichier qui a été signé
      // 4ème argument = un flag indiquant si on veut afficher dans la console de soap de sortie
      
      // Exemples de valeurs pour les tests
      // args[0] = c:/toto.sig.xml
      // args[1] = 2
      // args[2] = "Vd0T5ShUx4er0PJ8Z7WU2XDmzPozvQAdmN6C7lHthyI="
      // args[3] = true
      
//      BootStrap bootStrap = new BootStrap();
//      
//      String[] args2 = new String[] {"G:/pmareche/dictao/BouncyCastle.Crypto.dll.sig.xml","1","Vd0T5ShUx4er0PJ8Z7WU2XDmzPozvQAdmN6C7lHthyI=", "false"}; 
//      
//      bootStrap.execute(args2);
      
      BootStrap bootStrap = new BootStrap();
      bootStrap.execute(args);
      
   }
   
   
   protected final void execute(String[] args) {
      
      // Récupère les arguments de la ligne de commande
      String cheminFichierSignature = args[0];
      int nbIterations = Integer.parseInt(args[1]);
      String sha256 = args[2];
      boolean sysoutSoapEnSortie = Boolean.parseBoolean(args[3]);

      // Instancie la classe de service
      VerifierSignatureService service = new VerifierSignatureService() ;
      
      // URL du service web Dictao de vérification de signature
      String urlWs = "https://cnp69devdxsapp1.cer69.recouv:24943";
      
      // Paramètres pour le SSL
      //   - Certificat client
      URL keystoreURL = new Utils().buildPkcs12urlFromFichierRessource(
            "/pkcs12/DEMAT-TIRAGES-APP.p12");
      String keyStorePassword = "AmBmwPpYZn";
      //    - Certificats trustés => on s'appuie sur le magasin Java par défaut
      URL trustStoreURL = null;
      String trustStorePassword = null;
      
      // Fichier à vérifier pour ce test
      // Il est stocké dans les ressources du projet
      String signatureAverifier = new Utils().getContentFichierSignature(
            cheminFichierSignature);
      
      // Appel de la méthode de service
      service.verifierSignature(
            urlWs, 
            keystoreURL, 
            keyStorePassword, 
            trustStoreURL, 
            trustStorePassword, 
            signatureAverifier, 
            sha256,
            nbIterations,
            sysoutSoapEnSortie);
      
   }

   
   
}
