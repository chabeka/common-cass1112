/**
 * Index.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Element contenant les informations caracterisant un index
 */
public class Index  implements java.io.Serializable {
    private java.lang.String libelle;

    private java.lang.String valeur;

    public Index() {
    }

    public Index(
           java.lang.String libelle,
           java.lang.String valeur) {
           this.libelle = libelle;
           this.valeur = valeur;
    }


    /**
     * Gets the libelle value for this Index.
     * 
     * @return libelle
     */
    public java.lang.String getLibelle() {
        return libelle;
    }


    /**
     * Sets the libelle value for this Index.
     * 
     * @param libelle
     */
    public void setLibelle(java.lang.String libelle) {
        this.libelle = libelle;
    }


    /**
     * Gets the valeur value for this Index.
     * 
     * @return valeur
     */
    public java.lang.String getValeur() {
        return valeur;
    }


    /**
     * Sets the valeur value for this Index.
     * 
     * @param valeur
     */
    public void setValeur(java.lang.String valeur) {
        this.valeur = valeur;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Index)) return false;
        Index other = (Index) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.libelle==null && other.getLibelle()==null) || 
             (this.libelle!=null &&
              this.libelle.equals(other.getLibelle()))) &&
            ((this.valeur==null && other.getValeur()==null) || 
             (this.valeur!=null &&
              this.valeur.equals(other.getValeur())));
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
        if (getLibelle() != null) {
            _hashCode += getLibelle().hashCode();
        }
        if (getValeur() != null) {
            _hashCode += getValeur().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Index.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "Index"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("libelle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "libelle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("valeur");
        elemField.setXmlName(new javax.xml.namespace.QName("", "valeur"));
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
