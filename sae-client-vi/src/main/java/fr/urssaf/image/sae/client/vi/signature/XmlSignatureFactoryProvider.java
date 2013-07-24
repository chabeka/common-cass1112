package fr.urssaf.image.sae.client.vi.signature;

import java.security.Provider;

import javax.xml.crypto.dsig.XMLSignatureFactory;

import fr.urssaf.image.sae.client.vi.exception.SaeClientViRuntimeException;

/**
 * Classe permettant d'instancier un objet XMLSignatureFactory avec un choix
 * possible sur le provider à utiliser, en valorisant la propriété système
 * nommée "jsr105Provider".<br>
 * <br>
 * Par défaut, le provider est
 * <code>org.jcp.xml.dsig.internal.dom.XMLDSigRI</code>
 */
public class XmlSignatureFactoryProvider {

   private String DEFAULT_PROVIDER = "org.jcp.xml.dsig.internal.dom.XMLDSigRI";

   private final XMLSignatureFactory xmlSignatureFactory;

   /**
    * Constructeur
    */
   public XmlSignatureFactoryProvider() {

      // xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");

      // => provoque l'erreur suivante avec un déploiement sous JBoss
      // java.lang.ClassCastException:
      // org.jcp.xml.dsig.internal.dom.DOMXMLSignatureFactory cannot be cast to
      // javax.xml.crypto.dsig.XMLSignatureFactory

      // On contourne le problème avec ceci :
      // Si une propriété système a été renseignée, on utilise le nom du
      // provider
      // Sinon, on utilise par défaut le provider du JDK (attention, ce provider
      // a été également déclaré dans xmlsec-1.4.x
      String providerName = System.getProperty("jsr105Provider",
            DEFAULT_PROVIDER);
      try {
         xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM",
               (Provider) Class.forName(providerName).newInstance());
      } catch (InstantiationException e) {
         throw new SaeClientViRuntimeException(e);
      } catch (IllegalAccessException e) {
         throw new SaeClientViRuntimeException(e);
      } catch (ClassNotFoundException e) {
         throw new SaeClientViRuntimeException(e);
      }

   }

   /**
    * L'objet {@link XmlSignatureFactory} ad'hoc
    * 
    * @return l'objet {@link XmlSignatureFactory} ad'hoc
    */
   public final XMLSignatureFactory getXmlFactory() {
      return xmlSignatureFactory;
   }

}
