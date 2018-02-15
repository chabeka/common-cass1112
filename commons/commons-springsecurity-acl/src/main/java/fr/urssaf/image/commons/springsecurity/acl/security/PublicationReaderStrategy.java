package fr.urssaf.image.commons.springsecurity.acl.security;

import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.springsecurity.acl.model.Publication;

@Component
public class PublicationReaderStrategy implements ObjectIdentityRetrievalStrategy {

   @Override
   public ObjectIdentity getObjectIdentity(Object domainObject) {
      ObjectIdentity identity;

      if (domainObject instanceof Publication) {
         identity = new ObjectIdentityImpl(domainObject);
      } else {
         throw new IllegalArgumentException(domainObject
               + " n'est pas prise en compte dans publication ");
      }

      return identity;
   }

}
