package fr.urssaf.image.sae.services.document.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.AbstractMetadata;
import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.PaginatedUntypedDocuments;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedRangeMetadata;
import fr.urssaf.image.sae.building.services.BuildService;
import fr.urssaf.image.sae.droit.model.SaePrmd;
import fr.urssaf.image.sae.droit.service.PrmdService;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.SAESearchQueryParserService;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.document.model.SAESearchQueryParserResult;
import fr.urssaf.image.sae.services.exception.SAESearchQueryParseException;
import fr.urssaf.image.sae.services.exception.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.consultation.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.DoublonFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownFiltresMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.services.messages.ServiceMessageHandler;
import fr.urssaf.image.sae.services.util.FormatUtils;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.services.util.SAESearchUtil;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.NotRangeFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.NotValueFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.RangeFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.ValueFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.vi.spring.AuthenticationContext;
import fr.urssaf.image.sae.vi.spring.AuthenticationToken;

/**
 * Fournit l'implémentation des services pour la recherche.
 */
@Service
@Qualifier("saeSearchService")
public class SAESearchServiceImpl extends AbstractSAEServices implements
      SAESearchService {

   private static final Logger LOG = LoggerFactory
         .getLogger(SAESearchServiceImpl.class);

   @Autowired
   private MetadataReferenceDAO metaRefD;

   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices metadataCS;

   @Autowired
   @Qualifier("buildService")
   private BuildService buildService;

   @Autowired
   private MappingDocumentService mappingDocumentService;

   @Autowired
   private MetadataReferenceDAO mrdao;

   @Autowired
   private PrmdService prmdService;

   @Autowired
   private SAESearchQueryParserService queryParseService;

   /**
    * {@inheritDoc}
    */
   public final List<UntypedDocument> search(String requete,
         List<String> listMetaDesired) throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {

      int maxResult = Integer.parseInt(ServiceMessageHandler
            .getMessage("max.lucene.results")) + 1;

      return search(requete, listMetaDesired, maxResult);

   }

   /**
    * @param listCodCourtConsult
    * @param isFromRefrentiel
    * @throws MetaDataUnauthorizedToConsultEx
    */
   private void checkConsultableDesiredMetadata(
         List<SAEMetadata> listCodCourtConsult, boolean isFromRefrentiel)
         throws MetaDataUnauthorizedToConsultEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkConsultableDesiredMetadata()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug(
            "{} - Début de la vérification : Les métadonnées demandées dans les résultats de recherche sont autorisées à la consultation",
            prefixeTrc);
      // Fin des traces debug - entrée méthode
      // verification que la liste des codes courts est de type recherchable
      if (!isFromRefrentiel
            && !metadataCS.checkConsultableMetadata(listCodCourtConsult)
                  .isEmpty()) {
         List<String> consultMetadataErrors = new ArrayList<String>();
         for (MetadataError searchableMetadataError : Utils
               .nullSafeIterable(metadataCS
                     .checkConsultableMetadata(listCodCourtConsult))) {
            consultMetadataErrors.add(searchableMetadataError.getLongCode());
         }
         LOG.debug("{} - {}", prefixeTrc, ResourceMessagesUtils.loadMessage(
               "search.notconsult.error",
               FormatUtils.formattingDisplayList(consultMetadataErrors)));
         throw new MetaDataUnauthorizedToConsultEx(
               ResourceMessagesUtils.loadMessage("search.notconsult.error",
                     FormatUtils.formattingDisplayList(consultMetadataErrors)));
      }
      LOG.debug(
            "{} - Fin de la vérification : Les métadonnées demandées dans les résultats de recherche sont autorisées à la consultation",
            prefixeTrc);
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
      LOG.debug(
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
               "search.notsearcheable.error",
               FormatUtils.formattingDisplayList(searchableMetadataErrors)));
         throw new MetaDataUnauthorizedToSearchEx(
               ResourceMessagesUtils.loadMessage("search.notsearcheable.error",
                     FormatUtils
                           .formattingDisplayList(searchableMetadataErrors)));
      }
      LOG.debug(
            "{} - Fin de la vérification : Les métadonnées utilisées dans la requête de recherche sont des \"critères de recherche\"",
            prefixeTrc);
   }

   /**
    * Vérifie de l’existence des métadonnées souhaitées dans le référentiel.
    * 
    * @param listLongCodeDesired
    *           : Liste des métadonnées souhaitées.
    * @throws UnknownDesiredMetadataEx
    *            : Une exception de type {@link UnknownDesiredMetadataEx}
    */
   private void checkExistingMetadataDesired(List<String> listLongCodeDesired)
         throws UnknownDesiredMetadataEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkExistingMetadataDesired()";
      LOG.debug("{} - Début", prefixeTrc);
      LOG.debug(
            "{} - Début de la vérification : Les métadonnées demandées dans les résultats de recherche existent dans le référentiel des métadonnées",
            prefixeTrc);
      // Fin des traces debug - entrée méthode

      // verification des métadonnées présentent dans la requête de recherche
      // qui
      // n’existent pas dans le référentiel des métadonnées
      if (!metadataCS.checkExistingQueryTerms(listLongCodeDesired).isEmpty()) {
         List<String> listMetaDesiredErrors = new ArrayList<String>();
         for (MetadataError metadataError : metadataCS
               .checkExistingQueryTerms(listLongCodeDesired)) {
            listMetaDesiredErrors.add(metadataError.getLongCode());
         }
         LOG.debug("{} - {}", prefixeTrc, ResourceMessagesUtils.loadMessage(
               "search.notexist.metadata.desired.error",
               FormatUtils.formattingDisplayList(listMetaDesiredErrors)));
         throw new UnknownDesiredMetadataEx(ResourceMessagesUtils.loadMessage(
               "search.notexist.metadata.desired.error",
               FormatUtils.formattingDisplayList(listMetaDesiredErrors)));
      }
      LOG
            .debug(
                  "{} - Fin de la vérification : Les métadonnées demandées dans les résultats de recherche existent dans le référentiel des métadonnées",
                  prefixeTrc);
   }

   /**
    * Vérifie de l’existence des métadonnées de la requête Lucene dans le
    * référentiel.
    * 
    * @param longCodesReq
    *           : Liste des métadonnées.
    * @throws UnknownLuceneMetadataEx
    *            : Une exception de type {@link UnknownLuceneMetadataEx}
    */
   private void checkExistingFiltresMetadata(List<String> longCodesReq)
         throws UnknownFiltresMetadataEx {
      // Traces debug - entrée méthode
      String prefixeTrc = "checkExistingFiltresMetadata()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
      // Vérification de l'existance des codes dans le référentiel des
      // métadonnées.
      LOG.debug(
            "{} - Début de la vérification : Les métadonnées utilisées dans les filtres existent dans le référentiel des métadonnées",
            prefixeTrc);
      if (!metadataCS.checkExistingQueryTerms(longCodesReq).isEmpty()) {
         List<String> filtresMetadataErrors = new ArrayList<String>();
         for (MetadataError metadataError : metadataCS
               .checkExistingQueryTerms(longCodesReq)) {
            filtresMetadataErrors.add(metadataError.getLongCode());
         }
         LOG.debug("{} - {}", prefixeTrc, ResourceMessagesUtils.loadMessage(
               "search.notexist.filtres.metadata.error",
               FormatUtils.formattingDisplayList(filtresMetadataErrors)));
         throw new UnknownFiltresMetadataEx(ResourceMessagesUtils.loadMessage(
               "search.notexist.filtres.metadata.error",
               FormatUtils.formattingDisplayList(filtresMetadataErrors)));
      }
      LOG.debug(
            "{} - Fin de la vérification : Les métadonnées utilisées dans les filtres existent dans le référentiel des métadonnées",
            prefixeTrc);
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
      LOG.debug(
            "{} - Début de la vérification : Les métadonnées utilisées dans la requête de recherche existent dans le référentiel des métadonnées",
            prefixeTrc);
      if (!metadataCS.checkExistingQueryTerms(longCodesReq).isEmpty()) {
         List<String> luceneMetadataErrors = new ArrayList<String>();
         for (MetadataError metadataError : metadataCS
               .checkExistingQueryTerms(longCodesReq)) {
            luceneMetadataErrors.add(metadataError.getLongCode());
         }
         LOG.debug("{} - {}", prefixeTrc, ResourceMessagesUtils.loadMessage(
               "search.notexist.lucene.metadata.error",
               FormatUtils.formattingDisplayList(luceneMetadataErrors)));
         throw new UnknownLuceneMetadataEx(ResourceMessagesUtils.loadMessage(
               "search.notexist.lucene.metadata.error",
               FormatUtils.formattingDisplayList(luceneMetadataErrors)));
      }
      LOG.debug(
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
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.referentiel.error"),
               except);
      }
      return listCodCourtConsult;
   }

   /**
    * Recupération des codes courts et des codes longs pour la recherche
    * 
    * @throws ReferentialException
    * @throws SAESearchServiceEx
    */
   private List<SAEMetadata> recupererListDefaultMetadatas()
         throws SAESearchServiceEx {
      // si liste metadonnées désirée est vide alors recup la liste par default
      // des métadonnées consultables
      SAEMetadata saeM = null;
      List<SAEMetadata> listCodCourtConsult = null;
      try {
         listCodCourtConsult = new ArrayList<SAEMetadata>();
         Map<String, MetadataReference> mapConsult = metaRefD
               .getDefaultConsultableMetadataReferences();
         // parcours de la map pour recuperer tous les codes courts des
         // MetaDataReference
         for (Map.Entry<String, MetadataReference> metaDataRef : mapConsult
               .entrySet()) {
            saeM = new SAEMetadata();
            saeM.setLongCode(metaDataRef.getValue().getLongCode());
            saeM.setShortCode(metaDataRef.getValue().getShortCode());
            listCodCourtConsult.add(saeM);
         }
      } catch (ReferentialException except) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.referentiel.error"),
               except);
      }
      return listCodCourtConsult;
   }

   /**
    * Recupere la liste des metadonnees demandees sans les doublons.
    * 
    * @param listMetaDesired
    *           liste des metadonnees demandees
    * @return List<String> liste des metadonnees sans doublon
    */
   private List<String> getUniqueMetadata(List<String> listMetaDesired) {
      List<String> listUniqueMetaDesired;
      // On supprime les eventuels doublon de la liste des metadonnees demandees
      if (listMetaDesired != null && !listMetaDesired.isEmpty()) {
         // let set supprime les doublons
         Set<String> setUniqueMeta = new HashSet<String>(listMetaDesired);
         // retransforme le set en liste
         listUniqueMetaDesired = new ArrayList<String>(setUniqueMeta);
      } else {
         listUniqueMetaDesired = new ArrayList<String>();
      }
      return listUniqueMetaDesired;
   }

   /**
    * Recherche Une liste de type {@link StorageDocument} à partir d'une requête
    * Lucene,
    * 
    * @param luceneQuery
    *           : Requête Lucene.
    * @param maxResult
    *           : le nombre max de résultat à retourner.
    * @param listeDesiredMetadata
    *           : Liste des métadonnées souhaitées.
    * @return Une liste de type {@link StorageDocument}
    * @throws SAESearchServiceEx
    *            : Une exception de type {@link SAESearchServiceEx}
    * @throws QueryParseServiceEx
    *            : Une exception de type {@link QueryParseServiceEx}
    * @throws QueryParseServiceEx
    *            : Une exception de type {@link QueryParseServiceEx}
    * @throws ConnectionServiceEx
    *            : Une exception de type {@link ConnectionServiceEx}
    */
   private List<StorageDocument> searchStorageDocuments(String luceneQuery,
         int maxResult, List<SAEMetadata> listeDesiredMetadata)
         throws SAESearchServiceEx, QueryParseServiceEx {

      List<StorageDocument> allStorageDocuments = null;
      try {
         LuceneCriteria luceneCriteria = buildService
               .buildStorageLuceneCriteria(luceneQuery, maxResult,
                     listeDesiredMetadata);

         getStorageServiceProvider().openConnexion();

         StorageDocuments storageDocuments = getStorageServiceProvider()
               .getStorageDocumentService()
               .searchStorageDocumentByLuceneCriteria(luceneCriteria);
         allStorageDocuments = storageDocuments.getAllStorageDocuments();
      } catch (ConnectionServiceEx except) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.connection.error"),
               except);
      } catch (SearchingServiceEx except) {
         throw new SAESearchServiceEx(except.getMessage(), except);
      }
      return allStorageDocuments;
   }

   /**
    * Construit une chaîne qui comprends l'ensemble des objets à afficher dans
    * les logs. <br>
    * Exemple : "UntypedMetadata[code long:=Titre,value=Attestation],
    * UntypedMetadata[code long:=DateCreation,value=2011-09-01],
    * UntypedMetadata[code long:=ApplicationProductrice,value=ADELAIDE]"
    * 
    * @param <T>
    *           le type d'objet
    * @param list
    *           : liste des objets à afficher.
    * @return Une chaîne qui représente l'ensemble des objets à afficher.
    */
   private <T> String buildMessageFromList(Collection<T> list) {
      final ToStringBuilder toStrBuilder = new ToStringBuilder(this,
            ToStringStyle.SIMPLE_STYLE);
      for (T o : list) {
         toStrBuilder.append(o.toString());
      }
      return toStrBuilder.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<UntypedDocument> search(String requete,
         List<String> listMetaDesired, int maxResult)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownDesiredMetadataEx,
         UnknownLuceneMetadataEx, SyntaxLuceneEx, SAESearchServiceEx {

      // Traces debug - entrée méthode
      String prefixeTrc = "search()";
      LOG.debug("{} - Début", prefixeTrc);
      // Fin des traces debug - entrée méthode
      LOG.debug(
            "{} - Requête de recherche envoyée par l'application cliente : {}",
            prefixeTrc, requete);
      LOG.debug(
            "{} - Liste des métadonnées souhaitées envoyée par l'application cliente : {}",
            prefixeTrc, StringUtils
                  .isEmpty(buildMessageFromList(listMetaDesired)) ? "Vide"
                  : buildMessageFromList(listMetaDesired));

      // Trim la requête de recherche
      String requeteTrim = SAESearchUtil.trimRequeteClient(requete);
      LOG.debug("{} - Requête de recherche après trim : {}", prefixeTrc,
            requeteTrim);

      boolean isFromRefrentiel = false;
      // liste de résultats à envoyer
      List<UntypedDocument> listUntypedDocument = new ArrayList<UntypedDocument>();

      // gestion des droits
      LOG.debug("{} - Récupération des droits", prefixeTrc);
      AuthenticationToken token = (AuthenticationToken) AuthenticationContext
            .getAuthenticationToken();
      List<SaePrmd> prmds = token.getSaeDroits().get("recherche");
      LOG.debug("{} - Ajustage de la requete avec les éléments des droits",
            prefixeTrc);
      requeteTrim = prmdService.createLucene(requeteTrim, prmds);

      // conversion code court
      List<SAEMetadata> listCodCourt;
      List<SAEMetadata> listCodCourtConsult;

      try {

         // Vérifie globalement la syntaxe de la requête LUCENE
         SAESearchUtil.verifieSyntaxeLucene(requeteTrim);
         String requeteFinal = requeteTrim;

         // Conversion de la requête avec les codes long en code court
         SAESearchQueryParserResult parserResult = queryParseService
               .convertFromLongToShortCode(requeteFinal);
         requeteFinal = parserResult.getRequeteCodeCourts();
         LOG.debug(
               "{} - Requête de recherche après remplacement des codes longs par les codes courts : {}",
               prefixeTrc, requeteFinal);

         List<String> longCodesReq = new ArrayList<String>(parserResult
               .getMetaUtilisees().keySet());
         checkExistingLuceneMetadata(longCodesReq);

         // On supprime les eventuels doublon de la liste des metadonnees
         // demandees
         List<String> listUniqueMetaDesired = getUniqueMetadata(listMetaDesired);

         // Vérifie que les métadonnées demandées dans les résultats de
         // recherche
         // existent dans le référentiel des métadonnées
         checkExistingMetadataDesired(listUniqueMetaDesired);

         listCodCourt = recupererListCodCourtByLongCode(longCodesReq);
         checkSearchableLuceneMetadata(listCodCourt);
         if (listUniqueMetaDesired.isEmpty()) {
            listCodCourtConsult = recupererListDefaultMetadatas();
            isFromRefrentiel = true;
         } else {
            listCodCourtConsult = recupererListCodCourtByLongCode(listUniqueMetaDesired);
         }
         checkConsultableDesiredMetadata(listCodCourtConsult, isFromRefrentiel);
         LOG.debug(
               "{} - Début de la vérification DFCE: La requête de recherche est syntaxiquement correcte",
               prefixeTrc);
         List<StorageDocument> listStorageDocument = searchStorageDocuments(
               requeteFinal, maxResult, listCodCourtConsult);
         LOG.debug(
               "{} - Fin de la vérification DFCE: La requête de recherche est syntaxiquement correcte",
               prefixeTrc);
         LOG.debug(
               "{} - Le nombre de résultats de recherche renvoyé par le moteur de recherche est {}",
               prefixeTrc, listStorageDocument == null ? 0
                     : listStorageDocument.size());
         for (StorageDocument storageDocument : Utils
               .nullSafeIterable(listStorageDocument)) {
            listUntypedDocument.add(mappingDocumentService
                  .storageDocumentToUntypedDocument(storageDocument));
         }
         // A activer si besoin pour afficher la liste des résultats
         // LOG.debug("{} - Liste des résultats : \"{}\"",
         // prefixeTrc,buildMessageFromList(listUntypedDocument));

      } catch (SAESearchQueryParseException except) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.parse.error"), except);
      } catch (NumberFormatException except) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("max.lucene.results.required"),
               except);
      } catch (InvalidSAETypeException except) {
         throw new SAESearchServiceEx(except.getMessage(), except);
      } catch (MappingFromReferentialException except) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.mapping.error"),
               except);
      } catch (QueryParseServiceEx except) {
         throw new SyntaxLuceneEx(
               ResourceMessagesUtils.loadMessage("search.syntax.lucene.error"),
               except);
      }
      LOG.debug("{} - Sortie", prefixeTrc);
      return listUntypedDocument;

   }

   /**
    * {@inheritDoc}
    * 
    * @throws UnknownFiltresMetadataEx
    * 
    */
   @Override
   public final PaginatedUntypedDocuments searchPaginated(
         List<UntypedMetadata> fixedMetadatas,
         UntypedRangeMetadata varyingMetadata,
         List<AbstractMetadata> listeFiltreEgal,
         List<AbstractMetadata> listeFiltreDifferent, int nbDocumentsParPage,
         UUID lastIdDoc, List<String> listeDesiredMetadata)
         throws MetaDataUnauthorizedToSearchEx,
         MetaDataUnauthorizedToConsultEx, UnknownLuceneMetadataEx,
         SAESearchServiceEx, SyntaxLuceneEx, UnknownDesiredMetadataEx,
         UnknownFiltresMetadataEx, DoublonFiltresMetadataEx {

      PaginatedUntypedDocuments pagUntypedDoc = new PaginatedUntypedDocuments();
      try {

         // Traces debug - entrée méthode
         String prefixeTrc = "PaginatedUntypedDocuments()";
         LOG.debug("{} - Début", prefixeTrc);
         // Fin des traces debug - entrée méthode

         LOG.debug(
               "{} - Liste des métadonnées fixes d'un index composite : {}",
               prefixeTrc, StringUtils
                     .isEmpty(buildMessageFromList(fixedMetadatas)) ? "Vide"
                     : buildMessageFromList(fixedMetadatas));
         LOG.debug("{} - Métadonnée variable : {}", prefixeTrc,
               varyingMetadata.toString());
         LOG.debug(
               "{} - Filtres de type \"égal à\" : {}",
               prefixeTrc,
               StringUtils.isEmpty(buildMessageFromList(listeFiltreEgal)) ? "Vide"
                     : buildMessageFromList(listeFiltreEgal));
         LOG.debug(
               "{} - Filtres de type \"différent de\" : {}",
               prefixeTrc,
               StringUtils.isEmpty(buildMessageFromList(listeFiltreDifferent)) ? "Vide"
                     : buildMessageFromList(listeFiltreDifferent));
         LOG.debug("{} - Nombre de document à récupérer : {}", prefixeTrc,
               nbDocumentsParPage);
         LOG.debug(
               "{} - Identifiant du dernier document renvoyé par la recherche par iterateur précédente : {}",
               prefixeTrc, lastIdDoc);
         LOG.debug(
               "{} - Liste des métadonnées souhaitées envoyée par l'application cliente : {}",
               prefixeTrc,
               StringUtils.isEmpty(buildMessageFromList(listeDesiredMetadata)) ? "Vide"
                     : buildMessageFromList(listeDesiredMetadata));

         // Construction de la requête Lucène
         String requeteLucene = constructionReqLucene(fixedMetadatas,
               varyingMetadata);

         // Vérification de la requête lucène
         SAESearchUtil.verifieSyntaxeLucene(requeteLucene);

         // Conversion de la requête avec les codes long en code court
         SAESearchQueryParserResult parserResult = queryParseService
               .convertFromLongToShortCode(requeteLucene);
         String requeteFinal = parserResult.getRequeteCodeCourts();
         LOG.debug(
               "{} - Requête de recherche après remplacement des codes longs par les codes courts : {}",
               prefixeTrc, requeteFinal);

         // Vérification que les métadonnées sont recherchables
         List<String> longCodesReq = new ArrayList<String>(parserResult
               .getMetaUtilisees().keySet());
         checkExistingLuceneMetadata(longCodesReq);
         List<SAEMetadata> listCodCourt = recupererListCodCourtByLongCode(longCodesReq);
         checkSearchableLuceneMetadata(listCodCourt);

         // On supprime les eventuels doublon de la liste des metadonnees
         // demandees
         List<String> listUniqueMetaDesired = getUniqueMetadata(listeDesiredMetadata);

         // Vérifie que les métadonnées demandées dans les résultats de
         // recherche existent dans le référentiel des métadonnées
         checkExistingMetadataDesired(listUniqueMetaDesired);
         boolean isFromRefrentiel = false;
         List<SAEMetadata> listCodCourtConsult;
         if (listUniqueMetaDesired.isEmpty()) {
            listCodCourtConsult = recupererListDefaultMetadatas();
            isFromRefrentiel = true;
         } else {
            listCodCourtConsult = recupererListCodCourtByLongCode(listUniqueMetaDesired);
         }
         checkConsultableDesiredMetadata(listCodCourtConsult, isFromRefrentiel);

         // Vérification existence des métadonnées des filtres
         List<String> codeLongFiltresEgal = new ArrayList<String>();
         for (AbstractMetadata meta : listeFiltreEgal) {
            codeLongFiltresEgal.add(meta.getLongCode());
         }
         checkExistingFiltresMetadata(codeLongFiltresEgal);

         List<String> codeLongFiltresDifferent = new ArrayList<String>();
         for (AbstractMetadata meta : listeFiltreDifferent) {
            codeLongFiltresDifferent.add(meta.getLongCode());
         }
         checkExistingFiltresMetadata(codeLongFiltresDifferent);

         checkMetadataDoublon(codeLongFiltresEgal, codeLongFiltresDifferent);

         // Création de la liste des filtres
         List<AbstractFilter> abstractFilter = creationListeFiltres(
               listeFiltreEgal, listeFiltreDifferent);

         String codeCourtVaryingMeta = metaRefD.getByLongCode(
               varyingMetadata.getLongCode()).getShortCode();

         PaginatedStorageDocuments psd = searchPaginatedStorageDocuments(
               requeteFinal, nbDocumentsParPage, abstractFilter, lastIdDoc,
               listCodCourtConsult, codeCourtVaryingMeta);

         // liste de résultats à envoyer
         List<UntypedDocument> listUntypedDocument = new ArrayList<UntypedDocument>();
         List<StorageDocument> listStorageDocument = psd
               .getAllStorageDocuments();
         for (StorageDocument storageDocument : Utils
               .nullSafeIterable(listStorageDocument)) {
            listUntypedDocument.add(mappingDocumentService
                  .storageDocumentToUntypedDocument(storageDocument));
         }

         pagUntypedDoc.setDocuments(listUntypedDocument);
         pagUntypedDoc.setLastPage(psd.getLastPage());
         pagUntypedDoc.setValeurMetaLastPage(psd.getValeurMetaLastPage());

      } catch (SAESearchQueryParseException e) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.parse.error"), e);
      } catch (QueryParseServiceEx e) {
         throw new SyntaxLuceneEx(
               ResourceMessagesUtils.loadMessage("search.syntax.lucene.error"),
               e);
      } catch (InvalidSAETypeException e) {
         throw new SAESearchServiceEx(e.getMessage(), e);
      } catch (MappingFromReferentialException e) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.mapping.error"), e);
      } catch (ReferentialException e) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.referentiel.error"), e);
      }

      return pagUntypedDoc;
   }

   /**
    * Vérification qu'il n'y a pas de doublon dans les métadonnées
    * 
    * @param codeLongFiltresEgal
    * @param codeLongFiltresDifferent
    * @throws DoublonFiltresMetadataEx
    */
   private void checkMetadataDoublon(List<String> codeLongFiltresEgal,
         List<String> codeLongFiltresDifferent) throws DoublonFiltresMetadataEx {
      List<String> codeLongFiltres = new ArrayList<String>();
      codeLongFiltres.addAll(codeLongFiltresEgal);
      codeLongFiltres.addAll(codeLongFiltresDifferent);

      Map<String, Integer> comptage = new HashMap<String, Integer>();

      for (String codeLong : Utils.nullSafeIterable(codeLongFiltres)) {

         if (comptage.get(codeLong) == null) {
            comptage.put(codeLong, 1);
         } else {
            comptage.put(codeLong, comptage.get(codeLong) + 1);
         }

      }

      List<String> doublonMetadataErrors = new ArrayList<String>();
      for (String cle : comptage.keySet()) {
         if (comptage.get(cle) > 1) {
            doublonMetadataErrors.add(cle);
         }
      }

      if (!doublonMetadataErrors.isEmpty()) {
         throw new DoublonFiltresMetadataEx(ResourceMessagesUtils.loadMessage(
               "search.doublon.filtre.error",
               FormatUtils.formattingDisplayList(doublonMetadataErrors)));
      }

   }

   /**
    * Création de la liste des filtres pour la recherche paginée
    * 
    * @param listeFiltreEgal
    *           Les filtres de type "égal à" passés en paramètres
    * @param listeFiltreDifferent
    *           Les filtres de type "différent de" passés en paramètres
    * @return La liste des filtres
    * @throws ReferentialException
    */
   private List<AbstractFilter> creationListeFiltres(
         List<AbstractMetadata> listeFiltreEgal,
         List<AbstractMetadata> listeFiltreDifferent)
         throws ReferentialException {
      List<AbstractFilter> abstractFilter = new ArrayList<AbstractFilter>();
      for (AbstractMetadata filter : listeFiltreEgal) {
         String shortCode = mrdao.getByLongCode(filter.getLongCode())
               .getShortCode();
         if (filter instanceof UntypedMetadata) {
            ValueFilter valueFilter = new ValueFilter(shortCode,
                  filter.getLongCode(), ((UntypedMetadata) filter).getValue());
            abstractFilter.add(valueFilter);
         } else if (filter instanceof UntypedRangeMetadata) {
            RangeFilter rangeFilter = new RangeFilter(shortCode,
                  filter.getLongCode(),
                  ((UntypedRangeMetadata) filter).getValeurMin(),
                  ((UntypedRangeMetadata) filter).getValeurMax());
            abstractFilter.add(rangeFilter);
         }
      }
      for (AbstractMetadata filter : listeFiltreDifferent) {
         String shortCode = mrdao.getByLongCode(filter.getLongCode())
               .getShortCode();
         if (filter instanceof UntypedMetadata) {
            NotValueFilter notValueFilter = new NotValueFilter(shortCode,
                  filter.getLongCode(), ((UntypedMetadata) filter).getValue());
            abstractFilter.add(notValueFilter);
         } else if (filter instanceof UntypedRangeMetadata) {
            NotRangeFilter notRangeFilter = new NotRangeFilter(shortCode,
                  filter.getLongCode(),
                  ((UntypedRangeMetadata) filter).getValeurMin(),
                  ((UntypedRangeMetadata) filter).getValeurMax());
            abstractFilter.add(notRangeFilter);
         }
      }
      return abstractFilter;
   }

   /**
    * Construction de la requête Lucène pour les recherches paginées
    * 
    * @param fixedMetadatas
    *           Métadonnées fixes (peut être null)
    * @param varyingMetadata
    *           Métadonnées variables
    * @return
    */
   private String constructionReqLucene(List<UntypedMetadata> fixedMetadatas,
         UntypedRangeMetadata varyingMetadata) {
      String requeteLucene = "";
      int nbMetaFixes = fixedMetadatas.size();
      int compteur = 0;

      if (fixedMetadatas != null) {
         // On boucle sur les méta fixes et on sépare les couples code/valeur
         // par des AND
         for (UntypedMetadata metaFixe : fixedMetadatas) {
            requeteLucene = requeteLucene.concat(metaFixe.getLongCode()
                  .concat(":").concat("\"").concat(metaFixe.getValue())
                  .concat("\""));
            if (compteur < nbMetaFixes - 1) {
               requeteLucene = requeteLucene.concat(" AND ");
            }
            compteur++;
         }
      }

      if (varyingMetadata != null) {
         // On ajoute la partie variable
         if (!StringUtils.isEmpty(requeteLucene)) {
            requeteLucene = requeteLucene.concat(" AND ");
         }
         requeteLucene = requeteLucene.concat(varyingMetadata.getLongCode())
               .concat(":[").concat(varyingMetadata.getValeurMin())
               .concat(" TO ").concat(varyingMetadata.getValeurMax())
               .concat("]");
      }
      return requeteLucene;

   }

   /**
    * Recherche Une liste de type {@link StorageDocument} à partir d'une requête
    * Lucene,
    * 
    * @param luceneQuery
    *           : Requête Lucene.
    * @param maxResult
    *           : le nombre max de résultat à retourner.
    * @param listeDesiredMetadata
    *           : Liste des métadonnées souhaitées.
    * @return Une liste de type {@link StorageDocument}
    * @throws SAESearchServiceEx
    *            : Une exception de type {@link SAESearchServiceEx}
    * @throws QueryParseServiceEx
    *            : Une exception de type {@link QueryParseServiceEx}
    * @throws QueryParseServiceEx
    *            : Une exception de type {@link QueryParseServiceEx}
    * @throws ConnectionServiceEx
    *            : Une exception de type {@link ConnectionServiceEx}
    */
   private PaginatedStorageDocuments searchPaginatedStorageDocuments(
         String requeteLucene, int nbDocumentsParPage,
         List<AbstractFilter> abstractFilter, UUID lastIdDoc,
         List<SAEMetadata> listeDesiredMetadata, String codeCourtVaryingMeta)
         throws SAESearchServiceEx, QueryParseServiceEx {

      PaginatedStorageDocuments paginatedStorageDocuments = null;
      try {
         PaginatedLuceneCriteria paginatedLuceneCriteria = buildService
               .buildStoragePaginatedLuceneCriteria(requeteLucene,
                     nbDocumentsParPage, listeDesiredMetadata, abstractFilter,
                     lastIdDoc, codeCourtVaryingMeta);

         getStorageServiceProvider().openConnexion();

         paginatedStorageDocuments = getStorageServiceProvider()
               .getStorageDocumentService().searchPaginatedStorageDocuments(
                     paginatedLuceneCriteria);

      } catch (ConnectionServiceEx except) {
         throw new SAESearchServiceEx(
               ResourceMessagesUtils.loadMessage("search.connection.error"),
               except);
      } catch (SearchingServiceEx except) {
         throw new SAESearchServiceEx(except.getMessage(), except);
      }
      return paginatedStorageDocuments;
   }

}
