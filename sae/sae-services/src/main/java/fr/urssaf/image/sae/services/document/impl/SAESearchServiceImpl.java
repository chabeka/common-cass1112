package fr.urssaf.image.sae.services.document.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.bo.model.MetadataError;
import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.building.services.BuildService;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.mapping.services.MappingDocumentService;
import fr.urssaf.image.sae.metadata.control.services.MetadataControlServices;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.metadata.referential.model.MetadataReference;
import fr.urssaf.image.sae.metadata.referential.services.MetadataReferenceDAO;
import fr.urssaf.image.sae.services.document.SAESearchService;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToConsultEx;
import fr.urssaf.image.sae.services.exception.search.MetaDataUnauthorizedToSearchEx;
import fr.urssaf.image.sae.services.exception.search.SAESearchServiceEx;
import fr.urssaf.image.sae.services.exception.search.SyntaxLuceneEx;
import fr.urssaf.image.sae.services.exception.search.UnknownDesiredMetadataEx;
import fr.urssaf.image.sae.services.exception.search.UnknownLuceneMetadataEx;
import fr.urssaf.image.sae.services.messages.ServiceMessageHandler;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;
import fr.urssaf.image.sae.storage.dfce.utils.Utils;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.LuceneCriteria;

/**
 * Fournit l'implémentation des services pour la recherche.<BR />
 * 
 * @author lbaadj.
 */
@Service
@Qualifier("saeSearchService")
@SuppressWarnings( { "PMD.LongVariable", "PMD.ExcessiveImports" })
public class SAESearchServiceImpl extends AbstractSAEServices implements
      SAESearchService {

   @Autowired
   @Qualifier("metadataReferenceDAO")
   private MetadataReferenceDAO metaRefD;

   @Autowired
   @Qualifier("metadataControlServices")
   private MetadataControlServices metadataCS;

   @Autowired
   @Qualifier("buildService")
   private BuildService buildService;

   @Autowired
   @Qualifier("mappingDocumentService")
   private MappingDocumentService mappingDocumentService;

   private boolean checkLuceneQuery = false; // pour la verification
   private boolean isFromRefrentiel = false; // pour savoir si la valeur vient
   // du referentiel
   private static final String SPLIT = "(\\w+)\\s*[:>]";

   private final List<String> listLongCodeDesired = new ArrayList<String>();
   private List<SAEMetadata> listCodCourtConsult = new ArrayList<SAEMetadata>();

   /**
    * {@inheritDoc}
    */
   public final List<UntypedDocument> search(String requete,
         List<String> listMetaDesired) throws SAESearchServiceEx,
         MetaDataUnauthorizedToSearchEx, MetaDataUnauthorizedToConsultEx,
         UnknownDesiredMetadataEx, UnknownLuceneMetadataEx, SyntaxLuceneEx {
      // liste de résultats à envoyer
      List<UntypedDocument> listUntypedDocument = new ArrayList<UntypedDocument>();
      // conversion code court
      List<SAEMetadata> listCodCourt = new ArrayList<SAEMetadata>();
      try {
         List<String> longCodesReq = extractLongCodeFromQuery(requete);
         checkExistingLuceneMetadata(longCodesReq);
         String requeteFinal = requete;
         // Map pour l'association codeCourt codeLong - Remplacement des codes
         // long par les codes court.
         Map<String, String> map = longCodeToShortCode(longCodesReq);
         for (Map.Entry<String, String> e : map.entrySet()) {
            requeteFinal = requeteFinal.replace(e.getValue(), e.getKey());
         }
         String requeteVerif = requeteFinal;
         // parcours de la map et remplissement d'une liste afin de verifier
         // plus tard si la liste
         // des codes courts est rechercheable : map <codeCourt, codeLong>
         SAEMetadata saeM =null;
         for (Map.Entry<String, String> e : map.entrySet()) {
            saeM = new SAEMetadata();
            requeteVerif = requeteVerif.replace(e.getKey(), e.getValue());
            saeM.setLongCode(e.getValue());
            saeM.setShortCode(e.getKey());
            // listCodCourt.add(new SAEMetadata(e.getValue(), e.getKey()));
            listCodCourt.add(saeM);
         }
         checkConversion(requete, requeteVerif);
         if (checkLuceneQuery) {
            checkSearchableLuceneMetadata(listCodCourt);
            checkExistingMetadataDesired(listMetaDesired);
            recupererListCodCourtDefault(listMetaDesired);
            checkConsultableMetadataDesired(listCodCourtConsult,
                  isFromRefrentiel);
            List<StorageDocument> listStorageDocument = searchStorageDocuments(
                  requeteFinal, Integer.parseInt(ServiceMessageHandler
                        .getMessage("max.lucene.results")) + 1,
                  listCodCourtConsult);
            for (StorageDocument storageDocument : Utils
                  .nullSafeIterable(listStorageDocument)) {
               listUntypedDocument.add(mappingDocumentService
                     .storageDocumentToUntypedDocument(storageDocument));
            }
         }
      } catch (NumberFormatException except) {
         throw new SAESearchServiceEx(ResourceMessagesUtils
               .loadMessage("max.lucene.results.required"), except);
      } catch (InvalidSAETypeException except) {
         throw new SAESearchServiceEx(except.getMessage(), except);
      } catch (MappingFromReferentialException except) {
         throw new SAESearchServiceEx(ResourceMessagesUtils
               .loadMessage("search.mapping.error"), except);
      }catch (QueryParseServiceEx except) {
         throw new SyntaxLuceneEx(ResourceMessagesUtils
               .loadMessage("search.syntax.lucene.error"), except);
      }
      return listUntypedDocument;
   }

   /**
    * @param listCodCourtConsult
    * @param isFromRefrentiel
    * @throws MetaDataUnauthorizedToConsultEx
    */
   private void checkConsultableMetadataDesired(
         List<SAEMetadata> listCodCourtConsult, boolean isFromRefrentiel)
         throws MetaDataUnauthorizedToConsultEx {
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
         throw new MetaDataUnauthorizedToConsultEx(ResourceMessagesUtils
               .loadMessage("search.notconsult.error", consultMetadataErrors));
      }
   }

   /**
    * verification de la conversion la requete verif ainsi remplacée doit être
    * la même que la requête dedépart.
    * 
    * 
    * @throws SAESearchServiceEx
    */
   private void checkConversion(String requete, String requeteVerif)
         throws SAESearchServiceEx {
      // la requete verif ainsi remplacée doit être la même que la requête de
      // départ.
      if (requete.equals(requeteVerif)) {
         checkLuceneQuery = true;
      }
      if (!checkLuceneQuery) {
         throw new SAESearchServiceEx("search.analyse.lucene.error");
      }
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
      // verification que la liste des codes courts est de type recherchable
      if (!metadataCS.checkSearchableMetadata(listCodCourt).isEmpty()) {
         List<String> searchableMetadataErrors = new ArrayList<String>();
         for (MetadataError searchableMetadataError : Utils
               .nullSafeIterable(metadataCS
                     .checkSearchableMetadata(listCodCourt))) {
            searchableMetadataErrors.add(searchableMetadataError.getLongCode());
         }
         throw new MetaDataUnauthorizedToSearchEx(ResourceMessagesUtils
               .loadMessage("search.notsearcheable.error",
                     searchableMetadataErrors));
      }
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
      // verification des métadonnées présentent dans la requête de recherche
      // qui
      // n’existent pas dans le référentiel des métadonnées
      if (!metadataCS.checkExistingQueryTerms(listLongCodeDesired).isEmpty()) {
         List<String> listMetaDesiredErrors = new ArrayList<String>();
         for (MetadataError metadataError : metadataCS
               .checkExistingQueryTerms(listLongCodeDesired)) {
            listMetaDesiredErrors.add(metadataError.getLongCode());
         }
         throw new UnknownDesiredMetadataEx(ResourceMessagesUtils.loadMessage(
               "search.notexist.metadata.desired.error", listMetaDesiredErrors));
      }
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
   private void checkExistingLuceneMetadata(List<String> longCodesReq)
         throws UnknownLuceneMetadataEx {
      // Vérification de l'exsitance des codes dans le référentiel des
      // métadonnées.
      if (!metadataCS.checkExistingQueryTerms(longCodesReq).isEmpty()) {
         List<String> luceneMetadataErrors = new ArrayList<String>();
         for (MetadataError metadataError : metadataCS
               .checkExistingQueryTerms(longCodesReq)) {
            luceneMetadataErrors.add(metadataError.getLongCode());
         }
         throw new UnknownLuceneMetadataEx(ResourceMessagesUtils.loadMessage(
               "search.notexist.lucene.metadata.error", luceneMetadataErrors));
      }
   }

   /**
    * Convertit une liste de métadonnées code long en code court.
    * 
    * @param listCodeReq
    *           : Liste de code longs des métadonnées.
    * @return Un tableau de métadonnées <codeCourt, codeLong>
    * @throws ReferentialException
    *            : Une exception de type {@link ReferentialException}
    * @throws SAESearchServiceEx
    */
   private Map<String, String> longCodeToShortCode(List<String> listCodeReq)
         throws SAESearchServiceEx {
      Map<String, String> map = new HashMap<String, String>();
      try {
         for (String codeLong : listCodeReq) {
            // recuperer MetaDataReference qui nous renvoie un objet avec
            // codeCourt
            // et codeLong
            // MetadataReference metadaReference = new MetadataReference();
            if (metaRefD.getByLongCode(codeLong) != null) {
               MetadataReference metadaReference = metaRefD
                     .getByLongCode(codeLong);
               // et ensuite recup le code court
               String codeCourt = metadaReference.getShortCode();
               // ajout dans une Map<String, String>
               map.put(codeCourt, codeLong);
            }
         }
      } catch (ReferentialException except) {
         throw new SAESearchServiceEx(ResourceMessagesUtils
               .loadMessage("search.referentiel.error"), except);
      }
      return map;
   }

   /**
    * Extrait les codes long d'une requête Lucene.
    * 
    * @param requete
    *           : Requête Lucene.
    * @return Une liste de code court des métadonnées.
    * @throws SyntaxLuceneEx
    *            : Une exception de type {@link SyntaxLuceneEx}
    */
   private List<String> extractLongCodeFromQuery(String requete)
         throws SyntaxLuceneEx {
      List<String> listCodeReq = new ArrayList<String>();
      try {
         Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
         QueryParser queryParser = new QueryParser(Version.LUCENE_CURRENT, "",
               analyzer);
         queryParser.parse(requete);
         Pattern patt = Pattern.compile(SPLIT);
         Matcher matcher = patt.matcher(requete);
         while (matcher.find()) {
            // recup que la partie les codes longs.
            listCodeReq.add(matcher.group().replaceAll("\\s*[:>]", ""));
         }
      } catch (PatternSyntaxException except) {
         throw new SyntaxLuceneEx(ResourceMessagesUtils.loadMessage(
               "lucene.syntax.error", SPLIT), except);
      } catch (ParseException except) {
         throw new SyntaxLuceneEx(ResourceMessagesUtils
               .loadMessage("search.syntax.lucene.error"), except);
      }
      return listCodeReq;
   }

   /**
    * Recupération des codes courts et des codes longs pour la recherche
    * 
    * @throws ReferentialException
    * @throws SAESearchServiceEx
    */
   @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
   private void recupererListCodCourtDefault(List<String> listMetaDesired)
         throws SAESearchServiceEx {
      // si liste metadonnées désirée est vide alors recup la liste par default
      // des métadonnées consultables
      SAEMetadata saeM = null;
      try {
         if (listMetaDesired.isEmpty()) {
            listCodCourtConsult = new ArrayList<SAEMetadata>();
            isFromRefrentiel = true;
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
         }
         // si liste metadonnées non vide alors pour chaque code recup la
         // MetaDataReference associée
         for (String codeLong : listMetaDesired) {
            MetadataReference metaDataRef = metaRefD.getByLongCode(codeLong);
            saeM = new SAEMetadata();
            saeM.setLongCode(codeLong);
            saeM.setShortCode(metaDataRef.getShortCode());
            saeM.setValue("");
            listCodCourtConsult.add(saeM);
            listLongCodeDesired.add(codeLong);
         }
      } catch (ReferentialException except) {
         throw new SAESearchServiceEx(ResourceMessagesUtils
               .loadMessage("search.referentiel.error"), except);
      }
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
    * @throws SAESearchServiceEx: Une exception de type {@link SAESearchServiceEx}
    * @throws QueryParseServiceEx: Une exception de type {@link QueryParseServiceEx} 
    * @throws QueryParseServiceEx : Une exception de type {@link QueryParseServiceEx}
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
         throw new SAESearchServiceEx(ResourceMessagesUtils
               .loadMessage("search.connection.error"), except);
      } catch (SearchingServiceEx except) {
         throw new SAESearchServiceEx(except.getMessage(), except);
      }  finally {

         getStorageServiceProvider().closeConnexion();

      }
      return allStorageDocuments;
   }
}
