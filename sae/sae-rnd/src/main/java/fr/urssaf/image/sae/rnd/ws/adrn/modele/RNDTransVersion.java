/**
 * RNDTransVersion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class RNDTransVersion  implements java.io.Serializable {
    private fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDVersion version;

    private fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[] listeCorrespondance;

    public RNDTransVersion() {
    }

    public RNDTransVersion(
           fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDVersion version,
           fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[] listeCorrespondance) {
           this.version = version;
           this.listeCorrespondance = listeCorrespondance;
    }


    /**
     * Gets the version value for this RNDTransVersion.
     * 
     * @return version
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDVersion getVersion() {
        return version;
    }


    /**
     * Sets the version value for this RNDTransVersion.
     * 
     * @param version
     */
    public void setVersion(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDVersion version) {
        this.version = version;
    }


    /**
     * Gets the listeCorrespondance value for this RNDTransVersion.
     * 
     * @return listeCorrespondance
     */
    public fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[] getListeCorrespondance() {
        return listeCorrespondance;
    }


    /**
     * Sets the listeCorrespondance value for this RNDTransVersion.
     * 
     * @param listeCorrespondance
     */
    public void setListeCorrespondance(fr.urssaf.image.sae.rnd.ws.adrn.modele.RNDCorrespondance[] listeCorrespondance) {
        this.listeCorrespondance = listeCorrespondance;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RNDTransVersion)) return false;
        RNDTransVersion other = (RNDTransVersion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.version==null && other.getVersion()==null) || 
             (this.version!=null &&
              this.version.equals(other.getVersion()))) &&
            ((this.listeCorrespondance==null && other.getListeCorrespondance()==null) || 
             (this.listeCorrespondance!=null &&
              java.util.Arrays.equals(this.listeCorrespondance, other.getListeCorrespondance())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getVersion() != null) {
            _hashCode += getVersion().hashCode();
        }
        if (getListeCorrespondance() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListeCorrespondance());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListeCorrespondance(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RNDTransVersion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDTransVersion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("version");
        elemField.setXmlName(new javax.xml.namespace.QName("", "version"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDVersion"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listeCorrespondance");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listeCorrespondance"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "RNDCorrespondance"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
