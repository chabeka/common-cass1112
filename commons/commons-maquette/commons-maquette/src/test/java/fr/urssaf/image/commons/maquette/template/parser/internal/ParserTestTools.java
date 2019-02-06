package fr.urssaf.image.commons.maquette.template.parser.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.htmlparser.jericho.Source;
import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;


/**
 * Tests unitaires pour les Parser
 *
 */
@SuppressWarnings("PMD")
public final class ParserTestTools {

   
   private ParserTestTools() {
      
   }
   
   
   /**
    * Pour tester qu'une exception MissingHtmlElementInTemplateParserException
    * est bien levée s'il manque une balise HTML
    *  
    * @param parser l'objet {@link AbstractParser} à tester
    * @param sbHtml le HTML de test à parser
    * @param nomBaliseManquante le nom de la balise manquante que l'on attend dans
    *        le message de l'exception
    *        
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException
    */
   protected static void verifieLeveeExceptionBaliseManquante(
         AbstractParser parser,
         StringBuffer sbHtml,
         String nomBaliseManquante)
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      // Préparation du HTML à parser
      InputStream inputStream = new ByteArrayInputStream(
            sbHtml.toString().getBytes("UTF-8"));
      Source source = new Source(inputStream) ;
      
      // Appel de la méthode à tester
      // Une exception doit être levée
      try {
         parser.doParse(source);
      } catch (MissingHtmlElementInTemplateParserException ex) {
         
         // Vérifie le message de l'exception
         assertEquals(
               "Le message de l'exception est incorrect",
               nomBaliseManquante,
               ex.getMessage());
         
         // Si on arrive jusque là, le test est OK
         return;
         
      }
      
      // Si on arrive jusque là, c'est que l'exception attendu n'a pas été levée
      fail("L'exception attendue MissingHtmlElementInTemplateParserException n'a pas été levée");
      
   }
   
   
   /**
    * Pour tester qu'une exception MissingSourceParserException
    * est bien levée si la source HTML est <code>null</code>
    * 
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   protected static void verifieLeveeExceptionSourceNull(
         AbstractParser parser,
         String nomSourceManquante)
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      // Appel de la méthode à tester
      // Une exception doit être levée
      try {
         parser.doParse(null);
      } catch (MissingSourceParserException ex) {
         
         // Vérifie le message de l'exception
         assertEquals(
               "Le message de l'exception est incorrect",
               nomSourceManquante,
               ex.getMessage());
         
         // Si on arrive jusque là, le test est OK
         return;
         
      }
      
      // Si on arrive jusque là, c'est que l'exception attendu n'a pas été levée
      fail("L'exception attendue MissingSourceParserException n'a pas été levée");
      
   }
   
   
}
