package fr.urssaf.image.commons.springsecurity.service;

import javax.annotation.security.RolesAllowed;

import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

public interface SimpleService {

   @RolesAllowed("ROLE_ADMIN")
   void save(Modele modele);

   @RolesAllowed("ROLE_USER")
   Modele load();
}
