package fr.urssaf.image.sae.client.vi.signature;

/**
 * Factory de récupération de {@link XmlSignatureFactory}
 * 
 */
public final class XmlSignatureFactory {

   private XmlSignatureFactory() {
   }

   /**
    * La factory ad'hoc selon la version de jdk d'exécution
    * 
    * @return La factory ad'hoc selon la version de jdk d'exécution
    */
   public static XmlSignatureFactoryProvider getFactory() {
      return new XmlSignatureFactoryProvider();
   }
}
