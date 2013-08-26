package fr.urssaf.image.sae.regionalisation.dao.impl;

import java.util.UUID;

import net.docubase.toolkit.model.base.Base;
import net.docubase.toolkit.model.document.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.docubase.dfce.exception.FrozenDocumentException;
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
   public final Base getBase() {
      return serviceSupport.getBase();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final Document find(Base base, UUID idDoc) {
      return serviceSupport.getSearchService().getDocumentByUUID(base, idDoc);
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
