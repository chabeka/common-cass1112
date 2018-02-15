
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import fr.urssaf.image.commons.commons.pronom.exemple.exception.SignatureFileException;


public interface SignatureParser {

    /**
     * Parses the file specified and returns a collection of Format objects.
     * @param callback a callback to be executed whenver a format is found
     * @throws SignatureFileException if the signature file could not be parsed
     */
    void formats(FormatCallback callback) throws SignatureFileException;
    
}
