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
 * Tests unitaires de la classe {@link FooterParser}
 *
 */
@SuppressWarnings("PMD")
public class FooterParserTest {
   
   
   /**
    * Test unitaire de la méthode {@link FooterParser#doParse}<br>
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
      
      FooterParser parser = new FooterParser();
      
      String nomSourceManquante = "Footer";
      
      ParserTestTools.verifieLeveeExceptionSourceNull(parser, nomSourceManquante);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link FooterParser#doParse}<br>
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
      sbHtml.append("<div id=\"providedby\">la div providedby</div>");
      sbHtml.append("<div id=\"copyright\">la div copyright</div>");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
            
      // Préparation du HTML à parser
      InputStream inputStream = new ByteArrayInputStream(
            sbHtml.toString().getBytes("UTF-8"));
      Source source = new Source(inputStream) ;
      
      // Construction de l'objet Parser + parsing
      FooterParser parser = new FooterParser(source);
      
      // ------------------------------------------------------------------------
      // Vérifications du parsing
      // ------------------------------------------------------------------------
      
      // Balise "copyright"
      assertNotNull(
            "La balise providedby n'a pas été trouvée",
            parser.getProvidedByTag());
      assertEquals(
            "La balise \"providedby\" n'a pas été correctement parsée",
            "<div id=\"providedby\">la div providedby</div>",
            parser.getProvidedByTag().toString());
      
      // Balise "providedby"
      assertNotNull(
            "La balise copyright n'a pas été trouvée",
            parser.getCopyrightTag());
      assertEquals(
            "La balise \"copyright\" n'a pas été correctement parsée",
            "<div id=\"copyright\">la div copyright</div>",
            parser.getCopyrightTag().toString());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link FooterParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "providedby"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansProvidedBy()
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
      // sbHtml.append("<div id=\"providedby\">la div providedby</div>");
      sbHtml.append("<div id=\"copyright\">la div copyright</div>");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      FooterParser parser = new FooterParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "providedby");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link FooterParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "copyright"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansCopyright()
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
      sbHtml.append("<div id=\"providedby\">la div providedby</div>");
      // sbHtml.append("<div id=\"copyright\">la div copyright</div>");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      FooterParser parser = new FooterParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "copyright");
      
   }
   
   

}
