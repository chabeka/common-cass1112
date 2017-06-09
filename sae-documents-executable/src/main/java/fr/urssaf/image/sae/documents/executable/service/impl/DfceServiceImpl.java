package fr.urssaf.image.sae.documents.executable.service.impl;

import java.io.InputStream;
import java.util.Iterator;
import java.util.UUID;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.RecycleBinService;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEConnectionService;
import fr.urssaf.image.sae.documents.executable.service.DfceService;

/**
 * Classe d'implémentation du service <b>DfceService</b>. Cette classe est un
 * singleton, et est accessible via l'annotation <b>@AutoWired</b>.
 */
@Service
public class DfceServiceImpl implements DfceService {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DfceServiceImpl.class);

   /**
    * Service permettant de réaliser la connexion à DFCE.
    */
   @Autowired
   private DFCEConnectionService dfceConnectionService;

   /**
    * Le provider de service de DFCE.
    */
   private ServiceProvider serviceProvider;

   /**
    * Paramètres de connexion à DFCE.
    */
   @Autowired
   private DFCEConnection dfceConnection;

   /**
    * {@inheritDoc}
    */
   @Override
   public final void ouvrirConnexion() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      this.serviceProvider = getDfceConnectionService().openConnection();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void fermerConnexion() {
      LOGGER.debug("Fermeture de la connexion à DFCE");
      getServiceProvider().disconnect();
   }

   /**
    * {@inheritDoc}
    * 
    * @throws SearchQueryParseException
    */
   @Override
   public final Iterator<Document> executerRequete(final String requeteLucene)
         throws SearchQueryParseException {
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final SearchService searchService = getServiceProvider()
            .getSearchService();
      final Base base = getServiceProvider().getBaseAdministrationService()
            .getBase(getDfceConnection().getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      searchQuery.setSearchLimit(1000);
      return searchService.createDocumentIterator(searchQuery);
   }
   
   /**
    * {@inheritDoc}
    * 
    * @throws SearchQueryParseException
    */
   @Override
   public final Iterator<Document> executerRequeteCorbeille(final String requeteLucene)
         throws SearchQueryParseException {
      LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);
      final RecycleBinService recycleBinService = getServiceProvider().getRecycleBinService();

      final Base base = getServiceProvider().getBaseAdministrationService()
            .getBase(getDfceConnection().getBaseName());
      
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      
      return recycleBinService.createDocumentIterator(searchQuery);
      
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final InputStream recupererContenu(final Document document) {
      LOGGER.debug("Récupération du contenu du document : {}", document
            .getUuid());
      final StoreService storeService = getServiceProvider().getStoreService();
      return storeService.getDocumentFile(document);
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   public final Document getDocumentById(final UUID idDoc) {
      final SearchService searchService = getServiceProvider()
         .getSearchService();
      final Base base = getServiceProvider().getBaseAdministrationService()
         .getBase(getDfceConnection().getBaseName());
      return searchService.getDocumentByUUID(base, idDoc);
   }

   /**
    * Permet de récupérer le service permettant de réaliser la connexion à DFCE.
    * 
    * @return DFCEConnectionService
    */
   public final DFCEConnectionService getDfceConnectionService() {
      return dfceConnectionService;
   }

   /**
    * Permet de modifier le service permettant de réaliser la connexion à DFCE.
    * 
    * @param dfceConnectionService
    *           le service permettant de réaliser la connexion à DFCE
    */
   public final void setDfceConnectionService(
         final DFCEConnectionService dfceConnectionService) {
      this.dfceConnectionService = dfceConnectionService;
   }

   /**
    * {@inheritDoc}
    */
   public final ServiceProvider getServiceProvider() {
      return serviceProvider;
   }

   /**
    * Permet de modifier le provider de service de DFCE.
    * 
    * @param serviceProvider
    *           le provider de service de DFCE
    */
   public final void setServiceProvider(final ServiceProvider serviceProvider) {
      this.serviceProvider = serviceProvider;
   }

   /**
    * Permet de récupérer les paramètres de connexion à DFCE.
    * 
    * @return DFCEConnection
    */
   public final DFCEConnection getDfceConnection() {
      return dfceConnection;
   }

   /**
    * Permet de modifier les paramètres de connexion à DFCE.
    * 
    * @param dfceConnection
    *           paramètres de connexion à DFCE
    */
   public final void setDfceConnection(final DFCEConnection dfceConnection) {
      this.dfceConnection = dfceConnection;
   }
}
