/**
 * 
 */
package fr.urssaf.image.sae.droit.dao.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.droit.dao.model.Pagm;

/**
 * Sérialiseur / Désérialiseur de {@link Pagm}. Utilise un sérialiseur JSON.
 * 
 */
public class PagmSerializer extends JacksonSerializer<Pagm> {

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
