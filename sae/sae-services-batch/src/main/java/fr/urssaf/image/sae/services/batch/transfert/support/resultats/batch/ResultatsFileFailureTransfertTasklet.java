package fr.urssaf.image.sae.services.batch.transfert.support.resultats.batch;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.transfert.support.resultats.ResultatsFileEchecTransfertSupport;
import fr.urssaf.image.sae.services.batch.transfert.support.stockage.multithreading.TransfertPoolThreadExecutor;

/**
 * Tasklet pour l'écriture du fichier resultats.xml lors d'un échec de
 * traitement de transfert de masse
 * 
 */
@Component
public class ResultatsFileFailureTransfertTasklet extends
      AbstractResultatsFileFailureTransfertTasklet {

   /**
    * Support en cas d'echec du transfert de masse
    */
   @Autowired
   private ResultatsFileEchecTransfertSupport support;

   /**
    * Support pour la validation du sommaire
    */
   @Autowired
   private SommaireFormatValidationSupport validationSupport;

   /**
    * Support pour la validation des xsd
    */
   @Autowired
   private XsdValidationSupport xsdValidationSupport;

   /**
    * Pool d'execution des insertions de documents
    */
   @Autowired
   private TransfertPoolThreadExecutor executor;

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ConcurrentLinkedQueue<?> getIntegratedDocuments() {
      return executor.getIntegratedDocuments();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final ResultatsFileEchecTransfertSupport getResultatsFileEchecSupport() {
      return support;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final SommaireFormatValidationSupport getSommaireFormatValidationSupport() {
      return validationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final XsdValidationSupport getXsdValidationSupport() {
      return xsdValidationSupport;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   protected final boolean isVirtual() {
      return false;
   }

}
