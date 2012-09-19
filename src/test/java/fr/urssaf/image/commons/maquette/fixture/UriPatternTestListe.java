package fr.urssaf.image.commons.maquette.fixture;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitaire pour les tests unitaires<br>
 * <br>
 * Renvoie une liste de tests pour le matchage des pattern d'URI
 *
 */
@SuppressWarnings("PMD")
public final class UriPatternTestListe {

   
   private UriPatternTestListe() {
      
   }
   
   
   /**
    * Renvoie une liste de tests pour le matchage de pattern d'URI 
    * @return liste de tests pour le matchage de pattern d'URI
    */
   public static List<UriPatternTestItem> getListeTests() {
      
      // NB : NE SURTOUT PAS ENLEVER DE TESTS
      //      NE SURTOUT PAS CHANGER L'ORDRE DES TESTS
      
      // Création de la liste résultat
      ArrayList<UriPatternTestItem> liste = new ArrayList<UriPatternTestItem>();
      
      // Ajout d'une 1ère batterie de tests      
      liste.add(new UriPatternTestItem("/status/*", "/status/synopsis", true));                 // 0
      liste.add(new UriPatternTestItem("/status/*", "/status/complete?date=today", true));      // 1
      liste.add(new UriPatternTestItem("/status/*", "/status", true));                          // 2
      liste.add(new UriPatternTestItem("/status/*", "/server/status", false));                  // 3
      liste.add(new UriPatternTestItem("*.map", "/US/Oregon/Portland.map", true));              // 4
      liste.add(new UriPatternTestItem("*.map", "/US/Washington/Seattle.map", true));           // 5
      liste.add(new UriPatternTestItem("*.map", "/Paris.France.map", true));                    // 6
      liste.add(new UriPatternTestItem("*.map", "/US/Oregon/Portland.MAP", false));             // 7
      liste.add(new UriPatternTestItem("*.map", "/interface/description/mail.mapi", false));    // 8
      
      // Des tests inspirés de http://www.caucho.com/resin-3.0/servlet/servlet.xtp#url-pattern
      liste.add(new UriPatternTestItem("/foo/bar.html", "/foo/bar.html", true));                // 9
      liste.add(new UriPatternTestItem("/foo/bar.html", "/application-name/foo/bar.html", true)); // 10
      liste.add(new UriPatternTestItem("/foo/*", "/foo", true));                                // 11
      liste.add(new UriPatternTestItem("/foo/*", "/foo/", true));                               // 12
      liste.add(new UriPatternTestItem("/foo/*", "/foo/bar.html", true));                       // 13
      liste.add(new UriPatternTestItem("*.foo", "/application-name/toto.foo", true));           // 14
      liste.add(new UriPatternTestItem("*.foo", "/application-name/toto.FOO", false));          // 15
      
      // Quelques tests supplémentaires
      liste.add(new UriPatternTestItem("/*", "/foo", true));                                    // 16
      liste.add(new UriPatternTestItem("toto", "/foo", false));                                 // 17
      liste.add(new UriPatternTestItem("*", "/foo", true));                                     // 18
      liste.add(new UriPatternTestItem("*", null, false));                                      // 19
      liste.add(new UriPatternTestItem(null, "/foo", false));                                   // 20
      liste.add(new UriPatternTestItem("/images/*", "/images/img1.gif", true));                 // 21 
      liste.add(new UriPatternTestItem("/images/*", "/images/contour/img1.gif", true));         // 22
      liste.add(new UriPatternTestItem("/images/*", "/images2/contour/img1.gif", false));       // 23
      liste.add(new UriPatternTestItem("*.html", "/page1.html", true));                         // 24
      liste.add(new UriPatternTestItem("*.html", "/site3/page1.html", true));                   // 25
      liste.add(new UriPatternTestItem("*.html", "page1html", false));                          // 26
      liste.add(new UriPatternTestItem("*.html", "html.page1", false));                         // 27
      liste.add(new UriPatternTestItem("*.html", "/page1html", false));                         // 28
      liste.add(new UriPatternTestItem("*.html", "/html.page1", false));                        // 29
      liste.add(new UriPatternTestItem("*.html", "page1.html", false)); // cas bizarre          // 30
      
      // Renvoie la liste
      return liste;
      
   }
   
}
