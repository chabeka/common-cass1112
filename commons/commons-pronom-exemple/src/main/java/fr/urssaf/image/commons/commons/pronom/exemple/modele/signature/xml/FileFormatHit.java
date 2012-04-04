
package fr.urssaf.image.commons.commons.pronom.exemple.modele.signature.xml;

/**
 * Contient la description d'un hit(l'identification d'un format) d'un fichier.
 */
public class FileFormatHit extends SimpleElement {
    
    //hit type constants
    /**
     * Constant.
     */
    public static final int HIT_TYPE_POSITIVE_SPECIFIC = 10;
    /**
     * Constant.
     */
    public static final int HIT_TYPE_POSITIVE_GENERIC = 11;
    /**
     * Constant.
     */
    public static final int HIT_TYPE_TENTATIVE = 12;
    /**
     * Constant.
     */
    public static final int HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC = 15;
    /**
     * Constant.
     */
    public static final String HIT_TYPE_POSITIVE_SPECIFIC_TEXT = "Positive (Specific Format)";
    /**
     * Constant.
     */
    public static final String HIT_TYPE_POSITIVE_GENERIC_TEXT = "Positive (Generic Format)";
    /**
     * Constant.
     */
    public static final String HIT_TYPE_TENTATIVE_TEXT = "Tentative";
    /**
     * Constant.
     */
    public static final String HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT = "Positive";
    
    /**
     * Constant.
     */
    public static final String FILEEXTENSIONWARNING = "Possible file extension mismatch";
    /**
     * Constant.
     */
    public static final String POSITIVEIDENTIFICATIONSTATUS = "Positively identified";
    /**
     * Constant.
     */
    public static final String TENTATIVEIDENTIFICATIONSTATUS = "Tentatively identified";
    /**
     * Constant.
     */
    public static final String UNIDENTIFIEDSTATUS = "Unable to identify";
    
    
    private String myHitWarning = "";
    private int myHitType;
    private FileFormat myHitFileFormat;

    /**
     * Creates a new blank instance of fileFormatHit.
     *
     * @param theFileFormat  The file format which has been identified
     * @param theType        The type of hit i.e. Positive/tentative
     * @param theSpecificity Flag is set to true for Positive specific hits
     * @param theWarning     A warning associated with the hit
     */
    public FileFormatHit(FileFormat theFileFormat, int theType, boolean theSpecificity, String theWarning) {
        myHitFileFormat = theFileFormat;
        if (theType == HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC) {
            if (theSpecificity) {
                myHitType = HIT_TYPE_POSITIVE_SPECIFIC;
            } else {
                myHitType = HIT_TYPE_POSITIVE_GENERIC;
            }
        } else {
            myHitType = theType;
        }
        this.setIdentificationWarning(theWarning);
    }

    /**
     * Default constructor.
     */
    public FileFormatHit() {
    }

    /**
     * Updates the warning message for a hit.
     * <p/>
     * Used by XML reader for IdentificationFile/FileFormatHit/IdentificationWarning element
     *
     * @param theWarning A warning associated with the hit
     */
    public final void setIdentificationWarning(String theWarning) {
        myHitWarning = theWarning;
    }


    /**
     * get the fileFormat for the hit.
     *
     * @return The file format which was hit.
     */
    public final FileFormat getFileFormat() {
        return myHitFileFormat;
    }

    /**
     * get the name of the fileFormat of this hit.
     *
     * @return The name of the file format which was hit.
     */
    public final String getFileFormatName() {
        return myHitFileFormat.getName();
    }

    /**
     * get the version of the fileFormat of this hit.
     *
     * @return the version of the fileFormat of this hit
     */
    public final String getFileFormatVersion() {
        return myHitFileFormat.getVersion();
    }

    /**
     * Get the mime type.
     *
     * @return the mime type.
     */
    public final String getMimeType() {
        return myHitFileFormat.getMimeType();
    }

    /**
     * get the PUID of the fileFormat of this hit.
     *
     * @return the PUID of the fileFormat of this hit.
     */
    public final String getFileFormatPUID() {
        return myHitFileFormat.getPUID();
    }

    /**
     * get the code of the hit type.
     *
     * @return the code of the hit type
     */
    public final int getHitType() {
        return myHitType;
    }

    /**
     * get the name of the hit type.
     *
     * @return the name of the hit type.
     */
    public final String getHitTypeVerbose() {
        String theHitType = "";
        if (myHitType == HIT_TYPE_POSITIVE_GENERIC) {
            theHitType = HIT_TYPE_POSITIVE_GENERIC_TEXT;
        } else if (myHitType == HIT_TYPE_POSITIVE_SPECIFIC) {
            theHitType = HIT_TYPE_POSITIVE_SPECIFIC_TEXT;
        } else if (myHitType == HIT_TYPE_TENTATIVE) {
            theHitType = HIT_TYPE_TENTATIVE_TEXT;
        } else if (myHitType == HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC) {
            theHitType = HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT;
        }
        return theHitType;
    }

    /**
     * get any warning associated with the hit.
     *
     * @return any warning associated with the hit
     */
    public final String getHitWarning() {
        return myHitWarning;
    }

    /**
     * For positive hits, this returns true if hit is Specific
     * or returns false if hit is Generic.
     * Meaningless for Tentative hits. (though returns false)
     *
     * @return true if hit is Specific, false if hit is Generic
     */
    public final boolean isSpecific() {
        return myHitType == HIT_TYPE_POSITIVE_SPECIFIC;
    }


    /**
     * Populates the details of the IdentificationFile when 
     * it is read in from XML file.
     *
     * @param theName  Name of the attribute read in
     * @param theValue Value of the attribute read in
     */
    @Override
   public final void setAttributeValue(String theName, String theValue) {
        if ("HitStatus".equals(theName)) {
            this.setStatus(theValue);
        } else if ("FormatName".equals(theName)) {
            this.setName(theValue);
        } else if ("FormatVersion".equals(theName)) {
            this.setVersion(theValue);
        } else if ("FormatPUID".equals(theName)) {
            this.setPUID(theValue);
        } else if ("HitWarning".equals(theName)) {
            this.setIdentificationWarning(theValue);
        } else {
            unknownAttributeWarning(theName, this.getElementName());
        }
    }

    /**
     * Set hit status.  
     * Used by XML reader for IdentificationFile/FileFormatHit/Status element
     *
     * @param value The value of the hit.status.
     */
    public final void setStatus(String value) {
        //String value = element.getText();
        if (value.equals(HIT_TYPE_POSITIVE_GENERIC_TEXT)) {
            myHitType = HIT_TYPE_POSITIVE_GENERIC;
        } else if (value.equals(HIT_TYPE_POSITIVE_SPECIFIC_TEXT)) {
            myHitType = HIT_TYPE_POSITIVE_SPECIFIC;
        } else if (value.equals(HIT_TYPE_TENTATIVE_TEXT)) {
            myHitType = HIT_TYPE_TENTATIVE;
        } else if (value.equals(HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC_TEXT)) {
            myHitType = HIT_TYPE_POSITIVE_GENERIC_OR_SPECIFIC;
        } else {
            generalWarning("Unknown hit status listed: " + value);
        }
    }

    /**
     * Set hit format name.  
     * Used by XML reader for IdentificationFile/FileFormatHit/Name element
     *
     * @param value The value of the name.
     */
    public final void setName(String value) {
        //if necessary, this creates a new dummy File format
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("Name", value);
    }

    /**
     * Set hit format version.  
     * Used by XML reader for IdentificationFile/FileFormatHit/Version element
     *
     * @param value The value of the version.
     */
    public final void setVersion(String value) {
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("Version", value);
    }

    /**
     * Set hit format PUID.  
     * Used by XML reader for IdentificationFile/FileFormatHit/PUID element
     *
     * @param value The value of the PUID.
     */
    public final void setPUID(String value) {
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("PUID", value);
    }

    /**
     * Set hit format MIME type.
     * Used by XML reader for IdentificationFile/FileFormatHit/PUID element
     *
     * @param value The value of the mime type.
     */
    public final void setMimeType(String value) {
        if (myHitFileFormat == null) {
            myHitFileFormat = new FileFormat();
        }
        myHitFileFormat.setAttributeValue("MIMEType", value);
    }


}
