package fr.urssaf.image.commons.maquette.fixture;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import fr.urssaf.image.commons.maquette.definition.IMenu;
import fr.urssaf.image.commons.maquette.tool.MenuItem;

/**
 * Implémentation du menu principal pour les tests unitaires 
 *
 */
@SuppressWarnings("PMD")
public final class FixtureMenu implements IMenu {

   
   public static final String REQUEST_HEADER_POUR_AVOIR_UN_MENU = "avec_menu";
   public static final String REQUEST_HEADER_POUR_AVOIR_UN_FIL_ARIANE = "avec_filAriane";
   
   public static final String FIL_ARIANE_TEST = "LeFilArianeContextuelALaRequest";
   
   public static final String LINK_MENU_POUR_TEST = "LeLink";
   public static final String TITRE_MENU_POUR_TEST = "Titre";
   
   
   private Boolean isAvecMenu(HttpServletRequest request) {
      return !StringUtils.isEmpty(request.getHeader(REQUEST_HEADER_POUR_AVOIR_UN_MENU)) ;
   }
   
   
   private Boolean isAvecFilAriane(HttpServletRequest request) {
      return !StringUtils.isEmpty(request.getHeader(REQUEST_HEADER_POUR_AVOIR_UN_FIL_ARIANE)) ;
   }
   
   
   @Override
   public String getBreadcrumb(HttpServletRequest request) {
      
      if (isAvecFilAriane(request)) {
         return FIL_ARIANE_TEST;
      }
      else {
         return null;         
      }
      
   }

   
   @Override
   public List<MenuItem> getMenu(HttpServletRequest request) {
      
      if (isAvecMenu(request)) {
         
         List<MenuItem> result = new ArrayList<MenuItem>();
         
         MenuItem item = new MenuItem();
         item.setTitle(TITRE_MENU_POUR_TEST);
         item.setDescription("Description");
         item.setLink(LINK_MENU_POUR_TEST);
         result.add(item);
         
         return result;
         
      }
      else {
         return null;
      }
      
   }
   
   
   protected static String getRenduMenu() {
      return 
         "<ul><li><a href='" + 
         LINK_MENU_POUR_TEST + 
         "' class='firstrow' title='Description' tabindex='0'>" + 
         TITRE_MENU_POUR_TEST + 
         "</a></li></ul>";
   }

}
