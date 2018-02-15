package fr.urssaf.image.commons.exemple.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Une classe de service
 */
public final class LeService {

   
   private static final Logger LOG = LoggerFactory.getLogger(LeService.class);
   
   
   /**
    * Une méthode de service
    * 
    * @return 0
    */
   public int laMethode() {
      
      LOG.trace("Une trace de niveau TRACE");
      LOG.debug("Une trace de niveau DEBUG");
      LOG.info("Une trace de niveau INFO");
      LOG.warn("Une trace de niveau WARN");
      LOG.error("Une trace de niveau ERROR");
      
      return 0;
      
   }
   
   
   public int laMethodeAvecTexteOptimise(int a, int b) {
      
      int somme = a + b;
      
      LOG.debug("Le résultat de l'addition est : " + somme);
      
      LOG.debug("Le résultat de l'addition est : {}",somme);
      
      
      return somme;
   }
   
}
