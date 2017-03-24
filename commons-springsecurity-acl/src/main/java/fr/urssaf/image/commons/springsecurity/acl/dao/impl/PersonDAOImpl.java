package fr.urssaf.image.commons.springsecurity.acl.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import fr.urssaf.image.commons.springsecurity.acl.dao.PersonDAO;
import fr.urssaf.image.commons.springsecurity.acl.model.Person;

@Repository
public class PersonDAOImpl extends HibernateDaoSupport implements PersonDAO {

   @Autowired
   public PersonDAOImpl(
         @Qualifier("sessionFactory") SessionFactory sessionFactory) {
      super();
      this.setSessionFactory(sessionFactory);
   }

   @Override
   @SuppressWarnings("PMD.ShortVariable")
   public Person find(int id) {
      return (Person) this.getSession().get(Person.class, id);
   }

   @Override
   public List<Person> find() {

      Criteria criteria = this.getSession().createCriteria(Person.class);

      @SuppressWarnings("unchecked")
      List<Person> persons = criteria.list();

      return persons;
   }

}
