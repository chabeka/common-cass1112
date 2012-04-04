
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.IdentificationResult;
import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.RequestIdentifier;
import fr.urssaf.image.commons.commons.pronom.exemple.interfaces.RequestMetaData;

public class IdentificationResultImpl implements IdentificationResult {
    
    private String puid;
    private String name;
    private String mimeType;
    private String version;
    private String extId;
    private IdentificationMethod method;
    private RequestIdentifier identifier;
    private RequestMetaData requestMetaData;
    
    /**
     * @return the puid
     */
    public final String getPuid() {
        return puid;
    }
    
    /**
     * @param puid the puid to set
     */
    public final void setPuid(String puid) {
        this.puid = puid;
    }
    
    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the mimeType
     */
    public final String getMimeType() {
        return mimeType;
    }
    
    /**
     * @param mimeType the mimeType to set
     */
    public final void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    

    /**
     * @return the extId
     */
    public final String getExtId() {
        return extId;
    }

    /**
     * @param extId the extId to set
     */
    public final void setExtId(String extId) {
        this.extId = extId;
    }

    /**
     * 
     * @return The version of the file format.
     */
    public final String getVersion() {
        return version;
    }
    
    /**
     * 
     * @param version The file format version.
     */
    public final void setVersion(String version) {
        this.version = version;
    }
    
    /**
     * @return the method
     */
    public final IdentificationMethod getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public final void setMethod(IdentificationMethod method) {
        this.method = method;
    }

    /**
     * @param requestMetaData the requestMetaData to set
     */
    public final void setRequestMetaData(RequestMetaData requestMetaData) {
        this.requestMetaData = requestMetaData;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
   public final RequestMetaData getMetaData() {
        return requestMetaData;
    }
    
    /**
     * @param identifier the identifier to set
     */
    public final void setIdentifier(RequestIdentifier identifier) {
        this.identifier = identifier;
    }
    
    /**
     * @return the identifier
     */
    public final RequestIdentifier getIdentifier() {
        return identifier;
    }

}
