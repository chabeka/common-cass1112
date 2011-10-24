package fr.urssaf.image.sae.webdemo.maquette;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import fr.urssaf.image.commons.maquette.definition.IMenu;
import fr.urssaf.image.commons.maquette.exception.MenuException;
import fr.urssaf.image.commons.maquette.tool.MenuItem;
import fr.urssaf.image.sae.webdemo.component.MessageComponent;

/**
 * Menu de l'IHM maquette<br>
 * La menu est configué dans le fichier <code>web.xml</code>
 * 
 * <pre>
 * &lt;init-param>
 *          &lt;param-name>implementationIMenu</param-name>
 *          &lt;param-value>fr.urssaf.image.sae.webdemo.maquette.BoitesGauches</param-value>
 * &lt;/init-param>
 * 
 * </pre>
 * 
 * @see IMenu
 */
public class Menus implements IMenu {

   /**
    * aucune implémentation
    */
   @Override
   public final String getBreadcrumb(HttpServletRequest request) {
      return null;
   }

   /**
    * Menus :
    * <ul>
    * <li>Accueil : <code>/accueil.html</code></li>
    * <li>Trace :
    * <ul>
    * <li>Registre d'exploitation : <code>/registre_exploitation.html</code></li>
    * </ul>
    * </li>
    * 
    * </ul>
    * 
    * {@inheritDoc}
    */
   @Override
   public final List<MenuItem> getMenu(HttpServletRequest request)
         throws MenuException {

      // Construction de la collection de menus
      List<MenuItem> menus = new ArrayList<MenuItem>();

      // Le menu Accueil
      MenuItem accueil = new MenuItem();

      accueil.setTitle(MessageComponent.getMessage("menu.accueil", request));
      accueil.setLink("accueil.html");
      menus.add(accueil);

      // Le menu Trace
      MenuItem trace = new MenuItem();
      trace.setTitle(MessageComponent.getMessage("menu.trace", request));
      menus.add(trace);

      // Le menu Gestion\Registre d'exploitation
      MenuItem regExpl = new MenuItem();
      regExpl.setTitle(MessageComponent.getMessage("menu.regExpl", request));
      regExpl.setLink("registre_exploitation.html");

      trace.addChild(regExpl);

      return menus;
   }

}
