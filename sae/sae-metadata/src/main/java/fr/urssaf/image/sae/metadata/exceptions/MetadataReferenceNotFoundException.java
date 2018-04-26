package fr.urssaf.image.sae.metadata.exceptions;

import java.text.MessageFormat;

/**
 *Exception levée lorsque la métadonnée n'existe pas. 
 */
@SuppressWarnings("PMD")
public class MetadataReferenceNotFoundException extends Exception {
   
   /**
    * Constructeur
    * 
    * @param metaName nom de la métadonnée
    */
   public MetadataReferenceNotFoundException(String metaName) {
      super(MessageFormat.format("La métadonnée {0} n'''a pas été trouvé",
            metaName));

   }
}
