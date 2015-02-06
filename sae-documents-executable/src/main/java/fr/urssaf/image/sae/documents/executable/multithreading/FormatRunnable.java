package fr.urssaf.image.sae.documents.executable.multithreading;

import java.io.File;
import java.io.IOException;

import net.docubase.toolkit.model.document.Document;
import fr.urssaf.image.sae.documents.executable.exception.FormatValidationRuntimeException;
import fr.urssaf.image.sae.documents.executable.service.FormatFichierService;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorUnhandledException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;

/**
 * Thread de validation du format de fichiers.
 */
public class FormatRunnable implements Runnable {

   /**
    * Informations du document.
    */
   private Document document;

   /**
    * Fichier.
    */
   private File file;

   /**
    * Service permettant de réaliser des opérations de contrôle de format sur
    * les fichiers.
    */
   private FormatFichierService formatService;

   /**
    * Résultat de la validation du fichier.
    */
   private ValidationResult resultat;

   /**
    * Constructeur.
    * 
    * @param document
    *           document DFCE
    * @param file
    *           fichier
    * @param formatService
    *           service de contrôle de format sur les fichiers
    */
   public FormatRunnable(final Document document, final File file,
         final FormatFichierService formatService) {
      super();
      setDocument(document);
      setFile(file);
      setFormatService(formatService);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public final void run() throws FormatValidationRuntimeException {
      // lance la validation du fichier
      String idFormat = MetadataUtils.getMetadataByCd(getDocument(),
            Constantes.METADONNEES_FORMAT_FICHIER).toString();
      try {
         this.resultat = getFormatService().validerFichier(idFormat, getFile());
      } catch (UnknownFormatException e) {
         throw new FormatValidationRuntimeException(e);
      } catch (ValidatorInitialisationException e) {
         throw new FormatValidationRuntimeException(e);
      } catch (IOException e) {
         throw new FormatValidationRuntimeException(e);
      } catch (ValidatorUnhandledException e) {
         throw new FormatValidationRuntimeException(e);
      }
   }

   /**
    * Permet de récupérer l'objet Document.
    * 
    * @return Document
    */
   public final Document getDocument() {
      return document;
   }

   /**
    * Permet de modifier l'objet Document.
    * 
    * @param document
    *           document DFCE
    */
   public final void setDocument(final Document document) {
      this.document = document;
   }

   /**
    * Permet de récupérer le fichier.
    * 
    * @return File
    */
   public final File getFile() {
      return file;
   }

   /**
    * Permet de modifier le fichier.
    * 
    * @param file
    *           fichier.
    */
   public final void setFile(final File file) {
      this.file = file;
   }

   /**
    * Permet de récupérer le service de contrôle de format des fichier.
    * 
    * @return FormatFichierService
    */
   public final FormatFichierService getFormatService() {
      return formatService;
   }

   /**
    * Permet de modifier le service de contrôle de format des fichier.
    * 
    * @param formatService
    *           service de contrôle de format des fichier
    */
   public final void setFormatService(final FormatFichierService formatService) {
      this.formatService = formatService;
   }

   /**
    * Permet de récuperer le résultat de la validation.
    * 
    * @return ValidationResult
    */
   public final ValidationResult getResultat() {
      return resultat;
   }

   /**
    * Permet de modifier le résultat de la validation.
    * 
    * @param resultat
    *           résultat de validation
    */
   public final void setResultat(ValidationResult resultat) {
      this.resultat = resultat;
   }

}
