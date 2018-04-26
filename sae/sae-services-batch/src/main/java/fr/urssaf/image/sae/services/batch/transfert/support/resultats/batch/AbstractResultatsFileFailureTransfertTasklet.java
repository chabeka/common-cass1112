package fr.urssaf.image.sae.services.batch.transfert.support.resultats.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import fr.urssaf.image.sae.services.batch.capturemasse.CaptureMasseErreur;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.tasklet.AbstractCaptureMasseTasklet;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.transfert.support.resultats.ResultatsFileEchecTransfertSupport;

/**
 * Classe abstraite pour ecriture du resultat en cas d'echec du transfert de
 * masse
 * 
 *
 */
public abstract class AbstractResultatsFileFailureTransfertTasklet extends
      AbstractCaptureMasseTasklet {

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   /*
    * Alerte PMD car nous avons obligation de caster l'erreur afin de pouvoir
    * l'exploiter plus tard
    */
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) throws Exception {

      CaptureMasseErreur erreur = new CaptureMasseErreur();
      erreur.setListCodes(new ArrayList<String>(
            getCodesErreurListe(chunkContext)));
      erreur.setListException(new ArrayList<Exception>(
            getExceptionErreurListe(chunkContext)));
      erreur.setListIndex(new ArrayList<Integer>(
            getIndexErreurListe(chunkContext)));
      erreur.setListRefIndex(new ArrayList<Integer>(
            getIndexReferenceListe(chunkContext)));

      final Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();

      final String pathSommaire = (String) map.get(Constantes.SOMMAIRE_FILE);
      File sommaireFile = new File(pathSommaire);

      /*
       * On vérifie qu'on est mode tout ou rien et qu'il reste des rollback à
       * effectuer. Si c'est le cas on change le code possible fonctionnel en
       * code technique et le message correspondant
       */
      boolean isModeToutOuRien = true;
      try {
         getSommaireFormatValidationSupport().validerModeBatch(sommaireFile,
               Constantes.BATCH_MODE.TOUT_OU_RIEN.getModeNom());
      } catch (Exception e) {
         isModeToutOuRien = false;
      }

      ConcurrentLinkedQueue<?> listIntDocs = getIntegratedDocuments();
      final File ecdeDirectory = sommaireFile.getParentFile();

      int nbreDocs = (Integer) map.get(Constantes.DOC_COUNT);
      int nbDocsIntegres = 0;
      String batchModeTraitement = null;
      Object value = map.get(Constantes.NB_INTEG_DOCS);
      if (value != null) {
         nbDocsIntegres = (Integer) value;
      }
      value = map.get(Constantes.BATCH_MODE_NOM);
      if (value != null) {
         batchModeTraitement = (String) value;
      } else if (isModeToutOuRien) {
         batchModeTraitement = Constantes.BATCH_MODE.TOUT_OU_RIEN.getModeNom();
      } else {
         batchModeTraitement = Constantes.BATCH_MODE.PARTIEL.getModeNom();
      }

      if (isVirtual()) {
         getResultatsFileEchecSupport().writeVirtualResultatsFile(
               ecdeDirectory, sommaireFile, erreur, nbreDocs, nbDocsIntegres,
               batchModeTraitement, listIntDocs);
      } else {
         getResultatsFileEchecSupport().writeResultatsFile(ecdeDirectory,
               sommaireFile, erreur, nbreDocs, nbDocsIntegres,
               batchModeTraitement, listIntDocs);
      }
      File resultats = new File(ecdeDirectory, "resultats.xml");

      getXsdValidationSupport().resultatsValidation(resultats);

      return RepeatStatus.FINISHED;
   }

   /**
    * @return la liste des éléments intégrés
    */
   protected abstract ConcurrentLinkedQueue<?> getIntegratedDocuments();

   /**
    * @return l'indicateur de traitement de documents virtuels
    */
   protected abstract boolean isVirtual();

   /**
    * @return le support de génération des fichiers en erreur
    */
   protected abstract ResultatsFileEchecTransfertSupport getResultatsFileEchecSupport();

   /**
    * @return la classe permettant de réaliser des validations de fichier
    *         sommaire
    */
   protected abstract SommaireFormatValidationSupport getSommaireFormatValidationSupport();

   /**
    * @return le support de validation de fichiers XML
    */
   protected abstract XsdValidationSupport getXsdValidationSupport();
}
