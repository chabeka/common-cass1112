/**
 * 
 */
package fr.urssaf.image.sae.pile.travaux.dao.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.vi.modele.VIContenuExtrait;

/**
 * Sérialiseur / désérialiseur de <b>VIContenuExtrait</b>. Utilise un
 * sérialiseur JSON
 * 
 */
public final class VISerializer extends JacksonSerializer<VIContenuExtrait> {

   /**
    * Constructeur
    * 
    * @param clazz
    *           classe en entrée
    */
   private VISerializer(Class<VIContenuExtrait> clazz) {
      super(clazz);
   }

   /**
    * Renvoie un singleton
    * 
    * @return un singleton
    */
   public static VISerializer get() {
      return ViSerializerHolder.INSTANCE;
   }

   private static final class ViSerializerHolder {
      private ViSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final VISerializer INSTANCE = new VISerializer(
            VIContenuExtrait.class);
   }

}
