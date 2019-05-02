package fr.urssaf.image.sae.batch.documents.executable.service.impl;

import java.io.InputStream;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.docubase.dfce.exception.SearchQueryParseException;

import fr.urssaf.image.commons.dfce.model.DFCEConnection;
import fr.urssaf.image.commons.dfce.service.DFCEServices;
import fr.urssaf.image.commons.dfce.service.impl.DFCEServicesImpl;
import fr.urssaf.image.sae.batch.documents.executable.exception.ParametreRuntimeException;
import fr.urssaf.image.sae.batch.documents.executable.service.DfceService;
import net.docubase.toolkit.model.ToolkitFactory;
import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchQuery;

/**
 * Classe d'implémentation du service <b>DfceService</b>. Cette classe est un
 * singleton, et est accessible via l'annotation <b>@AutoWired</b>.
 */
public class DfceServiceImpl implements DfceService {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(DfceServiceImpl.class);

   /**
    * La connexion vers les services de DFCE.
    */
   private final DFCEServices dfceServices;


   /**
    * Constructeur
    * @param dfceConn
    */
   public DfceServiceImpl(final DFCEConnection dfceConnexionParameters) {
      this.dfceServices =  new DFCEServicesImpl(dfceConnexionParameters);
   }

   /**
    * {@inheritDoc}
    */
   public final void ouvrirConnexion() {
      LOGGER.debug("Ouverture de la connexion à DFCE");
      dfceServices.connectTheFistTime();
   }

   /**
    * {@inheritDoc}
    */
   public final void fermerConnexion() {
      LOGGER.debug("Fermeture de la connexion à DFCE");
      dfceServices.closeConnexion();
   }

   /**
    * {@inheritDoc}
    *
    * @throws SearchQueryParseException
    */
   public final Iterator<Document> executerRequete(final String requeteLucene)
         throws SearchQueryParseException {
      LOGGER.debug("Exécution de la requête lunèce : {}", requeteLucene);
      final Base base = dfceServices.getBase();
      final SearchQuery searchQuery = ToolkitFactory.getInstance()
            .createMonobaseQuery(requeteLucene, base);
      searchQuery.setSearchLimit(1000);
      return dfceServices.createDocumentIterator(searchQuery);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final InputStream recupererContenu(final Document document) {
      LOGGER.debug("Récupération du contenu du document : {}", document
                   .getUuid());
      return dfceServices.getDocumentFile(document);
   }

   /**
    * {@inheritDoc}
    */
   public final DFCEServices getDfceServices() {
      return dfceServices;
   }

  /**
   * {@inheritDoc}
   */
  @Override
  public final Iterator<Document> executerRequeteCorbeille(final String requeteLucene)
      throws SearchQueryParseException {
   LOGGER.debug("Exécution de la requête lucène : {}", requeteLucene);

   final Base base = dfceServices.getBase();

   final SearchQuery searchQuery = ToolkitFactory.getInstance()
         .createMonobaseQuery(requeteLucene, base);

   return dfceServices.createDocumentIteratorFromRecycleBin(searchQuery);

}

}
