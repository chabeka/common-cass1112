package fr.urssaf.image.sae.webservices.service.impl;

import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheResponse;
import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheNbResResponse;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheNbRes;
import fr.cirtil.www.saeservice.RechercheNbResResponse;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.constantes.RechercheConstantes;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.factory.ObjectTypeFactory;
import fr.urssaf.image.sae.webservices.service.WSRechercheService;
import fr.urssaf.image.sae.webservices.util.CollectionUtils;
import fr.urssaf.image.sae.webservices.util.WsMessageRessourcesUtils;

/**
 * Classe concrète pour le service de recherche.
 */
@Service
public class WSRechercheServiceImpl implements WSRechercheService {
   private static final Logger LOG = LoggerFactory
         .getLogger(WSRechercheServiceImpl.class);
   private static final String VIDE = "";

   /**
    * Façade de service SAE : Capture, Consultation, et Recherche.
    */
   @Autowired
   private SAEDocumentService documentService;

   @Autowired
   private WsMessageRessourcesUtils wsMessageRessourcesUtils;

   /**
    * {@inheritDoc}
    * 
    * */
   @Override
   public final RechercheResponse search(Recherche request)
         throws RechercheAxis2Fault {
      // Traces debug - entrée méthode
      String prefixeTrc = "search()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode

      int maxResult = RechercheConstantes.NB_MAX_RESULTATS_RECHERCHE;
      LOG.debug(
            "{} - Le nombre maximum de documents à renvoyer dans les résultats de "
                  + "recherche au niveau de la couche webservice est {}",
            prefixeTrc, maxResult);
      boolean resultatTronque = false;
      RechercheResponse response;
      String requeteLucene = VIDE;
      try {
         requeteLucene = recupererReqLucene(request);
         checkNotNull(requeteLucene);
         List<String> listMDDesired = recupererListMDDesired(recupererListMDSearch(request));
         List<UntypedDocument> untypedDocuments = documentService.search(
               requeteLucene, listMDDesired);
         if (untypedDocuments.size() > maxResult) {
            resultatTronque = true;
            LOG
                  .debug(
                        "{} - Les résultats de recherche sont tronqués à {} résultats",
                        prefixeTrc, maxResult);
         }

         response = createRechercheResponse(untypedDocuments, resultatTronque);

      } catch (SAESearchServiceEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "ErreurInterneRecherche", except);
      } catch (MetaDataUnauthorizedToSearchEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "RechercheMetadonneesInterdite", except);
      } catch (MetaDataUnauthorizedToConsultEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "ConsultationMetadonneesInterdite", except);
      } catch (UnknownDesiredMetadataEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "ConsultationMetadonneesInconnues", except);
      } catch (UnknownLuceneMetadataEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "RechercheMetadonneesInconnues", except);
      } catch (SyntaxLuceneEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "SyntaxeLuceneNonValide", except);
      } catch (RechercheAxis2Fault except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "RequeteLuceneVideOuNull", except);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      return response;
   }
   
   /**
    * {@inheritDoc}
    * 
    * */
   @Override
   public RechercheNbResResponse searchWithNbRes(RechercheNbRes request) throws RechercheAxis2Fault {
      
      //-- Traces debug - entrée méthode
      String prefixeTrc = "search()";
      LOG.debug("{} - Début", prefixeTrc);

      int maxResult = RechercheConstantes.NB_MAX_RESULTATS_RECHERCHE;
      LOG.debug(
            "{} - Le nombre maximum de documents à renvoyer dans les résultats de "
                  + "recherche au niveau de la couche webservice est {}",
            prefixeTrc, maxResult);
      boolean resultatTronque = false;
      RechercheNbResResponse response;
      String requeteLucene = VIDE;
      try {
         requeteLucene = recupererReqLucene(request);
         checkNotNull(requeteLucene);
         List<String> listMDDesired = recupererListMDDesired(recupererListMDSearch(request));
         List<UntypedDocument> untypedDocuments = documentService.search(requeteLucene, listMDDesired);
         if (untypedDocuments.size() > maxResult) {
            resultatTronque = true;
            String mssg = "{} - Les résultats de recherche sont tronqués à {} résultats";
            LOG.debug(mssg, prefixeTrc, maxResult);
         }
         response = createRechercheNbResResponse(untypedDocuments, resultatTronque);

      } catch (SAESearchServiceEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "ErreurInterneRecherche", except);
      } catch (MetaDataUnauthorizedToSearchEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "RechercheMetadonneesInterdite", except);
      } catch (MetaDataUnauthorizedToConsultEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "ConsultationMetadonneesInterdite", except);
      } catch (UnknownDesiredMetadataEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "ConsultationMetadonneesInconnues", except);
      } catch (UnknownLuceneMetadataEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "RechercheMetadonneesInconnues", except);
      } catch (SyntaxLuceneEx except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "SyntaxeLuceneNonValide", except);
      } catch (RechercheAxis2Fault except) {
         throw new RechercheAxis2Fault(except.getMessage(),
               "RequeteLuceneVideOuNull", except);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      return response;
   }

   /**
    * Verifier que la requête est non null et non vide
    * 
    * @throws RechercheAxis2Fault
    *            Une exception est levée lors de la recherche.
    */
   private void checkNotNull(String requeteLucene) throws RechercheAxis2Fault {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkNotNull()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
      LOG
            .debug(
                  "{} - Début de la vérification : La requête de recherche est renseignée",
                  prefixeTrc);
      if (isEmpty(requeteLucene.trim())) {
         throw new RechercheAxis2Fault(wsMessageRessourcesUtils
               .recupererMessage("search.lucene.videornull", null),
               "RequeteLuceneVideOuNull");
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : La requête de recherche est renseignée",
                  prefixeTrc);
      LOG.debug("{} - Sortie", prefixeTrc);
   }

   /**
    * Récupérer la liste des codes des meta données souhaitées.
    */
   protected static List<String> recupererListMDDesired(
         MetadonneeCodeType[] listeMDSearch) {

      return CollectionUtils.loadListNotNull(ObjectTypeFactory
            .buildMetaCodeFromWS(listeMDSearch));

   }

   /**
    * Récupérer la requête Lucene
    */
   private String recupererReqLucene(Recherche request) {
      return request.getRecherche().getRequete().getRequeteRechercheType();
   }
   
   /**
    * Récupérer la requête Lucene
    */
   private String recupererReqLucene(RechercheNbRes request) {
      return request.getRechercheNbRes().getRequete().getRequeteRechercheNbResType();
   }

   /**
    * Récupérer La liste métadonnées souhaitées.
    */
   private MetadonneeCodeType[] recupererListMDSearch(Recherche request) {
      return request.getRecherche().getMetadonnees().getMetadonneeCode();
   }
   
   /**
    * Récupérer La liste métadonnées souhaitées.
    */
   private MetadonneeCodeType[] recupererListMDSearch(RechercheNbRes request) {
      return request.getRechercheNbRes().getMetadonnees().getMetadonneeCode();
   }

   /**
    * @return Le provider pour les services de Capture, recherche et
    *         consultation.
    */
   public final SAEDocumentService getDocumentService() {
      return documentService;
   }

   /**
    * @param documentService
    *           : Le provider pour les services de Capture, recherche et
    *           consultation.
    */
   public final void setDocumentService(SAEDocumentService documentService) {
      this.documentService = documentService;
   }

}
