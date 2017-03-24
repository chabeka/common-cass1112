package fr.urssaf.image.commons.dao.spring.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.dao.spring.dao.fixture.Document;
import fr.urssaf.image.commons.dao.spring.dao.fixture.DocumentDao;


/**
 * Tests unitaires de la classe {@link EntityModifyDaoImpl} 
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-service.xml")
@SuppressWarnings("PMD")
public class EntityModifyDaoImplTest {

   
   @Autowired
   private DocumentDao documentDao;
   
   
   /**
    * Test unitaire de la méthode {@link EntityModifyDaoImpl#save(Object)}
    */
   @Test
   @Transactional
   public void save() {
      
      // Création d'un document
      
      Random random = new Random(1000000);
      int id = random.nextInt();
      
      String titre = "titre " + id;
      
      Document document = new Document();
      document.setId(id);
      document.setTitre(titre);
      document.setDate(new Date());
      
      // Sauvegarde
      documentDao.save(document);
      
      // Vérifie
      Document documentFind = documentDao.get(id);
      assertNotNull("Le document n'a pas été persisté",documentFind);
      assertEquals("Le document n'est pas le bon",titre,documentFind.getTitre());
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link EntityModifyDaoImpl#delete(Object)}
    */
   @Test
   @Transactional
   public void delete() {
      
      // Récupère le document à supprimer
      int docId = 1;
      Document document = documentDao.find(docId);
      assertNotNull("Le document que l'on veut supprimer n'est pas en base",document);
      
      // Supprime le document
      documentDao.delete(document);
      
      // Vérifie que le document n'est plus en base
      Document documentFind = documentDao.find(docId);
      assertNull("Le document n'a pas été supprimé",documentFind);
      
   }
   
   
   /**
    * Test unitaire de la méthode {@link EntityModifyDaoImpl#update(Object)}
    */
   @Test
   @Transactional
   public void update() {
      
      // Récupère le document à updater
      int docId = 1;
      Document document = documentDao.find(docId);
      assertNotNull("Le document que l'on veut supprimer n'est pas en base",document);
      
      // Modifier les propriétés du document
      String newTitre = "mis a jour";
      document.setTitre(newTitre);
      
      // Met à jour le document
      documentDao.update(document);
      
      // Vérifie que le document a été mis à jour
      Document documentFind = documentDao.find(docId);
      assertEquals("La mise à jour n'a pas été faite",newTitre,documentFind.getTitre());
      
   }
   
   
}
