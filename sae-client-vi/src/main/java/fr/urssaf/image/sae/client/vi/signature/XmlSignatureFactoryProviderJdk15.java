package fr.urssaf.image.sae.client.vi.signature;

import javax.xml.crypto.dsig.XMLSignatureFactory;

import org.jcp.xml.dsig.internal.dom.XMLDSigRI;

/**
 * Classe permettant de récupérer la bonne version du
 * {@link XMLSignatureFactory} pour la jdk 1.5
 */
public class XmlSignatureFactoryProviderJdk15 implements
      XmlSignatureFactoryProvider {

   private final XMLSignatureFactory xmlSignatureFactory;

   /**
    * Constructeur
    */
   public XmlSignatureFactoryProviderJdk15() {
      xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM",
            new XMLDSigRI());
   }

   /**
    * {@inheritDoc}
    */
   public final XMLSignatureFactory getXmlFactory() {
      return xmlSignatureFactory;
   }

}
