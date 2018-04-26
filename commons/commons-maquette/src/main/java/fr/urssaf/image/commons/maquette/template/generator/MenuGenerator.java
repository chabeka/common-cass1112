package fr.urssaf.image.commons.maquette.template.generator;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.maquette.session.SessionTools;
import fr.urssaf.image.commons.maquette.tool.MenuItem;

/**
 * Classe qui effectue le rendu HTML du menu haut de la maquette<br>
 * <br>
 * Elle stocke également le menu en cours<br>
 * <br>
 * Il s'agit d'un singleton
 * 
 */
public final class MenuGenerator {

   private static final Logger LOGGER = LoggerFactory.getLogger(MenuGenerator.class);

   private MenuGenerator() {

   }

   /**
    * Effectue le rendu HTML du menu
    * 
    * @param listItemsMenu
    *           le menu
    * @param request
    *           la requête HTTP en cours
    * @return le rendu HTML
    * 
    */
   public static StringBuilder buildMenu(List<MenuItem> listItemsMenu,
         HttpServletRequest request) {
      StringBuilder html = new StringBuilder();
      for (int i = 0; i < listItemsMenu.size(); i++) {
         // début de ul
         if (i == 0 || !listItemsMenu.get(i).hasParent()) {
            html.append("<ul>");
         }

         html.append(addRowToMenu(listItemsMenu.get(i), request));

         // fin de ul
         if (i == (listItemsMenu.size() - 1)
               || !listItemsMenu.get(i).hasParent()) {
            html.append("</ul>");
         }
      }

      return html;
   }

   private static StringBuilder addRowToMenu(MenuItem menuItem,
         HttpServletRequest request) {

      String requestURL = request.getServletPath().substring(
            request.getServletPath().indexOf("/") + 1);

      if (requestURL.compareTo(menuItem.getLink()) == 0) {
         LOGGER.debug(String.format(
               "Le menu en cours est stocké (Menu=\"%s\", RequestURL=\"%s\")",
               menuItem.getTitle(), requestURL));
         SessionTools.storeSelectedMenu(request, menuItem);
      }

      /*
       * // Traitement du menu StringBuilder html = new StringBuilder() ;
       * html.append( "<li><a href='" + menuItem.getLink() + "' class='" ) ;
       * 
       * // mise en évidence de la première ligne if( !menuItem.hasParent() ) {
       * html.append( "firstrow" ); }
       * 
       * String title = StringEscapeUtils.escapeHtml(menuItem.getTitle());
       * String description =
       * StringEscapeUtils.escapeHtml(menuItem.getDescription());
       * 
       * html.append( "' title='" + description + "' tabindex='" ) ; /*
       */

      // Traitement du menu
      String title = StringEscapeUtils.escapeHtml(menuItem.getTitle());
      String description = StringEscapeUtils.escapeHtml(menuItem
            .getDescription());
      String href = menuItem.getLink();
      String tabIndex;
      if (menuItem.hasParent()) {
         tabIndex = "9999";
      } else {
         tabIndex = "0";
      }

      // Traitement du menu
      StringBuilder html = new StringBuilder();
      html.append("<li>");
      html.append(String.format("<a href='%s'", href));
      if (!menuItem.hasParent()) {
         // mise en évidence de la première ligne
         html.append(" class='firstrow'");
      }
      html.append(String.format(" title='%s'", description));
      html.append(String.format(" tabindex='%s'", tabIndex));
      html.append(">"); // fin de la balise <a
      html.append(title);
      html.append("</a>");

      // recherche des enfants
      if (menuItem.hasChildren()) {
         html.append(buildMenu(menuItem.getChildren(), request));
      }

      html.append("</li>");

      return html;
   }

   /**
    * Effectue le rendu HTML du fil d'ariane<br>
    * <br>
    * Se base sur une variable statique qui stocke le menu en cours
    * 
    * @param request
    *           la requête HTTP en cours
    * @return le rendu HTML du fil d'ariane
    */
   public static String buildBreadcrumb(HttpServletRequest request) {
      StringBuffer sbFilAriane = new StringBuffer();

      MenuItem selectedMenu = SessionTools.getSelectedMenu(request);

      if (selectedMenu != null) {
         MenuItem item = selectedMenu;

         List<MenuItem> listMenuItem = new ArrayList<MenuItem>();
         do {
            listMenuItem.add(item);
            item = item.getParent();
         } while (item != null);

         for (int i = (listMenuItem.size() - 1); i >= 0; i--) {
            sbFilAriane.append(listMenuItem.get(i).getTitle());
            if (i != 0) {
               sbFilAriane.append(" &gt; ");
            }
         }

      }

      return sbFilAriane.toString();
   }

}
