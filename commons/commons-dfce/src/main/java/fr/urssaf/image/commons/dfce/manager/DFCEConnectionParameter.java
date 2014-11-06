package fr.urssaf.image.commons.dfce.manager;

/**
 * Ensemble des paramètres des fichiers de configuration pour la connexion à
 * DFCE
 * 
 */
public final class DFCEConnectionParameter {

   private DFCEConnectionParameter() {

   }

   /**
    * Paramètre {@value #DFCE_CONFIG} indiquant le paramètre de configuration du
    * chemin complet du fichier de configuration de DFCE
    */
   public static final String DFCE_CONFIG = "sae.dfce.cheminFichierConfig";

   /**
    * Paramètre {@value #DFCE_LOGIN} indiquant le paramètre de configuration du
    * login de connexion à DFCE
    */
   public static final String DFCE_LOGIN = "db.login";

   /**
    * Paramètre {@value #DFCE_PASSWORD} indiquant le paramètre de configuration
    * du mot de passe de connexion à DFCE
    */
   public static final String DFCE_PASSWORD = "db.password";

   /**
    * Paramètre {@value #DFCE_HOSTNAME} indiquant le paramètre de configuration
    * du hostname de l'URL de connexion à DFCE
    */
   public static final String DFCE_HOSTNAME = "db.hostName";

   /**
    * Paramètre {@value #DFCE_HOSTPORT} indiquant le paramètre de configuration
    * du port de l'URL de connexion à DFCE
    */
   public static final String DFCE_HOSTPORT = "db.hostPort";

   /**
    * Paramètre {@value #DFCE_CONTEXTROOT} indiquant le paramètre de
    * configuration du context root de l'URL de connexion à DFCE
    */
   public static final String DFCE_CONTEXTROOT = "db.contextRoot";

   /**
    * Paramètre {@value #DFCE_SECURE} indiquant le paramètre de configuration du
    * mode sécurisé de l'URL de connexion à DFCE
    */
   public static final String DFCE_SECURE = "db.secure";

   /**
    * Paramètre {@value #DFCE_TIMEOUT} indiquant le paramètre de configuration
    * du timeout de connexion à DFCE
    */
   public static final String DFCE_TIMEOUT = "db.timeout";

   /**
    * Paramètre {@value #DFCE_BASE_NAME} indiquant le paramètre de configuration
    * du nom de la base DFCE
    */
   public static final String DFCE_BASE_NAME = "db.baseName";

   /**
    * Paramètre {@value #DFCE_CHECK_HASH} indiquant le paramètre de
    * configuration de vérification du HASH
    */
   public static final String DFCE_CHECK_HASH = "db.checkHash";

   /**
    * Paramètre {@value #DFCE_DIGEST_ALGO} indiquant le paramètre de
    * configuration de nom du hash utilisé pour la vérification
    */
   public static final String DFCE_DIGEST_ALGO = "db.digestAlgo";

   /**
    * Paramètre {@value #DFCE_URL_TOOLKIT} indiquant le paramètre de
    * configuration de l'adresse du toolkit
    */
   public static final String DFCE_URL_TOOLKIT = "db.urlToolkit";
   
   /**
    * Paramètre {@value #TRANSFERT_DFCE_CONFIG} indiquant le paramètre de configuration du
    * chemin complet du fichier de configuration de DFCE pour le transfert
    */
   public static final String TRANSFERT_DFCE_CONFIG = "sae.dfce.transfert.cheminFichierConfig";

}
