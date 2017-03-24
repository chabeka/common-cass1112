/**
 * PreDocument.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Element contenant l'identifiant metier du document et son nom de
 * fichier
 */
public class PreDocument  implements java.io.Serializable {
    private java.lang.String idDocument;

    private java.lang.String nomFichier;

    private java.lang.String rangDocument;

    private java.lang.String codeOrganisme;

    public PreDocument() {
    }

    public PreDocument(
           java.lang.String idDocument,
           java.lang.String nomFichier,
           java.lang.String rangDocument,
           java.lang.String codeOrganisme) {
           this.idDocument = idDocument;
           this.nomFichier = nomFichier;
           this.rangDocument = rangDocument;
           this.codeOrganisme = codeOrganisme;
    }


    /**
     * Gets the idDocument value for this PreDocument.
     * 
     * @return idDocument
     */
    public java.lang.String getIdDocument() {
        return idDocument;
    }


    /**
     * Sets the idDocument value for this PreDocument.
     * 
     * @param idDocument
     */
    public void setIdDocument(java.lang.String idDocument) {
        this.idDocument = idDocument;
    }


    /**
     * Gets the nomFichier value for this PreDocument.
     * 
     * @return nomFichier
     */
    public java.lang.String getNomFichier() {
        return nomFichier;
    }


    /**
     * Sets the nomFichier value for this PreDocument.
     * 
     * @param nomFichier
     */
    public void setNomFichier(java.lang.String nomFichier) {
        this.nomFichier = nomFichier;
    }


    /**
     * Gets the rangDocument value for this PreDocument.
     * 
     * @return rangDocument
     */
    public java.lang.String getRangDocument() {
        return rangDocument;
    }


    /**
     * Sets the rangDocument value for this PreDocument.
     * 
     * @param rangDocument
     */
    public void setRangDocument(java.lang.String rangDocument) {
        this.rangDocument = rangDocument;
    }


    /**
     * Gets the codeOrganisme value for this PreDocument.
     * 
     * @return codeOrganisme
     */
    public java.lang.String getCodeOrganisme() {
        return codeOrganisme;
    }


    /**
     * Sets the codeOrganisme value for this PreDocument.
     * 
     * @param codeOrganisme
     */
    public void setCodeOrganisme(java.lang.String codeOrganisme) {
        this.codeOrganisme = codeOrganisme;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PreDocument)) return false;
        PreDocument other = (PreDocument) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idDocument==null && other.getIdDocument()==null) || 
             (this.idDocument!=null &&
              this.idDocument.equals(other.getIdDocument()))) &&
            ((this.nomFichier==null && other.getNomFichier()==null) || 
             (this.nomFichier!=null &&
              this.nomFichier.equals(other.getNomFichier()))) &&
            ((this.rangDocument==null && other.getRangDocument()==null) || 
             (this.rangDocument!=null &&
              this.rangDocument.equals(other.getRangDocument()))) &&
            ((this.codeOrganisme==null && other.getCodeOrganisme()==null) || 
             (this.codeOrganisme!=null &&
              this.codeOrganisme.equals(other.getCodeOrganisme())));
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
        if (getIdDocument() != null) {
            _hashCode += getIdDocument().hashCode();
        }
        if (getNomFichier() != null) {
            _hashCode += getNomFichier().hashCode();
        }
        if (getRangDocument() != null) {
            _hashCode += getRangDocument().hashCode();
        }
        if (getCodeOrganisme() != null) {
            _hashCode += getCodeOrganisme().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PreDocument.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "PreDocument"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idDocument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nomFichier");
        elemField.setXmlName(new javax.xml.namespace.QName("", "nomFichier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rangDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "rangDocument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeOrganisme");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codeOrganisme"));
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
