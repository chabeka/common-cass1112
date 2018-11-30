package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Classe de formulaire pour le test 402
 */
public class Test402Formulaire extends TestWsParentFormulaire {


   private final CaptureUnitaireFormulaire captureUnitaire = new CaptureUnitaireFormulaire(this) ;
   
   private final EnvoiSoapFormulaire envoiSoap = new EnvoiSoapFormulaire(this); 
   
   
   /**
    * Le sous-formulaire pour l'appel à l'opération "capture"
    * 
    * @return Le sous-formulaire pour l'appel à l'opération "capture"
    */
   public final CaptureUnitaireFormulaire getCaptureUnitaire() {
      return this.captureUnitaire;
   }
   
   
   /**
    * Le sous-formulaire pour l'envoi de message SOAP
    * 
    * @return Le sous-formulaire pour l'envoi de message SOAP
    */
   public final EnvoiSoapFormulaire getEnvoiSoap() {
      return this.envoiSoap;
   }
   
     
   
}
