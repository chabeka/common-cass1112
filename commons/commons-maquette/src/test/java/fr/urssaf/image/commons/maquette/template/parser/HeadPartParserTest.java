package fr.urssaf.image.commons.maquette.template.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.exception.MissingHtmlElementInTemplateParserException;
import fr.urssaf.image.commons.maquette.exception.MissingSourceParserException;


/**
 * Tests unitaires de la classe {@link HeadPartParser}
 *
 */
@SuppressWarnings("PMD")
public class HeadPartParserTest {

   
   
   /**
    * Test unitaire de la méthode {@link HeadPartParser#doParse}<br>
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
   MissingHtmlElementInTemplateParserException, MissingSourceParserException {
      
      // Le HTML à tester 
      StringBuffer sbHtml = new StringBuffer();
      StringBuffer sbHead = new StringBuffer();
      sbHtml.append("<html>");
      sbHead.append("<head>");
      sbHead.append("<title>Le titre</title>");
      sbHead.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
      sbHead.append("<meta http-equiv=\"Content-Style-Type\" content=\"text/css\" />");
      sbHead.append("<link href=\"getResourceImageMaquette.do?name=/resource/css/menu.css\" rel=\"stylesheet\" type=\"text/css\" />");
      sbHead.append("<link href=\"getResourceImageMaquette.do?name=/resource/css/menu-font.css\" rel=\"stylesheet\" type=\"text/css\" />");
      sbHead.append("<style type=\"text/css\">bla bla</style>");
      sbHead.append("<style type=\"text/css\">bli bli</style>");
      sbHead.append("<script type=\"text/javascript\" src=\"getResourceImageMaquette.do?name=/resource/js/classes/Menu.js\">Menu class</script>");
      sbHead.append("<script type=\"text/javascript\" src=\"getResourceImageMaquette.do?name=/resource/js/domreadyscript.js\">domready script</script>");
      sbHead.append("</head>");
      sbHtml.append(sbHead);
      sbHtml.append("<body>");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construction de l'objet Parser
      HeadPartParser headParser = new HeadPartParser();
      
      // Préparation du HTML à parser
      InputStream inputStream = new ByteArrayInputStream(
            sbHtml.toString().getBytes("UTF-8"));
      Source source = new Source(inputStream) ;
      
      // Appel de la méthode à tester
      headParser.doParse(source);
      
      // ------------------------------------------------------------------------
      // Vérifications du parsing
      // ------------------------------------------------------------------------
      
      // Balise <head>
      // La balise <head> a-t-elle été trouvée ?
      assertNotNull("La balise <head> n'a pas été trouvée",headParser.getHeadTag());
      // La balise <head> a-t-elle été correctement parsé
      assertEquals(
            "La balise <head> n'a pas été correctement parsée",
            sbHead.toString(),
            headParser.getHeadTag().toString());
      
      // Balise <title>
      assertNotNull("La balise <title> n'a pas été trouvée",headParser.getTitleTag());
      assertEquals(
            "La balise <title> n'a pas été correctement parsée",
            "<title>Le titre</title>",
            headParser.getTitleTag().toString());
      
      // Balises <meta>
      assertNotNull(
            "Les balises <meta> n'ont pas été trouvées",
            headParser.getMetaTag());
      assertEquals(
            "Les balises <meta> n'ont pas été correctement parsées",
            2,
            headParser.getMetaTag().size());
      assertEquals(
            "Les balises <meta> n'ont pas été correctement parsées",
            "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />",
            headParser.getMetaTag().get(0).toString());
      assertEquals(
            "Les balises <meta> n'ont pas été correctement parsées",
            "<meta http-equiv=\"Content-Style-Type\" content=\"text/css\" />",
            headParser.getMetaTag().get(1).toString());
      
      // Balises <link>
      assertNotNull(
            "Les balises <link> n'ont pas été trouvées",
            headParser.getLinkTag());
      assertEquals(
            "Les balises <link> n'ont pas été correctement parsées",
            2,
            headParser.getLinkTag().size());
      assertEquals(
            "Les balises <link> n'ont pas été correctement parsées",
            "<link href=\"getResourceImageMaquette.do?name=/resource/css/menu.css\" rel=\"stylesheet\" type=\"text/css\" />",
            headParser.getLinkTag().get(0).toString());
      assertEquals(
            "Les balises <link> n'ont pas été correctement parsées",
            "<link href=\"getResourceImageMaquette.do?name=/resource/css/menu-font.css\" rel=\"stylesheet\" type=\"text/css\" />",
            headParser.getLinkTag().get(1).toString());
      
      // Balises <style>
      assertNotNull(
            "Les balises <style> n'ont pas été trouvées",
            headParser.getStyleTag());
      assertEquals(
            "Les balises <style> n'ont pas été correctement parsées",
            2,
            headParser.getStyleTag().size());
      assertEquals(
            "Les balises <style> n'ont pas été correctement parsées",
            "<style type=\"text/css\">bla bla</style>",
            headParser.getStyleTag().get(0).toString());
      assertEquals(
            "Les balises <style> n'ont pas été correctement parsées",
            "<style type=\"text/css\">bli bli</style>",
            headParser.getStyleTag().get(1).toString());
      
      // Balises <script>
      assertNotNull(
            "Les balises <script> n'ont pas été trouvées",
            headParser.getScriptTag());
      assertEquals(
            "Les balises <script> n'ont pas été correctement parsées",
            2,
            headParser.getScriptTag().size());
      assertEquals(
            "Les balises <script> n'ont pas été correctement parsées",
            "<script type=\"text/javascript\" src=\"getResourceImageMaquette.do?name=/resource/js/classes/Menu.js\">Menu class</script>",
            headParser.getScriptTag().get(0).toString());
      assertEquals(
            "Les balises <script> n'ont pas été correctement parsées",
            "<script type=\"text/javascript\" src=\"getResourceImageMaquette.do?name=/resource/js/domreadyscript.js\">domready script</script>",
            headParser.getScriptTag().get(1).toString());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link HeadPartParser#doParse}<br>
    * <br>
    * Cas de test : pas de balise &lt;head&gt;<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test
   public void doParse_SansHead()
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      // Le HTML à tester 
      StringBuffer sbHtml = new StringBuffer();
      sbHtml.append("<html>");
      sbHtml.append("<body>");
      sbHtml.append("</body>");
      sbHtml.append("</html>");
      
      // Construction de l'objet Parser
      HeadPartParser headParser = new HeadPartParser();
      
      // Préparation du HTML à parser pour la méthode doGetHeadTag
      InputStream inputStream = new ByteArrayInputStream(
            sbHtml.toString().getBytes("UTF-8"));
      Source source = new Source(inputStream) ;
      
      // Appel de la méthode à tester
      // Une exception doit être levée
      try {
         headParser.doParse(source);
      } catch (MissingHtmlElementInTemplateParserException ex) {
         
         // Vérifie le message de l'exception
         assertEquals(
               "Le message de l'exception est incorrect",
               HTMLElementName.HEAD,
               ex.getMessage());
         
         // Si on arrive jusque là, le test est OK
         return;
         
      }
      
      // Si on arrive jusque là, c'est que l'exception attendu n'a pas été levée
      fail("L'exception attendue MissingHtmlElementInTemplateParserException n'a pas été levée");
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link HeadPartParser#doParse}<br>
    * <br>
    * Cas de test : la source HTML est null<br>
    * <br>
    * Résultat attendu : une exception est levée
    * 
    * @throws IOException
    * @throws MissingHtmlElementInTemplateParserException
    * @throws MissingSourceParserException  
    */
   @Test(expected=MissingSourceParserException.class)
   public void doParse_SourceNull()
   throws 
   IOException, 
   MissingHtmlElementInTemplateParserException,
   MissingSourceParserException {
      
      HeadPartParser headParser = new HeadPartParser();
      headParser.doParse(null);
      
   }
      
   
}
