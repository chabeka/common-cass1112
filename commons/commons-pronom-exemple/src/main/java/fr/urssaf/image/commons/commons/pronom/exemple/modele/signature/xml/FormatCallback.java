
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;


/**
 * Callback interface to implemented by classes which handle Formats.
 *
 */
public interface FormatCallback {
    
    /**
     * Invoked when a format needs handling.
     * @param format the format to handle
     */
    void onFormat(Format format);

}
