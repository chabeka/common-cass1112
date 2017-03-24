package fr.urssaf.image.commons.util.collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import fr.urssaf.image.commons.util.collection.CollectionUtils.CollectionComparator;
import fr.urssaf.image.commons.util.exceptions.TestConstructeurPriveException;
import fr.urssaf.image.commons.util.tests.TestsUtils;


/**
 * Tests unitaires de la classe {@link CollectionUtils}
 */
@SuppressWarnings("PMD")
public class CollectionUtilsTest {

   
   /**
    * Test unitaire du constructeur privé, pour le code coverage
    */
   @Test
   public void constructeurPrive() throws TestConstructeurPriveException {
      Boolean result = TestsUtils.testConstructeurPriveSansArgument(CollectionUtils.class);
      assertTrue("Le constructeur privé n'a pas été trouvé",result);
   }
   
   
   /**
    * Test unitaire de la méthode {@link CollectionComparator#equals(java.util.Collection, java.util.Collection)}<br>
    * <br>
    * Cas de test : les deux collections sont null<br>
    * <br>
    * Résultat attendu : la comparaison doit renvoyer TRUE
    */
   @Test
   public void collectionComparatorEqualsTest1() {
      
      List<String> collection1 = null;
      List<String> collection2 = null;
      
      CollectionComparator<String> comparator = new CollectionComparator<String>();
      
      boolean sontEgales = comparator.equals(collection1, collection2);
      
      assertTrue("La comparaison entre deux collections est erronée",sontEgales);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link CollectionComparator#equals(java.util.Collection, java.util.Collection)}<br>
    * <br>
    * Cas de test : une des deux collections est null<br>
    * <br>
    * Résultat attendu : la comparaison doit renvoyer FALSE
    */
   @Test
   public void collectionComparatorEqualsTest2() {
      
      List<String> collection1 = new ArrayList<String>();
      List<String> collection2 = null;
      
      CollectionComparator<String> comparator = new CollectionComparator<String>();
      
      boolean sontEgales = comparator.equals(collection1, collection2);
      
      assertFalse("La comparaison entre deux collections est erronée",sontEgales);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link CollectionComparator#equals(java.util.Collection, java.util.Collection)}<br>
    * <br>
    * Cas de test : les deux collections ne contiennent pas le même nombre d'éléments<br>
    * <br>
    * Résultat attendu : la comparaison doit renvoyer FALSE
    */
   @Test
   public void collectionComparatorEqualsTest3() {
      
      List<String> collection1 = new ArrayList<String>();
      List<String> collection2 = new ArrayList<String>();
      
      collection1.add("Toto");
      
      collection2.add("Toto");
      collection2.add("Titi");
      
      CollectionComparator<String> comparator = new CollectionComparator<String>();
      
      boolean sontEgales = comparator.equals(collection1, collection2);
      
      assertFalse("La comparaison entre deux collections est erronée",sontEgales);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link CollectionComparator#equals(java.util.Collection, java.util.Collection)}<br>
    * <br>
    * Cas de test : les deux collections contiennent le même nombre d'éléments, 
    * mais pas les mêmes<br>
    * <br>
    * Résultat attendu : la comparaison doit renvoyer FALSE
    */
   @Test
   public void collectionComparatorEqualsTest4() {
      
      List<String> collection1 = new ArrayList<String>();
      List<String> collection2 = new ArrayList<String>();
      
      collection1.add("Toto");
      collection1.add("Tata");
      
      collection2.add("Toto");
      collection2.add("Titi");
      
      CollectionComparator<String> comparator = new CollectionComparator<String>();
      
      boolean sontEgales = comparator.equals(collection1, collection2);
      
      assertFalse("La comparaison entre deux collections est erronée",sontEgales);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link CollectionComparator#equals(java.util.Collection, java.util.Collection)}<br>
    * <br>
    * Cas de test : les deux collections contiennent les mêmes éléments (dans un ordre différent)<br> 
    * <br>
    * Résultat attendu : la comparaison doit renvoyer TRUE
    */
   @Test
   public void collectionComparatorEqualsTest5() {
      
      List<String> collection1 = new ArrayList<String>();
      List<String> collection2 = new ArrayList<String>();
      
      collection1.add("Toto");
      collection1.add("Tata");
      
      collection2.add("Tata");
      collection2.add("Toto");
      
      CollectionComparator<String> comparator = new CollectionComparator<String>();
      
      boolean sontEgales = comparator.equals(collection1, collection2);
      
      assertTrue("La comparaison entre deux collections est erronée",sontEgales);
      
   }
   
   
}
