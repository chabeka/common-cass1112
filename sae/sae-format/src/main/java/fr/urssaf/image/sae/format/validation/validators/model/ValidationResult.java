package fr.urssaf.image.sae.format.validation.validators.model;

import java.util.List;

/**
 * Objet décrivant la structure du résultat à renvoyer après une validation.
 */
public class ValidationResult {

   private boolean valid;
   private List<String> details;

   /**
    * Constructeur
    * 
    * @param valid
    *           si le fichier est valide
    * @param details
    *           les anomalies
    */
   public ValidationResult(boolean valid, List<String> details) {
      this.valid = valid;
      this.details = details;
   }

   /**
    * Méthode permettant de savoir si le fichier ou flux est valide.
    * 
    * @return boolean vrai/faux
    */
   public final boolean isValid() {
      return valid;
   }

   /**
    * Méthode permettant de savoir si le fichier ou flux est valide.
    * 
    * @param valid
    *           si le fichier est valide
    * 
    */
   public final void setValid(boolean valid) {
      this.valid = valid;
   }

   /**
    * Méthode permettant d'avoir la trace d'exécution du processus de validation
    * en cas d'échec.
    * 
    * @return trace d'exécution du processus de validation en cas d'échec.
    */
   public final List<String> getDetails() {
      return details;
   }

   /**
    * Méthode permettant d'avoir la trace d'exécution du processus de validation
    * en cas d'échec.
    * 
    * @param details
    *           les anomalies
    */
   public final void setDetails(List<String> details) {
      this.details = details;
   }

}
