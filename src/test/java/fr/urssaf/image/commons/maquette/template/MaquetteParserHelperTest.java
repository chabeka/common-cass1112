package fr.urssaf.image.commons.maquette.template;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import net.htmlparser.jericho.Attributes;
import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.OutputDocument;
import net.htmlparser.jericho.Source;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;


/**
 * Tests unitaires de la classe {@link MaquetteParserHelper}
 *
 */
@SuppressWarnings("PMD")
public class MaquetteParserHelperTest {

   
   /**
    * Test du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(MaquetteParserHelper.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test de la méthode {@link MaquetteParserHelper#fusionneAttributs(OutputDocument, Element, Attributes)}<br>
    * <br>
    * Cas de test :<br>
    * <ul>
    *    <li>la balise source possède des attributs
    *    <li>la liste des attributs supplémentaires n'est pas vide
    * </ul>
    * 
    */
   @Test
   public void fusionneAttributs_Cas1() {
      
      final String html1 = "<div attr1=\"valeur1\" attr2=\"valeur2\">contenu</div>";
      final String html2 = "<font attr3=\"valeur3\" attr4=\"valeur4\">contenu</font>";
      
      Source documentSource1 = new Source(html1);
      Source documentSource2 = new Source(html2);
      
      Element baliseSource = documentSource1.getAllElements("div").get(0);
      
      OutputDocument outDoc = new OutputDocument(documentSource1);
      
      Attributes attrSupp = documentSource2.getAllElements("font").get(0).getAttributes();
      
      MaquetteParserHelper.fusionneAttributs(
            outDoc, 
            baliseSource, 
            attrSupp);
      
      String actual = outDoc.toString();
      String expected = "<div attr1=\"valeur1\" attr2=\"valeur2\" attr3=\"valeur3\" attr4=\"valeur4\">contenu</div>";
      assertEquals("La fusion des attributs a échoué",expected,actual);
      
   }
   
   
   
   /**
    * Test de la méthode {@link MaquetteParserHelper#fusionneAttributs(OutputDocument, Element, Attributes)}<br>
    * <br>
    * Cas de test :<br>
    * <ul>
    *    <li>la balise source ne possède pas d'attributs
    *    <li>la liste des attributs supplémentaires n'est pas vide
    * </ul>
    * 
    */
   @Test
   public void fusionneAttributs_Cas2() {
      
      final String html1 = "<div>contenu</div>";
      final String html2 = "<font attr3=\"valeur3\" attr4=\"valeur4\">contenu</font>";
      
      Source documentSource1 = new Source(html1);
      Source documentSource2 = new Source(html2);
      
      Element baliseSource = documentSource1.getAllElements("div").get(0);
      
      OutputDocument outDoc = new OutputDocument(documentSource1);
      
      Attributes attrSupp = documentSource2.getAllElements("font").get(0).getAttributes();
      
      MaquetteParserHelper.fusionneAttributs(
            outDoc, 
            baliseSource, 
            attrSupp);
      
      String actual = outDoc.toString();
      String expected = "<div attr3=\"valeur3\" attr4=\"valeur4\">contenu</div>";
      assertEquals("La fusion des attributs a échoué",expected,actual);
      
   }
   
   
   /**
    * Test de la méthode {@link MaquetteParserHelper#fusionneAttributs(OutputDocument, Element, Attributes)}<br>
    * <br>
    * Cas de test :<br>
    * <ul>
    *    <li>la balise source possède des attributs
    *    <li>la liste des attributs supplémentaires est vide
    * </ul>
    * 
    */
   @Test
   public void fusionneAttributs_Cas3() {
      
      final String html1 = "<div attr1=\"valeur1\" attr2=\"valeur2\">contenu</div>";
      final String html2 = "<font>contenu</font>";
      
      Source documentSource1 = new Source(html1);
      Source documentSource2 = new Source(html2);
      
      Element baliseSource = documentSource1.getAllElements("div").get(0);
      
      OutputDocument outDoc = new OutputDocument(documentSource1);
      
      Attributes attrSupp = documentSource2.getAllElements("font").get(0).getAttributes();
      
      MaquetteParserHelper.fusionneAttributs(
            outDoc, 
            baliseSource, 
            attrSupp);
      
      String actual = outDoc.toString();
      String expected = "<div attr1=\"valeur1\" attr2=\"valeur2\">contenu</div>";
      assertEquals("La fusion des attributs a échoué",expected,actual);
      
   }
   
}
