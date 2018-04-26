package fr.urssaf.image.commons.maquette.util;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.io.FilenameUtils;

/**
 * Méthodes utilitaires pour les fichiers temporaires
 */
public final class TempFileUtils {
   
   
   private TempFileUtils() {
      
   }
   
   
   /**
    * Renvoie un nom de fichier unique <br>
    * La construction est la suivante : date/heure + nombre aléatoire<br>
    * <br>
    * Il existe également une fonction de l'API Java qui permet de <b><u>CREER</u></b>
    * un fichier temporaire : {@link java.io.File#createTempFile(String, String)}
    * 
    * @return le nom de fichier unique
    * @see TestsUtils#getTemporaryFileName(String)
    * @see TestsUtils#getTemporaryFileName(String,String)
    */
   public static String getTemporaryFileName()
   {
      return getTemporaryFileName(null,null);
   }
   
   
   /**
    * Renvoie un nom de fichier unique <br>
    * La construction est la suivante : prefixe + date/heure + nombre aléatoire<br>
    * <br>
    * Il existe également une fonction de l'API Java qui permet de <b><u>CREER</u></b>
    * un fichier temporaire : {@link java.io.File#createTempFile(String, String)}
    * 
    * @param prefixe un préfixe éventuel (ou null)
    * @return le nom de fichier unique
    * @see TestsUtils#getTemporaryFileName
    * @see TestsUtils#getTemporaryFileName(String,String)
    */
   public static String getTemporaryFileName(String prefixe)
   {
      return getTemporaryFileName(prefixe,null);
   }
   
   
   /**
    * Renvoie un nom de fichier unique <br>
    * La construction est la suivante : prefixe + date/heure + nombre aléatoire + suffixe<br>
    * <br>
    * Il existe également une fonction de l'API Java qui permet de <b><u>CREER</u></b>
    * un fichier temporaire : {@link java.io.File#createTempFile(String, String)}
    * 
    * @param prefixe un préfixe éventuel (ou null)
    * @param suffixe un suffixe éventuel (ou null)
    * @return le nom de fichier unique
    * @see TestsUtils#getTemporaryFileName
    * @see TestsUtils#getTemporaryFileName(String)
    */
   public static String getTemporaryFileName(
         String prefixe,
         String suffixe)
   {
      
      // NB : Il n'est pas possible de tester unitairement l'intégralité du résultat de cette
      // méthode car elle contient un calcul de nombre aléatoire ainsi qu'une date
      // correspondant à "maintenant"
      
      // Création de l'objet résultat
      StringBuffer nomFicTemp = new StringBuffer();
      
      // 1ère partie du nom : le préfixe
      if (prefixe!=null) {
         nomFicTemp.append(prefixe);
      }
      
      // 2ème partie du nom : la date de maintenant, de l'année à la milli-secondes  
      final Date dMaintenant = new Date();
      final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmmss_SSS",Locale.FRENCH);
      nomFicTemp.append(dateFormat.format(dMaintenant));
      
      // 3ème partie du nom : un nombre aléatoire
      // L'algorithme utilisé est emprunté à java.io.File.createTempFile 
      final SecureRandom random = new SecureRandom();
      long nextLong = random.nextLong();
      if (nextLong == Long.MIN_VALUE) {
          nextLong = 0;      // corner case
      } else {
          nextLong = Math.abs(nextLong);
      }
      nomFicTemp.append('_');
      nomFicTemp.append(nextLong);
      
      // Dernière partie du nom : le suffixe
      if (suffixe!=null) {
         nomFicTemp.append(suffixe);
      }
      
      // Renvoie du résultat
      return nomFicTemp.toString();
      
   }
   
   
   /**
    * Renvoie un chemin complet de fichier temporaire. Le nom du fichier est unique.<br>
    * <br>
    * La construction du nom est la suivante : date/heure + nombre aléatoire<br>
    * <br>
    * Le répertoire est <code>java.io.tmpdir</code>.
    * 
    * @param extension l'extension du fichier temporaire
    * @return le chemin complet d'un nom de fichier temporaire
    */
   @SuppressWarnings("PMD.UseStringBufferForStringAppends")
   public static String getFullTemporaryFileName(String extension) {
      
      // Récupère un nom unique de fichier temporaire
      String nomFicSansExt = getTemporaryFileName();
      
      // Ajoute l'extension de fichier
      String nomFicAvecExt = nomFicSansExt;
      String extensionOk = extension;
      if (extensionOk!=null) {
         extensionOk = extensionOk.trim();
         if (extensionOk.length()>0) {
            
            if (extensionOk.charAt(0)!='.') {
               extensionOk = "." + extensionOk;
            }
            nomFicAvecExt += extensionOk;
            
         }
      }
      
      // Récupère le répertoire temporaire
      String tmpDir = System.getProperty("java.io.tmpdir") ;
      
      // Construit le chemin complet du fichier
      String cheminComplet = FilenameUtils.concat(
            tmpDir, 
            nomFicAvecExt);

      // Renvoie du résultat
      return cheminComplet;
      
   }

}
