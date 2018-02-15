
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Contient une collection de {@link FileFormat}.
 */
public class FileFormatCollection extends SimpleElement {
    private List<FileFormat> formats = new ArrayList<FileFormat>();
    private Map<String, FileFormat> puidFormats = new HashMap<String, FileFormat>();
    
    /* setters */
    

    /**
     * @param format A file format to add to the collection.
     */
    public final void addFileFormat(final FileFormat format) {
        formats.add(format);
        puidFormats.put(format.getPUID(), format);
    }

    /**
     * 
     * @param formatList A list of file formats to set for the collection.
     */
    public final void setFileFormats(final List<FileFormat> formatList) {
        formats.clear();
        puidFormats.clear();
        for (FileFormat format : formatList) {
            addFileFormat(format);
        }
    }

    /* getters */
    
    /**
     * @return The list of file formats held by this collection.
     */
    public final List<FileFormat> getFileFormats() {
        return formats;
    }
    
    /**
     * 
     * @param puid The puid
     * @return A file format for that puid.
     */
    public final FileFormat getFormatForPUID(final String puid) {
        return puidFormats.get(puid);
    }
    
}
