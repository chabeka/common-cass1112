package fr.urssaf.image.commons.dfce.service.impl;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianConnectionException;


/**
 * Permet de gérer par aspect la reconnexion DFCE pour méthodes annotées par
 * {@link AutoReconnectDfceServiceAnnotation}
 */
@Aspect
public class DFCEReconnectionAspect {

   /**
    * LOGGER
    */
   private static final Logger LOG = LoggerFactory.getLogger(DFCEReconnectionAspect.class);


   @Around("@annotation(AutoReconnectDfceServiceAnnotation)")
   public Object executeWithFailover(final ProceedingJoinPoint joinPoint) throws Throwable {
      final String LOG_PREFIX = "executeWithFailover";
      final DFCEServicesImpl dfceServices = (DFCEServicesImpl) joinPoint.getTarget();

      // On se connecte si on n'est pas encore connecté
      dfceServices.connectTheFistTime();

      Object proceed;
      try {
         // On tente d'exécuter les service DFCE
         LOG.debug("{} - Appel DFCE : {}",
                   new Object[] {LOG_PREFIX, joinPoint.getSignature()});
         proceed = joinPoint.proceed();
      }
      catch (final HessianConnectionException | NullPointerException ex) {
         LOG.warn("{} - Tentative d'établisssement d'une nouvelle connexion à DFCE suite à l'exception suivante reçue",
                  new Object[] {LOG_PREFIX}, ex);
         // On se reconnecte
         dfceServices.reconnect();
         // On retente l'exécution
         proceed = joinPoint.proceed();
      }
      return proceed;
   }
}
