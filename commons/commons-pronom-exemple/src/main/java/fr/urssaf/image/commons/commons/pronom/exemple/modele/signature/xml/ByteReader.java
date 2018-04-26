
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;


/**
 * Interface pour l'accès aux octets à partir d'un fichier ou d'une URL.
 * <p/>
 * Creer une instance avec <code>AbstractByteReader.newByteReader()</code>.
 */
public interface ByteReader {

    /* Setters for identification status */
    /**
     * Set identification status to Positive.
     */
    void setPositiveIdent();

    /**
     * Set identification status to Tentative.
     */
    void setTentativeIdent();

    /**
     * Set identification status to No identification.
     */
    void setNoIdent();

    /**
     * Set identification status to Error.
     */
    void setErrorIdent();

    /**
     * Checks whether the file has yet been classified.
     * @return is classified.
     */
    boolean isClassified();

    /**
     * Get classification of the file.
     * @return classification.
     */
    int getClassification();

    /**
     * Set identification warning.
     *
     * @param theWarning the warning message to use
     */
    void setIdentificationWarning(String theWarning);

    /**
     * Get any warning message created when identifying this file.
     * @return identification warning.
     */
    String getIdentificationWarning();

    /**
     * Add another hit to the list of hits for this file.
     *
     * @param theHit The <code>FileFormatHit</code> to be added
     */
    void addHit(FileFormatHit theHit);

    /**
     * Remove a hit from the list of hits for this file.
     *
     * @param theIndex Index of the hit to be removed
     */
    void removeHit(int theIndex);

    /**
     * Get number of file format hits.
     * @return number of hits.
     */
    int getNumHits();

    /**
     * Get a file format hit.
     *
     * @param theIndex index of the <code>FileFormatHit</code> to get
     * @return the hit associated with <code>theIndex</code>
     */
    FileFormatHit getHit(int theIndex);

    /**
     * Get file path of the associated file.
     * @return file path.
     */
    String getFilePath();

    /**
     * Get file name of the associated file.
     * @return file name.
     */
    String getFileName();


    /**
     * Position the file marker at a given byte position.
     * <p/>
     * The file marker is used to record how far through the file
     * the byte sequence matching algorithm has got.
     *
     * @param markerPosition The byte number in the file at which to position the marker
     */
    void setFileMarker(long markerPosition);

    /**
     * Gets the current position of the file marker.
     *
     * @return the current position of the file marker
     */
    long getFileMarker();

    /**
     * Get a byte from file.
     *
     * @param fileIndex position of required byte in the file
     * @return the byte at position <code>fileIndex</code> in the file
     */
    byte getByte(long fileIndex);

    /**
     * This is provided to avoid having to call getByte on this class.
     * Since getting bytes is called orders of magnitude more than anything
     * else it is extremely performance sensitive.
     * If the implementing class has direct access to bytes, return itself.
     * If not, return a child object which does implementing the 
     * net.domesdaybook.reader interface instead. 
     * @return An object which can read bytes.
     */
    net.domesdaybook.reader.ByteReader getReader();
    
    /**
     * Returns the number of bytes in the file.
     * @return number of bytes in the file.
     */
    long getNumBytes();
    /**
     * Returns the byte array buffer.
     *
     * @return the buffer associated with the file
     */
    byte[] getbuffer();

    /**
     * Closes any files associated with the ByteReader.
     */
    void close();
}
