package fr.urssaf.image.commons.maquette.template.parser.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.htmlparser.jericho.Source;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;


/**
 * Tests unitaires de la classe {@link PageReminderParser}
 *
 */
@SuppressWarnings("PMD")
public class PageReminderParserTest {
   
   
   /**
    * Test unitaire de la méthode {@link PageReminderParser#doParse}<br>
    * <br>
    * Cas de test : la source à parser est <code>null</code><br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansSource()
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      PageReminderParser parser = new PageReminderParser();
      
      String nomSourceManquante = "PageReminder";
      
      ParserTestTools.verifieLeveeExceptionSourceNull(parser, nomSourceManquante);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link PageReminderParser#doParse}<br>
    * <br>
    * Cas de test : cas standard<br>
    * <br>
    * Résultat attendu : le HTML est correctement parsé
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException 
    */
   @Test
   public void doParse_CasNormal()
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      // Le HTML à tester 
      StringBuffer sbHtml = new StringBuffer();
      sbHtml.append("<html>");
      sbHtml.append("<head>");
      sbHtml.append("<title>Le titre</title>");
      sbHtml.append("</head>");
      sbHtml.append("<body>");
      sbHtml.append("debut body");
      sbHtml.append("<div id=\"pagereminder\">dans la div</div>");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
            
      // Préparation du HTML à parser
      InputStream inputStream = new ByteArrayInputStream(
            sbHtml.toString().getBytes("UTF-8"));
      Source source = new Source(inputStream) ;
      
      // Construction de l'objet Parser + parsing
      PageReminderParser parser = new PageReminderParser(source);
      
      // ------------------------------------------------------------------------
      // Vérifications du parsing
      // ------------------------------------------------------------------------
      
      // Balise "pagereminder"
      assertNotNull(
            "La balise pagereminder n'a pas été trouvée",
            parser.getPageReminderTag());
      assertEquals(
            "La balise \"pagereminder\" n'a pas été correctement parsée",
            "<div id=\"pagereminder\">dans la div</div>",
            parser.getPageReminderTag().toString());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link PageReminderParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "pagereminder"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansPageReminder()
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      // Le HTML à tester 
      StringBuffer sbHtml = new StringBuffer();
      sbHtml.append("<html>");
      sbHtml.append("<head>");
      sbHtml.append("<title>Le titre</title>");
      sbHtml.append("</head>");
      sbHtml.append("<body>");
      sbHtml.append("debut body");
      /// sbHtml.append("<div id=\"pagereminder\">dans la div</div>");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      PageReminderParser parser = new PageReminderParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "pagereminder");
      
   }
   
}
