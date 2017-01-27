package fr.urssaf.image.sae.integration.ihmweb.modele;

/**
 * Résultat d'un test
 */
public class ResultatTest {

   private TestStatusEnum status = TestStatusEnum.NonLance;
   
   private ResultatTestLog log = new ResultatTestLog();
   
   private LienHttpList liens = new LienHttpList();
   
   
   /**
    * Status du test (Réussite / Echec / ...)
    * 
    * @return Status du test (Réussite / Echec / ...)
    */
   public TestStatusEnum getStatus() {
      return status;
   }
   
   /**
    * Status du test (Réussite / Echec / ...)
    * 
    * @param status Status du test (Réussite / Echec / ...)
    */
   public void setStatus(TestStatusEnum status) {
      this.status = status;
   }
   
   
   /**
    * Log
    * @return log
    */
   public ResultatTestLog getLog() {
      return log;
   }
   
   /**
    * Log
    * 
    * @param log log
    */
   public void setLog(ResultatTestLog log) {
      this.log = log;
   }
   
   /**
    * Liens pour le téléchargement des fichiers associés au résultat du test
    * 
    * @return Liens pour le téléchargement des fichiers associés au résultat du test
    */
   public LienHttpList getLiens() {
      return this.liens;
   }
   
   
   /**
    * Liens pour le téléchargement des fichiers associés au résultat du test
    * 
    * @param liens Liens pour le téléchargement des fichiers associés au résultat du test
    */
   public void setLiens(LienHttpList liens) {
      this.liens = liens;
   }
   
   
   /**
    * Efface les résultats
    */
   public void clear() {
      this.status = TestStatusEnum.NonLance;
      this.log.getLog().clear();
      this.getLiens().clear();
   }
   
   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Statut : ");
      sb.append(status);
      return sb.toString();
   }
   
   
}
