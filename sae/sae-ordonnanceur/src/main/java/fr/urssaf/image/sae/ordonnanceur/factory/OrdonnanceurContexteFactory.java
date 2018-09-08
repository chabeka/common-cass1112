package fr.urssaf.image.sae.ordonnanceur.factory;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;

/**
 * Classe de chargement du contexte de Spring
 * 
 * 
 */
public final class OrdonnanceurContexteFactory {

   private OrdonnanceurContexteFactory() {

   }

   /**
    * Cette méthode permet d'instancier des objets {@link ApplicationContext} à
    * partir de :
    * <ul>
    * <li>fichier de type ApplicationContext.xml</li>
    * <li>chemin pour un fichier dynamique de configuration</li>
    * </ul>
    * Ici le fichier de configuration du contexte doit contenir des références
    * vers le bean du fichier dynamque de configuration avec la balise : <br>
    * <br>
    * <code>&lt;ref bean="saeConfigResource" /></code> <br>
    * <br>
    * 
    * @param contextConfiguration
    *           fichier de configuration du contexte
    * 
    * @param saeConfiguration
    *           chemin complet du fichier de configuration générale du SAE
    * @return contexte d'application
    */
   public static ApplicationContext creerContext(String contextConfiguration,
         String saeConfiguration) {

      GenericApplicationContext genericContext = new GenericApplicationContext();
      BeanDefinitionBuilder saeConfigBean = BeanDefinitionBuilder
            .genericBeanDefinition(FileSystemResource.class);
      saeConfigBean.addConstructorArgValue(saeConfiguration);

      genericContext.registerBeanDefinition("saeConfigResource", saeConfigBean
            .getBeanDefinition());
      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfiguration }, genericContext);

      return context;
   }
}
