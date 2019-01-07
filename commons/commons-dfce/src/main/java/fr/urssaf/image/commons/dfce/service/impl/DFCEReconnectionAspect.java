package fr.urssaf.image.commons.dfce.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.caucho.hessian.client.HessianConnectionException;
import com.docubase.dfce.exception.runtime.DFCERuntimeException;

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

   /**
    * Nombre max de fois que l'on tente de jouer la méthode DFCE
    */
   private static final int MAX_RETRY_COUNT = 3;

   @Around("@annotation(AutoReconnectDfceServiceAnnotation)")
   public Object executeWithFailover(final ProceedingJoinPoint joinPoint) throws Throwable {
      final String LOG_PREFIX = "executeWithFailover";
      final DFCEServicesImpl dfceServices = (DFCEServicesImpl) joinPoint.getTarget();

      // On se connecte si on n'est pas encore connecté
      dfceServices.connectTheFistTime();

      LOG.debug("{} - Appel DFCE : {}", new Object[] {LOG_PREFIX, joinPoint.getSignature()});
      // Si jamais un des paramètres est un inputStream, on sauvegarde la position du stream
      // afin de pouvoir retenter la méthode en cas d'exception
      markInputStreams(joinPoint);

      Object proceed = null;

      // On tente plusieurs fois, pour gérer les exceptions "AlreadyLockedObjectException" qui peut nécessiter plusieurs secondes de temporisation
      for (int retryIndex = 0; retryIndex < MAX_RETRY_COUNT; retryIndex++) {
         try {
            // On tente d'exécuter le service DFCE
            proceed = joinPoint.proceed();
            // Si ok, on quitte
            return proceed;
         }
         catch (final HessianConnectionException | NullPointerException | IOException ex) {
            LOG.warn("{} - Tentative d'établisssement d'une nouvelle connexion à DFCE suite à l'exception suivante reçue",
                     new Object[] {LOG_PREFIX},
                     ex);
            // On se reconnecte
            dfceServices.reconnect();
            // On remet les inputStream à leur position d'origine
            resetInputStreams(joinPoint);
         }
         catch (final DFCERuntimeException ex) {
            // Gestion des exceptions "AlreadyLockedObjectException" uniquement
            if (ex.getMessage() != null && !ex.getMessage().contains("AlreadyLockedObjectException")) {
               throw (ex);
            }
            LOG.warn("{} - On retente l'appel à {} suite à la réception de l'exception AlreadyLockedObjectException sur la tentative n°{}",
                     new Object[] {LOG_PREFIX, joinPoint.getSignature(), retryIndex});

            // Temporisation d'une seconde
            Thread.sleep(1000);
            // On remet les inputStream à leur position d'origine
            resetInputStreams(joinPoint);
         }

      }
      // On retente une dernière fois sans catch, afin que l'exception arrive à l'appelant
      proceed = joinPoint.proceed();
      return proceed;
   }

   /**
    * Permet de sauvegarder la position des streams, afin de pouvoir rejouer la méthode en cas d'exception
    * 
    * @param joinPoint
    */
   private void markInputStreams(final ProceedingJoinPoint joinPoint) {
      final String LOG_PREFIX = "executeWithFailover";
      final Object[] args = joinPoint.getArgs();
      for (final Object arg : args) {
         if (arg instanceof InputStream) {
            final InputStream inputStream = (InputStream) arg;
            if (!inputStream.markSupported()) {
               final Class<? extends Object> theClass = arg.getClass();
               LOG.warn("{} - Attention : l'inputstream de class {} ne supporte pas le retour arrière. Le rejeu ne fonctionnera pas en cas de reconnexion !",
                        new Object[] {LOG_PREFIX, theClass});
            }
            inputStream.mark(5 * 1024);
         }
      }
   }

   /**
    * Restaure la position sauvegardée des inputStreams
    * 
    * @param joinPoint
    * @throws Exception
    */
   private void resetInputStreams(final ProceedingJoinPoint joinPoint) throws Exception {
      final String LOG_PREFIX = "executeWithFailover";
      // On remet les inputStream à leur position d'origine
      final Object[] args = joinPoint.getArgs();
      for (final Object arg : args) {
         if (arg instanceof InputStream) {
            final InputStream inputStream = (InputStream) arg;
            LOG.debug("{} - reset de l'inputstream...", new Object[] {LOG_PREFIX});
            inputStream.reset();
         }
      }
   }
}
