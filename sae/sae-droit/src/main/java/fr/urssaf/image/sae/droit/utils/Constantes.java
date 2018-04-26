package fr.urssaf.image.sae.droit.utils;

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
   
   /**cache duration*/
   public static final int DROIT_CACHE_DURATION = 30;

   // Message par défaut
   public static final String NO_MESS_FOR_KEY = "Pas de message correspondant à cette clé";

   public static final String DROIT_PAGMF = "DroitPagmf";
   public static final String CLOCK = "clock";
   public static final String IDENTIFICATION = "identification";
   public static final String VALIDATOR = "validator";
   

   /** colonne de la colonneFamily Pagmf */
   public static final String DROIT_FORMAT_CONTROL = "DroitFormatControlProfil";
   public static final String COL_CODEPAGMF = "codePagmf";
   public static final String COL_DESCRIPTION = "description";
   public static final String COL_CODEFORMATCONTROLPROFIL = "formatProfile";
   
   
   /** colonne de la colonneFamily DroitFormatControlPril */
   public static final String COL_CODEPROFIL = "codeProfil";
   public static final String COL_CONTROLPROFIL = "controlProfil";
   
   public static final String FORMAT_VALIDATION_MODE = "formatValidationMode";
   public static final String MONITOR = "Monitor";
   public static final String STRICT = "Strict";
   public static final String AUCUN = "Aucun";
   public static final String NONE = "None";

   public static final String PARAM_OBLIGATOIRE = "erreur.param.obligatoire.null";
   public static final String FILE_NOT_FOUND = "erreur.file.not.found";
   public static final String FICHIER = "fichier";
   public static final String STREAM = "stream";
   
   
   
   
   /** Cette classe n'est pas faite pour être instanciée. */
   private Constantes() {
      assert false;
   }

}
