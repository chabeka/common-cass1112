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
 * Tests unitaires de la classe {@link DivHeaderParser}
 *
 */
@SuppressWarnings("PMD")
public class DivHeaderParserTest {
   
   
   /**
    * Test unitaire de la méthode {@link DivHeaderParser#doParse}<br>
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
      
      DivHeaderParser parser = new DivHeaderParser();
      
      String nomSourceManquante = "Header";
      
      ParserTestTools.verifieLeveeExceptionSourceNull(parser, nomSourceManquante);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link DivHeaderParser#doParse}<br>
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
      sbHtml.append("<img id=\"logoimage\" src=\"tata.gif\" />");
      sbHtml.append("<h1 id=\"title-app\">le titre</h1>");
      sbHtml.append("<img id=\"logoappli\" src=\"titi.gif\" />");
      sbHtml.append("<div id=\"menu\">la div du menu</div>");
      sbHtml.append("<img id=\"minheight\" src=\"toto.gif\" />");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
            
      // Préparation du HTML à parser
      InputStream inputStream = new ByteArrayInputStream(
            sbHtml.toString().getBytes("UTF-8"));
      Source source = new Source(inputStream) ;
      
      // Construction de l'objet Parser + parsing
      DivHeaderParser parser = new DivHeaderParser(source);
      
      // ------------------------------------------------------------------------
      // Vérifications du parsing
      // ------------------------------------------------------------------------
      
      // Balise "logoimage"
      assertNotNull(
            "La balise logoimage n'a pas été trouvée",
            parser.getMainLogoTag());
      assertEquals(
            "La balise \"logoimage\" n'a pas été correctement parsée",
            "<img id=\"logoimage\" src=\"tata.gif\" />",
            parser.getMainLogoTag().toString());
      
      // Balise "title-app"
      assertNotNull(
            "La balise title-app n'a pas été trouvée",
            parser.getTitleTag());
      assertEquals(
            "La balise \"title-app\" n'a pas été correctement parsée",
            "<h1 id=\"title-app\">le titre</h1>",
            parser.getTitleTag().toString());
      
      // Balise "logoappli"
      assertNotNull(
            "La balise logoappli n'a pas été trouvée",
            parser.getLogoTag());
      assertEquals(
            "La balise \"logoappli\" n'a pas été correctement parsée",
            "<img id=\"logoappli\" src=\"titi.gif\" />",
            parser.getLogoTag().toString());
      
      // Balise "menu"
      assertNotNull(
            "La balise menu n'a pas été trouvée",
            parser.getMenuTag());
      assertEquals(
            "La balise \"menu\" n'a pas été correctement parsée",
            "<div id=\"menu\">la div du menu</div>",
            parser.getMenuTag().toString());
      
      // Balise "minheight"
      assertNotNull(
            "La balise minheight n'a pas été trouvée",
            parser.getMinHeightImg());
      assertEquals(
            "La balise \"minheight\" n'a pas été correctement parsée",
            "<img id=\"minheight\" src=\"toto.gif\" />",
            parser.getMinHeightImg().toString());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link DivHeaderParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "logoimage"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansMainLogo()
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
      // sbHtml.append("<img id=\"logoimage\" src=\"tata.gif\" />");
      sbHtml.append("<h1 id=\"title-app\">le titre</h1>");
      sbHtml.append("<img id=\"logoappli\" src=\"titi.gif\" />");
      sbHtml.append("<div id=\"menu\">la div du menu</div>");
      sbHtml.append("<img id=\"minheight\" src=\"toto.gif\" />");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      DivHeaderParser parser = new DivHeaderParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "logoimage");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link DivHeaderParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "title-app"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansTitleTag()
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
      sbHtml.append("<img id=\"logoimage\" src=\"tata.gif\" />");
      // sbHtml.append("<h1 id=\"title-app\">le titre</h1>");
      sbHtml.append("<img id=\"logoappli\" src=\"titi.gif\" />");
      sbHtml.append("<div id=\"menu\">la div du menu</div>");
      sbHtml.append("<img id=\"minheight\" src=\"toto.gif\" />");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      DivHeaderParser parser = new DivHeaderParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "title-app");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link DivHeaderParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "logoappli"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansLogoTag()
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
      sbHtml.append("<img id=\"logoimage\" src=\"tata.gif\" />");
      sbHtml.append("<h1 id=\"title-app\">le titre</h1>");
      // sbHtml.append("<img id=\"logoappli\" src=\"titi.gif\" />");
      sbHtml.append("<div id=\"menu\">la div du menu</div>");
      sbHtml.append("<img id=\"minheight\" src=\"toto.gif\" />");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      DivHeaderParser parser = new DivHeaderParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "logoappli");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link DivHeaderParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "menu"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansMenuTag()
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
      sbHtml.append("<img id=\"logoimage\" src=\"tata.gif\" />");
      sbHtml.append("<h1 id=\"title-app\">le titre</h1>");
      sbHtml.append("<img id=\"logoappli\" src=\"titi.gif\" />");
      // sbHtml.append("<div id=\"menu\">la div du menu</div>");
      sbHtml.append("<img id=\"minheight\" src=\"toto.gif\" />");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      DivHeaderParser parser = new DivHeaderParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "menu");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link DivHeaderParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise "minheight"<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansMinHeightImg()
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
      sbHtml.append("<img id=\"logoimage\" src=\"tata.gif\" />");
      sbHtml.append("<h1 id=\"title-app\">le titre</h1>");
      sbHtml.append("<img id=\"logoappli\" src=\"titi.gif\" />");
      sbHtml.append("<div id=\"menu\">la div du menu</div>");
      // sbHtml.append("<img id=\"minheight\" src=\"toto.gif\" />");
      sbHtml.append("fin body");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construit l'objet Parser
      DivHeaderParser parser = new DivHeaderParser(); 
      
      // Appel de la méthode de test
      ParserTestTools.verifieLeveeExceptionBaliseManquante(
            parser, sbHtml, "minheight");
      
   }

}
