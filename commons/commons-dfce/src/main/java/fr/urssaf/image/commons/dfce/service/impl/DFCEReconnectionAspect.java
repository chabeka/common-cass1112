package fr.urssaf.image.commons.dfce.service.impl;

import java.io.InputStream;

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
         LOG.debug("{} - Appel DFCE : {}",
                   new Object[] {LOG_PREFIX, joinPoint.getSignature()});

         // Si jamais un des paramètres est un inputStream, on sauvegarde la position du stream
         // afin de pouvoir retenter la méthode en cas d'exception
         LOG.debug("{} - Analyse des paramètres", new Object[] {LOG_PREFIX});
         final Object[] args = joinPoint.getArgs();
         for (final Object arg : args) {
            if (arg instanceof InputStream) {
               final InputStream inputStream = (InputStream) arg;
               if (!inputStream.markSupported()) {
                  final Class<? extends Object> theClass = arg.getClass();
                  LOG.warn("{} - Attention : l'inputstream de class {} ne supporte pas le retour arrière. Le rejeu ne fonctionnera pas en cas de reconnexion !",
                           new Object[] {LOG_PREFIX, theClass});
               }
               inputStream.mark(5*1024);
            }
         }

         // On tente d'exécuter le service DFCE
         proceed = joinPoint.proceed();
      }
      catch (final HessianConnectionException | NullPointerException ex) {
         LOG.warn("{} - Tentative d'établisssement d'une nouvelle connexion à DFCE suite à l'exception suivante reçue",
                  new Object[] {LOG_PREFIX}, ex);
         // On se reconnecte
         dfceServices.reconnect();
         // On remet les inputStream à leur position d'origine
         final Object[] args = joinPoint.getArgs();
         for (final Object arg : args) {
            if (arg instanceof InputStream) {
               final InputStream inputStream = (InputStream) arg;
               LOG.debug("{} - reset de l'inputstream...", new Object[] {LOG_PREFIX});
               inputStream.reset();
            }
         }
         // On retente l'exécution de la méthode
         proceed = joinPoint.proceed();
      }
      return proceed;
   }
}
