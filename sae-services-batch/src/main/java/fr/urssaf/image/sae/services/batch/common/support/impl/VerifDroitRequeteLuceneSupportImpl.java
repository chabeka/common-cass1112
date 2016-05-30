package fr.urssaf.image.sae.services.batch.common.support.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.batch.common.support.VerifDroitRequeteLuceneSupport;
import fr.urssaf.image.sae.services.document.SAESearchQueryParserService;
import fr.urssaf.image.sae.services.document.model.SAESearchQueryParserResult;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.services.util.FormatUtils;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.services.util.SAESearchUtil;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;

/**
 * Implémentation du support {@link VerifDroitRequeteLuceneSupport}
 * 
 */
@Component
public class VerifDroitRequeteLuceneSupportImpl implements
      VerifDroitRequeteLuceneSupport {
   
   private static final Logger LOG = LoggerFactory
         .getLogger(VerifDroitRequeteLuceneSupportImpl.class);

   @Autowired
   private MetadataReferenceDAO metaRefD;
   
   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices metadataCS;
   
   @Autowired
   private PrmdService prmdService;
   
   @Autowired
   private SAESearchQueryParserService queryParseService;
   
   /**
    * {@inheritDoc}
    */
   @Override
   public String verifDroitRequeteLucene(String requeteLucene,
         List<SaePrmd> prmds) throws SyntaxLuceneEx,
         SAESearchQueryParseException, SAESearchServiceEx,
         UnknownLuceneMetadataEx, MetaDataUnauthorizedToSearchEx {
      
      String prefixeTrc = "verifDroitRequeteLucene()";
      
      LOG.debug("{} - Ajustage de la requete avec les éléments des droits",
            prefixeTrc);
      String requeteDroit = prmdService.createLucene(requeteLucene, prmds);
      String requeteFinal = requeteDroit;
      
      // Vérification de la requête lucène
      SAESearchUtil.verifieSyntaxeLucene(requeteDroit);

      // Conversion de la requête avec les codes long en code court
      SAESearchQueryParserResult parserResult = queryParseService
            .convertFromLongToShortCode(requeteFinal);
      requeteFinal = parserResult.getRequeteCodeCourts();
      LOG
            .debug(
                  "{} - Requête de recherche après remplacement des codes longs par les codes courts : {}",
                  prefixeTrc, requeteFinal);

      List<String> longCodesReq = new ArrayList<String>(parserResult
            .getMetaUtilisees().keySet());
      checkExistingLuceneMetadata(longCodesReq);

      List<SAEMetadata> listCodCourt = recupererListCodCourtByLongCode(longCodesReq);
      checkSearchableLuceneMetadata(listCodCourt);
      
      return requeteFinal;
   }

   /**
    * Vérifie de l’existence des métadonnées des filtres dans le référentiel.
    * 
    * @param longCodesReq
    *           : Liste des métadonnées.
    * @throws UnknownLuceneMetadataEx
    *            : Une exception de type {@link UnknownLuceneMetadataEx}
    */
   private void checkExistingLuceneMetadata(List<String> longCodesReq)
         throws UnknownLuceneMetadataEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkExistingLuceneMetadata()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
      // Vérification de l'exsitance des codes dans le référentiel des
      // métadonnées.
      LOG
            .debug(
                  "{} - Début de la vérification : Les métadonnées utilisées dans la requête de recherche existent dans le référentiel des métadonnées",
                  prefixeTrc);
      if (!metadataCS.checkExistingQueryTerms(longCodesReq).isEmpty()) {
         List<String> luceneMetadataErrors = new ArrayList<String>();
         for (MetadataError metadataError : metadataCS
               .checkExistingQueryTerms(longCodesReq)) {
            luceneMetadataErrors.add(metadataError.getLongCode());
         }
         LOG.debug("{} - {}", prefixeTrc, ResourceMessagesUtils.loadMessage(
               "search.notexist.lucene.metadata.error", FormatUtils
                     .formattingDisplayList(luceneMetadataErrors)));
         throw new UnknownLuceneMetadataEx(ResourceMessagesUtils.loadMessage(
               "search.notexist.lucene.metadata.error", FormatUtils
                     .formattingDisplayList(luceneMetadataErrors)));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées utilisées dans la requête de recherche existent dans le référentiel des métadonnées",
                  prefixeTrc);
   }
   
   /**
    * Recupération des codes courts et des codes longs pour la recherche
    * 
    * @throws ReferentialException
    * @throws SAESearchServiceEx
    */
   private List<SAEMetadata> recupererListCodCourtByLongCode(
         List<String> listMetaDesired) throws SAESearchServiceEx {
      // si liste metadonnées désirée est vide alors recup la liste par default
      // des métadonnées consultables
      SAEMetadata saeM = null;
      List<SAEMetadata> listCodCourtConsult = new ArrayList<SAEMetadata>();
      try {
         // si liste metadonnées non vide alors pour chaque code recup la
         // MetaDataReference associée
         for (String codeLong : listMetaDesired) {
            MetadataReference metaDataRef = metaRefD.getByLongCode(codeLong);
            saeM = new SAEMetadata();
            saeM.setLongCode(codeLong);
            saeM.setShortCode(metaDataRef.getShortCode());
            saeM.setValue("");
            listCodCourtConsult.add(saeM);
         }
      } catch (ReferentialException except) {
         throw new SAESearchServiceEx(ResourceMessagesUtils
               .loadMessage("search.referentiel.error"), except);
      }
      return listCodCourtConsult;
   }
   
   /**
    * Contrôle que la liste des métadonnées est autorisée pour la recherche.
    * 
    * @param listCodCourt
    *           : Liste Lucene métadonnées
    * @throws MetaDataUnauthorizedToSearchEx
    *            Une exception de type {@link MetaDataUnauthorizedToSearchEx}
    */
   private void checkSearchableLuceneMetadata(List<SAEMetadata> listCodCourt)
         throws MetaDataUnauthorizedToSearchEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkSearchableLuceneMetadata()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG
            .debug(
                  "{} - Début de la vérification : Les métadonnées utilisées dans la requête de recherche sont des \"critères de recherche\"",
                  prefixeTrc);
      // Fin des traces debug - entrée méthode
      // verification que la liste des codes courts est de type recherchable
      if (!metadataCS.checkSearchableMetadata(listCodCourt).isEmpty()) {
         List<String> searchableMetadataErrors = new ArrayList<String>();
         for (MetadataError searchableMetadataError : Utils
               .nullSafeIterable(metadataCS
                     .checkSearchableMetadata(listCodCourt))) {
            searchableMetadataErrors.add(searchableMetadataError.getLongCode());
         }
         LOG.debug("{} - {}", prefixeTrc, ResourceMessagesUtils.loadMessage(
               "search.notsearcheable.error", FormatUtils
                     .formattingDisplayList(searchableMetadataErrors)));
         throw new MetaDataUnauthorizedToSearchEx(ResourceMessagesUtils
               .loadMessage("search.notsearcheable.error", FormatUtils
                     .formattingDisplayList(searchableMetadataErrors)));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées utilisées dans la requête de recherche sont des \"critères de recherche\"",
                  prefixeTrc);
   }
}
