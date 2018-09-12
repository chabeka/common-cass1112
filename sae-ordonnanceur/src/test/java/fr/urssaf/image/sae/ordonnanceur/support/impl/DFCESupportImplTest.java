package fr.urssaf.image.sae.ordonnanceur.support.impl;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.urssaf.image.sae.ordonnanceur.support.DFCESupport;
import junit.framework.Assert;

@SuppressWarnings("PMD.MethodNamingConventions")
public class DFCESupportImplTest {

   @Test
   public void isDfceUp_success() {

      final ApplicationContext context = new ClassPathXmlApplicationContext(
                                                                            new String[] { "/applicationContext-sae-ordonnanceur-dfce-test.xml" });

      final DFCESupport dfceSupport = context.getBean(DFCESupport.class);

      Assert.assertTrue("DFCE doit être Up!", dfceSupport.isDfceUp());
   }


   @Test
   public void isDfceUp_failure() {

      final ApplicationContext context = new ClassPathXmlApplicationContext(
                                                                            new String[] { "/applicationContext-sae-ordonnanceur-dfce-failure-test.xml" });

      final DFCESupport dfceSupport = context.getBean(DFCESupport.class);

      Assert.assertFalse("DFCE ne doit pas être Up!", dfceSupport.isDfceUp());
   }

}
