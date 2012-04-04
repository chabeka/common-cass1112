
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class InternalSignatureCollection extends SimpleElement {

    /**
     * Default size of signature collection.
     */
    private static final int DEFAULT_COLLECTION_SIZE = 10;
    
    private List<InternalSignature> intSigs = new ArrayList<InternalSignature>(DEFAULT_COLLECTION_SIZE);
    private Map<Integer, InternalSignature> sigsByID = new HashMap<Integer, InternalSignature>();
    
    /**
     * Runs all the signatures against the target file,
     * adding a hit for each of them, if any of them match.
     * 
     * @param targetFile The file to match the signatures against.
     * @param maxBytesToScan The maximum bytes to scan.
     * @return A list of the internal signatures which matched. 
     */
    public final List<InternalSignature> getMatchingSignatures(ByteReader targetFile, long maxBytesToScan) {
        List<InternalSignature> matchingSigs = new ArrayList<InternalSignature>();
        if (targetFile.getNumBytes() > 0) {
            final int stop = intSigs.size();
            for (int sigIndex = 0; sigIndex < stop; sigIndex++) {
                final InternalSignature internalSig = intSigs.get(sigIndex);
                if (internalSig.matches(targetFile, maxBytesToScan)) {
                    matchingSigs.add(internalSig);
                }
            }
        }
        return matchingSigs;
    }
    
   
    /**
     * Prepares the internal signatures in the collection for use.
     */
    public final void prepareForUse() {
        for (Iterator<InternalSignature> sigIterator = intSigs.iterator(); sigIterator.hasNext();) {
            InternalSignature sig = sigIterator.next();
            sig.prepareForUse();
            if (sig.isInvalidSignature()) {
                sigsByID.remove(sig.getID());
                getLog().warn(getInvalidSignatureWarningMessage(sig));
                sigIterator.remove();
            }
        }
    }
    
    private String getInvalidSignatureWarningMessage(InternalSignature sig) {
        return String.format("Removing invalid signature [id:%d]. " 
                + "Matches formats: %s", sig.getID(), sig.getFileFormatDescriptions());
    }

    
    /* setters */
    /**
     * @param iSig the signature to add.
     */
    public final void addInternalSignature(final InternalSignature iSig) {
        intSigs.add(iSig);
        sigsByID.put(iSig.getID(), iSig);
    }
    
    
    /**
     * 
     * @param iSig The signature to remove.
     */
    public final void removeInternalSignature(final InternalSignature iSig) {
        intSigs.remove(iSig);
        sigsByID.remove(iSig.getID());
    }
    
    
    /**
     * 
     * @param signatureID The id of the signature to get
     * @return The signature with the given id, or null if the signature does not exist.
     */
    public final InternalSignature getInternalSignature(int signatureID) {
        return sigsByID.get(signatureID);
    }

    
    /**
     * 
     * @param iSigs The list of signatures to add.
     */
    public final void setInternalSignatures(final List<InternalSignature> iSigs) {
        intSigs.clear();
        sigsByID.clear();
        for (InternalSignature signature : iSigs) {
            addInternalSignature(signature);
        }
    }

    /* getters */
    /**
     * A list of internal signatures in the collection.
     * @return A list of internal signatures in the collection.
     */
    public final List<InternalSignature> getInternalSignatures() {
        return intSigs;
    }

    /**
     * Sorts the signatures in an order which maximises performance.
     * @param compareWith the internal signature comparator to compare with.
     */
    public final void sortSignatures(final Comparator<InternalSignature> compareWith) {
        Collections.sort(intSigs, compareWith);
    }

}
