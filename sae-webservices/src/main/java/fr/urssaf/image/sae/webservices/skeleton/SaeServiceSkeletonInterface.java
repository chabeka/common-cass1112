package fr.urssaf.image.sae.webservices.skeleton;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import fr.cirtil.www.saeservice.AjoutNote;
import fr.cirtil.www.saeservice.AjoutNoteResponse;
import fr.cirtil.www.saeservice.ArchivageMasse;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHash;
import fr.cirtil.www.saeservice.ArchivageMasseAvecHashResponse;
import fr.cirtil.www.saeservice.ArchivageMasseResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaire;
import fr.cirtil.www.saeservice.ArchivageUnitairePJ;
import fr.cirtil.www.saeservice.ArchivageUnitairePJResponse;
import fr.cirtil.www.saeservice.ArchivageUnitaireResponse;
import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationAffichable;
import fr.cirtil.www.saeservice.ConsultationAffichableResponse;
import fr.cirtil.www.saeservice.ConsultationGNTGNS;
import fr.cirtil.www.saeservice.ConsultationGNTGNSResponse;
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.Copie;
import fr.cirtil.www.saeservice.CopieResponse;
import fr.cirtil.www.saeservice.DocumentExistant;
import fr.cirtil.www.saeservice.DocumentExistantResponse;
import fr.cirtil.www.saeservice.EtatTraitementsMasse;
import fr.cirtil.www.saeservice.EtatTraitementsMasseResponse;
import fr.cirtil.www.saeservice.GetDocFormatOrigine;
import fr.cirtil.www.saeservice.GetDocFormatOrigineResponse;
import fr.cirtil.www.saeservice.Modification;
import fr.cirtil.www.saeservice.ModificationMasse;
import fr.cirtil.www.saeservice.ModificationMasseResponse;
import fr.cirtil.www.saeservice.ModificationResponse;
import fr.cirtil.www.saeservice.PingRequest;
import fr.cirtil.www.saeservice.PingResponse;
import fr.cirtil.www.saeservice.PingSecureRequest;
import fr.cirtil.www.saeservice.PingSecureResponse;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheNbRes;
import fr.cirtil.www.saeservice.RechercheNbResResponse;
import fr.cirtil.www.saeservice.RechercheParIterateur;
import fr.cirtil.www.saeservice.RechercheParIterateurResponse;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.cirtil.www.saeservice.RecuperationMetadonnees;
import fr.cirtil.www.saeservice.RecuperationMetadonneesResponse;
import fr.cirtil.www.saeservice.RestoreMasse;
import fr.cirtil.www.saeservice.RestoreMasseResponse;
import fr.cirtil.www.saeservice.StockageUnitaire;
import fr.cirtil.www.saeservice.StockageUnitaireResponse;
import fr.cirtil.www.saeservice.Suppression;
import fr.cirtil.www.saeservice.SuppressionMasse;
import fr.cirtil.www.saeservice.SuppressionMasseResponse;
import fr.cirtil.www.saeservice.SuppressionResponse;
import fr.cirtil.www.saeservice.Transfert;
import fr.cirtil.www.saeservice.TransfertResponse;
import fr.urssaf.image.sae.droit.exception.InvalidPagmsCombinaisonException;
import fr.urssaf.image.sae.droit.exception.UnexpectedDomainException;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.MetadataValueNotInDictionaryEx;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.CaptureExistingUuuidException;
import fr.urssaf.image.sae.services.exception.capture.DuplicatedMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.exception.capture.InvalidValueTypeAndFormatMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.NotSpecifiableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredArchivableMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.RequiredStorageMetadataEx;
import fr.urssaf.image.sae.services.exception.capture.SAECaptureServiceEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownHashCodeEx;
import fr.urssaf.image.sae.services.exception.capture.UnknownMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.services.exception.copie.SAECopieServiceException;
import fr.urssaf.image.sae.services.exception.enrichment.ReferentialRndException;
import fr.urssaf.image.sae.services.exception.enrichment.UnknownCodeRndEx;
import fr.urssaf.image.sae.services.exception.format.validation.ValidationExceptionInvalidFile;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.exception.CopieAxisFault;
import fr.urssaf.image.sae.webservices.exception.DocumentExistantAxisFault;
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
    *            exception levée si droits insuffisants
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
    * endpoint de la capture unitaire avec fichier transmis
    * 
    * @param request
    *           requete du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   ArchivageUnitairePJResponse archivageUnitairePJSecure(
         ArchivageUnitairePJ request) throws AxisFault;

   /**
    * endpoint de la capture de masse
    * 
    * @param request
    *           request du web service
    * @param callerIP
    *           adresse IP de l'appelant
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   ArchivageMasseResponse archivageMasseSecure(ArchivageMasse request,
         String callerIP) throws AxisFault;

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

   /**
    * endpoint de la capture de masse avec hash
    * 
    * @param request
    *           request du web service
    * @param callerIP
    *           adresse IP de l'appelant
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   ArchivageMasseAvecHashResponse archivageMasseAvecHashSecure(
         ArchivageMasseAvecHash request, String callerIP) throws AxisFault;

   /**
    * endpoint de la suppression de document
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   SuppressionResponse suppressionSecure(Suppression request) throws AxisFault;

   /**
    * endpoint de la modification de document
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   ModificationResponse modificationSecure(Modification request)
         throws AxisFault;

   /**
    * endpoint de la modification en masse de document
    * 
    * @param request
    *           request du web service
    * @param callerIP
    *           adresse IP de l'appelant
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   ModificationMasseResponse modificationMasseSecure(ModificationMasse request,
         String callerIP)
         throws AxisFault;

   /**
    * endpoint de la récupération des métadonnées.
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   RecuperationMetadonneesResponse recuperationMetadonneesSecure(
         RecuperationMetadonnees request) throws AxisFault;

   /**
    * endpoint du transfert de document
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée dans la consommation du web service
    */
   TransfertResponse transfertSecure(Transfert request) throws AxisFault;

   /**
    * endpoint de consultation dans un format affichable
    * 
    * @param request
    *           request du web service
    * @return reponse du web service
    * @throws AxisFault
    *            exception levée lors de la consultation
    */
   ConsultationAffichableResponse consultationAffichableSecure(
         ConsultationAffichable request) throws AxisFault;

   /**
    * Endpoint de la recherche de documents avec retour du nombre de résultats
    * 
    * @param request
    *           request du web service
    * @return Instance de RechercheNbResResponse contenant le résultat de la
    *         recherche
    * @throws AxisFault
    *            exception levée lors de la recherche
    */
   RechercheNbResResponse rechercheNbResSecure(RechercheNbRes request)
         throws AxisFault;

   /**
    * Endpoint de la recherche de documents par iterateur
    * 
    * @param request
    *           Objet contenant les paramètres de la recherche
    * @return Instance de RechercheParIterateurResponse contenant le résultat de
    *         la recherche
    * @throws AxisFault
    *            Une exception est levée lors de la recherche
    */
   RechercheParIterateurResponse rechercheParIterateurSecure(
         RechercheParIterateur request) throws AxisFault;

   /**
    * Endpoint de l'ajout d'une note à un document
    * 
    * @param request
    *           Objet contenant les paramètres de l'ajout
    * @return reponse du web service
    * @throws AxisFault
    */
   AjoutNoteResponse ajoutNoteSecure(AjoutNote request) throws AxisFault;

   /**
    * Endpoint du stockage unitaire
    * 
    * @param request
    *           Objet contenant les paramètres de la capture
    * @return reponse du web service
    * @throws AxisFault
    */
   StockageUnitaireResponse stockageUnitaireSecure(StockageUnitaire request)
         throws AxisFault;

   /**
    * Endpoint de la récupération du document attaché
    * 
    * @param request
    *           Objet contenant les paramètres de la récupération du document
    *           attaché
    * @return reponse du web service
    * @throws AxisFault
    */
   GetDocFormatOrigineResponse getDocFormatOrigineSecure(
         GetDocFormatOrigine request) throws AxisFault;

   /**
    * Endpoint de la restore de masse
    * 
    * @param request
    *           Objet contenant les paramètres de la restore de masse
    * @param callerIP
    *           adresse IP de l'appelant
    * @return reponse du web service
    * @throws AxisFault
    */
   RestoreMasseResponse restoreMasseSecure(RestoreMasse request, String callerIP)
         throws AxisFault;

   /**
    * Endpoint de la suppression de masse
    * 
    * @param request
    *           Objet contenant les paramètres de la suppression de masse
    * @param callerIP
    *           adresse IP de l'appelant
    * @return reponse du web service
    * @throws AxisFault
    */
   SuppressionMasseResponse suppressionMasseSecure(SuppressionMasse request,
         String callerIP) throws AxisFault;

   /**
    * Endpoint de la récupération des états des traitements de masse (opération
    * sans authentification)
    * 
    * @param request
    *           Objet contenant les paramètres de la récupération des états des
    *           traitements de masse
    * @param callerIP
    *           adresse IP de l'appelant
    * @return reponse du web service
    * @throws AxisFault
    */
   EtatTraitementsMasseResponse etatTraitementsMasse(
         EtatTraitementsMasse request, String callerIP) throws AxisFault;

   CopieResponse copieSecure(Copie request) throws CopieAxisFault,
         SaeAccessDeniedAxisFault, ArchiveInexistanteEx,
         SAEConsultationServiceException, SAECaptureServiceEx, ReferentialRndException, UnknownCodeRndEx, ReferentialException, SAECopieServiceException, UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx, RequiredStorageMetadataEx, InvalidValueTypeAndFormatMetadataEx, UnknownMetadataEx, DuplicatedMetadataEx, NotSpecifiableMetadataEx, EmptyDocumentEx, RequiredArchivableMetadataEx, NotArchivableMetadataEx, UnknownHashCodeEx, EmptyFileNameEx, MetadataValueNotInDictionaryEx, UnknownFormatException, ValidationExceptionInvalidFile, UnexpectedDomainException, InvalidPagmsCombinaisonException, CaptureExistingUuuidException;

   DocumentExistantResponse documentExistant(DocumentExistant request)
         throws  DocumentExistantAxisFault, SearchingServiceEx, ConnectionServiceEx;
   
   ConsultationGNTGNSResponse consultationGNTGNSSecure(ConsultationGNTGNS request) throws ConsultationAxisFault, SearchingServiceEx, ConnectionServiceEx, SAEConsultationServiceException, UnknownDesiredMetadataEx, MetaDataUnauthorizedToConsultEx, AxisFault, SAEConsultationAffichableParametrageException, RemoteException;
}
