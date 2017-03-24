package fr.urssaf.image.commons.controller.spring3.jndi.exemple.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContextTestJndiComplet.xml")
public class ParametersComponentAvecJndiComplet {

   @Test
   public void testTitre()
   {
      String titre = ParametersComponent.getTitle();
      String expected = "Spring Web Exemple with JNDI";
      assertEquals("Titre incorrect !",expected,titre);
   }

}
