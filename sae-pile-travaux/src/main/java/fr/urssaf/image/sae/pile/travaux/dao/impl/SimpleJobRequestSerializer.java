package fr.urssaf.image.sae.pile.travaux.dao.impl;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.pile.travaux.model.SimpleJobRequest;

/**
 * Sérialiseur/dé-sérialiseur de SimpleJobRequest
 * Utilise un sérialiseur jackson (json)
 *
 */
@Deprecated
public final class SimpleJobRequestSerializer extends JacksonSerializer<SimpleJobRequest> {

   private static final SimpleJobRequestSerializer INSTANCE = new SimpleJobRequestSerializer(SimpleJobRequest.class);

   /**
    * Constructeur
    * @param clazz   : La classe, qui est obligatoirement SimpleJobRequest.class (merci java)
    */
   private SimpleJobRequestSerializer(Class<SimpleJobRequest> clazz) {
      super(clazz);
   }
   
   /**
    * Renvoie un singleton
    * @return  singleton
    */
   public static SimpleJobRequestSerializer get() {
      return INSTANCE;
   }

}
