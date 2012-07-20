package fr.urssaf.image.sae.regionalisation.factory;

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
    * @param postgresqlConfig
    *           fichier de configuration de connexion à POSTGRESQL
    * @return contexte d'application
    */
   public static ClassPathXmlApplicationContext load(String contextConfig,
         String dfceConfig, String postgresqlConfig) {

      GenericApplicationContext genericContext = new GenericApplicationContext();

      BeanDefinitionBuilder dfceConfigBean = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      dfceConfigBean.addConstructorArgValue(dfceConfig);

      genericContext.registerBeanDefinition("saeConfigResource", dfceConfigBean
            .getBeanDefinition());

      BeanDefinitionBuilder postgresqlConfigBean = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      postgresqlConfigBean.addConstructorArgValue(postgresqlConfig);

      genericContext.registerBeanDefinition("postgresqlConfigResource",
            postgresqlConfigBean.getBeanDefinition());

      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfig }, genericContext);

      return context;

   }
}
