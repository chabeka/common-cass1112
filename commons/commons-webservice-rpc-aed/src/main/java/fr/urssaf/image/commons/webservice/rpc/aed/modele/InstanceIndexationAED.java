/**
 * InstanceIndexationAED.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;

public class InstanceIndexationAED  implements java.io.Serializable {
    private java.lang.String identifiant;

    private fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] listeIndex;

    public InstanceIndexationAED() {
    }

    public InstanceIndexationAED(
           java.lang.String identifiant,
           fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] listeIndex) {
           this.identifiant = identifiant;
           this.listeIndex = listeIndex;
    }


    /**
     * Gets the identifiant value for this InstanceIndexationAED.
     * 
     * @return identifiant
     */
    public java.lang.String getIdentifiant() {
        return identifiant;
    }


    /**
     * Sets the identifiant value for this InstanceIndexationAED.
     * 
     * @param identifiant
     */
    public void setIdentifiant(java.lang.String identifiant) {
        this.identifiant = identifiant;
    }


    /**
     * Gets the listeIndex value for this InstanceIndexationAED.
     * 
     * @return listeIndex
     */
    public fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] getListeIndex() {
        return listeIndex;
    }


    /**
     * Sets the listeIndex value for this InstanceIndexationAED.
     * 
     * @param listeIndex
     */
    public void setListeIndex(fr.urssaf.image.commons.webservice.rpc.aed.modele.Index[] listeIndex) {
        this.listeIndex = listeIndex;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof InstanceIndexationAED)) return false;
        InstanceIndexationAED other = (InstanceIndexationAED) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.identifiant==null && other.getIdentifiant()==null) || 
             (this.identifiant!=null &&
              this.identifiant.equals(other.getIdentifiant()))) &&
            ((this.listeIndex==null && other.getListeIndex()==null) || 
             (this.listeIndex!=null &&
              java.util.Arrays.equals(this.listeIndex, other.getListeIndex())));
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
        if (getIdentifiant() != null) {
            _hashCode += getIdentifiant().hashCode();
        }
        if (getListeIndex() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getListeIndex());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getListeIndex(), i);
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
        new org.apache.axis.description.TypeDesc(InstanceIndexationAED.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "InstanceIndexationAED"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("identifiant");
        elemField.setXmlName(new javax.xml.namespace.QName("", "identifiant"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listeIndex");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listeIndex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Index"));
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
