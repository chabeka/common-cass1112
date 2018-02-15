/**
 * DocumentSynchro.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package fr.urssaf.image.commons.webservice.rpc.aed.modele;


/**
 * Correspond a un document, rejete ou non
 */
public class DocumentSynchro  implements java.io.Serializable {
    private java.lang.String idArchivage;

    private java.lang.String etat;

    private java.lang.String motifRejet;

    private java.lang.String idDocument;

    private java.lang.String type;

    private java.lang.String codeRnd;

    public DocumentSynchro() {
    }

    public DocumentSynchro(
           java.lang.String idArchivage,
           java.lang.String etat,
           java.lang.String motifRejet,
           java.lang.String idDocument,
           java.lang.String type,
           java.lang.String codeRnd) {
           this.idArchivage = idArchivage;
           this.etat = etat;
           this.motifRejet = motifRejet;
           this.idDocument = idDocument;
           this.type = type;
           this.codeRnd = codeRnd;
    }


    /**
     * Gets the idArchivage value for this DocumentSynchro.
     * 
     * @return idArchivage
     */
    public java.lang.String getIdArchivage() {
        return idArchivage;
    }


    /**
     * Sets the idArchivage value for this DocumentSynchro.
     * 
     * @param idArchivage
     */
    public void setIdArchivage(java.lang.String idArchivage) {
        this.idArchivage = idArchivage;
    }


    /**
     * Gets the etat value for this DocumentSynchro.
     * 
     * @return etat
     */
    public java.lang.String getEtat() {
        return etat;
    }


    /**
     * Sets the etat value for this DocumentSynchro.
     * 
     * @param etat
     */
    public void setEtat(java.lang.String etat) {
        this.etat = etat;
    }


    /**
     * Gets the motifRejet value for this DocumentSynchro.
     * 
     * @return motifRejet
     */
    public java.lang.String getMotifRejet() {
        return motifRejet;
    }


    /**
     * Sets the motifRejet value for this DocumentSynchro.
     * 
     * @param motifRejet
     */
    public void setMotifRejet(java.lang.String motifRejet) {
        this.motifRejet = motifRejet;
    }


    /**
     * Gets the idDocument value for this DocumentSynchro.
     * 
     * @return idDocument
     */
    public java.lang.String getIdDocument() {
        return idDocument;
    }


    /**
     * Sets the idDocument value for this DocumentSynchro.
     * 
     * @param idDocument
     */
    public void setIdDocument(java.lang.String idDocument) {
        this.idDocument = idDocument;
    }


    /**
     * Gets the type value for this DocumentSynchro.
     * 
     * @return type
     */
    public java.lang.String getType() {
        return type;
    }


    /**
     * Sets the type value for this DocumentSynchro.
     * 
     * @param type
     */
    public void setType(java.lang.String type) {
        this.type = type;
    }


    /**
     * Gets the codeRnd value for this DocumentSynchro.
     * 
     * @return codeRnd
     */
    public java.lang.String getCodeRnd() {
        return codeRnd;
    }


    /**
     * Sets the codeRnd value for this DocumentSynchro.
     * 
     * @param codeRnd
     */
    public void setCodeRnd(java.lang.String codeRnd) {
        this.codeRnd = codeRnd;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DocumentSynchro)) return false;
        DocumentSynchro other = (DocumentSynchro) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.idArchivage==null && other.getIdArchivage()==null) || 
             (this.idArchivage!=null &&
              this.idArchivage.equals(other.getIdArchivage()))) &&
            ((this.etat==null && other.getEtat()==null) || 
             (this.etat!=null &&
              this.etat.equals(other.getEtat()))) &&
            ((this.motifRejet==null && other.getMotifRejet()==null) || 
             (this.motifRejet!=null &&
              this.motifRejet.equals(other.getMotifRejet()))) &&
            ((this.idDocument==null && other.getIdDocument()==null) || 
             (this.idDocument!=null &&
              this.idDocument.equals(other.getIdDocument()))) &&
            ((this.type==null && other.getType()==null) || 
             (this.type!=null &&
              this.type.equals(other.getType()))) &&
            ((this.codeRnd==null && other.getCodeRnd()==null) || 
             (this.codeRnd!=null &&
              this.codeRnd.equals(other.getCodeRnd())));
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
        if (getIdArchivage() != null) {
            _hashCode += getIdArchivage().hashCode();
        }
        if (getEtat() != null) {
            _hashCode += getEtat().hashCode();
        }
        if (getMotifRejet() != null) {
            _hashCode += getMotifRejet().hashCode();
        }
        if (getIdDocument() != null) {
            _hashCode += getIdDocument().hashCode();
        }
        if (getType() != null) {
            _hashCode += getType().hashCode();
        }
        if (getCodeRnd() != null) {
            _hashCode += getCodeRnd().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DocumentSynchro.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://AE.cirtil.cer69.recouv/AE.xsd1", "DocumentSynchro"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idArchivage");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idArchivage"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("etat");
        elemField.setXmlName(new javax.xml.namespace.QName("", "etat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("motifRejet");
        elemField.setXmlName(new javax.xml.namespace.QName("", "motifRejet"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idDocument");
        elemField.setXmlName(new javax.xml.namespace.QName("", "idDocument"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("codeRnd");
        elemField.setXmlName(new javax.xml.namespace.QName("", "codeRnd"));
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
