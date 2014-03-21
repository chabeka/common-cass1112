package fr.urssaf.image.sae.webservices.aspect;

import java.util.List;

import org.apache.axis2.context.MessageContext;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.urssaf.image.sae.bo.model.untyped.UntypedMetadata;
import fr.urssaf.image.sae.services.capture.model.CaptureResult;
import fr.urssaf.image.sae.services.controles.traces.TracesControlesSupport;

/**
 * Aspect de traçabilité des erreurs de vérification de formation en mode
 * monitor. Cette classe intercepte le retour des méthode capture et
 * captureBinaire.
 */
@Component
public class CheckFormatCaptureServiceAspect {

   @Autowired
   private TracesControlesSupport tracesSupport;

   /**
    * Méthode interceptant le retour de la capture.
    * 
    * @param joinPoint
    *           point de jointure de l'aspect
    * @param retour
    *           retour de la méthode capture
    */
   @SuppressWarnings("unchecked")
   public final void checkFormatErrorForCapture(JoinPoint joinPoint,
         final CaptureResult retour) {
      List<UntypedMetadata> metadatas = (List<UntypedMetadata>) joinPoint
            .getArgs()[0];
      checkFormatError(metadatas, retour);
   }

   /**
    * Méthode permettant de vérifier s'il y a des erreurs d'identification ou de
    * validation à tracer dans le registre de surveillance technique en mode
    * monitor.
    * 
    * @param metadatas
    *           liste des métadonnées
    * @param captureResult
    *           résultat de la méthode capture
    */
   private void checkFormatError(final List<UntypedMetadata> metadatas,
         final CaptureResult captureResult) {

      // Récupère le nom de l'opération du WS
      MessageContext msgCtx = MessageContext.getCurrentMessageContext();
      String contexte = msgCtx.getAxisOperation().getName().getLocalPart();

      // Récupère le format du fichier
      String formatFichier = findMetadataValue("FormatFichier", metadatas);

      // Récupère l'identifiant de traitement unitaire
      String idTraitement = (String) MDC.get("log_contexte_uuid");

      if (captureResult.isIdentificationActivee()
            && captureResult.isIdentificationEchecMonitor()) {
         tracesSupport.traceErreurIdentFormatFichier(contexte, formatFichier,
               captureResult.getIdFormatReconnu(), captureResult.getIdDoc()
                     .toString(), idTraitement);
      }

      if (captureResult.isValidationActivee()
            && captureResult.isIdentificationEchecMonitor()) {
         tracesSupport.traceErreurValidFormatFichier(contexte, formatFichier,
               captureResult.getDetailEchecValidation(), captureResult
                     .getIdDoc().toString(), idTraitement);
      }
   }

   private String findMetadataValue(String metaName,
         List<UntypedMetadata> metadatas) {
      int index = 0;
      String valeur = null;
      boolean trouve = false;

      do { // récupération de la valeur de la métadonnéé "FormatFichier"
         UntypedMetadata metadata = metadatas.get(index);
         if (StringUtils.equalsIgnoreCase(metadata.getLongCode(), metaName)) {
            trouve = true;
            valeur = (String) metadata.getValue();
         }
         index++;
      } while (!trouve && index < metadatas.size());

      return valeur;
   }
}
