
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.IdentificationResultCollection;


public interface PronomCore {

    /**
     * Submits an identification request to identify files using
     * binary signatures.
     *  
     * @param request the identification request.
     * @return the identification result.
     */
    IdentificationResultCollection matchBinarySignatures(IdentificationRequest request);

    /**
     * 
     * matches a known file format extension.
     * 
     * @param request The identification request to identify files using
     * file extensions.
     * @param allExtensions check the extension against all known extensions.
     * If false, then only formats for which there is no other signature will
     * produce a file extension match (this is the default in DROID 5 and below).
     * @return the identification result.
     */
    IdentificationResultCollection matchExtensions(IdentificationRequest request, boolean allExtensions);
    
    
    /**
     * Sets the signature file for the DROID core to use.
     * @param sigFilename the signature file to use
     */
    void setSignatureFile(String sigFilename);

    /**
     * Removes binary Signatures which identify the PUID specified.
     * @param string a puid
     */
    void removeSignatureForPuid(String string);
    
    /**
     * Sets the maximum number of bytes to scan from the
     * beginning or end of a file.  If negative, scanning
     * is unlimited. 
     * @param maxBytes The number of bytes to scan, or negative meaning unlimited.
     */
    void setMaxBytesToScan(long maxBytes);
    
    
    /**
     * Removes hits from the collection where the file format is
     * flagged as lower priority than another in the collection.
     * 
     * @param results The results to remove lower priority hits for.
     */
    void removeLowerPriorityHits(IdentificationResultCollection results); 
    
    
    /**
     * Checks whether any of the results have a file extension mismatch.
     * 
     * @param results The collection to check for mismatches.
     * @param fileExtension The file extension to check against.
     */
    void checkForExtensionsMismatches(IdentificationResultCollection results, String fileExtension);
    
    
}
