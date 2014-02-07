package fr.urssaf.image.sae.documents.executable.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.docubase.toolkit.model.document.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.documents.executable.service.FormatFichierService;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.identification.service.IdentificationService;
import fr.urssaf.image.sae.format.model.EtapeEtResultat;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.service.ValidationService;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * Classe d'implémentation du service <b>FormatFichierService</b>. Cette classe
 * est un singleton, et est accessible via l'annotation <b>@AutoWired</b>.
 */
@Service
public class FormatFichierServiceImpl implements FormatFichierService {

   /**
    * Logger de la classe.
    */
   private static final Logger LOGGER = LoggerFactory
         .getLogger(FormatFichierServiceImpl.class);

   /**
    * Service permettant de réaliser les opérations de validation d'un fichier.
    */
   @Autowired
   private ValidationService validationService;

   /**
    * Service permettant de réaliser les opérations d'identification d'un
    * fichier.
    */
   @Autowired
   private IdentificationService identificationService;

   /**
    * Nombre de documents en erreur d'identification.
    */
   private int nombreDocsErreurIdent;

   /**
    * {@inheritDoc}
    */
   @Override
   public final boolean identifierFichier(final String idFormat,
         final File file, final Document document,
         final List<String> metadonnees) {
      LOGGER
            .debug(
                  "Identification du fichier {} situé dans le répertoire {} au format {}",
                  new Object[] { document.getUuid(), file.getAbsolutePath(),
                        idFormat });
      boolean identificationOK;

      try {
         final IdentificationResult identificationResult = getIdentificationService()
               .identifyFile(idFormat, file);
         if (!identificationResult.isIdentified()) {
            final String metaToLog = MetadataUtils.getMetadatasForLog(document,
                  metadonnees);
            final String resultatIdent = formatIdentificationResult(identificationResult);

            LOGGER.warn("{} ; {} ; {}", new Object[] { document.getUuid(),
                  metaToLog, resultatIdent });
         }
         identificationOK = identificationResult.isIdentified();
      } catch (UnknownFormatException e) {
         LOGGER.error("Format inconnu : {}", new Object[] { e.getMessage() });
         identificationOK = false;
      } catch (IdentifierInitialisationException e) {
         LOGGER.error("Impossible d'initialiser l'identificateur : {}",
               new Object[] { e.getMessage() });
         identificationOK = false;
      } catch (IOException e) {
         LOGGER.error("Impossible d'accèder au fichier : {}", new Object[] { e
               .getMessage() });
         identificationOK = false;
      }
      return identificationOK;
   }

   /**
    * Methode permettant de formatter le résultat de l'identification.
    * 
    * @param identificationResult
    *           résultat de l'identification
    * @return String
    */
   private String formatIdentificationResult(
         final IdentificationResult identificationResult) {
      final StringBuffer buffer = new StringBuffer();
      boolean first = true;
      for (EtapeEtResultat etape : identificationResult.getDetails()) {
         if (!first) {
            buffer.append(", ");
         }
         buffer.append(etape.getEtape());
         buffer.append(": ");
         buffer.append(etape.getResultat());
         first = false;
      }
      return buffer.toString();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final ValidationResult validerFichier(final String idFormat,
         final File file) throws UnknownFormatException,
         ValidatorInitialisationException, IOException {
      LOGGER.debug("Validation du fichier {} au format {}", new Object[] {
            file.getAbsolutePath(), idFormat });
      return getValidationService().validateFile(idFormat, file);
   }

   /**
    * Permet de récupérer le service permettant de réaliser les opérations de
    * validation d'un fichier.
    * 
    * @return ValidationService
    */
   public final ValidationService getValidationService() {
      return validationService;
   }

   /**
    * Permet de modifier le service permettant de réaliser les opérations de
    * validation d'un fichier.
    * 
    * @param validationService
    *           service permettant de réaliser les opérations de validation d'un
    *           fichier
    */
   public final void setValidationService(
         final ValidationService validationService) {
      this.validationService = validationService;
   }

   /**
    * Permet de récupérer le service permettant de réaliser les opérations
    * d'identification d'un fichier.
    * 
    * @return IdentificationService
    */
   public final IdentificationService getIdentificationService() {
      return identificationService;
   }

   /**
    * Permet de modifier le service permettant de réaliser les opérations
    * d'identification d'un fichier.
    * 
    * @param identificationService
    *           service permettant de réaliser les opérations d'identification
    *           d'un fichier
    */
   public final void setIdentificationService(
         final IdentificationService identificationService) {
      this.identificationService = identificationService;
   }

   /**
    * Permet de récupérer le nombre de documents en erreur d'identification.
    * 
    * @return int
    */
   public final int getNombreDocsErreurIdent() {
      return nombreDocsErreurIdent;
   }

   /**
    * Permet de modifier le nombre de documents en erreur d'identification.
    * 
    * @param nombreDocsErreurIdent
    *           nombre de documents en erreur d'identification
    */
   public final void setNombreDocsErreurIdent(final int nombreDocsErreurIdent) {
      this.nombreDocsErreurIdent = nombreDocsErreurIdent;
   }

}
