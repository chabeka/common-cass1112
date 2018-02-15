
package fr.urssaf.image.commons.commons.pronom.exemple.interfaces;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Classe contenant les informations pour identifier un resource node.
 */
public class ResourceId {

    private final long id;
    private final String path;
    
    /**
     * 
     * @param id The id of the resource.
     * @param path A representation of the path of the resource.
     */
    public ResourceId(long id, String path) {
        this.id = id;
        this.path = path;
    }

    /**
     * 
     * @return The id of the resource.
     */
    public final long getId() {
        return id;
    }

    /**
     * 
     * @return The path of the resource.
     */
    public final String getPath() {
        return path;
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
   public final int hashCode() {
        return new HashCodeBuilder()
            .append(id)
            .append(path)
            .toHashCode();
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
        ResourceId other = (ResourceId) obj;
        
        return new EqualsBuilder()
            .append(id, other.id)
            .append(path, other.path)
            .isEquals();
    }    
    
}
