package fr.urssaf.image.commons.maquette.tool;

import java.util.regex.Pattern;


/**
 * 
 * Classe de matchage entre une URI et un pattern, pour l'application d'un filtre 
 * ({@link javax.servlet.Filter}).<br>
 * <br>
 * L'algorithme de matchage utilisé dans cette classe a été récupéré des sources
 * de Apache Tomcat 6.0.26<br>
 * <br>
 * En effet, le matchage d'une URI par rapport à un mapping de filtre
 * est à la charge du serveur d'application. C'est pourquoi les implémentations
 * de ce mécanisme se trouvent dans le code source des serveurs d'appli.<br>
 * <br>
 * <i><u>Voici quelques ressources Internet concernant les filtres :</u></i><br>
 * <br>
 * &nbsp;&nbsp;http://www.oracle.com/technetwork/java/filters-137243.html<br>
 * &nbsp;&nbsp;http://www2.roguewave.com/support/docs/leif/leif/html/bobcatug/7-3.html<br>
 * &nbsp;&nbsp;http://java.sun.com/blueprints/corej2eepatterns/Patterns/InterceptingFilter.html<br>
 * &nbsp;&nbsp;http://www.javafaq.nu/java-example-code-1032.html<br>
 * &nbsp;&nbsp;http://www.orionserver.com/tutorials/filters/3.html<br>
 * <br>
 * <b><u><i>Algorithme :</i></u></b><br>
 * <br>
 * Soit urlMapping la définition du mapping (exemple : /images/*)<br>
 * Soit requestPath l'URI contextuel demandée (exemple : /images/img1.gif)<br>
 * <ul>
 *   <li>si urlMapping est null => false</li>
 *   <li>si requestPath est null => false</li>
 *   <li>si urlMapping est égal à requestPath => true</li>
 *   <li>si urlMapping est égal à "*" => true</li>
 *   <li>si urlMapping est égal à "/*" => true</li>
 *   <li>
 *      si urlMapping se termine par "/*"<br>
 *      <ul>
 *         <li>
 *            si requestPath est égal à ce qui précède le /* de urlMapping => true<br>
 *            (exemple : /images/* et /images/img1.gif)
 *         </li>
 *         <li>
 *            si requestPath correspond à un "sous-élément" de urlMapping => true<br>
 *            (exemple : /images/* et /images/contour/img1.gif)
 *         </li>
 *         <li>sinon, => false</li>
 *      </ul>
 *    </li>
 *    <li>
 *    si urlMapping est une extension de fichiers, et que la ressource demandée
 *    dans requestPath a la même extension => true<br>
 *    (exemple : *.html et /infos/info1.html)
 *    </li>
 *    <li>sinon, => false</li>
 * </ul>
*/
public final class UrlPatternMatcher {

   
   private UrlPatternMatcher() {
      
   }
   
   
   /**
     * Match une URI avec un pattern<br>
     * <br>
     * Voir les commentaires sur la classe ({@link UrlPatternMatcher}) pour plus d'information
     * 
     * @param urlMapping le pattern
     * @param requestPath le chemin relatif au contexte de la request
     * @return true s'il y a match, false sinon
     * @throws  PatternSyntaxException
     *         si urlMapping est une expression régulière invalide
     */
   protected static boolean match(
         String urlMapping, 
         String requestPath) {

      Boolean result;

      // Si l'URI est null
      if (requestPath == null) {
         result = false;
      }

      // Si le pattern est null
      else if (urlMapping == null) {
         result = false;
      } 

      // Si le pattern est égal à *
      // else if (urlMapping.equals("*")) {
      else if ("*".equals(urlMapping)) {
         result = true;
      }

      // Si le pattern est égal à /*
      else if ("/*".equals(urlMapping)) {
         result = true;
      }

      // Si le pattern est égal à l'URI
      else if (urlMapping.equals(requestPath)) {
         result = true;
      }

      // Si le pattern se termine par /*
      else if (urlMapping.endsWith("/*")) {
         result = matchCasFinitParSlashEtoile(urlMapping,requestPath);
      }
         
      // Si le pattern est une extension (se termine par *.)
      else if (urlMapping.startsWith("*.")) {
         result = matchCasExtension(urlMapping,requestPath);
      }

      // Tous les autres cas on vérifie si il s'agit d'une expression régulière
      //
      else {
         Pattern regex = Pattern.compile(urlMapping);
         result = regex.matcher(requestPath).find();
      }

      // Renvoie du résultat
      return result;

   }
   
   
   private static boolean matchCasFinitParSlashEtoile(
         String urlMapping, 
         String requestPath) {
      
      // Exemples :
      //  Pattern       URI
      //  /images/*     /images/img1.gif
      //  /images/*     /images/contour/img1.gif
      //  /images/*     /images2/contour/img1.gif
      //  /images/*     /img1.gif
      
      Boolean result;

      Boolean bCommencePareil = urlMapping.regionMatches(
            0, 
            requestPath, 
            0, 
            urlMapping.length() - 2);

      if (bCommencePareil) {
         if (requestPath.length() == (urlMapping.length() - 2)) {
            // Exemple: /images/* et /images/img1.gif
            result = true;
         } else if ('/' == requestPath.charAt(urlMapping.length() - 2)) {
            // Exemple: /images/* et /images/contour/img1.gif
            result = true;
         } else {
            // Exemple: /images/* et /images2/contour/img1.gif
            result = false;
         }
      }
      else {
         // Exemple: /images/* et /img1.gif
         result = false;
      }
      
      return result;
      
   }
   
   
   private static boolean matchCasExtension(
         String urlMapping, 
         String requestPath) {
      
      // Le mapping commence par *.
      
      Boolean result;
      
      int slash = requestPath.lastIndexOf('/');
      int period = requestPath.lastIndexOf('.');
      if ((slash >= 0) // présence d'un slash 
            && (period > slash)  // présence d'un point, et il se trouve après le slash
            && (period != requestPath.length() - 1) // le point n'est pas le dernier caractère
            && ((requestPath.length() - period) == (urlMapping.length() - 1))) {
         result = urlMapping.regionMatches(
               2, 
               requestPath, 
               period + 1,
               urlMapping.length() - 2);
      } else {
         result = false;
      }
      
      return result;
      
   }
   
   /**
     * Match l'URI avec les patterns.
     * 
     * @param urlPatterns les patterns
     * @param requestPath l'URI
     * @return true si l'URI match avec au moins 1 des patterns, false sinon
     */
   public static boolean matchOne(String[] urlPatterns, String requestPath)
   {
      
      Boolean result = false;
      
      if ((urlPatterns!=null) && (urlPatterns.length>0))
      {
         for(String urlPattern:urlPatterns)
         {
            if (match(urlPattern, requestPath)) {
               result = true;
               break;
            }
         }
      }
      
      return result;
      
   }
    
    
   /**
     * Match l'URI avec les patterns.
     * 
     * @param urlPatterns les patterns
     * @param requestPath l'URI
     * @return true si l'URI match avec tous les patterns, false sinon
     */
   public static boolean matchAll(String[] urlPatterns, String requestPath)
   {
      
      Boolean result = false;
      
      if ((urlPatterns!=null) && (urlPatterns.length>0))
      {
         result = true;
         for(String urlPattern:urlPatterns)
         {
            if (!match(urlPattern, requestPath)) {
               result = false;
               break;
            }
         }
      }
      
      return result;
      
   }	
}
