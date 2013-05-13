/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.resultats.batch;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import fr.urssaf.image.sae.services.capturemasse.common.CaptureMasseErreur;
import fr.urssaf.image.sae.services.capturemasse.common.Constantes;
import fr.urssaf.image.sae.services.capturemasse.support.resultats.ResultatsFileEchecSupport;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.capturemasse.support.xsd.XsdValidationSupport;

/**
 * Classe abstraite de gestion de l'écriture des fichiers de résultat en erreur
 * 
 */
public abstract class AbstractResultatsFileFailureTasklet implements Tasklet {

   private static final String LIBELLE_BUL003 = "La capture de masse en mode "
         + "\"Tout ou rien\" a été interrompue. Une procédure d'exploitation a été "
         + "initialisée pour supprimer les données qui auraient pu être stockées.";

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(StepContribution contribution,
         ChunkContext chunkContext) throws Exception {

      final Map<String, Object> map = chunkContext.getStepContext()
            .getJobExecutionContext();
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<String> codes = (ConcurrentLinkedQueue<String>) map
            .get(Constantes.CODE_EXCEPTION);
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Integer> index = (ConcurrentLinkedQueue<Integer>) map
            .get(Constantes.INDEX_EXCEPTION);
      @SuppressWarnings("unchecked")
      ConcurrentLinkedQueue<Exception> exceptions = (ConcurrentLinkedQueue<Exception>) map
            .get(Constantes.DOC_EXCEPTION);

      CaptureMasseErreur erreur = new CaptureMasseErreur();
      erreur.setListCodes(Arrays.asList(codes.toArray(new String[0])));
      erreur.setListException(Arrays.asList(exceptions
            .toArray(new Exception[0])));
      erreur.setListIndex(Arrays.asList(index.toArray(new Integer[0])));

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
               "TOUT_OU_RIEN");
      } catch (Exception e) {
         isModeToutOuRien = false;
      }

      ConcurrentLinkedQueue<?> listIntDocs = getIntegratedDocuments();

      if (isModeToutOuRien && CollectionUtils.isNotEmpty(listIntDocs)) {
         erreur.getListCodes().set(0, Constantes.ERR_BUL003);
         erreur.getListException().set(0, new Exception(LIBELLE_BUL003));
      }

      final File ecdeDirectory = sommaireFile.getParentFile();

      int nbreDocs = (Integer) map.get(Constantes.DOC_COUNT);

      if (isVirtual()) {
         getResultatsFileEchecSupport().writeVirtualResultatsFile(
               ecdeDirectory, sommaireFile, erreur, nbreDocs);
      } else {
         getResultatsFileEchecSupport().writeResultatsFile(ecdeDirectory,
               sommaireFile, erreur, nbreDocs);
      }
      File resultats = new File(ecdeDirectory, "resultats.xml");

      getXsdValidationSupport().resultatsValidation(resultats);

      return RepeatStatus.FINISHED;
   }

   /**
    * @return la liste des éléments intégrés
    */
   abstract ConcurrentLinkedQueue<?> getIntegratedDocuments();

   /**
    * @return l'indicateur de traitement de documents virtuels
    */
   abstract boolean isVirtual();

   /**
    * @return le support de génération des fichiers en erreur
    */
   abstract ResultatsFileEchecSupport getResultatsFileEchecSupport();

   /**
    * @return la classe permettant de réaliser des validations de fichier
    *         sommaire
    */
   abstract SommaireFormatValidationSupport getSommaireFormatValidationSupport();

   /**
    * @return le support de validation de fichiers XML
    */
   abstract XsdValidationSupport getXsdValidationSupport();

}
