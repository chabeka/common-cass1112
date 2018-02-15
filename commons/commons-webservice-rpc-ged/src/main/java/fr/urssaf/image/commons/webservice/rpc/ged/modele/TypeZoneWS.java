/**
 * TypeZoneWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.ged.modele;


/**
 * Element contenant la description d'un type zone
 */
@SuppressWarnings("all")
public class TypeZoneWS  implements java.io.Serializable {
    private java.lang.String code;

    private boolean obligatoire;

    private int tailleMax;

    private fr.urssaf.image.commons.webservice.rpc.ged.modele.Format format;

    public TypeZoneWS() {
    }

    public TypeZoneWS(
           java.lang.String code,
           boolean obligatoire,
           int tailleMax,
           fr.urssaf.image.commons.webservice.rpc.ged.modele.Format format) {
           this.code = code;
           this.obligatoire = obligatoire;
           this.tailleMax = tailleMax;
           this.format = format;
    }


    /**
     * Gets the code value for this TypeZoneWS.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this TypeZoneWS.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the obligatoire value for this TypeZoneWS.
     * 
     * @return obligatoire
     */
    public boolean isObligatoire() {
        return obligatoire;
    }


    /**
     * Sets the obligatoire value for this TypeZoneWS.
     * 
     * @param obligatoire
     */
    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }


    /**
     * Gets the tailleMax value for this TypeZoneWS.
     * 
     * @return tailleMax
     */
    public int getTailleMax() {
        return tailleMax;
    }


    /**
     * Sets the tailleMax value for this TypeZoneWS.
     * 
     * @param tailleMax
     */
    public void setTailleMax(int tailleMax) {
        this.tailleMax = tailleMax;
    }


    /**
     * Gets the format value for this TypeZoneWS.
     * 
     * @return format
     */
    public fr.urssaf.image.commons.webservice.rpc.ged.modele.Format getFormat() {
        return format;
    }


    /**
     * Sets the format value for this TypeZoneWS.
     * 
     * @param format
     */
    public void setFormat(fr.urssaf.image.commons.webservice.rpc.ged.modele.Format format) {
        this.format = format;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TypeZoneWS)) return false;
        TypeZoneWS other = (TypeZoneWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.code==null && other.getCode()==null) || 
             (this.code!=null &&
              this.code.equals(other.getCode()))) &&
            this.obligatoire == other.isObligatoire() &&
            this.tailleMax == other.getTailleMax() &&
            ((this.format==null && other.getFormat()==null) || 
             (this.format!=null &&
              this.format.equals(other.getFormat())));
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
        if (getCode() != null) {
            _hashCode += getCode().hashCode();
        }
        _hashCode += (isObligatoire() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += getTailleMax();
        if (getFormat() != null) {
            _hashCode += getFormat().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TypeZoneWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GedImage.cirtil.cer69.recouv/GedImage.xsd1", "TypeZoneWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("code");
        elemField.setXmlName(new javax.xml.namespace.QName("", "code"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obligatoire");
        elemField.setXmlName(new javax.xml.namespace.QName("", "obligatoire"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tailleMax");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tailleMax"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("format");
        elemField.setXmlName(new javax.xml.namespace.QName("", "format"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://GedImage.cirtil.cer69.recouv/GedImage.xsd1", "Format"));
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
