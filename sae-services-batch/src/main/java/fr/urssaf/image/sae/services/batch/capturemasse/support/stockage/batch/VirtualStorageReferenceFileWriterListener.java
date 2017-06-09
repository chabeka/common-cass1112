/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.stockage.batch;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.storage.model.storagedocument.AbstractStorageDocument;
import fr.urssaf.image.sae.storage.model.storagedocument.StorageReferenceFile;
import fr.urssaf.image.sae.storage.services.StorageServiceProvider;

/**
 * Composant permettant de capter les erreurs d'écriture des fichiers de
 * référence
 * 
 */
@Component
public class VirtualStorageReferenceFileWriterListener extends
      AbstractDocumentWriterListener {

   private static final Logger LOGGER = LoggerFactory
         .getLogger(VirtualStorageReferenceFileWriterListener.class);

   @Autowired
   private StorageServiceProvider storageServiceProvider;

   /**
    * Méthode déclenchée lors d'une erreur d'écriture
    * 
    * @param exception
    *           exception levée lors de l'écriture
    * @param references
    *           la liste des fichiers de référence en cours d'écriture
    */
   
   @OnWriteError
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   /*
    * Alerte PMD car nous avons obligation de caster l'erreur afin de
    * pouvoir l'exploiter plus tard
    */
   public final void onWriteError(Exception exception,
         List<StorageReferenceFile> references) {

      String trcPrefix = "onWriteError()";
      LOGGER.debug("{} - erreur lors de l'écriture des données", trcPrefix);

      getCodesErreurListe().add(Constantes.ERR_BUL002);
      getIndexErreurListe().add(
            getStepExecution().getExecutionContext().getInt(
                  Constantes.CTRL_INDEX));
      getExceptionErreurListe().add(new Exception(exception.getMessage()));
   }

   @Override
   public UUID launchTraitement(AbstractStorageDocument storageDocument)
         throws Exception {
      return null;
   }
   
   /**
    * {@inheritDoc}
    */
   @Override
   protected final Logger getLogger() {
      return LOGGER;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final StorageServiceProvider getServiceProvider() {
      return storageServiceProvider;
   }
}
