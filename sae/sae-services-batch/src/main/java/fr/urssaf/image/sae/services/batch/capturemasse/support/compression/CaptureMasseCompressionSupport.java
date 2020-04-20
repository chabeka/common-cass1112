package fr.urssaf.image.sae.services.batch.capturemasse.support.compression;

import java.io.File;

import fr.urssaf.image.sae.bo.model.untyped.UntypedDocument;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.exception.CompressionException;
import fr.urssaf.image.sae.services.batch.capturemasse.support.compression.model.CompressedDocument;

/**
 * Composant de compression d'un document dans un traitement de capture de
 * masse.
 */
public interface CaptureMasseCompressionSupport {

   /**
    * Methode permettant de savoir si le document est a compresser.
    * 
    * @param document
    *           document
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement de l'ECDE
    * @return boolean (vrai si le document doit être compressé)
    */
   boolean isDocumentToBeCompress(UntypedDocument document, final File ecdeDirectory);

   /**
    * Methode permettant de compresser le document.
    * 
    * @param document
    *           document
    * @param ecdeDirectory
    *           chemin absolu du répertoire de traitement de l'ECDE
    * @return CompressedDocument document compressé
    * @throws CompressionException
    *            Exception levée lorsqu'il y a une erreur de compression
    */
   CompressedDocument compresserDocument(UntypedDocument document, final File ecdeDirectory)
         throws CompressionException;
}
