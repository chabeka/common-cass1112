package fr.urssaf.image.sae.regionalisation.fond.documentaire.factory;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;

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
    * @param cassandraConf
    *           fichier de configuration de connexion à CASSANDRA
    * @return contexte d'application
    */
   public static ClassPathXmlApplicationContext load(String contextConfig,
         String cassandraConf) {

      GenericApplicationContext genericContext = new GenericApplicationContext();

      BeanDefinitionBuilder configFile = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      configFile.addConstructorArgValue(cassandraConf);

      genericContext.registerBeanDefinition("cassandraFile", configFile
            .getBeanDefinition());

      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig }, genericContext);

      return context;

   }
}
