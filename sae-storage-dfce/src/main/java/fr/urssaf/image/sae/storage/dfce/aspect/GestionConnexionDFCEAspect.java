package fr.urssaf.image.sae.storage.dfce.aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import fr.urssaf.image.commons.dfce.exception.DFCEConnectionServiceException;
import fr.urssaf.image.sae.storage.dfce.manager.DFCEServicesManager;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.StorageException;

/**
 * Aspect permettant de gérer la connection à DFCe.<br>
 * <br>
 * 
 */
@Component
public class GestionConnexionDFCEAspect {

   /**
    * LOGGER
    */
   private static final Logger LOG = LoggerFactory
         .getLogger(GestionConnexionDFCEAspect.class);

   /**
    * Manager de service DFCe
    */
   private DFCEServicesManager dfceServicesManager;

   /**
    * Classe precedente permettant d'initialis le manager.
    */
   private Class<?> targetClassPrecedent;


   /**
    * Méthode permettant de gerer la connexion DFCE
    * 
    * @param jp
    *           JoinPonit processing
    * @throws Throwable
    *            Exception
    */
   public final Object gererConnexionDFCE(ProceedingJoinPoint jp)
         throws Throwable {
      String LOG_PREFIX = "gererConnexionDFCE";
      LOG.debug("{} - Gestion de connection aux services DFCE",
            new Object[] { LOG_PREFIX });
      Object objReturn = null;

      try {
         objReturn = jp.proceed();
      } catch (StorageException ex) {
         Class<?> classe = jp.getSignature().getDeclaringType();
         jp.getTarget();
         if (dfceServicesManager != null
               && dfceServicesManager.getDFCEService() != null
               && dfceServicesManager.getDFCEService().isSessionActive()) {
            objReturn = jp.proceed();
         } else {
            int tentativecnx = dfceServicesManager.getCnxParameters()
                  .getNbtentativecnx();
            objReturn = openConnectionDFCe(jp, ex, tentativecnx, tentativecnx);
         }
      } catch (DFCEConnectionServiceException ex) {
         int tentativecnx = dfceServicesManager.getCnxParameters()
               .getNbtentativecnx();
         objReturn = openConnectionDFCe(jp, ex, tentativecnx, tentativecnx);
      } catch (Throwable ex) {
         throw ex;
      }

      // StorageApplicationContext.getApplicationContext().getBean(
      // "messageSource_sae_storage_dfce", DFCEServicesManager.class);

      LOG.debug("{} - Fin contrôle de connection aux services DFCE",
            new Object[] { LOG_PREFIX });

      return objReturn;
   }

   /**
    * 
    * Setter du manager de connexion à DFCE.
    * 
    * @param jp
    *           JoinPonit processing
    * @throws InvocationTargetException
    * @throws IllegalAccessException
    * @throws IllegalArgumentException
    */
   public final void setConnexionManagerDFCE(JoinPoint jp) {
      String LOG_PREFIX = "setConnexionManagerDFCE";
      if (jp != null) {
         Class<?> aClass = jp.getTarget().getClass();

         if (dfceServicesManager != null && targetClassPrecedent.equals(aClass)) {
            return;
         }

         Method[] methods = aClass.getMethods();
         for (Method method : methods) {
            if (isGetterDfceServicesManager(method)) {
               Object retour = null;
               try {
                  if (Modifier.isStatic(method.getModifiers())) {
                     retour = method.invoke(null, new Object[] {});

                  } else {
                     retour = method.invoke(jp.getTarget());
                  }
               } catch (IllegalArgumentException e) {
               } catch (IllegalAccessException e) {
               } catch (InvocationTargetException e) {
               }

               if (retour != null && retour instanceof DFCEServicesManager) {
                  dfceServicesManager = (DFCEServicesManager) retour;
                  break;
               }
               System.out.println("  getter : " + method);

            }

         }

         if (dfceServicesManager == null) {
            throw new DFCEConnectionServiceException(
                  LOG_PREFIX
                  + " - Le manager de connexion à DFCe n'a pas été trouvé. Le processus de connexion est en échec");
         }

         targetClassPrecedent = aClass;
         LOG.debug("{} - Le manager de connexion à DFCe est initialisé",
               new Object[] { LOG_PREFIX });
      }
   }

   /**
    * 
    * Methode permettant de retrouver la methode getDfceServicesManager dans la
    * classe de Service.
    * 
    * @param method
    *           Method à analyser.
    * @return True si la méthode passé en parametre est nommé
    *         getDfceServicesManager, false sinon.
    */
   private boolean isGetterDfceServicesManager(Method method) {
      boolean result = method.getName().startsWith("getDfceServicesManager")
            && (method.getParameterTypes().length == 0)
            && (!Void.class.equals(method.getReturnType()));
      return result;

   }

   /**
    * Méthode permettant de se connecter à DFCe.
    * 
    * @param jp
    *           JointPoint
    * @param ex
    *           Exception de connection à gerer
    * @param tentativecnxTotal
    *           Nombre total de tentative de connection
    * @param tentativecnxRestant
    *           Nombre restant de tentative de connection
    * @throws Throwable
    *            Exception de connection
    */
   private Object openConnectionDFCe(ProceedingJoinPoint jp, Throwable ex,
         int tentativecnxTotal, int tentativecnxRestant)
               throws Throwable {
      String LOG_PREFIX = "openConnectionDFCe";
      if (dfceServicesManager == null) {
         throw ex;
      }
      int step = (tentativecnxTotal + 1) - tentativecnxRestant;
      int tentativecnxNew = --tentativecnxRestant;
      try {
         if (tentativecnxNew > 0) {
            LOG.debug("{} - Tentative n°{}/{} de connexion à DFCE",
                  new Object[] { LOG_PREFIX, step, tentativecnxTotal });
            if (!dfceServicesManager.isActive()) {
               dfceServicesManager.openConnection();
               LOG.debug(
                     "{} - Réussite de la tentative n°{}/{} de connexion à DFCE ",
                     new Object[] { LOG_PREFIX, step, tentativecnxTotal });
               LOG.info("{} - Connection aux services DFCe réussi",
                     new Object[] { LOG_PREFIX });
               // On retente de lancer la methode en erreur uniquement lorsque
               // la connexion a pu être remise en place.
               return jp.proceed();
            } else {
               // Si la connexion est déjà active et que l'on a une exception
               // c'est soit un soucis autre, soit que le referencement n'est
               // pas mise à jour.
               LOG.info("{} - Connection aux services DFCe déjà active",
                     new Object[] { LOG_PREFIX });
            }
         } else {
            LOG.error(
                  "{} - Le nombre de tentative de connexion est atteint {}/{}",
                  new Object[] { LOG_PREFIX, tentativecnxTotal,
                        tentativecnxTotal });
         }
      } catch (ConnectionServiceEx connex) {
         LOG.debug("{} - Echec de la tentative n°{}/{} de connexion à DFCE ",
               new Object[] { LOG_PREFIX, step, tentativecnxTotal });
         if (tentativecnxNew > 0) {
            openConnectionDFCe(jp, connex, tentativecnxTotal, tentativecnxNew);
         }
         LOG.error("{} - Connection aux services DFCe à échoué", new Object[] { LOG_PREFIX });
         throw connex;
      } catch (StorageException storex) {
         LOG.debug("{} - Echec de la tentative n°{}/{} de connexion à DFCE ",
               new Object[] { LOG_PREFIX, step, tentativecnxTotal });
         if (tentativecnxNew > 0) {
            openConnectionDFCe(jp, storex, tentativecnxTotal, tentativecnxNew);
         }
         LOG.error("{} - Connection aux services DFCe à échoué",
               new Object[] { LOG_PREFIX });
         throw storex;
      }

      throw ex;
   }

}
