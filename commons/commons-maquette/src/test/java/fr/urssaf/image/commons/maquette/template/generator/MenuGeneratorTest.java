package fr.urssaf.image.commons.maquette.template.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import fr.urssaf.image.commons.maquette.exception.ReferentialIntegrityException;
import fr.urssaf.image.commons.maquette.session.SessionTools;
import fr.urssaf.image.commons.maquette.tool.MenuItem;
import fr.urssaf.image.commons.maquette.util.TestConstructeurPriveException;
import fr.urssaf.image.commons.maquette.util.TestsUtils;

/**
 * Tests unitaires de la classe {@link MenuGenerator}
 * 
 */
@SuppressWarnings("PMD")
public class MenuGeneratorTest {

   /**
    * Test unitaire du constructeur privé, pour le code coverage
    * 
    * @throws TestConstructeurPriveException
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils
            .testConstructeurPriveSansArgument(MenuGenerator.class);
      assertTrue("Le constructeur privé n'a pas été trouvé", result);
   }

   /**
    * Test unitaire de la méthode {@link MenuGenerator#buildMenu}<br>
    * <br>
    * Cas de test : le menu est vide (aucun item)<br>
    * <br>
    * Résultat attendu : le menu est vide
    */
   @Test
   public void buildMenu_MenuVide() {

      // La request
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setRequestURI("/site/page1.do");

      // Construction du menu
      // => menu vide
      List<MenuItem> listItemsMenu = new ArrayList<MenuItem>();

      // Appel de la méthode de rendu
      StringBuilder sbHtml = MenuGenerator.buildMenu(listItemsMenu, request);

      // Vérifie le résultat attendu
      String sExpected = "";
      String sActual = sbHtml.toString();
      assertEquals("Le rendu HTML aurait dû être vide", sExpected, sActual);

   }

   /**
    * Test unitaire de la méthode {@link MenuGenerator#buildMenu}
    * 
    * @throws ReferentialIntegrityException
    */
   @Test
   public void buildMenu() throws ReferentialIntegrityException {

      // La request
      // On utilise le lien du 1er item de menu pour passer
      // dans la condition qui stocke le menu en cours afin
      // de constituer ensuite le fil d'ariane
      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setServletPath("/C1L1_link");

      // Construction du menu
      List<MenuItem> listItemsMenu = new ArrayList<MenuItem>();

      // Menu à construire :
      // C1L1 C2L1
      // C2L2
      // C2L3

      // C1L1
      MenuItem item_C1L1 = new MenuItem();
      item_C1L1.setTitle("C1L1_titre");
      item_C1L1.setDescription("C1L1_description");
      item_C1L1.setLink("C1L1_link");
      listItemsMenu.add(item_C1L1);
      // C2L1
      MenuItem item_C2L1 = new MenuItem();
      item_C2L1.setTitle("C2L1_titre");
      item_C2L1.setDescription("C2L1_description");
      item_C2L1.setLink("C2L1_link");
      listItemsMenu.add(item_C2L1);
      // C2L2
      MenuItem item_C2L2 = new MenuItem();
      item_C2L2.setTitle("C2L2_titre");
      item_C2L2.setDescription("C2L2_description");
      item_C2L2.setLink("C2L2_link");
      item_C2L1.addChild(item_C2L2);
      // C2L3
      MenuItem item_C2L3 = new MenuItem();
      item_C2L3.setTitle("C2L3_titre_<éà");
      item_C2L3.setDescription("C2L3_description_<éà");
      item_C2L3.setLink("C2L3_link");
      item_C2L1.addChild(item_C2L3);

      // Appel de la méthode de rendu
      StringBuilder sbHtml = MenuGenerator.buildMenu(listItemsMenu, request);

      // Résultat attendu
      StringBuilder sbExpected = new StringBuilder();
      sbExpected.append("<ul>");
      sbExpected.append("<li>");
      sbExpected
            .append("<a href='C1L1_link' class='firstrow' title='C1L1_description' tabindex='0'>C1L1_titre</a>");
      sbExpected.append("</li>");
      sbExpected.append("</ul>");
      sbExpected.append("<ul>");
      sbExpected.append("<li>");
      sbExpected
            .append("<a href='C2L1_link' class='firstrow' title='C2L1_description' tabindex='0'>C2L1_titre</a>");
      sbExpected.append("<ul>");
      sbExpected.append("<li>");
      sbExpected
            .append("<a href='C2L2_link' title='C2L2_description' tabindex='9999'>C2L2_titre</a>");
      sbExpected.append("</li>");
      sbExpected.append("<li>");
      sbExpected
            .append("<a href='C2L3_link' title='C2L3_description_&lt;&eacute;&agrave;' tabindex='9999'>C2L3_titre_&lt;&eacute;&agrave;</a>");
      sbExpected.append("</li>");
      sbExpected.append("</ul>");
      sbExpected.append("</li>");
      sbExpected.append("</ul>");

      // Vérification du résultat
      assertEquals("Le rendu du menu est incorrect", sbExpected.toString(),
            sbHtml.toString());

      // On en profite pour vérifier que le menu C1L1 est bien
      // considéré comme le menu sélectionné
      assertSame("Le menu sélectionné est incorrect", item_C1L1, SessionTools
            .getSelectedMenu(request));

   }

   /**
    * Test unitaire de la méthode {@link MenuGenerator#buildBreadcrumb}
    */
   @Test
   public void buildBreadcrumb() {

      MockHttpServletRequest request = new MockHttpServletRequest();
      request.setServletPath("/item3_link");

      // ---------------------------------------------------------------
      // 1ère étape : appel de la construction du menu
      // pour provoquer le stockage du menu en cours
      // ---------------------------------------------------------------

      // Menu à construire :
      // item1
      // item2
      // item3 <==== menu sélectioné

      // La request
      // MockHttpServletRequest request = new MockHttpServletRequest();
      // request.setRequestURI("item3_link");

      // Liste
      List<MenuItem> listItemsMenu = new ArrayList<MenuItem>();
      // item1
      MenuItem item1 = new MenuItem();
      item1.setTitle("item1_titre");
      item1.setDescription("item1_description");
      item1.setLink("item1_link");
      listItemsMenu.add(item1);
      // item2
      MenuItem item2 = new MenuItem();
      item2.setTitle("item2_titre");
      item2.setDescription("item2_description");
      item2.setLink("item2_link");
      item1.addChild(item2);
      // item3
      MenuItem item3 = new MenuItem();
      item3.setTitle("item3_titre");
      item3.setDescription("item3_description");
      item3.setLink("item3_link");
      item2.addChild(item3);

      // Construction du menu
      MenuGenerator.buildMenu(listItemsMenu, request);

      // Vérifie que le menu sélectionné est le bon,
      // avant de continuer
      assertSame("Le menu sélectionné est incorrect", item3, SessionTools
            .getSelectedMenu(request));

      // ---------------------------------------------------------------
      // 2ème étape : construction du fil d'ariane
      // ---------------------------------------------------------------

      // Appel de la méthode à tester
      String filAriane = MenuGenerator.buildBreadcrumb(request);

      // Vérification du résultat
      String sExpected = "item1_titre &gt; item2_titre &gt; item3_titre";
      String sActual = filAriane;
      assertEquals("Le fil d'ariane est incorrect", sExpected, sActual);

   }

}
