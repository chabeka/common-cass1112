package fr.urssaf.image.sae.vi.service;

import java.net.URI;

import fr.urssaf.image.sae.saml.util.ConverterUtils;

/**
 * Classe de constantes du jeton SAML 2.0
 * 
 *
 */
public final class VIConfiguration {
   
   private VIConfiguration(){
      
   }

   public static final URI METHOD_AUTH2 = ConverterUtils
         .uri("urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified");

}
