/**
 * 
 */
package fr.urssaf.image.sae.services.batch.restore.support.stockage.batch;

import java.util.Iterator;
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

import fr.urssaf.image.sae.building.services.BuildService;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.InterruptionTraitementMasseSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.exception.InterruptionTraitementException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.interruption.model.InterruptionTraitementConfig;
import fr.urssaf.image.sae.services.batch.restore.exception.RestoreMasseSearchException;
import fr.urssaf.image.sae.storage.dfce.services.impl.StorageServiceProviderImpl;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.QueryParseServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.PaginatedStorageDocuments;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.searchcriteria.PaginatedLuceneCriteria;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Item reader permettant de récupérer les documents à partir de la requête lucene.
 * 
 */
@Component
@Scope("step")
public class RecycleBeanStorageDocumentReader 
implements ItemReader<StorageDocument> {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(RecycleBeanStorageDocumentReader.class);

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
    * Permet de verifier s'il y a un élément suivant.
    * @return boolean
    * @throws RestoreMasseSearchException 
    */
   private boolean hasNext() throws RestoreMasseSearchException {
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
    * @throws RestoreMasseSearchException 
    */
   private boolean fetchMore() throws RestoreMasseSearchException {
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
                     MAX_PAR_PAGE, null, null,
                     lastIdDoc, "");

         ((StorageServiceProviderImpl) storageServiceProvider)
               .getDfceServicesManager().getConnection();

         paginatedStorageDocuments = storageServiceProvider
               .getStorageDocumentService().searchStorageDocumentsInRecycleBean(
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
         throw new RestoreMasseSearchException(except);
      } catch (SearchingServiceEx except) {
         throw new RestoreMasseSearchException(except);
      } catch (QueryParseServiceEx except) {
         throw new RestoreMasseSearchException(except);
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
    * @throws RestoreMasseSearchException
    */
   private void gererInterruption() throws RestoreMasseSearchException {
      // on vérifie que le traitement ne doit pas s'interrompre
      final DateTime currentDate = new DateTime();

      if (config != null
            && support.hasInterrupted(currentDate, config)) {

         try {
            support.interruption(currentDate, config);
         } catch (InterruptionTraitementException e) {
            throw new RestoreMasseSearchException(e);
         }
      }
   }
}
