package fr.urssaf.image.sae.webdemo.form;

import java.util.Date;

import org.springframework.validation.Errors;

/**
 * Formulaire de l'écran des traces d'exploitations
 * 
 * 
 */
public class LoggerForm {

   private Date start;

   private Date end;

   /**
    * @return valeur du critère 'A partir de'
    */
   public final Date getStartDate() {
      return start == null ? null : (Date) start.clone();
   }

   /**
    * @param start
    *           initialise le critère 'A partir de'
    */
   public final void setStartDate(Date start) {
      Date value = null;

      if (start != null) {
         value = (Date) start.clone();
      }
      
      this.start = value;
   }

   /**
    * @return valeur du critère 'Jusqu'au'
    */
   public final Date getEndDate() {
      return end == null ? null : (Date) end.clone();
   }

   /**
    * @param end
    *           initialise le critère 'Jusqu'au'
    */
   public final void setEndDate(Date end) {
      Date value = null;

      if (end != null) {
         value = (Date) end.clone();
      }
      
      this.end = value;

   }

   /**
    * Méthode de validation du formulaire
    * <ul>
    * <li>les dates <code>start</code> et <code>end</code> ne sont pas rangées :
    * erreur 'error.validDate'</li>
    * </ul>
    * 
    * @param errors
    *           liste des erreurs
    */
   public final void validate(Errors errors) {

      if (this.end != null && this.start != null
            && this.end.compareTo(this.start) < 0) {
         errors.rejectValue("endDate", "error.validDate");
      }

   }

}
