package fr.urssaf.image.commons.exemple.logback;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LeServiceAvecMDCTest {

   @Test
   public void methodesTest() {
      
      LeServiceAvecMDC leService = new LeServiceAvecMDC();
      
      int retour = leService.methode1();
      assertEquals(0,retour);
      
      retour = leService.methode2();
      assertEquals(0,retour);
      
   }
   
}
