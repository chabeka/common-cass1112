/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.batch;

import java.io.File;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import fr.urssaf.image.sae.services.batch.capturemasse.CaptureMasseErreur;
import fr.urssaf.image.sae.services.batch.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.support.xsd.XsdValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.tasklet.AbstractCaptureMasseTasklet;
import fr.urssaf.image.sae.services.batch.common.Constantes;

/**
 * Classe abstraite de gestion de l'écriture des fichiers de résultat en erreur
 * 
 */
public abstract class AbstractResultatsFileFailureTasklet extends
AbstractCaptureMasseTasklet {

   private static final String LIBELLE_BUL003 = "La capture de masse en mode "
         + "\"Tout ou rien\" a été interrompue. Une procédure d'exploitation a été "
         + "initialisée pour supprimer les données qui auraient pu être stockées.";

   private static final String LIBELLE_BUL004 = "Le job {0} en mode "
         + "\"Partiel\" a été interrompue. Une procédure d'exploitation a été "
         + "initialisée pour supprimer les données qui auraient pu être stockées.";

   /**
    * {@inheritDoc}
    */
   @Override
   @SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
   /*
    * Alerte PMD car nous avons obligation de caster l'erreur afin de pouvoir
    * l'exploiter plus tard
    */
   public final RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {

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

      String jobName = chunkContext.getStepContext().getStepExecution()
            .getJobExecution().getJobInstance().getJobName();
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

      if (isModeToutOuRien && CollectionUtils.isNotEmpty(listIntDocs)) {
         erreur.getListCodes().set(0, Constantes.ERR_BUL003);
         erreur.getListException().set(0, new Exception(LIBELLE_BUL003));
      } else if (!isModeToutOuRien && CollectionUtils.isNotEmpty(listIntDocs)) {
         erreur.getListCodes().set(0, Constantes.ERR_BUL004);
         erreur.getListException().set(0,
               new Exception(MessageFormat.format(LIBELLE_BUL004, jobName)));
      }

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
   protected abstract ResultatsFileEchecSupport getResultatsFileEchecSupport();

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
