package fr.urssaf.image.sae.client.vi.signature;

/**
 * Factory de récupération de {@link XmlSignatureFactory} selon la version de
 * jdk d'execution
 * 
 * 
 */
public final class XmlSignatureFactory {

   private XmlSignatureFactory() {
   }

   /**
    * @return la factory ad'hoc selon la version de jdk d'exécution
    */
   public static XmlSignatureFactoryProvider getFactory() {
      return new XmlSignatureFactoryProviderJdk16();
   }
}
