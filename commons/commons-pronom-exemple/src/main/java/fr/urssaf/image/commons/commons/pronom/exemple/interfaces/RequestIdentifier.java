
package fr.urssaf.image.commons.commons.pronom.exemple.interfaces;

import java.net.URI;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;


/**
 * 
 * Classe permettant d'encapsuler les informations necessaire pour identifier
 * la source d'une "identification request".
 * 
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class RequestIdentifier {

    @XmlAttribute(name = "NodeId")
    private Long nodeId;
    
    @XmlAttribute(name = "Prefix")
    private String prefix;
    
    @XmlAttribute(name = "ParentId")
    private Long parentId;
    
    @XmlAttribute(name = "ParentPrefix")
    private String parentPrefix;

    @XmlAttribute(name = "AncestorId")
    private Long ancestorId;

    @XmlValue
    private URI uri;
    
    /**
     * Default Constructor. 
     */
    RequestIdentifier() { }
    
    /**
     * 
     * @param uri the URI of the request's data.
     */
    public RequestIdentifier(URI uri) {
        this.uri = uri;
    }

    /**
     * @return the nodeId
     */
    public final Long getNodeId() {
        return nodeId;
    }
    
    /**
     * 
     * @return The prefix.
     */
    public final String getPrefix() {
        return prefix;
    }

    /**
     * @param nodeId the nodeId to set
     */
    public final void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }
    
    /**
     * 
     * @param prefix the prefix to set.
     */
    public final void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    /**
     * Sets the node id and prefix from a ResourceId object.
     * 
     * @param id The resourceId to set.
     */
    public final void setResourceId(ResourceId id) {
        if (id != null) {
            this.nodeId = id.getId();
            this.prefix = id.getPath();
        } else {
            this.nodeId = null;
            this.prefix = "";
        }
    }

    /**
     * @return the parentId
     */
    public final Long getParentId() {
        return parentId;
    }
    
    /**
     * 
     * @return The parent prefix.
     */
    public final String getParentPrefix() {
        return parentPrefix;
    }

    /**
     * @param parentId the parentId to set
     */
    public final void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    /**
     * 
     * @param parentPrefix The parent prefix to set.
     */
    public final void setParentPrefix(String parentPrefix) {
        this.parentPrefix = parentPrefix;
    }

    /**
     * Sets the parent id and prefix from a ResourceId object.
     * 
     * @param id The resourceId to set.
     */
    public final void setParentResourceId(ResourceId id) {
        if (id != null) {
            this.parentId = id.getId();
            this.parentPrefix = id.getPath();
        } else {
            this.parentId = null;
            this.parentPrefix = "";
        }
    }
    
    /**
     * 
     * @return A resourceId for the parent, or null if there is a null parentId.
     */
    public final ResourceId getParentResourceId() {
        return parentId == null ? null : new ResourceId(parentId, parentPrefix);
    }
    
    /**
     * 
     * @return A resourceId for the request, or null if there is a null nodeId.
     */
    public final ResourceId getResourceId() {
        return nodeId == null ? null : new ResourceId(nodeId, prefix);
    }
    
    
    /**
     * The ancestorId is the node id of a containing archival file.
     * It is used when replaying a paused profile, to remove results
     * which need to be recreated.
     * 
     * @return the ancestorId
     */
    public final Long getAncestorId() {
        return ancestorId;
    }

    /**
     * @param ancestorId the ancestorId to set
     */
    public final void setAncestorId(Long ancestorId) {
        this.ancestorId = ancestorId;
    }

    /**
     * @return the uri
     */
    public final URI getUri() {
        return uri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
   public final int hashCode() {
        return new HashCodeBuilder()
            .append(ancestorId)
            .append(nodeId)
            .append(prefix)
            .append(parentId)
            .append(parentPrefix)
            .append(uri)
            .toHashCode();
       //return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
   public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        RequestIdentifier other = (RequestIdentifier) obj;
        
        return new EqualsBuilder()
            .append(ancestorId, other.ancestorId)
            .append(nodeId, other.nodeId)
            .append(prefix, other.prefix)
            .append(parentId, other.parentId)
            .append(parentPrefix, other.parentPrefix)
            .append(uri, other.uri)
            .isEquals();
      // return true;
            
    }

}
