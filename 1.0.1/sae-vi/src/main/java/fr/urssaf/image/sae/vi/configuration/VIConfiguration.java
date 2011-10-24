package fr.urssaf.image.sae.vi.configuration;

/**
 * Classe de configuration du schéma XSD du VI<br>
 */
public final class VIConfiguration {

   private static final String NAME_VALUE = "sae-anais.xsd";

   private static final String PATH_VALUE = "/xsd/" + NAME_VALUE;

   private VIConfiguration() {

   }

   /**
    * Retourne le chemin du schema XSD du VI
    * 
    * @return <code>/xsd/sae-anais.xsd</code>
    */
   public static String path() {
      return PATH_VALUE;
   }

   /**
    * Retourne le nom du schema XSD du VI
    * 
    * @return <code>sae-anais.xsd</code>
    */
   public static String name() {
      return NAME_VALUE;
   }
}
