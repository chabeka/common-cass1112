package fr.urssaf.image.sae.commons.bo;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests unitaires de la classe ParameterRowType
 * 
 */
public class ParameterRowTypeTest {

   @Test
   public void tracabiliteTest() {

      Assert
            .assertEquals(
                  "La méthode toString() du type de paramètre \"Traçabilité\" renvoie une valeur incorrecte",
                  "parametresTracabilite", ParameterRowType.TRACABILITE
                        .toString());

   }

   @Test
   public void rndTest() {

      Assert
            .assertEquals(
                  "La méthode toString() du type de paramètre \"RND\" renvoie une valeur incorrecte",
                  "RND", ParameterRowType.RND.toString());

   }

}
