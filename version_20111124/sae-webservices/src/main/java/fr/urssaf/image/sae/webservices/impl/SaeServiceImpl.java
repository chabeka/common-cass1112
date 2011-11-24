package fr.urssaf.image.sae.webservices.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.security.RolesAllowed;

import fr.urssaf.image.sae.webservices.SaeService;

/**
 * implémentation des services web du SAE
 * 
 * 
 */
@Service
public class SaeServiceImpl implements SaeService {

   private static final Logger LOG = LoggerFactory.getLogger(SaeServiceImpl.class);
   
   
   @Override
   public final String ping() {
      LOG.info("Consommation du service ping");
      return PING_MSG;
   }

   /**
    * roles autorisés: 'ROLE_TOUS'<br>
    * {@inheritDoc}
    */
   @RolesAllowed("ROLE_TOUS")
   @Override
   public final String pingSecure() {
      LOG.info("Consommation du service pingSecure");
      return PING_SECURE_MSG;
   }
}
