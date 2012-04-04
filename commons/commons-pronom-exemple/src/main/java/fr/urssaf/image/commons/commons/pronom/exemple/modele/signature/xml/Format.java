
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

public class Format {

    /** The NULL format. */
    public static final Format NULL = nullFormat();
    private String puid;
    private String mimeType;
    private String name;
    private String version;
    
    /**
     * @return the puid
     */
    public final String getPuid() {
        return this.equals(NULL) ? null : puid;
    }

    /**
     * @param puid the puid to set
     */
    public final void setPuid(String puid) {
        this.puid = puid;
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
     * @return the version
     */
    public final String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public final void setVersion(String version) {
        this.version = version;
    }

    /**
     * Null formats are an entry in the database to represent the absence of a format!
     * 
     * This is only due to performance reasons: it is quicker to do an inner join from
     * the profile resources to the format identifications, but this requires that the
     * profile resources always have something to link to.  Hence, the NULL puid.
     * 
     * It's properties are blank strings, otherwise reports are confused: e.g things don't
     * have mime types if they aren't identified (NULL puid), and also don't have mime types
     * if the identified format doesn't have a mime-type.  In both cases, the reporting on
     * mime types should be consistent - so a blank string is preferred rather than an actual
     * null value.
     * @return the null Format.
     */
    private static Format nullFormat() {
        Format fmt = new Format();
        fmt.setPuid("");
        fmt.setName("");
        fmt.setMimeType("");
        fmt.setVersion("");
        return fmt;
    }
    
}
