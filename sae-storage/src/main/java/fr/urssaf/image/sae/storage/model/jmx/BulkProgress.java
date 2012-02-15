package fr.urssaf.image.sae.storage.model.jmx;

/**
 * États d'avancement du traitement de masse.
 * 
 * @author akenore
 * 
 */
public enum BulkProgress {
   // Début de l'archivage en masse.
   BEGIN_OF_ARCHIVE("Début de l'archivage en masse."),
   // Fin de l'archivage en masse.
   END_OF_ARCHIVE("Fin de l'archivage en masse."),
   // Lecture des documents en cours
   READING_DOCUMENTS(
         "Lecture des documents à partir du fichier sommaire en cours..."),
   // Contrôle des documents en cours
   CONTROL_DOCUMENTS(
         "Contrôle et enrichissement des documents à archiver en cours..."),
   // Insertion des documents en cours
   INSERTION_DOCUMENTS("Archivage des documents en cours..."),
   // Suppression des documents en cours
   DELETION_DOCUMENTS("Suppression des documents en cours..."),
   // Génération du fichier résultat en cours
   GENERATION_RESULT_FILE("Génération du fichier résultat en cours..."),
   // Pas d'archivage en masse en cours
   @Deprecated
   NO_BULK_STORAGE_BEING("Aucun archivage en masse est en cours d'exécution.."),
   // Il n'y aucun identifiant de traitement.
   NO_TREATMENT_ID("Il n'y aucun identifiant de traitement."),
   // Aucun identifiant de traitement n'a été trouvé pour l'archivage de masse
   // en cours.
   NO_TREATMENT_ID_FOUND(
         "Aucun identifiant de traitement n'a été trouvé pour l'archivage en masse en cours d'exécution."),
   // Aucun identifiant de traitement n'a été trouvé car la lecture du fichier
   // sommaire est en cours d'éxécution ou ne s'est pas exécutée.
   NO_TREATMENT_ID_FOUND_BEFORE_READING(
         "Aucun identifiant de traitement n'a été trouvé car la lecture du fichier sommaire n'est pas terminée."),
   // Il n'y aucun identifiant de traitement car aucun archivage en masse est
   // en cours d'exécution.
   @Deprecated
   NO_TREATMENT_ID_NO_BULK_STORAGE_BEING(
         "Aucun identifiant de traitement n'a été trouvé car il n'y pas d'archivage en masse en cours d'exécution."),
   // Interruption du traitement en cours
   INTERRUPTED_TREATMENT("Interruption du traitement en cours."),
   // Reprise du traitement en cours.
   RESTART_TREATMENT("Reprise du traitement en cours.");
   // Le message.
   private String message;

   /**
    * Construit un état d'avancement.
    * 
    * @param message
    */
   BulkProgress(final String message) {
      this.message = message;
   }

   /**
    * @return Le message qui indique l'état d'avancement de l'insertion en
    *         masse.
    */
   public String getMessage() {
      return message;
   }

}
