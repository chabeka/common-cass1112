package fr.urssaf.image.commons.exemple.logback;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LeServiceTest {

   @Test
   public void laMethodeTest() {
      
      LeService leService = new LeService();
      
      int retour = leService.laMethode();
      
      assertEquals(0,retour);
      
   }
   
   
   @Test
   public void laMethodeAvecTexteOptimiseTest() {
      
      LeService leService = new LeService();
      
      int somme = leService.laMethodeAvecTexteOptimise(2, 3);
      
      assertEquals(5,somme);
      
   }
   
}
