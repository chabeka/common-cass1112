/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.impl;

import java.util.UUID;

import net.docubase.toolkit.model.document.Document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docubase.dfce.exception.FrozenDocumentException;
import com.docubase.dfce.exception.TagControlException;

import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocumentDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.support.ServiceProviderSupport;

/**
 * classe d'implémentation de l'interface {@link DocumentDao}
 * 
 */
@Component
public class DocumentDaoImpl implements DocumentDao {

   @Autowired
   private ServiceProviderSupport providerSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public Document getDocument(UUID uuid) {

      return providerSupport.getSearchService().getDocumentByUUID(
            providerSupport.getBase(), uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void updateDocument(Document document) throws DfceException {
      
      try {
         providerSupport.getStoreService().updateDocument(document);
      } catch (TagControlException exception) {
         throw new DfceException(exception);
         
      } catch (FrozenDocumentException exception) {
         throw new DfceException(exception);
      }

   }

}
