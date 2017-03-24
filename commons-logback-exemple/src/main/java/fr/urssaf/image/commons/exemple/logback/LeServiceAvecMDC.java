package fr.urssaf.image.commons.exemple.logback;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;


/**
 * Une classe de service
 */
public final class LeServiceAvecMDC {

   
   private static final Logger LOG = LoggerFactory.getLogger(LeServiceAvecMDC.class);
   
   
   /**
    * Constructeur
    */
   public LeServiceAvecMDC() {
      
      // On met un UUID généré aléatoirement dans le MDC
      // avec comme clé "idCinematique"
      MDC.put("idCinematique", UUID.randomUUID().toString());
      
   }
   
   
   /**
    * Une méthode de service
    * 
    * @return 0
    */
   public int methode1() {
      
      LOG.trace("Une trace de niveau TRACE");
      LOG.debug("Une trace de niveau DEBUG");
      LOG.info("Une trace de niveau INFO");
      LOG.warn("Une trace de niveau WARN");
      LOG.error("Une trace de niveau ERROR");
      
      return 0;
      
   }
   
   
   /**
    * Une méthode de service
    * 
    * @return 0
    */
   public int methode2() {
      
      LOG.trace("Une trace de niveau TRACE");
      LOG.debug("Une trace de niveau DEBUG");
      LOG.info("Une trace de niveau INFO");
      LOG.warn("Une trace de niveau WARN");
      LOG.error("Une trace de niveau ERROR");
      
      return 0;
      
   }
   
}
