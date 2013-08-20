package fr.urssaf.image.sae.regionalisation.factory;

import java.io.File;

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
    * @param dfceConfig
    *           fichier de configuration de connexion à DFCE
    * @param dirPath
    *           chemin du répertoire où seront créés les fichiers de suivi
    * @return contexte d'application
    */
   public static ClassPathXmlApplicationContext load(String contextConfig,
         String dfceConfig, String dirPath) {

      GenericApplicationContext genericContext = new GenericApplicationContext();

      BeanDefinitionBuilder configFile = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      configFile.addConstructorArgValue(dfceConfig);

      genericContext.registerBeanDefinition("saeConfigResource", configFile
            .getBeanDefinition());

      BeanDefinitionBuilder parentFile = BeanDefinitionBuilder
            .genericBeanDefinition(File.class);
      parentFile.addConstructorArgValue(dirPath);
      genericContext.registerBeanDefinition("repository", parentFile
            .getBeanDefinition());

      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig }, genericContext);

      return context;

   }
}
