/**
 * 
 */
package fr.urssaf.image.sae.trace.executable.factory;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;

/**
 * Classe de chargement du contexte SPRING
 * 
 */
public final class TraceContextFactory {

   private TraceContextFactory() {
   }

   /**
    * Charge le contexte et le renvoie
    * 
    * @param contextConfiguration
    *           fichier de configuration du contexte
    * @param saeConfig
    *           fichier de configuration général
    * @return le contexte SPRING
    */
   public static ApplicationContext loadContext(String contextConfiguration,
         String saeConfig) {

      GenericApplicationContext genericContext = new GenericApplicationContext();

      BeanDefinitionBuilder saeConfigBean = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      saeConfigBean.addConstructorArgValue(saeConfig);

      genericContext.registerBeanDefinition("saeConfigResource", saeConfigBean
            .getBeanDefinition());

      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfiguration }, genericContext);

      return context;

   }
}
