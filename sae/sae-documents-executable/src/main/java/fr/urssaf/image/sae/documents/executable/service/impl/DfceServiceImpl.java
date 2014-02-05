package fr.urssaf.image.sae.documents.executable.service.impl;

import java.io.InputStream;
import java.util.Iterator;

import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;
import net.docubase.toolkit.service.ServiceProvider;
import net.docubase.toolkit.service.ged.SearchService;
import net.docubase.toolkit.service.ged.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
      this.serviceProvider = getDfceConnectionService().openConnection();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void fermerConnexion() {
      getServiceProvider().disconnect();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Iterator<Document> executerRequete(final String requeteLucene) {
      final SearchService searchService = getServiceProvider()
            .getSearchService();
      final Base base = getServiceProvider().getBaseAdministrationService()
            .getBase(getDfceConnection().getBaseName());
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      return searchService.createDocumentIterator(searchQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final InputStream recupererContenu(final Document document) {
      final StoreService storeService = getServiceProvider().getStoreService();
      return storeService.getDocumentFile(document);
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
    * Permet de récupérer le provider de service de DFCE.
    * 
    * @return ServiceProvider
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
