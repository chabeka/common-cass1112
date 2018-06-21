/**
 * 
 */
package fr.urssaf.image.sae.trace.service.validation;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Classe de validation des implémentations des méthodes de l'interface
 * RegService
 * 
 */
@Aspect
public class RegServiceValidation {

  private static final String ARG_0 = "{0}";
  
   private static final String MESSAGE_ERREUR = "l'argument {0} est obligatoire";

   private static final String CLASS_NAME = "fr.urssaf.image.sae.trace.service.*.";
   private static final String LECTURE_METHOD = "execution(fr.urssaf.image.sae.trace.dao.model.* "
         + CLASS_NAME + "lecture(*))" + " && args(uuid)";
   private static final String PURGE_METHOD = "execution(void " + CLASS_NAME
         + "purge(*))" + " && args(date)";
   private static final String HAS_RECORDS_METHOD = "execution(boolean "
         + CLASS_NAME + "hasRecords(*))" + " && args(date)";
   
   private static final String LECTURE_METHOD_UN = "execution(java.util.List "
       + CLASS_NAME + "lecture(*,*,*,*))"
       + " && args(dateDebut, dateFin, limite, reversed)";

   
   @Before(LECTURE_METHOD_UN)
   public final void testLecture(final Date dateDebut, final Date dateFin, final int limite,
                                 final boolean reversed) {

     if (dateDebut == null) {
       throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
                                                              ARG_0,
                                                              "date de début"));
     }

     if (dateFin == null) {
       throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
                                                              ARG_0,
                                                              "date de fin"));
     }

     if (dateDebut.compareTo(dateFin) >= 0) {
       throw new IllegalArgumentException(
                                          "la date de début doit être inférieure à la date de fin");
     }

     if (limite < 1) {
       throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
                                                              ARG_0,
                                                              "limite"));
     }

   }
   /**
    * Réalise la validation de la méthode lecture de l'interface RegService
    * 
    * @param uuid
    *           identifiant de la trace
    */
   @Before(LECTURE_METHOD)
   public final void testLecture(UUID uuid) {

      if (uuid == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "identifiant"));
      }
   }

   /**
    * Réalise la validation de la méthode purge de l'interface RegService
    * 
    * @param date
    *           date de la purge
    */
   @Before(PURGE_METHOD)
   public final void testPurge(Date date) {
      if (date == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "date de purge"));
      }
   }

   /**
    * Réalise la validation de la méthode hasRecords de l'interface RegService
    * 
    * @param date
    *           date pour laquelle vérifier qu'il y a des enregistrements
    */
   @Before(HAS_RECORDS_METHOD)
   public final void testHasRecords(Date date) {
      if (date == null) {
         throw new IllegalArgumentException(StringUtils.replace(MESSAGE_ERREUR,
               "{0}", "date"));
      }
   }

}
