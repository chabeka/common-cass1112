/**
 * 
 */
package fr.urssaf.image.sae.regionalisation.fond.documentaire.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.docubase.toolkit.model.document.Document;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.dao.DocumentDao;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.exception.DfceException;
import fr.urssaf.image.sae.regionalisation.fond.documentaire.service.DocumentService;

/**
 * Classe d'implémentation de l'interface {@link DocumentService}
 * 
 */
@Component
public class DocumentServiceImpl implements DocumentService {

   @Autowired
   private DocumentDao documentDao;

   /**
    * {@inheritDoc}
    */
   @Override
   public Document getDocument(UUID uuid) {

      return documentDao.getDocument(uuid);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void updateDocument(Document document) throws DfceException {

      documentDao.updateDocument(document);

   }

}
