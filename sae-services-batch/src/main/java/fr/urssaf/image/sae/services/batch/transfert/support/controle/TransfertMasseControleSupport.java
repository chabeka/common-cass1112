package fr.urssaf.image.sae.services.batch.transfert.support.controle;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.mapping.exception.InvalidSAETypeException;
import fr.urssaf.image.sae.mapping.exception.MappingFromReferentialException;
import fr.urssaf.image.sae.metadata.exceptions.ReferentialException;
import fr.urssaf.image.sae.services.exception.ArchiveInexistanteEx;
import fr.urssaf.image.sae.services.exception.transfert.ArchiveAlreadyTransferedException;
import fr.urssaf.image.sae.services.exception.transfert.TransfertException;
import fr.urssaf.image.sae.storage.exception.ConnectionServiceEx;
import fr.urssaf.image.sae.storage.exception.SearchingServiceEx;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageDocument;

public interface TransfertMasseControleSupport {

   public boolean controleSAEDocumentSuppression(UntypedDocument item)
         throws SearchingServiceEx, ConnectionServiceEx;
   
   public StorageDocument controleSAEDocumentTransfert(UntypedDocument item)
         throws ReferentialException, SearchingServiceEx,
         ArchiveAlreadyTransferedException, ArchiveInexistanteEx,
         TransfertException, InvalidSAETypeException, MappingFromReferentialException;
}
