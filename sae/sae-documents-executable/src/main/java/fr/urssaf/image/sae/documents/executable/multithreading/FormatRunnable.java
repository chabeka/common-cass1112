package fr.urssaf.image.sae.documents.executable.multithreading;

import java.io.IOException;
import java.io.InputStream;

import fr.urssaf.image.sae.documents.executable.exception.FormatValidationRuntimeException;
import fr.urssaf.image.sae.documents.executable.service.FormatFichierService;
import fr.urssaf.image.sae.documents.executable.utils.Constantes;
import fr.urssaf.image.sae.documents.executable.utils.MetadataUtils;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.validation.exceptions.ValidatorInitialisationException;
import fr.urssaf.image.sae.format.validation.validators.model.ValidationResult;
import net.docubase.toolkit.model.document.Document;

/**
 * Thread de validation du format de fichiers.
 */
public class FormatRunnable implements Runnable {

   /**
    * Informations du document.
    */
   private Document document;

   /**
    * Contenu du fichier.
    */
   private InputStream stream;

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
    * @param stream
    *           contenu du fichier
    * @param formatService
    *           service de contrôle de format sur les fichiers
    */
   public FormatRunnable(final Document document, final InputStream stream,
         final FormatFichierService formatService) {
      super();
      setDocument(document);
      setStream(stream);
      setFormatService(formatService);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void run() throws FormatValidationRuntimeException {
      // lance la validation du fichier
      String idFormat = MetadataUtils.getMetadataByCd(getDocument(), Constantes.METADONNEES_FORMAT_FICHIER).toString();
      try {
         this.resultat = getFormatService().validerFichier(idFormat, getStream());
      } catch (UnknownFormatException e) {
         throw new FormatValidationRuntimeException(e);
      } catch (ValidatorInitialisationException e) {
         throw new FormatValidationRuntimeException(e);
      } catch (IOException e) {
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
    * Permet de récupérer le contenu du fichier.
    * 
    * @return InputStream
    */
   public final InputStream getStream() {
      return stream;
   }

   /**
    * Permet de modifier le contenu du fichier.
    * 
    * @param stream
    *           contenu du fichier.
    */
   public final void setStream(final InputStream stream) {
      this.stream = stream;
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
