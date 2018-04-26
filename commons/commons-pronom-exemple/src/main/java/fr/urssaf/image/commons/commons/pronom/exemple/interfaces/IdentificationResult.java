
package fr.urssaf.image.commons.commons.pronom.exemple.interfaces;

import fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml.IdentificationMethod;


/**
 * Encapsuler l'identification du fichier.
 */
public interface IdentificationResult {
    
    /**
     * @return the PUID
     */
    String getPuid();
    
    /**
     * 
     * @return the external ID
     */
    String getExtId();
    
    /**
     * 
     * @return the name of the format
     */
    String getName();
    
    /**
     * 
     * @return the mime types
     */
    String getMimeType();

    
    /**
     * 
     * @return The version.
     */
    String getVersion();
    
    /**
     * 
     * @return the identification method
     */
    IdentificationMethod getMethod();
    
    /**
     * 
     * @return the request meta data.
     */
    RequestMetaData getMetaData();
    
    /**
     * @return the request identifier
     * @return
     */
    RequestIdentifier getIdentifier();

}
