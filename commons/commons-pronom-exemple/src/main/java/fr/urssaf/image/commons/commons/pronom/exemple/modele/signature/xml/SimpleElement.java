
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contient les details basics d'un element lu a partir d'un fichier XML.
 */
public class SimpleElement {

    private Log log = LogFactory.getLog(this.getClass());

    private String myText = "";

    /* setters */
    
    /**
     * @param theText the text to set for the element.
     */
    public final void setText(String theText) {
        this.myText += theText;
    }

    /**
     * Implementations override this method.
     * 
     * @param name The name of the attribute to set.
     * @param value The value of the attribute.
     */
    public void setAttributeValue(String name, String value) {
        unknownAttributeWarning(name, this.getElementName());
    }

    /* getters */
    /**
     * @return the text of the element.
     */
    public final String getText() {
        return myText.trim();
    }

    /**
     * 
     * @return The element name.
     */
    public final String getElementName() {
        String className = this.getClass().getName();
        className = className.substring(className.lastIndexOf(".") + 1);
        return className;
    }

    /**
     * method to be overridden in cases where the element content needs 
     * to be specified only when the end of element tag is reached.
     */
    public void completeElementContent() {
    }
    
    /**
     * Displays a special warning for unknown XML attributes when reading
     * XML files.
     *
     * @param unknownAttribute The name of the attribute which was not recognised
     * @param containerElement The name of the element which contains the unrecognised attribute
     */
    public final void unknownAttributeWarning(String unknownAttribute, String containerElement) {
        final String warning = "WARNING: Unknown XML attribute " + unknownAttribute + " found for " + containerElement;
        log.debug(warning);
    }    
    
    /**
     * Displays a general warning.
     *
     * @param theWarning The text to be displayed
     */
    public final void generalWarning(String theWarning) {
        String theMessage = "WARNING: " + theWarning.replaceFirst("java.lang.Exception: ", "");
        log.debug(theMessage);
    }    
    
    /**
     * 
     * @return the log object owned by SimpleElement.
     */
    protected final Log getLog() {
        return log;
    }
}
