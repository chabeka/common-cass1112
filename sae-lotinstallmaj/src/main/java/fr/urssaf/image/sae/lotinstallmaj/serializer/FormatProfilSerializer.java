/**
 * 
 */
package fr.urssaf.image.sae.lotinstallmaj.serializer;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.lotinstallmaj.modele.FormatProfil;

/**
 * Sérialiseur / désérialiseur de {@link FormatProfil}. Utilise un
 * sérialiseur JSON
 * 
 */
public final class FormatProfilSerializer extends
      JacksonSerializer<FormatProfil> {

   /**
    * Constructeur
    * 
    * @param clazz
    *           classe en entrée
    */
   private FormatProfilSerializer(Class<FormatProfil> clazz) {
      super(clazz);
   }

   /**
    * Renvoie un singleton
    * 
    * @return un singleton
    */
   public static FormatProfilSerializer get() {
      return FormatProfilSerializerHolder.INSTANCE;
   }

   private static final class FormatProfilSerializerHolder {
      private FormatProfilSerializerHolder() {
      }

      @SuppressWarnings("PMD.AccessorClassGeneration")
      private static final FormatProfilSerializer INSTANCE = new FormatProfilSerializer(
            FormatProfil.class);
   }

}
