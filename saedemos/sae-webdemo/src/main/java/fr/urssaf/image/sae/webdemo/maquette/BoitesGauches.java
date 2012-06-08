package fr.urssaf.image.sae.webdemo.maquette;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContextHolder;

import fr.urssaf.image.commons.maquette.definition.ILeftCol;
import fr.urssaf.image.commons.maquette.tool.InfoBoxItem;
import fr.urssaf.image.sae.webdemo.security.SecurityAuthentication;

/**
 * Boites de gauche de l'IHM maquette<br>
 * La configuration correspond à dans le fichier <code>web.xml</code>
 * 
 * <pre>
 * &lt;init-param>
 *          &lt;param-name>implementationILeftCol</param-name>
 *          &lt;param-value>fr.urssaf.image.sae.webdemo.maquette.BoitesGauches</param-value>
 * &lt;/init-param>
 * 
 * </pre>
 * <ul>
 * <li><code>nomUtilisateur</code> : Prénom NOM du VI</li>
 * <li><code>roleUtilisateur</code> : 1ère habilitation du VI</li>
 * <li><code>nomApplication</code> :
 * <code>SAE - Application web de démonstration</code></li>
 * <li><code>versionApplication</code> : <code>0.1</code></li>
 * <li><code>Déconnexion</code> : <code>/logout</code></li>
 * </ul>
 * 
 * @see ILeftCol
 */
public class BoitesGauches implements ILeftCol {

   @Override
   public final List<InfoBoxItem> getInfoBox(HttpServletRequest request) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public final String getLienDeconnexion(HttpServletRequest request) {

      String link = null;

      if (SecurityContextHolder.getContext().getAuthentication() instanceof SecurityAuthentication) {

         link = "javascript:document.location.href='logout'";
      }

      return link;
   }

   @Override
   public final String getNomApplication(HttpServletRequest request) {
      return "SAE - Application web de démonstration";
   }

   @Override
   public final String getNomUtilisateur(HttpServletRequest request) {

      String nomUtilisateur = null;

      if (SecurityContextHolder.getContext().getAuthentication() instanceof SecurityAuthentication) {

         SecurityAuthentication jeton = (SecurityAuthentication) SecurityContextHolder
               .getContext().getAuthentication();

         if (jeton != null) {
            nomUtilisateur = jeton.getPrincipal();
         }

      }

      return nomUtilisateur;
   }

   @Override
   public final String getRoleUtilisateur(HttpServletRequest request) {

      String roleUtilisateur = null;

      if (SecurityContextHolder.getContext().getAuthentication() instanceof SecurityAuthentication) {

         SecurityAuthentication jeton = (SecurityAuthentication) SecurityContextHolder
               .getContext().getAuthentication();

         if (jeton != null) {

            roleUtilisateur = jeton.getCredentials();
         }

      }

      return roleUtilisateur;
   }

   @Override
   public final String getVersionApplication(HttpServletRequest request) {
      return "0.1";
   }

}
