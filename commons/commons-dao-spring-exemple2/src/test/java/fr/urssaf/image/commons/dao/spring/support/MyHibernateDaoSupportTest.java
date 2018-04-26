package fr.urssaf.image.commons.dao.spring.support;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.dao.spring.dao.fixture.Document;
import fr.urssaf.image.commons.dao.spring.dao.fixture.DocumentDao;


/**
 * Tests unitaires de la classe {@link MyHibernateDaoSupport}<br>
 * <br>
 * Les tests sont effectués en passant par la classe de fixture 
 * DocumentDaoImpl
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
@SuppressWarnings("PMD")
public class MyHibernateDaoSupportTest {

   
   @Autowired
   private DocumentDao documentDao;
   
   
   /**
    * Test unitaire de la méthode {@link MyHibernateDaoSupport#getTable()}
    */
   @Test
   public void getTable() {
      
      String actual = documentDao.testGetTable();
      String expected = "fr.urssaf.image.commons.dao.spring.dao.fixture.Document";
      
      assertEquals(
            "Erreur dans la méthode MyHibernateDaoSupport.getTable",
            expected,
            actual);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link MyHibernateDaoSupport#find(Criteria, int, int, String, boolean)}
    */
   @Test
   public void find_V1() {
      
      // Recherche
      int idGreaterOrEqualThen = 2;
      int firstResult = 1; // 2ème enregistrement
      int maxResults = 3;
      String champTri = "id";
      Boolean decroissant = true;
      List<Document> documents = documentDao.testFindAvecCriteria1(
            idGreaterOrEqualThen, 
            firstResult, 
            maxResults, 
            champTri, 
            decroissant);
      
      // Vérifie
      
      // On est censé récupérer 3 documents, triés par ordre décroissant d'identifiant
      // unique, en commençant par l'avant-dernier document
      
      assertNotNull(
            "Les documents n'ont pas été retrouvés",
            documents);
      
      assertEquals(
            "Le nombre de documents trouvés est incorrect",
            maxResults,
            documents.size());
      
      assertEquals(
            "Le résultat de la recherche est incorrect",
            (Integer)4,
            documents.get(0).getId());
      
      assertEquals(
            "Le résultat de la recherche est incorrect",
            (Integer)3,
            documents.get(1).getId());
      
      assertEquals(
            "Le résultat de la recherche est incorrect",
            (Integer)2,
            documents.get(2).getId());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link MyHibernateDaoSupport#find(org.hibernate.Criteria, String, boolean)}
    */
   @Test
   public void find_V2() {
      
      // Recherche
      int idGreaterOrEqualThen = 2;
      String champTri = "id";
      Boolean decroissant = true;
      List<Document> documents = documentDao.testFindAvecCriteria2(
            idGreaterOrEqualThen, 
            champTri, 
            decroissant);
      
      // Vérifie
      
      // On est censé récupérer les 4 premiers documents, trié par ordre
      // décroissant d'identifiant unique
      
      assertNotNull(
            "Les documents n'ont pas été retrouvés",
            documents);
      
      assertEquals(
            "Le nombre de documents trouvés est incorrect",
            4,
            documents.size());
      
      assertEquals(
            "Le résultat de la recherche est incorrect",
            (Integer)5,
            documents.get(0).getId());
      
      assertEquals(
            "Le résultat de la recherche est incorrect",
            (Integer)4,
            documents.get(1).getId());
      
      assertEquals(
            "Le résultat de la recherche est incorrect",
            (Integer)3,
            documents.get(2).getId());
      
      assertEquals(
            "Le résultat de la recherche est incorrect",
            (Integer)2,
            documents.get(3).getId());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link MyHibernateDaoSupport#find(org.hibernate.Criteria, int, int)}
    */
   @Test
   public void find_V3() {
      
      // Recherche
      int idGreaterOrEqualThen = 2;
      int firstResult = 1; // 2ème enregistrement
      int maxResults = 3;
      List<Document> documents = documentDao.testFindAvecCriteria3(
            idGreaterOrEqualThen, 
            firstResult, 
            maxResults);
      
      // Vérifie
      
      // On est censé récupérer 3 documents, à partir du 2ème dans le jeu de
      // résultat, avec un id unique >= 2
      // Aucun tri demandé
      
      
      assertNotNull(
            "Les documents n'ont pas été retrouvés",
            documents);
      
      assertEquals(
            "Le nombre de documents trouvés est incorrect",
            3,
            documents.size());
      
      // NB : comme il n'y a pas de tri demandé, on ne peut pas vérifier
      // que les documents ramenés soient ceux que l'on attendait : cela
      // dépend de l'implémentation du SGBDR pour le renvoi non trié des 
      // enregistrements
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link MyHibernateDaoSupport#find(org.hibernate.Criteria)}
    */
   @Test
   public void find_V4() {
      
      // Recherche
      int idGreaterOrEqualThen = 2;
      List<Document> documents = documentDao.testFindAvecCriteria4(
            idGreaterOrEqualThen);
      
      // Vérifie
      
      // On est censé récupérer 4 documents
      // Aucun tri demandé
      
      
      assertNotNull(
            "Les documents n'ont pas été retrouvés",
            documents);
      
      assertEquals(
            "Le nombre de documents trouvés est incorrect",
            4,
            documents.size());
      
      // NB : comme il n'y a pas de tri demandé, on ne peut pas vérifier
      // que les documents ramenés soient ceux que l'on attendait : cela
      // dépend de l'implémentation du SGBDR pour le renvoi non trié des 
      // enregistrements
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link MyHibernateDaoSupport#findByExample(Object)}
    */
   @Test
   public void findByExample() {
      
      // Création du document à recherche
      Document documentRecherche = new Document();
      documentRecherche.setTitre("titre 1");
      
      // Recherche
      List<Document> documents = documentDao.testFindByExample(documentRecherche);
      
      // Vérifie
      
      // On est censé récupérer le document avec l'id 1

      assertNotNull(
            "Le document n'a pas été retrouvé",
            documents);
      
      assertEquals(
            "Le nombre de documents trouvés est incorrect",
            1,
            documents.size());
      
      assertEquals(
            "Le document retrouvé n'est pas le bon",
            (Integer)1,
            documents.get(0).getId());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link MyHibernateDaoSupport#count(org.hibernate.Criteria)}
    */
   @Test
   public void countAvecCriteria() {
      
      int idGreaterOrEqualThen = 2;
      
      int count = documentDao.testCountAvecCriteria(idGreaterOrEqualThen);
      
      assertEquals(
            "Le nombre de documents est incorrect",
            (int)4,
            count);
      
   }
   
}

