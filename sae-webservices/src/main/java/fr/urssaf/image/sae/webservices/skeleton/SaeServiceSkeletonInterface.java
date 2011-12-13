package fr.urssaf.image.sae.webservices.skeleton;

import org.apache.axis2.AxisFault;

import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.PingRequest;
import fr.cirtil.www.saeservice.PingResponse;
import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;

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
    */
   ConsultationResponse consultationSecure(Consultation request)
         throws AxisFault;

   /**
    * endpoint de recherche
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
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
    */
   ArchivageUnitaireResponse archivageUnitaireSecure(ArchivageUnitaire request)
         throws AxisFault;

   /**
    * endpoint de la capture de masse
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   ArchivageMasseResponse archivageMasseSecure(ArchivageMasse request)
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
