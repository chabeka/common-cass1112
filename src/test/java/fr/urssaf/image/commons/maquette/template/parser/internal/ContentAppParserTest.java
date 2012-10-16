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
 * Tests unitaires de la classe {@link ContentAppParser}
 *
 */
@SuppressWarnings("PMD")
public class ContentAppParserTest {
   
   
   /**
    * Test unitaire de la méthode {@link ContentAppParser#doParse}<br>
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
      
      ContentAppParser parser = new ContentAppParser();
      
      String nomSourceManquante = "ContentApp";
      
      ParserTestTools.verifieLeveeExceptionSourceNull(parser, nomSourceManquante);
      
   }
   
   
   
   /**
    * Test unitaire de la méthode {@link ContentAppParser#doParse}<br>
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
      
      sbHtml.append("<div id=\"content-application\">contenu de la div</div>");
      
      sbHtml.append("<noscript>noscript1</noscript>");
      sbHtml.append("<noscript>noscript2</noscript>");
      
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      
      // Préparation du HTML à parser
      InputStream inputStream = new ByteArrayInputStream(
            sbHtml.toString().getBytes("UTF-8"));
      Source source = new Source(inputStream) ;
      
      // Construction de l'objet Parser + parsing
      ContentAppParser parser = new ContentAppParser(source);
      
      
      // ------------------------------------------------------------------------
      // Vérifications du parsing
      // ------------------------------------------------------------------------
      
      // Balise "content-application"
      assertNotNull(
            "La balise content-application n'a pas été trouvée",
            parser.getContentAppTag());
      assertEquals(
            "La balise \"content-application\" n'a pas été correctement parsée",
            "<div id=\"content-application\">contenu de la div</div>",
            parser.getContentAppTag().toString());
      
      // Balises <noscript>
      assertNotNull(
            "Les balises <noscript> n'ont pas été trouvées",
            parser.getNoScriptTag());
      assertEquals(
            "Les balises <noscript> n'ont pas été correctement parsées",
            2,
            parser.getNoScriptTag().size());
      assertEquals(
            "Les balises <noscript> n'ont pas été correctement parsées",
            "<noscript>noscript1</noscript>",
            parser.getNoScriptTag().get(0).toString());
      assertEquals(
            "Les balises <noscript> n'ont pas été correctement parsées",
            "<noscript>noscript2</noscript>",
            parser.getNoScriptTag().get(1).toString());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link ContentAppParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "content-application"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansContentApplication()
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      // Le HTML à tester 
      StringBuffer sbHtml = new StringBuffer();
      sbHtml.append("<html>");
      sbHtml.append("<body>");
      sbHtml.append("<noscript>noscript1</noscript>");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construction de l'objet Parser
      ContentAppParser parser = new ContentAppParser();
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "content-application");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link ContentAppParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise &lt;noscript&gt;<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansNoScript()
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      // Le HTML à tester 
      StringBuffer sbHtml = new StringBuffer();
      sbHtml.append("<html>");
      sbHtml.append("<body>");
      sbHtml.append("<div id=\"content-application\">contenu de la div</div>");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construction de l'objet Parser
      ContentAppParser parser = new ContentAppParser();
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "noscript");
      
   }

}
