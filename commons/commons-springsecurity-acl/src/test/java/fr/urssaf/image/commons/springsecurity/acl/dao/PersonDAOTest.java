package fr.urssaf.image.commons.springsecurity.acl.dao;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import fr.urssaf.image.commons.springsecurity.acl.model.Person;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/applicationContext-acl-test.xml")
public class PersonDAOTest {

   @Autowired
   private PersonDAO dao;

   @Test
   public void findById() {

      Person person = dao.find(1);
      assertPerson(person, "Clement", "Will", 1);
   }

   @Test
   public void find() {

      List<Person> persons = dao.find();

      assertPerson(persons.get(0), "Clement", "Will", 1);
      assertPerson(persons.get(1), "Wilkinson", "Bill", 2);
      assertPerson(persons.get(2), "Harris", "Charles", 3);
      assertPerson(persons.get(3), "Walls", "Chris", 4);
      assertPerson(persons.get(4), "Moore", "Tim", 5);
      assertPerson(persons.get(5), "Fisher", "Peter", 6);

      assertEquals("nombre inattendu de personnes", 6, persons.size());
   }

   private static void assertPerson(Person person, String lastname,
         String firstname, Integer identity) {

      assertEquals("lastname non attendu", lastname, person.getLastname());
      assertEquals("firstname non attendu", firstname, person.getFirstname());
      assertEquals("id non attendu", identity, person.getId());
   }

}
