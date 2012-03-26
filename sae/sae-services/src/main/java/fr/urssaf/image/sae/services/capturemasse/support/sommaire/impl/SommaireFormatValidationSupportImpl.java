/**
 * 
 */
package fr.urssaf.image.sae.services.capturemasse.support.sommaire.impl;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseRuntimeException;
import fr.urssaf.image.sae.services.capturemasse.exception.CaptureMasseSommaireFormatValidationException;
import fr.urssaf.image.sae.services.capturemasse.support.sommaire.SommaireFormatValidationSupport;
import fr.urssaf.image.sae.services.util.XmlValidationUtils;

/**
 * Implémentation du support {@link SommaireFormatValidationSupport}
 * 
 */
@Component
public class SommaireFormatValidationSupportImpl implements
      SommaireFormatValidationSupport {

   private static final String XSD_FILE = "xsd_som_res/sommaire.xsd";

   /**
    * {@inheritDoc}
    */
   @Override
   public final void validationSommaire(final File sommaireFile)
         throws CaptureMasseSommaireFormatValidationException {

      try {
         XmlValidationUtils.parse(sommaireFile, XSD_FILE);

      } catch (IOException e) {
         throw new CaptureMasseRuntimeException(e);

      } catch (ParserConfigurationException e) {
         throw new CaptureMasseSommaireFormatValidationException(sommaireFile
               .getAbsolutePath(), e);

      } catch (SAXException e) {
         throw new CaptureMasseSommaireFormatValidationException(sommaireFile
               .getAbsolutePath(), e);
      }

   }

}
