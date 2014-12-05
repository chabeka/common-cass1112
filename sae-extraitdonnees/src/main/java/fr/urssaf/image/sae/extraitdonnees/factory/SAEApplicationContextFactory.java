package fr.urssaf.image.sae.extraitdonnees.factory;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Classe d'instanciation de {@link ClassPathXmlApplicationContext} pour les
 * services nécessitant un contexte SPRING
 * 
 * 
 */
public final class SAEApplicationContextFactory {

   private SAEApplicationContextFactory() {

   }

   /**
    * Réalise le chargement du fichier de contexte SPRING
    * 
    * @param contextConfig
    *           fichier de configuration du contexte
    * @return contexte d'application
    */
   public static ClassPathXmlApplicationContext load(String contextConfig) {

      GenericApplicationContext genericContext = new GenericApplicationContext();

      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig }, genericContext);

      return context;

   }
}
