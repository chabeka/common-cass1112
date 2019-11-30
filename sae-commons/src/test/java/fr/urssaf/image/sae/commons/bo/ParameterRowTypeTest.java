package fr.urssaf.image.sae.commons.bo;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Tests unitaires de la classe ParameterRowType
 * 
 */
public class ParameterRowTypeTest {

   @Test
   public void tracabiliteTest() {

      Assert.assertEquals(
            "La méthode toString() du type de paramètre \"Traçabilité\" renvoie une valeur incorrecte",
            "parametresTracabilite", ParameterRowType.TRACABILITE.toString());

   }

   @Test
   public void rndTest() {

      Assert.assertEquals(
            "La méthode toString() du type de paramètre \"RND\" renvoie une valeur incorrecte",
            "parametresRnd", ParameterRowType.parametresRnd.toString());

   }

   @Test
   public void corbeilleTest() {

      Assert.assertEquals(
            "La méthode toString() du type de paramètre \"Corbeille\" renvoie une valeur incorrecte",
            "parametresCorbeille", ParameterRowType.CORBEILLE.toString());

   }

}
