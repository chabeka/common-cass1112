package fr.urssaf.image.commons.springsecurity.service;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import fr.urssaf.image.commons.springsecurity.service.modele.Modele;

public interface OtherService {

   @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#modele, 'ROLE_AUTH')")
   void save(Modele modele);

   @PostAuthorize("hasRole('ROLE_ADMIN') or hasPermission(returnObject, 'ROLE_AUTH')")
   Modele load(int identifiant);
}
