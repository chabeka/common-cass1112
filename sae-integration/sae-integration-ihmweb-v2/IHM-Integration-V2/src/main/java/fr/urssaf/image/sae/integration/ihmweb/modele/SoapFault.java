package fr.urssaf.image.sae.integration.ihmweb.modele;

import javax.xml.namespace.QName;


/**
 * Classe représentant une SoapFault
 */
public class SoapFault {

   private String identifiant;
   
   private String message;
   
   private QName code;

   /**
    * @return l'identifiant unique de la SoapFault dans le référentiel interne des SoapFault
    */
   public final String getIdentifiant() {
      return identifiant;
   }

   /**
    * @param identifiant l'identifiant unique de la SoapFault dans le référentiel interne des SoapFault
    */
   public final void setIdentifiant(String identifiant) {
      this.identifiant = identifiant;
   }

   /**
    * @return le message
    */
   public final String getMessage() {
      return message;
   }

   /**
    * @param message le message
    */
   public final void setMessage(String message) {
      this.message = message;
   }

   /**
    * @return le code
    */
   public final QName getCode() {
      return code;
   }

   /**
    * @param code le code
    */
   public final void setCode(QName code) {
      this.code = code;
   }
   
   
   /**
    * Renvoie le code de la SoapFault formaté en prefixe:localPart
    * 
    * @return le code de la SoapFault formaté en prefixe:localPart
    */
   public final String codeToString() {
      return this.code.getPrefix() + ":" + this.code.getLocalPart();
   }
   
   
}
