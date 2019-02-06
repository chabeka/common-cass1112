package fr.urssaf.image.commons.maquette.tool;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


/**
 * Tests unitaires de la méthode {@link InfoBoxItem}
 *
 */
@SuppressWarnings("PMD")
public class InfoBoxItemTest {
   
   
   /**
    * Tests unitaires des getter/setter
    */
   @Test
   public void getterSetters() {
      
      // Instancie un objet
      InfoBoxItem infoBoxItem = new InfoBoxItem();
      
      // Vérifie la configuration initiale
      assertEquals(
            "La configuration initiale de l'objet est incorrecte",
            "",
            infoBoxItem.getShortIdentifier());
      assertEquals(
            "La configuration initiale de l'objet est incorrecte",
            "",
            infoBoxItem.getTitle());
      assertEquals(
            "La configuration initiale de l'objet est incorrecte",
            "",
            infoBoxItem.getBoxDesc());
      assertEquals(
            "La configuration initiale de l'objet est incorrecte",
            "",
            infoBoxItem.getContent());
      
      // Test pour shortIdentifier
      infoBoxItem.setShortIdentifier("le_id");
      assertEquals(
            "Echec du getter/setter de shortIdentifier",
            "le_id",
            infoBoxItem.getShortIdentifier());
      
      // Test pour title
      infoBoxItem.setTitle("le_title");
      assertEquals(
            "Echec du getter/setter de title",
            "le_title",
            infoBoxItem.getTitle());
      
      // Test pour boxDesc
      infoBoxItem.setBoxDesc("le_boxDesc");
      assertEquals(
            "Echec du getter/setter de boxDesc",
            "le_boxDesc",
            infoBoxItem.getBoxDesc());
      
      // Test pour content
      infoBoxItem.setContent("le_content");
      assertEquals(
            "Echec du getter/setter de content",
            "le_content",
            infoBoxItem.getContent());
      
   }
   
   
   /**
    * Tests unitaires des constructeurs
    */
   @Test
   public void constructeurs() {
      
      InfoBoxItem infoBoxItem;
      
      // Constructeur par défaut
      infoBoxItem = new InfoBoxItem();
      assertEquals(
            "Erreur dans le constructeur sans argument",
            "",
            infoBoxItem.getShortIdentifier());
      assertEquals(
            "Erreur dans le constructeur sans argument",
            "",
            infoBoxItem.getTitle());
      assertEquals(
            "Erreur dans le constructeur sans argument",
            "",
            infoBoxItem.getBoxDesc());
      assertEquals(
            "Erreur dans le constructeur sans argument",
            "",
            infoBoxItem.getContent());
      
      // Constructeur avec 3 paramètres
      infoBoxItem = new InfoBoxItem("le_shortIdentifier","le_title","le_boxDesc");
      assertEquals(
            "Erreur dans le constructeur avec 3 arguments",
            "le_shortIdentifier",
            infoBoxItem.getShortIdentifier());
      assertEquals(
            "Erreur dans le constructeur avec 3 arguments",
            "le_title",
            infoBoxItem.getTitle());
      assertEquals(
            "Erreur dans le constructeur avec 3 arguments",
            "le_boxDesc",
            infoBoxItem.getBoxDesc());
      assertEquals(
            "Erreur dans le constructeur avec 3 arguments",
            "",
            infoBoxItem.getContent());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link InfoBoxItem#addSpan}
    */
   @Test
   public void addSpan() {
      
      InfoBoxItem infoBoxItem = new InfoBoxItem("box_id","box_title","box_desc");
      
      infoBoxItem.setContent("ContenuExistant");
      
      infoBoxItem.addSpan("span_id", "span_title", "span_content");
      
      String sExpected = String.format(
            "ContenuExistant<span id=\"%s-%s\" title=\"%s\">%s</span><br />%s", 
            "box_id",
            "span_id",
            "span_title",
            "span_content",
            MaquetteConstant.HTML_CRLF);
      
      String sActual = infoBoxItem.getContent();
      
      assertEquals("Erreur dans la méthode d'ajout d'un span",sExpected,sActual);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link InfoBoxItem#addBtn}
    */
   @Test
   public void addBtn() {
      
      InfoBoxItem infoBoxItem = new InfoBoxItem("box_id","box_title","box_desc");
      
      infoBoxItem.setContent("ContenuExistant");
      
      infoBoxItem.addBtn("btn_id", "btn_name", "btn_js");
      
      String sExpected = String.format(
            "ContenuExistant<input id=\"%s-%s\" class=\"%s-%s\" type=\"button\" value=\"%s\" onclick=\"%s\" tabindex=\"0\" />", 
            "box_id",
            "btn_id",
            "box_id",
            "btn_id",
            "btn_name",
            "btn_js"
      );
      
      String sActual = infoBoxItem.getContent();
      
      assertEquals("Erreur dans la méthode d'ajout d'un bouton",sExpected,sActual);
      
   }

}
