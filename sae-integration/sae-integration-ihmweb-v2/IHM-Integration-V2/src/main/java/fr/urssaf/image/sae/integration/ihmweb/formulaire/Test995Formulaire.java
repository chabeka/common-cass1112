package fr.urssaf.image.sae.integration.ihmweb.formulaire;

import fr.urssaf.image.sae.integration.ihmweb.modele.TestTechnique;


/**
 * Classe de formulaire
 */
public class Test995Formulaire extends TestWsParentFormulaire {

   
   private TestTechnique test001;
   private TestTechnique test002;
   private TestTechnique test003;
   private TestTechnique test004;
   private TestTechnique test005;
   private TestTechnique test006;
   private TestTechnique test007;
   private TestTechnique test008;
   // private TestTechnique test009;
   // private TestTechnique test010;
   
   
   /**
    * @return the test001
    */
   public final TestTechnique getTest001() {
      return test001;
   }
   
   
   /**
    * @param test001 the test001 to set
    */
   public final void setTest001(TestTechnique test001) {
      this.test001 = test001;
   }
   
   
   /**
    * @return the test002
    */
   public final TestTechnique getTest002() {
      return test002;
   }
   
   
   /**
    * @param test002 the test002 to set
    */
   public final void setTest002(TestTechnique test002) {
      this.test002 = test002;
   }
   
   
   /**
    * @return the test003
    */
   public final TestTechnique getTest003() {
      return test003;
   }
   
   
   /**
    * @param test003 the test003 to set
    */
   public final void setTest003(TestTechnique test003) {
      this.test003 = test003;
   }
   
   
   /**
    * @return the test004
    */
   public final TestTechnique getTest004() {
      return test004;
   }
   
   
   /**
    * @param test004 the test004 to set
    */
   public final void setTest004(TestTechnique test004) {
      this.test004 = test004;
   }
   
   
   /**
    * @return the test005
    */
   public final TestTechnique getTest005() {
      return test005;
   }
   
   
   /**
    * @param test005 the test005 to set
    */
   public final void setTest005(TestTechnique test005) {
      this.test005 = test005;
   }
   
   
   /**
    * @return the test006
    */
   public final TestTechnique getTest006() {
      return test006;
   }
   
   
   /**
    * @param test006 the test006 to set
    */
   public final void setTest006(TestTechnique test006) {
      this.test006 = test006;
   }
   
   
   /**
    * @return the test007
    */
   public final TestTechnique getTest007() {
      return test007;
   }
   
   
   /**
    * @param test007 the test007 to set
    */
   public final void setTest007(TestTechnique test007) {
      this.test007 = test007;
   }
   
   
   /**
    * @return the test008
    */
   public final TestTechnique getTest008() {
      return test008;
   }
   
   
   /**
    * @param test008 the test008 to set
    */
   public final void setTest008(TestTechnique test008) {
      this.test008 = test008;
   }
   
   
   /**
    * @return the test009
    */
//   public final TestTechnique getTest009() {
//      return test009;
//   }
   
   
   /**
    * @param test009 the test009 to set
    */
//   public final void setTest009(TestTechnique test009) {
//      this.test009 = test009;
//   }
   
   
//   /**
//    * @return the test010
//    */
//   public final TestTechnique getTest010() {
//      return test010;
//   }
//   
//   
//   /**
//    * @param test010 the test010 to set
//    */
//   public final void setTest010(TestTechnique test010) {
//      this.test010 = test010;
//   }
   
   
   
   /**
    * Constructeur
    */
   public Test995Formulaire() {
      super();
      test001 = new TestTechnique("001_Ping_ok");
      test002 = new TestTechnique("002_PingSecure_ok");
      test003 = new TestTechnique("003_PingSecure-SoapFault_wsse_SecurityTokenUnavailable");
      test004 = new TestTechnique("004_PingSecure-SoapFault_wsse_InvalidSecurityToken");
      test005 = new TestTechnique("005_PingSecure-SoapFault_wsse_FailedCheck");
      test006 = new TestTechnique("006_PingSecure-SoapFault_vi_InvalidVI");
      test007 = new TestTechnique("007_PingSecure-SoapFault_vi_InvalidService");
      test008 = new TestTechnique("008_PingSecure-SoapFault_vi_InvalidAuthLevel");
      // test009 = new TestTechnique("009_PingSecure-SoapFault_vi_InvalidPagm");
//      test010 = new TestTechnique("010_PingSecure-SoapFault_sae_DroitsInsuffisants");
   }
   
   
   /**
    * Efface les résultats des tests précédents
    */
   public final void clearResultatsTests() {
      test001.clearResultats();
      test002.clearResultats();
      test003.clearResultats();
      test004.clearResultats();
      test005.clearResultats();
      test006.clearResultats();
      test007.clearResultats();
      test008.clearResultats();
//      test009.clearResultats();
//      test010.clearResultats();
   }
  
}
