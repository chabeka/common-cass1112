package fr.urssaf.image.sae.batch.documents.executable.model;

import fr.urssaf.image.sae.batch.documents.executable.multithreading.DocumentsThreadExecutor;


/**
 * Objet permettant de stocker les paramètres concernant un traitement
 */
public abstract class AbstractParametres {


   /**
    * Requête permettant de sélectionner les documents à vérifier (réalisée à
    * partir des codes courts).
    */
   private String requeteLucene;

   /**
    * Nombre de threads exécutés en même temps dans le pool (par défaut 5)
    */
   private int taillePool;

   /**
    * Taille de pas d'exécution ("x enregistrements traités" dans les logs)
    */
   private int taillePasExecution;
   
   /**
    * Taille de la linked blocking queue.
    */
   private int tailleQueue; 
   
   /**
    * Temps d'attente avant de relancer un traiment si l'ajout d'un
    * traitement à été rejeté à cause la taille de la linked blocking queue.
    */
   private int queueSleepTime;
   
   /**
    * Chemin du dossier de travail
    */
   private String dossierTravail; 

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
    * Permet de récupérer le nombre d'éléments maximum en attente dans la queue
    * du pool de thread.
    * 
    * @return int
    */
   public final int getTailleQueue() {
      return tailleQueue;
   }
   
   /**
    * Permet de modifier le nombre d'éléments maximum en attente dans la queue
    * du pool de thread.
    * 
    * @param tailleQueue
    *           nombre maximum d'éléments en attente dans la queue du pool de
    *           thread
    */
   public final void setTailleQueue(int tailleQueue){
      this.tailleQueue = tailleQueue;
   }
   
   
   /**
    * Permet de récupérer le paramètre d'attente de la blocking queue
    * @see DocumentsThreadExecutor
    * 
    * @return String
    */
   public final int getQueueSleepTime(){
      return queueSleepTime;
   }
   
   /**
    * Permet de modifier le paramètre d'attente de la blocking queue
    * 
    * @return String
    */   
   public void setQueueSleepTime(int queueSleepTime){
      this.queueSleepTime = queueSleepTime;
   }
   
   /**
    * Récupération du chemin du dossier de travail
    * @return : le chemin du dossier de travail
    */
   public String getDossierTravail() {
      return dossierTravail;
   }

   /**
    * Modification du chemin du dossier de travail
    * @param cheminDossierTravail
    */
   public void setDossierTravail(String cheminDossierTravail) {
      this.dossierTravail = cheminDossierTravail;
   }
}
