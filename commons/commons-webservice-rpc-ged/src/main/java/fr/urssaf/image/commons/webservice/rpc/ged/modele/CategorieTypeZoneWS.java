/**
 * CategorieTypeZoneWS.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.ged.modele;


/**
 * Element contenant la description d'un type zone ainsi que l'indice
 * de sa catégorie
 */
@SuppressWarnings("all")
public class CategorieTypeZoneWS  implements java.io.Serializable {
    private int indice;

    private java.lang.String code;

    private boolean obligatoire;

    private int tailleMax;

    private fr.urssaf.image.commons.webservice.rpc.ged.modele.Format format;

    public CategorieTypeZoneWS() {
    }

    public CategorieTypeZoneWS(
           int indice,
           java.lang.String code,
           boolean obligatoire,
           int tailleMax,
           fr.urssaf.image.commons.webservice.rpc.ged.modele.Format format) {
           this.indice = indice;
           this.code = code;
           this.obligatoire = obligatoire;
           this.tailleMax = tailleMax;
           this.format = format;
    }


    /**
     * Gets the indice value for this CategorieTypeZoneWS.
     * 
     * @return indice
     */
    public int getIndice() {
        return indice;
    }


    /**
     * Sets the indice value for this CategorieTypeZoneWS.
     * 
     * @param indice
     */
    public void setIndice(int indice) {
        this.indice = indice;
    }


    /**
     * Gets the code value for this CategorieTypeZoneWS.
     * 
     * @return code
     */
    public java.lang.String getCode() {
        return code;
    }


    /**
     * Sets the code value for this CategorieTypeZoneWS.
     * 
     * @param code
     */
    public void setCode(java.lang.String code) {
        this.code = code;
    }


    /**
     * Gets the obligatoire value for this CategorieTypeZoneWS.
     * 
     * @return obligatoire
     */
    public boolean isObligatoire() {
        return obligatoire;
    }


    /**
     * Sets the obligatoire value for this CategorieTypeZoneWS.
     * 
     * @param obligatoire
     */
    public void setObligatoire(boolean obligatoire) {
        this.obligatoire = obligatoire;
    }


    /**
     * Gets the tailleMax value for this CategorieTypeZoneWS.
     * 
     * @return tailleMax
     */
    public int getTailleMax() {
        return tailleMax;
    }


    /**
     * Sets the tailleMax value for this CategorieTypeZoneWS.
     * 
     * @param tailleMax
     */
    public void setTailleMax(int tailleMax) {
        this.tailleMax = tailleMax;
    }


    /**
     * Gets the format value for this CategorieTypeZoneWS.
     * 
     * @return format
     */
    public fr.urssaf.image.commons.webservice.rpc.ged.modele.Format getFormat() {
        return format;
    }


    /**
     * Sets the format value for this CategorieTypeZoneWS.
     * 
     * @param format
     */
    public void setFormat(fr.urssaf.image.commons.webservice.rpc.ged.modele.Format format) {
        this.format = format;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CategorieTypeZoneWS)) return false;
        CategorieTypeZoneWS other = (CategorieTypeZoneWS) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.indice == other.getIndice() &&
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
        _hashCode += getIndice();
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
        new org.apache.axis.description.TypeDesc(CategorieTypeZoneWS.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://GedImage.cirtil.cer69.recouv/GedImage.xsd1", "CategorieTypeZoneWS"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("indice");
        elemField.setXmlName(new javax.xml.namespace.QName("", "indice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
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
