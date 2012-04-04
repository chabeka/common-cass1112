
package fr.urssaf.image.commons.commons.pronom.exemple.resources;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.CachedBytes;

public final class CachedByteArray implements CachedBytes {

    private File source;
    private final byte[] bytes;
    private int maxSize;
    
    /**
     * 
     * @param bytes A byte array containing the bytes to read.
     * @param maxSize - the number of bytes possible to read.
     * Note: this is not checked on reading bytes for
     * performance reasons.  It is only used to return
     * an appropriate input stream if requested.
     */
    public CachedByteArray(byte[] bytes, int maxSize) {
        this.bytes = bytes;
        this.maxSize = maxSize;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getSourceFile() {
        return source;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getSourceInputStream() throws IOException {
        byte[] buffer;
        if (bytes.length > maxSize) {
            buffer = Arrays.copyOf(bytes, maxSize);
        } else {
            buffer = bytes;
        }
        return new ByteArrayInputStream(buffer);
        // return new ByteArrayInputStream(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSourceFile(File sourceFile) throws FileNotFoundException {
        this.source = sourceFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte readByte(long position) {
        return bytes[(int) position];
    }

}
