/**
 * 
 */
package fr.urssaf.image.sae.commons.utils;

import fr.urssaf.image.commons.cassandra.serializer.JacksonSerializer;
import fr.urssaf.image.sae.commons.bo.Parameter;

/**
 * Sérialiseur / Désérialiseur de {@link Parameter}. Utilise un sérialiseur JSON.
 */
public final class ParameterSerializer extends JacksonSerializer<Parameter> {

  private static final ParameterSerializer INSTANCE = new ParameterSerializer(Parameter.class);

  /**
   * @param clazz
   */
  private ParameterSerializer(final Class<Parameter> clazz) {
    super(clazz, false);
  }

  /**
   * Renvoie un singleton
   * 
   * @return singleton
   */
  public static ParameterSerializer get() {
    return INSTANCE;
  }


}
