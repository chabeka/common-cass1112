package fr.urssaf.image.sae.webservices.aspect;

import me.prettyprint.cassandra.utils.TimeUUIDUtils;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Aspect permettant de gérer le MDC pour les logs.
 * 
 *
 */

@Component
public class BuildOrClearMDCAspect {

   public static final String LOG_CONTEXTE = "log_contexte_uuid";
   
   /**
    * Mise en place du Log contexte.
    */
   public final void buildLogContext() {
      String contexteLog = TimeUUIDUtils.getUniqueTimeUUIDinMillis().toString();
      MDC.put(LOG_CONTEXTE, contexteLog);
   }

   /**
    * Nettoie le contexte pour le logback.
    */
   public static void clearLogContext() {
      MDC.clear();
   }
   
   
}
