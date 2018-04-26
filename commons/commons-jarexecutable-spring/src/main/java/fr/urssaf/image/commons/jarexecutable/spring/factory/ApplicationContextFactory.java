package fr.urssaf.image.commons.jarexecutable.spring.factory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Classe d'instanciation de {@link ApplicationContext} pour les services
 * nécessitant un contexte SPRING
 * 
 * 
 */
public final class ApplicationContextFactory {

   private ApplicationContextFactory() {

   }

   /**
    * Cette méthode permet d'instancier des objets {@link ApplicationContext} à
    * partir de :
    * <ul>
    * <li>fichier de type ApplicationContext.xml</li>
    * </ul>
    * 
    * @param contextConfig
    *           fichier de configuration du contexte
    * 
    * @return contexte d'application
    */
   public static ApplicationContext createApplicationContext(
         String contextConfig) {

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig });

      return context;

   }
}
