package fr.urssaf.image.commons.springsecurity.acl.dao;

import static fr.urssaf.image.commons.springsecurity.acl.dao.util.AssertPublication.assertPublication;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.springsecurity.acl.model.Publication;
import fr.urssaf.image.commons.springsecurity.acl.security.AuthenticateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-acl-test.xml",
      "/applicationContext-security.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class PublicationDAOFindTest {

   @Autowired
   private PublicationDAO dao;

   @After
   public void after() {
      AuthenticateUtils.logout();
   }

   @Test
   public void findByIdentity_success() {
      AuthenticateUtils.authenticateReader();
      Publication publication = dao.find(1);

      assertPublication(publication, 1, "Semantique RDF");

   }

   @Test(expected = AccessDeniedException.class)
   public void findByIdentity_failure() {
      AuthenticateUtils.authenticateReader();
      dao.find(2);

   }

   @Test
   public void findByAuthor() {
      AuthenticateUtils.authenticateReader();
      List<Publication> publications = dao.findByAuthor(1);

      assertPublication(publications.get(0), 1, "Semantique RDF");
      assertPublication(publications.get(1), 2, "HTML5 pour les web designers");

      assertEquals("nombre inattendu de publications", 2, publications.size());

   }

   @Test
   public void find() {
      AuthenticateUtils.authenticateReader();
      List<Publication> publications = dao.find();

      assertPublication(publications.get(0), 1, "Semantique RDF");
      assertPublication(publications.get(1), 2, "HTML5 pour les web designers");
      assertPublication(publications.get(2), 3, "Formatage des donnees en PHP");

      assertEquals("nombre inattendu de publications", 3, publications.size());
   }

}
