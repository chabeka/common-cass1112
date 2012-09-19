package fr.urssaf.image.commons.maquette.theme;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;

/**
 * Tests unitaires de la classe {@link AbstractMaquetteTheme}
 * 
 */
@SuppressWarnings("PMD")
public class AbstractMaquetteThemeTest {

   /**
    * Tests unitaires de la méthode
    * {@link AbstractMaquetteTheme#getFilterValue(String)}
    * 
    * @throws MaquetteThemeException
    * @throws IOException
    */
   @Test
   public void getFilterValue() throws MaquetteThemeException, IOException {

      Properties filterConfig;
      AbstractMaquetteTheme theme;
      String valeur;

      // Test avec un FilterConfig à null
      filterConfig = null;
      theme = new MaquetteThemeParDefaut(filterConfig);

      valeur = theme.getFilterValue(null);
      assertEquals("", valeur);

      valeur = theme.getFilterValue("");
      assertEquals("", valeur);

      valeur = theme.getFilterValue("toto");
      assertEquals("", valeur);

      // Tests avec un FilterConfig non null
      String value = "toto=tata";
      InputStream stream = new ByteArrayInputStream(value.getBytes());

      filterConfig = new Properties();
      filterConfig.load(stream);
      theme = new MaquetteThemeParDefaut(filterConfig);

      valeur = theme.getFilterValue(null);
      assertEquals("", valeur);

      valeur = theme.getFilterValue("");
      assertEquals("", valeur);

      valeur = theme.getFilterValue("toto");
      assertEquals("tata", valeur);

   }

}
