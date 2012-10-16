package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.util.List;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;
import net.docubase.toolkit.model.search.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.docubase.dfce.exception.ExceededSearchLimitException;
import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.SearchQueryParseException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.regionalisation.dao.SaeDocumentDao;
import fr.urssaf.image.sae.regionalisation.exception.ErreurTechniqueException;
import fr.urssaf.image.sae.regionalisation.support.ServiceProviderSupport;

/**
 * Classe implémentant le service {@link SaeDocumentDao}
 * 
 * 
 */
@Repository
public class SaeDocumentDaoImpl implements SaeDocumentDao {

   private final ServiceProviderSupport serviceSupport;

   /**
    * 
    * @param serviceSupport
    *           service DFCE
    */
   @Autowired
   public SaeDocumentDaoImpl(ServiceProviderSupport serviceSupport) {
      this.serviceSupport = serviceSupport;

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final List<Document> getDocuments(String lucene) {

      Base base = this.serviceSupport.getBase();

      /*
       * SearchQuery paramSearchQuery = new QueryImpl(lucene, base);
       * SearchResult searchResult;
       * 
       * try {
       * 
       * searchResult = serviceSupport.getSearchService().search(
       * paramSearchQuery);
       * 
       * } catch (ExceededSearchLimitException e) { throw new
       * ErreurTechniqueException(e); } catch (SearchQueryParseException e) {
       * throw new ErreurTechniqueException(e); }
       */

      SearchResult searchResult;
      try {
         searchResult = serviceSupport.getSearchService().search(lucene, 200,
               base);
      } catch (ExceededSearchLimitException e) {
         throw new ErreurTechniqueException(e);
      } catch (SearchQueryParseException e) {
         throw new ErreurTechniqueException(e);
      }

      return searchResult.getDocuments();

   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void update(Document document) {

      try {
         serviceSupport.getStoreService().updateDocument(document);
      } catch (TagControlException e) {
         throw new ErreurTechniqueException(e);
      } catch (FrozenDocumentException e) {
         throw new ErreurTechniqueException(e);
      }

   }

}
