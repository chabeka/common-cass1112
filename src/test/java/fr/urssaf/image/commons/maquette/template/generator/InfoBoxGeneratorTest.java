package fr.urssaf.image.commons.maquette.template.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import fr.urssaf.image.commons.maquette.exception.MissingInfoBoxPropertyException;
import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;
import fr.urssaf.image.commons.maquette.tool.MaquetteConstant;
import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;


/**
 * Tests unitaires de la classe {@link InfoGoxGenerator}
 *
 */
@SuppressWarnings("PMD")
public class InfoBoxGeneratorTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException 
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(InfoBoxGenerator.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test unitaire de la méthode {@link InfoBoxGenerator#build}<br>
    * <br>
    * Cas de test : L'identifiant de la boîte de gauche n'est pas spécifié<br>
    * <br>
    * Résultat attendu : levée d'une exception
    * 
    * @throws MissingInfoBoxPropertyException 
    */
   @Test
   public void build_TestMissingShortIdentifier() 
   throws MissingInfoBoxPropertyException {
      
      InfoBoxItem boiteGauche = new InfoBoxItem();
      
      try {
         InfoBoxGenerator.build(boiteGauche);
      } catch (MissingInfoBoxPropertyException ex) {
         
         assertEquals(
               "Le propriété manquante indiquée dans l'exception est incorrecte",
               "shortIdentifier",
               ex.getMissingProperty());
         
         return;
         
      }
      
      fail("L'exception attendu n'a pas été levée");

   }
   
   
   /**
    * Test unitaire de la méthode {@link InfoBoxGenerator#build}<br>
    * <br>
    * Cas de test : le contenu HTML de la boîte de gauche n'est pas spécifié<br>
    * <br>
    * Résultat attendu : levée d'une exception
    * 
    * @throws MissingInfoBoxPropertyException 
    */
   @Test
   public void build_TestMissingContent() 
   throws MissingInfoBoxPropertyException {
      
      InfoBoxItem boiteGauche = new InfoBoxItem();
      boiteGauche.setShortIdentifier("boite_id");
      
      try {
         InfoBoxGenerator.build(boiteGauche);
      } catch (MissingInfoBoxPropertyException ex) {
         
         assertEquals(
               "Le propriété manquante indiquée dans l'exception est incorrecte",
               "content",
               ex.getMissingProperty());
         
         return;
         
      }
      
      fail("L'exception attendu n'a pas été levée");

   }
   
   
   
   /**
    * Test unitaire de la méthode {@link InfoBoxGenerator#build}<br>
    * <br>
    * Cas de test : le titre de la boîte de gauche n'est pas spécifié<br>
    * <br>
    * Résultat attendu : levée d'une exception
    * 
    * @throws MissingInfoBoxPropertyException 
    */
   @Test
   public void build_TestMissingTitle() 
   throws MissingInfoBoxPropertyException {
      
      InfoBoxItem boiteGauche = new InfoBoxItem();
      boiteGauche.setShortIdentifier("boite_id");
      boiteGauche.setContent("boite_content");
      
      try {
         InfoBoxGenerator.build(boiteGauche);
      } catch (MissingInfoBoxPropertyException ex) {
         
         assertEquals(
               "Le propriété manquante indiquée dans l'exception est incorrecte",
               "title",
               ex.getMissingProperty());
         
         return;
         
      }
      
      fail("L'exception attendu n'a pas été levée");

   }
   
   
   
   /**
    * Test unitaire de la méthode {@link InfoBoxGenerator#build}<br>
    * <br>
    * Cas de test : la description de la boîte de gauche n'est pas spécifié<br>
    * <br>
    * Résultat attendu : levée d'une exception
    * 
    * @throws MissingInfoBoxPropertyException 
    */
   @Test
   public void build_TestMissingDesc() 
   throws MissingInfoBoxPropertyException {
      
      InfoBoxItem boiteGauche = new InfoBoxItem();
      boiteGauche.setShortIdentifier("boite_id");
      boiteGauche.setContent("boite_content");
      boiteGauche.setTitle("boite_titre");
      
      try {
         InfoBoxGenerator.build(boiteGauche);
      } catch (MissingInfoBoxPropertyException ex) {
         
         assertEquals(
               "Le propriété manquante indiquée dans l'exception est incorrecte",
               "boxDesc",
               ex.getMissingProperty());
         
         return;
         
      }
      
      fail("L'exception attendu n'a pas été levée");

   }
   
   
   
   /**
    * Test unitaire de la méthode {@link InfoBoxGenerator#build}<br>
    * <br>
    * Cas de test : cas normal
    * <br>
    * Résultat attendu : obtention du rendu HTML
    * 
    * @throws MissingInfoBoxPropertyException 
    *  
    */
   @Test
   public void build_TestStandard() 
   throws MissingInfoBoxPropertyException {
      
      InfoBoxItem boiteGauche = new InfoBoxItem();
      
      boiteGauche.setShortIdentifier("box_id");
      boiteGauche.setTitle("box_title");
      boiteGauche.setBoxDesc("box_desc");
      boiteGauche.setContent("box_content");
      
      String html = InfoBoxGenerator.build(boiteGauche);
      
      StringBuffer sbExpected = new StringBuffer();
      sbExpected.append("<h3 class=\"boxTitle\" id=\"box_id-title\">box_title</h3>");
      sbExpected.append(MaquetteConstant.HTML_CRLF);
      sbExpected.append("<p class=\"boxContent\" id=\"box_id\" title=\"box_desc\">");
      sbExpected.append(MaquetteConstant.HTML_CRLF);
      sbExpected.append("box_content</p>");
      sbExpected.append(MaquetteConstant.HTML_CRLF);
      
      String sExpected = sbExpected.toString() ;
      
      String sActual = html;
      
      assertEquals("Erreur dans le rendu HTML",sExpected,sActual);
      
   }
   
}
