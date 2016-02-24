package fr.urssaf.image.sae.webservices.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.Consultation;
import fr.cirtil.www.saeservice.ConsultationAffichable;
import fr.cirtil.www.saeservice.ConsultationAffichableResponse;
import fr.cirtil.www.saeservice.ConsultationMTOM;
import fr.cirtil.www.saeservice.ConsultationMTOMResponse;
import fr.cirtil.www.saeservice.ConsultationResponse;
import fr.cirtil.www.saeservice.ListeMetadonneeCodeType;
import fr.cirtil.www.saeservice.MetadonneeType;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.consultation.model.ConsultParams;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationAffichableParametrageException;
import fr.urssaf.image.sae.services.exception.consultation.SAEConsultationServiceException;
import fr.urssaf.image.sae.webservices.exception.ConsultationAxisFault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSConsultationService;
import fr.urssaf.image.sae.webservices.service.factory.ObjectConsultationFactory;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;

/**
 * Implémentation de {@link WSConsultationService}<br>
 * L'implémentation est annotée par {@link Service}
 * 
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@Service
public final class WSConsultationServiceImpl implements WSConsultationService {

   private static final Logger LOG = LoggerFactory
         .getLogger(WSConsultationServiceImpl.class);

   private static final String FORMAT_FICHIER = "FormatFichier";

   @Autowired
   @Qualifier("documentService")
   private SAEDocumentService saeService;

   /**
    * {@inheritDoc}
    */
   @Override
   public ConsultationResponse consultation(Consultation request)
         throws ConsultationAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "consultation()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Lecture de l'UUID depuis l'objet de requête de la couche ws
      UUID uuid = UUID.fromString(request.getConsultation().getIdArchive()
            .getUuidType());
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", prefixeTrc,
            uuid);

      // Lecture des métadonnées depuis l'objet de requête de la couche ws
      ListeMetadonneeCodeType listeMetaWs = request.getConsultation()
            .getMetadonnees();

      // Convertit la liste des métadonnées de l'objet de la couche ws vers un
      // objet plus exploitable
      List<String> listeMetas = convertListeMetasWebServiceToService(listeMetaWs);

      // Appel de la méthode commune entre avec MTOM et sans MTOM
      // Cette méthode se charge des vérifications et de la levée des AxisFault
      UntypedDocument untypedDocument = consultationCommune(uuid, listeMetas);

      // Conversion de l'objet UntypedDocument en un objet de la couche web
      // service
      List<MetadonneeType> metadatas = convertListeMetasServiceToWebService(untypedDocument
            .getUMetadatas());
      ConsultationResponse response = ObjectConsultationFactory
            .createConsultationResponse(untypedDocument.getContent(), metadatas);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Renvoie l'objet de réponse de la couche web service
      return response;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ConsultationMTOMResponse consultationMTOM(ConsultationMTOM request)
         throws ConsultationAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "consultationMTOM()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Lecture de l'UUID depuis l'objet de requête de la couche ws
      UUID uuid = UUID.fromString(request.getConsultationMTOM().getIdArchive()
            .getUuidType());
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", prefixeTrc,
            uuid);

      // Lecture des métadonnées depuis l'objet de requête de la couche ws
      ListeMetadonneeCodeType listeMetaWs = request.getConsultationMTOM()
            .getMetadonnees();

      // Convertit la liste des métadonnées de l'objet de la couche ws vers un
      // objet plus exploitable
      List<String> listeMetas = convertListeMetasWebServiceToService(listeMetaWs);

      // Ajout de la métadonnée FormatFichier si besoin
      // Pour pouvoir récupérer le type MIME par la suite
      boolean fmtFicAjoute = ajouteSiBesoinMetadonneeFormatFichier(listeMetas);

      // Appel de la méthode commune entre avec MTOM et sans MTOM
      // Cette méthode se charge des vérifications et de la levée des AxisFault
      UntypedDocument untypedDocument = consultationCommune(uuid, listeMetas);

      // Récupération du type MIME et suppression si besoin de FormatFichier
      String typeMime = typeMimeDepuisFormatFichier(
            untypedDocument.getUMetadatas(), fmtFicAjoute);

      // Conversion de l'objet UntypedDocument en un objet de la couche web
      // service
      List<MetadonneeType> metadatas = convertListeMetasServiceToWebService(untypedDocument
            .getUMetadatas());
      ConsultationMTOMResponse response = ObjectConsultationFactory
            .createConsultationMTOMResponse(untypedDocument.getContent(),
                  metadatas, typeMime);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Renvoie l'objet de réponse de la couche web service
      return response;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ConsultationAffichableResponse consultationAffichable(
         ConsultationAffichable request) throws ConsultationAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "consultationAffichable()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Lecture de l'UUID depuis l'objet de requête de la couche ws
      UUID uuid = UUID.fromString(request.getConsultationAffichable()
            .getIdArchive().getUuidType());
      LOG.debug("{} - UUID envoyé par l'application cliente : {}", prefixeTrc,
            uuid);

      // Lecture des métadonnées depuis l'objet de requête de la couche ws
      ListeMetadonneeCodeType listeMetaWs = request.getConsultationAffichable()
            .getMetadonnees();

      // Convertit la liste des métadonnées de l'objet de la couche ws vers un
      // objet plus exploitable
      List<String> listeMetas = convertListeMetasWebServiceToService(listeMetaWs);

      // recuperation du numero de page et du nombre de pages
      Integer numeroPage = null;
      Integer nombrePages = null;
      if (request.getConsultationAffichable().isNumeroPageSpecified()) {
         numeroPage = Integer.valueOf(request.getConsultationAffichable()
               .getNumeroPage());
      }
      if (request.getConsultationAffichable().isNombrePagesSpecified()) {
         nombrePages = Integer.valueOf(request.getConsultationAffichable()
               .getNombrePages());
      }

      // Ajout de la métadonnée FormatFichier si besoin
      // Pour pouvoir récupérer le type MIME par la suite
      boolean fmtFicAjoute = ajouteSiBesoinMetadonneeFormatFichier(listeMetas);

      // Appel de la méthode de consultation affichable
      // Cette méthode se charge des vérifications et de la levée des AxisFault
      UntypedDocument untypedDocument = consultationAffichable(uuid,
            listeMetas, numeroPage, nombrePages);

      // Récupération du type MIME et suppression si besoin de FormatFichier
      String typeMime = typeMimeDepuisFormatFichier(
            untypedDocument.getUMetadatas(), fmtFicAjoute);

      // Conversion de l'objet UntypedDocument en un objet de la couche web
      // service
      List<MetadonneeType> metadatas = convertListeMetasServiceToWebService(untypedDocument
            .getUMetadatas());

      ConsultationAffichableResponse response = ObjectConsultationFactory
            .createConsultationAffichableResponse(untypedDocument.getContent(),
                  metadatas, typeMime);

      // Traces debug - sortie méthode
      LOG.debug("{} - Sortie", prefixeTrc);

      // Renvoie l'objet de réponse de la couche web service
      return response;
   }

   private UntypedDocument consultationCommune(UUID uuid, List<String> listMetas)
         throws ConsultationAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "consultationCommune()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      try {

         // Appel de la couche service
         ConsultParams consultParams = new ConsultParams(uuid, listMetas);
         UntypedDocument untypedDocument = saeService
               .consultation(consultParams);

         // Regarde si l'archive a été retrouvée dans le SAE. Si ce n'est pas le
         // cas, on lève la SoapFault correspondante
         if (untypedDocument == null) {
            LOG.debug(
                  "{} - L'archive demandée n'a pas été retrouvée dans le SAE ({})",
                  prefixeTrc, uuid);
            throw new ConsultationAxisFault("ArchiveNonTrouvee",
                  "Il n'existe aucun document pour l'identifiant d'archivage '"
                        + uuid + "'");

         } else {

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);

            // Renvoie le UntypedDocument
            return untypedDocument;

         }

      } catch (SAEConsultationServiceException e) {
         throw new ConsultationAxisFault(e);

      } catch (UnknownDesiredMetadataEx e) {
         throw new ConsultationAxisFault(e.getMessage(),
               "ConsultationMetadonneesInexistante", e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         throw new ConsultationAxisFault(e.getMessage(),
               "ConsultationMetadonneesNonAutorisees", e);
      }

   }

   private UntypedDocument consultationAffichable(UUID uuid,
         List<String> listMetas, Integer numeroPage, Integer nombrePages)
         throws ConsultationAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "consultationAffichable()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      try {

         // Appel de la couche service
         ConsultParams consultParams = new ConsultParams(uuid, listMetas,
               numeroPage, nombrePages);
         UntypedDocument untypedDocument = saeService
               .consultationAffichable(consultParams);

         // Regarde si l'archive a été retrouvée dans le SAE. Si ce n'est pas le
         // cas, on lève la SoapFault correspondante
         if (untypedDocument == null) {
            LOG.debug(
                  "{} - L'archive demandée n'a pas été retrouvée dans le SAE ({})",
                  prefixeTrc, uuid);
            throw new ConsultationAxisFault("ArchiveNonTrouvee",
                  "Il n'existe aucun document pour l'identifiant d'archivage '"
                        + uuid + "'");

         } else {

            // Traces debug - sortie méthode
            LOG.debug("{} - Sortie", prefixeTrc);

            // Renvoie le UntypedDocument
            return untypedDocument;

         }

      } catch (SAEConsultationServiceException e) {
         throw new ConsultationAxisFault(e);

      } catch (UnknownDesiredMetadataEx e) {
         throw new ConsultationAxisFault(e.getMessage(),
               "ConsultationMetadonneesInexistante", e);
      } catch (MetaDataUnauthorizedToConsultEx e) {
         throw new ConsultationAxisFault(e.getMessage(),
               "ConsultationMetadonneesNonAutorisees", e);
      } catch (SAEConsultationAffichableParametrageException e) {
         throw new ConsultationAxisFault(e.getMessage(),
               "ConsultationAffichableParametrageIncorrect", e);
      }

   }

   private List<String> convertListeMetasWebServiceToService(
         ListeMetadonneeCodeType listeMetaWs) {

      if (listeMetaWs == null) {
         return null;
      } else {
         return ObjectTypeFactory.buildMetaCodeFromWS(listeMetaWs);
      }

   }

   private List<MetadonneeType> convertListeMetasServiceToWebService(
         List<UntypedMetadata> listeMetasService) {

      List<MetadonneeType> metadatas = new ArrayList<MetadonneeType>();

      for (UntypedMetadata untypedMetadata : CollectionUtils
            .loadListNotNull(listeMetasService)) {

         String code = untypedMetadata.getLongCode();
         String valeur = untypedMetadata.getValue();
         if (untypedMetadata.getValue() == null) {
            valeur = StringUtils.EMPTY;
         }
         MetadonneeType metadonnee = ObjectTypeFactory.createMetadonneeType(
               code, valeur);

         metadatas.add(metadonnee);
      }

      return metadatas;

   }

   /**
    * Ajoute la métadonnée FormatFichier à la liste des métadonnées demandées :<br>
    * <ul>
    * <li>si la liste n'est pas vide. En effet, si la liste est vide, la
    * métadonnée FormatFichier sera renvoyée par la couche service, car elle est
    * "consultée par défaut"</li>
    * <li>si la liste ne contient pas déjà la métadonnée FormatFichier</li>
    * </ul>
    * 
    * @param listeMetas
    *           la liste des métadonnées demandées par l'application cliente
    * @return true si la métadonnée FormatFichier a dû être ajoutée à la liste,
    *         false dans le cas contraire
    */
   protected boolean ajouteSiBesoinMetadonneeFormatFichier(
         List<String> listeMetas) {

      // Traces debug - entrée méthode
      String prefixeTrc = "ajouteSiBesoinMetadonneeFormatFichier()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      boolean metaAjoutee;

      if (org.apache.commons.collections.CollectionUtils.isEmpty(listeMetas)
            || listeMetas.contains(FORMAT_FICHIER)) {

         LOG.debug(
               "{} - La métadonnée FormatFichier n'a pas besoin d'être ajoutée à la liste",
               prefixeTrc);
         metaAjoutee = false;

      } else {

         LOG.debug(
               "{} - Ajout automatique et temporaire de la métadonnée FormatFichier",
               prefixeTrc);
         metaAjoutee = listeMetas.add(FORMAT_FICHIER);

      }

      LOG.debug("{} - Sortie", prefixeTrc);
      return metaAjoutee;

   }

   /**
    * Renvoie le type MIME déterminé à partir de la métadonnée FormatFichier.<br>
    * Supprime éventuellement la métadonnée FormatFichier de liste des
    * métadonnées.
    * 
    * @param listeMetas
    *           la liste des métadonnées issues de la couche service
    * @param supprMetaFmtFic
    *           flag indiquant s'il faut retirer la métadonnée FormatFichier de
    *           la liste des métadonnées
    * @return le type MIME
    * @throws ConsultationAxisFault
    *            levée si la métadonnée FormatFichier n'est pas présente dans la
    *            liste des métadonnées
    */
   protected String typeMimeDepuisFormatFichier(
         List<UntypedMetadata> listeMetas, boolean supprMetaFmtFic)
         throws ConsultationAxisFault {

      // Traces debug - entrée méthode
      String prefixeTrc = "typeMimeDepuisFormatFichier()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // Cherche la métadonnée FormatFichier
      UntypedMetadata metaFormatFichier = null;
      if (!org.apache.commons.collections.CollectionUtils.isEmpty(listeMetas)) {
         for (UntypedMetadata meta : listeMetas) {
            if (FORMAT_FICHIER.equals(meta.getLongCode())) {
               metaFormatFichier = meta;
               break;
            }
         }
      }
      if (metaFormatFichier == null) {
         // Erreur technique et non fonctionnelle
         LOG.debug(
               "{} - Levée d'une ConsultationAxisFault : la métadonnée FormatFichier n'a pas été trouvée dans la liste des mtadonnées, alors qu'elle est censée être présente.",
               prefixeTrc);
         throw new ConsultationAxisFault("ErreurInterne",
               "Une erreur interne à l'application est survenue.");
      }

      // Si besoin, supprime la métadonnée FormatFichier de la liste des
      // métadonnées
      if (supprMetaFmtFic) {
         LOG.debug(
               "{} - Suppression de la métadonnée FormatFichier de la liste des métadonnées.",
               prefixeTrc);
         listeMetas.remove(metaFormatFichier);
      }

      // Convertit le type PRONOM en type MIME
      String typePronom = metaFormatFichier.getValue();
      LOG.debug("{} - Type PRONOM : {}", prefixeTrc, typePronom);
      String typeMime = convertitPronomEnTypeMime(metaFormatFichier.getValue());
      LOG.debug("{} - Type Mime déduit : {}", prefixeTrc, typeMime);

      // Renvoie du type MIME à l'appelant
      LOG.debug("{} - Sortie", prefixeTrc);
      return typeMime;

   }

   /**
    * Convertit un type PRONOM en type MIME<br>
    * <br>
    * NB : extraire plus tard cette méthode dans la future gestion des formats<br>
    * 
    * @param typePronom
    *           le type PRONOM
    * @return le type MIME correspondant
    */
   protected String convertitPronomEnTypeMime(String typePronom) {

      // Traces debug - entrée méthode
      String prefixeTrc = "convertitPronomEnTypeMime()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      // C'est parti pour une clause if
      // Pour l'instant, le SAE n'accepte que le "fmt/354"
      String typeMime;
      if (StringUtils.equalsIgnoreCase("fmt/354", typePronom)) {
         typeMime = "application/pdf";
      } else {
         typeMime = "application/octet-stream"; // correspond à la valeur par
         // défaut précédemment utilisée
      }

      // Renvoie du type MIME à l'appelant
      LOG.debug("{} - Sortie", prefixeTrc);
      return typeMime;

   }

}
