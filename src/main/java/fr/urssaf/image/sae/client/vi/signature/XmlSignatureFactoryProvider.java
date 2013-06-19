package fr.urssaf.image.sae.client.vi.signature;

import javax.xml.crypto.dsig.XMLSignatureFactory;

/**
 * Interface définissant l'accès à l'objet {@link XMLSignatureFactory}
 * 
 */
public interface XmlSignatureFactoryProvider {

   /**
    * L'objet {@link XmlSignatureFactory} ad'hoc
    * 
    * @return l'objet {@link XmlSignatureFactory} ad'hoc
    */
   XMLSignatureFactory getXmlFactory();

}
