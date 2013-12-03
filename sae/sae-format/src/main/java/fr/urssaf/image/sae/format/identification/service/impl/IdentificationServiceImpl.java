package fr.urssaf.image.sae.format.identification.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.urssaf.image.sae.format.context.SaeFormatApplicationContext;
import fr.urssaf.image.sae.format.exception.UnknownFormatException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentificationRuntimeException;
import fr.urssaf.image.sae.format.identification.exceptions.IdentifierInitialisationException;
import fr.urssaf.image.sae.format.identification.identifiers.Identifier;
import fr.urssaf.image.sae.format.identification.identifiers.model.IdentificationResult;
import fr.urssaf.image.sae.format.identification.service.IdentificationService;
import fr.urssaf.image.sae.format.referentiel.exceptions.ReferentielRuntimeException;
import fr.urssaf.image.sae.format.referentiel.model.FormatFichier;
import fr.urssaf.image.sae.format.referentiel.service.impl.ReferentielFormatServiceImpl;
import fr.urssaf.image.sae.format.utils.message.SaeFormatMessageHandler;

/**
 * 
 * Implémentation du processus d’identification d’un flux ou d’un fichier.
 * 
 */
@Service
public class IdentificationServiceImpl implements IdentificationService {

   /**
    * Service permettant d’interroger le référentiel des formats
    */
   @Autowired
   private ReferentielFormatServiceImpl referentielFormatService;

   @Override
   public final IdentificationResult identifyFile(String idFormat, File fichier)
         throws IdentifierInitialisationException, UnknownFormatException,
         IOException {

      FormatFichier format;
      Identifier identifier;
      IdentificationResult identifResult;
      try {
         // On récupère l'identificateur à utiliser pour l'idFormat donné à
         // partir du référentiel des formats
         format = referentielFormatService.getFormat(idFormat);

         if (format != null) {
            String identificateur = format.getIdentificateur();

            // On utilise l'application contexte pour récupérer une instance de
            // l'identificateur
            identifier = SaeFormatApplicationContext.getApplicationContext()
                  .getBean(identificateur, Identifier.class);

            // On appel la méthode identifyFile en passant en paramètre le
            // fichier et l'idFormat
            identifResult = identifier.identifyFile(idFormat, fichier);

            // En retour on récupère et on renvoi un objet
            // IdentificationResult
            // contenant le résultat de l'identification et une liste des
            // traces.
            return identifResult;

         } else {
            throw new IdentifierInitialisationException(SaeFormatMessageHandler
                  .getMessage("erreur.recup.identif"));
         }

      } catch (NoSuchBeanDefinitionException except) {
         // S'il n'est pas possible de récupérer une instance de
         // l'identificateur
         throw new IdentifierInitialisationException(SaeFormatMessageHandler
               .getMessage("erreur.recup.identif"));
      } catch (ReferentielRuntimeException except) {
         throw new IdentificationRuntimeException(except.getMessage(), except);
      }

   }

   @Override
   public final IdentificationResult identifyStream(String idFormat,
         InputStream stream) throws UnknownFormatException,
         IdentifierInitialisationException {

      try {
         File createdFile;
         IdentificationResult identificationResult;

         createdFile = File.createTempFile(SaeFormatMessageHandler
               .getMessage("file.generated"), SaeFormatMessageHandler
               .getMessage("extension.file"));
         FileUtils.copyInputStreamToFile(stream, createdFile);

         identificationResult = identifyFile(idFormat, createdFile);

         FileUtils.forceDelete(createdFile);

         return identificationResult;

      } catch (IOException except) {
         throw new IdentificationRuntimeException(SaeFormatMessageHandler
               .getMessage("erreur.identification.convert.stream.to.file"),
               except);
      }
   }

}
