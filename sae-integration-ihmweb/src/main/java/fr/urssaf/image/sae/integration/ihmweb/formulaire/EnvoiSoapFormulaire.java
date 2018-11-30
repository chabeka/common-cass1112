package fr.urssaf.image.sae.integration.ihmweb.formulaire;


/**
 * Un objet de cette classe s'associe au tag "envoiSoap.tag" (attribut
 * "objetFormulaire")
 */
public class EnvoiSoapFormulaire extends GenericForm {

   private String soapRequest ;
   

   /**
    * Constructeur
    * 
    * @param parent
    *           formulaire pere
    */
   public EnvoiSoapFormulaire(TestWsParentFormulaire parent) {
      super(parent);
   }


   /**
    * Requête SOAP
    * @return Requête SOAP
    */
   public final String getSoapRequest() {
      return soapRequest;
   }


   /**
    * Requête SOAP
    * @param soapRequest Requête SOAP
    */
   public final void setSoapRequest(String soapRequest) {
      this.soapRequest = soapRequest;
   }

   

}
