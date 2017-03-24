package fr.urssaf.image.commons.springsecurity.acl.dao;

import java.util.List;

import fr.urssaf.image.commons.springsecurity.acl.model.Person;

public interface PersonDAO {

   @SuppressWarnings("PMD.ShortVariable")
   Person find(int id);

   List<Person> find();
   
}
