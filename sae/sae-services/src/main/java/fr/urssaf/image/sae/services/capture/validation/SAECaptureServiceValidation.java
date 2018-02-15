package fr.urssaf.image.sae.services.capture.validation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.activation.DataHandler;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.CollectionUtils;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.exception.capture.EmptyDocumentEx;
import fr.urssaf.image.sae.services.exception.capture.EmptyFileNameEx;
import fr.urssaf.image.sae.services.util.ResourceMessagesUtils;

/**
 * Classe de validation des arguments en entrée des implémentations du service
 * SAECaptureService
 * 
 * 
 */
@Aspect
public class SAECaptureServiceValidation {

   private static final String CAPTURE_METHOD = "execution(fr.urssaf.image.sae.services.capture.model.CaptureResult fr.urssaf.image.sae.services.capture.SAECaptureService.capture(*,*))"
         + "&& args(metadatas,ecdeURL)";

   private static final String CAPTURE_BINAIRE_METHOD = "execution(fr.urssaf.image.sae.services.capture.model.CaptureResult fr.urssaf.image.sae.services.capture.SAECaptureService.captureBinaire(*,*,*))"
         + "&& args(metadatas, content, fileName)";

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode capture de l'interface SAECaptureService sont bien correct.
    * 
    * @param metadatas
    *           liste des métadonnées à archiver doit non vide
    * @param ecdeURL
    *           url ECDE du fichier numérique à archiver doit non null
    * 
    */
   @Before(CAPTURE_METHOD)
   public final void capture(List<UntypedMetadata> metadatas, URI ecdeURL) {

      if (ecdeURL == null) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "ecdeURL"));
      }

      if (CollectionUtils.isEmpty(metadatas)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "metadatas"));
      }

   }

   /**
    * Methode permettant de venir verifier si les paramétres d'entree de la
    * methode captureBinaire de l'interface SAECaptureService sont bien correct.
    * 
    * @param metadatas
    *           liste des métadonnées à archiver doit non vide
    * @param content
    *           contenu du fichier numérique à archiver doit non null
    * @param fileName
    *           nom du fichier numérique à archiver doit non null
    * @throws EmptyDocumentEx
    *            Exception levée si la taille du document est égale à 0 octet
    * @throws EmptyFileNameEx
    *            Exception levée si le nom de fichier est vide ou rempli
    *            d'espaces
    */
   @Before(CAPTURE_BINAIRE_METHOD)
   public final void captureBinaire(List<UntypedMetadata> metadatas,
         DataHandler content, String fileName) throws EmptyDocumentEx,
         EmptyFileNameEx {

      if (StringUtils.isBlank(fileName)) {
         throw new EmptyFileNameEx(ResourceMessagesUtils
               .loadMessage("nomfichier.vide"));
      }

      if (content == null) {
         throw new EmptyDocumentEx(ResourceMessagesUtils
               .loadMessage("capture.fichier.binaire.vide"));
      }

      try {
         InputStream stream = content.getInputStream();

         if (stream == null) {
            throw new EmptyDocumentEx(ResourceMessagesUtils
                  .loadMessage("capture.fichier.binaire.vide"));
         }

         stream.close();

      } catch (IOException e) {
         throw new EmptyDocumentEx(ResourceMessagesUtils
               .loadMessage("capture.fichier.binaire.vide"), e);
      }

      if (CollectionUtils.isEmpty(metadatas)) {
         throw new IllegalArgumentException(ResourceMessagesUtils.loadMessage(
               "argument.required", "metadatas"));
      }

   }

}
