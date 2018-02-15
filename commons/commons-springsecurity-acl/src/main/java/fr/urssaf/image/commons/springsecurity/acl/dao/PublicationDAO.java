package fr.urssaf.image.commons.springsecurity.acl.dao;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import fr.urssaf.image.commons.springsecurity.acl.model.Publication;

public interface PublicationDAO {

   @RolesAllowed( { "ROLE_READER", "PUBLICATION_READ" })
   Publication find(int identity);

   //Publication findById(int identity);

   @RolesAllowed("ROLE_READER")
   List<Publication> find();

   @RolesAllowed("ROLE_READER")
   List<Publication> findByAuthor(int idAuthor);

   @RolesAllowed("PUBLICATION_AUTHOR")
   void save(Publication publication);

   @RolesAllowed( { "ROLE_EDITOR", "PUBLICATION_AUTHOR" })
   void update(Publication publication);

}
