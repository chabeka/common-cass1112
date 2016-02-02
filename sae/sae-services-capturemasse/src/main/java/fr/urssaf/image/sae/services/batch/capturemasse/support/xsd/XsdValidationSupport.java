package fr.urssaf.image.sae.services.batch.capturemasse.support.xsd;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.commons.xml.StaxValidateUtils;
import fr.urssaf.image.sae.services.batch.capturemasse.exception.CaptureMasseRuntimeException;

/**
 * Support de validation de fichier XML à l'aide de fichiers XSD
 */
@Component
public final class XsdValidationSupport {

   @Autowired
   private ApplicationContext applContext;

   private static final String RESULTATS_XSD = "xsd_som_res/resultats.xsd";

   /**
    * Réalise la validation XSD du fichier resultats.xml
    * 
    * @param fileResultats
    *           l'objet File pointant sur le fichier resultats.xml
    */
   public void resultatsValidation(File fileResultats) {

      // TODO Faire 3 tentatives pour couvrir les coupures NFS

      try {

         Resource sommaireXSD = applContext.getResource(RESULTATS_XSD);

         URL xsdSchema = sommaireXSD.getURL();

         StaxValidateUtils.parse(fileResultats, xsdSchema);

      } catch (IOException ioExcept) {
         throw new CaptureMasseRuntimeException(
               "Erreur lors de la validation XSD du resultats.xml", ioExcept);
      } catch (ParserConfigurationException parseExcept) {
         throw new CaptureMasseRuntimeException(
               "Erreur lors de la validation XSD du resultats.xml", parseExcept);
      } catch (SAXException saxExcept) {
         throw new CaptureMasseRuntimeException(
               "Erreur lors de la validation XSD du resultats.xml", saxExcept);
      }

   }

}
