/**
 * 
 */
package fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.batch;

import java.io.File;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.services.batch.capturemasse.controles.SAEControleSupportService;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFileNotFoundException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseSommaireTypeHashException;
import fr.urssaf.image.sae.services.batch.capturemasse.modele.commun_sommaire_et_resultat.BatchModeType;
import fr.urssaf.image.sae.services.batch.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.batch.capturemasse.tasklet.AbstractCaptureMasseTasklet;
import fr.urssaf.image.sae.services.batch.capturemasse.utils.XmlReadUtils;
import fr.urssaf.image.sae.services.batch.common.Constantes;
import fr.urssaf.image.sae.services.batch.common.Constantes.BATCH_MODE;

/**
 * Tasklet de vérification du format de fichier sommaire.xml
 * 
 */
@Component
public class CheckFormatFileSommaireTasklet extends AbstractCaptureMasseTasklet {

   @Autowired
   protected SommaireFormatValidationSupport validationSupport;

   protected static final Logger LOGGER = LoggerFactory
         .getLogger(CheckFormatFileSommaireTasklet.class);

   protected static final String TRC_EXEC = "execute()";

   @Autowired
   private SAEControleSupportService controleSupport;

   /**
    * {@inheritDoc}
    */
   @Override
   public final RepeatStatus execute(final StepContribution contribution,
         final ChunkContext chunkContext) {

      LOGGER.debug("{} - Début de méthode", TRC_EXEC);

      final StepExecution stepExecution = chunkContext.getStepContext()
            .getStepExecution();
      final ExecutionContext context = stepExecution.getJobExecution()
            .getExecutionContext();
      final String sommairePath = context.getString(Constantes.SOMMAIRE_FILE);

      final File sommaireFile = new File(sommairePath);

      final String hash = (String) chunkContext.getStepContext()
            .getJobParameters().get(Constantes.HASH);

      final String typeHash = (String) chunkContext.getStepContext()
            .getJobParameters().get(Constantes.TYPE_HASH);

      try {

         LOGGER.debug("{} - Début de validation du fichier sommaire.xml",
               TRC_EXEC);

         if (hash != null) {
            controleSupport.checkHash(sommaireFile, hash, typeHash);
         }

         validationSupport.validationSommaire(sommaireFile);

         LOGGER.debug("{} - Fin de validation du fichier sommaire.xml",
               TRC_EXEC);


         this.validationSpecifiqueSommaire(sommaireFile);

         boolean restitutionUuids = false;
         String valeur = XmlReadUtils.getElementValue(sommaireFile,
               "restitutionUuids");
         if (StringUtils.isNotBlank(valeur)) {
            restitutionUuids = BooleanUtils.toBoolean(valeur);
            context.put(Constantes.RESTITUTION_UUIDS, restitutionUuids);
         }

         String batchModeSommaire = XmlReadUtils.getElementValue(sommaireFile,
               Constantes.BATCH_MODE_ELEMENT_NAME);
         String batchmodeRedirection = null;
         String batchmode = null;

         LOGGER.debug("{} - Fin du dénombrement", TRC_EXEC);

         if (batchModeSommaire == null
               || (batchModeSommaire != null && batchModeSommaire.isEmpty())) {
            throw new CaptureMasseRuntimeException(
                  "le fichier sommaire.xml n'est pas valide car la balise "
                        + Constantes.BATCH_MODE_ELEMENT_NAME
                        + "n'est pas correctement renseigné");
         } else if (batchModeSommaire.equalsIgnoreCase(BATCH_MODE.TOUT_OU_RIEN
               .getModeNom())) {
            batchmodeRedirection = BATCH_MODE.TOUT_OU_RIEN.getModeNomCourt();
            batchmode = BATCH_MODE.TOUT_OU_RIEN.getModeNom();
         } else if (batchModeSommaire.equalsIgnoreCase(BATCH_MODE.PARTIEL
               .getModeNom())) {
            batchmodeRedirection = BATCH_MODE.PARTIEL.getModeNomCourt();
            batchmode = BATCH_MODE.PARTIEL.getModeNom();
         } else {
            throw new CaptureMasseRuntimeException(
                  "Le mode de traitement du batch n'est pas reconnu : "
                        + batchModeSommaire);
         }

         context.put(Constantes.BATCH_MODE_NOM, batchmode);
         context.put(Constantes.BATCH_MODE_NOM_REDIRECT, batchmodeRedirection);

      } catch (CaptureMasseSommaireFormatValidationException e) {
         getErrorMessageList(chunkContext).add(e.getMessage());
         logFailedValidationSommaire(e);
      } catch (CaptureMasseRuntimeException e) {
         getErrorMessageList(chunkContext).add(e.getMessage());
         logFailedValidationSommaire(e);
      } catch (CaptureMasseSommaireHashException e) {
         getErrorMessageList(chunkContext).add(e.getMessage());
         logFailedValidationSommaire(e);
      } catch (CaptureMasseSommaireTypeHashException e) {
         logFailedValidationSommaire(e);
      } catch (CaptureMasseSommaireFileNotFoundException e) {
         getErrorMessageList(chunkContext).add(e.getMessage());
         logFailedValidationSommaire(e);
      }

      LOGGER.debug("{} - Fin de méthode", TRC_EXEC);

      return RepeatStatus.FINISHED;
   }

   /**
    * Validation spécifique du sommaire.
    * 
    * @param sommaireFile
    *           Fichier sommaire.
    * @throws CaptureMasseSommaireFormatValidationException
    * @throws CaptureMasseSommaireFileNotFoundException
    * @{@link CaptureMasseSommaireFormatValidationException}
    */
   protected void validationSpecifiqueSommaire(File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException,
         CaptureMasseSommaireFileNotFoundException {
      LOGGER.debug("{} - Début de validation du BATCH_MODE du sommaire.xml",
            TRC_EXEC);

      validationSupport.validerModeBatch(sommaireFile,
            Constantes.BATCH_MODE.TOUT_OU_RIEN.getModeNom(),
            Constantes.BATCH_MODE.PARTIEL.getModeNom());

      LOGGER.debug("{} - Fin de validation du BATCH_MODE du sommaire.xml",
            TRC_EXEC);
      LOGGER.debug(
            "{} - Début de validation spécifique de la présence du chemin/nom du fichier",
            TRC_EXEC);
      validationSupport.validationDocumentBaliseRequisSommaire(sommaireFile,
            "cheminEtNomDuFichier");
      LOGGER.debug(
            "{} - Fin de validation spécifique de la présence du chemin/nom du fichier",
            TRC_EXEC);

      String batchModeSommaire = XmlReadUtils.getElementValue(sommaireFile,
            Constantes.BATCH_MODE_ELEMENT_NAME);

      if (batchModeSommaire != null && !batchModeSommaire.isEmpty()
            && BatchModeType.PARTIEL.name().equals(batchModeSommaire)) {

         LOGGER.debug(
               "{} - Début de validation spécifique de la présence de l'IdGed",
               TRC_EXEC);
         validationSupport.validationDocumentValeurBaliseRequisSommaire(
               sommaireFile, "code", "IdGed");
         LOGGER.debug(
               "{} - Fin de validation spécifique de la présence de l'IdGed",
               TRC_EXEC);
      }

    if (batchModeSommaire != null && !batchModeSommaire.isEmpty()
        && BatchModeType.TOUT_OU_RIEN.name().equals(batchModeSommaire)) {
      LOGGER.debug("{} - Début de validation unicité IdGed des documents",
                   TRC_EXEC);

      validationSupport.validerUniciteIdGed(sommaireFile);

      LOGGER.debug("{} - Fin de validation unicité IdGed des documents",
                   TRC_EXEC);
    }
   }
   
   private void logFailedValidationSommaire(Exception e){
      LOGGER.warn("Erreur lors de la validation du fichier sommaire", e);
   }
   
}
