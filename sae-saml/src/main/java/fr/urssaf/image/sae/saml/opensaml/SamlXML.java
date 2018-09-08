package fr.urssaf.image.sae.saml.opensaml;

import javax.xml.namespace.QName;

import org.opensaml.Configuration;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.XMLObjectBuilder;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.io.Unmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.w3c.dom.Element;

/**
 * Ensemble de méthode courantes pour manipulation du jeton SAML
 * 
 * 
 */
public final class SamlXML {

   private SamlXML() {

   }

   /**
    * Méthode de pour transformer un objet de type {@link XMLObject} en un objet
    * de type {@link Element}<br>
    * La méthode appelle {@link Marshaller#marshall(org.opensaml.xml.XMLObject)}
    * 
    * @param xmlObject
    *           objet qui represente le xml
    * @return element W3C DOM element représentant le jeton SAML
    */
   public static Element marshaller(XMLObject xmlObject) {

      Marshaller marshaller = Configuration.getMarshallerFactory()
            .getMarshaller(xmlObject);

      try {

         return marshaller.marshall(xmlObject);

      } catch (MarshallingException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * méthode de transformation un objet de type {@link Element} en un objet de
    * type {@link XMLObject}<br>
    * La méthode appelle {@link Unmarshaller#unmarshall(org.w3c.dom.Element)}
    * 
    * @param element
    *           W3C DOM element représentant le jeton SAML
    * @return jeton SAML
    */
   public static XMLObject unmarshaller(Element element) {

      Unmarshaller unmarshaller = Configuration.getUnmarshallerFactory()
            .getUnmarshaller(element);

      try {
         return unmarshaller.unmarshall(element);
      } catch (UnmarshallingException e) {
         throw new IllegalStateException(e);
      }
   }

   /**
    * méthode pour créer des balises dans le jeton SAML
    * <pre>
    * exemple pour créer une balise Assertion : 
    * Assertion assertion = SamlXML.create(Assertion.DEFAULT_ELEMENT_NAME);
    * </pre>
    * 
    * @param <T>
    *           type de balise
    * @param qname
    *           nom de la balise
    * @return l'objet instanciant cette balise
    */
   @SuppressWarnings("unchecked")
   public static <T> T create(QName qname) {
      return (T) ((XMLObjectBuilder) Configuration.getBuilderFactory()
            .getBuilder(qname)).buildObject(qname);
   }

}
