
package fr.urssaf.image.commons.commons.pronom.exemple.interfaces;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml.IdentificationRequest;

/**
 * Une collection de Identification results.
 *
 */
public class IdentificationResultCollection {

    private Map<String, IdentificationResult> puidMap = new HashMap<String, IdentificationResult>();
    private List<IdentificationResult> results = new ArrayList<IdentificationResult>();
    private URI resourceUri;
    private Long fileLength;
    private ResourceId correlationId;
    private boolean archive;
    private RequestMetaData requestMetaData;
    private Boolean fileExtMismatch = false;
    
    /**
     * 
     * @param request the original request.
     */
    public IdentificationResultCollection(IdentificationRequest request) {
        correlationId = request.getIdentifier().getParentResourceId();
        resourceUri = request.getIdentifier().getUri();
    }

    /**
     * Adds a result.
     * @param result the result to add
     */
    public final void addResult(IdentificationResult result) {
        // Don't add the same puid more than once to a result collection.
        final String puid = result.getPuid();
        if (!puidMap.containsKey(puid)) {
            puidMap.put(puid, result);
            results.add(result);
        }
    }
    
    /**
     * Removes a result.
     * 
     * @param result The result to remove.
     */
    public final void removeResult(IdentificationResult result) {
        if (results.remove(result)) {
            final String puid = result.getPuid();
            puidMap.remove(puid);
        }
    }    
    
    /**
     * 
     * @return a Collection of all results added.
     */
    public final List<IdentificationResult> getResults() {
        return results;
    }
    
    /**
     * @return the jobCorrelationId
     */
    public final ResourceId getCorrelationId() {
        return correlationId;
    }

    /**
     * The URI of the request.
     * @param uri the uri of the request
     */
    public final void setUri(URI uri) {
        resourceUri = uri;
    }

    /**
     * 
     * @return the URI of the request
     */
    public final URI getUri() {
        return resourceUri;
    }
    
    /**
     * The file length of the resource.
     * @param fileLength the length of the file
     */
    public final void setFileLength(Long fileLength) {
        this.fileLength = fileLength;
    }
    
    /**
     * @return The file lenghth of the resource
     */
    public final Long getFileLength() {
        return fileLength;
    }
    
    /**
     * @param archive true if the identification idicated an archive format; false otherwise
     */
    public final void setArchive(boolean archive) {
        this.archive = archive;
    }
    
    /**
     * @return the archive
     */
    public final boolean isArchive() {
        return archive;
    }
    
    /**
     * @param value Whether there is a file extension mismatch
     */
    public final void setExtensionMismatch(Boolean value) {
        fileExtMismatch = value;
    }
    
    /**
     * 
     * @return whether there is a file extension mismatch.
     */
    public final Boolean getExtensionMismatch() {
        return fileExtMismatch;
    }
    
    /**
     * @param requestMetaData the requestMetaData to set
     */
    public final void setRequestMetaData(RequestMetaData requestMetaData) {
        this.requestMetaData = requestMetaData;
    }
    
    /**
     * @return the requestMetaData
     */
    public final RequestMetaData getRequestMetaData() {
        return requestMetaData;
    }


    
}
