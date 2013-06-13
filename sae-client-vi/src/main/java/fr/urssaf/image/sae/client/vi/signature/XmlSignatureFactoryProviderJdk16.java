package fr.urssaf.image.sae.client.vi.signature;

import javax.xml.crypto.dsig.XMLSignatureFactory;

/**
 * Classe permettant de récupérer la bonne version du
 * {@link XMLSignatureFactory} pour la jdk 1.6
 * 
 */
public class XmlSignatureFactoryProviderJdk16 implements
      XmlSignatureFactoryProvider {

   private final XMLSignatureFactory xmlSignatureFactory;

   /**
    * Constructeur
    */
   public XmlSignatureFactoryProviderJdk16() {
      xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
   }

   /**
    * {@inheritDoc}
    */
   public final XMLSignatureFactory getXmlFactory() {
      return xmlSignatureFactory;
   }

}
