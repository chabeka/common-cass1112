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
public class VISerializer extends JacksonSerializer<VIContenuExtrait> {

   public static final VISerializer INSTANCE = new VISerializer(
         VIContenuExtrait.class);

   /**
    * Constructeur
    * 
    * @param clazz
    *           classe en entrée
    */
   public VISerializer(Class<VIContenuExtrait> clazz) {
      super(clazz);
   }

   /**
    * Renvoie un singleton
    * 
    * @return un singleton
    */
   public static VISerializer get() {
      return INSTANCE;
   }

}
