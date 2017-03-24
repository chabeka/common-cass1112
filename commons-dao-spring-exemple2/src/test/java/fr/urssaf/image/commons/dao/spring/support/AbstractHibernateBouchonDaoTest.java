package fr.urssaf.image.commons.dao.spring.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import fr.urssaf.image.commons.dao.spring.dao.fixture.Document;


/**
 * Tests unitaires de la classe {@link AbstractHibernateBouchonDao}
 *
 */
@SuppressWarnings("PMD")
public class AbstractHibernateBouchonDaoTest {

   
   private class ClasseBouchon extends AbstractHibernateBouchonDao<Document,Integer> {
      @Override
      protected Integer getId(Document obj) {
         return obj.getId();
      }

      @Override
      protected Comparator<Document> getComparator(String order, Boolean inverse) {
         return new DocumentComparator(order,inverse);          
      }
   }
   
   private class DocumentComparator implements Comparator<Document> {

      private String order;
      private Boolean inverse;
      
      public DocumentComparator(String order, Boolean inverse) {
         this.order = order;
         this.inverse = inverse;
      }
      
      @Override
      public int compare(Document o1, Document o2) {
         if (order.equals("id")) {
            int result = o1.getId().compareTo(o2.getId());
            if (inverse) {
               result *= -1;
            }
            return result;
         }
         else {
            throw new RuntimeException("Ordre de tri pas implémenté");
         }
      }
      
   }
   
   
   @Test
   public void testComplet() {
      
      // Variables
      int count;
      Document document;
      List<Document> documents;
      
      // Création de la classe bouchon
      ClasseBouchon bouchon = new ClasseBouchon();
      
      // Compte le nombre d'éléments
      count = bouchon.count();
      assertEquals("Le nombre d'éléments est incorrect",0,count);
      
      // Ajoute trois éléments
      Date date1 = new Date(); Date date2 = new Date(); Date date3 = new Date();
      bouchon.save(new Document(101,"titre bouchon 1",date1));
      bouchon.save(new Document(102,"titre bouchon 2",date2));
      bouchon.save(new Document(103,"titre bouchon 3",date3));
      
      // Compte le nombre d'éléments
      count = bouchon.count();
      assertEquals("Le nombre d'éléments est incorrect",3,count);
      
      // Vérifie un élément
      document = bouchon.get(102);
      assertEquals("Mauvais élément ramené",(Integer)102,document.getId());
      assertEquals("Mauvais élément ramené","titre bouchon 2",document.getTitre());
      assertEquals("Mauvais élément ramené",date2,document.getDate());
      
      // Met à jour un document
      document.setTitre("titre bouchon 2 maj");
      bouchon.update(document);
      
      // Compte le nombre d'éléments
      count = bouchon.count();
      assertEquals("Le nombre d'éléments est incorrect",3,count);
      
      // Vérifie que le document a été mis à jour
      document = bouchon.get(102);
      assertEquals("Mauvais élément ramené",(Integer)102,document.getId());
      assertEquals("Mauvais élément ramené","titre bouchon 2 maj",document.getTitre());
      assertEquals("Mauvais élément ramené",date2,document.getDate());
      
      // Suppression d'un document
      document = bouchon.get(101);
      bouchon.delete(document);
      
      // Compte le nombre d'éléments
      count = bouchon.count();
      assertEquals("Le nombre d'éléments est incorrect",2,count);
      
      // Vérifie que le document a été supprimé
      document = bouchon.get(101);
      assertNull("L'élément n'a pas été supprimé",document);
      
      // Ajoute 3 éléments, pour que l'on en ait 5
      Date date4 = new Date(); Date date5 = new Date(); Date date6 = new Date();
      bouchon.save(new Document(104,"titre bouchon 4",date4));
      bouchon.save(new Document(105,"titre bouchon 5",date5));
      bouchon.save(new Document(106,"titre bouchon 6",date6));
      
      // find() sur toutes les données, pas de tri
      documents = bouchon.find();
      assertNotNull("Les éléments n'ont pas été ramenés",documents);
      assertEquals("Le nombre d'éléments est incorrect",5,documents.size());
      
      // find() sur un identifiant unique
      document = bouchon.find(105);
      assertNotNull("L'élément n'a pas été retrouvé",document);
      assertEquals("L'élément ramené est incorrect",(Integer)105,document.getId());
      assertEquals("L'élément ramené est incorrect","titre bouchon 5",document.getTitre());
      assertEquals("L'élément ramené est incorrect",date5,document.getDate());
      
      // find() sur toutes les données, tri croissant
      documents = bouchon.find("id");
      assertNotNull("Les éléments n'ont pas été ramenés",documents);
      assertEquals("Le nombre d'éléments est incorrect",5,documents.size());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)102,documents.get(0).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)103,documents.get(1).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)104,documents.get(2).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)105,documents.get(3).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)106,documents.get(4).getId());
      
      // find() sur toutes les données, tri décroissant
      documents = bouchon.find("id",true);
      assertNotNull("Les éléments n'ont pas été ramenés",documents);
      assertEquals("Le nombre d'éléments est incorrect",5,documents.size());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)106,documents.get(0).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)105,documents.get(1).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)104,documents.get(2).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)103,documents.get(3).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)102,documents.get(4).getId());
      
      // find() sur une plage de données, tri décroissant
      documents = bouchon.find(1,3,"id",true);
      assertNotNull("Les éléments n'ont pas été ramenés",documents);
      assertEquals("Le nombre d'éléments est incorrect",3,documents.size());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)105,documents.get(0).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)104,documents.get(1).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)103,documents.get(2).getId());
      
      // find() sur une plage de données, tri décroissant, #2
      documents = bouchon.find(1,20,"id",true);
      assertNotNull("Les éléments n'ont pas été ramenés",documents);
      assertEquals("Le nombre d'éléments est incorrect",4,documents.size());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)105,documents.get(0).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)104,documents.get(1).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)103,documents.get(2).getId());
      assertEquals("Les éléments ne sont pas triés correctement",(Integer)102,documents.get(3).getId());
      
      // find() sur une plage de données, tri décroissant, #3
      documents = bouchon.find(15,2,"id",true);
      assertNotNull("Les éléments n'ont pas été ramenés",documents);
      assertEquals("Le nombre d'éléments est incorrect",0,documents.size());
      
      
      
   }
   
   
}
