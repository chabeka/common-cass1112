/**
 * 
 */
package fr.urssaf.image.sae.trace.service.validation;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * RegExploitationService
 * 
 */
@Aspect
public class RegSpecifiquesServiceValidation {

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.service.*.";
   private static final String LECTURE_METHOD = "execution(java.util.List "
         + CLASS_NAME + "lecture(*,*,*,*))"
         + " && args(dateDebut, dateFin, limite, reversed)";
   private static final String EXPORT_METHOD = "execution(java.lang.String "
         + CLASS_NAME + "export(*,*,*,*))"
         + " && args(date, repertoire, id, hash)";

   /**
    * Réalise la validation de la méthode lecture
    * 
    * @param dateDebut
    *           date de début
    * @param dateFin
    *           date de fin
    * @param limite
    *           nombre maximum d'enregistrements
    * @param reversed
    *           boolean pour savoir si l'ordre est décroissant ou non<br>
    *           <ul>
    *           <li>true : décroissant</li>
    *           <li>false : croissant</li>
    *           </ul>
    * 
    */
   @Before(LECTURE_METHOD)
   public final void testLecture(Date dateDebut, Date dateFin, int limite,
         boolean reversed) {

      if (dateDebut == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "date de début"));
      }

      if (dateFin == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "date de fin"));
      }

      if (dateDebut.compareTo(dateFin) >= 0) {
         throw new IllegalArgumentException(
               "la date de début doit être inférieure à la date de fin");
      }

      if (limite < 1) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "limite"));
      }

   }

   /**
    * Réalise la validation de la méthode export
    * 
    * @param date
    *           date concernée par l'export
    * @param repertoire
    *           répertoire dans lequel créer le fichier d'export
    * @param id
    *           identifiant du journal précédent
    * @param hash
    *           hash du journal précédent
    * 
    */
   @Before(EXPORT_METHOD)
   public final void testExport(Date date, String repertoire, String id,
         String hash) {

      if (date == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "date d'export"));
      }

      if (StringUtils.isEmpty(repertoire)) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "répertoire"));
      }

      if (StringUtils.isEmpty(id)) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "identifiant du journal précédent"));
      }

      if (StringUtils.isEmpty(hash)) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "hash du journal précédent"));
      }

      File file = new File(repertoire);
      if (!file.exists()) {
         throw new IllegalArgumentException("le répertoire doit exister");
      }

      if (!file.isDirectory()) {
         throw new IllegalArgumentException(
               "le paramètre n'est pas un répertoire");
      }

   }
}
