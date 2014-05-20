package fr.urssaf.image.commons.droid.factory;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.springframework.core.io.Resource;

import uk.gov.nationalarchives.droid.container.ContainerSignatureDefinitions;
import uk.gov.nationalarchives.droid.container.ContainerSignatureSaxParser;
import uk.gov.nationalarchives.droid.core.BinarySignatureIdentifier;
import uk.gov.nationalarchives.droid.core.SignatureParseException;
import fr.urssaf.image.commons.droid.exception.FormatIdentificationRuntimeException;
import fr.urssaf.image.commons.droid.support.MyBinarySignatureIdentifier;

/**
 * Factory de création de mécanismes nécessaires à DROID pour l'identification
 * des formats de fichier.
 */
public final class DroidFactory {

   private static final Long MAX_BYTES_TO_SCAN = Long.valueOf(65536);

   private DroidFactory() {

   }

   /**
    * Instancie le mécanisme d'identification du format par rapport aux
    * signatures binaires
    * 
    * @param signatures
    *           l'objet Resource pointant vers les signagures binaries
    * @return l'objet BinarySignatureIdentifier prêt à être utilisé
    */
   public static BinarySignatureIdentifier loadSignatures(Resource signatures) {

      MyBinarySignatureIdentifier binarySignatureIdentifier = new MyBinarySignatureIdentifier(
            signatures);

      binarySignatureIdentifier.init();

      binarySignatureIdentifier.setMaxBytesToScan(MAX_BYTES_TO_SCAN);

      return binarySignatureIdentifier;

   }

   /**
    * Instancie l'objet contenant les signatures des formats de type
    * "conteneurs"
    * 
    * @param containerSignatures
    *           l'objet Resource pointant vers les signatures des conteneurs
    * @return l'objet ContainerSignatureDefinitions prêt à être utilisé
    */
   public static ContainerSignatureDefinitions loadContaineur(
         Resource containerSignatures) {

      ContainerSignatureDefinitions containerSignatureDefinitions = null;

      try {

         ContainerSignatureSaxParser parser = new ContainerSignatureSaxParser();

         containerSignatureDefinitions = parser.parse(containerSignatures
               .getInputStream());

      } catch (JAXBException e) {
         throw new FormatIdentificationRuntimeException(e);
      } catch (SignatureParseException e) {
         throw new FormatIdentificationRuntimeException(e);
      } catch (IOException e) {
         throw new FormatIdentificationRuntimeException(e);
      }

      return containerSignatureDefinitions;

   }

}
