package fr.urssaf.image.sae.documents.executable.utils;

import java.text.SimpleDateFormat;

/**
 * Cette classe contient les constantes utilisées dans SAE-DOCUMENTS-EXECUTABLE.
 * 
 */
public final class Constantes {

   public static final String REQUETELUCENE = "requeteLucene";
   public static final String DOCUMENT = "document";
   public static final String IDFORMAT = "idFormat";
   public static final String FILE = "file";
   public static final String PARAMETRES = "parametres";
   public static final String METADONNEES = "metadonnees";

   public static final String PARAM_OBLIGATOIRE = "erreur.param.obligatoire.null";
   public static final String PARAM_METADONNEES_NON_AUTORISEES = "erreur.metadonnees.non.autorisee";

   public static final String[] METADONNEES_DEFAULT = new String[] {
         "SM_DOCUMENT_TYPE", "SM_ARCHIVAGE_DATE", "cse", "apr", "atr",
         Constantes.METADONNEES_FORMAT_FICHIER };

   public static final String[] METADONNEES_NON_AUTORISEES = new String[] {
         "dco", "SM_LIFE_CYCLE_REFERENCE_DATE", "gel", "SM_DIGEST",
         "SM_DIGEST_ALGORITHM", "SM_VERSION", "SM_MODIFICATION_DATE" };

   public static final String METADONNEES_FORMAT_FICHIER = "ffi";
   public static final String METADONNEES_NOM_FICHIER = "nfi";

   public static final long CONVERT_MILLISECONDS_TO_MINUTES = 60 * 1000;

   public static final SimpleDateFormat FORMATTER_DATE = new SimpleDateFormat(
         "yyyy-mm-dd");

   /** Cette classe n'est pas faite pour être instanciée. */
   private Constantes() {
      assert false;
   }

}
