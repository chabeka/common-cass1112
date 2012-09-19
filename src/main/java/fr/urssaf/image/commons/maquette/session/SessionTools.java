package fr.urssaf.image.commons.maquette.session;

import javax.servlet.http.HttpServletRequest;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.tool.MenuItem;


/**
 * Stockage et lecture des objets en session 
 *
 */
public final class SessionTools {

   
   private static final String SESSION_FILTER = "maquetteFilterConfig" ;
   private static final String SESSION_MENU = "selectedMenu" ;
   
   
   private SessionTools() {
      
   }
   
   
   /**
    * Enregistre en session la configuration du filtre
    * 
    * @param request la requête HTTP en cours
    * @param maqFilterConfig la configuration du filtre
    */
   public static void storeFilterConfig(
         HttpServletRequest request,
         MaquetteFilterConfig maqFilterConfig) {
      request.getSession().setAttribute(SESSION_FILTER, maqFilterConfig) ;
   }
   
   
   /**
    * Renvoie la configuration du filtre de la maquette, stocké en session
    * 
    * @param request la requête HTTP
    * @return l'objet MaquetteFilterConfig, ou null si la configuration 
    *         n'est pas stockée en session
    */
   public static MaquetteFilterConfig getFilterConfig(HttpServletRequest request) {
      

      MaquetteFilterConfig result;
      
      Object obj = request.getSession().getAttribute(SESSION_FILTER);
      
      if (obj==null) {
         result = null; // NOPMD
      }
      else {
         result = (MaquetteFilterConfig)obj;
      }
         
      return result;
      
   }
   
   
   
   /**
    * Enregistre en session le menu en cours
    * 
    * @param request la requête HTTP en cours
    * @param selectedMenu le menu en cours
    */
   public static void storeSelectedMenu(
         HttpServletRequest request,
         MenuItem selectedMenu) {
      request.getSession().setAttribute(SESSION_MENU, selectedMenu) ;
   }
   
   
   
   /**
    * Renvoie le menu en cours, stocké en session
    * 
    * @param request la requête HTTP
    * @return l'objet MenuItem, ou null si le menu en cours n'est pas
    *         stocké en session
    */
   public static MenuItem getSelectedMenu(HttpServletRequest request) {
      

      MenuItem result;
      
      Object obj = request.getSession().getAttribute(SESSION_MENU);
      
      if (obj==null) {
         result = null; // NOPMD
      }
      else {
         result = (MenuItem)obj;
      }
         
      return result;
      
   }
   
   
}
