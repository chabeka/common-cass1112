package fr.urssaf.image.sae.documents.executable.model;

import java.util.List;

/**
 * Objet permettant de stocker les paramètres concernant la vérification des
 * formats de fichier.
 */
public class FormatValidationParametres {

   /**
    * Mode de vérification disponible :
    * <ul>
    * <li>identification : IDENTIFICATION</li>
    * <li>validation : VALIDATION</li>
    * <li>identification et validation : IDENT_VALID</li>
    * </ul>
    */
   public static enum MODE_VERIFICATION {
      IDENTIFICATION, VALIDATION, IDENT_VALID
   }

   /**
    * Mode de vérification choisit pour les fichiers.
    */
   private MODE_VERIFICATION modeVerification;

   /**
    * Requête permettant de sélectionner les documents à vérifier (réalisée à
    * partir des codes courts).
    */
   private String requeteLucene;

   /**
    * Nombre maximum de documents à traiter.
    */
   private int nombreMaxDocs;

   /**
    * Temps maximum de traitement (en min). Peut être non renseigné. Si c'est le
    * cas, la valeur est de 0
    */
   private int tempsMaxTraitement;

   /**
    * Liste des métadonnées à consulter.
    */
   private List<String> metadonnees;

   /**
    * Nombre de threads exécutés en même temps dans le pool (par défaut 5)
    */
   private int taillePool;

   /**
    * Taille de pas d'exécution ("x enregistrements traités" dans les logs)
    */
   private int taillePasExecution;

   /**
    * Chemin du répertoire temporaire. Peut être non renseigné. Si c'est le cas,
    * on prendra le répertoire temporaire de l'OS.
    */
   private String cheminRepertoireTemporaire;

   /**
    * Permet de récupérer le mode de vérification choisit pour les fichiers.
    * 
    * @return {@link MODE_VERIFICATION} mode de vérification
    */
   public final MODE_VERIFICATION getModeVerification() {
      return modeVerification;
   }

   /**
    * Permet de modifier le mode de vérification choisit pour les fichiers.
    * 
    * @param modeVerification
    *           {@link MODE_VERIFICATION} mode de vérification
    */
   public final void setModeVerification(
         final MODE_VERIFICATION modeVerification) {
      this.modeVerification = modeVerification;
   }

   /**
    * Permet de récupérer la requête permettant de sélectionner les documents à
    * vérifier.
    * 
    * @return String
    */
   public final String getRequeteLucene() {
      return requeteLucene;
   }

   /**
    * Permet de modifier la requête permettant de sélectionner les documents à
    * vérifier.
    * 
    * @param requeteLucene
    *           requête permettant de sélectionner les documents à vérifier
    */
   public final void setRequeteLucene(final String requeteLucene) {
      this.requeteLucene = requeteLucene;
   }

   /**
    * Permet de récupérer le nombre maximum de documents à traiter.
    * 
    * @return int
    */
   public final int getNombreMaxDocs() {
      return nombreMaxDocs;
   }

   /**
    * Permet de modifier le nombre maximum de documents à traiter.
    * 
    * @param nombreMaxDocs
    *           nombre maximum de documents à traiter
    */
   public final void setNombreMaxDocs(final int nombreMaxDocs) {
      this.nombreMaxDocs = nombreMaxDocs;
   }

   /**
    * Permet de récupérer le temps maximum de traitement (en min).
    * 
    * @return int
    */
   public final int getTempsMaxTraitement() {
      return tempsMaxTraitement;
   }

   /**
    * Permet de modifier le temps maximum de traitement (en min).
    * 
    * @param tempsMaxTraitement
    *           temps maximum de traitement (en min)
    */
   public final void setTempsMaxTraitement(final int tempsMaxTraitement) {
      this.tempsMaxTraitement = tempsMaxTraitement;
   }

   /**
    * Permet de récupérer la liste des métadonnées à consulter.
    * 
    * @return List<String>
    */
   public final List<String> getMetadonnees() {
      return metadonnees;
   }

   /**
    * Permet de modifier la liste des métadonnées à consulter.
    * 
    * @param metadonnees
    *           liste des métadonnées à consulter
    */
   public final void setMetadonnees(final List<String> metadonnees) {
      this.metadonnees = metadonnees;
   }

   /**
    * Permet de récupérer le nombre de threads exécutés en même temps dans le
    * pool.
    * 
    * @return int
    */
   public final int getTaillePool() {
      return taillePool;
   }

   /**
    * Permet de modifier le nombre de threads exécutés en même temps dans le
    * pool.
    * 
    * @param taillePool
    *           nombre de threads exécutés en même temps dans le pool
    */
   public final void setTaillePool(final int taillePool) {
      this.taillePool = taillePool;
   }

   /**
    * Permet de récupérer la taille du pas de l'exécution
    * ("x enregistrements traités" dans les logs).
    * 
    * @return int
    */
   public final int getTaillePasExecution() {
      return taillePasExecution;
   }

   /**
    * Permet de modifier la taille du pas de l'exécution
    * ("x enregistrements traités" dans les logs).
    * 
    * @param taillePasExecution
    *           la taille du pas de l'exécution ("x enregistrements traités"
    *           dans les logs).
    */
   public final void setTaillePasExecution(final int taillePasExecution) {
      this.taillePasExecution = taillePasExecution;
   }

   /**
    * Permet de récupérer le chemin du répertoire temporaire.
    * 
    * @return String
    */
   public final String getCheminRepertoireTemporaire() {
      return cheminRepertoireTemporaire;
   }

   /**
    * Permet de modifier le chemin du répertoire temporaire.
    * 
    * @param cheminRepertoireTemporaire
    *           chemin du répertoire temporaire
    */
   public final void setCheminRepertoireTemporaire(
         final String cheminRepertoireTemporaire) {
      this.cheminRepertoireTemporaire = cheminRepertoireTemporaire;
   }

}
