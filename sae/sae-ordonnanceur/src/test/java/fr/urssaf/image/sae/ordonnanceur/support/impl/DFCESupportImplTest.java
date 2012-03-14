package fr.urssaf.image.sae.ordonnanceur.support.impl;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import fr.urssaf.image.commons.dfce.manager.DFCEConnectionParameter;
import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;

@SuppressWarnings("PMD.MethodNamingConventions")
public class DFCESupportImplTest {

   private static ApplicationContext creerContext(String contextConfiguration,
         String dfceConfiguration) {

      Properties dfceProperties = new Properties();
      dfceProperties.setProperty(DFCEConnectionParameter.DFCE_CONFIG,
            dfceConfiguration);

      GenericApplicationContext genericContext = new GenericApplicationContext();
      BeanDefinitionBuilder saeConfigBean = BeanDefinitionBuilder
            .genericBeanDefinition(Properties.class);
      saeConfigBean.addConstructorArgValue(dfceProperties);

      genericContext.registerBeanDefinition("saeConfigProperties",
            saeConfigBean.getBeanDefinition());
      genericContext.refresh();

      ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            new String[] { contextConfiguration }, genericContext);

      return context;
   }

   @Test
   public void isDfceUp_success() {

      ApplicationContext context = creerContext(
            "/applicationContext-sae-ordonnanceur-dfce-test.xml",
            "src/test/resources/config/dfce-config-test.properties");

      DFCESupport dfceSupport = context.getBean(DFCESupport.class);

      Assert.assertTrue("DFCE doit être Up!", dfceSupport.isDfceUp());
   }

   @Test
   public void isDfceUp_failure() {

      ApplicationContext context = creerContext(
            "/applicationContext-sae-ordonnanceur-dfce-test.xml",
            "src/test/resources/config/dfce-config-failure-test.properties");

      DFCESupport dfceSupport = context.getBean(DFCESupport.class);

      Assert.assertFalse("DFCE ne doit pas être Up!", dfceSupport.isDfceUp());
   }

}
