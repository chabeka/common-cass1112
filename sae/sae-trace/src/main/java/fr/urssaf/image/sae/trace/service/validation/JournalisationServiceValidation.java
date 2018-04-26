/**
 * 
 */
package fr.urssaf.image.sae.trace.service.validation;

import java.io.File;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import fr.urssaf.image.sae.trace.model.JournalisationType;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * RegService
 * 
 */
@Aspect
public class JournalisationServiceValidation {

   private static final String ARG_0 = "{0}";

   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.service.JournalisationService.";

   private static final String EXPORT_METHOD = "execution(java.lang.String "
         + CLASS_NAME + "exporterTraces(*,*,*))"
         + " && args(type,repertoire,date)";
   private static final String RECUPERER_METHOD = "execution(java.util.List "
         + CLASS_NAME + "recupererDates(*))" + " && args(type)";

   /**
    * Réalise la validation de la méthode export de l'interface
    * JournalisationService
    * 
    * @param type
    *           type de la purge
    * @param repertoire
    *           repertoire dans lequel réaliser l'export
    * @param date
    *           date pour laquelle réaliser l'export
    */
   @Before(EXPORT_METHOD)
   public final void testExport(JournalisationType type, String repertoire,
         Date date) {

      if (type == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"));
      }

      if (StringUtils.isEmpty(repertoire)) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "repertoire"));
      }

      if (date == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "date"));
      }

      File file = new File(repertoire);

      if (!file.exists()) {
         throw new IllegalArgumentException("le répertoire n'existe pas");
      }

      if (!file.isDirectory()) {
         throw new IllegalArgumentException(
               "le chemin spécifié n'est pas un répertoire");
      }
   }

   /**
    * Réalise la validation de la méthode recupererDates de l'interface
    * JournalisationService
    * 
    * @param type
    *           type de la purge
    */
   @Before(RECUPERER_METHOD)
   public final void testRecup(JournalisationType type) {

      if (type == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               ARG_0, "type de journalisation"));
      }
   }
}
