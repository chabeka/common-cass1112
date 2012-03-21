package fr.urssaf.image.sae.storage.dfce.constants;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Cette classe contient la liste des constantes utilisées dans l'application.
 * 
 * @author akenore, rhofir.
 */
public final class Constants {
   // Code erreur pour la suppression.
   public static final String DEL_CODE_ERROR = "delete.code.message";
   // Code erreur insertion.
   public static final String INS_CODE_ERROR = "insertion.code.message";
   // Code erreur consultation.
   public static final String RTR_CODE_ERROR = "retrieve.code.message";
   // Code erreur recherche.
   public static final String SRH_CODE_ERROR = "search.code.message";
   // Connexion code erreur .
   public static final String CNT_CODE_ERROR = "connection.code.message";
   // Protocole HTTPS
   public static final String HTTPS = "https";
   // Protocole HTTP
   public static final String HTTP = "http";
   // format de date iso 8601
   public static final String DATE_PATTERN = "yyyy-MM-dd";
   // format de date avec heure
   public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";
   // le local
   public static final Locale DEFAULT_LOCAL = Locale.FRENCH;
   /** encoding de lecture **/
   public static final Charset ENCODING = Charset.forName("UTF-8");
   /** Constante vide. */
   public static final String BLANK = "";
   // Le local par défaut
   public static final Locale LOCAL = Locale.FRENCH;
   // Message par défaut
   @SuppressWarnings("PMD.LongVariable")
   public
   static final String NO_MESSAGE_FOR_THIS_KEY = "Pas de méssage correspondant à cette clé";

   /** Cette classe n'est pas faite pour être instanciée. */
   private Constants() {
      assert false;
   }

}
