/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.lotinstallmaj.modele.Pagm;

/**
 * Sérialiseur / Désérialiseur de {@link Pagm}. Utilise un sérialiseur JSON.
 * 
 */
public final class PagmSerializer extends JacksonSerializer<Pagm> {

   private static final PagmSerializer INSTANCE = new PagmSerializer(Pagm.class);

   /**
    * @param clazz
    */
   private PagmSerializer(Class<Pagm> clazz) {
      super(clazz);
   }

   /**
    * Renvoie un singleton
    * 
    * @return singleton
    */
   public static PagmSerializer get() {
      return INSTANCE;
   }
   
   
}
