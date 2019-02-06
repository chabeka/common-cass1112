package fr.urssaf.image.commons.maquette.template;

import java.lang.reflect.Constructor;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.urssaf.image.commons.maquette.config.MaquetteFilterConfig;
import fr.urssaf.image.commons.maquette.definition.ILeftCol;
import fr.urssaf.image.commons.maquette.definition.IMenu;
import fr.urssaf.image.commons.maquette.exception.MaquetteConfigException;
import fr.urssaf.image.commons.maquette.exception.MaquetteThemeException;
import fr.urssaf.image.commons.maquette.exception.MenuException;
import fr.urssaf.image.commons.maquette.template.generator.MenuGenerator;
import fr.urssaf.image.commons.maquette.tool.MenuItem;


/**
 * Représente la configuration de la maquette, pour une requête HTTP donnée
 *
 */
public final class MaquetteConfig
{
	
   private static final Logger LOGGER = LoggerFactory.getLogger(MaquetteConfig.class);

   
   /**
    * Si le navigateur web est MSIE 6.0
    */
   private final Boolean internetExplorer;

   
   /**
    * La configuration du filtre
    */
   private final MaquetteFilterConfig configDuFiltre;

   
   /**
    * L'implémentation pour générer le menu principal<br>
    * <br>
    * Correspond au paramètre "implementationIMenu" du web.xml
    */
   private IMenu implMenu ;
   
   
   /**
    * L'implémentation pour générer la colonne de gauche<br>
    * <br>
    * Correspond au paramètre "implementationILeftCol" du web.xml
    */
   private ILeftCol implLeftCol ;

   /**
    * Constructeur
    * 
    * @param request la requête HTTP en cours
    * @param maqFilterConfig la configuration du filtre
    * 
    * @throws MaquetteThemeException si une erreur se produit concernant le thème à utiliser
    * @throws MaquetteConfigException si une erreur se produit lors de 
    * la lecture de la configuration du filtre de la maquette
    */
   public MaquetteConfig(
         HttpServletRequest request,
         MaquetteFilterConfig maqFilterConfig)
   throws
   MaquetteConfigException,
   MaquetteThemeException
   {
      
      internetExplorer =   
         (request.getHeader("User-Agent")!=null) && 
         (request.getHeader("User-Agent").contains("MSIE 6.0"));
      
      configDuFiltre = maqFilterConfig;
      
      instancieImplMenu();
      
      instancieImplLeftCol();
      
   }
   
   
   /**
    * @return si le navigateur est MSIE 6
    */
   public Boolean isInternetExplorer() {
      return internetExplorer;
   }
   
   
   /**
    * @return la configuration filtre
    */
   public MaquetteFilterConfig getConfigDuFiltre() {
      return configDuFiltre;
   }

   
   /**
    * Renvoie l'implémentation pour générer le menu principal
    * @return L'implémentation pour générer le menu principal
    */
   public IMenu getImplMenu() {
      return implMenu;
   }


   /**
    * Renvoie l'implémentation pour générer la colonne de gauche
    * @return L'implémentation pour générer la colonne de gauche
    */
   public ILeftCol getImplLeftCol() {
      return implLeftCol;
   }
   

   /**
    * Renvoie le rendu HTML du menu
    * 
    * @param request la requête HTTP en cours
    * @return le rendu HTML du menu
    * 
    * @throws MenuException si une erreur se produit lors de la génération du menu principal
    */
   public String getMenu(HttpServletRequest request) throws MenuException
   {
      String html = "" ;
      if(configDuFiltre.getImplMenu()==null)
      {
         LOGGER.debug( "L'implémentation de l'interface IMenu n'est pas disponible" ) ;
      }
      else {
         List<MenuItem> listMenuItem = getImplMenu().getMenu(request) ;
         if(listMenuItem == null) {
            LOGGER.debug( "La méthode getMenu de l'implémentation de l'interface IMenu retourne null, aucun menu n'est affichable" ) ;
         }
         else {
            html = MenuGenerator.buildMenu(listMenuItem, request).toString() ;
         }
      }

      return html ;
   }
   
   
   
   @SuppressWarnings("unchecked")
   private void instancieImplMenu()
   throws MaquetteConfigException {
      
      // Lecture du nom complet de la classe implémentant le menu
      String fullQualifiedName = configDuFiltre.getImplMenu(); 

      // Utilisation de la reflexion pour charger l'implémentation de IMenu   
      if (!StringUtils.isEmpty(fullQualifiedName))
      {
         try {
            
            Class<IMenu> classImplMenu = (Class<IMenu>) Class.forName(fullQualifiedName);
            
            Constructor constructor = classImplMenu.getConstructor() ;
            
            implMenu = (IMenu) constructor.newInstance();
            
         } catch (Exception e) {
            throw new MaquetteConfigException(e);
         }
      }
      
   }
   
   
   @SuppressWarnings("unchecked")
   private void instancieImplLeftCol()
   throws MaquetteConfigException {
      
      // Lecture du nom complet de la classe implémentant les boîtes de gauche
      String fullQualifiedName = configDuFiltre.getImplLeftCol();

      if (!StringUtils.isEmpty(fullQualifiedName))
      {
         try {
            
            Class<ILeftCol> classImplLeftCol = (Class<ILeftCol>) Class.forName( fullQualifiedName ) ;

            Constructor constructor = classImplLeftCol.getConstructor() ;

            implLeftCol = (ILeftCol) constructor.newInstance();
         
         } catch (Exception e) {
            throw new MaquetteConfigException(e);
         }
      }
      
   }

   
}
