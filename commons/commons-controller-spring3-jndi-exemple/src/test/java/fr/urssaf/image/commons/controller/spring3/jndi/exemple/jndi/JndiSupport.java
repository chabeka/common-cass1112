package fr.urssaf.image.commons.controller.spring3.jndi.exemple.jndi;

import java.sql.Driver;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

/**
 * Mise en place des ressources JNDI.<br>
 * <br>
 * Utilisation :<br>
 * <code>
 * &nbsp;&nbsp;&nbsp;JndiSupport.insertParametre1();<br>
 * &nbsp;&nbsp;&nbsp;JndiSupport.insertParametreN();<br>
 * </code>
 * <br>
 * Pour insérer toutes les ressources, on fait :<br>
 * <code>&nbsp;&nbsp;&nbsp;JndiSupport.insereTous();</code>
 */
public final class JndiSupport {

   private static SimpleNamingContextBuilder builder;
   
   /**
    * Constructeur privé
    */
   private JndiSupport() {
   }
   
   /**
    * Constructeur statique
    */
   static {
      try {
         builder = SimpleNamingContextBuilder.emptyActivatedContextBuilder();
         builder.activate();
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
   
   
   /**
    * Insère toutes les ressources
    */
   public static void insereTous()
   {
      insereTitre();
      insereDataSource();
   }
   
   
   /**
    * Vérifie si une ressource a déjà été insérée.<br>
    * Cela évite d'insérer plusieurs fois le même paramètre.
    * 
    * @param name le nom à rechercher
    */
   private static Boolean dejaInsere(String name)
   {
      Context context = null;
      try
      {
         context = new InitialContext();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
      try {
         context.lookup(name);
         return true;
      } catch (NamingException e) {
         return false;
      }
   }
   
   
   /**
    * Insertion du titre 
    */
   public static void insereTitre() {
      String name = "java:comp/env/title/default";
      if (dejaInsere(name))
         return;
      builder.bind(name, "Spring Web Exemple with JNDI");
   }
   
   
   /**
    * Insertion du Datasource
    */
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   public static void insereDataSource() {
      
      String name = "java:comp/env/jdbc/mysql";
      
      if (dejaInsere(name))
         return;
      
      try {

         // Lecture des paramètres JDBC depuis un fichier properties
         PropertiesConfiguration jdbc = new PropertiesConfiguration("jdbc.properties");
         String driverClassName = jdbc.getString("jdbc.driverClassName");
         String url = jdbc.getString("jdbc.url");
         String username = jdbc.getString("jdbc.username");
         String password = jdbc.getString("jdbc.password");

         // Instanciation des objets
         Driver driver = (Driver) BeanUtils.instantiate(
               Class.forName(driverClassName));
         DataSource dataSource = new SimpleDriverDataSource(
               driver, url, username, password);

         // Enregistrement JDNI
         builder.bind("java:comp/env/jdbc/mysql", dataSource);

      }
      catch (Exception e) {
         throw new RuntimeException(e);
      }
      
   }
   
}
