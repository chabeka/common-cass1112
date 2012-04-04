
package fr.urssaf.image.commons.commons.pronom.exemple.interfaces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.domesdaybook.reader.ByteReader;

/**
 * Interface pour objets pour pouvoir recuperer les bytes d'un inputStream.
 * Elle etends {@link ByteReader}
 * 
 */
public interface CachedBytes extends ByteReader {

    
    /**
     * Sets the optional Random Access File for the whole binary.
     * @param sourceFile the binary data source.
     * @throws IOException if the source file was not found or could not close previous file.
     */
    void setSourceFile(File sourceFile) throws IOException;

    /**
     * Closes the internal Random Access File.
     * @throws IOException if the file could not be closed.
     */
    void close() throws IOException;

    /**
     * @return the source input stream
     * @throws IOException if there was an exception reading the source
     */
    InputStream getSourceInputStream() throws IOException;

    /**
     * Returns a source file (if any) for this cached binary.
     * If the file size is less than the size of a single cache block, 
     * the source file may not be set, and this method will return null.
     *  
     * @return The source file, or null if not set.
     */
    File getSourceFile();

}
