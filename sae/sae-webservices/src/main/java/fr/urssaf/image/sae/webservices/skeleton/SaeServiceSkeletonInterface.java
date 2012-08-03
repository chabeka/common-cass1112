package fr.urssaf.image.sae.webservices.skeleton;

import org.apache.axis2.AxisFault;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.PingRequest;
import fr.cirtil.www.saeservice.PingResponse;
import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.urssaf.image.sae.webservices.security.exception.SaeAccessDeniedAxisFault;

/**
 * interface des services web du SAE
 * 
 * 
 */
public interface SaeServiceSkeletonInterface {

   /**
    * endpoint de consultation
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée lors de la consultation
    * @throws SaeAccessDeniedAxisFault
    *             exception levée si droits insuffisants
    */
   ConsultationResponse consultationSecure(Consultation request)
         throws AxisFault, SaeAccessDeniedAxisFault;
   
   /**
    * endpoint de consultation avec MTOM
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée lors de la consultation
    * @throws SaeAccessDeniedAxisFault
    *             exception levée si droits insuffisants
    */
   ConsultationMTOMResponse consultationMTOMSecure(ConsultationMTOM request)
         throws AxisFault;

   /**
    * endpoint de recherche
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    * @throws SaeAccessDeniedAxisFault
    *             exception levée si droits insuffisants
    */
   RechercheResponse rechercheSecure(Recherche request) throws AxisFault;

   /**
    * endpoint de la capture unitaire
    * 
    * @param request
    *           requete du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    * @throws SaeAccessDeniedAxisFault
    *             exception levée si droits insuffisants
    */
   ArchivageUnitaireResponse archivageUnitaireSecure(ArchivageUnitaire request)
         throws AxisFault;
   
   /**
    * endpoint de la capture unitaire avec fichier transmis
    * 
    * @param request
    *           requete du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    * @throws SaeAccessDeniedAxisFault
    *             exception levée si droits insuffisants
    */
   ArchivageUnitairePJResponse archivageUnitairePJSecure(ArchivageUnitairePJ request)
         throws AxisFault;

   /**
    * endpoint de la capture de masse
    * 
    * @param request
    *           request du web service
    * @param callerIP
    *          adresse IP de l'appelant 
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    * @throws SaeAccessDeniedAxisFault
    *             exception levée si droits insuffisants
    */
   ArchivageMasseResponse archivageMasseSecure(ArchivageMasse request, String callerIP)
         throws AxisFault;

   /**
    * endpoint du ping sécurisé
    * 
    * @param pingRequest
    *           vide
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   PingSecureResponse pingSecure(PingSecureRequest pingRequest)
         throws AxisFault;

   /**
    * endpoint du ping
    * 
    * @param pingRequest
    *           vide
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   PingResponse ping(PingRequest pingRequest) throws AxisFault;

}
