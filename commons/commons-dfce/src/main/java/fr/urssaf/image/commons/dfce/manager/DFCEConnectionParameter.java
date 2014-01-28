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

}
