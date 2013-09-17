package fr.urssaf.image.sae.ordonnanceur.util;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Tests unitaires de la classe ListeUtils
 * 
 */
public class ListeUtilsTest {

   @Test
   public void nettoieListeSiBesoin_success() {

      List<URI> liste = new ArrayList<URI>();

      liste.add(URI.create("ecde://ecde01"));
      liste.add(URI.create("ecde://ecde02"));
      liste.add(URI.create("ecde://ecde03"));
      liste.add(URI.create("ecde://ecde04"));
      liste.add(URI.create("ecde://ecde05"));
      liste.add(URI.create("ecde://ecde06"));
      liste.add(URI.create("ecde://ecde07"));
      liste.add(URI.create("ecde://ecde08"));
      liste.add(URI.create("ecde://ecde09"));
      liste.add(URI.create("ecde://ecde10"));
      liste.add(URI.create("ecde://ecde11"));

      ListeUtils.nettoieListeSiBesoin(liste, 10);

      // La liste faisait 11 éléments au départ
      // Vu qu'on élémine la moitié des éléments, 
      // il doit en rester 5
      Assert
            .assertEquals(
                  "Le nombre d'éléments attendus dans la liste n'est pas celui attendu",
                  5, liste.size());

      // Les éléments qui restent doivent être les plus récents
      Assert.assertTrue("L'élément ecde://ecde07 aurait du être présent", liste
            .contains(URI.create("ecde://ecde07")));
      Assert.assertTrue("L'élément ecde://ecde08 aurait du être présent", liste
            .contains(URI.create("ecde://ecde08")));
      Assert.assertTrue("L'élément ecde://ecde09 aurait du être présent", liste
            .contains(URI.create("ecde://ecde09")));
      Assert.assertTrue("L'élément ecde://ecde10 aurait du être présent", liste
            .contains(URI.create("ecde://ecde10")));
      Assert.assertTrue("L'élément ecde://ecde11 aurait du être présent", liste
            .contains(URI.create("ecde://ecde11")));

   }

}
