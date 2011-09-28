package fr.urssaf.image.sae.webservices.service.impl;

import static fr.urssaf.image.sae.webservices.service.factory.ObjectRechercheFactory.createRechercheResponse;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.cirtil.www.saeservice.MetadonneeCodeType;
import fr.cirtil.www.saeservice.Recherche;
import fr.cirtil.www.saeservice.RechercheResponse;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.document.SAEDocumentService;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.webservices.exception.RechercheAxis2Fault;
import fr.urssaf.image.sae.webservices.service.WSRechercheService;
import fr.urssaf.image.sae.webservices.util.MessageRessourcesUtils;

/**
 * Classe concrète pour le service de recherche.
 */
@SuppressWarnings("PMD.PreserveStackTrace")
@Service
public class WSRechercheServiceImpl implements WSRechercheService {

   private static final String VIDE = "";

   /**
    * Façade de service SAE : Capture, Consultation, et Recherche.
    */
   @Autowired
   private SAEDocumentService documentService;

   /**
    * {@inheritDoc}
    * 
    * */
   @Override
   public final RechercheResponse search(Recherche request)
         throws RechercheAxis2Fault {

      int maxResult = Integer.parseInt(MessageRessourcesUtils.recupererMessage(
            "max.lucene.results", null));
      boolean resultatTronque = false;
      RechercheResponse response;
      String requeteLucene = VIDE;
      try {
         requeteLucene = recupererReqLucene(request);
         checkNotNull(requeteLucene);
         List<String> listMDDesired = recupererListMDDesired(recupererListMDSearch(request));
         List<UntypedDocument> untypedDocuments = documentService.search(
               requeteLucene, listMDDesired);
         if (untypedDocuments.size() >= maxResult + 1) {
            resultatTronque = true;
         }
         response = createRechercheResponse(untypedDocuments, resultatTronque);

      } catch (SAESearchServiceEx except) {
         throw new RechercheAxis2Fault(except.getMessage(), "ErreurInterneRecherche");
      } catch (MetaDataUnauthorizedToSearchEx except) {
         throw new RechercheAxis2Fault(except.getMessage(), "RechercheMetadonneesInterdite");
      } catch (MetaDataUnauthorizedToConsultEx except) {
         throw new RechercheAxis2Fault(except.getMessage(), "ConsultationMetadonneesInterdite");
      } catch (UnknownDesiredMetadataEx except) {
         throw new RechercheAxis2Fault(except.getMessage(), "ConsultationMetadonneesInconnues");
      } catch (UnknownLuceneMetadataEx except) {
         throw new RechercheAxis2Fault(except.getMessage(), "RechercheMetadonneesInconnues");
      } catch (SyntaxLuceneEx except) {
         throw new RechercheAxis2Fault(except.getMessage(), "SyntaxeLuceneNonValide");
      } catch (RechercheAxis2Fault except) {
         throw new RechercheAxis2Fault(except.getMessage(), "RequeteLuceneVideOuNull");
      }
      return response;
   }

   /**
    * Verifier que la requête est non null et non vide
    * 
    * @throws RechercheAxis2Fault
    *            Une exception est levée lors de la recherche.
    */
   private void checkNotNull(String requeteLucene) throws RechercheAxis2Fault {
      if (isEmpty(requeteLucene.trim())) {
         throw new RechercheAxis2Fault(MessageRessourcesUtils.recupererMessage(
               "search.lucene.videornull", null), "RequeteLuceneVideOuNull");
      }
   }

   /**
    * Récupérer la liste des codes des meta données souhaitées.
    */
   private List<String> recupererListMDDesired(
         MetadonneeCodeType[] listeMDSearch) {
      List<String> listMDDesired = new ArrayList<String>();
      if (listeMDSearch != null) {
         for (MetadonneeCodeType metadonneeCodeType : listeMDSearch) {
            String code = metadonneeCodeType.getMetadonneeCodeType();
            listMDDesired.add(code);
         }
      }
      return listMDDesired;
   }

   /**
    * Récupérer la requête Lucene
    */
   private String recupererReqLucene(Recherche request) {
      return request.getRecherche().getRequete().getRequeteRechercheType();
   }

   /**
    * Récupérer La liste métadonnées souhaitées.
    */
   private MetadonneeCodeType[] recupererListMDSearch(Recherche request) {
      return request.getRecherche().getMetadonnees().getMetadonneeCode();
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
