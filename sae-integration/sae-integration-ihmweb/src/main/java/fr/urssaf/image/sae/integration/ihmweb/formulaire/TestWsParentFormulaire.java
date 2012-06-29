package fr.urssaf.image.sae.integration.ihmweb.formulaire;

/**
 * Classe mère pour les classes de formulaire utilisées dans les contrôleurs qui
 * testent les opérations du service web SaeService
 */
public class TestWsParentFormulaire {

   private String urlServiceWeb;

   private String etape;

   private SoapFormulaire soapFormulaire = new SoapFormulaire();
   
   private ViFormulaire viFormulaire = new ViFormulaire(); 

   /**
    * URL du service web SaeService
    * 
    * @return URL du service web SaeService
    */
   public final String getUrlServiceWeb() {
      return urlServiceWeb;
   }

   /**
    * URL du service web SaeService
    * 
    * @param urlServiceWeb
    *           URL du service web SaeService
    */
   public final void setUrlServiceWeb(String urlServiceWeb) {
      this.urlServiceWeb = urlServiceWeb;
   }

   /**
    * Le numéro de l'étape du test
    * 
    * @return Le numéro de l'étape du test
    */
   public final String getEtape() {
      return etape;
   }

   /**
    * Le numéro de l'étape du test
    * 
    * @param etape
    *           Le numéro de l'étape du test
    */
   public final void setEtape(String etape) {
      this.etape = etape;
   }

   /**
    * Formulaire contenant les échanges WS
    * 
    * @return the soapFormulaire
    */
   public SoapFormulaire getSoapFormulaire() {
      return soapFormulaire;
   }

   /**
    * Formulaire contenant les échanges WS
    * 
    * @param soapFormulaire
    *           the soapFormulaire to set
    */
   public void setSoapFormulaire(SoapFormulaire soapFormulaire) {
      this.soapFormulaire = soapFormulaire;
   }

   /**
    * Formulaire contenant les paramètres du VI
    * @return Formulaire contenant les paramètres du VI
    */
   public final ViFormulaire getViFormulaire() {
      return viFormulaire;
   }

   /**
    * Formulaire contenant les paramètres du VI
    * @param viFormulaire Formulaire contenant les paramètres du VI
    */
   public final void setViFormulaire(ViFormulaire viFormulaire) {
      this.viFormulaire = viFormulaire;
   }

}
