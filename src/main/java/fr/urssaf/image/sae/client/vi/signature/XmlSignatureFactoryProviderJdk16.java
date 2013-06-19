package fr.urssaf.image.sae.client.vi.signature;

import java.security.Provider;

import javax.xml.crypto.dsig.XMLSignatureFactory;

import fr.urssaf.image.sae.client.vi.exception.SaeClientViRuntimeException;

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
            "org.jcp.xml.dsig.internal.dom.XMLDSigRI");
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
    * {@inheritDoc}
    */
   public final XMLSignatureFactory getXmlFactory() {
      return xmlSignatureFactory;
   }

}
