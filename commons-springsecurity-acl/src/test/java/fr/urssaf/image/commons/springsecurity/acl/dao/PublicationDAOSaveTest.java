package fr.urssaf.image.commons.springsecurity.acl.dao;

import static fr.urssaf.image.commons.springsecurity.acl.dao.util.AssertPublication.assertPublication;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import fr.urssaf.image.commons.springsecurity.acl.dao.service.TestDAO;
import fr.urssaf.image.commons.springsecurity.acl.model.Person;
import fr.urssaf.image.commons.springsecurity.acl.model.Publication;
import fr.urssaf.image.commons.springsecurity.acl.security.AuthenticateUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext-acl-test.xml",
      "/applicationContext-security.xml" })
@SuppressWarnings("PMD.MethodNamingConventions")
public class PublicationDAOSaveTest {

   private static final String TEST_TITLE = "new publication";

   @Autowired
   private PublicationDAO dao;

   @Autowired
   private TestDAO<Publication, Integer> find;

   @After
   public void after() {
      AuthenticateUtils.logout();
   }

   @Test
   @Transactional
   public void save_success() {
      AuthenticateUtils.authenticate(2, "ROLE_AUTHOR");

      int identifiant = save(TEST_TITLE, 2);

      Publication publication = find.findById(identifiant);

      assertPublication(publication, identifiant, TEST_TITLE);
   }

   @Test(expected = AccessDeniedException.class)
   @Transactional
   public void save_failure_editor() {
      AuthenticateUtils.authenticateEditor();

      save(TEST_TITLE, 2);
   }

   @Test(expected = AccessDeniedException.class)
   @Transactional
   public void save_failure_author() {
      AuthenticateUtils.authenticate(1, "ROLE_AUTHOR");

      save(TEST_TITLE, 2);
   }

   private int save(String title, int idAuthor) {

      Person author = new Person();
      author.setId(idAuthor);
      Publication publication = new Publication(author, title);

      dao.save(publication);

      return publication.getId();
   }

}
