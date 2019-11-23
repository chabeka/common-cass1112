package fr.urssaf.image.sae.format.utils;

import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Cette classe contient les constantes utilisées dans SAE-FORMAT.
 * 
 */
public final class Constantes {

   // Numérique pattern.
   public static final String DATE_FORMAT = "yyyy-MM-dd";
   // ici la date est comprise entre 1900-01-01 et 2099-12-31 et doit être de
   // la syntaxe yyyy-mm-dd
   public static final String DATE_PATTERN = "(19|20)\\d\\d[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])";
   // Numérique pattern.
   public static final String NUMERIC_PATTERN = "[0-9]*";
   // Numérique pattern.
   public static final String STRING_PATTERN = "[A-Za-z0-9]*";
   // Numérique pattern.
   public static final String BOOLEAN_PATTERN = "true|false";
   // le local
   public static final Locale DEFAULT_LOCAL = Locale.FRENCH;
   /** encoding de lecture **/
   public static final Charset ENCODING = Charset.forName("UTF-8");

   // Message par défaut
   public static final String NO_MESS_FOR_KEY = "Pas de message correspondant à cette clé";

   public static final String REFERENTIEL_FORMAT = "ReferentielFormat";
   public static final String CLOCK = "clock";
   public static final String IDENTIFICATION = "identification";
   public static final String VALIDATOR = "validator";
   public static final String VISUALISABLE = "visualisable";
   public static final String DESCRIPTION = "description";
   public static final String IDFORMAT = "idFormat";
   public static final String TYPE_MIME = "typeMime";
   public static final String EXTENSION = "extension";
   public static final String IDENTIFIEUR = "identifieur";

   /** colonne de la colonneFamily ReferentielFormat */
   public static final String COL_IDFORMAT = "idFormat";
   public static final String COL_TYPEMIME = "typeMime";
   public static final String COL_EXTENSION = "extension";
   public static final String COL_DESCRIPTION = "description";
   public static final String COL_AUTORISE_GED = "autoriseGED";
   public static final String COL_VISUALISABLE = "visualisable";
   public static final String COL_VALIDATOR = "validator";
   public static final String COL_IDENTIFIEUR = "identifieur";
   public static final String COL_CONVERTISSEUR = "convertisseur";

   public static final String PARAM_OBLIGATOIRE = "erreur.param.obligatoire.null";
   public static final String FORMAT_PARAM = "erreur.format.param";
   public static final String FILE_NOT_FOUND = "erreur.file.not.found";
   public static final String FICHIER = "fichier";
   public static final String STREAM = "stream";
   public static final String BYTE = "byte";
   public static final String NOMFICHIER = "nomFichier";
   
   public static final String FMT_354 = "fmt/354";

   /** Cette classe n'est pas faite pour être instanciée. */
   private Constantes() {
      assert false;
   }

}
