package fr.urssaf.image.sae.webservices.aspect;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.apache.axis2.context.MessageContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import fr.cirtil.www.saeservice.EcdeUrlSommaireType;
import fr.cirtil.www.saeservice.EcdeUrlType;
import fr.cirtil.www.saeservice.RequeteRechercheNbResType;
import fr.cirtil.www.saeservice.RequeteRechercheType;
import fr.cirtil.www.saeservice.UuidType;
import fr.urssaf.image.sae.commons.exception.ParameterNotFoundException;
import fr.urssaf.image.sae.commons.utils.SearchObjectClassUtil;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;
import fr.urssaf.image.sae.webservices.enumeration.MethodeService;

/**
 * Aspect permettant de logger le message SOAP de requete lorsqu'une exception
 * est levée lors de la consommation d'un webservice.<br>
 * <br>
 * Invoque également le dispatcheur de traces.
 * 
 */
@Aspect
public class LogSkeletonAspect {

   /**
    * Logger
    */
   private static final Logger LOG = LoggerFactory
         .getLogger(LogSkeletonAspect.class);

   /**
    * MESSAGE_LOG_ERROR_FOUND
    */
   private static final String MESSAGE_LOG_ERROR_FOUND = "Le paramètre recherché n'a pas été trouvé";

   /**
    * MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT
    */
   private static final String MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT = "Le paramètre recherché n'existe pas dans la requête";

   /**
    * Liste des service comprenant une URL ECDE en paramètre
    */
   private List<Class> listeServiceClassECDE = Arrays.asList(
         MethodeService.ARCHIVAGE_UNITAIRE_PJ.getClasseMethodeRequest(),
         MethodeService.ARCHIVAGE_UNITAIRE.getClasseMethodeRequest(),
         MethodeService.ARCHIVAGE_MASSE.getClasseMethodeRequest(),
         MethodeService.ARCHIVAGE_MASSE_HASH.getClasseMethodeRequest(),
         MethodeService.MODIFICATION_MASSE.getClasseMethodeRequest(),
         MethodeService.STOCKAGE_UNITAIRE.getClasseMethodeRequest(),
         MethodeService.TRANSFERT_MASSE.getClasseMethodeRequest());

   /**
    * Liste des service comprenant un UUID en paramètre
    */
   private List<Class> listeServiceClassID = Arrays.asList(
         MethodeService.CONSULTATION.getClasseMethodeRequest(),
         MethodeService.CONSULTATION_AFFICHABLE.getClasseMethodeRequest(),
         MethodeService.CONSULTATION_GNT_GNS.getClasseMethodeRequest(),
         MethodeService.CONSULTATION_MTOM.getClasseMethodeRequest(),
         MethodeService.MODIFICATION.getClasseMethodeRequest(),
         MethodeService.TRANSFERT.getClasseMethodeRequest(),
         MethodeService.SUPPRESSION.getClasseMethodeRequest(),
         MethodeService.AJOUT_NOTE.getClasseMethodeRequest(),
         MethodeService.GET_DOC_FORMAT_ORIG.getClasseMethodeRequest(),
         MethodeService.RESTORE_MASSE.getClasseMethodeRequest(),
         MethodeService.COPIE.getClasseMethodeRequest(),
         MethodeService.DOCUMENT_EXISTANT.getClasseMethodeRequest(),
         MethodeService.CONSULTATION_GNT_GNS.getClasseMethodeRequest(),
         MethodeService.DEBLOCAGE.getClasseMethodeRequest(),
         MethodeService.REPRISE.getClasseMethodeRequest());

   /**
    * Liste des service comprenant une requête en paramètre
    */
   private List<Class> listeServiceClassRequete = Arrays.asList(
         MethodeService.RECHERCHE.getClasseMethodeRequest(),
         MethodeService.SUPPRESSION_MASSE.getClasseMethodeRequest());
   
   /**
    * Liste des service comprenant une requête de recherche avec nb res en paramètre
    */
   private List<Class> listeServiceClassRequeteNbRes = Arrays.asList(
         MethodeService.RECHERCHE_NB_RES.getClasseMethodeRequest());

   /**
    * Méthode permettant de logger les informations des services du skeleton
    * 
    * @param exception
    *           l'exception levée
    */
   @Before(value = "execution(public * fr.urssaf.image.sae.webservices.skeleton.*Skeleton.*(..))")
   public final void logService(JoinPoint jp) {
      String logPrefixe = "LogSkeletonAspect.logService - ";

      for (Object arg : jp.getArgs()) {
         Class<?> jpClass = arg.getClass();
         try {
            if (listeServiceClassECDE.contains(jpClass)) {
               logServiceInformationServiceECDE(arg);
            } else if (listeServiceClassID.contains(jpClass)) {
               logServiceInformationServiceID(arg);
            } else if (listeServiceClassRequete.contains(jpClass)) {
               logServiceInformationServiceRequete(arg);
            } else if (listeServiceClassRequeteNbRes.contains(jpClass)) {
               logServiceInformationServiceRequeteNbRes(arg);
            } else {
               logServiceInformationServiceAutres(arg);
            }
         } catch (ParameterNotFoundException e) {
            LOG.warn(logPrefixe + e.getMessage());
            // En cas d'erreur, on logge les informations simple du service.
            try {
               logServiceInformationServiceAutres(arg);
            } catch (ParameterNotFoundException e1) {
               // Do nothing
            }
         }
         
         break;
      }

   }

   /**
    * Méthode permettant de logger les informations des services utilisant l'URL
    * ECDE comme parmètre
    * 
    * @param target
    *           Object cible contenant l'URL ECDE
    * @throws ParameterNotFoundException
    * @{@link ParameterNotFoundException}
    */
   private void logServiceInformationServiceECDE(Object target)
         throws ParameterNotFoundException {
      try {
         Object objRet = SearchObjectClassUtil.searchObjectByClass(target,
               EcdeUrlType.class.toString(),
               EcdeUrlSommaireType.class.toString());
         if (objRet instanceof EcdeUrlType) {
            EcdeUrlType url = (EcdeUrlType) objRet;
            if (url.getEcdeUrlType() != null) {
               LOG.info(getLogInformation(target, url.toString()));
            } else {
               throw new ParameterNotFoundException(
                     MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
            }
         } 
         else if (objRet instanceof EcdeUrlSommaireType) {
           EcdeUrlSommaireType url = (EcdeUrlSommaireType) objRet;
           if (url.getEcdeUrlSommaireType() != null) {
             LOG.info(getLogInformation(target, url.toString()));
          } else {
             throw new ParameterNotFoundException(
                   MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
          }
           
         } else {
            throw new ParameterNotFoundException(MESSAGE_LOG_ERROR_FOUND);
         }
      } catch (Exception e) {
         throw new ParameterNotFoundException(
               MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
      }
   }

   /**
    * Méthode permettant de logger les informations des services utilisant un
    * UUID comme paramètre
    * 
    * @param target
    *           Object cible contenant l'UUID
    * @throws ParameterNotFoundException
    * @{@link ParameterNotFoundException}
    */
   private void logServiceInformationServiceID(Object target)
         throws ParameterNotFoundException {
      try {
         Object objRet = SearchObjectClassUtil.searchObjectByClass(target,
               UuidType.class.toString());
         if (objRet instanceof UuidType) {
            UuidType uuid = (UuidType) objRet;
            if (uuid.getUuidType() != null) {
               LOG.info(getLogInformation(target, uuid.getUuidType().toString()));
            } else {
               throw new ParameterNotFoundException(
                     MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
            }
         } else {
            throw new ParameterNotFoundException(MESSAGE_LOG_ERROR_FOUND);
         }
      } catch (Exception e) {
         throw new ParameterNotFoundException(
               MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
      }
   }

   /**
    * Méthode permettant de logger les informations des services utilisant une
    * requête comme paramètre
    * 
    * @param target
    *           Object cible contenant la requête ECDE
    * @throws ParameterNotFoundException
    * @{@link ParameterNotFoundException}
    */
   private void logServiceInformationServiceRequete(Object target)
         throws ParameterNotFoundException {
      try {
         Object objRet = SearchObjectClassUtil.searchObjectByClass(target,
               RequeteRechercheType.class.toString());
         if (objRet instanceof RequeteRechercheType) {
            RequeteRechercheType req = (RequeteRechercheType) objRet;
            if (req.getRequeteRechercheType() != null) {
               LOG.info(getLogInformation(target, req.getRequeteRechercheType()
                     .toString()));
            } else {
               throw new ParameterNotFoundException(
                     MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
            }
         } else {
            throw new ParameterNotFoundException(MESSAGE_LOG_ERROR_FOUND);
         }
      } catch (Exception e) {
         throw new ParameterNotFoundException(
               MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
      }
   }
   
   /**
    * Méthode permettant de logger les informations des services utilisant une
    * requête comme paramètre
    * 
    * @param target
    *           Object cible contenant la requête ECDE
    * @throws ParameterNotFoundException
    * @{@link ParameterNotFoundException}
    */
   private void logServiceInformationServiceRequeteNbRes(Object target)
         throws ParameterNotFoundException {
      try {
         Object objRet = SearchObjectClassUtil.searchObjectByClass(target,
               RequeteRechercheNbResType.class.toString());
         if (objRet instanceof RequeteRechercheNbResType) { 
            RequeteRechercheNbResType req = (RequeteRechercheNbResType) objRet;
            if (req.getRequeteRechercheNbResType() != null) {
               LOG.info(getLogInformation(target, req.getRequeteRechercheNbResType()
                     .toString()));
            } else {
               throw new ParameterNotFoundException(
                     MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
            }
            
         } else {
            throw new ParameterNotFoundException(MESSAGE_LOG_ERROR_FOUND);
         }
      } catch (Exception e) {
         throw new ParameterNotFoundException(
               MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
      }
   }

   /**
    * Méthode permettant de logger les informations de tous les services qui
    * n'ont pas de paramètre particulier pouvant les différentiés des autres.
    * 
    * @param target
    *           Object cible
    * @throws ParameterNotFoundException
    */
   private void logServiceInformationServiceAutres(Object target)
         throws ParameterNotFoundException {
      LOG.info(getLogInformation(target, null));

   }

   /**
    * Log les informations contenu dans un objet cible.
    * 
    * @param target
    *           Objet cible permettant de constuire la log
    * @param valueToLog
    *           Valeur à logger
    * @return Les informations contenues dans la log
    * @throws ParameterNotFoundException
    */
   private String getLogInformation(Object target, String valueToLog)
         throws ParameterNotFoundException {
      StringBuffer buf = new StringBuffer();
      try {
         MessageContext msgCtx = MessageContext.getCurrentMessageContext();
         if (msgCtx != null) {
            buf.append("messageContextID::" + msgCtx.getLogCorrelationID());
         }
         AuthenticationToken token = (AuthenticationToken) SecurityContextHolder
               .getContext().getAuthentication();
         if (token != null && token.getIssuer() != null) {
            buf.append(" - CS::" + token.getIssuer());
         }
         if (token != null && token.getPagms() != null && !token.getPagms().isEmpty()) {
           buf.append(" - PAGM::" + String.join(",", token.getPagms()));
        }
         Object qName = SearchObjectClassUtil.searchObjectByClass(target,
               QName.class.toString());
         if (qName != null) {
            buf.append(" - " + qName.toString());
         }
         if (valueToLog != null && !valueToLog.isEmpty()) {
            buf.append(" - " + valueToLog);
         }
      } catch (Exception e) {
         throw new ParameterNotFoundException(
               MESSAGE_LOG_ERROR_PARAMETRE_NON_EXISTANT);
      }

      return buf.toString();
   }

}
