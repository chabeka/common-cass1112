/**
 * NumVersionDate.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.sae.rnd.ws.adrn.modele;

// CHECKSTYLE:OFF
@SuppressWarnings("all") 
public class NumVersionDate  implements java.io.Serializable {
    private java.lang.String numeroVersion;

    private java.lang.String dateDebutVersion;

    private java.lang.String dateFinVersion;

    public NumVersionDate() {
    }

    public NumVersionDate(
           java.lang.String numeroVersion,
           java.lang.String dateDebutVersion,
           java.lang.String dateFinVersion) {
           this.numeroVersion = numeroVersion;
           this.dateDebutVersion = dateDebutVersion;
           this.dateFinVersion = dateFinVersion;
    }


    /**
     * Gets the numeroVersion value for this NumVersionDate.
     * 
     * @return numeroVersion
     */
    public java.lang.String getNumeroVersion() {
        return numeroVersion;
    }


    /**
     * Sets the numeroVersion value for this NumVersionDate.
     * 
     * @param numeroVersion
     */
    public void setNumeroVersion(java.lang.String numeroVersion) {
        this.numeroVersion = numeroVersion;
    }


    /**
     * Gets the dateDebutVersion value for this NumVersionDate.
     * 
     * @return dateDebutVersion
     */
    public java.lang.String getDateDebutVersion() {
        return dateDebutVersion;
    }


    /**
     * Sets the dateDebutVersion value for this NumVersionDate.
     * 
     * @param dateDebutVersion
     */
    public void setDateDebutVersion(java.lang.String dateDebutVersion) {
        this.dateDebutVersion = dateDebutVersion;
    }


    /**
     * Gets the dateFinVersion value for this NumVersionDate.
     * 
     * @return dateFinVersion
     */
    public java.lang.String getDateFinVersion() {
        return dateFinVersion;
    }


    /**
     * Sets the dateFinVersion value for this NumVersionDate.
     * 
     * @param dateFinVersion
     */
    public void setDateFinVersion(java.lang.String dateFinVersion) {
        this.dateFinVersion = dateFinVersion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof NumVersionDate)) return false;
        NumVersionDate other = (NumVersionDate) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.numeroVersion==null && other.getNumeroVersion()==null) || 
             (this.numeroVersion!=null &&
              this.numeroVersion.equals(other.getNumeroVersion()))) &&
            ((this.dateDebutVersion==null && other.getDateDebutVersion()==null) || 
             (this.dateDebutVersion!=null &&
              this.dateDebutVersion.equals(other.getDateDebutVersion()))) &&
            ((this.dateFinVersion==null && other.getDateFinVersion()==null) || 
             (this.dateFinVersion!=null &&
              this.dateFinVersion.equals(other.getDateFinVersion())));
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
        if (getNumeroVersion() != null) {
            _hashCode += getNumeroVersion().hashCode();
        }
        if (getDateDebutVersion() != null) {
            _hashCode += getDateDebutVersion().hashCode();
        }
        if (getDateFinVersion() != null) {
            _hashCode += getDateFinVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(NumVersionDate.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:InterfaceDuplication", "NumVersionDate"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("numeroVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "numeroVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateDebutVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateDebutVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dateFinVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dateFinVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
