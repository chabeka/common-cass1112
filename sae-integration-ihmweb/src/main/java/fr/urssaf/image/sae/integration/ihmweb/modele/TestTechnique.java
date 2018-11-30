package fr.urssaf.image.sae.integration.ihmweb.modele;


/**
 * Classe représentant un test dit "technique" du service web SaeService<br>
 * <br>
 * Les tests techniques regroupent les tests d'accrochage (opération ping) et les
 * tests d'authentification (opération pingSecure et SoapFault de sécurité)
 */
public class TestTechnique {

   private String code;
   private boolean aLancer;
   private TestStatusEnum status ;
   private ResultatTestLog details;
   
   
   /**
    * Le code du test
    * 
    * @return le code du test
    */
   public final String getCode() {
      return code;
   }
   
   
   /**
    * @param code Le code du test
    */
   public final void setCode(String code) {
      this.code = code;
   }
   
   
   /**
    * @return un flag indiquant si le test doit être lancer ou non
    */
   public final boolean isaLancer() {
      return aLancer;
   }
   
   
   /**
    * @param aLancer un flag indiquant si le test doit être lancer ou non
    */
   public final void setaLancer(boolean aLancer) {
      this.aLancer = aLancer;
   }
   
   
   /**
    * @return le résultat du test
    */
   public final TestStatusEnum getStatus() {
      return status;
   }
   
   
   /**
    * @param status le résultat du test
    */
   public final void setStatus(TestStatusEnum status) {
      this.status = status;
   }
   
   
   /**
    * @return des détails associés au déroulement du test
    */
   public final ResultatTestLog getDetails() {
      return details;
   }
   
   
   /**
    * @param details des détails associés au déroulement du test
    */
   public final void setDetails(ResultatTestLog details) {
      this.details = details;
   }

   
   /**
    * Constructeur par défaut
    */
   public TestTechnique() {
      this.aLancer = true;
      this.status = TestStatusEnum.NonLance ;
      details = new ResultatTestLog();
   }
   
   
   /**
    * Constructeur
    * @param code le code du test
    */
   public TestTechnique(String code) {
      this();
      this.code = code;
   }
   
   
   
   /**
    * Vide les propriétés liées au résultat du passage du test
    */
   public final void clearResultats() {
      this.status = TestStatusEnum.NonLance;
      this.details.getLog().clear();
   }

   
}
