/**
 * 
 */
package fr.urssaf.image.sae.services.batch.suppression.support.stockage.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.bo.SAEMetadata;
import fr.urssaf.image.sae.building.services.BuildService;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.suppression.exception.SuppressionMasseSearchException;
import fr.urssaf.image.sae.storage.dfce.model.StorageTechnicalMetadatas;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.filters.AbstractFilter;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item reader permettant de récupérer les documents à partir de la requête lucene.
 * 
 */
@Component
@Scope("step")
public class StorageDocumentReader 
      implements ItemReader<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(StorageDocumentReader.class);
   
   /**
    * Requete lucene récupéré dans le contexte du job d'exécution
    */
   @Value("#{jobExecutionContext['requeteFinale']}")
   private String requeteLucene;
   
   /**
    * Service de build permettant de générer les critères de la recherche paginée.
    */
   @Autowired
   @Qualifier("buildService")
   private BuildService buildService;
   
   /**
    * Service permettant d'exécuter la recherche paginée
    */
   @Autowired
   @Qualifier("storageServiceProvider")
   private StorageServiceProvider storageServiceProvider;
   
   @Autowired
   private InterruptionTraitementMasseSupport support;
   
   @Autowired
   private InterruptionTraitementConfig config;
   
   /**
    * Nombre de documents par page d'itération.
    */
   private final static int MAX_PAR_PAGE = 500;
   
   /**
    * Iterateur de documents.
    */
   private Iterator<StorageDocument> iterateurDoc = null;
   
   /**
    * Flag indiquant s'il s'agit de la dernière itération.
    */
   private boolean lastIteration = false;
   
   /**
    * Dernier identifiant de document remonté à l'itération précédente.
    */
   private UUID lastIdDoc = null;
   
   /**
    * Liste des métadonnées que l'on souhaite récupérés
    */
   private final static List<SAEMetadata> DESIRED_METADATAS = new ArrayList<SAEMetadata>();
   
   /**
    * Initialisation de la liste des métadonnées désirées.
    */
   static {
      // En l'occurrence, on veut savoir si le document est gelé ou non
      SAEMetadata metadataGel = new SAEMetadata(StorageTechnicalMetadatas.GEL.getLongCode(),
            StorageTechnicalMetadatas.GEL.getShortCode(), null);
      DESIRED_METADATAS.add(metadataGel);
   }
   
   /**
    * Permet de verifier s'il y a un élément suivant.
    * @return boolean
    * @throws SuppressionMasseSearchException 
    */
   private boolean hasNext() throws SuppressionMasseSearchException {
      boolean hasNext;
      if (iterateurDoc == null || !iterateurDoc.hasNext()) {
         // dans ce cas, on est soit à la première itération, soit on n'a plus d'éléments dans notre itérateurs 'local'
         // il faut donc aller récupérer des nouveaux éléments si on n'est pas à la dernière itération
         if (lastIteration) {
            hasNext = false;
         } else {
            // avant de faire appel à la base pour récupérer les éléments, on va tester qu'on est pas à l'heure 
            // de l'interruption des serveurs
            gererInterruption();
            // on appelle la récupération des prochains éléments
            hasNext = fetchMore();
         }
      } else {
         // on a encore des éléments dans l'itérateur 'local'
         hasNext = true;
      }
      return hasNext;
   }
   
   /**
    * Recupere le prochain element.
    * @return StorageDocument
    */
   private StorageDocument next() {
      return iterateurDoc.next();
   }
   
   /**
    * Permet de récupérer les prochains éléments.
    * @return boolean
    * @throws SuppressionMasseSearchException 
    */
   private boolean fetchMore() throws SuppressionMasseSearchException {
      PaginatedStorageDocuments paginatedStorageDocuments = null;
      boolean hasMore = false;
      try {
         String strIdDoc;
         if (lastIdDoc == null) {
            strIdDoc = "null";
         } else {
            strIdDoc = lastIdDoc.toString();
         }
         
         LOGGER.debug("fetchMore : {} - {}", requeteLucene, strIdDoc);
         PaginatedLuceneCriteria paginatedLuceneCriteria = buildService
               .buildStoragePaginatedLuceneCriteria(requeteLucene,
                     MAX_PAR_PAGE, DESIRED_METADATAS, new ArrayList<AbstractFilter>(),
                     lastIdDoc, "");

         storageServiceProvider.openConnexion();

         paginatedStorageDocuments = storageServiceProvider
               .getStorageDocumentService().searchPaginatedStorageDocuments(
                     paginatedLuceneCriteria);
         
         // recupere les infos de la requete
         iterateurDoc = paginatedStorageDocuments.getAllStorageDocuments().iterator();
         lastIteration = paginatedStorageDocuments.getLastPage();
         if (!paginatedStorageDocuments.getAllStorageDocuments().isEmpty()) {
            int nbElements = paginatedStorageDocuments.getAllStorageDocuments().size();
            lastIdDoc = paginatedStorageDocuments.getAllStorageDocuments().get(nbElements - 1).getUuid();
            hasMore = true;
         } else {
            hasMore = false;
         }

      } catch (ConnectionServiceEx except) {
         throw new SuppressionMasseSearchException(except);
      } catch (SearchingServiceEx except) {
         throw new SuppressionMasseSearchException(except);
      } catch (QueryParseServiceEx except) {
         throw new SuppressionMasseSearchException(except);
      }
      return hasMore;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public StorageDocument read() throws Exception, UnexpectedInputException,
         ParseException, NonTransientResourceException {
      
      StorageDocument doc = null;
      if (hasNext()) {
         doc = next();
      }
      if (doc != null) {
         LOGGER.debug("Read du document : {}", doc.getUuid().toString());
      }
      return doc;
   }
   
   /**
    * Methode permettant de gerer la plage d'interruption.
    * @throws SuppressionMasseSearchException
    */
   private void gererInterruption() throws SuppressionMasseSearchException {
      // on vérifie que le traitement ne doit pas s'interrompre
      final DateTime currentDate = new DateTime();

      if (config != null
            && support.hasInterrupted(currentDate, config)) {
         
         try {
            support.interruption(currentDate, config);
         } catch (InterruptionTraitementException e) {
            throw new SuppressionMasseSearchException(e);
         }
      }
   }
}
